package isped.sitis.etl.controller;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class jdbcConnectionKais {
	// JDBC driver name and database URL
	public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static String host = "localhost:8889";
	public static String dbName = "cancerETL";

	public static Connection conn = null;
	public static Statement stmt = null;
	// public static ResultSet rs;
	// Database credentials
	public static final String USER = "root"; //rendu public pour acces depuis un autre package
	public static final String PASS = "root";

	public jdbcConnection(String dbName) {
		this.dbName=dbName;
	}

	public static void jdbcload() {
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations

			Class.forName(JDBC_DRIVER).newInstance();
		} catch (Exception ex) {
			// handle the error
		}
	}

	public static boolean jdbcConnect(String host, String dbName, String USER, String PASS) {
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
			return false;
		}
		return true;
	}

	public static ResultSet getResultSet(String sql) {
		ResultSet rs = null;
		try {
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		return rs;
	}
	public static void execInsertQuery(String sql) throws SQLException {
		
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate(sql);


	}
	public static void execQuery(String sql) throws SQLException {
		jdbcload();
		if (jdbcConnect(host, dbName, USER, PASS)) {
			ResultSet rs = null;
			rs = getResultSet(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int i = 1; i <= columnsNumber; i++) {
				System.out.print(rsmd.getColumnName(i) + "	");
			}
			System.out.println(System.getProperty("line.separator"));
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					String columnName = rsmd.getColumnName(i);
					int type = rsmd.getColumnType(i);
					if (type == Types.VARCHAR || type == Types.CHAR) {
						System.out.print(rs.getString(columnName));
						// String name= rs.getString(columnName);
					}
					if (type == Types.INTEGER) {
						System.out.print(rs.getInt(columnName));
					}
					if (type == Types.DATE) {
						System.out.print(rs.getDate(columnName));
					}
					System.out.print("	");
				}
				System.out.print(System.getProperty("line.separator"));
			}
		}

	}

	public static void affiche(String tab) {

	}

	public static void main(String[] args) throws SQLException {
		execQuery("SELECT * from tab_cim10");
	}
}
