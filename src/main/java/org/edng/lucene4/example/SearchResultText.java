package org.edng.lucene4.example;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * Created by ed on 1/30/15.
 */
public class SearchResultText {

    // we're going to index these 'documents'
    public static String[] contents = {
        "Humpty Dumpty sat on a wall,",
        "Humpty Dumpty had a great fall.",
        "All the king's horses and all the king's men",
        "Couldn't put Humpty together again."};

    public static void run(String queryString, int retrievedResults) throws Exception {

        // setup stuff for writing into index...
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document doc = new Document();
        TextField textField = new TextField("content", "", Field.Store.YES);

        // write documents into index
        for (String content : contents) {
            textField.setStringValue(content);
            doc.removeField("content");
            doc.add(textField);
            indexWriter.addDocument(doc);
        }

        indexWriter.commit();

        // prepare objects needed for querying the index...
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("content", analyzer);
        Query query = queryParser.parse(queryString);

        // return query results, sorted by score
        TopDocs topDocs = indexSearcher.search(query, retrievedResults);
        System.out.println("Total hits: " + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            doc = indexReader.document(scoreDoc.doc);
            System.out.println(scoreDoc.score + ": " + doc.getField("content").stringValue());
        }
    }
}
