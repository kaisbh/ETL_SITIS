package isped.sitis.etl.owlApi;

import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class TOPO {
	
	
	private static String baseIRIStr="http://www.semanticweb.org/owlapi/ontologieRegistre#";
	private static IRI baseIRI=IRI.create(baseIRIStr);
	private static PrefixManager pm = new DefaultPrefixManager(null, null, baseIRIStr);
	static jdbcConnection req = new jdbcConnection(); 
	
	static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	 static OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
	 static Pere pere =new Pere(null, null);
	 static Set<Pere> peres=new HashSet<Pere>();
	 static Set<OWLClassExpression> arguments=new HashSet<OWLClassExpression>();
	 static Set<label> labelstopo=new HashSet<label>();
	 static label labeltopo= new label(null, null);
	 	 
	 
	 public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		public static String host = "127.0.0.1";
		public static String dbName = "registre";
		public static java.sql.Connection conn = null;
		public static Statement stmt = null;
		static final String USER = "root";
		static final String PASS = "";
	
	
	
	public static void ajoutClasseTopo(OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre)
	
			throws OWLOntologyCreationException {
	
	
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

			
			try { 
				String sql = "SELECT CIM03 , LIBTOPOCIM03  FROM CIM03TOPO";
				ResultSet rs =null;
				System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
				
			
				while (rs.next()) {
					
					
					String CIM03 = rs.getString("CIM03 " );
					String libelleCIM03 = rs.getString("LIBTOPOCIM03");
					
					
					
					 labeltopo= new label(CIM03,libelleCIM03);
					 labelstopo.add(labeltopo)
					;
						
					 
					 
					 
						 for (label l:labelstopo) {
								OWLClass addclass  =dataFactory.getOWLClass(":"+l.getclasse(), pm);
								OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
								manager.addAxiom(ontologieRegistre, declarationAxiom);
						
								 OWLAnnotation commentAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(
								            l.getlabel()));
								      
								        OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLClass(":"+l.getclasse(), pm).getIRI(), commentAnno);
								     
								        manager.addAxiom(ontologieRegistre, ax);
								        // add a version info annotation to 
						 }						
				}
				}
					
			catch(SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
	}

	
	
	/*
	
	public static void ajoutClasseMorphoIACR (OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre)
	
			throws OWLOntologyCreationException {
	
	
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

			
			try { 
				String sql = "SELECT CODMORPHOCIMO3 , LIBMORPHOCIMO3 , MORPHO_IACR , GROUPE_MORPHO_IACR FROM cimo3morpho";
				ResultSet rs =null;
				System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
				
			
				while (rs.next()) {
					int codemorphoO3 = rs.getInt("CODMORPHOCIMO3" );
					int regionmorphoIACR = rs.getInt("GROUPE_MORPHO_IACR");
					
					String codmorpho03 = Integer.toString(codemorphoO3 );
					String regionmorphIACR = Integer.toString(regionmorphoIACR);
					
					String libellemorphoO3 = rs.getString("LIBMORPHOCIMO3" );
					String libellemorphoIACR = rs.getString("MORPHO_IACR");
					
					
					
					 label= new label(regionmorphIACR,libellemorphoIACR);
					 labels.add(label)
					;
						
						 for (label l:labels) {
								OWLClass addclass  =dataFactory.getOWLClass(":"+l.getclasse(), pm);
								OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
								manager.addAxiom(ontologieRegistre, declarationAxiom);
						
								 OWLAnnotation commentAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(
								            l.getlabel()));
								      
								        OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLClass(":"+l.getclasse(), pm).getIRI(), commentAnno);
								     
								        manager.addAxiom(ontologieRegistre, ax);
								        // add a version info annotation to 
						 }						
				}
				}
					
			catch(SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		
	}
	
	
	
	
	
	
	
	
	
	
	public static void addPereFils () {
	   
		
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
		}}
			
	catch(SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	
	}
	
	
	
	*/
	
	
	
	
	
	
	

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		 OWLOntology ontologieRegistre = manager.createOntology(baseIRI); 
		
		ajoutClasseTopo(manager, dataFactory, ontologieRegistre);
		
		
		
		
		File enregistrement = new File("C:/Users/hadri/Desktop/onto_registre/ONTO_REGISTRE/OR.owl");
		manager.saveOntology(ontologieRegistre,IRI.create(enregistrement.toURI()));
		
		
		
		
		
		
		

	}

}
