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
import net.bitnine.agensgraph.test.TestUtil;
import org.junit.Test;
import org.postgresql.util.PSQLState;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AgResultSetTest extends TestCase {
    private Connection conn;

    @Override
    public void setUp() throws Exception {
        conn = TestUtil.openDB();
    }

    @Override
    public void tearDown() throws SQLException {
        TestUtil.closeDB(conn);
    }

    @Test
    public void testGetString() throws SQLException {
        Statement stmt = conn.createStatement();

        // string

        ResultSet rs = stmt.executeQuery("RETURN 'Agens\\\\Graph' AS s");
        assertTrue(rs.next());

        assertEquals("Agens\\Graph", rs.getString(1));
        assertEquals("Agens\\Graph", rs.getString("s"));

        assertTrue(!rs.next());
        rs.close();

        // SQL NULL

        rs = stmt.executeQuery("RETURN null AS z");
        assertTrue(rs.next());

        assertNull(rs.getString(1));
        assertTrue(rs.wasNull());
        assertNull(rs.getString("z"));
        assertTrue(rs.wasNull());

        assertTrue(!rs.next());
        rs.close();

        // jsonb null

        rs = stmt.executeQuery("SELECT 'null'::jsonb AS z");
        assertTrue(rs.next());

        assertNull(rs.getString(1));
        assertNull(rs.getString("z"));

        assertTrue(!rs.next());
        rs.close();

        // SQL boolean

        rs = stmt.executeQuery("RETURN false AS f, true AS t");
        assertTrue(rs.next());

        assertEquals("f", rs.getString(1));
        assertEquals("f", rs.getString("f"));
        assertEquals("t", rs.getString(2));
        assertEquals("t", rs.getString("t"));

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean

        rs = stmt.executeQuery("RETURN false::jsonb AS f, true::jsonb AS t");
        assertTrue(rs.next());

        assertEquals("false", rs.getString(1));
        assertEquals("false", rs.getString("f"));
        assertEquals("true", rs.getString(2));
        assertEquals("true", rs.getString("t"));

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN " + Long.MAX_VALUE + " AS l, " + Math.PI + " AS d, [0, 1] AS a, {k: 'v'} AS o");
        assertTrue(rs.next());

        assertEquals(Long.toString(Long.MAX_VALUE), rs.getString(1));
        assertEquals(Long.toString(Long.MAX_VALUE), rs.getString("l"));
        assertEquals(Double.toString(Math.PI), rs.getString(2));
        assertEquals(Double.toString(Math.PI), rs.getString("d"));
        assertEquals("[0, 1]", rs.getString(3));
        assertEquals("[0, 1]", rs.getString("a"));
        assertEquals("{\"k\": \"v\"}", rs.getString(4));
        assertEquals("{\"k\": \"v\"}", rs.getString("o"));

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetBoolean() throws SQLException {
        Statement stmt = conn.createStatement();

        // SQL boolean

        ResultSet rs = stmt.executeQuery("RETURN false AS f, true AS t");
        assertTrue(rs.next());

        assertFalse(rs.getBoolean(1));
        assertFalse(rs.getBoolean("f"));
        assertTrue(rs.getBoolean(2));
        assertTrue(rs.getBoolean("t"));

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean

        rs = stmt.executeQuery("RETURN false::jsonb AS f, true::jsonb AS t");
        assertTrue(rs.next());

        assertFalse(rs.getBoolean(1));
        assertFalse(rs.getBoolean("f"));
        assertTrue(rs.getBoolean(2));
        assertTrue(rs.getBoolean("t"));

        assertTrue(!rs.next());
        rs.close();

        // SQL NULL

        rs = stmt.executeQuery("RETURN null AS z");
        assertTrue(rs.next());

        assertFalse(rs.getBoolean(1));
        assertFalse(rs.getBoolean("z"));

        assertTrue(!rs.next());
        rs.close();

        // jsonb NULL

        rs = stmt.executeQuery("SELECT 'null'::jsonb AS z");
        assertTrue(rs.next());

        assertFalse(rs.getBoolean(1));
        assertFalse(rs.getBoolean("z"));

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '' AS sf, 's' AS st, 0 AS lf, 7 AS lt, 0.0 AS df, 7.7 AS dt, [] AS af, [7] AS at, {} AS of, {i: 7} AS ot");
        assertTrue(rs.next());

        assertFalse(rs.getBoolean(1));
        assertFalse(rs.getBoolean("sf"));
        assertTrue(rs.getBoolean(2));
        assertTrue(rs.getBoolean("st"));
        assertFalse(rs.getBoolean(3));
        assertFalse(rs.getBoolean("lf"));
        assertTrue(rs.getBoolean(4));
        assertTrue(rs.getBoolean("lt"));
        assertFalse(rs.getBoolean(5));
        assertFalse(rs.getBoolean("df"));
        assertTrue(rs.getBoolean(6));
        assertTrue(rs.getBoolean("dt"));
        assertFalse(rs.getBoolean(7));
        assertFalse(rs.getBoolean("af"));
        assertTrue(rs.getBoolean(8));
        assertTrue(rs.getBoolean("at"));
        assertFalse(rs.getBoolean(9));
        assertFalse(rs.getBoolean("of"));
        assertTrue(rs.getBoolean(10));
        assertTrue(rs.getBoolean("ot"));

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetShort() throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("RETURN " + Short.MAX_VALUE + " AS l, 7.7 AS d");
        assertTrue(rs.next());

        assertEquals(Short.MAX_VALUE, rs.getShort(1));
        assertEquals(Short.MAX_VALUE, rs.getShort("l"));
        assertEquals(7, rs.getShort(2));
        assertEquals(7, rs.getShort("d"));

        assertTrue(!rs.next());
        rs.close();

        // out of range

        rs = stmt.executeQuery("RETURN " + ((int) Short.MAX_VALUE + 1) + " AS l");
        assertTrue(rs.next());

        try {
            rs.getShort(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        try {
            rs.getShort("l");
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetInt() throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("RETURN " + Integer.MAX_VALUE + " AS l, 7.7 AS d");
        assertTrue(rs.next());

        assertEquals(Integer.MAX_VALUE, rs.getInt(1));
        assertEquals(Integer.MAX_VALUE, rs.getInt("l"));
        assertEquals(7, rs.getInt(2));
        assertEquals(7, rs.getInt("d"));

        assertTrue(!rs.next());
        rs.close();

        // out of range

        rs = stmt.executeQuery("RETURN " + ((long) Integer.MAX_VALUE + 1) + " AS l");
        assertTrue(rs.next());

        try {
            rs.getInt(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        try {
            rs.getInt("l");
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetLong() throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("RETURN " + Long.MAX_VALUE + " AS l, 7.7 AS d");
        assertTrue(rs.next());

        assertEquals(Long.MAX_VALUE, rs.getLong(1));
        assertEquals(Long.MAX_VALUE, rs.getLong("l"));
        assertEquals(7, rs.getLong(2));
        assertEquals(7, rs.getLong("d"));

        assertTrue(!rs.next());
        rs.close();

        // out of range

        rs = stmt.executeQuery("RETURN " + Long.MAX_VALUE + "0 AS l");
        assertTrue(rs.next());

        try {
            rs.getLong(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        try {
            rs.getLong("l");
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetFloat() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN " + Float.MAX_VALUE + " AS d");
        assertTrue(rs.next());

        assertEquals(Float.MAX_VALUE, rs.getFloat(1));
        assertEquals(Float.MAX_VALUE, rs.getFloat("d"));

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetDouble() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN " + Double.MAX_VALUE + " AS d");
        assertTrue(rs.next());

        assertEquals(Double.MAX_VALUE, rs.getDouble(1));
        assertEquals(Double.MAX_VALUE, rs.getDouble("d"));

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetBigDecimal() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN " + Double.MAX_VALUE + " AS d");
        assertTrue(rs.next());

        assertEquals(Double.MAX_VALUE, rs.getBigDecimal(1).doubleValue());
        assertEquals(Double.MAX_VALUE, rs.getBigDecimal("d").doubleValue());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetStringToShort() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ' 0' AS s");
        assertTrue(rs.next());
        assertEquals(0, rs.getShort(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '" + Short.MAX_VALUE + "' AS s");
        assertTrue(rs.next());
        assertEquals(Short.MAX_VALUE, rs.getShort(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '0x' AS s");
        assertTrue(rs.next());
        try {
            rs.getShort(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN "+  Short.toString(Short.MAX_VALUE) + " AS s");
        assertTrue(rs.next());
        assertEquals(Short.MAX_VALUE, rs.getShort(1));
        assertTrue(!rs.next());

        rs.close();
        stmt.close();
    }

    @Test
    public void testGetStringToInt() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ' 0' AS i");
        assertTrue(rs.next());
        assertEquals(0, rs.getInt(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '" + Integer.MAX_VALUE + "' AS i");
        assertTrue(rs.next());
        assertEquals(Integer.MAX_VALUE, rs.getLong(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '0x' AS i");
        assertTrue(rs.next());
        try {
            rs.getInt(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN "+  Integer.toString(Integer.MAX_VALUE) + " AS i");
        assertTrue(rs.next());
        assertEquals(Integer.MAX_VALUE, rs.getInt(1));
        assertTrue(!rs.next());

        rs.close();
        stmt.close();
    }

    @Test
    public void testGetStringToLong() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ' 0' AS l");
        assertTrue(rs.next());
        assertEquals(0, rs.getLong(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '" + Long.MAX_VALUE + "' AS l");
        assertTrue(rs.next());
        assertEquals(Long.MAX_VALUE, rs.getLong(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '0x' AS l");
        assertTrue(rs.next());
        try {
            rs.getLong(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN "+  Long.toString(Long.MAX_VALUE) + " AS l");
        assertTrue(rs.next());
        assertEquals(Long.MAX_VALUE, rs.getLong(1));
        assertTrue(!rs.next());

        rs.close();
        stmt.close();
    }

    @Test
    public void testGetStringToFloat() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ' 0.0' AS f");
        assertTrue(rs.next());
        assertEquals(0.0F, rs.getFloat(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '" + Float.MAX_VALUE + "' AS f");
        assertTrue(rs.next());
        assertEquals(Float.MAX_VALUE, rs.getFloat(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '0.0x' AS d");
        assertTrue(rs.next());
        try {
            rs.getFloat(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN "+  Float.toString(Float.MAX_VALUE) + " AS f");
        assertTrue(rs.next());
        assertEquals(Float.MAX_VALUE, rs.getFloat(1));
        assertTrue(!rs.next());

        rs.close();
        stmt.close();
    }

    @Test
    public void testGetStringToDouble() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ' 0.0' AS d");
        assertTrue(rs.next());
        assertEquals(0.0D, rs.getDouble(1));
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '0.0x' AS d");
        assertTrue(rs.next());
        try {
            rs.getDouble(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN "+  Double.toString(Double.MAX_VALUE) + " AS d");
        assertTrue(rs.next());
        assertEquals(Double.MAX_VALUE, rs.getDouble(1));
        assertTrue(!rs.next());

        rs.close();
        stmt.close();
    }

    @Test
    public void testGetStringToBigDecimal() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ' 0.0' AS b");
        assertTrue(rs.next());
        assertEquals(0.0, rs.getBigDecimal(1).doubleValue());
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN '0.0x' AS b");
        assertTrue(rs.next());
        try {
            rs.getBigDecimal(1);
            fail("SQLExecption expected");
        } catch (SQLException e) {
            assertEquals(PSQLState.NUMERIC_VALUE_OUT_OF_RANGE.getState(), e.getSQLState());
        }
        assertTrue(!rs.next());

        rs = stmt.executeQuery("RETURN "+  Double.toString(Double.MAX_VALUE) + " AS d");
        assertTrue(rs.next());
        assertEquals(Double.MAX_VALUE, rs.getBigDecimal(1).doubleValue());
        assertTrue(!rs.next());

        rs.close();
        stmt.close();
    }
}
