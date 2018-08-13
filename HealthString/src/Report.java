import java.util.ArrayList;

public class Report { 
	
	public ArrayList<Medication> medications; 
	
	public String diagnosis;
	
	public Report (ArrayList<Medication> meds, String diagnosis) { 
		
		this.medications = meds; 
		this.diagnosis = diagnosis;
	}

}
