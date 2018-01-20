package isped.sitis.etl.identitoVigilance;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class Searcher {
	private IndexReader reader;
	private IndexSearcher searcher;
	private TopScoreDocCollector collector = TopScoreDocCollector.create(5);
	
	
	Searcher(String indexDir) {
	    try {
			reader = DirectoryReader.open( FSDirectory.open(new File(indexDir).toPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    searcher = new IndexSearcher(reader);
	}
	
	public ArrayList<Document> fuzzyQuery(String nom, String prenom, String sexe, String ddn) {
		Builder booleanQuery = new BooleanQuery.Builder();
		
		FuzzyQuery query1 = new FuzzyQuery(new Term("nom", nom));
		FuzzyQuery query2 = new FuzzyQuery(new Term("prenom", prenom));
		FuzzyQuery query3 = new FuzzyQuery(new Term("sexe", sexe));
		FuzzyQuery query4 = new FuzzyQuery(new Term("ddn", ddn));
		
		booleanQuery.add(query1, Occur.MUST);
		booleanQuery.add(query2, Occur.MUST);
		booleanQuery.add(query3, Occur.MUST);
		booleanQuery.add(query4, Occur.MUST);
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(10);
		
		ArrayList<Document> returnedDocs = new ArrayList<>();
		try {
			searcher.search(booleanQuery.build(),collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			for(int i=0;i<hits.length;++i) {
		          int docId = hits[i].doc;
		          Document d = searcher.doc(docId);
		          returnedDocs.add(d);
		          System.out.println((i + 1) + ". " + d.get("nom") + d.get("prenom") + d.get("sexe") + d.get("ddn") + " score=" + hits[i].score);
		        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnedDocs;
 		
	}
	
	public Optional<Document> exactQuery(String nom, String prenom, String sexe, String ddn) {
		Builder booleanQuery = new BooleanQuery.Builder();
		
		Query query1 = new TermQuery(new Term("nom", nom));
		Query query2 = new TermQuery(new Term("prenom", prenom));
		Query query3 = new TermQuery(new Term("sexe", sexe));
		Query query4 = new TermQuery(new Term("ddn", ddn));
		
		booleanQuery.add(query1, Occur.MUST);
		booleanQuery.add(query2, Occur.MUST);
		booleanQuery.add(query3, Occur.MUST);
		booleanQuery.add(query4, Occur.MUST);
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(5);
		
		ArrayList<Document> returnedDocs = new ArrayList<>();
		Document returnedDoc = null;
		try {
			searcher.search(booleanQuery.build(),collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			for(int i=0;i<hits.length;++i) {
		          int docId = hits[i].doc;
		          Document d = searcher.doc(docId);
		          returnedDocs.add(d);
		          System.out.println((i + 1) + ". " + d.get("nom") + d.get("prenom") + d.get("sexe") + d.get("ddn") + " score=" + hits[i].score);
		        }
			returnedDoc = returnedDocs.get(0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IndexOutOfBoundsException e) {
			System.out.println("Aucun match");
		}
		
		return Optional.ofNullable(returnedDoc);
	}
	
	public ArrayList<Document> exactQuery(String field, String value) {
		Builder booleanQuery = new BooleanQuery.Builder();
		
		Query query1 = new TermQuery(new Term(field, value));
		
		booleanQuery.add(query1, Occur.MUST);
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(1000000);
		
		ArrayList<Document> returnedDocs = new ArrayList<>();
		
		try {
			searcher.search(booleanQuery.build(),collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			for(int i=0;i<hits.length;++i) {
		          int docId = hits[i].doc;
		          Document d = searcher.doc(docId);
		          returnedDocs.add(d);
		          System.out.println((i + 1) + ". " + d.get("nom") + d.get("prenom") + d.get("sexe") + d.get("ddn") + " score=" + hits[i].score);
		        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IndexOutOfBoundsException e) {
			System.out.println("Aucun match");
		}
		
		return returnedDocs;
	}
	
}
