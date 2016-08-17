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
            st.execute("drop vlabel child");
            st.execute("drop vlabel parent");
        }
        catch (Exception ignored) {}
        st.execute("create vlabel parent");
        st.execute("create vlabel child inherits (parent)");
        create();
    }

    private void create() throws Exception {
        st.execute("create (:parent '{\"name\":\"father\"}')");
        st.execute("create (:child '{\"name\":\"son\"}')");
    }

    public void tearDown() throws Exception {
        st.execute("drop vlabel child");
        st.execute("drop vlabel parent");
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
            assertEquals("son", v.getProperty().getString("name"));
            ++i;
        }
        assertEquals(1, i);
        rs.close();
    }
}
