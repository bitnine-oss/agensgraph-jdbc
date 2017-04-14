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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LabelInheritTest extends TestCase {

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
        st.execute("create vlabel parent");
        st.execute("create vlabel child inherits (parent)");
        create();
    }

    private void create() throws Exception {
        st.execute("create (:parent '{\"name\":\"father\"}')");
        st.execute("create (:child '{\"name\":\"son\"}')");
    }

    private void dropSchema() throws Exception {
        st.execute("drop graph u cascade");
    }

    public void tearDown() throws Exception {
        st.close();
        TestUtil.closeDB(con);
    }

    public void testMultiLabel() throws Exception {
        ResultSet rs = st.executeQuery("MATCH (x:parent) RETURN x");
        int i = 0;
        while (rs.next()) {
            ++i;
        }
        assertEquals(2, i);
        rs = st.executeQuery("MATCH (x:child) RETURN x");
        i = 0;
        while (rs.next()) {
            Vertex v = (Vertex)rs.getObject("x");
            assertEquals("son", v.getString("name"));
            ++i;
        }
        assertEquals(1, i);
        rs.close();
    }
}
