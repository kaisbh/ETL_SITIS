package isped.sitis.etl.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;

/** Assumes UTF-8 encoding. JDK 7+. */
public class FileParser {

	// static jdbcConnection conn = new jdbcConnection();

	public static void main(String[] args) throws IOException, SQLException {
		jdbcConnection.jdbcload();
		jdbcConnection.jdbcConnect(jdbcConnection.host, "chc", jdbcConnection.USER, jdbcConnection.PASS);
		FileParser parser = new FileParser("D:\\Anapath.txt");
		parser.processLineByLine();
		log("Done.");

	}

	/**
	 * Constructor.
	 * 
	 * @param aFileName
	 *            full name of an existing, readable file.
	 */
	public FileParser(String aFileName) {
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
		// use a second Scanner to parse the content of each line
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(",");
		if (scanner.hasNext()) {
			// assumes the line has a certain structure
			String val1 = scanner.next();
			String val2 = scanner.next();
			String val3 = ConvertDate(scanner.next());
			String val4 = scanner.next();
			String val5 = scanner.next();
			String val6 = ConvertDate(scanner.next());
			String str = scanner.next();

			String s[] = str.split(";");
			String val7 = s[0];
			int i;
			String val8 = "";
			for (i = 1; i < s.length; i++) {
				if (val8 == "")
					val8 = s[i];
				else
					val8 = val8 + ";" + s[i];
			}
			scanner.close();
			// String val8 = scanner.next();

			log(quote(val1.trim()) + "," + quote(val2.trim()) + "," + quote(val3.trim()) + "," + quote(val4.trim())
					+ "," + quote(val5.trim()) + "," + quote(val6.trim()) + "," + quote(val7.trim())
					+ quote(val8.trim()));
			String query = "INSERT INTO `anapath` (`NumPatient`, `Sexe`, `DateNaissance`, `Prenom`, `Nom`, `DATPREL`, `ADICAP1`, `ADICAP2`) VALUES('"
					+ val1.trim() + "','" + val2.trim() + "', '" + val3.trim() + "', '" + val4.trim() + "', '"
					+ val5.trim() + "','" + val6.trim() + "', '" + val7.trim() + "', '" + val8.trim() + "');";
			// String query = "SELECT id, nom FROM tab_patient";

			try {
				jdbcConnection.execInsertQuery(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			log("Empty or invalid line. Unable to process.");
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
