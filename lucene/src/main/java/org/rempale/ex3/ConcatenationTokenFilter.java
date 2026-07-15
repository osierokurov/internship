package org.rempale.ex3;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public final class ConcatenationTokenFilter extends TokenFilter {

    private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAttr = addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute posIncAttr =
            addAttribute(PositionIncrementAttribute.class);

    private final String delimiter;

    private boolean emitted = false;
    private State endState;

    public ConcatenationTokenFilter(TokenStream input) {
        this(input, " ");
    }

    public ConcatenationTokenFilter(TokenStream input, String delimiter) {
        super(input);
        this.delimiter = delimiter;
    }

    @Override
    public boolean incrementToken() throws IOException {

        if (emitted) {
            return false;
        }

        StringBuilder builder = new StringBuilder();

        int startOffset = -1;
        int endOffset = -1;

        while (input.incrementToken()) {

            if (!builder.isEmpty()) {
                builder.append(delimiter);
            }

            builder.append(termAttr.toString());

            if (startOffset == -1) {
                startOffset = offsetAttr.startOffset();
            }

            endOffset = offsetAttr.endOffset();
        }

        endState = captureState();

        if (builder.isEmpty()) {
            return false;
        }

        clearAttributes();

        termAttr.append(builder);
        offsetAttr.setOffset(startOffset, endOffset);
        posIncAttr.setPositionIncrement(1);

        emitted = true;
        return true;
    }

    @Override
    public void end() throws IOException {
        super.end();
        if (endState != null) {
            restoreState(endState);
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        emitted = false;
        endState = null;
    }
}
