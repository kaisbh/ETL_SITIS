package isped.sitis.etl.util;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;


// ? Faire une liste des tables chacun etant une liste de string correspondant aux attributs
//pour ensuite iterer dessus dans les requetes
public class Gestion
{
	private final static Charset ENCODING = StandardCharsets.UTF_8;
    public static void  main ( String[] arg) throws IOException {
    		JdbcConnection jdbc = new JdbcConnection("cancerETL", "com.mysql.jdbc.Driver", "localhost:8889", "root", "root");
		jdbc.jdbcload();
		jdbc.jdbcConnect();
		AnapathProcess("/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/UTF8/AnapathUTF8.txt", true, jdbc);
		jdbc.jdbcClose();
    }
    
    public static void AnapathProcess(String filePath, boolean entete, JdbcConnection jdbc) throws IOException {
    	
    		//----Get max NumAuto---
    		String getmaxQuery = "SELECT max(NumAuto) FROM `TAB_Anapath`";
    		Hashtable<Integer, String> typeForcol = new Hashtable<>();
    		typeForcol.put(1, "Integer");
    		ArrayList<String> maxNumAutos = jdbc.requeteEnBase(getmaxQuery, typeForcol);
    		System.out.println(maxNumAutos.get(0));
    		int currentNumAuto = Integer.valueOf(maxNumAutos.get(0).trim());
    		currentNumAuto++;
    	
		Scanner scanner = new Scanner(Paths.get(filePath), ENCODING.name());
		if(entete) {scanner.nextLine();} //jump entete
		while (scanner.hasNextLine()) {
			String line = (scanner.nextLine());
		
    			String lineWithColToPipe = line.replaceAll("\\^M", "");
    			System.out.println(lineWithColToPipe);
			ArrayList<String> parameters = new ArrayList<String>();
			String[] attributs = lineWithColToPipe.split("\\|");
			parameters.add(attributs[0]);
			parameters.add(attributs[1]);//sexe
			parameters.add(ConvertDate(attributs[2])); //ddn
			parameters.add(attributs[3]);//prenom
			parameters.add(attributs[4]);//nom
			parameters.add(ConvertDate(attributs[5])); //date prel
			parameters.add(String.valueOf(currentNumAuto));
			
			ArrayList<String> Adicaps = new ArrayList<String>();
			
			for(String code : attributs[6].split(";")) {
				Adicaps.add(code);
			}
			for(String code : attributs[6].split(";")) {
				Adicaps.add(code);
			}
			

			String statementString = "INSERT INTO `TAB_Anapath` (`NumPatient`, `Sexe`, `DateNaissance`, `Prenom`,`Nom`,`DatePrel`,`NumAuto`) VALUES(?,?,?,?,?,?,?)";
			System.out.println(statementString);
			System.out.println(parameters);

			jdbc.ajoutEnBase(statementString, parameters);
			
			for (Iterator iterator = Adicaps.iterator(); iterator.hasNext();) {
				String codeAcidap = (String) iterator.next();
				String insertInCodeAnapath = "INSERT INTO `TAB_CODE_ANAPATH` (`NumAnapath`, `NumAdicap`) VALUES (?,?)";
				
				ArrayList<String> params = new ArrayList<String>();
				params.add(String.valueOf(currentNumAuto));
				params.add(codeAcidap);
				System.out.println(params);
				jdbc.ajoutEnBase(insertInCodeAnapath, params);
			}
			currentNumAuto++;
			
		}
			
		}

public static String ConvertDate(String data) {
	// data =data.charAt(0). toString();
	System.out.println("date: "+data);
	String convertedDate;
	String str1 = Character.toString(data.charAt(0)) + Character.toString(data.charAt(1));
	String str2 = Character.toString(data.charAt(2)) + Character.toString(data.charAt(3))
			+ Character.toString(data.charAt(4));
	String str3 = Character.toString(data.charAt(5)) + Character.toString(data.charAt(6))
			+ Character.toString(data.charAt(7)) + Character.toString(data.charAt(8));
	convertedDate = str3 + "-" + ConvertMois(str2) + "-" + str1;
	return convertedDate.trim();

}

public static String ConvertMois(String mois) {
	String monthString;

	switch (mois) {
	case "JAN":
		monthString = "01";
		break;

	case "FEB":
		monthString = "02";
		break;

	case "MAR":
		monthString = "03";
		break;

	case "APR":
		monthString = "04";
		break;

	case "MAY":
		monthString = "05";
		break;

	case "JUN":
		monthString = "06";
		break;

	case "JUL":
		monthString = "07";
		break;

	case "AUG":
		monthString = "08";
		break;

	case "SEP":
		monthString = "09";
		break;

	case "OCT":
		monthString = "10";
		break;

	case "NOV":
		monthString = "11";
		break;

	case "DEC":
		monthString = "12";
		break;

	default:
		monthString = "00";
		break;

	}

	return (monthString);
}
    
    
    public static String saisie_chaine(){
	try {
	    BufferedReader buff = new BufferedReader
		(new InputStreamReader(System.in));
	    String chaine=buff.readLine();
	    return chaine;
	}
	catch(IOException e) {
	    System.out.println(" impossible de lire cette chaine" +e);
	    return null;
	}
    }
    public static Date saisie_date(){
    	try {
    	    BufferedReader buff = new BufferedReader
    		(new InputStreamReader(System.in));
    	    String chaine=buff.readLine();
    	    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy/dd/MM");
    	    //formatter.parse(dateInString);
    	    Date dob = null;
    	    if(!chaine.equals("")){
    	    	dob = Date.valueOf(chaine);
    	    }
    	    return dob;
    	}
    	catch(IOException e) {
    	    System.out.println(" impossible de lire cette chaine" +e);
    	    return null;
    	}
        }
    public static int saisie_entier()
    {
    	
	try{
	    BufferedReader buff = new BufferedReader
		(new InputStreamReader(System.in));
	    String chaine=buff.readLine();
	    int num = 0;
	    if (!chaine.equals("")){
	    	 num = Integer.parseInt(chaine);
	    }
	   
	    return num;
	}
	catch(IOException e){return 0;}
    }

}
