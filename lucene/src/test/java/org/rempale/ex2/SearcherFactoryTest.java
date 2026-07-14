package org.rempale.ex2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.rempale.ex2.core.IndexBuilder;
import org.rempale.ex2.core.ProximitySearcher;
import org.rempale.ex2.core.SearcherFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearcherFactoryTest {

    @TempDir
    static Path tempDir;

    static private SearcherFactory searcherFactory;
    static private Path index;

    @BeforeAll
    static void setUp() throws IOException {
        searcherFactory = new SearcherFactory();
        index = tempDir.resolve("index");

        Path source = tempDir.resolve("source");
        Files.createDirectories(source);
        Files.writeString(source.resolve("document.txt"), "Hello Lucene");

        new IndexBuilder(new StandardAnalyzer()).build(source, index);
    }

    @Test
    void createProximitySearcher_validIndex_returnsProximitySearcher() throws IOException {
        ProximitySearcher searcher = searcherFactory.createProximitySearcher(index);

        assertNotNull(searcher);
    }

    @Test
    void createProximitySearcher_nonExistingIndex_throwsIOException() {
        Path invalidIndex = tempDir.resolve("missing-index");

        assertThrows(IOException.class,
                () -> searcherFactory.createProximitySearcher(invalidIndex));
    }
}