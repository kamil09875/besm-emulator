package kamil09875.besm;

public class Cell{
	private final int addr;
	private final int value;
	
	public Cell(final int addr, final int value){
		this.addr = addr;
		this.value = value;
	}
	
	public int getAddress(){
		return addr;
	}
	
	public int getValue(){
		return value;
	}
}
