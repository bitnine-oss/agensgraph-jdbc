package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Vertex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MatchTest extends TestCase {

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
        st.execute("create graph u");
        st.execute("set graph_path = u");
        st.execute("create vlabel company");
        st.execute("create vlabel person");
        st.execute("create elabel employee");
        st.execute("create elabel manage");
        create();
    }

    private void dropSchema() throws Exception {
        st.execute("drop graph u cascade");
    }

    private void create() throws Exception {
        st.execute("create (:company '{\"name\":\"bitnine\"}')"
                + "-[:employee]"
                + "->(:person '{\"name\":\"kskim\"}')"
                + "-[:manage]"
                + "->(:person '{\"name\":\"ktlee\"}')");
        st.execute("match (c:company '{\"name\":\"bitnine\"}') "
                + "create (c)-[:employee]"
                + "->(:person '{\"name\":\"jsyang\"}')");
        st.execute("match (c:company '{\"name\":\"bitnine\"}') "
                + ", (p:person '{\"name\":\"ktlee\"}') "
                + "create (c)-[:employee]->(p)");
        st.execute("match (m:person '{\"name\":\"kskim\"}')"
                +", (p:person '{\"name\":\"jsyang\"}') "
                +"create (m)-[:manage]->(p)");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    public void testMatch() throws Exception {
        ResultSet rs = st.executeQuery("MATCH (c)-[e]->(p1)-[m]->(p2) RETURN p1, p2");
        while (rs.next()) {
            Vertex boss = (Vertex)rs.getObject("p1");
            assertEquals("person", boss.getLabel());
            assertEquals("kskim", boss.getProperty().getString("name"));
            Vertex member = (Vertex)rs.getObject("p2");
            String memberName = member.getProperty().getString("name");
            //FIXME use ORDER By clause
            assertTrue(memberName.equals("ktlee") || memberName.equals("jsyang"));
        }
        rs.close();
    }
}
