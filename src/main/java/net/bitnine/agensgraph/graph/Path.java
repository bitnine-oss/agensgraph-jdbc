/*
 * Copyright (c) 2014-2016, Bitnine Inc.
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

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.bitnine.agensgraph.util.TopCommaTokenizer;
import org.postgresql.util.GT;
import org.postgresql.util.PGobject;
import org.postgresql.util.PGtokenizer;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class Path
        extends PGobject
        implements Serializable, Closeable {

    private List<Vertex> vertexs;
    private List<Edge> edges;
    private String s;

    public Path() {
        setType("path");
    }

    @Override
    public void setValue(String s) throws SQLException {
        this.s = s;
        vertexs = new ArrayList<>();
        edges = new ArrayList<>();
        String p = PGtokenizer.removeBox(this.s);
        TopCommaTokenizer t;
        try {
            t = new TopCommaTokenizer(p);
        }
        catch (Exception e) {
            throw new PSQLException(GT.tr(
                        "Conversion to type {0} failed: {1}."
                        , new Object[]{type, s})
                    , PSQLState.DATA_TYPE_MISMATCH);
        }
        for (int i = 0; i < t.getSize(); ++i) {
            if (i % 2 == 0)
                vertexs.add(new Vertex(t.getToken(i)));
            else
                edges.add(new Edge(t.getToken(i)));
        }
    }

    @Override
    public String getValue() {
        return s;
    }

    @Override
    public void close() throws IOException {
    }

    public Vertex start() {
        return vertexs.get(0);
    }

    public Vertex end() {
        int size = vertexs.size();
        if (0 == size)
            return null;
        else
            return vertexs.get(size - 1);
    }

    public int length() {
        return edges.size();
    }

    public Iterable<Vertex> vertexs() {
        return vertexs;
    }

    public Iterable<Edge> edges() {
        return edges;
    }
}
