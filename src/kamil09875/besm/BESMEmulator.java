package kamil09875.besm;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class BESMEmulator{
	private final Config conf;
	private final BESMInput input;
	
	private Map<Integer, Integer> memory;
	private int cp;
	
	public BESMEmulator(final Config conf, final BESMInput input){
		this.conf = conf;
		this.input = input;
	}
	
	public void run() throws IOException{
		Set<Cell> cells = new HashSet<>();
		
		try{
			while(true) cells.add(input.read());
		}catch(NoSuchElementException e){
			// end
		}
		
		memory = cells.stream().sorted((c1, c2) -> {
			return c1.getAddress() - c2.getAddress();
		}).collect(Collectors.toMap(Cell::getAddress, Cell::getValue));
		
		cells = null;
		
		cp = 0;
		try{
			while(execute()){}
		}catch(BESMException e){
			crash(e.getMessage());
		}
	}
	
	@SuppressWarnings("static-method")
	private void crash(final String message){
		PrintStream err = System.err;
		err.println("== BDSM CRASH ==");
		err.println("Message: " + message);
	}
	
	private int get(final int address){
		if(memory.containsKey(address)){
			return memory.get(address);
		}else{
			memory.put(address, 0);
			return 0;
		}
	}
	
	private void set(final int address, final int value){
		memory.put(address, value);
	}
	
	private boolean execute() throws BESMException{
		int instr = get(cp);
		
		byte code = (byte)(instr >> 24);
		byte param1 = (byte)((instr >> 16) & 0xFF);
		byte param2 = (byte)((instr >> 8) & 0xFF);
		byte param3 = (byte)((instr >> 0) & 0xFF);
		
		switch(code){
			case 0:
				if(param1 == 0 && param2 == 0 && param3 == 0){
					cp = 0;
					return false;
				}
				
				// nop
				if(param1 == 0 && param2 == 0 && param3 == 1){
					break;
				}
				
				if(param1 == 1 && param2 == 0){
					System.out.println(get(param3));
					break;
				}
				
				//$FALL-THROUGH$
			case 1:
				set(param3, get(param1) + get(param2));
				break;
				
			case 2:
				set(param3, get(param1) - get(param2));
				break;
				
			case 3:
				set(param3, get(param1) * get(param2));
				break;
				
			case 4:
				set(param3, get(param1) / get(param2));
				break;
				
			case 5:
				switch(param1){
					case 0:
						if(param2 == 0){
							cp = param3;
						}else{
							throw new BESMUnknownInstructionException(cp, instr);
						}
						break;
						
					case 1:
						if(get(param2) > 0){
							cp = param3;
						}
						break;
						
					case 2:
						if(get(param2) < 0){
							cp = param3;
						}
						break;
						
					case 3:
						if(get(param2) == 0){
							cp = param3;
						}
						break;
						
					default:
						throw new BESMUnknownInstructionException(cp, instr);
				}
				break;
				
			default:
				throw new BESMUnknownInstructionException(cp, instr);
		}
		
		++cp;
		return true;
	}
}
