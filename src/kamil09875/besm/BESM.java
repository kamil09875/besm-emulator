package kamil09875.besm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

class Config{
	private final boolean debug;
	private final boolean extendedDebug;
	private final boolean binary;
	
	public Config(final boolean debug, final boolean extendedDebug, final boolean binary){
		this.debug = debug;
		this.extendedDebug = extendedDebug;
		this.binary = binary;
	}
	
	public boolean debug(){
		return debug;
	}
	
	public boolean extendedDebug(){
		return extendedDebug;
	}
	
	public boolean binary(){
		return binary;
	}
}

public class BESM{
	public static void main(final String... args) throws FileNotFoundException, IOException{
		if(args.length == 0 || args[0].equals("-h")){
			printHelp();
			return;
		}
		
		boolean debug = false;
		boolean extendedDebug = false;
		boolean binary = false;
		
		String filename = args[0];
		
		if(args.length > 1){
			for(int i = 1; i < args.length; ++i){
				switch(args[i]){
					case "-e":
						extendedDebug = true;
						debug = true;
						break;
						
					case "-d":
						debug = true;
						break;
						
					case "-b":
						binary = true;
						break;
						
					default:
						System.err.println("Unknown parameter: " + args[i]);
						return;
				}
			}
		}
		
		Config conf = new Config(debug, extendedDebug, binary);
		
		try(BESMInput input = new BESMInput(filename, conf)){
			BESMEmulator emulator = new BESMEmulator(conf, input);
			emulator.run();
		}
	}
	
	private static void printHelp(){
		PrintStream out = System.out;
		
		out.println("BESM Extended Emulator created by Kamil Jarosz");
		out.println();
		out.println("Help:");
		out.println("    besm -h           - print help");
		out.println("    besm <filename>   - load file into memory and process it");
		out.println();
		out.println("Additional flags (placed at the end):");
		out.println("    -d    - output debug information");
		out.println("    -e    - output extended debug information");
		out.println("    -b    - binary mode");
		out.println();
	}
}
