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
import net.bitnine.agensgraph.graph.GraphId;
import net.bitnine.agensgraph.test.TestUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GraphIdTest extends TestCase {
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
    public void testGraphId() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("CREATE (n {}) RETURN id(n)");
        assertTrue(rs.next());

        GraphId gid = (GraphId) rs.getObject(1);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();

        GraphId param = new GraphId(gid.getId());
        assertEquals(param, gid);

        PreparedStatement pstmt = conn.prepareStatement("MATCH (n) WHERE id(n) = ? RETURN id(n)");
        pstmt.setObject(1, param);
        rs = pstmt.executeQuery();
        assertTrue(rs.next());

        gid = (GraphId) rs.getObject(1);
        assertEquals(gid, param);

        assertTrue(!rs.next());
        rs.close();
        pstmt.close();
    }
}
