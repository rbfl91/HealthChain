
public class Consultation { 
	
	private String consultationId;
	public String date; 
	public String type; 
	
	public Patient patient; 
	public Doctor doctor;
	
	Report report;
	
	public Consultation (String date, String type, Report report, Patient patient, Doctor doctor, String consultationId) { 
		
		this.date = date; 
		this.type = type; 
		this.report = report; 
		this.patient = patient; 
		this.doctor = doctor; 
		this.consultationId = consultationId; 
		
	}
	
	public String getId () { 
		
		return this.consultationId;
	}

}
