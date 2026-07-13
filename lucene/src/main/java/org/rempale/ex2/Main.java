package org.rempale.ex2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;
import org.rempale.ex1.IndexFiles;

public class Main {
    private final int MAX_RESULTS = 20;
    private final Scanner scanner = new Scanner(System.in);

    void main() throws Exception {

        Path pathToIndex = Paths.get("lucene/src/main/resources/lucence/queryparser/docs/text");
        Path pathToSaveIndex = Paths.get("lucene/src/main/resources/textIndex");

        buildIndex(pathToIndex, pathToSaveIndex);

        ProximitySearcher proxSearcher = createSearcher(pathToSaveIndex);

        while (true) {
            System.out.print("Query + slop (or 'exit'): ");

            Optional<QueryInput> input = nextQuery();
            if (input.isEmpty()) {
                break;
            }

            QueryInput queryInput = input.get();

            for (String fileName : proxSearcher.search(
                    queryInput.query(),
                    queryInput.slop(),
                    MAX_RESULTS)) {

                System.out.println(fileName.substring(fileName.lastIndexOf('/') + 1));
            }
        }

    }

    private void buildIndex(Path pathToIndex, Path pathToSaveIndex) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try (Directory dir = FSDirectory.open(pathToSaveIndex);
             IndexWriter writer = new IndexWriter(dir, config)) {

            new IndexFiles(pathToIndex, writer).indexDocs();
        }
    }

    private ProximitySearcher createSearcher(Path pathToSaveIndex) throws IOException {
        Directory dir = FSDirectory.open(pathToSaveIndex);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        return new ProximitySearcher(searcher);
    }

    private Optional<QueryInput> nextQuery() {
        while (true) {
            String line = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(line)) {
                return Optional.empty();
            }

            String[] parts = line.split(",", 2);
            if (parts.length != 2) {
                System.out.println("Usage: <query>,<slop>");
                continue;
            }

            try {
                int slop = Integer.parseInt(parts[1].trim());
                return Optional.of(new QueryInput(parts[0].trim(), slop));
            } catch (NumberFormatException e) {
                System.out.println("Slop must be an integer.");
            }
        }
    }
    }