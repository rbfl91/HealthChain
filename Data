import java.util.ArrayList;
import java.util.Date;

public class Data {
	
	public boolean isPayment;
	public float value; 
	public String medicalData;
	
	//Data Constructor.  
	public Data(boolean isPayment, String data) {
		
		this.isPayment = isPayment;
		
		if (isPayment) { 
			
			this.value = Float.parseFloat(data); 
			this.medicalData = null;
		} 
		else { 
			
			this.medicalData = data;
		}
	
	}
	
	public String getData() {
		
		String toReturn;
		
		if (isPayment) { 
			
			toReturn = String.valueOf(this.value);
		} 
		else { 
			
			toReturn = this.medicalData;
		}
	
		return toReturn;
	}

}
