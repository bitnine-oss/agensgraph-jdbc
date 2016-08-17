package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PathTest extends TestCase {

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
        st.execute("create vlabel company");
        st.execute("create vlabel person");
        st.execute("create elabel employee");
        st.execute("create elabel manage");
        create();
    }

    private void dropSchema() throws Exception {
        st.execute("drop vlabel company");
        st.execute("drop vlabel person");
        st.execute("drop elabel employee");
        st.execute("drop elabel manage");
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

    private static String vnames[] = { "bitnine", "kskim", "ktlee" };
    private static String elabels[] = { "employee", "manage" };

    public void testPath() throws Exception {
        ResultSet rs = st.executeQuery("MATCH p=()-[]->()-[]->('{\"name\":\"ktlee\"}') RETURN p");
        while (rs.next()) {
            Path p = (Path)rs.getObject("p");
            int i = 0;
            for (Vertex v : p.vertexs()) {
                assertEquals(vnames[i++], v.getProperty().getString("name"));
            }
            i = 0;
            for (Edge e : p.edges()) {
                assertEquals(elabels[i++], e.getLabel());
            }
            assertEquals(2, p.length());
        }
        rs.close();
    }
}
