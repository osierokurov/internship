package org.rempale.ex2;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.rempale.ex2.core.IndexBuilder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndexBuilderTest {

    @TempDir
    Path tempDir;

    @Test
    void build_validSourceDirectory_createsSearchIndex() throws IOException {
        Path source = tempDir.resolve("source");
        Path index = tempDir.resolve("index");

        Files.createDirectories(source);
        Files.writeString(source.resolve("document.txt"), "Hello Lucene!");

        IndexBuilder builder = new IndexBuilder(new StandardAnalyzer());

        builder.build(source, index);

        assertTrue(DirectoryReader.indexExists(FSDirectory.open(index)));
    }

    @Test
    void build_emptySourceDirectory_doesNotThrowException() throws IOException {
        Path source = tempDir.resolve("source");
        Path index = tempDir.resolve("index");

        Files.createDirectories(source);

        IndexBuilder builder = new IndexBuilder(new StandardAnalyzer());

        assertDoesNotThrow(() -> builder.build(source, index));
    }
}