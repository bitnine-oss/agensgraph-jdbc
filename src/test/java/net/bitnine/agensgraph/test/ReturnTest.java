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
import net.bitnine.agensgraph.graph.property.JsonArray;
import net.bitnine.agensgraph.graph.property.JsonObject;
import net.bitnine.agensgraph.graph.property.Jsonb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReturnTest extends TestCase {

    private Connection con;
    private Statement st;

    public void setUp() throws Exception {
        con = TestUtil.openDB();
        con.setAutoCommit(true);
        st = con.createStatement();
    }

    public void tearDown() throws Exception {
        st.close();
        TestUtil.closeDB(con);
    }

    public void testReturn() throws Exception {
        ResultSet rs = st.executeQuery("RETURN 'be' + ' happy!', 1 + 1");
        while (rs.next()) {
            Jsonb j = (Jsonb)rs.getObject(1);
            assertEquals("be happy!", j.getString());
            assertEquals(2, rs.getInt(2));
        }
        rs.close();
    }

    public void testSimpleBind() throws Exception {
        ResultSet rs;
        PreparedStatement pstmt = con.prepareStatement("RETURN ?");
        JsonObject jo;

        Jsonb data = new Jsonb();
        data.setJsonValue(JsonObject.create("{\"name\": \"ktlee\"}"));
        pstmt.setObject(1, data);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals("ktlee", jo.getString("name"));
        }
        rs.close();

        Map<String, Object> map = new HashMap<>();
        map.put("name", "ktlee");
        map.put("age", 41);
        pstmt.setObject(1, JsonObject.create(map));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals(41, jo.getInt("age").intValue());
        }
        rs.close();

        map.put("id", JsonArray.create(1, 2, 3));
        pstmt.setObject(1, JsonObject.create(map));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals(3, (int)jo.getArray("id").getInt(2));
        }
        rs.close();

        List<String> hobbies = new LinkedList<>();
        hobbies.add("climbing");
        hobbies.add("woodwork");
        map.put("hobbies", hobbies);
        pstmt.setObject(1, JsonObject.create(map));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals("woodwork", jo.getArray("hobbies").getString(1));
        }
        rs.close();

        Map<String, Object> physique = new HashMap<>();
        physique.put("height", 172);
        physique.put("weight", 74);
        map.put("physique", physique);
        pstmt.setObject(1, JsonObject.create(map));
        rs = pstmt.executeQuery();
        while (rs.next()) {
            jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals(172, (int)jo.getObject("physique").getInt("height"));
        }
        rs.close();

        data.setJsonValue(10);
        pstmt.setObject(1, data);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            assertEquals(10, (int)((Jsonb)rs.getObject(1)).getInt());
        }
        rs.close();
        pstmt.close();
    }
}
