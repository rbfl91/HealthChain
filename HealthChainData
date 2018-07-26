import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class HealthChainData { 
	
	public ArrayList<Block> blockchain = new ArrayList<Block>(); 
	
	public int difficulty = 3; 
	
	public HealthChainData () { 
		
		this.blockchain = new ArrayList<Block>(); 
		
	}
	
	public Boolean isChainValid()
    {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[this.difficulty]).replace('\0', '0');

		//Checks the genesis block:
		currentBlock = blockchain.get(0);
        if (!blockchain.get(0).hash.equals(currentBlock.calculateHash()))
        {
        	System.out.println("Current Hashes not equal");
            return false;
        } 

		if (!currentBlock.calculateHash().substring(0, this.difficulty).equals(hashTarget))
        {
			System.out.println("This block hasn't been mined");
            return false;
        }

		//loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++)
        {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            //compare registered hash and calculated hash:
            if (!currentBlock.calculateHash().equals(currentBlock.calculateHash()))
            {
            	System.out.println("Current Hashes not equal");
                return false;
            }

            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash))
            {
            	System.out.println("Previous Hashes not equal");
                return false;
            }

            //check if hash is solved
            if (!currentBlock.hash.substring(0, this.difficulty).equals(hashTarget))
            {
            	System.out.println("This block hasn't been mined");
                return false;
            }
        }

        System.out.println("DataChain is valid.");
        return true;
    } 
	
	public void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	} 
	
	public void getDataFrom(PublicKey patient, PublicKey doctor) { 
		
		int i = 0;
		
		for (Block block : blockchain) { 
			
			System.out.println("block " + i);
			
			for (Transaction transaction : block.transactions) { 
				
				System.out.println(transaction.data.getData());
				
				if (transaction.reciepient.equals(doctor) && transaction.sender.equals(patient) && !transaction.data.isPayment) { 
					
					System.out.println(transaction.data.getData());
				}
			} 
			i++;
		}
		
	}
	
}


