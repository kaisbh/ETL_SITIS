package isped.sitis.etl.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;

import isped.sitis.etl.controller.jdbcConnection;

/** Assumes UTF-8 encoding. JDK 7+. */
public class SejourParser {

	// static jdbcConnection conn = new jdbcConnection();

	public static void main(String[] args) throws IOException, SQLException {
		jdbcConnection.jdbcload();
		jdbcConnection.jdbcConnect(jdbcConnection.host, "cancerETL", jdbcConnection.USER, jdbcConnection.PASS);
		SejourParser parser = new SejourParser("/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/UTF8/SejourUTF8.txt");
		parser.processLineByLine();
		log("Done.");

	}

	/**
	 * Constructor.
	 * 
	 * @param aFileName
	 *            full name of an existing, readable file.
	 */
	public SejourParser(String aFileName) {
		fFilePath = Paths.get(aFileName);
	}

	/** Template method that calls {@link #processLine(String)}. */
	public final void processLineByLine() throws IOException {
		Scanner scanner = new Scanner(fFilePath, ENCODING.name());
		scanner.nextLine();//jump entete
		while (scanner.hasNextLine()) {
			processLine(scanner.nextLine());
		}
	}

	public static String ConvertDate(String data) {
		// data =data.charAt(0). toString();
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

	/**
	 * Overridable method for processing lines in different ways.
	 * 
	 * <P>
	 * This simple default implementation expects simple name-value pairs, separated
	 * by an '=' sign. Examples of valid input: <tt>height = 167cm</tt>
	 * <tt>mass =  65kg</tt> <tt>disposition =  "grumpy"</tt>
	 * <tt>this is the name = this is the value</tt>
	 */

	protected void processLine(String aLine) {
		String lineWithColToPipe = aLine.replaceAll(":", "|").replaceAll("'", " ").replaceAll("\\^M", "");
		System.out.println(lineWithColToPipe);
		
			String[] attributs = lineWithColToPipe.split("\\|");
			String NumPatient = attributs[0];
			String Sexe = attributs[1];
			String DateNaissance = ConvertDate(attributs[2]);
			System.out.println(DateNaissance);
			String Prenom = attributs[3];
			String Nom = attributs[4];
			String edath = ConvertDate(attributs[5]);
			String sdath = ConvertDate(attributs[6]);
			String edat = ConvertDate(attributs[7]);
			String sdat = ConvertDate(attributs[8]);
			String DP = attributs[9];
			String DR = attributs[10].toString();
			String NumSejour = attributs[11];
			

			/*log(quote(type.trim()) + "," + quote(code.trim()) + "," + quote(lib.trim()) + "," + 
					quote(txt.trim()));*/
			System.out.println(DR);
			String query = "INSERT INTO `TAB_sejour` (`NumPatient`, `Sexe`, `DateNaissance`, `Prenom`,`Nom`,`edath`,`sdath`,`edat`,`sdat`,`DP`,`DR`,`NumSejour`) VALUES("
					+ NumPatient + "," + Sexe + ", " + "DATE '" +DateNaissance + "', '" + Prenom + "', '"
					+ Nom + "', " + "DATE '" +edath + "', " + "DATE '" +sdath + "', " + "DATE '" +edat + "', "
					+ "DATE '" +sdat + "', '" + DP + "', '" + DR + "', " + NumSejour + ");";
			System.out.println(query);
			// String query = "SELECT id, nom FROM tab_patient";

			try {
				jdbcConnection.execInsertQuery(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	

	// PRIVATE
	private final Path fFilePath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;

	private static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}

	private static String quote(String aText) {
		String QUOTE = "'";
		return QUOTE + aText + QUOTE;
	}
}