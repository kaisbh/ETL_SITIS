package isped.sitis.etl.owlApi;



public class label {
	private String classe;
	private String label;
	
	public label (String classe, String label) {
		setclasse(classe);
		setlabel(label);
		}
	
	
	public String getclasse() {
		return classe;
	}
	public void setlabel(String label) {
		this.label = label;
	}
	public String getlabel() {
		return label;
	}
	public void setclasse(String classe) {
		this.classe= classe;
	}
	

}
