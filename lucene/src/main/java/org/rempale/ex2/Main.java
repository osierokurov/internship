package org.rempale.ex2;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.rempale.ex2.core.IndexBuilder;
import org.rempale.ex2.core.ProximitySearcher;
import org.rempale.ex2.core.QueryInputParser;
import org.rempale.ex2.core.SearcherFactory;
import org.rempale.ex2.pojos.QueryInput;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final int MAX_RESULTS = 20;

    private final Scanner scanner = new Scanner(System.in);
    private final QueryInputParser parser = new QueryInputParser();
    private final IndexBuilder indexBuilder = new IndexBuilder(new StandardAnalyzer());
    private final SearcherFactory searcherFactory = new SearcherFactory();

    void main() throws Exception {

        Path source = Paths.get("lucene/src/main/resources/lucence/queryparser/docs/text");
        Path index = Paths.get("lucene/src/main/resources/textIndex");

        indexBuilder.build(source, index);

        ProximitySearcher searcher = searcherFactory.createProximitySearcher(index);

        while (true) {

            System.out.print("Query + slop (or '" + QueryInputParser.getExitKeyword() + "'): ");

            try {
                Optional<QueryInput> input = parser.parse(scanner.nextLine());

                if (input.isEmpty()) {
                    break;
                }

                QueryInput query = input.get();

                for (String fileName : searcher.search(
                        query.query(),
                        query.slop(),
                        MAX_RESULTS)) {

                    System.out.println(fileName.substring(fileName.lastIndexOf('/') + 1));
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}