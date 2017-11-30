package isped.sitis.etl.owlApi;

import java.util.Set;


// CLasse servant à simplifier l'implémentation de la méthode d'ajout de classe et relation pere\fils


public class Pere {
	private String pere;
	private String fils;
	
	
	public Pere (String pere,String fils) {
		setPere(pere);
		setFils(fils);
	}
	public Pere(Object pere2, Object fils2) {
		// TODO Auto-generated constructor stub
	}
	public String getPere() {
		return pere;
	}
	public void setPere(String pere) {
		this.pere = pere;
	}
	public String getFils() {
		return fils;
	}
	public void setFils(String fils) {
		this.fils = fils;
	}
	

}
