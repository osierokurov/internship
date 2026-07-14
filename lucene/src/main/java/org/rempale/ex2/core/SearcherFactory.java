package org.rempale.ex2.core;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

public class SearcherFactory {

    public ProximitySearcher createProximitySearcher(Path index) throws IOException {
        Directory dir = FSDirectory.open(index);
        IndexReader reader = DirectoryReader.open(dir);

        return new ProximitySearcher(new IndexSearcher(reader));
    }
}
