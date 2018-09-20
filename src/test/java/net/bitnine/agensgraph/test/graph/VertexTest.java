/*
 * Copyright (c) 2014-2018 Bitnine, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.bitnine.agensgraph.test.graph;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.test.TestUtil;
import net.bitnine.agensgraph.util.Jsonb;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VertexTest extends TestCase {
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
    public void testVertex() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("CREATE (n:v {s: '', l: 0, d: 0.0, f: false, t: true, z: null, a: [], o: {}}) RETURN n");
        assertTrue(rs.next());

        Vertex v = (Vertex) rs.getObject(1);
        assertEquals("v", v.getLabel());
        assertFalse(v.containsKey("x"));
        assertTrue(v.containsKey("s"));
        assertEquals("", v.getString("s"));
        assertEquals(0, v.getInt("l"));
        assertEquals(0, v.getLong("l"));
        assertEquals(0.0, v.getDouble("d"));
        assertFalse(v.getBoolean("f"));
        assertTrue(v.getBoolean("t"));
        assertTrue(v.isNull("z"));
        assertEquals(Jsonb.class, v.getArray("a").getClass());
        assertEquals(Jsonb.class, v.getObject("o").getClass());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement("MATCH (n) WHERE id(n) = ? RETURN count(*)");
        pstmt.setObject(1, v.getVertexId());
        rs = pstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        pstmt.close();
    }
}
