package net.bitnine.agensgraph.test;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCBasicTest extends TestCase {
    private Connection con;

    public void setUp() throws Exception {
        con = TestUtil.openDB();

        Statement st = con.createStatement();
        st.execute("create vlabel person");
        st.execute("create elabel employee");
    }

    public void tearDown() throws Exception {
        Statement st = con.createStatement();
        st.execute("drop vlabel person");
        st.execute("drop elabel employee");

        TestUtil.closeDB(con);
    }

    public void test1() throws Exception {
        System.out.println("test case no. 1");
        Statement st = con.createStatement();
        assertNotNull(st);

        st.execute("create (:person '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"prop\":\"employee\"}']->"
                + "(:person '{\"name\":\"jsyang\"}')"
                + "-[:employee '{\"prop\":\"manage\"}']->"
                + "(:person '{\"name\":\"someone\"}')");

        st.execute("match (p:person '{\"name\":\"bitnine\"}')"
                + "create (p)-[:employee '{\"prop\":\"branch\"}']->"
                + "(:person '{\"name\":\"tree\"}')");

        ResultSet rs = st.executeQuery("MATCH (n)<-[r]-(m), (m)-[s]->(q) RETURN n, r, m, s, q");
        while (rs.next()) {
            String n = rs.getObject("n").toString();
            String r = rs.getObject("r").toString();
            String m = rs.getObject("m").toString();
            String s = rs.getObject("s").toString();
            String q = rs.getObject("q").toString();
            System.out.println("row: " + n + ", " + r + ", " + m + ", " + s + ", " + q);
        }
    }
}
