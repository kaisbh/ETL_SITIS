package isped.sitis.etl.owlApi;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import isped.sitis.etl.owlApi.Pere;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.HasApplyChange;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.PriorityCollection;
//import com.hp.hpl.jena.graph.impl.AdhocDatatype;



public class OntologieISN {

	
	private static String baseIRIStr="http://www.semanticweb.org/owlapi/ontologieRegistre#";
	private static IRI baseIRI=IRI.create(baseIRIStr);
	private static PrefixManager pm = new DefaultPrefixManager(null, null, baseIRIStr);
	static jdbcConnection req = new jdbcConnection(); 
	
	static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	 static OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
	 static Pere pere =new Pere(null, null);
	 static Set<Pere> peres=new HashSet<Pere>();
	 
	 
	 static Set<Pere> peresMorpho=new HashSet<Pere>();
	 static Pere peremorpho =new Pere(null, null);
	 
	 static Set<OWLClassExpression> arguments=new HashSet<OWLClassExpression>();
	 
	 static Set<label> labelsmorpho03=new HashSet<label>();
	 static label labelmorpho03= new label(null, null);
	 
	 static Set<label> labelsmorphoIACR=new HashSet<label>();
	 static label labelmorphoIACR= new label(null, null);
	 
	 static Set<label> labelstopo03=new HashSet<label>();
	 static label labeltopo03= new label(null, null);
	 
	 static Set<label> labelstopoIACR=new HashSet<label>();
	 static label labeltopoIACR= new label(null, null);
	 	 
	 static Set<Pere> perestopo=new HashSet<Pere>();
	 static Pere peretopo =new Pere(null, null);
	 
	 public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		public static String host = "127.0.0.1";
		public static String dbName = "registre";
		public static java.sql.Connection conn = null;
		public static Statement stmt = null;
		public static Statement stmt2 = null;
		static final String USER = "root";
		static final String PASS = "";
	
		

	 

	// AJOUT CLASSES MORPHO
		
	public static void ajoutClasseMorphoO3 (OWLOntologyManager manager,  
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
					
					String libellemorphoO3 = rs.getString("LIBMORPHOCIMO3" );
					String libellemorphoIACR = rs.getString("MORPHO_IACR");
					
					String codmorpho03 = Integer.toString(codemorphoO3 );
					String regionmorphIACR = Integer.toString(regionmorphoIACR);
					
					 labelmorpho03= new label(codmorpho03,libellemorphoO3);
					 labelsmorpho03.add(labelmorpho03)
					;
						 for (label m03:labelsmorpho03) {
						 		OWLClass addclass  =dataFactory.getOWLClass(":"+m03.getclasse(), pm);
								OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
								manager.addAxiom(ontologieRegistre, declarationAxiom);
						
								 OWLAnnotation commentAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(m03.getlabel()));
								        OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLClass(":"+m03.getclasse(), pm).getIRI(), commentAnno);
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
	
	// AJOUT CLASSE GROUPE MORPHO

	public static void ajoutClasseMorphoIACR (OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre)
			throws OWLOntologyCreationException {
	
					try {
					String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
					System.out.println("Connecting to database...");
					conn = DriverManager.getConnection(DB_URL, USER, PASS);
				} catch (SQLException ex) {
					// handle any errors
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
			try { 
				String sql = "SELECT MORPHO_IACR,GROUPE_MORPHO_IACR FROM cimo3morpho";
				ResultSet rs =null;
				System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			
				while (rs.next()) {
					
					int regionmorphoIACR = rs.getInt("GROUPE_MORPHO_IACR");
					String regionmorphIACR = Integer.toString(regionmorphoIACR);
					String libellemorphoIACR = rs.getString("MORPHO_IACR");
					
					 labelmorphoIACR= new label(regionmorphIACR,libellemorphoIACR);
					 labelsmorphoIACR.add(labelmorphoIACR)
					;
						 for (label mIACR:labelsmorphoIACR) {
								OWLClass addclass  =dataFactory.getOWLClass(":m"+mIACR.getclasse(), pm);
								OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
								manager.addAxiom(ontologieRegistre, declarationAxiom);
						
								 OWLAnnotation commentAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(
										 mIACR.getlabel()));
								        OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLClass(":m"+mIACR.getclasse(), pm).getIRI(), commentAnno);
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
	
	// AJOUT RELATION SUBSOMPTION ENTRE CLASSE GROUPE MOPRHO ET MORPHO
	
	public static void addPereFilsMorpho (OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre) {
		try {
			String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			}
		

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
			 peremorpho=new Pere(regionmorphIACR,codmorpho03);
			 peresMorpho.add(peremorpho);
			 
			 for(Pere pmorpho:peresMorpho) {
			 OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(
					 dataFactory.getOWLClass(":"+pmorpho.getFils(),pm),
					 dataFactory.getOWLClass(":m"+pmorpho.getPere(),pm)				 
					 );
			 
			AddAxiom addAxiom = new AddAxiom(ontologieRegistre, axiom);
			manager.applyChange(addAxiom);
			 }
		}}
			
	catch(SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
	
	
	//AJOUT CLASSES TOPO
	
	public static void ajoutClasseTopo(OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre)
	
			throws OWLOntologyCreationException {
				try {
					String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
					System.out.println("Connecting to database...");
					conn = DriverManager.getConnection(DB_URL, USER, PASS);
				} catch (SQLException ex) {
					// handle any errors
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
					}
			try { 
				String sql = "SELECT CODTOPOCIM03, LIBTOPOCIM03  FROM CIM03TOPO";
				ResultSet rs =null;
				System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
				
				while (rs.next()) {
					
					String CODTOPOCIM03  = rs.getString("CODTOPOCIM03" );
					String libelleCIM03 = rs.getString("LIBTOPOCIM03");
					
					 labeltopo03= new label(CODTOPOCIM03 ,libelleCIM03);
					 labelstopo03.add(labeltopo03);
						
					 	for (label lt:labelstopo03) {
								OWLClass addclass  =dataFactory.getOWLClass(":"+lt.getclasse(), pm);
								OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
								manager.addAxiom(ontologieRegistre, declarationAxiom);
						
								 OWLAnnotation commentAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(
								 lt.getlabel()));
								OWLAxiom axtopo = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLClass(":"+lt.getclasse(), pm).getIRI(), commentAnno);
								manager.addAxiom(ontologieRegistre, axtopo);
						 }						
				}
				}	
			catch(SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	// AJOUT CLASSES GROUPE TOPO
	
	public static void ajoutClasseTopoIACR (OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre)
			throws OWLOntologyCreationException {
	
					try {
					String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
					System.out.println("Connecting to database...");
					conn = DriverManager.getConnection(DB_URL, USER, PASS);

				} catch (SQLException ex) {
					// handle any errors
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
					}
			try { 
				String sql = "SELECT TOPO_IACR , GROUPE_TOPO_IACR  FROM cim03topo";
				ResultSet rs =null;
				System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
				
				while (rs.next()) {
					int GROUPE_TOPO_IACR = rs.getInt("GROUPE_TOPO_IACR");
					String GROUP_TOPO_IACR = Integer.toString(GROUPE_TOPO_IACR);
					
					String TOPO_IACR = rs.getString("TOPO_IACR");
					
					 labeltopoIACR= new label(GROUP_TOPO_IACR,TOPO_IACR);
					 labelstopoIACR.add(labeltopoIACR);
				
						 for (label lIACR:labelstopoIACR) {
								OWLClass addclass  =dataFactory.getOWLClass(":t"+lIACR.getclasse(), pm);
								OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
								manager.addAxiom(ontologieRegistre, declarationAxiom);
						
								 OWLAnnotation commentAnno = dataFactory.getOWLAnnotation(dataFactory.getRDFSLabel(), dataFactory.getOWLLiteral(
								            lIACR.getlabel())); 
								        OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLClass(":t"+lIACR.getclasse(), pm).getIRI(), commentAnno);
								        manager.addAxiom(ontologieRegistre, ax);
								        // add a version info annotation to 
						 }						
				}
				}catch(SQLException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
	}
	

	// AJOUT RELATION SUBSOMPTION CLASSES GROUPE TOPO / TOPO

	public static void addPereFilsTopo (OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre) {
	  
		try {
			String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			}
		

	try { 
		String sql = "SELECT CODTOPOCIM03 , GROUPE_TOPO_IACR FROM cim03topo";
		ResultSet rs =null;
		System.out.println("Creating statement...");
	stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	rs = stmt.executeQuery(sql);
	System.out.println("Cette requête renvoie : " + rs.getRow() + " résultat(s).");

		while (rs.next()) {
			String CODTOPOCIM03  = rs.getString("CODTOPOCIM03" );
			int GROUPE_TOPO_IACR = rs.getInt("GROUPE_TOPO_IACR");

			String GROUP_TOP_IACR = Integer.toString(GROUPE_TOPO_IACR);
			
			 peretopo=new Pere(GROUP_TOP_IACR,CODTOPOCIM03);
			 perestopo.add(peretopo);
			 
			 for(Pere ptopo:perestopo) {
				 OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(
						 dataFactory.getOWLClass(":"+ptopo.getFils(),pm),
						 dataFactory.getOWLClass(":t"+ptopo.getPere(),pm)				 
						 );
				 AddAxiom addAxiom = new AddAxiom(ontologieRegistre, axiom);
				manager.applyChange(addAxiom);
				 }			 
		}
			
	}catch(SQLException e1)
	{
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}
	
	// AJOUT RELATION SUBSOMPTION ENTRE 2 CLASSES / A MODIFIER
	
	public static void addPereFils(String P, String F,OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre) {
		 
		
		pere=new Pere(P,F);
		 peres.add(pere);
		 
		 for(Pere p:peres) {
			 OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(
					 dataFactory.getOWLClass(":"+p.getFils(),pm),
					 dataFactory.getOWLClass(":"+p.getPere(),pm)				 
					 );
			 AddAxiom addAxiom = new AddAxiom(ontologieRegistre, axiom);
			manager.applyChange(addAxiom);	 
		        }
	}
	
	
	// AJOUT RELATION SUBSOMPTION
	
	public static void ajouterAxiomSubClass (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, 
			String subClass, 
			String superclass ) throws OWLOntologyCreationException {
		 OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(
				 dataFactory.getOWLClass(":"+subClass,pm),
				 dataFactory.getOWLClass(":"+superclass,pm)				 
				 );
		AddAxiom addAxiom = new AddAxiom(ontologieRegistre, axiom);
		manager.applyChange(addAxiom);
	}
		
				// Simplification de la méthode
		
	// DEFINITION D'UNE PROPIETE
	
	
	public static void AddObjectProperties (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, String ObjectProperties, String Domaine, String CoDomaine ) 
	{
		OWLObjectProperty Objectproperties = dataFactory.getOWLObjectProperty(":"+ObjectProperties, pm);
		 OWLDeclarationAxiom declarationAxiom1 = dataFactory.getOWLDeclarationAxiom(Objectproperties);
		 manager.addAxiom(ontologieRegistre, declarationAxiom1 );
		 OWLObjectPropertyDomainAxiom domainAxiom = dataFactory.getOWLObjectPropertyDomainAxiom(Objectproperties, dataFactory.getOWLClass(":"+Domaine, pm));
	     OWLObjectPropertyRangeAxiom rangeAxiom = dataFactory.getOWLObjectPropertyRangeAxiom( Objectproperties,  dataFactory.getOWLClass(":"+CoDomaine, pm));
	       manager.addAxiom(ontologieRegistre, domainAxiom);
	       manager.addAxiom(ontologieRegistre,  rangeAxiom);
	}
	
	// DEFINTION D'UNE PROPRIETE AVEC RESTRICTION SOME 
	
	public static void AddLogiqueDescriptionSOME (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, String superclass1, String objectproperty, String subClass1){
			OWLClassExpression LogiqueDescription = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+objectproperty, pm),
			 dataFactory.getOWLClass(":"+subClass1,pm));
			OWLSubClassOfAxiom axiome = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLClass(":"+superclass1,pm),  LogiqueDescription);
			AddAxiom addAxiom1 = new AddAxiom(ontologieRegistre, axiome);
			manager.applyChange(addAxiom1);	
	}
	
	// CLASSE DEFINIE AVEC PROPRITETE ET RESTRCITION SOME
	
	public static void AddLogiqueDescriptionSOMEequivalence (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, String superclass2, String objectproperty, String subClass2){
			OWLClassExpression LogiqueDescription = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+objectproperty, pm),
			 dataFactory.getOWLClass(":"+subClass2,pm));
			OWLEquivalentClassesAxiom axiome = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+superclass2,pm),  LogiqueDescription);
			AddAxiom addAxiom1 = new AddAxiom(ontologieRegistre, axiome);
			manager.applyChange(addAxiom1);					
	}
	
	
	
	
	
	
	
	
	
	
	
	
	// CLASSE DEFINIE DIAGNOSTIC (avec l'ensemble des combinaisons possibles issues des tables)	
	
	
	
	public static void addClassDefDiagMal (OWLOntology ontologieRegistre, OWLDataFactory dataFactory, OWLOntologyManager manager) {
		
		try {
			String DB_URL = "jdbc:mysql://" + host + "/" + dbName;
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		try { 
			String sql = "SELECT DISTINCT `GROUPE_MORPHO_IACR`FROM `cimo3morpho` WHERE`GROUPE_MORPHO_IACR`"
					+ " NOT IN (SELECT `GROUPE_MORPHO_IACR`FROM `cimo3morpho`WHERE `GROUPE_MORPHO_IACR` = 8 OR `GROUPE_MORPHO_IACR` = 9 "
					+ "OR `GROUPE_MORPHO_IACR` = 10 OR `GROUPE_MORPHO_IACR` = 11 OR `GROUPE_MORPHO_IACR` = 12 "
					+ "OR `GROUPE_MORPHO_IACR` = 13 OR `GROUPE_MORPHO_IACR` = 14 OR `GROUPE_MORPHO_IACR` = 15 ) ";
			String sql2 = "SELECT DISTINCT `GROUPE_TOPO_IACR` FROM `cim03topo` ";
			String sql3 = "SELECT DISTINCT `GROUPE_MORPHO_IACR`FROM `cimo3morpho` WHERE `GROUPE_MORPHO_IACR` = 8 "
					+ "OR `GROUPE_MORPHO_IACR` = 9 OR `GROUPE_MORPHO_IACR` = 10 OR `GROUPE_MORPHO_IACR` = 11 OR `GROUPE_MORPHO_IACR` = 12"
					+ " OR `GROUPE_MORPHO_IACR` = 13 OR `GROUPE_MORPHO_IACR` = 14 OR `GROUPE_MORPHO_IACR` = 15  ";
			ResultSet rs =null;
			ResultSet rs2 =null;
			System.out.println("Creating statement...");
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs = stmt.executeQuery(sql);
		stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		Statement stmt3 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		// def diag hors hemopathie
			while (rs.next()) {
				
				
				
				int GROUPE_MORPHO_IACR= rs.getInt("GROUPE_MORPHO_IACR");
				String GROUPE_MORPHO_IACR1 = Integer.toString(GROUPE_MORPHO_IACR);
				System.out.println (GROUPE_MORPHO_IACR1);
				rs2 =stmt2.executeQuery(sql2);
				
				while (rs2.next()) {
					//System.out.println (rs2.getString("CIM03"));
					String GROUPE_TOPO_IACR  = rs2.getString("GROUPE_TOPO_IACR" );
					
					
					//System.out.println (LIBTOPOCIM03);
					String libelle = "Dm"+GROUPE_MORPHO_IACR1+"t"+GROUPE_TOPO_IACR;
					String libelle2 ="Mm"+GROUPE_MORPHO_IACR1+"t"+GROUPE_TOPO_IACR;
					//System.out.println (libelle);
					//addClassesdéfiniesD_Diag (manager, dataFactory, ontologieRegistre, libelle, "Cat_Diagnostic", "aPourDgMorpho",
					//		CODMORPHOCIM3, "aPourDgTopo", CIM03 );
					
					String aPourDgMorpho = "aPourDgMorpho";
					String aPourDgTopo = "aPourDgTopo";
				
				OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(
						dataFactory.getOWLObjectProperty(":"+aPourDgMorpho, pm),
						 dataFactory.getOWLClass(":m"+GROUPE_MORPHO_IACR1,pm));
				
				OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(
						dataFactory.getOWLObjectProperty(":"+aPourDgTopo, pm),
						 dataFactory.getOWLClass(":t"+GROUPE_TOPO_IACR ,pm));
				
				OWLClassExpression Description = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );
				OWLEquivalentClassesAxiom axiome111 = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+libelle, pm),Description);
				AddAxiom addAxiom111 = new AddAxiom(ontologieRegistre, axiome111);
				manager.applyChange(addAxiom111);
				
				
				
				
				
				OWLClassExpression avoirunemaladie = dataFactory.getOWLObjectSomeValuesFrom(
						dataFactory.getOWLObjectProperty(":maladieDe", pm),
						 dataFactory.getOWLClass(":Patient",pm));
				
				OWLClassExpression avoirundiag= dataFactory.getOWLObjectSomeValuesFrom(
						dataFactory.getOWLObjectProperty(":aPourDg", pm),
						dataFactory.getOWLClass(":"+libelle, pm));

				OWLClassExpression Description2 = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Maladie", pm),avoirunemaladie, avoirundiag );
				OWLEquivalentClassesAxiom axiome1111 = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+libelle2, pm),Description2);
				AddAxiom addAxiom1111 = new AddAxiom(ontologieRegistre, axiome1111);
				manager.applyChange(addAxiom1111);
				
				}	
				
			}
				
		}catch(SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	 
	// CLASSES DEFINIE DIAGNOSTIC AUTRE (useless)
	
	public static void addClassesdéfiniesD_Diag (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre,String classedéfinie, String Diagnostic, String aPourDgMorpho,
			String Morpho, String aPourDgTopo, String topo  )
	{
		aPourDgMorpho = "aPourDgMorpho";
		aPourDgTopo = "aPourDgTopo";
	
	OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+aPourDgMorpho, pm),
			 dataFactory.getOWLClass(":"+Morpho,pm));
	
	OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+aPourDgTopo, pm),
			 dataFactory.getOWLClass(":"+topo,pm));
	
	OWLClassExpression Description = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":"+Diagnostic, pm),avoirunemorpho,avoirunetopo );
	OWLEquivalentClassesAxiom axiome111 = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+classedéfinie, pm),Description);
	AddAxiom addAxiom111 = new AddAxiom(ontologieRegistre, axiome111);
	manager.applyChange(addAxiom111);
	}
	
	public static void addClassesdéfiniesMaladie (OWLOntologyManager manager,OWLDataFactory dataFactory,OWLOntology ontologieRegistre,
			String classedéfinie, String diagnostic)
	{
	OWLClassExpression avoirunemaladie = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":maladieDe", pm),
			 dataFactory.getOWLClass("Patient",pm));
	
	OWLClassExpression avoirundiag= dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":aPourDg", pm),
			 dataFactory.getOWLClass(":"+diagnostic,pm));

	OWLClassExpression Description2 = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Maladie", pm),avoirunemaladie, avoirundiag );
	OWLEquivalentClassesAxiom axiome1111 = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+classedéfinie, pm),Description2);
	AddAxiom addAxiom1111 = new AddAxiom(ontologieRegistre, axiome1111);
	manager.applyChange(addAxiom1111);
	}
	
	
	// CLASSE DEFINIE PATIENT (useless)
	public static void addClassesdéfiniesPatient (OWLOntologyManager manager,OWLDataFactory dataFactory,OWLOntology ontologieRegistre,
			String classedéfinie, String Patient, String aPourDg,String Diagnostic)
	{
	OWLClassExpression avoirunDiagnostic = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+aPourDg, pm),
			 dataFactory.getOWLClass(":"+Diagnostic,pm));

	OWLClassExpression Description2 = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":"+Patient, pm),avoirunDiagnostic );
	OWLEquivalentClassesAxiom axiome1111 = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+classedéfinie, pm),Description2);
	AddAxiom addAxiom1111 = new AddAxiom(ontologieRegistre, axiome1111);
	manager.applyChange(addAxiom1111);
	}
	
	
	
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, SQLException {
	  
		        
		        
		        
		 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		 OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		 OWLOntology ontologieRegistre = manager.createOntology(baseIRI); 
		 
		
		// CREATION CLASSE A LA MAIN
		 
		 Set<String> classes=new HashSet<String>();
		
		 classes.add("Diagnostic");
		 classes.add("Topo");
		 classes.add("Morpho");
		 classes.add("Patient");
		 for (String c:classes) {
				OWLClass addclass  =dataFactory.getOWLClass(c, pm);
				OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
				manager.addAxiom(ontologieRegistre, declarationAxiom);
		 }
		 
		 // ajout classe morpho 
		 
		       //ajoutClasseMorphoO3(manager, dataFactory, ontologieRegistre);
		       ajoutClasseMorphoIACR(manager, dataFactory, ontologieRegistre);
		       //addPereFilsMorpho(manager, dataFactory, ontologieRegistre);
		       
		       
		 // ajout classes topo
		       
		      // ajoutClasseTopo(manager, dataFactory, ontologieRegistre);
		       ajoutClasseTopoIACR(manager, dataFactory, ontologieRegistre);
		      //addPereFilsTopo(manager, dataFactory, ontologieRegistre);
		       
		       
		 // ajout classes définies diagnostic et maladie (hors hémopathie)
		     addClassDefDiagMal(ontologieRegistre, dataFactory, manager);
		 
		     // ajout classes définies diagnostic et maladie hémopathie
			
		     addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm14t","Diagnostic","aPourDgMorpho","m14","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm8t","Diagnostic","aPourDgMorpho","m8","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm9t","Diagnostic","aPourDgMorpho","m9","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm10t","Diagnostic","aPourDgMorpho","m10","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm11t","Diagnostic","aPourDgMorpho","m11","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm12t","Diagnostic","aPourDgMorpho","m12","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm13t","Diagnostic","aPourDgMorpho","m13","aPourDgTopo","Topo");
				addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Dm15t","Diagnostic","aPourDgMorpho","m15","aPourDgTopo","Topo");
			
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm14t","Dm14t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm8t","Dm8t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm9t","Dm9t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm10t","Dm10t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm11t","Dm11t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm12t","Dm12t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm13t","Dm13t");
				addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"Mm15t","Dm15t");
		 
		 
		    
		
		  
				// RELATION  DE SUBSOMPTION ENTRE GROUPE TOPO / faire une methode
		      
				 addPereFils("Topo","t52",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t1",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t2",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t3",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t4",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t5",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t6",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t7",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t8",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t9",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t10",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t11",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t12",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t13",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t14",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t15",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t16",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t17",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t18",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t19",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t20",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t21",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t22",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t23",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t24",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t25",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t26",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t27",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t28",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t29",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t30",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t31",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t32",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t33",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t34",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t35",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t36",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t37",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t38",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t39",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t40",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t41",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t42",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t43",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t44",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t45",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t46",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t47",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t48",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t49",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t50",manager, dataFactory, ontologieRegistre);
				 addPereFils("t52","t51",manager, dataFactory, ontologieRegistre);
				
		 
				 
				 
				 // RELATION SUBSOMPTION ENTRE GROUPE MORPHO 
				 
		 addPereFils("Morpho","m17",manager, dataFactory, ontologieRegistre);
		 addPereFils("m17","m5",manager, dataFactory, ontologieRegistre);
		 addPereFils("m17","m14",manager, dataFactory, ontologieRegistre);
		 addPereFils("m17","m15",manager, dataFactory, ontologieRegistre);
		 addPereFils("m17","m16",manager, dataFactory, ontologieRegistre);		 
		 
		 addPereFils("m5","m1",manager, dataFactory, ontologieRegistre);
		 addPereFils("m5","m2",manager, dataFactory, ontologieRegistre);
		 addPereFils("m5","m3",manager, dataFactory, ontologieRegistre);
		 addPereFils("m5","m4",manager, dataFactory, ontologieRegistre);
		 addPereFils("m5","m6",manager, dataFactory, ontologieRegistre);
		 addPereFils("m5","m7",manager, dataFactory, ontologieRegistre);
		 
		 addPereFils("m14","m8",manager, dataFactory, ontologieRegistre);
		 addPereFils("m14","m9",manager, dataFactory, ontologieRegistre);
		 addPereFils("m14","m10",manager, dataFactory, ontologieRegistre);
		 addPereFils("m14","m11",manager, dataFactory, ontologieRegistre);
		 addPereFils("m14","m12",manager, dataFactory, ontologieRegistre);
		 addPereFils("m14","m13",manager, dataFactory, ontologieRegistre);
		
		
		 // creation propriété
		
		AddObjectProperties(manager, dataFactory, ontologieRegistre, "aPourDgMorpho", "Diagnostic", "Morpho");
		AddObjectProperties(manager, dataFactory, ontologieRegistre, "aPourDgTopo", "Diagnostic", "Topo");
		AddObjectProperties(manager, dataFactory, ontologieRegistre, "maladieDe", "Maladie", "Patient");
		AddObjectProperties(manager, dataFactory, ontologieRegistre, "aPourDg", "", "Diagnostic");
		
		
		
		// classes défnies
		
		/*
		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinome_SAI_Glande_prostatique","Diagnostic","aPourDgMorpho","81403","aPourDgTopo","C61.9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Tumeur_maligne_SAI_Glande_prostatique","Diagnostic","aPourDgMorpho","80003","aPourDgTopo","C61.9");



		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Bouche","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Langue","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Glande_parotide","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Glandes_salivaires","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Nasopharynx","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Oesophage","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Estomac","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Intestin_gr?le","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Colon","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Pancreas","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Larynx","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Thymus","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Mediastin","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Peau","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Sein","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Vulve","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Vagin","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Col_uterin","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Corps_uterin","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Uterus_SAI","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Ovaire","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Placenta","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Prostate","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Testicule","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Rein","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Voies_urinaires","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Meninges","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Encephale","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Glande_thyroide","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_types_of_cancer_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m17","aPourDgTopo","t52");

		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Bouche","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Langue","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Glande_parotide","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Glandes_salivaires","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Nasopharynx","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Oesophage","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Estomac","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Intestin_grêle","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Colon","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Pancreas","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Larynx","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Thymus","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Mediastin","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Peau","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Sein","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Vulve","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Vagin","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Col_uterin","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Corps_uterin","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Uterus_SAI","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Ovaire","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Placenta","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Prostate","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Testicule","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Rein","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Voies_urinaires","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Meninges","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Encephale","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Glande_thyroide","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Unspecified_carcinomas_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m5","aPourDgTopo","t52");
	
		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Bouche","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Langue","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Glande_parotide","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Glandes_salivaires","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Nasopharynx","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Oesophage","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Estomac","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Intestin_grêle","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Colon","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Pancreas","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Larynx","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Thymus","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Mediastin","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Peau","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Sein","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Vulve","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Vagin","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Col_uterin","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Corps_uterin","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Uterus_SAI","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Ovaire","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Placenta","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Prostate","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Testicule","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Rein","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Voies_urinaires","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Meninges","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Encephale","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Glande_thyroide","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m1","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Squamous_and_transitional_cell_carcinomas_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t52");
	
		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Bouche","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Langue","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Glande_parotide","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Glandes_salivaires","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Nasopharynx","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Oesophage","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Estomac","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Intestin_grêle","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Colon","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Pancreas","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Larynx","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Thymus","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Mediastin","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Peau","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Sein","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Vulve","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Vagin","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Col_uterin","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Corps_uterin","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Uterus_SAI","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Ovaire","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Placenta","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Prostate","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Testicule","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Rein","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Voies_urinaires","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Meninges","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Encephale","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Glande_thyroide","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Basal_cell_carcinomas_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m2","aPourDgTopo","t52");
	
		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Bouche","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Langue","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Glande_parotide","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Glandes_salivaires","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Nasopharynx","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Oesophage","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Estomac","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Intestin_grêle","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Colon","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Pancreas","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Larynx","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Thymus","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Mediastin","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Peau","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Sein","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Vulve","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Vagin","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Col_uterin","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Corps_uterin","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Uterus_SAI","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Ovaire","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Placenta","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Prostate","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Testicule","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Rein","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Voies_urinaires","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Meninges","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Encephale","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Glande_thyroide","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Adenocarcinomas_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m3","aPourDgTopo","t52");

		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Bouche","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Langue","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Glande_parotide","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Glandes_salivaires","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Nasopharynx","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Oesophage","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Estomac","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Intestin_grêle","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Colon","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Pancreas","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Larynx","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Thymus","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Mediastin","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Peau","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Sein","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Vulve","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Vagin","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Col_uterin","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Corps_uterin","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Uterus_SAI","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Ovaire","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Placenta","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Prostate","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Testicule","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Rein","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Voies_urinaires","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Meninges","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Encephale","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Glande_thyroide","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specific_carcinomas_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m4","aPourDgTopo","t52");
	
		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Bouche","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Langue","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Glande_parotide","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Glandes_salivaires","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Nasopharynx","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Oesophage","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Estomac","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Intestin_grêle","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Colon","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Pancreas","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Larynx","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Thymus","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Mediastin","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Peau","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Sein","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Vulve","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Vagin","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Col_uterin","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Corps_uterin","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Uterus_SAI","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Ovaire","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Placenta","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Prostate","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Testicule","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Rein","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Voies_urinaires","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Meninges","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Encephale","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Glande_thyroide","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m6","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m8","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Sarcomas_and_soft_tissue_tumors_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m9","aPourDgTopo","t52");
	
		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Bouche","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Langue","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Glande_parotide","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Glandes_salivaires","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Nasopharynx","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Oesophage","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Estomac","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Intestin_grêle","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Colon","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Pancreas","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Larynx","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Thymus","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Mediastin","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Peau","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Sein","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Vulve","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Vagin","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Col_uterin","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Corps_uterin","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Uterus_SAI","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Ovaire","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Placenta","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Prostate","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Testicule","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Rein","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Voies_urinaires","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Meninges","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Encephale","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Glande_thyroide","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Mesotheliomas_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m7","aPourDgTopo","t52");

		
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Bouche","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t1");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Langue","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t2");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Glande_parotide","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t3");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Glandes_salivaires","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t4");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Pharynx_(hors_nasopahrynx)","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t5");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Nasopharynx","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t6");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Oesophage","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t7");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Estomac","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t8");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Intestin_grêle","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t9");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Colon","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t10");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Rectum_et_jonction","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t11");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Anus_et_canal_anal","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t12");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Foie_et_voies_biliaires_intrahepatiques","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t13");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Vesicule_et_autres_localisations_des_voies_biliaires","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t14");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Pancreas","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t15");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Tractus_gastro-intestinal_SAI","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t16");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Fosse_nasale_et_oreille_moyenne","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t17");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Sinus_de_la_face","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t18");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Larynx","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t19");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Trachee_Bronche_et_poumon","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t20");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Thymus","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t21");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Mediastin","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t22");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Appareil_respiratoire","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t23");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Os,_articulations_et_cartilage_articulaire","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t24");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Systemes_hematopoietique_et_reticulo-endothelial","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t25");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Peau","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t26");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Nerfs_peripheriques_et_systeme_nerveux_autonome","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t27");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Retroperitoine_et_peritoine","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t28");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t29");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Sein","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t30");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Vulve","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t31");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Vagin","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t32");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Col_uterin","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t33");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Corps_uterin","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t34");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Uterus_SAI","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t35");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Ovaire","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t36");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Appareil_genital_feminin_SAI","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t37");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Placenta","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t38");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Organes_genitaux_masculins","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t39");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Prostate","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t40");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Testicule","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t41");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Rein","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t42");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Voies_urinaires","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t43");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Oeil_et_annexes","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t44");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Meninges","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t45");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Encephale","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t46");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t47");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Glande_thyroide","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t48");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Autres_glandes_endocrines_et_structures_apparentees","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t49");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Autres_localisations_et_localisations_mal_definies","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t50");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Ganglions_lymphatiques","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t51");
		addClassesdéfiniesD_Diag(manager,dataFactory,ontologieRegistre,"Other_specified_types_of_cancer_Site_primaire_inconnu","Diagnostic","aPourDgMorpho","m16","aPourDgTopo","t52")
	
	
	
	
	
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Bouche","Unspecified_types_of_cancer_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Langue","Unspecified_types_of_cancer_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Glande_parotide","Unspecified_types_of_cancer_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Glandes_salivaires","Unspecified_types_of_cancer_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Pharynx_(hors_nasopahrynx)","Unspecified_types_of_cancer_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Nasopharynx","Unspecified_types_of_cancer_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Oesophage","Unspecified_types_of_cancer_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Estomac","Unspecified_types_of_cancer_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Intestin_gr?le","Unspecified_types_of_cancer_Intestin_gr?le");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Colon","Unspecified_types_of_cancer_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Rectum_et_jonction","Unspecified_types_of_cancer_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Anus_et_canal_anal","Unspecified_types_of_cancer_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Foie_et_voies_biliaires_intrahepatiques","Unspecified_types_of_cancer_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Vesicule_et_autres_localisations_des_voies_biliaires","Unspecified_types_of_cancer_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Pancreas","Unspecified_types_of_cancer_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Tractus_gastro-intestinal_SAI","Unspecified_types_of_cancer_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Fosse_nasale_et_oreille_moyenne","Unspecified_types_of_cancer_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Sinus_de_la_face","Unspecified_types_of_cancer_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Larynx","Unspecified_types_of_cancer_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Trachee_Bronche_et_poumon","Unspecified_types_of_cancer_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Thymus","Unspecified_types_of_cancer_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Mediastin","Unspecified_types_of_cancer_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Appareil_respiratoire","Unspecified_types_of_cancer_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Os,_articulations_et_cartilage_articulaire","Unspecified_types_of_cancer_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Systemes_hematopoietique_et_reticulo-endothelial","Unspecified_types_of_cancer_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Peau","Unspecified_types_of_cancer_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Nerfs_peripheriques_et_systeme_nerveux_autonome","Unspecified_types_of_cancer_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Retroperitoine_et_peritoine","Unspecified_types_of_cancer_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Unspecified_types_of_cancer_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Sein","Unspecified_types_of_cancer_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Vulve","Unspecified_types_of_cancer_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Vagin","Unspecified_types_of_cancer_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Col_uterin","Unspecified_types_of_cancer_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Corps_uterin","Unspecified_types_of_cancer_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Uterus_SAI","Unspecified_types_of_cancer_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Ovaire","Unspecified_types_of_cancer_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Appareil_genital_feminin_SAI","Unspecified_types_of_cancer_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Placenta","Unspecified_types_of_cancer_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Organes_genitaux_masculins","Unspecified_types_of_cancer_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Prostate","Unspecified_types_of_cancer_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Testicule","Unspecified_types_of_cancer_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Rein","Unspecified_types_of_cancer_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Voies_urinaires","Unspecified_types_of_cancer_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Oeil_et_annexes","Unspecified_types_of_cancer_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Meninges","Unspecified_types_of_cancer_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Encephale","Unspecified_types_of_cancer_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Unspecified_types_of_cancer_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Glande_thyroide","Unspecified_types_of_cancer_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Autres_glandes_endocrines_et_structures_apparentees","Unspecified_types_of_cancer_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Autres_localisations_et_localisations_mal_definies","Unspecified_types_of_cancer_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Ganglions_lymphatiques","Unspecified_types_of_cancer_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_cancer_Site_primaire_inconnu","Unspecified_types_of_cancer_Site_primaire_inconnu");





		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Bouche","Unspecified_carcinomas_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Langue","Unspecified_carcinomas_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Glande_parotide","Unspecified_carcinomas_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Glandes_salivaires","Unspecified_carcinomas_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Pharynx_(hors_nasopahrynx)","Unspecified_carcinomas_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Nasopharynx","Unspecified_carcinomas_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Oesophage","Unspecified_carcinomas_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Estomac","Unspecified_carcinomas_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Intestin_grêle","Unspecified_carcinomas_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Colon","Unspecified_carcinomas_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Rectum_et_jonction","Unspecified_carcinomas_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Anus_et_canal_anal","Unspecified_carcinomas_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Unspecified_carcinomas_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Unspecified_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Pancreas","Unspecified_carcinomas_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Tractus_gastro-intestinal_SAI","Unspecified_carcinomas_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Fosse_nasale_et_oreille_moyenne","Unspecified_carcinomas_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Sinus_de_la_face","Unspecified_carcinomas_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Larynx","Unspecified_carcinomas_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Trachee_Bronche_et_poumon","Unspecified_carcinomas_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Thymus","Unspecified_carcinomas_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Mediastin","Unspecified_carcinomas_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Appareil_respiratoire","Unspecified_carcinomas_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Os,_articulations_et_cartilage_articulaire","Unspecified_carcinomas_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Unspecified_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Peau","Unspecified_carcinomas_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Unspecified_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Retroperitoine_et_peritoine","Unspecified_carcinomas_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Unspecified_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Sein","Unspecified_carcinomas_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Vulve","Unspecified_carcinomas_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Vagin","Unspecified_carcinomas_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Col_uterin","Unspecified_carcinomas_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Corps_uterin","Unspecified_carcinomas_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Uterus_SAI","Unspecified_carcinomas_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Ovaire","Unspecified_carcinomas_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Appareil_genital_feminin_SAI","Unspecified_carcinomas_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Placenta","Unspecified_carcinomas_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Organes_genitaux_masculins","Unspecified_carcinomas_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Prostate","Unspecified_carcinomas_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Testicule","Unspecified_carcinomas_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Rein","Unspecified_carcinomas_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Voies_urinaires","Unspecified_carcinomas_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Oeil_et_annexes","Unspecified_carcinomas_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Meninges","Unspecified_carcinomas_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Encephale","Unspecified_carcinomas_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Unspecified_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Glande_thyroide","Unspecified_carcinomas_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Unspecified_carcinomas_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Autres_localisations_et_localisations_mal_definies","Unspecified_carcinomas_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Ganglions_lymphatiques","Unspecified_carcinomas_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_carcinomas_Site_primaire_inconnu","Unspecified_carcinomas_Site_primaire_inconnu");


		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Bouche","Squamous_and_transitional_cell_carcinomas_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Langue","Squamous_and_transitional_cell_carcinomas_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Glande_parotide","Squamous_and_transitional_cell_carcinomas_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Glandes_salivaires","Squamous_and_transitional_cell_carcinomas_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Pharynx_(hors_nasopahrynx)","Squamous_and_transitional_cell_carcinomas_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Nasopharynx","Squamous_and_transitional_cell_carcinomas_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Oesophage","Squamous_and_transitional_cell_carcinomas_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Estomac","Squamous_and_transitional_cell_carcinomas_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Intestin_grêle","Squamous_and_transitional_cell_carcinomas_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Colon","Squamous_and_transitional_cell_carcinomas_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Rectum_et_jonction","Squamous_and_transitional_cell_carcinomas_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Anus_et_canal_anal","Squamous_and_transitional_cell_carcinomas_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Squamous_and_transitional_cell_carcinomas_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Squamous_and_transitional_cell_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Pancreas","Squamous_and_transitional_cell_carcinomas_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Tractus_gastro-intestinal_SAI","Squamous_and_transitional_cell_carcinomas_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Fosse_nasale_et_oreille_moyenne","Squamous_and_transitional_cell_carcinomas_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Sinus_de_la_face","Squamous_and_transitional_cell_carcinomas_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Larynx","Squamous_and_transitional_cell_carcinomas_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Trachee_Bronche_et_poumon","Squamous_and_transitional_cell_carcinomas_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Thymus","Squamous_and_transitional_cell_carcinomas_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Mediastin","Squamous_and_transitional_cell_carcinomas_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Appareil_respiratoire","Squamous_and_transitional_cell_carcinomas_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Os,_articulations_et_cartilage_articulaire","Squamous_and_transitional_cell_carcinomas_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Squamous_and_transitional_cell_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Peau","Squamous_and_transitional_cell_carcinomas_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Squamous_and_transitional_cell_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Retroperitoine_et_peritoine","Squamous_and_transitional_cell_carcinomas_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Squamous_and_transitional_cell_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Sein","Squamous_and_transitional_cell_carcinomas_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Vulve","Squamous_and_transitional_cell_carcinomas_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Vagin","Squamous_and_transitional_cell_carcinomas_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Col_uterin","Squamous_and_transitional_cell_carcinomas_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Corps_uterin","Squamous_and_transitional_cell_carcinomas_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Uterus_SAI","Squamous_and_transitional_cell_carcinomas_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Ovaire","Squamous_and_transitional_cell_carcinomas_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Appareil_genital_feminin_SAI","Squamous_and_transitional_cell_carcinomas_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Placenta","Squamous_and_transitional_cell_carcinomas_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Organes_genitaux_masculins","Squamous_and_transitional_cell_carcinomas_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Prostate","Squamous_and_transitional_cell_carcinomas_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Testicule","Squamous_and_transitional_cell_carcinomas_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Rein","Squamous_and_transitional_cell_carcinomas_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Voies_urinaires","Squamous_and_transitional_cell_carcinomas_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Oeil_et_annexes","Squamous_and_transitional_cell_carcinomas_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Meninges","Squamous_and_transitional_cell_carcinomas_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Encephale","Squamous_and_transitional_cell_carcinomas_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Squamous_and_transitional_cell_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Glande_thyroide","Squamous_and_transitional_cell_carcinomas_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Squamous_and_transitional_cell_carcinomas_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Autres_localisations_et_localisations_mal_definies","Squamous_and_transitional_cell_carcinomas_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Ganglions_lymphatiques","Squamous_and_transitional_cell_carcinomas_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Squamous_and_transitional_cell_carcinomas_Site_primaire_inconnu","Squamous_and_transitional_cell_carcinomas_Site_primaire_inconnu");




		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Bouche","Basal_cell_carcinomas_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Langue","Basal_cell_carcinomas_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Glande_parotide","Basal_cell_carcinomas_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Glandes_salivaires","Basal_cell_carcinomas_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Pharynx_(hors_nasopahrynx)","Basal_cell_carcinomas_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Nasopharynx","Basal_cell_carcinomas_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Oesophage","Basal_cell_carcinomas_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Estomac","Basal_cell_carcinomas_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Intestin_grêle","Basal_cell_carcinomas_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Colon","Basal_cell_carcinomas_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Rectum_et_jonction","Basal_cell_carcinomas_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Anus_et_canal_anal","Basal_cell_carcinomas_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Basal_cell_carcinomas_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Basal_cell_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Pancreas","Basal_cell_carcinomas_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Tractus_gastro-intestinal_SAI","Basal_cell_carcinomas_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Fosse_nasale_et_oreille_moyenne","Basal_cell_carcinomas_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Sinus_de_la_face","Basal_cell_carcinomas_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Larynx","Basal_cell_carcinomas_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Trachee_Bronche_et_poumon","Basal_cell_carcinomas_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Thymus","Basal_cell_carcinomas_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Mediastin","Basal_cell_carcinomas_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Appareil_respiratoire","Basal_cell_carcinomas_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Os,_articulations_et_cartilage_articulaire","Basal_cell_carcinomas_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Basal_cell_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Peau","Basal_cell_carcinomas_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Basal_cell_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Retroperitoine_et_peritoine","Basal_cell_carcinomas_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Basal_cell_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Sein","Basal_cell_carcinomas_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Vulve","Basal_cell_carcinomas_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Vagin","Basal_cell_carcinomas_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Col_uterin","Basal_cell_carcinomas_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Corps_uterin","Basal_cell_carcinomas_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Uterus_SAI","Basal_cell_carcinomas_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Ovaire","Basal_cell_carcinomas_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Appareil_genital_feminin_SAI","Basal_cell_carcinomas_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Placenta","Basal_cell_carcinomas_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Organes_genitaux_masculins","Basal_cell_carcinomas_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Prostate","Basal_cell_carcinomas_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Testicule","Basal_cell_carcinomas_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Rein","Basal_cell_carcinomas_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Voies_urinaires","Basal_cell_carcinomas_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Oeil_et_annexes","Basal_cell_carcinomas_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Meninges","Basal_cell_carcinomas_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Encephale","Basal_cell_carcinomas_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Basal_cell_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Glande_thyroide","Basal_cell_carcinomas_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Basal_cell_carcinomas_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Autres_localisations_et_localisations_mal_definies","Basal_cell_carcinomas_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Ganglions_lymphatiques","Basal_cell_carcinomas_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Basal_cell_carcinomas_Site_primaire_inconnu","Basal_cell_carcinomas_Site_primaire_inconnu");





		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Bouche","Adenocarcinomas_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Langue","Adenocarcinomas_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Glande_parotide","Adenocarcinomas_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Glandes_salivaires","Adenocarcinomas_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Pharynx_(hors_nasopahrynx)","Adenocarcinomas_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Nasopharynx","Adenocarcinomas_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Oesophage","Adenocarcinomas_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Estomac","Adenocarcinomas_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Intestin_grêle","Adenocarcinomas_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Colon","Adenocarcinomas_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Rectum_et_jonction","Adenocarcinomas_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Anus_et_canal_anal","Adenocarcinomas_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Foie_et_voies_biliaires_intrahepatiques","Adenocarcinomas_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Adenocarcinomas_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Pancreas","Adenocarcinomas_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Tractus_gastro-intestinal_SAI","Adenocarcinomas_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Fosse_nasale_et_oreille_moyenne","Adenocarcinomas_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Sinus_de_la_face","Adenocarcinomas_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Larynx","Adenocarcinomas_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Trachee_Bronche_et_poumon","Adenocarcinomas_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Thymus","Adenocarcinomas_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Mediastin","Adenocarcinomas_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Appareil_respiratoire","Adenocarcinomas_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Os,_articulations_et_cartilage_articulaire","Adenocarcinomas_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Adenocarcinomas_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Peau","Adenocarcinomas_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Adenocarcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Retroperitoine_et_peritoine","Adenocarcinomas_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Adenocarcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Sein","Adenocarcinomas_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Vulve","Adenocarcinomas_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Vagin","Adenocarcinomas_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Col_uterin","Adenocarcinomas_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Corps_uterin","Adenocarcinomas_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Uterus_SAI","Adenocarcinomas_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Ovaire","Adenocarcinomas_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Appareil_genital_feminin_SAI","Adenocarcinomas_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Placenta","Adenocarcinomas_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Organes_genitaux_masculins","Adenocarcinomas_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Prostate","Adenocarcinomas_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Testicule","Adenocarcinomas_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Rein","Adenocarcinomas_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Voies_urinaires","Adenocarcinomas_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Oeil_et_annexes","Adenocarcinomas_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Meninges","Adenocarcinomas_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Encephale","Adenocarcinomas_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Adenocarcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Glande_thyroide","Adenocarcinomas_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Autres_glandes_endocrines_et_structures_apparentees","Adenocarcinomas_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Autres_localisations_et_localisations_mal_definies","Adenocarcinomas_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Ganglions_lymphatiques","Adenocarcinomas_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Adenocarcinomas_Site_primaire_inconnu","Adenocarcinomas_Site_primaire_inconnu");



		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Bouche","Other_specific_carcinomas_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Langue","Other_specific_carcinomas_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Glande_parotide","Other_specific_carcinomas_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Glandes_salivaires","Other_specific_carcinomas_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Pharynx_(hors_nasopahrynx)","Other_specific_carcinomas_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Nasopharynx","Other_specific_carcinomas_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Oesophage","Other_specific_carcinomas_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Estomac","Other_specific_carcinomas_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Intestin_grêle","Other_specific_carcinomas_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Colon","Other_specific_carcinomas_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Rectum_et_jonction","Other_specific_carcinomas_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Anus_et_canal_anal","Other_specific_carcinomas_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Foie_et_voies_biliaires_intrahepatiques","Other_specific_carcinomas_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires","Other_specific_carcinomas_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Pancreas","Other_specific_carcinomas_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Tractus_gastro-intestinal_SAI","Other_specific_carcinomas_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Fosse_nasale_et_oreille_moyenne","Other_specific_carcinomas_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Sinus_de_la_face","Other_specific_carcinomas_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Larynx","Other_specific_carcinomas_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Trachee_Bronche_et_poumon","Other_specific_carcinomas_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Thymus","Other_specific_carcinomas_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Mediastin","Other_specific_carcinomas_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Appareil_respiratoire","Other_specific_carcinomas_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Os,_articulations_et_cartilage_articulaire","Other_specific_carcinomas_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial","Other_specific_carcinomas_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Peau","Other_specific_carcinomas_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Other_specific_carcinomas_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Retroperitoine_et_peritoine","Other_specific_carcinomas_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Other_specific_carcinomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Sein","Other_specific_carcinomas_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Vulve","Other_specific_carcinomas_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Vagin","Other_specific_carcinomas_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Col_uterin","Other_specific_carcinomas_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Corps_uterin","Other_specific_carcinomas_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Uterus_SAI","Other_specific_carcinomas_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Ovaire","Other_specific_carcinomas_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Appareil_genital_feminin_SAI","Other_specific_carcinomas_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Placenta","Other_specific_carcinomas_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Organes_genitaux_masculins","Other_specific_carcinomas_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Prostate","Other_specific_carcinomas_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Testicule","Other_specific_carcinomas_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Rein","Other_specific_carcinomas_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Voies_urinaires","Other_specific_carcinomas_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Oeil_et_annexes","Other_specific_carcinomas_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Meninges","Other_specific_carcinomas_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Encephale","Other_specific_carcinomas_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Other_specific_carcinomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Glande_thyroide","Other_specific_carcinomas_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Autres_glandes_endocrines_et_structures_apparentees","Other_specific_carcinomas_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Autres_localisations_et_localisations_mal_definies","Other_specific_carcinomas_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Ganglions_lymphatiques","Other_specific_carcinomas_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specific_carcinomas_Site_primaire_inconnu","Other_specific_carcinomas_Site_primaire_inconnu");



		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Bouche","Sarcomas_and_soft_tissue_tumors_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Langue","Sarcomas_and_soft_tissue_tumors_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Glande_parotide","Sarcomas_and_soft_tissue_tumors_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Glandes_salivaires","Sarcomas_and_soft_tissue_tumors_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Pharynx_(hors_nasopahrynx)","Sarcomas_and_soft_tissue_tumors_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Nasopharynx","Sarcomas_and_soft_tissue_tumors_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Oesophage","Sarcomas_and_soft_tissue_tumors_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Estomac","Sarcomas_and_soft_tissue_tumors_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Intestin_grêle","Sarcomas_and_soft_tissue_tumors_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Colon","Sarcomas_and_soft_tissue_tumors_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Rectum_et_jonction","Sarcomas_and_soft_tissue_tumors_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Anus_et_canal_anal","Sarcomas_and_soft_tissue_tumors_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Foie_et_voies_biliaires_intrahepatiques","Sarcomas_and_soft_tissue_tumors_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Vesicule_et_autres_localisations_des_voies_biliaires","Sarcomas_and_soft_tissue_tumors_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Pancreas","Sarcomas_and_soft_tissue_tumors_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Tractus_gastro-intestinal_SAI","Sarcomas_and_soft_tissue_tumors_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Fosse_nasale_et_oreille_moyenne","Sarcomas_and_soft_tissue_tumors_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Sinus_de_la_face","Sarcomas_and_soft_tissue_tumors_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Larynx","Sarcomas_and_soft_tissue_tumors_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Trachee_Bronche_et_poumon","Sarcomas_and_soft_tissue_tumors_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Thymus","Sarcomas_and_soft_tissue_tumors_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Mediastin","Sarcomas_and_soft_tissue_tumors_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Appareil_respiratoire","Sarcomas_and_soft_tissue_tumors_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Os,_articulations_et_cartilage_articulaire","Sarcomas_and_soft_tissue_tumors_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Systemes_hematopoietique_et_reticulo-endothelial","Sarcomas_and_soft_tissue_tumors_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Peau","Sarcomas_and_soft_tissue_tumors_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Nerfs_peripheriques_et_systeme_nerveux_autonome","Sarcomas_and_soft_tissue_tumors_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Retroperitoine_et_peritoine","Sarcomas_and_soft_tissue_tumors_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Sarcomas_and_soft_tissue_tumors_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Sein","Sarcomas_and_soft_tissue_tumors_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Vulve","Sarcomas_and_soft_tissue_tumors_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Vagin","Sarcomas_and_soft_tissue_tumors_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Col_uterin","Sarcomas_and_soft_tissue_tumors_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Corps_uterin","Sarcomas_and_soft_tissue_tumors_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Uterus_SAI","Sarcomas_and_soft_tissue_tumors_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Ovaire","Sarcomas_and_soft_tissue_tumors_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Appareil_genital_feminin_SAI","Sarcomas_and_soft_tissue_tumors_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Placenta","Sarcomas_and_soft_tissue_tumors_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Organes_genitaux_masculins","Sarcomas_and_soft_tissue_tumors_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Prostate","Sarcomas_and_soft_tissue_tumors_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Testicule","Sarcomas_and_soft_tissue_tumors_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Rein","Sarcomas_and_soft_tissue_tumors_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Voies_urinaires","Sarcomas_and_soft_tissue_tumors_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Oeil_et_annexes","Sarcomas_and_soft_tissue_tumors_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Meninges","Sarcomas_and_soft_tissue_tumors_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Encephale","Sarcomas_and_soft_tissue_tumors_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Sarcomas_and_soft_tissue_tumors_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Glande_thyroide","Sarcomas_and_soft_tissue_tumors_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Autres_glandes_endocrines_et_structures_apparentees","Sarcomas_and_soft_tissue_tumors_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Autres_localisations_et_localisations_mal_definies","Sarcomas_and_soft_tissue_tumors_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Ganglions_lymphatiques","Sarcomas_and_soft_tissue_tumors_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Sarcomas_and_soft_tissue_tumors_Site_primaire_inconnu","Sarcomas_and_soft_tissue_tumors_Site_primaire_inconnu");


		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Bouche","Mesotheliomas_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Langue","Mesotheliomas_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Glande_parotide","Mesotheliomas_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Glandes_salivaires","Mesotheliomas_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Pharynx_(hors_nasopahrynx)","Mesotheliomas_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Nasopharynx","Mesotheliomas_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Oesophage","Mesotheliomas_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Estomac","Mesotheliomas_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Intestin_grêle","Mesotheliomas_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Colon","Mesotheliomas_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Rectum_et_jonction","Mesotheliomas_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Anus_et_canal_anal","Mesotheliomas_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Foie_et_voies_biliaires_intrahepatiques","Mesotheliomas_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Vesicule_et_autres_localisations_des_voies_biliaires","Mesotheliomas_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Pancreas","Mesotheliomas_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Tractus_gastro-intestinal_SAI","Mesotheliomas_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Fosse_nasale_et_oreille_moyenne","Mesotheliomas_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Sinus_de_la_face","Mesotheliomas_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Larynx","Mesotheliomas_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Trachee_Bronche_et_poumon","Mesotheliomas_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Thymus","Mesotheliomas_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Mediastin","Mesotheliomas_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Appareil_respiratoire","Mesotheliomas_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Os,_articulations_et_cartilage_articulaire","Mesotheliomas_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Systemes_hematopoietique_et_reticulo-endothelial","Mesotheliomas_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Peau","Mesotheliomas_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Nerfs_peripheriques_et_systeme_nerveux_autonome","Mesotheliomas_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Retroperitoine_et_peritoine","Mesotheliomas_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Mesotheliomas_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Sein","Mesotheliomas_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Vulve","Mesotheliomas_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Vagin","Mesotheliomas_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Col_uterin","Mesotheliomas_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Corps_uterin","Mesotheliomas_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Uterus_SAI","Mesotheliomas_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Ovaire","Mesotheliomas_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Appareil_genital_feminin_SAI","Mesotheliomas_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Placenta","Mesotheliomas_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Organes_genitaux_masculins","Mesotheliomas_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Prostate","Mesotheliomas_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Testicule","Mesotheliomas_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Rein","Mesotheliomas_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Voies_urinaires","Mesotheliomas_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Oeil_et_annexes","Mesotheliomas_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Meninges","Mesotheliomas_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Encephale","Mesotheliomas_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Mesotheliomas_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Glande_thyroide","Mesotheliomas_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Autres_glandes_endocrines_et_structures_apparentees","Mesotheliomas_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Autres_localisations_et_localisations_mal_definies","Mesotheliomas_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Ganglions_lymphatiques","Mesotheliomas_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mesotheliomas_Site_primaire_inconnu","Mesotheliomas_Site_primaire_inconnu");


		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Bouche","Other_specified_types_of_cancer_Bouche");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Langue","Other_specified_types_of_cancer_Langue");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Glande_parotide","Other_specified_types_of_cancer_Glande_parotide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Glandes_salivaires","Other_specified_types_of_cancer_Glandes_salivaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Pharynx_(hors_nasopahrynx)","Other_specified_types_of_cancer_Pharynx_(hors_nasopahrynx)");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Nasopharynx","Other_specified_types_of_cancer_Nasopharynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Oesophage","Other_specified_types_of_cancer_Oesophage");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Estomac","Other_specified_types_of_cancer_Estomac");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Intestin_grêle","Other_specified_types_of_cancer_Intestin_grêle");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Colon","Other_specified_types_of_cancer_Colon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Rectum_et_jonction","Other_specified_types_of_cancer_Rectum_et_jonction");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Anus_et_canal_anal","Other_specified_types_of_cancer_Anus_et_canal_anal");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Foie_et_voies_biliaires_intrahepatiques","Other_specified_types_of_cancer_Foie_et_voies_biliaires_intrahepatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Vesicule_et_autres_localisations_des_voies_biliaires","Other_specified_types_of_cancer_Vesicule_et_autres_localisations_des_voies_biliaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Pancreas","Other_specified_types_of_cancer_Pancreas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Tractus_gastro-intestinal_SAI","Other_specified_types_of_cancer_Tractus_gastro-intestinal_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Fosse_nasale_et_oreille_moyenne","Other_specified_types_of_cancer_Fosse_nasale_et_oreille_moyenne");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Sinus_de_la_face","Other_specified_types_of_cancer_Sinus_de_la_face");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Larynx","Other_specified_types_of_cancer_Larynx");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Trachee_Bronche_et_poumon","Other_specified_types_of_cancer_Trachee_Bronche_et_poumon");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Thymus","Other_specified_types_of_cancer_Thymus");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Mediastin","Other_specified_types_of_cancer_Mediastin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Appareil_respiratoire","Other_specified_types_of_cancer_Appareil_respiratoire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Os,_articulations_et_cartilage_articulaire","Other_specified_types_of_cancer_Os,_articulations_et_cartilage_articulaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Systemes_hematopoietique_et_reticulo-endothelial","Other_specified_types_of_cancer_Systemes_hematopoietique_et_reticulo-endothelial");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Peau","Other_specified_types_of_cancer_Peau");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Nerfs_peripheriques_et_systeme_nerveux_autonome","Other_specified_types_of_cancer_Nerfs_peripheriques_et_systeme_nerveux_autonome");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Retroperitoine_et_peritoine","Other_specified_types_of_cancer_Retroperitoine_et_peritoine");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous","Other_specified_types_of_cancer_Tissu_conjonctif,_tissu_sous-cutane_et_autres_tissus_mous");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Sein","Other_specified_types_of_cancer_Sein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Vulve","Other_specified_types_of_cancer_Vulve");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Vagin","Other_specified_types_of_cancer_Vagin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Col_uterin","Other_specified_types_of_cancer_Col_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Corps_uterin","Other_specified_types_of_cancer_Corps_uterin");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Uterus_SAI","Other_specified_types_of_cancer_Uterus_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Ovaire","Other_specified_types_of_cancer_Ovaire");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Appareil_genital_feminin_SAI","Other_specified_types_of_cancer_Appareil_genital_feminin_SAI");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Placenta","Other_specified_types_of_cancer_Placenta");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Organes_genitaux_masculins","Other_specified_types_of_cancer_Organes_genitaux_masculins");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Prostate","Other_specified_types_of_cancer_Prostate");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Testicule","Other_specified_types_of_cancer_Testicule");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Rein","Other_specified_types_of_cancer_Rein");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Voies_urinaires","Other_specified_types_of_cancer_Voies_urinaires");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Oeil_et_annexes","Other_specified_types_of_cancer_Oeil_et_annexes");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Meninges","Other_specified_types_of_cancer_Meninges");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Encephale","Other_specified_types_of_cancer_Encephale");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central","Other_specified_types_of_cancer_Moelle_epiniere,_nerfs_cr?niens_et_autres_regions_du_systeme_nerveux_central");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Glande_thyroide","Other_specified_types_of_cancer_Glande_thyroide");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Autres_glandes_endocrines_et_structures_apparentees","Other_specified_types_of_cancer_Autres_glandes_endocrines_et_structures_apparentees");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Autres_localisations_et_localisations_mal_definies","Other_specified_types_of_cancer_Autres_localisations_et_localisations_mal_definies");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Ganglions_lymphatiques","Other_specified_types_of_cancer_Ganglions_lymphatiques");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Other_specified_types_of_cancer_Site_primaire_inconnu","Other_specified_types_of_cancer_Site_primaire_inconnu");


		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Unspecified_types_of_haematopoietic_and_lymphoid_t...","Unspecified_types_of_haematopoietic_and_lymphoid_t...");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Myeloid","Myeloid");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_B-cell_neoplasms","B-cell_neoplasms");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_T-cell_and_NK-cell_neoplasms","T-cell_and_NK-cell_neoplasms");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Hodgkin_lymphomas","Hodgkin_lymphomas");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Mast-cell_tumours","Mast-cell_tumours");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Histiocytes_and_accessory_lymphoid_cells","Histiocytes_and_accessory_lymphoid_cells");
		addClassesdéfiniesMaladie(manager,dataFactory,ontologieRegistre,"M_Kaposi_sarcomas","Kaposi_sarcomas");
			
			
	
	

*/
		
		
		//File enregistrement = new File("C:/Users/hadri/Desktop/onto_registre/ONTO_REGISTRE/OR.owl");
		File enregistrement = new File("./ressource/OR.owl");
		manager.saveOntology(ontologieRegistre,IRI.create(enregistrement.toURI()));
		 
		 
	}
	

	
	
	
	
	
	
	
}
