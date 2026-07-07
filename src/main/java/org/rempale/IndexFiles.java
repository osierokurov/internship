/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rempale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;

/**
 * Index all text files under a directory.
 *
 */
public class IndexFiles {
    private final Path pathToIndex;
    private final IndexWriter indexWriter;

    public IndexFiles(Path pathToIndex, IndexWriter writer) {
        this.indexWriter = writer;
        this.pathToIndex = pathToIndex;
    }

    /**
     * Indexes the given file using the given writer, or if a directory is given, recurses over files
     * and directories found under the given directory.
     *
     * <p>NOTE: This method indexes one document per input file. This is slow. For good throughput,
     * put multiple documents into your input file(s). An example of this is in the benchmark module,
     * which can create "line doc" files, one document per line, using the <a
     * href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     * >WriteLineDocTask</a>.
     *
     * @throws IOException If there is a low-level I/O error
     */
    void indexDocs() throws IOException {
        if (Files.isDirectory(pathToIndex)) {
            Files.walkFileTree(
                    pathToIndex,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                            try {
                                indexDoc(file);
                            } catch (
                                    @SuppressWarnings("unused")
                                    IOException ignore) {
                                ignore.printStackTrace(System.err);
                                // don't index files that can't be read.
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
        } else {
            indexDoc(pathToIndex);
        }
    }

    /** Indexes or updates a single document */
    private void indexDoc(Path file) throws IOException {

        Document doc = new Document();

        String id = file.normalize().toString();

        // Unique identifier (not analyzed)
        doc.add(new StringField("id", id, Field.Store.YES));
        // Searchable path
        doc.add(new TextField("path", id, Field.Store.NO));

        doc.add(new TextField(
                "abbreviation",
                abbreviation(id),
                Field.Store.NO
        ));

        // Update existing document or add a new one
        indexWriter.updateDocument(new Term("id", id), doc);
        indexWriter.commit();
    }

    private String abbreviation(String file) {
        StringBuilder sb = new StringBuilder();

        for (String part : file.split("[/.]")) {
            if (!part.isBlank()) {
                sb.append(Character.toLowerCase(part.charAt(0)));
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}