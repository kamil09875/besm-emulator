package kamil09875.besm;

public class BESMUnknownInstructionException extends BESMException{
	private static final long serialVersionUID = 2303610800974982748L;
	
	//private final int addr;
	//private final int instr;
	
	public BESMUnknownInstructionException(final int addr, final int instr){
		super("unknown instruction 0x" + Integer.toHexString(instr) + " at 0x" + Integer.toHexString(addr));
		
		//this.addr = addr;
		//this.instr = instr;
	}
}
