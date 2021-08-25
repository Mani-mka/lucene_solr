package com.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class LuceneConstants {
    public static void main(String[] args) {
        try {
            // Path where you want indices to be stored
            Path indexPath = FileSystems.getDefault().getPath("D:\\Projects\\Java Projects\\index for documents");
            Directory dir = FSDirectory.open(indexPath);
            //File file = new File("D:\\Study Materials\\IntelliJ shortcuts.txt");
            //writeDocFromFile(dir, file);
            //writeDoc(dir);
            searchDoc(dir, "content", "commit");
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    public static void searchDoc(Directory dir, String field, String text){
        try {
            // Read from the directory that has indices and run query on it
            DirectoryReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            TermQuery tq = new TermQuery(new Term(field, text));
            TopDocs results = searcher.search(tq, 20);
            System.out.println("Match found in " + results.scoreDocs.length + " document/s");

            //print details of the matched documents
            for(ScoreDoc scoreDoc: results.scoreDocs){
                // System.out.println("document ID: " + scoreDoc.doc + "\n" + "Score: " + scoreDoc.score );
                Document doc = reader.document(scoreDoc.doc);
                String content = doc.get("content");
                System.out.println("Content of the document is: " + content);
            }
            // close the reader
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDoc(Directory dir){
        try {
            // perform analysis using standard analyzer
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig icw = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(dir, icw);

            // create a new document and index it
            Document doc = new Document();
            doc.add(new TextField("content", "I was there", Field.Store.YES));
            doc.add(new StringField("students", "trust fund baby", Field.Store.YES));
            writer.addDocument(doc);

            // closes and commits the indexing operation
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDocFromFile(Directory dir, File file){
        try {
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig icw = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(dir, icw);

            Document document = new Document();

            //index file contents
            Field contentField = new TextField("content",
                    new FileReader(file));

            //index file name
            Field fileNameField = new StringField("File Name",
                    file.getName(),
                    Field.Store.YES);

            //index file path
            Field filePathField = new StringField("File Path",
                    file.getCanonicalPath(), Field.Store.YES);

            document.add(contentField);
            document.add(fileNameField);
            document.add(filePathField);

            writer.addDocument(document);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
