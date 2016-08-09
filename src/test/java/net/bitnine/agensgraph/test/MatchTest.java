package net.bitnine.agensgraph.test;

import org.junit.BeforeClass;
import org.junit.AfterClass;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class MatchTest extends TestCase {

    private Connection con;
    private Statement st;

    public void setUp() throws Exception {
        con = TestUtil.openDB();
        con.setAutoCommit(true);
        st = con.createStatement();
        try {
            st.execute("drop vlabel person");
            st.execute("drop elabel employee");
        }
        catch (Exception e) {}
        st.execute("create vlabel person");
        st.execute("create elabel employee");
        create();
    }

    private void create() throws Exception {
        st.execute("create (:person '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"prop\":\"employee\"}']"
                + "->(:person '{\"name\":\"jsyang\"}')"
                + "-[:employee '{\"prop\":\"manage\"}']"
                + "->(:person '{\"name\":\"someone\"}')");
        st.execute("match (p:person '{\"name\":\"bitnine\"}')"
                + "create (p)-[:employee '{\"prop\":\"branch\"}']"
                + "->(:person '{\"name\":\"tree\"}')");
    }

    public void tearDown() throws Exception {
        st.execute("drop vlabel person");
        st.execute("drop elabel employee");
        st.close();
        TestUtil.closeDB(con);
    }

    public void testMatch() throws Exception {
        ResultSet rs = st.executeQuery("MATCH (n)<-[r]-(m), (m)-[s]->(q) RETURN n, r, m, s, q");
        while (rs.next()) {
            String n = rs.getObject("n").toString();
            String r = rs.getObject("r").toString();
            String m = rs.getObject("m").toString();
            String s = rs.getObject("s").toString();
            String q = rs.getObject("q").toString();
            System.out.println("row: " + n + ", " + r + ", " + m + ", " + s + ", " + q);
        }
        rs.close();
    }
}
