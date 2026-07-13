package org.rempale.ex2.core;

import org.rempale.ex2.pojos.QueryInput;

import java.util.Optional;

public class QueryInputParser {

    private static final String EXIT_KEYWORD = "exit";
    private static final String QUERY_DELIMITER = ",";

    public Optional<QueryInput> parse(String line) throws IllegalArgumentException {

        line = line.trim();

        if (EXIT_KEYWORD.equalsIgnoreCase(line)) {
            return Optional.empty();
        }

        String[] parts = line.split(QUERY_DELIMITER, 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Usage: <query>" + QUERY_DELIMITER + "<slop>");
        }

        try {
            int slop = Integer.parseInt(parts[1].trim());
            return Optional.of(new QueryInput(parts[0].trim(), slop));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Slop must be an integer.", e);
        }
    }

    public static String getExitKeyword() {
        return EXIT_KEYWORD;
    }

    public static String getQueryDelimiter() {
        return QUERY_DELIMITER;
    }
}