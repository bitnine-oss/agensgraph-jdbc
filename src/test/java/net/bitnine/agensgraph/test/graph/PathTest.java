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
import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.test.TestUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PathTest extends TestCase {
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
    public void testPath() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("CREATE p=({s: '[}\\\\\"'})-[:e]->() RETURN p");
        assertTrue(rs.next());

        Path p = (Path) rs.getObject(1);
        assertEquals(1, p.length());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement("MATCH (n) WHERE id(n) = ? RETURN count(*)");
        for (Vertex v : p.vertices()) {
            pstmt.setObject(1, v.getVertexId());
            rs = pstmt.executeQuery();
            assertTrue(rs.next());

            assertEquals(1, rs.getLong(1));

            assertTrue(!rs.next());
            rs.close();
        }
        pstmt.close();

        pstmt = conn.prepareStatement("MATCH ()-[r]->() WHERE id(r) = ? RETURN count(*)");
        for (Edge e : p.edges()) {
            pstmt.setObject(1, e.getEdgeId());
            rs = pstmt.executeQuery();
            assertTrue(rs.next());

            assertEquals(1, rs.getLong(1));

            assertTrue(!rs.next());
            rs.close();
        }
        pstmt.close();
    }
}
