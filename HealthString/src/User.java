public class User { 
	
	private String name; 
	protected String login; 
	protected String password; 
	protected Address addr;
	private Wallet wallet; 
	
	public User (String name, String login, String password, Address addr) { 
		
		this.name = name;  
		this.login = login; 
		this.password = password; 
		this.addr = addr;
		this.wallet = new Wallet();
	}    
	
	public String getName () { 
		
		return this.name;
	} 
	
	public Wallet getWallet () { 
		
		return this.wallet;
	}

}