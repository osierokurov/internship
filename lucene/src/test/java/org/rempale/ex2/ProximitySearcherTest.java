package org.rempale.ex2;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rempale.ex2.core.ProximitySearcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProximitySearcherTest {

    private static Directory directory;
    private static DirectoryReader reader;
    private static ProximitySearcher searcher;

    @BeforeAll
    static void setUp() throws IOException {

        directory = new ByteBuffersDirectory();

        Analyzer analyzer = new WhitespaceAnalyzer();
        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(analyzer));

        addDocument(writer, "1", "quick brown fox");
        addDocument(writer, "2", "quick red fox");
        addDocument(writer, "3", "brown dog");
        addDocument(writer, "4", "quick brown rabbit");

        writer.close();

        reader = DirectoryReader.open(directory);
        searcher = new ProximitySearcher(new IndexSearcher(reader));
    }

    @AfterAll
    static void tearDown() throws IOException {
        reader.close();
        directory.close();
    }

    @Test
    void shouldFindDocumentsUsingPrefixQueryForSingleWord() throws IOException {

        List<String> result = searcher.search("qui", 0, 10);

        assertEquals(List.of("1", "2", "4"), result);
    }

    @Test
    void shouldFindDocumentsUsingSpanNearQueryForMultipleWords() throws IOException {

        List<String> result = searcher.search("quick bro", 0, 10);

        assertEquals(List.of("1", "4"), result);
    }

    private static void addDocument(IndexWriter writer, String id, String content)
            throws IOException {

        Document document = new Document();

        document.add(new StoredField("id", id));
        document.add(new TextField("content", content, Field.Store.NO));

        writer.addDocument(document);
    }
}