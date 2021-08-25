package com.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class LuceneConstants {
    public static void main(String[] args) {
        try {
            Path indexPath = FileSystems.getDefault().getPath("D:\\Projects\\Java Projects\\index");
            Directory dir = FSDirectory.open(indexPath);
            writeDoc(dir);
            searchDoc(dir, "content", "there");
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    public static void searchDoc(Directory dir, String field, String text){
        try {
            DirectoryReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            TermQuery tq = new TermQuery(new Term(field, text));
            TopDocs results = searcher.search(tq, 20);
            System.out.println("Match found in " + results.scoreDocs.length + " documents");
            for(ScoreDoc scoreDoc: results.scoreDocs){
                // System.out.println("document ID: " + scoreDoc.doc + "\n" + "Score: " + scoreDoc.score );
                Document doc = reader.document(scoreDoc.doc);
                String content = doc.get("content");
                System.out.println("Content of the document is: " + content);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDoc(Directory dir){
        try {
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig icw = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(dir, icw);
            Document doc = new Document();
            doc.add(new TextField("content", "I was there", Field.Store.YES));
            doc.add(new StringField("students", "trust fund baby", Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
