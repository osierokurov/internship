package org.rempale.ex3;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ConcatenationAnalyzerTest {

    private static ConcatenationAnalyzer analyzer;

    @BeforeAll
    static void setUp() {
        CharArraySet stopWords = new CharArraySet(Arrays.asList("the", "is"), true);

        analyzer = new ConcatenationAnalyzer(stopWords);
    }

    @AfterAll
    static void tearDown() {
        analyzer.close();
    }

    @Test
    void shouldRemoveStopWordsAndProduceSingleToken() throws IOException {

        TokenStream stream = analyzer.tokenStream("field", "the quick brown fox is fast");

        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);

        stream.reset();

        assertTrue(stream.incrementToken());
        assertEquals("quick brown fox fast", term.toString());
        assertFalse(stream.incrementToken());

        stream.end();
        stream.close();
    }
}