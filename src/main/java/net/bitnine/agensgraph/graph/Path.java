/*
 * Copyright (c) 2014-2018, Bitnine Inc.
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

/**
 * This class defines the type path.
 */
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
                        // Parse "string".
                        // Leave pos unchanged if unmatched right " were found.
                        boolean escape = false;
                        for (int i = pos + 1; i < len; i++) {
                            c = value.charAt(i);
                            if (c == '\\') {
                                escape = !escape;
                            } else if (c == '"') {
                                if (escape)
                                    escape = false;
                                else {
                                    pos = i;
                                    break;
                                }
                            } else {
                                escape = false;
                            }
                        }
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

    /**
     * Returns a set of the vertices.
     *
     * @return a set of the vertices.
     */
    public Iterable<Vertex> vertices() {
        return vertices;
    }

    /**
     * Returns a set of the edges.
     *
     * @return a set of the edges.
     */
    public Iterable<Edge> edges() {
        return edges;
    }

    /**
     * Returns the length of the edges.
     *
     * @return the length of the edges
     */
    public int length() {
        return edges.size();
    }
}
