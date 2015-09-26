package kamil09875.besm;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class BESMInput implements Closeable{
	private final boolean binary;
	private final FileInputStream stream;
	private final Scanner scanner;
	
	private int lastAddr = -1;
	private final Pattern addrPattern = Pattern.compile("(\\d+):");
	
	public BESMInput(final String filename, final Config conf) throws FileNotFoundException{
		this.binary = conf.binary();
		this.stream = new FileInputStream(filename);
		
		if(!binary){
			this.scanner = new Scanner(stream);
		}else{
			this.scanner = null;
		}
	}
	
	@Override
	public void close() throws IOException{
		if(stream != null) stream.close();
	}
	
	public Cell read() throws IOException{
		// 4 cells:
		//  - instruction code
		//  - param 1
		//  - param 2
		//  - param 3
		byte[] ret = new byte[4];
		int addr;
		
		if(binary){
			addr = lastAddr + 1;
			if(stream.read(ret) == -1){
				throw new NoSuchElementException();
			}
		}else{
			if(scanner.hasNextLine()){
				if(scanner.hasNext(addrPattern)){
					String next = scanner.next(addrPattern);
					addr = Integer.parseInt(next.substring(0, next.length() - 1));
				}else{
					addr = lastAddr + 1;
				}
				
				int f = scanner.nextInt();
				if(scanner.hasNextByte()){
					ret[0] = (byte)f;
					ret[1] = scanner.nextByte();
					ret[2] = scanner.nextByte();
					ret[3] = scanner.nextByte();
				}else{
					ret[0] = (byte)(f >> 24);
					ret[1] = (byte)((f >> 16) & 0xFF);
					ret[2] = (byte)((f >> 8) & 0xFF);
					ret[3] = (byte)(f & 0xFF);
				}
				
				scanner.nextLine();
			}else{
				throw new NoSuchElementException();
			}
		}
		
		lastAddr = addr;
		return new Cell(addr, (ret[3] << 0) + (ret[2] << 8) + (ret[1] << 16) + (ret[0] << 24));
	}
}
