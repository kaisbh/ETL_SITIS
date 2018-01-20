package isped.sitis.etl.identitoVigilance;



import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.mysql.jdbc.Connection;

import isped.sitis.etl.util.JdbcConnection;

public class Gestion {
	static boolean firstTime = true; //mettre true si premiere indexation, false sinon
	static Integer lastIndexValue = 0; // gade en memoire le dernier index , sorte de numAuto pour doc lucene, a sauver hors de l'app pour recupéré plus tard
	static String indexDir ="/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/indexFinal";
	//String tmpIndexDir ="/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/indexIndividus";

	static ArrayList<String> preExistingConflicts = new ArrayList<String>();
	static ArrayList<String> newConflicts = new ArrayList<String>();

	public static void main(String[] args) throws IndexNotFoundException {
		
		Stream<Record> allRecords = getRecordsFromDB();
		ArrayList<Document> documents = groupRecordsByTraitAndConvertIntoDocument(allRecords,lastIndexValue);
		
		if(firstTime) {
			addNewToEmptyIndex(indexDir, documents);
		}else {
			mergeExistingIndividualOrAddNewToIndex(indexDir, documents);
		}
			
		Map<String,List<String>> conflictMap =  FindConflicts(indexDir);
		/*ConcurrentHashMap<String,List<String>> testConflictMap = new ConcurrentHashMap<String,List<String>>();
		
		testConflictMap.put("11", Arrays.asList("4","12","10"));
		testConflictMap.put("12", Arrays.asList("3","4"));
		testConflictMap.put("3", Arrays.asList("12"));
		testConflictMap.put("4", Arrays.asList("10"));
		
		System.out.println(walkConflictMapforKey("11", testConflictMap));*/
		System.out.println(conflictMap);
		
	}
	
	public static Map<String,List<String>> FindConflicts(String indexDir) {
		// TODO attention si indexer ici et indexer pour ajout de doc en meme temps = erreurs ?
		
		Map<String,List<String>> ConflictMap = new ConcurrentHashMap<String,List<String>>();
		
		final Searcher searcher = new Searcher(indexDir);
		
		ConflictMap = searcher.exactQuery("maj","true") /*On récupère les documents maj avec exact query sur maj==true*/
			.parallelStream()
			.collect(Collectors.toMap( /*On créé une map=  id docMaj : [id fuzzy hit,id fuzzy hit,...] */
						  			(x -> x.getField("id").stringValue()) /* La cle est l'identifiant du document courrant*/
						  			,
						  			(x ->{					/* La valeur est la liste des id des hits fuzzy*/
						  					return searcher
						  						.fuzzyQuery(
						  								x.getField("nom").stringValue(),
						  								x.getField("prenom").stringValue(), 
						  								x.getField("sexe").stringValue(), 
						  								x.getField("ddn").stringValue()
								  						)
						  						.stream()
						  						.map(hit -> hit.getField("id").stringValue())
						  						.collect(Collectors.toList());
						  				}
						  			)
						  		)
				);

		return ConflictMap;
	}
	
	
	public static List<String> walkConflictMapforKey(String key, Map<String,List<String>> conflictMap){
		ArrayList<String> conflicts = new ArrayList<String>();
		recWalk(key, conflictMap, conflicts);
		return conflicts;
	}
	public static void  recWalk(final String key, Map<String,List<String>> conflictMap, ArrayList<String> conflicts) {
		if(conflicts.contains(key)) {
			return ;
		}
		conflicts.add(key);
		if(!conflictMap.containsKey(key)) {
			 return ;
		}
		for (int i = 0; i < conflictMap.get(key).size(); i++) {
			recWalk(conflictMap.get(key).get(i),conflictMap,conflicts);
		}
		return;
	}

	
	
	public static Document individuToDocument(String traits, List<Record> records, Integer docIndex) {
		String[] splitTraits = traits.split("\t");
		
		ArrayList<String> cim10 = new ArrayList<String>();
		ArrayList<String> adicap = new ArrayList<String>();

		Document doc = new Document();
	    doc.add(new StringField("id", String.valueOf(docIndex), Field.Store.YES));
	    doc.add(new StringField("prenom", splitTraits[0], Field.Store.YES));
	    doc.add(new StringField("nom", splitTraits[1], Field.Store.YES));
	    doc.add(new StringField("sexe", splitTraits[2], Field.Store.YES));
	    doc.add(new StringField("ddn", splitTraits[3], Field.Store.YES));
	    doc.add(new StringField("maj", "true", Field.Store.YES));
	    doc.add(new StringField("traite", "false", Field.Store.YES));

	    
	    
	    records.stream()
	    		.filter(x -> x instanceof SejourRecord)
	    		.map(x -> (SejourRecord) x ) 
	    		.forEach(x -> {
				doc.add(new StringField("numeroRecordSejour", x.getNumAuto(),Field.Store.YES));
				cim10.add(x.getDr());
				try{ 
    					cim10.add(x.getDp());
				}catch (Exception e) {}
	    			}
	    		);
	    cim10.stream().distinct().forEach(v -> doc.add(new StringField("cim10", v, Field.Store.YES)));
	    

	    /*records.stream()
		.filter(x -> x instanceof SejourRecord)
		.map(x -> (SejourRecord) x ) 
		.map(r -> {
					Set<String> set = new HashSet<String>();
					set.add(r.getDp());
					try{ 
	    	    				set.add(r.getDr());
					}catch (Exception e) {}
					return set;
				})
		.flatMap(x -> x.stream())
		.distinct()
		.forEach(v -> {
				doc.add(new StringField("cim10", v, Field.Store.YES));
				});*/
	    
	    records.stream()
		.filter(x -> x instanceof AnapathRecord)
		.map(x -> (AnapathRecord) x ) 
		.forEach(x -> {
			doc.add(new StringField("numeroRecordAnapath", x.getNumAuto(),Field.Store.YES));
			adicap.add(x.getAdicap());
			}
		);
	    adicap.stream().distinct().forEach(v -> doc.add(new StringField("adicap", v, Field.Store.YES)));

	    
	   /* records.stream()
		.filter(x -> x instanceof AnapathRecord)
		.map(x -> (AnapathRecord) x ) 
		.map(r -> {
					Set<String> set = new HashSet<String>();
					set.add(r.getAdicap());
					return set;
				})
		.flatMap(x -> x.stream())
		.distinct()
		.forEach(v -> {
				doc.add(new StringField("adicap", v, Field.Store.YES));
				});*/
	    
	    return doc;
				
	}
	
	public static Document mergeDocs (Document doc1, Document doc2, String docIndex) {	
		List<String> fieldsConserves = Arrays.asList("id", "maj","traite");
		List<String> codeFields = Arrays.asList("cim10", "adicap");

		Document mergedDoc = new Document();
		ArrayList<IndexableField> fields = new ArrayList<IndexableField>();
		
		doc1.getFields().stream()
		.filter(f -> !(fieldsConserves.contains(f.name())))
		.forEach(f -> fields.add((IndexableField) f));
		
		doc2.getFields().stream()
		.filter(f -> !(fieldsConserves.contains(f.name())))
		.forEach(f -> fields.add((IndexableField) f));
	  
		fields.stream()
		.collect(Collectors.groupingBy(IndexableField::name)).entrySet().stream() //cim10: field1,field2 , anapath:field1,field2 ...
		.forEach(e -> {	
						e.getValue().stream()			
						.map(f -> f.stringValue())		//field1 -> value of field1
						.distinct()
						.forEach(v -> mergedDoc.add(new StringField(e.getKey(), v, Field.Store.YES)));
						}
				);
		
		mergedDoc.add(new StringField("id", docIndex, Field.Store.YES));
		mergedDoc.add(new StringField("traite", doc2.get("traite"), Field.Store.YES));
		
		Set<String> existingCodes = doc2.getFields().stream()
				.filter(f -> (codeFields.contains(f.name())))
				.map(f -> f.stringValue())
				.collect(Collectors.toSet());
		
		Set<String> newCodes = mergedDoc.getFields().stream()
				.filter(f -> (codeFields.contains(f.name())))
				.map(f -> f.stringValue())
				.collect(Collectors.toSet());
		
		String maj = (existingCodes.equals(newCodes)) ? "false" : "true"; // on flag  comme maj que si les codes on changés
		mergedDoc.add(new StringField("maj", maj, Field.Store.YES));
		
		return mergedDoc;

		}
	
	public static Document sejourRecordToDocument(String record) {
		String[] traits = record.split("\t");
		String sexe = traits[0];
		String DDN = traits[1];
		String prenom = traits[2];
		String nom = traits[3];
		String NumAuto = traits[4];
		String DP = traits[5];

		Document doc = new Document();
	    doc.add(new StringField("sexe", sexe, Field.Store.YES));
	    doc.add(new StringField("ddn", DDN, Field.Store.YES));
	    doc.add(new StringField("prenom", prenom, Field.Store.YES));
	    doc.add(new StringField("nom", nom, Field.Store.YES));
	    doc.add(new StringField("numeroRecordSejour", NumAuto,Field.Store.YES));  // si on search pas dessus poura remplacer par storedfield
	    doc.add(new StringField("cim10", DP,Field.Store.YES));
	    String DR = ""; 
	    try{ 
	    		DR = traits[6];
	    	    doc.add(new StringField("cim10", DR,Field.Store.YES));
	    	} catch (ArrayIndexOutOfBoundsException e) {} // DR facultatif

	    // doc.add(new StringField("adicap", "",Field.Store.YES));
	   // doc.add(new StringField("numeroRecordAnapath", DR,Field.Store.YES)); pas besoin car on peu ajouter de nouveau field avec meme nom ce qui cré un ensemble de valeurs pour un meme field
		
	    return doc;
	}
	public static Document anapathRecordToDocument(String record) {
		String[] traits = record.split("\t");
		String sexe = traits[0];
		String DDN = traits[1];
		String prenom = traits[2];
		String nom = traits[3];
		String NumAuto = traits[4];
		String DP = traits[5];

		Document doc = new Document();
	    doc.add(new StringField("sexe", sexe, Field.Store.YES));
	    doc.add(new StringField("ddn", DDN, Field.Store.YES));
	    doc.add(new StringField("prenom", prenom, Field.Store.YES));
	    doc.add(new StringField("nom", nom, Field.Store.YES));
	    doc.add(new StringField("numeroRecordSejour", NumAuto,Field.Store.YES));  // si on search pas dessus poura remplacer par storedfield
	    doc.add(new StringField("cim10", DP,Field.Store.YES));
	    String DR = ""; 
	    try{ 
	    		DR = traits[6];
	    	    doc.add(new StringField("cim10", DR,Field.Store.YES));
	    	} catch (ArrayIndexOutOfBoundsException e) {} // DR facultatif

	    // doc.add(new StringField("adicap", "",Field.Store.YES));
	   // doc.add(new StringField("numeroRecordAnapath", DR,Field.Store.YES)); pas besoin car on peu ajouter de nouveau field avec meme nom ce qui cré un ensemble de valeurs pour un meme field
		
	    return doc;
	}
	public static ArrayList<String> getRecordsFromSejour(JdbcConnection jdbc, int NumAutoStart, int NumAutoStop) {
		ArrayList parameters = new ArrayList();
		Hashtable typeForColumnIndex = new Hashtable<Integer, String>();
		
		String sql = "select Sexe, DateNaissance, Prenom, Nom, NumAuto, DP, DR FROM TAB_sejour WHERE NumAuto BETWEEN ? AND ?";
		parameters.add(NumAutoStart);
		parameters.add(NumAutoStop);
		typeForColumnIndex.put(1, "Integer");typeForColumnIndex.put(1, "String");typeForColumnIndex.put(2, "String");typeForColumnIndex.put(3, "String");
		typeForColumnIndex.put(4, "String");typeForColumnIndex.put(5, "Integer");typeForColumnIndex.put(6, "String");typeForColumnIndex.put(7, "String");
		
		System.out.println(sql);
		return jdbc.requeteEnBase(sql,parameters ,typeForColumnIndex);
	
	}
	public static ArrayList<String> getRecordsFromAnapath(JdbcConnection jdbc, int NumAutoStart, int NumAutoStop) {
		ArrayList parameters = new ArrayList();
		Hashtable typeForColumnIndex = new Hashtable<Integer, String>();
		
		String sql = "SELECT Sexe, DateNaissance, Prenom, Nom, sej.NumAuto as NumAuto, NumAdicap"
				+ " FROM TAB_anapath sej INNER JOIN TAB_CODE_ANAPATH code ON sej.NumAuto = code.NumAnapath"
				+ " WHERE sej.NumAuto BETWEEN ? AND ?";
		parameters.add(NumAutoStart);
		parameters.add(NumAutoStop);
		typeForColumnIndex.put(1, "Integer");typeForColumnIndex.put(1, "String");typeForColumnIndex.put(2, "String");typeForColumnIndex.put(3, "String");
		typeForColumnIndex.put(4, "String");typeForColumnIndex.put(5, "Integer");typeForColumnIndex.put(6, "String");
		
		System.out.println(sql);
		return jdbc.requeteEnBase(sql,parameters ,typeForColumnIndex);
	
	}
	/*
	ResultSet rs = stmt.executeQuery(sql);
	while (rs.next()) {
	      
	    // ... repeat for each column in result set
	    writer.addDocument(doc);*/
	
	public static Stream<Record> getRecordsFromDB(){
		
		Stream<Record> allRecords;
		
		JdbcConnection jdbc = new JdbcConnection("cancerETL", "com.mysql.jdbc.Driver", "localhost:8889", "root", "root");
		jdbc.jdbcload();
		jdbc.jdbcConnect();
		
		 Stream<Record> sejourRecords = getRecordsFromSejour(jdbc, 1, 10000).parallelStream()
				 .map(x -> new SejourRecord(x));
		 
		 Stream<Record> anapathRecords = getRecordsFromAnapath(jdbc, 1, 10000).parallelStream()
					.map(x -> new AnapathRecord(x));
		
		allRecords = Stream.concat(anapathRecords,sejourRecords);
		
		jdbc.jdbcClose();
		return allRecords;
	}
	
	public static ArrayList<Document> groupRecordsByTraitAndConvertIntoDocument(Stream<Record> allRecords, Integer lastIndexValue){
		 /* Create object of AtomicInteger with initial value `0` */
	    AtomicInteger docIndex = new AtomicInteger(lastIndexValue);
		
		return (ArrayList<Document>) allRecords
		.collect(Collectors.groupingBy(Record::getTraits))
		.entrySet().parallelStream()
		.map( e -> individuToDocument( e.getKey(), e.getValue(), docIndex.getAndIncrement() ) )
		.collect(Collectors.toList());
	
	}
	public static void mergeExistingIndividualOrAddNewToIndex(String indexDir, ArrayList<Document> documents){
		try {
			final Indexer finalIndexer = new Indexer(indexDir);
			final Searcher totalSearcher = new Searcher(indexDir);
			documents.parallelStream()
			.filter(x -> x.getField("nom").stringValue().equals("ELOSTE"))// se limite aux patients EMOSTE pour testing
			.forEach( x -> {
				
				//-----------Recherche du hit exacte si individu existe deja dans DB ------------	
				//TODO check si recupere plus de 1 doc
				Optional<Document> exactHit = (Optional<Document>)totalSearcher.exactQuery(x.getField("nom").stringValue(), 
																	x.getField("prenom").stringValue(), 
																	x.getField("sexe").stringValue(), 
																	x.getField("ddn").stringValue()
																	);
				if(exactHit.isPresent()){
					
				
					String exactHitId = exactHit.get().getField("id").stringValue();
					//-----------------------------------------------------------------------------------
					
					//-----------Creation + MAJ d'un nouveau document : fusion du courrant (x) et de l'existant (exacthit)-----	
					Document mergedDoc = mergeDocs(x, exactHit.get(), exactHitId);
					try {
						finalIndexer.getWriter().updateDocument(new Term("id", exactHit.get().getField("id").stringValue()), mergedDoc);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					//Le mergedDoc remplace le document existant dans l'index, alors flaggé supprimé dans l'index (est alors skip lors recherche) 
					//-----------------------------------------------------------------------------------				
					System.out.println("Document"+ x +"Fusioné dans le document: "+ exactHit.get()+ "\n" + mergedDoc);	
					
				}else {
					try {
						finalIndexer.getWriter().addDocument(x);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Nouveau Document");	
					System.out.println(x);
				}
				
			} );
			finalIndexer.closeIndex();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	public static void addNewToEmptyIndex(String indexDir, ArrayList<Document> documents){
		try {
			final Indexer finalIndexer = new Indexer(indexDir);
			documents.parallelStream()
			//.filter(x -> x.getField("nom").stringValue().equals("ELOSTE"))// se limite aux patients EMOSTE pour testing
			.forEach( x -> {
						try {
							finalIndexer.getWriter().addDocument(x);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Nouveau Document");	
						System.out.println(x);
					});
			finalIndexer.closeIndex();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
		
}