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
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.graph.property.JsonObject;
import net.bitnine.agensgraph.graph.property.Jsonb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class WhereTest extends TestCase {

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
        st.execute("create vlabel person");
        create();
    }

    private void create() throws Exception {
        st.execute("CREATE (:person { 'name': 'Emil', 'from': 'Sweden', 'klout': 99 })");
    }

    private void dropSchema() throws Exception {
        st.execute("drop graph u cascade");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    public void testWhere() throws Exception {
        ResultSet rs = st.executeQuery("MATCH (ee:person) WHERE ee.name = 'Emil' return ee");
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals(99, (int) n.getProperty().getInt("klout"));
        }
        rs = st.executeQuery("MATCH (ee:person) WHERE ee.klout::int = 99 return ee");
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals(99, (int) n.getProperty().getInt("klout"));
        }
        rs = st.executeQuery("MATCH (ee:person) WHERE ee.from = 'Korea' return ee");
        assertFalse(rs.next());
        rs.close();
    }

    public void testWhereBind() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("MATCH (ee:person) WHERE ee.from = ? return ee");
        pstmt.setString(1, "Sweden");
        ResultSet rs = pstmt.executeQuery();
        assertTrue(rs.next());
        Vertex n = (Vertex) rs.getObject("ee");
        assertEquals(99, (int) n.getProperty().getInt("klout"));
        assertFalse(rs.next());
        rs.close();

        pstmt = con.prepareStatement("MATCH ( ee:person {'name': ? } ) return ee");
        pstmt.setString(1, "Emil");
        rs = pstmt.executeQuery();
        assertTrue(rs.next());
        n = (Vertex) rs.getObject("ee");
        assertEquals(99, (int) n.getProperty().getInt("klout"));
        assertFalse(rs.next());
        rs.close();
        pstmt.close();
    }

    public void testWhereBind_Json() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("MATCH ( ee:person ) WHERE ee.name = ? return ee");
        pstmt.setString(1, "Emil");
        ResultSet rs = pstmt.executeQuery();
        assertTrue(rs.next());
        Vertex n = (Vertex)rs.getObject("ee");
        assertEquals(99, (int)n.getProperty().getInt("klout"));
        assertFalse(rs.next());
        rs.close();
        pstmt.close();
    }

    public void testMatchBind_Str() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("MATCH ( ee:person =to_jsonb(?) ) return ee");
        pstmt.setString(1, "{ \"name\": \"Emil\" }");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("ee");
            assertEquals(99, (int)n.getProperty().getInt("klout"));
        }
        assertFalse(rs.next());
        rs.close();
        pstmt.close();
    }

    public void testMatchBind_Json() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("MATCH ( ee:person ? ) return ee");
        JsonObject nameFilter = new JsonObject();
        nameFilter.put("name", "Emil");
        pstmt.setObject(1, nameFilter);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("ee");
            assertEquals(99, (int)n.getProperty().getInt("klout"));
        }
        assertFalse(rs.next());
        rs.close();
        pstmt.close();
    }
}
