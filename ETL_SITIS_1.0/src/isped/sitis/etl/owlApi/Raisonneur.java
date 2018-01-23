package isped.sitis.etl.owlApi;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataRangeVisitor;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

import com.google.common.base.Strings;





public class Raisonneur {

	private static String baseIRIStr="http://www.semanticweb.org/owlapi/ontologieRegistre#";
	private static IRI baseIRI=IRI.create(baseIRIStr);
	private static PrefixManager pm = new DefaultPrefixManager(null, null, baseIRIStr);
	static Set <String> topo = new HashSet <String>();
	static Set<OWLClass> dg = new HashSet <OWLClass>();
	static Set<OWLClassExpression> aprdg = new HashSet<OWLClassExpression>();
	static int compteur = 1;

	public Raisonneur() {
		// TODO Auto-generated constructor stub
	}

	public static  Maladie getDiseases(Set <String> cims10, Set <String> adicaps, String ID, Maladie M) throws OWLOntologyCreationException, OWLOntologyStorageException {

		// creation objet Maladie qui contiendra l'ID et les maladies 


		M.setID(ID); 


		// CHARGEMENT DE LONTOLOGIE
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLDataFactory dataFactory  = OWLManager.getOWLDataFactory();
		OWLOntology ont = man.loadOntologyFromOntologyDocument(new File("./ressource/OR.owl"));

		// chargement raisonneur
		OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(ont);


		Connection con;

		// definition des diags cim10

		for (String cim10 :cims10) {
			try { 

				// connection a la base
				Class.forName("com.mysql.jdbc.Driver");
				con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");

				// REQUETE : SELECTION DES groupes topo et morpho
				String cim = ("select transcodagecim10_cimo.topo_iacr, transcodagecim10_cimo.morpho_iacr from transcodagecim10_cimo where cim10 = '"+cim10+"'") ;
				PreparedStatement prepare = con.prepareStatement (cim);

				ResultSet res= prepare.executeQuery();

				// insertion des diags a l'ontologie

				while (res.next()){
					String TOPO  = res.getString("Topo_IACR" );
					String MORPHO = res.getString("Morpho_IACR");

					// SI pathologie hemato -> diagnostic est defini avec topo indifferencié car non discriminant pour maladies

					if (MORPHO.equals("8") || MORPHO.equals("9") || MORPHO.equals("10") || MORPHO.equals("11")|| MORPHO.equals("12")|| MORPHO.equals("13")|| MORPHO.equals("14") || MORPHO.equals("15") ) {

						OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
								dataFactory.getOWLClass(":m"+MORPHO,pm));

						OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
								dataFactory.getOWLClass(":Topo",pm));
						OWLClassExpression intersection  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );

						OWLClass Classediag = dataFactory.getOWLClass("D"+compteur,pm);
						OWLAxiom DiagEquil = dataFactory.getOWLEquivalentClassesAxiom(Classediag, intersection);
						man.addAxiom(ont, DiagEquil);


						aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));
					} else {


						OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
								dataFactory.getOWLClass(":m"+MORPHO,pm));

						OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
								dataFactory.getOWLClass(":t"+TOPO,pm));
						OWLClassExpression intersection = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );

						OWLClass Classediag = dataFactory.getOWLClass("D"+compteur,pm);
						OWLAxiom DiagEquil = dataFactory.getOWLEquivalentClassesAxiom(Classediag, intersection);
						man.addAxiom(ont, DiagEquil);
						aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));

					}
					compteur = compteur + 1 ;


				}
				con.close();
				prepare.close();
				res.close();

			}catch (ClassNotFoundException e){
				System.out.println("Driver spécifié non trouvé");
				e.printStackTrace();
			}catch (SQLException e) {
				// TODO Auto-generated catch bloc
				e.printStackTrace();
			}
			finally {
			}
		}

		// definition des diags adicap 

		for (String adicap :adicaps) {
			//decoupe codes
			String topoAd = adicap.substring(2,4);
			String morphoAd = adicap.substring(4,8);

			try { 
				// connection a la base
				Class.forName("com.mysql.jdbc.Driver");
				con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");

				String anapath = ("SELECT Distinct cimo3morpho.GROUPE_MORPHO_IACR, cim03topo.GROUPE_TOPO_IACR FROM cimo3morpho "
						+ "inner join anapath_cimo_morpho on cimo3morpho.CODMORPHOCIMO3 = anapath_cimo_morpho.CODMORPHOCIMO3,"
						+ " cim03topo inner join anapath_cimo_topo on cim03topo.CODTOPOCIM03 = anapath_cimo_topo.CODTOPOCIMO3"
						+ " WHERE anapath_cimo_morpho.LESION = '"+morphoAd+"' and anapath_cimo_topo.ORGANE = '"+topoAd+"'");


				PreparedStatement prepareanap =con.prepareStatement(anapath);
				ResultSet rsAnap = prepareanap.executeQuery();

				//  insertion des diags a l'ontologie

				while (rsAnap.next()){
					String MORPHOA = rsAnap.getString("GROUPE_MORPHO_IACR");
					String TOPOA  = rsAnap.getString("GROUPE_TOPO_IACR" );

					if (MORPHOA.equals("8") || MORPHOA.equals("9") || MORPHOA.equals("10") || MORPHOA.equals("11")|| MORPHOA.equals("12")|| MORPHOA.equals("13")|| MORPHOA.equals("14") || MORPHOA.equals("15") ) {

						OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
								dataFactory.getOWLClass(":m"+MORPHOA,pm));

						OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
								dataFactory.getOWLClass(":Topo",pm));
						OWLClassExpression intersection  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );

						OWLClass Classediag = dataFactory.getOWLClass("D"+compteur,pm);
						OWLAxiom DiagEquil = dataFactory.getOWLEquivalentClassesAxiom(Classediag, intersection);
						man.addAxiom(ont, DiagEquil);

						aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));
					} else {


						// Create your desired query class expression. 			
						OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
								dataFactory.getOWLClass(":m"+MORPHOA,pm));

						OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
								dataFactory.getOWLClass(":t"+TOPOA,pm));
						OWLClassExpression intersection = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );

						OWLClass Classediag = dataFactory.getOWLClass("D"+compteur,pm);
						OWLAxiom DiagEquil = dataFactory.getOWLEquivalentClassesAxiom(Classediag, intersection);
						man.addAxiom(ont, DiagEquil);
						aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));

					}
					compteur = compteur + 1 ;
				}

			}catch (ClassNotFoundException e){

				System.out.println("Driver spécifié non trouvé");

				e.printStackTrace();

			}catch (SQLException e) {
				// TODO Auto-generated catch bloc
				e.printStackTrace();
			}finally {
			}
		}

		// Definiton maladie du patient avec les diags défini précédemment

		OWLClassExpression maladieDe = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":maladieDe", pm),
				dataFactory.getOWLClass(":Patient",pm));
		OWLClassExpression aprdgs  = dataFactory.getOWLObjectIntersectionOf(aprdg);
		OWLClassExpression query2  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Maladie", pm),aprdgs,maladieDe);

		OWLClass M1 = dataFactory.getOWLClass(":M1",pm);
		OWLAxiom definition2 = dataFactory.getOWLEquivalentClassesAxiom (M1, query2);
		man.addAxiom(ont, definition2);

		// Raisonnement sur l'ontologie pour récupérer maladie

		reasoner.flush();
		String ClasseEquivalent = reasoner.getEquivalentClasses(M1).toString();
		M.setClassEquilMal(ClasseEquivalent);
		String SuperClass = reasoner.getSuperClasses(M1, true).toString();
		M.setSuperclass(SuperClass);
		//return ClasseEquivalent;
		//return SuperClass;
		return M;




		//File enregistrement = new File("C:/Users/hadri/Desktop/testraisonneur/OR1.owl");
		//File enregistrement = new File("./res/OR1.owl");
		//man.saveOntology(ont,IRI.create(enregistrement.toURI()));


		//reasoner.dispose();
	}






	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		
		
		
		
		
		
		Connection con;
	
 		try { 			
 			
			 Class.forName("com.mysql.jdbc.Driver");
			 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");
			 Maladie M = new Maladie();
			 String part = ""; 
			String partok=""; 
			String id="";
			 // requête selection idpat  et liste cim10 et string
			String sql = (" select `id`,`CIM10`,`adicap` from new_testmaladie ORDER BY 'id'");
			// requete insertion resultat
			String mInsert = ("INSERT INTO notif_cas(id_Patient, id_Cancer) VALUES ('"+id+"','"+ partok+ "');") ;
			
				
			PreparedStatement prepare =con.prepareStatement(sql);
			PreparedStatement prepare1 = con.prepareStatement (mInsert);
			ResultSet res= prepare.executeQuery();
			// pour chaque résultat appel de la methode get disease puis ajout résultats dans table
				 while (res.next()) {
					 String ID =  res.getString("id");
					 String CIM10 = res.getString("CIM10");
					 String ADICAP = res.getString("adicap");
				
					 Set <String> cims10 = new HashSet <String>();
					 String [] cim = CIM10.split(",");
					 for( int i = 0; i<cim.length; ++i) {
						 cims10.add(cim[i]);
					 }
					 					 
					 Set <String> adicaps = new HashSet <String>();
					 ADICAP = ADICAP+",";
					 String [] adicap = ADICAP.split(",");
					 for( int i = 0; i<adicap.length; ++i) {
						 adicaps.add(adicap[i]);
					 					 }
					
					Raisonneur.getDiseases(cims10, adicaps, ID , M);
				 	System.out.println( M.getID()+"  "+ M.getSuperclass()+ M.getClassEquilMal());
				 	 					
				 	
				 	
				 	
					// Création d'un string equivMal qui récupère l'(les) URI des classes ééquivalentes
					// partsEquiv récupère toutes les URI
					// s'il y a qu'une URI : récupération des superClasses
					//s'il y a plusieurs URI : récupération des classes équivalentes uniquement (dans le else)
					//String equivMal = M.getClassEquilMal();
				 
				 	String equivMal = "Node( <http://www.semanticweb.org/owlapi/ontologieRegistre#Mm1> ), Node( <http://www.semanticweb.org/owlapi/ontologieRegistre#Mm34> ))";
					String[] partsEquiv = equivMal.split(Pattern.quote(","));
					id = M.getID();
=======




		Set <String> cimss10 = new HashSet <String>();
		cimss10.add("C00.1");
		cimss10.add("C02.1");

		Set <String> adicapss = new HashSet<String>();
		adicapss.add("BHOTB7A0");
		adicapss.add("PHUVU7G3");

		Maladie M = new Maladie();

		Raisonneur.getDiseases(cimss10, adicapss, "ab", M);

		// Création d'un string equivMal qui récupère l'(les) URI des classes ééquivalentes
		// partsEquiv récupère toutes les URI
		// s'il y a qu'une URI : récupération des superClasses
		//s'il y a plusieurs URI : récupération des classes équivalentes uniquement (dans le else)
		//String equivMal = M.getClassEquilMal();
		String equivMal = "Node( <http://www.semanticweb.org/owlapi/ontologieRegistre#Mm1> ), Node( <http://www.semanticweb.org/owlapi/ontologieRegistre#Mm34> ))";
		String[] partsEquiv = equivMal.split(Pattern.quote(","));
		String id = M.getID();

		if(partsEquiv.length == 1)
		{	 	 	
			String superClassMal = M.getSuperclass();
			String[] partsSuper = superClassMal.split(Pattern.quote(","));

			for (int i=0; i<partsSuper.length; i++) 
			{
				String part = partsSuper[i]; 
				String partok = part.substring(part.indexOf("#") +2 , part.indexOf(">"));

				Connection con;

				try {

					// connection a la base
					Class.forName("com.mysql.jdbc.Driver");
					con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");

					// REQUETE : insertion de la maladie et de l'identifiant patient
					// le flag se met automatiquement à 0 et la date et heure se met à l'heure courante de l'insertion
					String mInsert = ("INSERT INTO notif_cas(id_Patient, id_Cancer) VALUES ('"+id+"','"+ partok+ "');") ;
					PreparedStatement prepare = con.prepareStatement (mInsert);

					prepare.executeUpdate();

					con.close();
					prepare.close();


				}catch (ClassNotFoundException e){
					System.out.println("Driver spécifié non trouvé");
					e.printStackTrace();
				}catch (SQLException e) {
					// TODO Auto-generated catch bloc
					e.printStackTrace();
				}

				System.out.println(partok);
				// ce system out.println sert à vérifier la liste des URI
				System.out.println(M.getID()+ "-" + M.getSuperclass() +  "-" + M.getClassEquilMal());
			}

		}
		else	 
		{
			for (int i=0; i<partsEquiv.length; i++) 
			{
				String part = partsEquiv[i]; 
				String partok = part.substring(part.indexOf("#") +2 , part.indexOf(">"));


				Connection con;

				try {

					// connection a la base
					Class.forName("com.mysql.jdbc.Driver");
					con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");

					// REQUETE : insertion de la maladie et de l'identifiant patient
					// le flag se met automatiquement à 0 et la date et heure se met à l'heure courante de l'insertion
					String mInsert = ("INSERT INTO notif_cas(id_Patient, id_Cancer) VALUES ('"+id+"','"+ partok+ "');") ;
					PreparedStatement prepare = con.prepareStatement (mInsert);

					prepare.executeUpdate();

					con.close();
					prepare.close();


				}catch (ClassNotFoundException e){
					System.out.println("Driver spécifié non trouvé");
					e.printStackTrace();
				}catch (SQLException e) {
					// TODO Auto-generated catch bloc
					e.printStackTrace();
				}


				System.out.println(partok);
				// ce system out.println sert à vérifier la liste des URI
				System.out.println(M.getID()+ "-" + M.getSuperclass() +  "-" + M.getClassEquilMal());
			}	 
		}
	}
>>>>>>> branch 'ETLhad' of https://github.com/kaisbh/ETL_SITIS.git

<<<<<<< HEAD
					if(partsEquiv.length == 1)
					{	 	 	
						String superClassMal = M.getSuperclass();
						String[] partsSuper = superClassMal.split(Pattern.quote(","));

						for (int i=0; i<partsSuper.length; i++) 
						{
							 part = partsSuper[i]; 
							 partok = part.substring(part.indexOf("#") +2 , part.indexOf(">"));

							prepare1.executeUpdate();
							

							System.out.println(partok);
							// ce system out.println sert à vérifier la liste des URI
							System.out.println(M.getID()+ "-" + M.getSuperclass() +  "-" + M.getClassEquilMal());
				 				 }
					}else {
						for (int i=0; i<partsEquiv.length; i++) 
						{
							part = partsEquiv[i]; 
							partok = part.substring(part.indexOf("#") +2 , part.indexOf(">"));
							prepare1.executeUpdate();

							System.out.println(partok);
							// ce system out.println sert à vérifier la liste des URI
							System.out.println(M.getID()+ "-" + M.getSuperclass() +  "-" + M.getClassEquilMal());
						}}
								

		

		}
		con.close();
		prepare.close();
		res.close();
		}catch (ClassNotFoundException e){

			System.out.println("Driver spécifié non trouvé");

			e.printStackTrace();

		}catch (SQLException e) {
			e.printStackTrace();
		}
							
					
 		
 	
						}}
=======


}

>>>>>>> branch 'ETLhad' of https://github.com/kaisbh/ETL_SITIS.git

