package net.bitnine.agensgraph.test;

import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

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
        catch (Exception e) {}
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
        System.out.println("Query Parent Label:");
        while (rs.next()) {
            System.out.println("|- " + rs.getObject("x").toString());
        }
        rs = st.executeQuery("MATCH (x:child) RETURN x");
        System.out.println("Query Child Label:");
        while (rs.next()) {
            System.out.println("|- " + rs.getObject("x").toString());
        }
        rs.close();
    }
}
