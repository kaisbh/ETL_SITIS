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
public class Cim10Parser {

	// static jdbcConnection conn = new jdbcConnection();

	public static void main(String[] args) throws IOException, SQLException {
		jdbcConnection.jdbcload();
		jdbcConnection.jdbcConnect(jdbcConnection.host, "cancerETL", jdbcConnection.USER, jdbcConnection.PASS);
		Cim10Parser parser = new Cim10Parser("/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/UTF8/cim10UTF8.txt");
		parser.processLineByLine();
		log("Done.");

	}

	/**
	 * Constructor.
	 * 
	 * @param aFileName
	 *            full name of an existing, readable file.
	 */
	public Cim10Parser(String aFileName) {
		fFilePath = Paths.get(aFileName);
	}

	/** Template method that calls {@link #processLine(String)}. */
	public final void processLineByLine() throws IOException {
		Scanner scanner = new Scanner(fFilePath, ENCODING.name());

		while (scanner.hasNextLine()) {
			processLine(scanner.nextLine());
		}
	}

	public static String ConvertDate(String data) {
		// data =data.charAt(0). toString();
		String str1 = Character.toString(data.charAt(0)) + Character.toString(data.charAt(1));
		String str2 = Character.toString(data.charAt(2)) + Character.toString(data.charAt(3))
				+ Character.toString(data.charAt(4));
		String str3 = Character.toString(data.charAt(5)) + Character.toString(data.charAt(6))
				+ Character.toString(data.charAt(7)) + Character.toString(data.charAt(8));
		data = str3 + "-" + ConvertMois(str2) + "-" + str1;
		return data;

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
		String lineWithColToPipe = aLine.replace(":", "|").replaceAll("'", " ");
		System.out.println(lineWithColToPipe);
		
			String[] attributs = lineWithColToPipe.split("\\|");
			String type = attributs[1];
			String code = attributs[2];
			String lib = attributs[3];
			String txt = attributs[4];
			

			/*log(quote(type.trim()) + "," + quote(code.trim()) + "," + quote(lib.trim()) + "," + 
					quote(txt.trim()));*/
			String query = "INSERT INTO `TAB_CIM10` (`type`, `code`, `lib`, `txt`) VALUES('"
					+ type + "','" + code + "', '" + lib + "', '" + txt + "');";
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
