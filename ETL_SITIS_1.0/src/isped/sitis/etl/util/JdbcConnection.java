package isped.sitis.etl.util;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class JdbcConnection {
	// JDBC driver name and database URL
		public  String JDBC_DRIVER ;
		public  String host = "localhost:8889";
		public  String dbName = "cancerETL";
		public  Connection conn = null;
		
		// public static ResultSet rs;
		// Database credentials
		public  String USER ; //rendu public pour acces depuis un autre package
		public  String PASS ;

		public JdbcConnection(String dbName,String JDBC_DRIVER,String host,String USER,String PASS) {
			this.dbName=dbName;
			this.JDBC_DRIVER=JDBC_DRIVER;
			this.host=host;
			this.USER=USER;
			this.PASS=PASS;
		}

		public void jdbcload() {
			try {
				// The newInstance() call is a work around for some
				// broken Java implementations

				Class.forName(JDBC_DRIVER).newInstance();
			} catch (Exception ex) {
				// handle the error
			}
		}
		public void jdbcClose(){
			if(conn!= null) {
				try {
					conn.close();
					System.out.println("Connection closed");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public boolean jdbcConnect() {
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
		public Connection getConnection() {
			return this.conn;
		}
	public static void main(String[] args) {
	}
	
	private static void setStatementParameters(PreparedStatement statement, ArrayList<?> parameters){
		/*
		 * The input is a preparedStatement waiting for its '?' in the statement string to be 
		 * replaced by parameters
		 * It iterates over input parameters, checks their type for use of proper setXX sql function.
		 */
		int paramCounter = 0; // Used to keep track of which parameter to set
		for (Object object : parameters){		// current Element declared as object
			paramCounter++;
			if (object instanceof Integer) {	// checking current object element type
				try {
					statement.setInt(paramCounter, (Integer) object); //set parameter using 
				} catch (SQLException e) {								//corresponding type
					System.out.println(e);
					e.printStackTrace();
				}
			    }else if (object instanceof String){
			    	try {
						statement.setString(paramCounter, (String) object);
					} catch (SQLException e) {
						System.out.println(e);
						e.printStackTrace();
					}
			    }else if (object instanceof Date){
			    	try {
						statement.setDate(paramCounter, (Date) object);
					} catch (SQLException e) {
						System.out.println(e);
						e.printStackTrace();
					}
			    }else if (object instanceof Double){
			    	try {
						statement.setDouble(paramCounter, (Double) object);
					} catch (SQLException e) {
						System.out.println(e);
						e.printStackTrace();
					}
			    }else if (object instanceof Boolean){
			    	try {
						statement.setBoolean(paramCounter, (Boolean) object);
					} catch (SQLException e) {
						System.out.println(e);
						e.printStackTrace();
					}
			    }else if (object instanceof Float){
			    	try {
						statement.setFloat(paramCounter, (Float) object);
					} catch (SQLException e) {
						System.out.println(e);
						e.printStackTrace();
					}
			    }
			}
	}
	
	public ArrayList<String> requeteEnBase(String statementString, ArrayList<?> parameters, Hashtable<Integer, String> typeForColumnIndex){
		PreparedStatement statement = null;
		ResultSet resultset = null;
		ArrayList<String> rows = new ArrayList<String>();
		
		try{
			
			statement = conn.prepareStatement(statementString);
			
			if(parameters!=null){
				setStatementParameters(statement, parameters);
				resultset = statement.executeQuery();
			}else{
				resultset = statement.executeQuery();
			}
			//System.out.println(statement);
			
			
		    
			while(resultset.next()){	
							//Iteration on rows
				String currentRow = new String();	// Declare a string, to fill with columns values
				
				//Set<Integer> columnsIndex = typeForColumnIndex.keySet();	// Preparing iteration on table key/value : column index / type								
			    //Iterator<Integer> itr = columnsIndex.iterator();			//Obtaining iterator over set entries
			    
			    //Using columnIndex/type pair to use proper resultset getXXX (getType) function
				int nbColumns = typeForColumnIndex.size();
			    for(int columnsIndex =1; columnsIndex<=nbColumns;columnsIndex++){
			        
					if (typeForColumnIndex.get(columnsIndex).equals("String")) {	// checking current object element type
						try {
							currentRow = currentRow + resultset.getString(columnsIndex) + "\t";
						} catch (SQLException e) {
							currentRow = currentRow + "error" + "\t";
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Date") ){
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy/dd/MM/");
							currentRow = currentRow + formatter.format(resultset.getDate(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("int") || typeForColumnIndex.get(columnsIndex).equals("Integer") ){
						try {
							currentRow = currentRow + String.valueOf( resultset.getInt(columnsIndex) ) + "\t";
						} catch (SQLException e) {								
							currentRow = currentRow + "error" + "\t";
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Float") ){
						try {
							currentRow = currentRow + String.valueOf(resultset.getFloat(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Boolean") ){
						try {
							currentRow = currentRow + String.valueOf(resultset.getBoolean(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Double") ){
						try {
							currentRow = currentRow + String.valueOf(resultset.getDouble(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}
			    }
			    rows.add(currentRow);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur sql");
		}
		finally{
			if ( resultset != null ) {
		        try {
		            resultset.close();
		        } catch ( SQLException ignore ) {
		        }
		    }
		    if ( statement != null ) {
		        try {
		            statement.close();
		        } catch ( SQLException ignore ) {
		        }
		    }
		}
		return rows;

	}
	public ArrayList<String> requeteEnBase(String statementString, Hashtable<Integer, String> typeForColumnIndex){
		PreparedStatement statement = null;
		ResultSet resultset = null;
		ArrayList<String> rows = new ArrayList<String>();
		
		try{
			
			statement = conn.prepareStatement(statementString);
			resultset = statement.executeQuery();
			
			
		    
			while(resultset.next()){	
							//Iteration on rows
				String currentRow = new String();	// Declare a string, to fill with columns values
				
				//Set<Integer> columnsIndex = typeForColumnIndex.keySet();	// Preparing iteration on table key/value : column index / type								
			    //Iterator<Integer> itr = columnsIndex.iterator();			//Obtaining iterator over set entries
			    
			    //Using columnIndex/type pair to use proper resultset getXXX (getType) function
				int nbColumns = typeForColumnIndex.size();
			    for(int columnsIndex =1; columnsIndex<=nbColumns;columnsIndex++){
			        
					if (typeForColumnIndex.get(columnsIndex).equals("String")) {	// checking current object element type
						try {
							currentRow = currentRow + resultset.getString(columnsIndex) + "\t";
						} catch (SQLException e) {
							currentRow = currentRow + "error" + "\t";
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Date") ){
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy/dd/MM/");
							currentRow = currentRow + formatter.format(resultset.getDate(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("int") || typeForColumnIndex.get(columnsIndex).equals("Integer") ){
						try {
							currentRow = currentRow + String.valueOf( resultset.getInt(columnsIndex) ) + "\t";
						} catch (SQLException e) {								
							currentRow = currentRow + "error" + "\t";
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Float") ){
						try {
							currentRow = currentRow + String.valueOf(resultset.getFloat(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Boolean") ){
						try {
							currentRow = currentRow + String.valueOf(resultset.getBoolean(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}else if (typeForColumnIndex.get(columnsIndex).equals("Double") ){
						try {
							currentRow = currentRow + String.valueOf(resultset.getDouble(columnsIndex)) + "\t";
						} catch (SQLException e) {								
							System.out.println(e);
							e.printStackTrace();
						}
					}
			    }
			    rows.add(currentRow);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur sql");
		}
		finally{
			if ( resultset != null ) {
		        try {
		            resultset.close();
		        } catch ( SQLException ignore ) {
		        }
		    }
		    if ( statement != null ) {
		        try {
		            statement.close();
		        } catch ( SQLException ignore ) {
		        }
		    }
		}
		return rows;

	}
	
	public void ajoutEnBase(String statementString, ArrayList<?> parameters){
		PreparedStatement statement = null;
		int statut = 0;
		ArrayList<String> rows = new ArrayList<String>();
		
		try{
			statement = conn.prepareStatement(statementString);
			
			if(parameters!=null){
				setStatementParameters(statement, parameters);
				statut = statement.executeUpdate();
			}else{
				statut = statement.executeUpdate();
			}
			System.out.println("Lignes affact�es par l'insertion: " + statut);
			
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur sql");
		}
		finally{
			
		    if ( statement != null ) {
		        try {
		            statement.close();
		        } catch ( SQLException ignore ) {
		        }
		    }
		}

	}
	

public int ajoutEnBaseAndKey(String statementString, ArrayList<?> parameters){
	PreparedStatement statement = null;
	int statut = 0;
	ArrayList<String> rows = new ArrayList<String>();
	int generatedKey=(Integer) null;
	
	
	try{
		statement = conn.prepareStatement(statementString);
		
		if(parameters!=null){
			setStatementParameters(statement, parameters);
			statut = statement.executeUpdate();
		}else{
			statut = statement.executeUpdate();
		}
		System.out.println("Lignes affact�es par l'insertion: " + statut);
		try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedKey= (int) generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
	}catch (SQLException e) {
		e.printStackTrace();
		System.out.println("erreur sql");
	}
	finally{
		
	    if ( statement != null ) {
	        try {
	            statement.close();
	            
	        } catch ( SQLException ignore ) {
	        }
	    }
	}
	return generatedKey;
	}
}


