package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.graph.property.JsonArray;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PropertyTest extends TestCase {

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
        catch (Exception ignored) {}
        st.execute("create vlabel person");
        st.execute("create elabel employee");
        create();
    }

    private void create() throws Exception {
        st.execute("create (:person '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"no\":1}']"
                + "->(:person '{\"name\":\"jsyang\",\"age\":20,\"height\":178.5,\"married\":false}')");
        st.execute("create (:person '{\"name\":\"bitnine\"}')"
                + "-[:employee '{\"no\":2}']"
                + "->(:person '{\"array\":[1,2,3,4,5]}')");
    }

    public void tearDown() throws Exception {
        st.execute("drop vlabel person");
        st.execute("drop elabel employee");
        st.close();
        TestUtil.closeDB(con);
    }

    public void testProperty() throws Exception {
        ResultSet rs = st.executeQuery("MATCH (n)-[:employee '{\"no\":1}']->(m) RETURN n, m");
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("m");
            assertEquals((int)n.getProperty().getInt("age"), 20);
            assertEquals(n.getProperty().getDouble("height"), 178.5);
            assertFalse(n.getProperty().getBoolean("married"));
        }
        rs = st.executeQuery("MATCH (n)-[:employee '{\"no\":2}']->(m) RETURN n, m");
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("m");
            JsonArray array = n.getProperty().getArray("array");
            assertEquals((int)array.getInt(3), 4);
        }
        rs.close();
    }
}
