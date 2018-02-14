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

package net.bitnine.agensgraph.test.ds;

import junit.framework.TestCase;
import net.bitnine.agensgraph.ds.AGPoolingDataSource;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.test.TestUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AGPoolingDataSourceTest extends TestCase {
    private static final String DS_NAME = "Test DataSource";
    private AGPoolingDataSource bds;
    private Connection con;

    @Override
    public void setUp() throws Exception {
        bds = new AGPoolingDataSource();
        bds.setDataSourceName(DS_NAME);
        bds.setUrl(TestUtil.getURL());
        bds.setInitialConnections(2);
        bds.setMaxConnections(5);
    }

    @Override
    public void tearDown() throws Exception {
        bds.close();
    }

    /**
     * In this case, we *do* want it to be pooled.
     */
    @Test
    public void testNotPooledConnection() throws SQLException {
        con = bds.getConnection();
        String name = con.toString();
        con.close();
        con = bds.getConnection();
        String name2 = con.toString();
        con.close();
        assertEquals("Pooled DS doesn't appear to be pooling connections!", name, name2);
    }

    /**
     * Check that 2 DS instances can't use the same name.
     */
    @Test
    public void testCantReuseName() throws Exception {
        AGPoolingDataSource source = new AGPoolingDataSource();
        try {
            source.setDataSourceName(DS_NAME);
            fail("Should hava denied 2nd DataSource with same name");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Closing a Connection twice is not an error.
     */
    @Test
    public void testDoubleConnectionClose() throws SQLException {
        con = bds.getConnection();
        con.close();
        con.close();
    }

    /**
     * Closing a Statement twice is not an error.
     */
    @Test
    public void testDoubleStatementClose() throws SQLException {
        con = bds.getConnection();
        Statement stmt = con.createStatement();
        stmt.close();
        stmt.close();
        con.close();
    }

    @Test
    public void testConnectionObjectMethods() throws SQLException {
        con = bds.getConnection();

        Connection conRef = con;
        assertEquals(con, conRef);

        int hc1 = con.hashCode();
        con.close();
        int hc2 = con.hashCode();

        assertEquals(con, conRef);
        assertEquals(hc1, hc2);
    }

    @Test
    public void testExecuteQuery() throws Exception {
        con = bds.getConnection();
        Statement stmt = con.createStatement();
        stmt.execute("DROP GRAPH IF EXISTS t CASCADE");
        stmt.execute("CREATE GRAPH t");
        stmt.execute("SET graph_path = t");
        stmt.execute("CREATE (n:v {s: '', l: 100, d: 0.0, f: false, t: true, z: null, a: [], o: {}}) RETURN n");
        ResultSet rs = stmt.executeQuery("MATCH (n) RETURN n");
        rs.next();
        Vertex v = (Vertex) rs.getObject(1);
        assertEquals(100, v.getInt("l"));

        rs.close();
        stmt.close();
        con.close();
    }
}
