package isped.sitis.etl.identitoVigilance;



import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexNotFoundException;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.mysql.jdbc.Connection;

import isped.sitis.etl.util.JdbcConnection;

public class Gestion {
	
	public static void main(String[] args) throws IndexNotFoundException {
		Indexer indexer = null;
		String indexDir ="/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/indexFinal";
		String tmpIndexDir ="/Users/pierreo/Documents/Cours/COURS VIANNEY/projet_V5-2017/indexIndividus";
		try {
			indexer = new Indexer(tmpIndexDir);
			
			
			JdbcConnection jdbc = new JdbcConnection("cancerETL", "com.mysql.jdbc.Driver", "localhost:8889", "root", "root");
			jdbc.jdbcload();
			jdbc.jdbcConnect();
			
			 Stream<Record> sejourRecords = getRecordsFromSejour(jdbc, 1, 10000).parallelStream()
					 .map(x -> new SejourRecord(x));
			 
			 Stream<Record> anapathRecords = getRecordsFromAnapath(jdbc, 1, 10000).parallelStream()
						.map(x -> new AnapathRecord(x));
			
			Stream<Record> allRecords = Stream.concat(anapathRecords,sejourRecords);
			
			
			/*.filter(x -> x.getNom().equals("ELOSTE")).forEach(x -> System.out.println(x.toString()));*/
			
			/*.collect(Collectors.groupingBy(Record::getTraits))
			.entrySet().stream().map(e -> e.getKey()).filter(x -> x.contains("EMOSTE")).forEach(x-> System.out.println(x));
			System.out.println("finished iteration on concat");*/
			
			ArrayList<Document> documents = (ArrayList<Document>) allRecords
			.collect(Collectors.groupingBy(Record::getTraits))
			.entrySet().parallelStream()
			.map( e -> individuToDocument( e.getKey(), e.getValue() ) )
			.collect(Collectors.toList());
			
			
			documents.parallelStream().forEach(t -> {
				try {
					Indexer.indexDocument(t);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			indexer.closeIndex();
			System.out.println("finished indexing");
			
			final Searcher tmpSearcher = new Searcher(tmpIndexDir);
			final Searcher totalSearcher;
			totalSearcher = new Searcher(indexDir);

			documents.stream()
			.filter(x -> x.getField("nom").stringValue().equals("EMOSTE"))
			.forEach( x -> {
				System.out.println(x);
				//generate Stream and concatenate tehm to iter over all hits from both 
				//	tmpIndex and Index
				Stream<Document> tmpHits = tmpSearcher.fuzzyQuery(x.getField("nom").stringValue(), x.getField("prenom").stringValue(), x.getField("sexe").stringValue(), x.getField("ddn").stringValue())
														.stream();
				Stream totalHits = totalSearcher.fuzzyQuery(x.getField("nom").stringValue(), x.getField("prenom").stringValue(), x.getField("sexe").stringValue(), x.getField("ddn").stringValue())
												.stream();
				Stream<Document> allHits = Stream.concat(tmpHits.filter(d -> d.getField("nom").stringValue().equals(x.getField("nom").stringValue())
																			&& d.getField("prenom").stringValue().equals(x.getField("sexe").stringValue())
																			&& d.getField("sexe").stringValue().equals(x.getField("sexe").stringValue())
																			&& d.getField("ddn").stringValue().equals(x.getField("ddn").stringValue())
																			)
														,totalHits);
				allHits.forEach(h -> System.out.println(h.toString()));
				
				
			} );
			
			
			jdbc.jdbcClose();
			indexer.closeIndex();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	public static Document individuToDocument(String traits, List<Record> records) {
		String[] splitTraits = traits.split("\t");
		
		Document doc = new Document();
	    doc.add(new StringField("prenom", splitTraits[0], Field.Store.YES));
	    doc.add(new StringField("nom", splitTraits[1], Field.Store.YES));
	    doc.add(new StringField("sexe", splitTraits[2], Field.Store.YES));
	    doc.add(new StringField("ddn", splitTraits[3], Field.Store.YES));
	    
	    
	    records.stream()
	    		.filter(x -> x instanceof SejourRecord)
	    		.map(x -> (SejourRecord) x ) 
	    		.forEach(x -> {
				doc.add(new StringField("numeroRecordSejour", x.getNumAuto(),Field.Store.YES));
				doc.add(new StringField("cim10", x.getDp(),Field.Store.YES));
				
				try{ 
		    	    		doc.add(new StringField("cim10", x.getDr(),Field.Store.YES));
		    		} catch (Exception e) {}
	    		}
	    		);
	    
	    records.stream()
		.filter(x -> x instanceof AnapathRecord)
		.map(x -> (AnapathRecord) x ) 
		.forEach(x -> {
			doc.add(new StringField("numeroRecordAnapath", x.getNumAuto(),Field.Store.YES));
			doc.add(new StringField("adicap", x.getAdicap(),Field.Store.YES));
			}
		);
	    
	    return doc;
				
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
	
		
	
	public void processDocument(Document doc) {
		
	
	}
}