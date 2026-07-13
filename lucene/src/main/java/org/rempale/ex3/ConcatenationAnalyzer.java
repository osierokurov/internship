package org.rempale.ex3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

public class ConcatenationAnalyzer extends Analyzer {

    private final CharArraySet stopWords;
    private final String delimiter;

    public ConcatenationAnalyzer(CharArraySet stopWords) {
        this(stopWords, " ");
    }

    public ConcatenationAnalyzer(CharArraySet stopWords, String delimiter) {
        this.stopWords = stopWords;
        this.delimiter = delimiter;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        Tokenizer tokenizer = new WhitespaceTokenizer();

        TokenStream stream = new StopFilter(tokenizer, stopWords);
        stream = new ConcatenationTokenFilter(stream, delimiter);

        return new TokenStreamComponents(tokenizer, stream);
    }
}