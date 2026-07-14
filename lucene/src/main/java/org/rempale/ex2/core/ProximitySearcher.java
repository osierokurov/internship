package org.rempale.ex2.core;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queries.spans.SpanTermQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProximitySearcher {

    private final IndexSearcher searcher;

    public ProximitySearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public List<String> search(String text, int slop, int max_results) throws IOException {

        String[] words = text.toLowerCase().split("\\s+");

        Query query;

        if (isSingleWordQuery(words)) {
            query = new PrefixQuery(new Term("content", words[0]));
        } else {
            SpanQuery[] clauses = new SpanQuery[words.length];

            for (int i = 0; i < words.length - 1; i++) {
                clauses[i] = new SpanTermQuery(new Term("content", words[i]));
            }

            clauses[words.length - 1] =
                    new SpanMultiTermQueryWrapper<>(
                            new PrefixQuery(
                                    new Term("content", words[words.length - 1])
                            )
                    );
            query = new SpanNearQuery(
                    clauses,
                    slop,
                    true      // ordered
            );

        }

        TopDocs hits = searcher.search(query, max_results);

        List<String> result = new ArrayList<>();

        StoredFields storedFields = searcher.storedFields();

        for (ScoreDoc hit : hits.scoreDocs) {
            Document doc = storedFields.document(hit.doc);
            result.add(doc.get("id"));
        }

        return result;
    }

    private boolean isSingleWordQuery(String[] words) {
        return words.length == 1;
    }
}