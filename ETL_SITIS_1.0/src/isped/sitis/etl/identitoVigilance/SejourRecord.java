package isped.sitis.etl.identitoVigilance;

import java.util.StringJoiner;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

public class SejourRecord extends Record{
	private  String nom;
	private  String prenom;
	private  String sexe;
	private  String ddn;
	private  String numAuto;
	private  String dp;
	private String dr = "";
	
	
	public SejourRecord(String dbRecord) {
		String[] traits = dbRecord.split("\t");
		this.sexe = traits[0];
		this.ddn = traits[1];
		this.prenom = traits[2];
		this.nom = traits[3];
		this.numAuto = traits[4];
		try{this.dp = traits[5];	} catch (ArrayIndexOutOfBoundsException e) {this.dp = "";}
	
		try{this.dr = traits[6];	} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
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
	
	public String getNumAuto() {
		return numAuto;
	}

	public void setNumAuto(String numAuto) {
		this.numAuto = numAuto;
	}
	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}
	public String getDr() {
		return dr;
	}
	public String setDr(String dr) {
		return this.dr = dr;
	}
}
