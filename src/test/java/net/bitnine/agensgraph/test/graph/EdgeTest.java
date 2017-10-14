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

package net.bitnine.agensgraph.test.graph;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.test.TestUtil;
import net.bitnine.agensgraph.util.Jsonb;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EdgeTest extends TestCase {
    private Connection conn;

    @Override
    public void setUp() throws Exception {
        conn = TestUtil.openDB();
        Statement stmt = conn.createStatement();
        stmt.execute("DROP GRAPH IF EXISTS t CASCADE");
        stmt.execute("CREATE GRAPH t");
        stmt.execute("SET graph_path = t");
        stmt.close();
    }

    @Override
    public void tearDown() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP GRAPH t CASCADE");
        stmt.close();
        TestUtil.closeDB(conn);
    }

    @Test
    public void testEdge() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("CREATE (n)-[r:e {s: '', l: 0, d: 0.0, f: false, t: true, z: null, a: [], o: {}}]->(m) RETURN n, r, m");
        assertTrue(rs.next());

        Vertex v0 = (Vertex) rs.getObject(1);
        Edge e = (Edge) rs.getObject(2);
        Vertex v1  = (Vertex) rs.getObject(3);

        assertEquals("e", e.getLabel());
        assertEquals(e.getStartVertexId(), v0.getVertexId());
        assertEquals(e.getEndVertexId(), v1.getVertexId());
        assertEquals("", e.getString("s"));
        assertEquals(0, e.getInt("l"));
        assertEquals(0, e.getLong("l"));
        assertEquals(0.0, e.getDouble("d"));
        assertFalse(e.getBoolean("f"));
        assertTrue(e.getBoolean("t"));
        assertTrue(e.isNull("z"));
        assertEquals(Jsonb.class, e.getArray("a").getClass());
        assertEquals(Jsonb.class, e.getObject("o").getClass());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement("MATCH ()-[r]->() WHERE id(r) = ? RETURN count(*)");
        pstmt.setObject(1, e.getEdgeId());
        rs = pstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        pstmt.close();
    }
}
