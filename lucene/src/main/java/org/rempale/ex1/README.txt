Exercise 1

As a search developer, I would like to implement a "Github like(opens in a new tab)" file finder. Basically,
I would like to have all subsequences of a document name to give a match, e.g.:

lqdocsplgi => lucene/queryparser/docs/xml/img/plus.gif

lqd///gif => lucene/queryparser/docs/xml/img/join.gif

minusbottom.gif => lucene/queryparser/docs/xml/img/minusbottom.gif

    Input:

A set of documents with one field that contains a fully-qualified path to a file.

A set of user queries.

    Output:

An index schema with a correct analyzer chain.

A list of answers for every user’s query.

    Test Data:

Strings to index:

lucene/queryparser/docs/xml/img/plus.gif

lucene/queryparser/docs/xml/img/join.gif

lucene/queryparser/docs/xml/img/minusbottom.gif

User queries:

lqdocspg

    lucene/queryparser/docs/xml/img/plus.gif

lqd///gif

    lucene/queryparser/docs/xml/img/join.gif

    lucene/queryparser/docs/xml/img/plus.gif

    lucene/queryparser/docs/xml/img/minusbottom.gif

minusbottom.gif

    lucene/queryparser/docs/xml/img/minusbottom.gif