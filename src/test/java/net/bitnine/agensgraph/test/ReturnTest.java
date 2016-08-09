package net.bitnine.agensgraph.test;

import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

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
    }
}
