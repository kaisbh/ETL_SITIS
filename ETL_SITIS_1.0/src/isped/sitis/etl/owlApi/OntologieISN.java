package isped.sitis.etl.owlApi;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import isped.sitis.etl.owlApi.Pere;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.HasApplyChange;
import org.semanticweb.owlapi.model.IRI;
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
	
	
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	 OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
	 static Pere pere =new Pere(null, null);
	 static Set<Pere> peres=new HashSet<Pere>();
	
	 
	 
	 
	// ******* méthode ajout classe dans l'ontologie ******
	
	public static void ajouterclass (OWLOntologyManager manager,  
			OWLDataFactory dataFactory,OWLOntology ontologieRegistre,
			String className)
	
			throws OWLOntologyCreationException {
	
	
		OWLClass addclass  =dataFactory.getOWLClass(":"+className, pm);
		OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(addclass);
		manager.addAxiom(ontologieRegistre, declarationAxiom);
		
		System.out.println("Added OWLClass :" + addclass.toString());
		
	}
	
	
	
	// ******* méthode  ajout Classe + relation pere/fils ********
	
			// Création de la méthode
	
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
	
	public static void addPereFils ( String P, String F ){
		
		
		 pere=new Pere(P,F);
		 peres.add(pere);
		 
	}
	
	
	
	// ******* Méthode Création d'une relation (object properties) ********
	
	
	
	
	
	public static void AddObjectProperties (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, String ObjectProperties, String Domaine, String CoDomaine ) {
		
		OWLObjectProperty Objectproperties = dataFactory.getOWLObjectProperty(":"+ObjectProperties, pm);
		 OWLDeclarationAxiom declarationAxiom1 = dataFactory.getOWLDeclarationAxiom(Objectproperties);
		 manager.addAxiom(ontologieRegistre, declarationAxiom1 );
		 
		 OWLObjectPropertyDomainAxiom domainAxiom = dataFactory.getOWLObjectPropertyDomainAxiom(Objectproperties, dataFactory.getOWLClass(":"+Domaine, pm));
	     OWLObjectPropertyRangeAxiom rangeAxiom = dataFactory.getOWLObjectPropertyRangeAxiom( Objectproperties,  dataFactory.getOWLClass(":"+CoDomaine, pm));

	       manager.addAxiom(ontologieRegistre, domainAxiom);
	       manager.addAxiom(ontologieRegistre,  rangeAxiom);
	}
	
	
	
	// ***** Méthode Création d'une logique de description avec SOME
	
	
	public static void AddLogiqueDescriptionSOME (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, String superclass1, String objectproperty, String subClass1){
		
			OWLClassExpression LogiqueDescription = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+objectproperty, pm),
			 dataFactory.getOWLClass(":"+subClass1,pm));
			
			OWLSubClassOfAxiom axiome = dataFactory.getOWLSubClassOfAxiom(dataFactory.getOWLClass(":"+superclass1,pm),  LogiqueDescription);
			AddAxiom addAxiom1 = new AddAxiom(ontologieRegistre, axiome);
			manager.applyChange(addAxiom1);
					
	}
	

	// ***** Méthode Création d'une logique de description avec SOME  (EQUIVALENCE)
	
	public static void AddLogiqueDescriptionSOMEequivalence (OWLOntologyManager manager,OWLDataFactory dataFactory,
			OWLOntology ontologieRegistre, String superclass2, String objectproperty, String subClass2){
		
			OWLClassExpression LogiqueDescription = dataFactory.getOWLObjectSomeValuesFrom(
			dataFactory.getOWLObjectProperty(":"+objectproperty, pm),
			 dataFactory.getOWLClass(":"+subClass2,pm));
			
			OWLEquivalentClassesAxiom axiome = dataFactory.getOWLEquivalentClassesAxiom(dataFactory.getOWLClass(":"+superclass2,pm),  LogiqueDescription);
			AddAxiom addAxiom1 = new AddAxiom(ontologieRegistre, axiome);
			manager.applyChange(addAxiom1);
					
	}
	
	
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		// TODO Auto-generated method stub

		 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		 OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
		 OWLOntology ontologieRegistre = manager.createOntology(baseIRI); 
		 
		 
		 
		 /*     
		  *         ******* Exemple d'utilisation de chaque méthode ******
		  */
		  
		  
		
		 
		 
		 // Creation classe
		 
		 Set<String> classes=new HashSet<String>();
		 classes.add("Patient");
		 classes.add("Maladie");
		 classes.add("Diagnostic");
		 classes.add("Topo");
		 classes.add("Morpho");
		 
		 for (String c:classes) {
			 ajouterclass(manager, dataFactory, ontologieRegistre,c);
		 }
		 
		 
		 
		 
		 
		 // Creation  relation pere/fils (+classe) 
		 
		 
		 
		 addPereFils("Topo","Topo_langue");
		 addPereFils("Diagnostic","Dg_Carcinome_Langue");
		
		 
			
		 for(Pere p:peres) {
			 ajouterAxiomSubClass(manager, dataFactory, ontologieRegistre, p.getFils(), p.getPere());	 
		 }
		 
		
		 
		 // creation object property
		
		AddObjectProperties(manager, dataFactory, ontologieRegistre, "aPourDgMorpho", "Diagnostic", "Morpho");
		AddObjectProperties(manager, dataFactory, ontologieRegistre, "aPourDgMorpho", "Diagnostic", "Morpho");
		
		
		// creation logique de description avec SOME
		
		AddLogiqueDescriptionSOME(manager, dataFactory, ontologieRegistre, "Dg_Carcinome_Langue", "aPourDgTopo", "Topo_langue");
		 
		
		
		
		
				
	// **********  enregistement de  l'ontologie *************
		 
		File enregistrement = new File("C:/Users/ccecqa/Desktop/ont_registre.owl");
		manager.saveOntology(ontologieRegistre,IRI.create(enregistrement.toURI()));
		 
		 
	}
	

	
	
	
	
	
	
	
}
