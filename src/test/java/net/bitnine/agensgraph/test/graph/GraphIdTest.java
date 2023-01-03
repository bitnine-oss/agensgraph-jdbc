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

package net.bitnine.agensgraph.test.graph;

import net.bitnine.agensgraph.graph.GraphId;
import net.bitnine.agensgraph.test.AbstractAGDockerizedTest;
import net.bitnine.agensgraph.test.TestUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;


public class GraphIdTest extends AbstractAGDockerizedTest {
    private static Connection conn;

    @BeforeClass
    public static void setUp() throws Exception {
        conn = TestUtil.openDB();
        Statement stmt = conn.createStatement();
        stmt.execute("DROP GRAPH IF EXISTS t CASCADE");
        stmt.execute("CREATE GRAPH t");
        stmt.execute("SET graph_path = t");
        stmt.close();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
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

        assertFalse(rs.next());
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

        assertFalse(rs.next());
        rs.close();
        pstmt.close();
    }
}
