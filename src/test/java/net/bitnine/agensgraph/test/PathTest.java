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

package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PathTest extends TestCase {

    private Connection con;
    private Statement st;

    public void setUp() throws Exception {
        con = TestUtil.openDB();
        con.setAutoCommit(true);
        st = con.createStatement();
        try {
            dropSchema();
        }
        catch (Exception ignored) {}
        st.execute("CREATE GRAPH u");
        st.execute("SET graph_path = u");
        st.execute("CREATE VLABEL company");
        st.execute("CREATE VLABEL person");
        st.execute("CREATE ELABEL employee");
        st.execute("CREATE ELABEL manage");
        create();
    }

    private void dropSchema() throws Exception {
        st.execute("DROP GRAPH u CASCADE");
    }

    private void create() throws Exception {
        st.execute("CREATE (:company {name: 'bitnine'})"
                + "-[:employee]->"
                + "(:person {name: 'kskim'})"
                + "-[:manage]->"
                + "(:person {name: 'ktlee'})");
        st.execute("MATCH (c:company {name: 'bitnine'}) "
                + "CREATE (c)-[:employee]->"
                + "(:person {name: 'jsyang'})");
        st.execute("MATCH (c:company {name: 'bitnine'}), "
                + "(p:person {name: 'ktlee'}) "
                + "CREATE (c)-[:employee]->(p)");
        st.execute("MATCH (m:person {name: 'kskim'}), "
                +"(p:person {name: 'jsyang'}) "
                +"CREATE (m)-[:manage]->(p)");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    private static String vnames[] = {"bitnine", "kskim", "ktlee"};
    private static String elabels[] = {"employee", "manage"};

    public void testPath() throws Exception {
        ResultSet rs = st.executeQuery("MATCH p=()-[]->()-[]->({name: 'ktlee'}) RETURN p");
        while (rs.next()) {
            Path p = (Path)rs.getObject("p");
            int i = 0;
            for (Vertex v : p.vertexs()) {
                assertEquals(vnames[i++], v.getString("name"));
            }
            i = 0;
            for (Edge e : p.edges()) {
                assertEquals(elabels[i++], e.getLabel());
            }
            assertEquals(2, p.length());
        }
        rs.close();
    }
}
