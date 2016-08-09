package net.bitnine.agensgraph.test;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.Statement;

public class JDBCBasicTest extends TestCase {
    private Connection con;
    private Statement st;

    public void setUp() throws Exception {
        con = TestUtil.openDB();
        con.setAutoCommit(true);
        st = con.createStatement();
        try {
            st.execute("drop vlabel person");
            st.execute("drop elabel employee");
        } catch (Exception ignore) {
        }

        st.execute("create vlabel person");
        st.execute("create elabel employee");
    }

    public void tearDown() throws Exception {
        st.execute("drop vlabel person");
        st.execute("drop elabel employee");
        st.close();
        TestUtil.closeDB(con);
    }

    public void testCreate() throws Exception {
        st.execute("create (:person '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"prop\":\"employee\"}']->"
                + "(:person '{\"name\":\"jsyang\"}')"
                + "-[:employee '{\"prop\":\"manage\"}']->"
                + "(:person '{\"name\":\"someone\"}')");
        st.execute("match (p:person '{\"name\":\"bitnine\"}')"
                + "create (p)-[:employee '{\"prop\":\"branch\"}']->"
                + "(:person '{\"name\":\"tree\"}')");
        assertTrue(true);
    }
}
