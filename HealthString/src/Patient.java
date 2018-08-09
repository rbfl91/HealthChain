import java.security.*;
import java.security.Security; 
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import com.google.gson.GsonBuilder;
import java.util.Map; 


public class Patient { 
	
	private String name;  
	private String login; 
	private String password; 
	private Address addr;
	private Wallet wallet; 
	
	public Patient (String name, String login, String password, Address addr) { 
		
		this.name = name;  
		this.login = login; 
		this.password = password; 
		this.addr = addr;
		this.wallet = new Wallet();
	}  
	
	
	
	public PublicKey makePayment (Doctor doctor, float value) {  
		
		PublicKey toReturn = null;
		
		if (this.wallet.getBalance() < value) { 
			
			Main.hasPayed = false;
			System.out.println("Not enough funds to make payment."); 
		} 
		else { 
			
			String previousHash = Main.chainPay.blockchain.get(Main.chainPay.blockchain.size() - 1).hash; 
			Block newBlock = new Block(previousHash); 
			System.out.println("\nPatient " + this.name + "'s balance is: " + this.wallet.getBalance());
			System.out.println("\nDoctor " + doctor.getName() + "'s balance is: " + doctor.getWallet().getBalance());
			newBlock.addTransaction(this.wallet.sendFunds(doctor.getWallet().publicKey, value));
			Main.chainPay.addBlock(newBlock);
			System.out.println("\nPatient " + this.name + "'s balance is: " + this.wallet.getBalance());
			System.out.println("\nDoctor " + doctor.getName() + "'s balance is: " + doctor.getWallet().getBalance()); 
			
			Main.hasPayed = true;
			toReturn = this.wallet.publicKey;
			
		}
		
		return toReturn;
	}
	
	public String getName () { 
		
		return this.name;
	} 
	
	public Wallet getWallet () { 
		
		return this.wallet;
	}

}
