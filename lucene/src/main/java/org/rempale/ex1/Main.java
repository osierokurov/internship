package org.rempale.ex1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    static void main(String[] args) throws Exception {
        if (args.length == 0 || args.length > 2) {
            return;
        }
        Path pathToIndex = Paths.get("lucene/src/main/resources/lucence/queryparser/docs/xml/img");
        Path pathToSaveIndex = Paths.get("lucene/src/main/resources/index");
        Analyzer analyzer = new PathAnalyzer();
        Analyzer inputAnalyzer = new InputAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try (Directory dir = FSDirectory.open(pathToSaveIndex);
             IndexWriter writer = new IndexWriter(dir, config)) {

            IndexFiles indexer = new IndexFiles(pathToIndex, writer);
            indexer.indexDocs();

            IndexReader reader = DirectoryReader.open(FSDirectory.open(pathToSaveIndex));
            IndexSearcher searcher = new IndexSearcher(reader);

            MultiFieldQueryParser parser = new MultiFieldQueryParser(
                    new String[] { "path", "abbreviation" },
                    inputAnalyzer
            );
            Query query = parser.parse(QueryParser.escape(args[0]));

            TopDocs docs = searcher.search(query, 10);
            StoredFields storedFields = reader.storedFields();

            if (docs.scoreDocs.length > 0) {
                BigDecimal maxScore = BigDecimal.valueOf(docs.scoreDocs[0].score);

                for (ScoreDoc scoreDoc : docs.scoreDocs) {
                    BigDecimal score = BigDecimal.valueOf(scoreDoc.score);

                    if (score.compareTo(maxScore) != 0) {
                        break;
                    }

                    Document doc = storedFields.document(scoreDoc.doc);
                    System.out.println("Score: " + score);

                    for (IndexableField field : doc.getFields()) {
                        System.out.println(field.name() + " = " + field.stringValue());
                    }
                }
            }


//            Directory directory = FSDirectory.open(pathToSaveIndex);
//            try (DirectoryReader docReader = DirectoryReader.open(directory)) {
//
//                StoredFields storedFields = docReader.storedFields();
//
//                for (int docId = 0; docId < docReader.maxDoc(); docId++) {
//                    Document doc = storedFields.document(docId);
//
//                    System.out.println("Document " + docId);
//
//                    for (IndexableField field : doc.getFields()) {
//                        System.out.println(field.name() + " = " + field.stringValue());
//                    }
//                }
//            }
        }
    }
}
