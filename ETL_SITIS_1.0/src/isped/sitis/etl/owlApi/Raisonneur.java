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
		
		
		// CHARGEMENT DE LONTOLOGIE
		/*
		 OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		 OWLDataFactory dataFactory  = OWLManager.getOWLDataFactory();
		 OWLOntology ont = man.loadOntologyFromOntologyDocument(new File("C:/Users/hadri/Desktop/onto_registre/ONTO_REGISTRE/OR.owl"));
		*/
		    // decoupage des code adicap 
		
		 	
		 	
		  
///////////////////////////////////////////////////////////////////////////////////////////    
		    // on charge l'ontologie
		    
			// OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
			  //  OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
			    
		    // Creer boucle avec codes reçus : selection dans bases des topo et morpho
			
			//Connection con;
		
			
	 		Set <String> cimss10 = new HashSet <String>();
	 		cimss10.add("C00.1");
	 		cimss10.add("C02.1");
	 		/*
	 		for (String cim10 :cims10) {
	 		try { 
	 		 	
	 			// connection a la base
				Class.forName("com.mysql.jdbc.Driver");
				 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");
				
				 // REQUETE : SELECTION DES groupes topo et morpho
				 String cim = ("select transcodecim10_cimo.topo_iacr, transcodecim10_cimo.morpho_iacr from transcodecim10_cimo where cim10 = '"+cim10+"'") ;
				
		
				 
				 
					PreparedStatement prepare = con.prepareStatement (cim);
				
					ResultSet res= prepare.executeQuery();
				
				
					
					
				
				// UTILSATION RESULTAT REQUETE POUR RAISONNER SUR LONTOLOGIE
				
					
					 
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
					    System.out.println(DiagEquil.toString());
					    aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));
				} else {
					System.out.println(TOPO+ MORPHO);
					 
				    // Create your desired query class expression. 			
					OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
							 dataFactory.getOWLClass(":m"+MORPHO,pm));
					
					OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
							 dataFactory.getOWLClass(":t"+TOPO,pm));
					OWLClassExpression intersection = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );
				
					OWLClass Classediag = dataFactory.getOWLClass("D"+compteur,pm);
					   OWLAxiom DiagEquil = dataFactory.getOWLEquivalentClassesAxiom(Classediag, intersection);
					    man.addAxiom(ont, DiagEquil);
					    aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));
					    System.out.println(DiagEquil.toString());
				}
				    
				   
				    compteur = compteur + 1 ;
					  System.out.println (compteur);
		
				
			        }
			

				    	 con.close();
						prepare.close();
						res.close();
				    
				
		

			}catch (ClassNotFoundException e){

				System.out.println("Driver spécifié non trouvé");

				e.printStackTrace();

			}catch (SQLException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			finally {

			}
	 		
	 		}
	 		*/
		    Set <String> adicapss = new HashSet<String>();
		    adicapss.add("BHOTB7A0");
	 	 	adicapss.add("PHUVU7G3");
	 	 	Maladie M = new Maladie();
	 	 	Raisonneur.getDiseases(cimss10, adicapss, "ab", M);
	 	 	System.out.println(M.getID()+ M.getSuperclass()+ M.getClassEquilMal());
	 	 
	 	 	/*
	 	 	for (String adicap :adicaps) {
		 	String topoAd = adicap.substring(2,4);
		 	String morphoAd = adicap.substring(4,8);
		 	System.out.println(topoAd + "  morpho  " +morphoAd);
	try { 
	 		 	
	 			// connection a la base
				Class.forName("com.mysql.jdbc.Driver");
				 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");
				
				
				
				 String anapath = ("SELECT Distinct cimo3morpho.GROUPE_MORPHO_IACR, cim03topo.GROUPE_TOPO_IACR FROM cimo3morpho "
				 		+ "inner join anapath_cimo_morpho on cimo3morpho.CODMORPHOCIMO3 = anapath_cimo_morpho.CODMORPHOCIMO3,"
				 		+ " cim03topo inner join anapath_cimo_topo on cim03topo.CODTOPOCIM03 = anapath_cimo_topo.CODTOPOCIMO3"
				 		+ " WHERE anapath_cimo_morpho.LESION = '"+morphoAd+"' and anapath_cimo_topo.ORGANE = '"+topoAd+"'");
				// faire retour utilisateur : si topo ou morpho > 2
				 
				 
				
					PreparedStatement prepareanap =con.prepareStatement(anapath);
					
					ResultSet rsAnap = prepareanap.executeQuery();
					System.out.println(anapath.toString());
				
					
					
				
				// UTILSATION RESULTAT REQUETE POUR RAISONNER SUR LONTOLOGIE
				
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
							    System.out.println(DiagEquil.toString());
							    aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),Classediag));
						} else {
							System.out.println(TOPOA+ MORPHOA);
							 
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
							    System.out.println(DiagEquil.toString());
						}
						compteur = compteur + 1 ;
					}
				
		

			}catch (ClassNotFoundException e){

				System.out.println("Driver spécifié non trouvé");

				e.printStackTrace();

			}catch (SQLException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			finally {

			}
	 	 	}
	// on definit une maladie au patient contenant tous ses diag
	
	 OWLClassExpression maladieDe = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":maladieDe", pm),
	 dataFactory.getOWLClass(":Patient",pm));
	 OWLClassExpression aprdgs  = dataFactory.getOWLObjectIntersectionOf(aprdg);
	 OWLClassExpression query2  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Maladie", pm),aprdgs,maladieDe);

OWLClass M1 = dataFactory.getOWLClass(":M1",pm);
OWLAxiom definition2 = dataFactory.getOWLEquivalentClassesAxiom (M1, query2);
man.addAxiom(ont, definition2);

// On raisonne

   reasoner.flush();			    
   System.out.println(reasoner.getSuperClasses(M1, true));
   System.out.println(reasoner.getEquivalentClasses(M1));
   
File enregistrement = new File("C:/Users/hadri/Desktop/testraisonneur/OR1.owl");
//File enregistrement = new File("./res/OR1.owl");
	man.saveOntology(ont,IRI.create(enregistrement.toURI()));
	
	  // for (Node<OWLClass> a : newname ) {System.out.println(a);}
	    // You can now add new definitions for new queries in the same way

	    // After you are done with all queries, do not forget to free the
	    // resources occupied by the reasoner
	    reasoner.dispose();
	    */
	}

}
