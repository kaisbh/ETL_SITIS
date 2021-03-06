package isped.sitis.etl.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;

import isped.sitis.etl.util.FileUtil;

public class Indexer {

	private static IndexWriter writer;
	private static ArrayList<File> queue = new ArrayList<File>();

	// Constructeur
	Indexer(String indexDir, String analyzerLang) throws IOException {

		CharArraySet CharArraySetSW;
		IndexWriterConfig config;
		FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
		switch (analyzerLang) {
		// Choix de l'Analyse du corpus
		case "FR":
			CharArraySetSW = new CharArraySet(getStopWord("./resources/StopWord/stop-words-french.txt"), true);
			FrenchAnalyzer analyzerFR = new FrenchAnalyzer(CharArraySetSW);
			config = new IndexWriterConfig(analyzerFR);
			break;
		case "EN":
			CharArraySetSW = new CharArraySet(getStopWord("./resources/StopWord/stop-words-english1.txt"), true);
			// EnglishAnalyzer analyzerEN = new
			// EnglishAnalyzer(EnglishAnalyzer.getDefaultStopSet());
			EnglishAnalyzer analyzerEN = new EnglishAnalyzer(CharArraySetSW);
			config = new IndexWriterConfig(analyzerEN);
			break;
		default:
			StandardAnalyzer analyzerStd = new StandardAnalyzer();
			config = new IndexWriterConfig(analyzerStd);
			break;
		}

		writer = new IndexWriter(dir, config);
	}

	public static void CreateIndex(String indexLocation, String corpusLocation, String analyzerLang) {
		Indexer indexer = null;
		FileUtil.deleteFiles(indexLocation);
		try {
			indexer = new Indexer(indexLocation, "EN");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			indexer.indexFileOrDirectory(corpusLocation);
		} catch (Exception e) {
			System.out.println("Error indexing " + corpusLocation + " : " + e.getMessage());
		}
		try {
			indexer.closeIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Chemain de l'index");
		// String indexLocation = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s1 = br.readLine();
		System.out.println(
				"Enter the full path to add into the index (q=quit): (e.g. /home/ron/mydir or c:\\Users\\ron\\mydir)");
		System.out.println("[Acceptable file types: .xml, .html, .html, .txt]");
		String s2 = null;
		s2 = br.readLine();
		
		CreateIndex(s1, s2, "EN");

	}

	public static ArrayList<String> getStopWord(String FileName) {
		ArrayList<String> stop_words = new ArrayList<String>();
		try {
			BufferedReader is = new BufferedReader(new FileReader(FileName));
			String inputLine;
			while ((inputLine = is.readLine()) != null) {
				// stop_words.add(inputLine);
				// System.out.println(inputLine);
			}
		} catch (IOException io) {

		}
		return stop_words;
	}

	/**
	 * Indexes a file or directory
	 * 
	 * @param fileName
	 *            the name of a text file or a folder we wish to add to the index
	 * @throws java.io.IOException
	 *             when exception
	 */

	public void indexFileOrDirectory(String fileName) throws IOException {
		// ===================================================
		// gets the list of files in a folder (if user has submitted
		// the name of a folder) or gets a single file name (is user
		// has submitted only the file name)
		// ===================================================
		addFiles(new File(fileName));

		int originalNumDocs = writer.numDocs();
		for (File f : queue) {
			FileReader fr = null;
			try {
				Document doc = new Document();

				// ===================================================
				// add contents of file
				// ===================================================
				fr = new FileReader(f);
				doc.add(new TextField("contents", fr));
				doc.add(new StringField("path", f.getPath(), Field.Store.YES));
				doc.add(new StringField("filename", f.getName(), Field.Store.YES));

				writer.addDocument(doc);
				System.out.println("Added: " + f);
			} catch (Exception e) {
				System.out.println("Could not add: " + f);
			} finally {
				fr.close();
			}
		}

		int newNumDocs = writer.numDocs();
		System.out.println("");
		System.out.println("************************");
		System.out.println((newNumDocs - originalNumDocs) + " documents added.");
		System.out.println("************************");

		queue.clear();
	}

	

	public static void addFiles(File file) {

		if (!file.exists()) {
			System.out.println(file + " does not exist.");
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				addFiles(f);
			}
		} else {
			String filename = file.getName().toLowerCase();
			// ===================================================
			// Only index text files
			// ===================================================
			if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith(".xml")
					|| filename.endsWith(".txt")) {
				queue.add(file);
			} else {
				System.out.println("Skipped " + filename);
			}
		}
	}

	/**
	 * Close the index.
	 * 
	 * @throws java.io.IOException
	 *             when exception closing
	 */
	public void closeIndex() throws IOException {
		writer.close();
	}

}
