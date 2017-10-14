/*
 * Copyright (c) 2014-2017, Bitnine Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bitnine.agensgraph.graph;

import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class Path extends PGobject implements Serializable, Cloneable {
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;

    public Path() {
        setType("graphpath");
    }

    @Override
    public void setValue(String value) throws SQLException {
        ArrayList<String> tokens = tokenize(value);
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (i % 2 == 0) {
                Vertex v = new Vertex();
                v.setValue(tokens.get(i));
                vertices.add(v);
            } else {
                Edge e = new Edge();
                e.setValue(tokens.get(i));
                edges.add(e);
            }
        }
        this.vertices = vertices;
        this.edges = edges;

        super.setValue(value);
    }

    private ArrayList<String> tokenize(String value) throws SQLException {
        ArrayList<String> tokens = new ArrayList<>();

        // ignore wrapping '[' and ']' characters
        int pos = 1;
        int len = value.length() -1;

        int start = pos;
        int depth = 0;
        boolean gid = false;

        while (pos < len) {
            char c = value.charAt(pos);

            switch (c) {
                case '"':
                    if (depth > 0) {
                        // parse "string"
                        int i = pos + 1;
                        while (i > -1 && i < len) {
                            i = value.indexOf('"', i);
                            if (value.charAt(i - 1) != '\\')
                                break;
                        }
                        if (i > pos)
                            pos = i;

                        // leave pos unchanged if unmatched right " were found
                    }
                    break;
                case '[':
                    if (depth == 0)
                        gid = true;
                    break;
                case ']':
                    if (depth == 0)
                        gid = false;
                    break;
                case '{':
                    depth++;
                    break;
                case '}':
                    depth--;
                    if (depth < 0) {
                        throw new PSQLException("Parsing graphpath failed", PSQLState.DATA_ERROR);
                    }
                    break;
                case ',':
                    if (depth == 0 && !gid) {
                        tokens.add(value.substring(start, pos));
                        start = pos + 1;
                    }
                    break;
                default:
                    break;
            }

            pos++;
        }

        /* add the last token */
        tokens.add(value.substring(start, pos));

        return tokens;
    }

    public Iterable<Vertex> vertices() {
        return vertices;
    }

    public Iterable<Edge> edges() {
        return edges;
    }

    public int length() {
        return edges.size();
    }
}
