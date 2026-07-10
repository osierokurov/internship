package org.rempale.ex2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.rempale.ex1.IndexFiles;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    void main() throws Exception {

        Path pathToIndex = Paths.get("lucene/src/main/resources/lucence/queryparser/docs/text");
        Path pathToSaveIndex = Paths.get("lucene/src/main/resources/textIndex");
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        try (Directory dir = FSDirectory.open(pathToSaveIndex);
             IndexWriter writer = new IndexWriter(dir, config)) {

            IndexFiles indexer = new IndexFiles(pathToIndex, writer);
            indexer.indexDocs();

            IndexReader reader = DirectoryReader.open(FSDirectory.open(pathToSaveIndex));
            IndexSearcher searcher = new IndexSearcher(reader);
            ProximitySearcher proxSearcher = new ProximitySearcher(searcher);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Query + slop (or 'exit'): ");

                String line = scanner.nextLine();

                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                String[] parts = line.split(",", 2);

                if (parts.length != 2) {
                    System.out.println("Usage: <query> <slop>");
                    continue;
                }

                String query = parts[0].trim();
                int slop = Integer.parseInt(parts[1].trim());

               for (String fileName : proxSearcher.search(query, slop)) {
                   System.out.println(fileName.substring(fileName.lastIndexOf('/') + 1));
               }
            }
        }

        }
    }