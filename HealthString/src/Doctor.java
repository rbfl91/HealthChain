import java.security.PublicKey;

public class Doctor extends User { 
	
	
	
	public Doctor (String name, String login, String password, Address addr) { 
		
		super(name, login, password, addr);
	
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

}
