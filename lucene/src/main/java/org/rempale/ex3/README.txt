Exercise 3. Concatenation token filter

As a search developer, I would like to extend org.apache.lucene.analysis.TokenFilter to have a filter that concatenates
all tokens from a given token stream via a configured delimiter (" " by default).
Such a filter can be useful to solve the problem of indexing whole field values with stop words removal.

    Acceptance criteria

A token filter that concatenates all tokens is implemented.

A custom analyzer based on a whitespace tokenizer, a stop filter and a concatenation filter are created
(StopFilter and WhitespaceTokenizer from lucene-analyzers-common may be used).

A unit test that verifies that the implemented analyzer removes stop words and produces non-tokenized values is created