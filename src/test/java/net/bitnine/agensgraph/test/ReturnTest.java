package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.property.JsonObject;
import net.bitnine.agensgraph.graph.property.Jsonb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
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
        ResultSet rs = st.executeQuery("RETURN 'be' || ' happy!', 1+1");
        while (rs.next()) {
            assertEquals("be happy!", rs.getString(1));
            assertEquals(2, rs.getInt(2));
        }
        rs.close();
        PreparedStatement pstmt = con.prepareStatement("RETURN ?");
        Jsonb data = new Jsonb();
        data.setJsonValue(JsonObject.create("{\"name\":\"ktlee\"}"));
        pstmt.setObject(1, data);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            JsonObject jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals("ktlee", jo.getString("name"));
        }
        rs.close();
        Map<String, Object> jobj = new HashMap<>();
        jobj.put("name", "ktlee");
        jobj.put("age", 41);
        data.setJsonValue(JsonObject.create(jobj));
        pstmt.setObject(1, data);
        rs = pstmt.executeQuery();
        while (rs.next()) {
            JsonObject jo = ((Jsonb)rs.getObject(1)).getJsonObject();
            assertEquals(41, jo.getInt("age").intValue());
        }
        rs.close();
    }
}
