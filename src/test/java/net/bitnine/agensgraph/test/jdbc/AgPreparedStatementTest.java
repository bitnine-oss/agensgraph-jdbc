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
