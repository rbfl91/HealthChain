import java.util.ArrayList;
import java.util.Date;

public class Data {
	
	public boolean isPayment;
	public float value; 
	public String medicalData; 
	public Consultation consult;
	
	
	
	//Data Constructor.  
	public Data(boolean isPayment, String data, Consultation consult) {
		
		this.isPayment = isPayment;
		
		if (isPayment) { 
			
			this.value = Float.parseFloat(data); 
			this.medicalData = null;
		} 
		else {  
			
			this.consult = consult;
			this.medicalData = consult.getId();
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
	
	public void printConsult () {
		
		System.out.println("Date = " + this.consult.date); 
		System.out.println("Date = " + this.consult.type); 
		System.out.println("Patient = " + this.consult.patient.getName()); 
		System.out.println("Doctor = " + this.consult.doctor.getName());
	}

}
