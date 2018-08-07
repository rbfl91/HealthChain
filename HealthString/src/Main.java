import java.security.*;
import java.security.Security; 
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import com.google.gson.GsonBuilder;
import java.util.Map; 

public class Main {  
	
	// Unique list of UTXOs that will be used for the payment blockchain
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); 
	
	public static HealthChainPay chainPay = new HealthChainPay(); 
	public static HealthChainData chainData = new HealthChainData();
	
	// Wallets (used for both blockchains)
	public static Wallet walletA;
	public static Wallet walletB; 
	
	// The first payment transaction 
	public static Transaction genesisTransaction;

	public static void main(String[] args) {
		// TODO Auto-generated method stub  
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
				
		Patient patient1 = new Patient ("Pedro"); 
		Doctor doctor1 = new Doctor ("Douglas");  
		Doctor doctor2 = new Doctor ("Julio");
		
		Wallet coinbase = new Wallet();
		
		//create genesis transaction, which sends 100 coins to Pedro's wallet: 
		genesisTransaction = new Transaction(coinbase.publicKey, patient1.getWallet().publicKey, "100", true, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		chainPay.addBlock(genesis); 
		
		patient1.makePayment(doctor1, 50); 
		patient1.makePayment(doctor2, 40);
		
		
		chainPay.getDataFrom(patient1.getWallet().publicKey, doctor1.getWallet().publicKey);
		chainPay.isChainValid(); 
		
		/*
		chainData.getDataFrom(walletB.publicKey, walletA.publicKey);
		chainData.isChainValid();   
		*/
				
		/*
				
				//Create wallets:
				walletA = new Wallet();
				walletB = new Wallet();		
				Wallet coinbase = new Wallet();
				
				//create genesis transaction, which sends 100 NoobCoin to walletA: 
				genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, "100", true, null);
				genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
				genesisTransaction.transactionId = "0"; //manually set the transaction id
				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
				
				System.out.println("Creating and Mining Genesis block... ");
				Block genesis = new Block("0");
				genesis.addTransaction(genesisTransaction);
				chainPay.addBlock(genesis);
				
				//testing
				Block block1 = new Block(genesis.hash);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
				block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
				chainPay.addBlock(block1);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("WalletB's balance is: " + walletB.getBalance());
				
				Block block2 = new Block(block1.hash);
				System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
				block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
				chainPay.addBlock(block2);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("WalletB's balance is: " + walletB.getBalance());
				
				Block block3 = new Block(block2.hash);
				System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
				block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20)); 
				chainPay.addBlock(block3);
				System.out.println("\nWalletA's balance is: " + walletA.getBalance());
				System.out.println("WalletB's balance is: " + walletB.getBalance());  
				
				//DataChain genesis block
				Block block4 = new Block("0"); 
				System.out.println("\nWalletB is Attempting to send data to WalletA...");
				block4.addTransaction(walletB.sendData(walletA.publicKey, "Medical Data from Wallet B[1]")); 
				chainData.addBlock(block4);  
				
				Block block5 = new Block(block4.hash);
				System.out.println("\nWalletB is Attempting to send data to WalletA...");
				block5.addTransaction(walletB.sendData(walletA.publicKey, "Medical Data from Wallet B[2]")); 
				chainData.addBlock(block5);  
				
				*/
				

	}

}
