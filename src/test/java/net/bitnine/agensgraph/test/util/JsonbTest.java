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

package net.bitnine.agensgraph.test.util;

import junit.framework.TestCase;
import net.bitnine.agensgraph.jdbc.AgResultSet;
import net.bitnine.agensgraph.test.TestUtil;
import net.bitnine.agensgraph.util.Jsonb;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class JsonbTest extends TestCase {
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
    public void testGetJsonValueString() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN 'Agens\\\\Graph'");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals("\"Agens\\\\Graph\"", j.getValue());
        Object obj = j.getJsonValue();
        assertEquals(String.class, obj.getClass());
        assertEquals("Agens\\Graph", obj);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetJsonValueLong() throws SQLException {
        Statement stmt = conn.createStatement();
        AgResultSet rs = stmt.executeQuery("RETURN " + Long.MAX_VALUE).unwrap(AgResultSet.class);
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals(Long.toString(Long.MAX_VALUE), j.getValue());
        Object obj = j.getJsonValue();
        assertEquals(Long.class, obj.getClass());
        assertEquals(Long.MAX_VALUE, obj);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetJsonValueDouble() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN " + Math.PI);
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals(Double.toString(Math.PI), j.getValue());
        Object obj = j.getJsonValue();
        assertEquals(Double.class, obj.getClass());
        assertEquals(Math.PI, obj);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetJsonValueBoolean() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb");
        assertTrue(rs.next());

        Jsonb j1 = (Jsonb) rs.getObject(1);
        assertEquals("false", j1.getValue());
        Object obj1 = j1.getJsonValue();
        assertEquals(Boolean.class, obj1.getClass());
        assertFalse((Boolean) obj1);

        Jsonb j2 = (Jsonb) rs.getObject(2);
        assertEquals("true", j2.getValue());
        Object obj2 = j2.getJsonValue();
        assertEquals(Boolean.class, obj2.getClass());
        assertTrue((Boolean) obj2);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetJsonValueNull() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 'null'::jsonb");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals("null", j.getValue());
        assertTrue(j.isNull());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetJsonValueArray() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN [0, 1]");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals("[0, 1]", j.getValue());

        Object obj = j.getJsonValue();
        assertEquals(JSONArray.class, obj.getClass());

        JSONArray a = (JSONArray) obj;
        Object e0 = a.get(0);
        assertEquals(Long.class, e0.getClass());
        assertEquals(0L, e0);
        Object e1 = a.get(1);
        assertEquals(Long.class, e1.getClass());
        assertEquals(1L, e1);

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetJsonValueObject() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN {s: 's', l: 7, d: 7.7, f: false, t: true, z: null, a: [], _: {}}");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals("{\"_\": {}, \"a\": [], \"d\": 7.7, \"f\": false, \"l\": 7, \"s\": \"s\", \"t\": true}", j.getValue());

        Object obj = j.getJsonValue();
        assertEquals(JSONObject.class, obj.getClass());

        JSONObject o = (JSONObject) obj;
        Object s = o.get("s");
        assertEquals(String.class, s.getClass());
        assertEquals("s", s);
        Object l = o.get("l");
        assertEquals(Long.class, l.getClass());
        assertEquals(7L, l);
        Object d = o.get("d");
        assertEquals(Double.class, d.getClass());
        assertEquals(7.7, d);
        Object f = o.get("f");
        assertEquals(Boolean.class, f.getClass());
        assertFalse((Boolean) f);
        Object t = o.get("t");
        assertEquals(Boolean.class, t.getClass());
        assertTrue((Boolean) t);
        assertNull(o.get("z"));
        assertEquals(JSONArray.class, o.get("a").getClass());
        assertEquals(JSONObject.class, o.get("_").getClass());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetString() throws SQLException {
        Statement stmt = conn.createStatement();

        // string

        ResultSet rs = stmt.executeQuery("RETURN 'Agens\\\\Graph'");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals("Agens\\Graph", j.getString());

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean

        rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb");
        assertTrue(rs.next());

        for (int i = 1; i <= 2; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getString();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        // jsonb null

        rs = stmt.executeQuery("SELECT 'null'::jsonb");
        assertTrue(rs.next());

        j = (Jsonb) rs.getObject(1);
        try {
            j.getString();
            fail("SQLException expected");
        } catch (UnsupportedOperationException ignored){
        }

        assertTrue(!rs.next());
        rs.close();

        rs = stmt.executeQuery("RETURN 'null'::jsonb");
        assertTrue(rs.next());

        j = (Jsonb) rs.getObject(1);
        assertEquals("null", j.getString());

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN 0, 0.0, [], {}");
        assertTrue(rs.next());

        for (int i = 1; i <= 4; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getString();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetInt() throws SQLException {
        Statement stmt = conn.createStatement();

        // int

        ResultSet rs = stmt.executeQuery("RETURN " + Integer.MAX_VALUE);
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals(Integer.MAX_VALUE, j.getInt());

        assertTrue(!rs.next());
        rs.close();

        // int - out of range

        rs = stmt.executeQuery("RETURN " + Long.MAX_VALUE);
        assertTrue(rs.next());

        j = (Jsonb) rs.getObject(1);
        try {
            j.getInt();
            fail("SQLExecption expected");
        } catch (IllegalArgumentException ignored) {
        }

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean and null

        rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb, 'null'::jsonb");
        assertTrue(rs.next());

        for (int i = 1; i <= 3; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getInt();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '', 0.0, [], {}");
        assertTrue(rs.next());

        for (int i = 1; i <= 4; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getInt();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetLong() throws SQLException {
        Statement stmt = conn.createStatement();

        // long

        ResultSet rs = stmt.executeQuery("RETURN " + Long.MAX_VALUE);
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals(Long.MAX_VALUE, j.getLong());

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean and null

        rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb, 'null'::jsonb");
        assertTrue(rs.next());

        for (int i = 1; i <= 3; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getLong();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '', 0.0, [], {}");
        assertTrue(rs.next());

        for (int i = 1; i <= 4; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getLong();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetDouble() throws SQLException {
        Statement stmt = conn.createStatement();

        // double

        ResultSet rs = stmt.executeQuery("RETURN " + Math.PI);
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals(Math.PI, j.getDouble());

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean and null

        rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb, 'null'::jsonb");
        assertTrue(rs.next());

        for (int i = 1; i <= 3; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getDouble();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '', 0, [], {}");
        assertTrue(rs.next());

        for (int i = 1; i <= 4; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getDouble();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetBoolean() throws SQLException {
        Statement stmt = conn.createStatement();

        // boolean

        ResultSet rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertFalse(j.getBoolean());
        j = (Jsonb) rs.getObject(2);
        assertTrue(j.getBoolean());

        assertTrue(!rs.next());
        rs.close();

        // jsonb null

        rs = stmt.executeQuery("SELECT 'null'::jsonb");
        assertTrue(rs.next());

        j = (Jsonb) rs.getObject(1);
        assertFalse(j.getBoolean());

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '', 's', 0, 7, 0.0, 7.7, [], [7], {}, {i: 7}");
        assertTrue(rs.next());

        j = (Jsonb) rs.getObject(1);
        assertFalse(j.getBoolean());
        j = (Jsonb) rs.getObject(2);
        assertTrue(j.getBoolean());
        j = (Jsonb) rs.getObject(3);
        assertFalse(j.getBoolean());
        j = (Jsonb) rs.getObject(4);
        assertTrue(j.getBoolean());
        j = (Jsonb) rs.getObject(5);
        assertFalse(j.getBoolean());
        j = (Jsonb) rs.getObject(6);
        assertTrue(j.getBoolean());
        j = (Jsonb) rs.getObject(7);
        assertFalse(j.getBoolean());
        j = (Jsonb) rs.getObject(8);
        assertTrue(j.getBoolean());
        j = (Jsonb) rs.getObject(9);
        assertFalse(j.getBoolean());
        j = (Jsonb) rs.getObject(10);
        assertTrue(j.getBoolean());

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testIsNull() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 'null'::jsonb");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertTrue(j.isNull());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetArray() throws SQLException {
        Statement stmt = conn.createStatement();

        // array

        ResultSet rs = stmt.executeQuery("RETURN []");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        Object obj = j.getArray();
        assertEquals(Jsonb.class, obj.getClass());
        Jsonb a = (Jsonb) obj;
        assertEquals(JSONArray.class, a.getJsonValue().getClass());

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean and null

        rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb, 'null'::jsonb");
        assertTrue(rs.next());

        for (int i = 1; i <= 3; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getArray();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '', 0, 0.0, {}");
        assertTrue(rs.next());

        for (int i = 1; i <= 4; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getArray();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testGetObject() throws SQLException {
        Statement stmt = conn.createStatement();

        // object

        ResultSet rs = stmt.executeQuery("RETURN {}");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        Object obj = j.getObject();
        assertEquals(Jsonb.class, obj.getClass());
        Jsonb a = (Jsonb) obj;
        assertEquals(JSONObject.class, a.getJsonValue().getClass());

        assertTrue(!rs.next());
        rs.close();

        // jsonb boolean and null

        rs = stmt.executeQuery("RETURN false::jsonb, true::jsonb, 'null'::jsonb");
        assertTrue(rs.next());

        for (int i = 1; i <= 3; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getObject();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        // other types

        rs = stmt.executeQuery("RETURN '', 0, 0.0, []");
        assertTrue(rs.next());

        for (int i = 1; i <= 4; i++) {
            j = (Jsonb) rs.getObject(i);
            try {
                j.getObject();
                fail("SQLExecption expected");
            } catch (UnsupportedOperationException ignored) {
            }
        }

        assertTrue(!rs.next());
        rs.close();

        stmt.close();
    }

    @Test
    public void testArrayGet() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN ['', 0, 0.0, false, true, null, [], {}]");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        assertEquals(8, j.size());
        assertEquals("", j.getString(0));
        assertEquals(0, j.getInt(1));
        assertEquals(0, j.getLong(1));
        assertEquals(0.0, j.getDouble(2));
        assertFalse(j.getBoolean(3));
        assertTrue(j.getBoolean(4));
        assertTrue(j.isNull(5));
        assertEquals(Jsonb.class, j.getArray(6).getClass());
        assertEquals(Jsonb.class, j.getObject(7).getClass());

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testObjectGet() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN {s: '', l: 0, d: 0.0, f: false, t: true, z: null, a: [], o: {}}");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);

        assertEquals(7, j.size());

        for (String k : j.getKeys())
            assertTrue("s".equals(k) || "l".equals(k) || "d".equals(k) ||
                    "f".equals(k) || "t".equals(k) || "a".equals(k) || "o".equals(k));

        assertFalse(j.containsKey("x"));
        assertTrue(j.containsKey("s"));
        assertEquals("", j.getString("s"));
        assertEquals(0, j.getInt("l"));
        assertEquals(0, j.getLong("l"));
        assertEquals(0.0, j.getDouble("d"));
        assertFalse(j.getBoolean("f"));
        assertTrue(j.getBoolean("t"));
        assertTrue(j.isNull("z"));
        assertEquals(Jsonb.class, j.getArray("a").getClass());
        assertEquals(Jsonb.class, j.getObject("o").getClass());

        assertEquals("", j.getString("x", ""));
        assertEquals(0, j.getInt("x", 0));
        assertEquals(0L, j.getLong("x", 0L));
        assertEquals(0.0, j.getDouble("x", 0.0));
        assertEquals(false, j.getBoolean("x", false));

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }

    @Test
    public void testGetTypedValue() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("RETURN {s: '', l: 0, d: 0.0, f: false, t: true, z: null, a: ['', 0], o: {s: '', l: 4294967296}}");
        assertTrue(rs.next());

        Jsonb j = (Jsonb) rs.getObject(1);
        Map o = (Map) j.getTypedValue();
        assertEquals("", o.get("s"));
        assertEquals(0, o.get("l"));
        assertEquals(0.0, o.get("d"));
        assertFalse((boolean) o.get("f"));
        assertTrue((boolean) o.get("t"));
        assertNull(o.get("z"));
        List a = (List) o.get("a");
        assertEquals("", a.get(0));
        assertEquals(0, a.get(1));
        Map po = (Map) o.get("o");
        assertEquals("", po.get("s"));
        assertEquals(4294967296L, po.get("l"));

        assertTrue(!rs.next());
        rs.close();
        stmt.close();
    }
}
