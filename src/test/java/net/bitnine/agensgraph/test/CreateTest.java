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
import net.bitnine.agensgraph.graph.property.JsonObject;
import net.bitnine.agensgraph.graph.property.Jsonb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CreateTest extends TestCase {

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
        st.execute("CREATE GRAPH u");
        st.execute("SET graph_path = u");
        st.execute("CREATE VLABEL person");
    }

    private void dropSchema() throws Exception {
        st.execute("DROP GRAPH u CASCADE");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    public void testCreateWithBind() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE (:person {name: 'XXX', from: 'Sweden', klout: ?})");
        pstmt.setInt(1, 99);
        boolean inserted = pstmt.execute();
        assertFalse(inserted);
    }

    public void testCreateWithBind_WholeProp() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE (:person ?)");
        JsonObject ktlee = new JsonObject();
        ktlee.put("name", "ktlee");
        ktlee.put("from", "Korea");
        ktlee.put("klout", 17);
        pstmt.setObject(1, ktlee);
        boolean inserted = pstmt.execute();
        assertFalse(inserted);
    }

    public void testCreateWithBind_PrimitiveProp() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE (:person ?)");
        pstmt.setObject(1, new Jsonb(10));
        try {
            pstmt.execute();
            assertTrue(false);
        }
        catch (Exception e) {
            assertEquals("ERROR: jsonb object is expected for property map", e.getMessage());
        }
    }

    public void testCreate_EscapeSyntax() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE (:person {txid: ?})");
        pstmt.setObject(1, new Jsonb(10));
        try {
            pstmt.execute();
            assertTrue(true);
        }
        catch (Exception e) {
            assertTrue(false);
        }
    }
}
