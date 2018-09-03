import java.io.File;
import java.security.*;
import java.security.Security; 
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import com.google.gson.GsonBuilder;
import java.util.Map; 
import java.sql.*;

public class Main {  
	
	public static HealthChainPay chainPay = new HealthChainPay(3); 
	public static HealthChainData chainData = new HealthChainData(3);
	
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
		
		// Data Base and Genesis Blocks Operations:
		
		ArrayList<Patient> patients = dbGetPatients(); 
		
		ArrayList<Doctor> doctors = dbGetDoctors(); 
		
		ArrayList<Consultation> consults = dbGetConsults(doctors, patients);  	
		
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
		HealthChainPay.UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
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
		
		storeConsultsOnBlockchain (consults); 
		
		// Testing payment and data transactions:
		
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

	} 
	
	public static ArrayList<Patient> dbGetPatients() { 
		
		ArrayList<Patient> patients_temp = new ArrayList<Patient>();
		
		
		   Connection c = null;
		   Statement stmt1 = null; 
		   Statement stmt2 = null;
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt1 = c.createStatement(); 
		      stmt2 = c.createStatement();
		      ResultSet rs_patient = stmt1.executeQuery( "SELECT * FROM mockaroo_patient;" ); 
		      ResultSet rs_user = stmt2.executeQuery( "SELECT * FROM mockaroo_user;" );
		      
		      while (rs_patient.next() && rs_user.next()){ 
		    	  
		         int id = rs_patient.getInt("id");
		         String  name = rs_patient.getString("name"); 
		     
		         String  city = rs_user.getString("city"); 
		         String  country = rs_user.getString("country"); 
		         String  street = rs_user.getString("street"); 
		         String  postCode = rs_user.getString("postcode"); 
		         String  coords = rs_user.getString("coodinates");  
		         
		         
		         
		         Address address_temp = new Address (city, country, street, postCode, coords); 
		         
		         patients_temp.add(new Patient (name, name, name + "123", address_temp));
		         
		      }
		      rs_patient.close(); 
		      rs_user.close();
		      stmt1.close(); 
		      stmt2.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   System.out.println("Operation done successfully");
		
			
		   return patients_temp; 
	} 
	
	public static ArrayList<Doctor> dbGetDoctors() { 
		
		ArrayList<Doctor> doctors_temp = new ArrayList<Doctor>();
		
		
		   Connection c = null;
		   Statement stmt1 = null; 
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt1 = c.createStatement(); 
		      ResultSet rs_doctor = stmt1.executeQuery( "SELECT * FROM mockaroo_doctor;" ); 
		      
		      while (rs_doctor.next()){ 
		    	  
		         int id = rs_doctor.getInt("id");
		         String  name = rs_doctor.getString("name"); 
		     
		         doctors_temp.add(new Doctor (name, name, name + "123", null));
		         
		      }
		      rs_doctor.close(); 
		      stmt1.close(); 
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   System.out.println("Operation done successfully");
		
			
		   return doctors_temp; 
	}  
	
	public static ArrayList<Consultation> dbGetConsults(ArrayList<Doctor> doctors, ArrayList<Patient> patients) { 
		
		ArrayList<Consultation> consults_temp = new ArrayList<Consultation>();
		
		
		   Connection c = null;
		   Statement stmt1 = null;  
		   Statement stmt2 = null;  
		   Statement stmt3 = null; 
		   try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:db.sqlite3");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt1 = c.createStatement(); 
		      ResultSet rs_consults = stmt1.executeQuery( "SELECT * FROM mockaroo_consultation;" );  
		      
		      stmt2 = c.createStatement(); 
		      ResultSet rs_meds = stmt2.executeQuery( "SELECT * FROM mockaroo_treatmeds;" );  
		      
		      stmt3 = c.createStatement(); 
		      ResultSet rs_reports = stmt3.executeQuery( "SELECT * FROM mockaroo_report;" ); 
		      
		      while (rs_meds.next() && rs_consults.next() && rs_reports.next()){ 
		    	 
		         String med_name = rs_meds.getString("med_name");  
		         String posology = rs_meds.getString("posology");
		     
		         Medication medication_temp = new Medication (med_name, posology);
		         ArrayList<Medication> meds_temp = new ArrayList<Medication>(); 
		         meds_temp.add(medication_temp);
		         
		         String diagnosis = rs_reports.getString("diagnosis"); 
		         
		         Report report_temp = new Report (meds_temp, diagnosis);  
		         
		         String consult_date = rs_consults.getString("date");  
		         String consult_type = rs_consults.getString("_type");
		         int patient_id = rs_consults.getInt("patient_id"); 
		         int doctor_id = rs_consults.getInt("doctor_id"); 
		         int consult_id = rs_consults.getInt("id");
		         
		         consults_temp.add(new Consultation(consult_date, consult_type, report_temp, patients.get(patient_id - 1), doctors.get(doctor_id - 1), Integer.toString(consult_id)));
		         
		      }
		      rs_meds.close();  
		      rs_reports.close(); 
		      rs_consults.close(); 
		      
		      stmt1.close();  
		      stmt2.close();  
		      stmt3.close(); 
		      
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		   System.out.println("Operation done successfully");
		
			
		   return consults_temp; 
	}
	
	public static void storeConsultsOnBlockchain (ArrayList<Consultation> consults) { 
		
		int count = 1;
		for (Consultation p : consults){  
			
			System.out.println("Storing consult: " + count);
			System.out.println(p.patient.getName()); 
			System.out.println(p.doctor.getName());    
			
			String previousHash = Main.chainData.blockchain.get(Main.chainData.blockchain.size() - 1).hash; 
			Block newBlock = new Block(previousHash); 
			newBlock.addTransaction(p.doctor.getWallet().sendData(p.patient.getWallet().publicKey, p));
			Main.chainData.addBlock(newBlock);
			
			count++; 
		} 
		
	}
	
}
