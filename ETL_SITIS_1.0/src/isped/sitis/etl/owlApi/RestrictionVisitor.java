package isped.sitis.etl.owlApi;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {
	
	
	
	static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	 static OWLDataFactory dataFactory = OWLManager.getOWLDataFactory();
    private final Set<OWLClass> processedClasses;
    private final Set<OWLObjectPropertyExpression> restrictedProperties;
    private final Set<OWLOntology> onts;

    public RestrictionVisitor(Set<OWLOntology> onts) {
        restrictedProperties = new HashSet<OWLObjectPropertyExpression>();
        processedClasses = new HashSet<OWLClass>();
        this.onts = onts;
    }

    public Set<OWLObjectPropertyExpression> getRestrictedProperties() {
        return restrictedProperties;
    }

    @Override
    public void visit(OWLClass desc) {
        if (!processedClasses.contains(desc)) {
            // If we are processing inherited restrictions then we
            // recursively visit named supers. Note that we need to keep
            // track of the classes that we have processed so that we don't
            // get caught out by cycles in the taxonomy
            processedClasses.add(desc);
            for (OWLOntology ont : onts) {
                for (OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(desc)) {
                    ax.getSuperClass().accept(this);
                }
            }
        }
    }

    @Override
    public void visit(OWLObjectSomeValuesFrom desc) {
        // This method gets called when a class expression is an existential
        // (someValuesFrom) restriction and it asks us to visit it
        restrictedProperties.add(desc.getProperty());
    }
}