package isped.sitis.etl.identitoVigilance;

import java.util.StringJoiner;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

public abstract class Record {
	private  String nom;
	private  String prenom;
	private  String sexe;
	private  String ddn;
	
	public String getTraits(){
		StringJoiner sj = new StringJoiner("\t");
        sj.add(prenom);
        sj.add(nom);
        sj.add(sexe);
        sj.add(ddn);
        return sj.toString();
	}
	
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getDdn() {
		return ddn;
	}

	public void setDdn(String ddn) {
		this.ddn = ddn;
	}

	
}

