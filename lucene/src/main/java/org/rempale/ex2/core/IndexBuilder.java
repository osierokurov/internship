package org.rempale.ex2.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.rempale.ex1.IndexFiles;

import java.io.IOException;
import java.nio.file.Path;

public class IndexBuilder {

    private final Analyzer analyzer;

    public IndexBuilder(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void build(Path source, Path index) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try (Directory dir = FSDirectory.open(index);
             IndexWriter writer = new IndexWriter(dir, config)) {

            new IndexFiles(source, writer).indexDocs();
        }
    }
}
