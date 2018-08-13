import java.security.PublicKey;

public class Doctor { 
	
	private String name; 
	private String login; 
	private String password; 
	private Address addr;
	private Wallet wallet; 
	
	public Doctor (String name, String login, String password, Address addr) { 
		
		this.name = name;  
		this.login = login; 
		this.password = password; 
		this.addr = addr;
		this.wallet = new Wallet();
	}    
	
	public void storeConsult (Patient patient, Consultation consult) {  
		
		if (Main.hasPayed == false) { 
			
			System.out.println("The patient has not made the payment."); 
		} 
		else { 
			
			String previousHash = Main.chainData.blockchain.get(Main.chainData.blockchain.size() - 1).hash; 
			Block newBlock = new Block(previousHash); 
			newBlock.addTransaction(this.wallet.sendData(patient.getWallet().publicKey, consult));
			Main.chainData.addBlock(newBlock); 
			
			Main.hasPayed = false;
			
		}
		
	}
	
	public String getName () { 
		
		return this.name;
	} 
	
	public Wallet getWallet () { 
		
		return this.wallet;
	}

}
