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
	
	// The first payment transaction 
	public static Transaction genesisTransaction;  
	
	// The data blockchain genesis transaction 
	public static Transaction genesisTransactionData; 
	
	// The consultation price
	public static float consultPrice = 80; 
	
	// Flag to check if the payment has been made 
	public static Boolean hasPayed;

	public static void main(String[] args) {
		// TODO Auto-generated method stub  
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
				
		Address addr1 = new Address ("Nottingham", "UK", "Mount Street", "NG1 6HE", "52.954783, -1.158109"); 
		Address addr2 = new Address ("Nottingham", "UK", "New Castle Road", "NG1 6HE", "52.954783, -1.158109");
		
		Patient patient1 = new Patient ("Pedro", "pedro1", "1234", addr1); 
		Doctor doctor1 = new Doctor ("Douglas", "douglas1", "4321", addr2);   
		
		Wallet coinbase = new Wallet(); // The system's "bank" wallet
		
		//create genesis transaction, which sends 100 coins to Pedro's wallet: 
		genesisTransaction = new Transaction(coinbase.publicKey, patient1.getWallet().publicKey, "100", null, true, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Creating and Mining Payment Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		chainPay.addBlock(genesis);   
		
		Consultation genesisConsult = new Consultation ("genesis", "genesis", null, patient1, doctor1, "Consultation Genesis");
		
		// Create Data genesis transaction
		genesisTransactionData = new Transaction(coinbase.publicKey, coinbase.publicKey, "0", genesisConsult, false, null);
		genesisTransactionData.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransactionData.transactionId = "0"; //manually set the transaction id

		System.out.println("Creating and Mining Data Genesis block... ");
		Block genesisData = new Block("0");
		genesisData.addTransaction(genesisTransactionData);
		chainData.addBlock(genesis); 
		
		
		PublicKey tempKey = patient1.makePayment(doctor1, consultPrice);  
		
		chainPay.getDataFrom(patient1.getWallet().publicKey, doctor1.getWallet().publicKey);
		chainPay.isChainValid();  
		
		Medication treat1 = new Medication ("ibuprofen", "every day, 200mg"); 
		ArrayList <Medication> tempTreats = new ArrayList <Medication> ();
		tempTreats.add(treat1);
		Report report1 = new Report (tempTreats, "Migraine");
		Consultation consult1 = new Consultation ("9/08/2018", "check-up", report1, patient1, doctor1, "Consultation #1");
		
		doctor1.storeConsult(patient1, consult1);
		
		chainData.getDataFrom(patient1.getWallet().publicKey, doctor1.getWallet().publicKey);
		chainData.isChainValid();   
		
				
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
