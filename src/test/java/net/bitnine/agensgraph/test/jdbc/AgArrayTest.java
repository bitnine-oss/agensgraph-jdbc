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

package net.bitnine.agensgraph.test.jdbc;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.GraphId;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.jdbc.AgConnection;
import net.bitnine.agensgraph.test.TestUtil;
import org.junit.Test;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;


public class AgArrayTest extends TestCase {
    private AgConnection conn;

    @Override
    public void setUp() throws Exception {
        conn = TestUtil.openDB().unwrap(AgConnection.class);
        Statement stmt = conn.createStatement();
        stmt.execute("DROP GRAPH IF EXISTS t CASCADE");
        stmt.execute("CREATE GRAPH t");
        stmt.execute("SET graph_path = t");
        stmt.execute("CREATE (:v{vid:1})-[:r{rid:1}]->(:v{vid:2})-[:r{rid:2}]->(:v{vid:3})");
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
    public void testEdgeArray() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH p=()-[]->()-[]->() RETURN relationships(p)");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);
        Edge[] e = (Edge[]) arr.getArray();

        for (int i = 0; i < e.length; i++) {
            assertEquals("edge", e[i].getType());
        }

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testEdgeResultSet() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH ()-[rel:r *2..]-() RETURN rel");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);
        rs = arr.getResultSet();

        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        Edge e1 = (Edge) rs.getObject(2);
        assertEquals(1, e1.getInt("rid"));

        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));
        Edge e2 = (Edge) rs.getObject(2);
        assertEquals(2, e2.getInt("rid"));

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testVertexArray() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH p=()-[]->()-[]->() RETURN nodes(p)");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);
        Vertex[] v = (Vertex[]) arr.getArray();

        for (int i = 0; i < v.length; i++) {
            assertEquals("vertex", v[i].getType());
        }

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testVertexResultSet() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH p=()-[]->()-[]->() RETURN nodes(p)");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);
        rs = arr.getResultSet();

        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        Vertex v1 = (Vertex) rs.getObject(2);
        assertEquals(1, v1.getInt("vid"));

        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));
        Vertex v2 = (Vertex) rs.getObject(2);
        assertEquals(2, v2.getInt("vid"));

        assertTrue(rs.next());
        assertEquals(3, rs.getInt(1));
        Vertex v3 = (Vertex) rs.getObject(2);
        assertEquals(3, v3.getInt("vid"));

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGraphIdArray() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH (v)-[]->() RETURN array_agg(id(v))");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);
        GraphId[] gid = (GraphId[]) arr.getArray();

        for (int i = 0; i < gid.length; i++) {
            assertEquals("graphid", gid[i].getType());
        }

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGraphIdResultSet() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH ()-[r]->() RETURN array_agg(id(r))");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);
        rs = arr.getResultSet();

        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        GraphId gid1 = (GraphId) rs.getObject(2);
        assertEquals("graphid", gid1.getType());

        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));
        GraphId gid2 = (GraphId) rs.getObject(2);
        assertEquals("graphid", gid2.getType());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetBaseTypeName() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH p=()-[]->()-[]->() RETURN relationships(p)");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);

        assertEquals("edge", arr.getBaseTypeName());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetBaseType() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH p=()-[]->()-[]->() RETURN relationships(p)");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);

        assertEquals(Types.STRUCT, arr.getBaseType());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testWriteGraphIdArray() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("MATCH (v)-[]->() RETURN array_agg(id(v))");
        assertTrue(rs.next());
        Array arr = rs.getArray(1);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement("RETURN ?");
        pstmt.setArray(1, arr);
        rs = pstmt.executeQuery();
        assertTrue(rs.next());
        arr = rs.getArray(1);
        GraphId[] gid = (GraphId[]) arr.getArray();
        for (int i = 0; i < gid.length; i++) {
            assertEquals("graphid", gid[i].getType());
        }

        assertTrue(!rs.next());
        rs.close();

        pstmt.setArray(1, conn.createArrayOf("graphid", gid));
        rs = pstmt.executeQuery();
        assertTrue(rs.next());
        arr = rs.getArray(1);
        gid = (GraphId[]) arr.getArray();
        for (int i = 0; i < gid.length; i++) {
            assertEquals("graphid", gid[i].getType());
        }

        assertTrue(!rs.next());
        rs.close();
        pstmt.close();
    }
}
