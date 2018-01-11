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
	public Raisonneur() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		// TODO Auto-generated method stub
		
		
		// CHARGEMENT DE LONTOLOGIE
		
		 OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		 OWLDataFactory dataFactory  = OWLManager.getOWLDataFactory();
		 OWLOntology ont = man.loadOntologyFromOntologyDocument(new File("C:/Users/hadri/Desktop/onto_registre/ONTO_REGISTRE/OR.owl"));
		 //System.out.println(ont.toString());
		 
		 
		
		 /*////////////////////////////////////////////////////////////////////////////////////////////////
		 
		 
	 		Connection con;
	 		try { 
	 	
	 			// connection a la base
				Class.forName("com.mysql.jdbc.Driver");
				 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1/registre", "root", "");
				
				 
				 // REQUETE : SELECTION DES CODES CIMO CORESSPONDANT AU CODE CIM10
				 String sql = ("select distinct CODTOPOCIMO3, CODMORPHOCIMO3 from cimo3_cui "
				 		+ "inner join transcodagecim10_cui on cimo3_cui.CUI_UMLS = transcodagecim10_cui.CUI "
				 		+ "where transcodagecim10_cui.CIM10 = 'C46.7'");
				 
				 String sql2 = ("INSERT INTO `transcodagecim10_cui`(`CIM10`, `CUI`) VALUES (?,?)");
	
				PreparedStatement prepare = con.prepareStatement (sql);
				PreparedStatement prepare2 = con.prepareStatement (sql2);
				 
				
				ResultSet res= prepare.executeQuery();
				
				// UTILSATION RESULTAT REQUETE POUR RAISONNER SUR LONTOLOGIE
				
				while (res.next()){
					
					String CODTOPOCIM03  = res.getString("CODTOPOCIMO3" );
					int CODeMORPHOCIMO3 = res.getInt("CODMORPHOCIMO3");
					String CODMORPHOCIMO3 = Integer.toString(CODeMORPHOCIMO3);
				
					System.out.println(CODTOPOCIM03+ CODMORPHOCIMO3);
					 
					
					OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
							 dataFactory.getOWLClass(":"+CODTOPOCIM03,pm));
					
					
					// RAISONNEMENT SUR LONTOLOGIE : ON CREE UN AXIOM PUIS ON RAISONNE SUR CET AXIOM
				    // Create an ELK reasoner.
				    OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
				    OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
				    
				    // Create your desired query class expression. 			
					OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
							 dataFactory.getOWLClass(":"+CODMORPHOCIMO3,pm));
					
					OWLClassExpression avoirunetopoi = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
							 dataFactory.getOWLClass(":"+CODTOPOCIM03,pm));
					OWLClassExpression query  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );
				
				    // Create a fresh name for the query.
				    OWLClass newName = dataFactory.getOWLClass(":temp001",pm);
				    
				    // Make the query equivalent to the fresh class
				    OWLAxiom definition = dataFactory.getOWLEquivalentClassesAxiom(newName, query);
				    man.addAxiom(ont, definition);
				   
				    
				    // Remember to either flush the reasoner after the ontology change
				    // or create the reasoner in non-buffering mode. Note that querying
				    // a reasoner after an ontology change triggers re-classification of
				    // the whole ontology which might be costly. Therefore, if you plan
				    // to query for multiple complex class expressions, it will be more
				    // efficient to add the corresponding definitions to the ontology at
				    // once before asking any queries to the reasoner.
				    reasoner.flush();

				    // You can now retrieve subclasses, superclasses, and instances of
				    // the query class by using its new name instead.
				   

				    // RECUPERE SUPERCLASSE / INSTANCE / CLASSES EQUIVALENTE
				    
				    System.out.println(reasoner.getSuperClasses(newName, true));
				    System.out.println(reasoner.getInstances(newName, false));
				    System.out.println(reasoner.getEquivalentClasses(newName));
				   
				 		
				    
				    OWLClass vegPizza = dataFactory.getOWLClass(IRI
			                .create("http://www.semanticweb.org/owlapi/ontologieRegistre#Kaposi_sarcomas"));
				   
				    System.out.println(reasoner.getEquivalentClasses(vegPizza));
				   
				    
				   // TROUVER METHODE POUR REUTILISER LES RESULTAT DE LA DL QUERY ! NOTAMMENT SES PROPRIETE ET RESTRICTION
				    
			        // We want to examine the restrictions on margherita pizza. To do this,
			        // we need to obtain a reference to the margherita pizza class. In this
			        // case, we know the URI for margherita pizza (it happens to be the
			        // ontology URI - the base URI plus #Margherita - note that this isn't
			        // always the case. A class may have a URI that bears no resemblance to
			        // the ontology URI which contains axioms about the class).
			        IRI kaposi = IRI.create(ont.getOntologyID().getOntologyIRI()
			                + "#Kaposi_sarcomas");
			        OWLClass margheritaPizza1 = man.getOWLDataFactory()
			                .getOWLClass(IRI
					                .create("http://www.semanticweb.org/owlapi/ontologieRegistre#Kaposi_sarcomas"));
			      
			        OWLClass american=dataFactory.getOWLClass(":Kaposi_sarcomas", pm);
			        Set<OWLClassAxiom> tempAx=ont.getAxioms(american);
			        for(OWLClassAxiom ax: tempAx){
			            for(OWLClassExpression nce:ax.getNestedClassExpressions())
			                if(nce.getClassExpressionType()!=ClassExpressionType.OWL_CLASS)
			                    System.out.println(ax);
			        }
				    
				  
				    
						       			        
			        
			        
			        
			        
			        
				    
				    
				    // After you are done with the query, you should remove the definition
				    man.removeAxiom(ont, definition);
				    
				    
				    NodeSet<OWLClass> newname= reasoner.getSuperClasses(newName, true);
				    
				    
				    Set<OWLClass> clses = newname.getFlattened();
			        System.out.println("Subclasses of vegetarian: ");
			        for (OWLClass cls : clses) {
			        
			            System.out.println("    " + cls);
			        }
				  // for (Node<OWLClass> a : newname ) {System.out.println(a);}
				    // You can now add new definitions for new queries in the same way

				    // After you are done with all queries, do not forget to free the
				    // resources occupied by the reasoner
				    reasoner.dispose();
				    

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
*/////////////////////////////////////////////////////////////////////////////////////////
		  OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
		    OWLReasoner reasoner = reasonerFactory.createReasoner(ont);
		    
		    
		    
		    /*
		 
		 topo.add("m10");
		 topo.add("m14");
		 Set<OWLClassExpression> morpho = new HashSet <OWLClassExpression>();
		 for (String morphol:topo)
		 {	
			 
			
				 int compte = 1;
				 String i =Integer.toString(compte);
				 OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
						 dataFactory.getOWLClass(":t52",pm));
				
			  
		
			    man.addAxiom(ont, dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":D"+i,pm), dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),
			    		dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
						 dataFactory.getOWLClass(":"+morphol,pm)),avoirunetopo )));
			    dg.add(dataFactory.getOWLClass(":D"+i,pm));
				 compte = compte + 1;
				 
				 dg.getClass();
				 aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),dataFactory.getOWLClass(":D"+i,pm)));
				 
			 }
		 */
		    
		    // creation classes coresspondant aux diagnostics du patient
		    
			OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
					 dataFactory.getOWLClass(":m5",pm));
			OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
					 dataFactory.getOWLClass(":t1",pm));
			OWLClassExpression query  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );
				 
		    OWLClass newName = dataFactory.getOWLClass(":D1",pm);
		    
		
		    OWLAxiom definition = dataFactory.getOWLEquivalentClassesAxiom(newName, query);
		    man.addAxiom(ont, definition);
		   
			OWLClassExpression avoirunemorpho1 = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
		 					 dataFactory.getOWLClass(":m2",pm));
		 			OWLClassExpression avoirunetopo1 = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
		 					 dataFactory.getOWLClass(":t1",pm));
		 			OWLClassExpression query1  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho1,avoirunetopo1 );
		 		
		 		    OWLClass newName1 = dataFactory.getOWLClass(":D2",pm);
		 		    
		
		 		    OWLAxiom definition1 = dataFactory.getOWLEquivalentClassesAxiom(newName1, query1);
		 		    man.addAxiom(ont, definition1);
		 
		 		   aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),newName1));
		 		  aprdg.add(dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),newName));
		 
	 // creation maladie avec diagnostics
		    
		   OWLClassExpression maladieDe = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":maladieDe", pm),
					 dataFactory.getOWLClass(":Patient",pm));
		   
	 		 OWLClassExpression aprdgs  = dataFactory.getOWLObjectIntersectionOf(aprdg);
	 		 OWLClassExpression query2  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Maladie", pm),aprdgs,maladieDe);
		
		 OWLClass M1 = dataFactory.getOWLClass(":A1",pm);
		 OWLAxiom definition2 = dataFactory.getOWLEquivalentClassesAxiom (M1, query2);
		man.addAxiom(ont, definition2);
		
			//File enregistrement = new File("C:/Users/hadri/Desktop/testraisonneur/OR1.owl");
		File enregistrement = new File("./ressource/OR1.owl");
			man.saveOntology(ont,IRI.create(enregistrement.toURI()));

			// on lance le raisonneur 
		   reasoner.flush();
		   System.out.println(reasoner.getSuperClasses(M1, true));
		   
		   System.out.println(reasoner.getEquivalentClasses(M1));
		   
		  

	/*
			
	
			
			
			// RAISONNEMENT SUR LONTOLOGIE : ON CREE UN AXIOM PUIS ON RAISONNE SUR CET AXIOM
		    // Create an ELK reasoner.
		    
		    
		    // Create your desired query class expression. 			
			OWLClassExpression avoirunemorpho = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
					 dataFactory.getOWLClass(":m10",pm));
			
			OWLClassExpression avoirunetopo = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
					 dataFactory.getOWLClass(":t52",pm));
			OWLClassExpression query  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho,avoirunetopo );
		
		    // Create a fresh name for the query.
		    OWLClass newName = dataFactory.getOWLClass(":D1",pm);
		    
		    // Make the query equivalent to the fresh class
		    OWLAxiom definition = dataFactory.getOWLEquivalentClassesAxiom(newName, query);
		    man.addAxiom(ont, definition);
		   
		 
		 // Create your desired query class expression. 			
		 			OWLClassExpression avoirunemorpho1 = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgMorpho", pm),
		 					 dataFactory.getOWLClass(":m14",pm));
		 			
		 			OWLClassExpression avoirunetopo1 = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDgTopo", pm),
		 					 dataFactory.getOWLClass(":t52",pm));
		 			OWLClassExpression query1  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Diagnostic", pm),avoirunemorpho1,avoirunetopo1 );
		 		
		 		    // Create a fresh name for the query.
		 		    OWLClass newName1 = dataFactory.getOWLClass(":D2",pm);
		 		    
		 		    // Make the query equivalent to the fresh class
		 		    OWLAxiom definition1 = dataFactory.getOWLEquivalentClassesAxiom(newName1, query1);
		 		    man.addAxiom(ont, definition1);
		 
		 		    
		 		   OWLClassExpression maladieDe = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":maladieDe", pm),
		 					 dataFactory.getOWLClass(":Patient",pm));
		 		    
		 		    
		 		    
		 		   OWLClassExpression apourdg = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),newName);
		 		  OWLClassExpression apourdg1 = dataFactory.getOWLObjectSomeValuesFrom(dataFactory.getOWLObjectProperty(":aPourDg", pm),newName1);
		 		  
		 		 OWLClassExpression query2  = dataFactory.getOWLObjectIntersectionOf(dataFactory.getOWLClass(":Maladie", pm),apourdg,apourdg1,maladieDe);
		 		  
		 		 OWLClass M1 = dataFactory.getOWLClass(":M1",pm);
		 		 OWLAxiom definition2 = dataFactory.getOWLEquivalentClassesAxiom (M1, query2);
		 		man.addAxiom(ont, definition2);
		 		
		 			File enregistrement = new File("C:/Users/hadri/Desktop/testraisonneur/OR1.owl");
		 			man.saveOntology(ont,IRI.create(enregistrement.toURI()));
		 
		 		   reasoner.flush();

				    
				    System.out.println(reasoner.getSuperClasses(M1, true));
				   
				    System.out.println(reasoner.getEquivalentClasses(M1));
*/
	}

}
