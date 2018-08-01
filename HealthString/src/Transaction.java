import java.security.*;
import java.util.ArrayList;

public class Transaction {
	
	public String transactionId; //Contains a hash of transaction*
	public PublicKey sender; //Senders address/public key.
	public PublicKey reciepient; //Recipients address/public key.
	public float value; //Contains the amount we wish to send to the recipient. 
	// HERE
	public Data data; //The medical data sent in the transaction.
	public byte[] signature; //This is to prevent anybody else from spending funds in our wallet.
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; //A rough count of how many transactions have been generated 
	
	// HERE
	// Constructor: 
	public Transaction(PublicKey from, PublicKey to, String data, boolean isPayment,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.inputs = inputs; 
		this.data = new Data (isPayment, data);  
		
		if (this.data.isPayment) { 
			
			System.out.println("It's a payment transaction!");
			this.value = Float.parseFloat(this.data.getData()); 
			System.out.println("The transaction value is: " + this.value);
		} 
		else 
		{  
			System.out.println("It's a data transaction!");
			this.value = 0;
		}
		
	}
	
	public boolean processTransaction() {
		
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
				
		//Gathers transaction inputs (Making sure they are unspent):
		// HERE STATIC
		for(TransactionInput i : inputs) {
			i.UTXO = Main.UTXOs.get(i.transactionOutputId);
		}

		//Checks if transaction is valid:
		if(getInputsValue() < HealthChainPay.minimumTransaction) {
			System.out.println("Transaction Inputs too small: " + getInputsValue());
			System.out.println("Please enter the amount greater than " + HealthChainPay.minimumTransaction);
			return false;
		}
		
		//Generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
		//Add outputs to Unspent list 
		//HERE STATIC
		for(TransactionOutput o : outputs) {
			Main.UTXOs.put(o.id , o);
		}
		
		//Remove transaction inputs from UTXO lists as spent: 
		// HERE STATIC
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			Main.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it, This behaviour may not be optimal.
			total += i.UTXO.value;
		}
		return total;
	}
	
	// HERE
	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value) + this.data.getData();
		signature = StringUtil.applyECDSASig(privateKey,data);		
	}
	
	// HERE
	public boolean verifySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value) + this.data.getData();
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
	
	// HERE
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + this.data.getData() + sequence
				);
	}
}