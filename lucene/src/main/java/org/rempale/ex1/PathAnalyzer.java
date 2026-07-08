package org.rempale.ex1;

import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.pattern.PatternTokenizer;

public class PathAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {

        Tokenizer tokenizer =
                new PatternTokenizer(Pattern.compile("[/.]+"), -1);

        TokenStream stream = new LowerCaseFilter(tokenizer);

        stream = new NGramTokenFilter(stream, 1, 10, true);

        return new TokenStreamComponents(tokenizer, stream);
    }
}