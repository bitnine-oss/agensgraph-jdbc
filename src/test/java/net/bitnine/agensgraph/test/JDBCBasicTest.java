package net.bitnine.agensgraph.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCBasicTest extends TestCase {

    private Connection con;

    public void setUp() throws Exception {
        con = TestUtil.openDB();

        Statement st = con.createStatement();
        st.execute("create vlabel person");
        st.execute("create elabel employee");
    }

    public void tearDown() throws Exception {
        TestUtil.dropTable(con, "test_a");
        Statement st = con.createStatement();
        st.execute("drop vlabel person");
        st.execute("drop elabel employee");

        TestUtil.closeDB(con);
    }

    public void test1() throws Exception {
        System.out.println("test case no. 1");
        Statement st = con.createStatement();
        assertNotNull(st);

        st.executeUpdate("insert into graph.person values (1, '{\"name\": \"bitnine\"}')");
        st.executeUpdate("insert into graph.person values (2, '{\"name\": \"jsyang\"}')");
        st.executeUpdate("insert into graph.person values (3, '{\"name\": \"someone\"}')");
        st.executeUpdate("insert into graph.person values (4, '{\"name\": \"tree\"}')");

        st.executeUpdate("insert into graph.employee values (1, 'graph.person'::regclass, 1, 'graph.person'::regclass, 2, '{\"prop\": \"employee\"}')");
        st.executeUpdate("insert into graph.employee values (2, 'graph.person'::regclass, 2, 'graph.person'::regclass, 3, '{\"prop\": \"manage\"}')");
        st.executeUpdate("insert into graph.employee values (3, 'graph.person'::regclass, 1, 'graph.person'::regclass, 4, '{\"prop\": \"branch\"}')");

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
