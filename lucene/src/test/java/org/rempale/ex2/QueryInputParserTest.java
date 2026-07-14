package org.rempale.ex2;


import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rempale.ex2.core.QueryInputParser;
import org.rempale.ex2.pojos.QueryInput;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryInputParserTest {

    private static QueryInputParser parser;

    @BeforeAll
    static void setUp() {
        parser = new QueryInputParser();
    }

    @Test
    void parse_validQuery_returnsQueryInput() {
        Optional<QueryInput> result = parser.parse("hello world,2");

        assertTrue(result.isPresent());
        assertEquals("hello world", result.get().query());
        assertEquals(2, result.get().slop());
    }

    @Test
    void parse_queryWithWhitespace_returnsTrimmedQueryInput() {
        Optional<QueryInput> result = parser.parse("  hello world  ,  5  ");

        assertTrue(result.isPresent());
        assertEquals("hello world", result.get().query());
        assertEquals(5, result.get().slop());
    }

    @Test
    void parse_exitKeyword_returnsEmptyOptional() {
        Optional<QueryInput> result = parser.parse("exit");

        assertTrue(result.isEmpty());
    }

    @Test
    void parse_exitKeywordIgnoringCase_returnsEmptyOptional() {
        Optional<QueryInput> result = parser.parse("EXIt");

        assertTrue(result.isEmpty());
    }

    @Test
    void parse_missingDelimiter_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("hello world"));
    }

    @Test
    void parse_missingSlop_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("hello world,"));
    }

    @Test
    void parse_nonNumericSlop_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("hello world,two"));
    }

    @Test
    void getExitKeyword_noArguments_returnsExitKeyword() {
        assertEquals("exit", QueryInputParser.getExitKeyword());
    }

    @Test
    void getQueryDelimiter_noArguments_returnsQueryDelimiter() {
        assertEquals(",", QueryInputParser.getQueryDelimiter());
    }
}