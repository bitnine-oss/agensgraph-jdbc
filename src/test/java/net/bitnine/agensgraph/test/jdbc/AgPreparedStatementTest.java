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

package net.bitnine.agensgraph.test.jdbc;

import junit.framework.TestCase;
import net.bitnine.agensgraph.jdbc.AgConnection;
import net.bitnine.agensgraph.jdbc.AgPreparedStatement;
import net.bitnine.agensgraph.test.TestUtil;
import net.bitnine.agensgraph.util.Jsonb;
import net.bitnine.agensgraph.util.JsonbUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AgPreparedStatementTest extends TestCase {
    private AgConnection conn;

    @Override
    public void setUp() throws Exception {
        conn = TestUtil.openDB().unwrap(AgConnection.class);
        Statement stmt = conn.createStatement();
        stmt.execute("DROP GRAPH IF EXISTS t CASCADE");
        stmt.execute("CREATE GRAPH t");
        stmt.execute("SET graph_path = t");
        stmt.execute("CREATE ({s: 's', l: 7, d: 7.7, t: true, a: [7], o: {i: 7}})");
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
    public void testSetBoolean() throws SQLException {
        AgPreparedStatement apstmt = conn.prepareNamedParameterStatement("MATCH (n) WHERE n.t = $b RETURN count(*)");
        apstmt.setBoolean("b", true);
        ResultSet rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        apstmt.close();
    }

    @Test
    public void testSetInteger() throws SQLException {
        AgPreparedStatement apstmt = conn.prepareNamedParameterStatement("MATCH (n) WHERE n.l = $integer RETURN count(*)");

        // short

        apstmt.setShort("integer", (short) 7);
        ResultSet rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();

        // int

        apstmt.setInt("integer", 7);
        rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();

        // long

        apstmt.setLong("integer", 7L);
        rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();

        apstmt.close();
    }

    @Test
    public void testSetReal() throws SQLException {
        AgPreparedStatement apstmt = conn.prepareNamedParameterStatement("RETURN $real");

        // float

        apstmt.setFloat("real", 7.7F);
        ResultSet rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals("7.69999981", rs.getString(1));
        assertTrue(7.69999981 != rs.getFloat(1));

        assertTrue(!rs.next());
        rs.close();

        // double

        apstmt.setDouble("real", 7.7);
        rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals("7.70000000000000018", rs.getString(1));
        assertEquals(7.70000000000000018, rs.getDouble(1));

        assertTrue(!rs.next());
        rs.close();

        apstmt.close();

        // numeric

        apstmt = conn.prepareNamedParameterStatement("MATCH (n) WHERE n.d = $real RETURN count(*)");
        apstmt.setBigDecimal("real", new BigDecimal("7.7"));
        rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        apstmt.close();
    }

    @Test
    public void testSetString() throws SQLException {
        AgPreparedStatement apstmt = conn.prepareNamedParameterStatement("MATCH (n) WHERE n.s = $str RETURN count(*)");
        apstmt.setString("str", "s");
        ResultSet rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        apstmt.close();
    }

    @Test
    public void testSetObject() throws SQLException {
        // array

        AgPreparedStatement apstmt = conn.prepareNamedParameterStatement("MATCH (n {a: $arr}) RETURN count(*)");
        Jsonb j = JsonbUtil.createArrayBuilder().add(7).build();
        apstmt.setObject("arr", j);
        ResultSet rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        apstmt.close();

        // object

        apstmt = conn.prepareNamedParameterStatement("MATCH (n {o: $obj}) RETURN count(*)");
        j = JsonbUtil.createObjectBuilder().add("i", 7).build();
        apstmt.setObject("obj", j);
        rs = apstmt.executeQuery();
        assertTrue(rs.next());

        assertEquals(1, rs.getLong(1));

        assertTrue(!rs.next());
        rs.close();
        apstmt.close();
    }
}
