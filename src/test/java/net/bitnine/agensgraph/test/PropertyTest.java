/*
 * Copyright (c) 2014-2016, Bitnine Inc.
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

package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.property.JsonType;
import net.bitnine.agensgraph.graph.property.Jsonb;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.graph.property.JsonArray;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PropertyTest extends TestCase {

    private Connection con;
    private Statement st;

    public void setUp() throws Exception {
        con = TestUtil.openDB();
        con.setAutoCommit(true);
        st = con.createStatement();
        try {
            dropSchema();
        }
        catch (Exception ignored) {}
        st.execute("create graph u");
        st.execute("set graph_path = u");
        st.execute("create vlabel company");
        st.execute("create vlabel person");
        st.execute("create elabel employee");
        create();
    }

    private void create() throws Exception {
        st.execute("create (:company '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"no\":1}']"
                + "->(:person '{\"name\":\"jsyang\",\"age\":20,\"height\":178.5,\"married\":false}')");
        st.execute("create (:company '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"no\":2}']"
                + "->(:person '{\"name\":\"ktlee\",\"hobbies\":[\"reading\",\"climbing\"],\"age\":null}')");
        st.execute("CREATE (:person '{ \"name\": \"Emil\", \"from\": \"Sweden\", \"klout\": 99}')");
    }

    private void dropSchema() throws Exception {
        st.execute("drop graph u cascade");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    public void testProperty() throws Exception {
        ResultSet rs = st.executeQuery("MATCH (n)-[:employee '{\"no\":1}']->(m) RETURN n, m");
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("m");
            assertEquals(20, (int)n.getProperty().getInt("age"));
            assertEquals(178.5, n.getProperty().getDouble("height"));
            assertFalse(n.getProperty().getBoolean("married"));
        }
        rs = st.executeQuery("MATCH (n)-['{\"no\":2}']->(m) RETURN n, m");
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("m");
            JsonArray array = n.getProperty().getArray("hobbies");
            assertEquals("climbing", array.getString(1));
            Long age = n.getProperty().getLong("age");
            assertEquals(null, age);
        }
        rs = st.executeQuery("MATCH (n)-['{\"no\":2}']->(m) RETURN m.hobbies::jsonb as hobbies");
        while (rs.next()) {
            Jsonb val = (Jsonb)rs.getObject("hobbies");
            assertEquals("reading", val.ja().getString(0));
        }
        rs = st.executeQuery("MATCH (ee:person) WHERE ee.klout::int = 99 "
                + "RETURN to_jsonb(ee.name::text), ee.name");
        assertTrue(rs.next());
        Object val = rs.getObject(1);
        assertTrue(val instanceof Jsonb);
        Jsonb jval = (Jsonb)val;
        assertEquals(JsonType.STRING, jval.getJsonType());
        String name = jval.getString();
        assertEquals("Emil", name);;
        String qname = (String)rs.getString(2);
        assertEquals("Emil", qname);
        assertFalse(rs.next());
        rs.close();
    }
}
