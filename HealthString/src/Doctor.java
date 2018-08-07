
public class Doctor { 
	
	private String name; 
	private Wallet wallet; 
	
	public Doctor (String name) { 
		
		this.name = name; 
		this.wallet = new Wallet();
	}   
	
	public String getName () { 
		
		return this.name;
	} 
	
	public Wallet getWallet () { 
		
		return this.wallet;
	}

}
