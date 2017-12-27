package isped.sitis.etl.identitoVigilance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

	// private static EnglishAnalyzer analyzer = new
	// EnglishAnalyzer(Version.LUCENE_40, EnglishAnalyzer.getDefaultStopSet());
	private static FrenchAnalyzer analyzer = new FrenchAnalyzer(FrenchAnalyzer.getDefaultStopSet());
	private static IndexWriter writer;
	private static ArrayList<Document> queue;
	

	Indexer(String indexDir) throws IOException {
		
		FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, config);
	}

	
	public static void indexDocument(Document doc) throws IOException {
		writer.addDocument(doc);		
	}

	

	
	public void closeIndex() throws IOException {
		writer.close();
	}

}
