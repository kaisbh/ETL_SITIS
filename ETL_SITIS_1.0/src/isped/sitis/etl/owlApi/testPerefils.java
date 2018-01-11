package isped.sitis.etl.owlApi;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.mysql.jdbc.Connection;

public class testPerefils {
	
	public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static String host = "127.0.0.1";
	public static String dbName = "registre";
	public static java.sql.Connection conn = null;
	public static Statement stmt = null;
	static final String USER = "root";
	static final String PASS = "";

	 static Pere pere =new Pere(null, null);
	 static Set<Pere> peres=new HashSet<Pere>();
	

	public static void main(String[] args) {
		
		try {
			String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Do something with the Connection

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			}
		
		// TODO Auto-generated method stub

	
	
	
	
	
	try { 
		String sql = "SELECT CODMORPHOCIMO3 , GROUPE_MORPHO_IACR FROM cimo3morpho";
		ResultSet rs =null;
		System.out.println("Creating statement...");
	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	rs = stmt.executeQuery(sql);
		
	System.out.println("Cette requête renvoie : " + rs.getRow() + " résultat(s).");
;
	
	
	
		while (rs.next()) {
			int codemorphoO3 = rs.getInt("CODMORPHOCIMO3" );
			int regionmorphoIACR = rs.getInt("GROUPE_MORPHO_IACR");
			
			String codmorpho03 = Integer.toString(codemorphoO3 );
			String regionmorphIACR = Integer.toString(regionmorphoIACR);
			
			;
			 pere=new Pere(regionmorphIACR,codmorpho03);
			 peres.add(pere);
			 
			
 

			
			 
		}

		for(Pere p:peres) {
			System.out.println(p.getPere() + p.getFils() );	 
		 }
		}
			catch(SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	

	

	
	
	
	
	
	
	}
	
}

