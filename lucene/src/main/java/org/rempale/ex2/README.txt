Exercise 2. Proximity search

As a search developer, I would like to implement proximity search in text files, i.e. for a given string S containing
only words (sequences of lowercase English letters) and whitespaces and a non-negative integer number N,
I'd like to match only those files that contain all words from S within overall edit distance no more than N.

    Input

List of key-value pairs with file names and their content separated by the equals sign.

List of (string S, edit distance threshold N) pairs.

    Output

File names of matched files for each string and an edit distance pair.

    Test input

file1="to be or not to be that is the question"

file2="make a long story short"

file3="see eye to eye"

"to be not" 1

"to or to" 1

"to" 1

"long story short" 0

"long short" 0

"long short" 1

"story long" 1

"story long" 2

    Test output

"to be not"            1 - [file1]         //since overall edit distance will be 1

"to or to"               1 - []               //file1 was not matched since overall edit distance is 3

"to"                       1 - [file1, file3]

"long story short" 0 - [file2]

"long short"          0 - []

"long short"          1 - [file2]

"story long"          1 - []

"story long"          2 - [file2]