package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.NamedParameterStatement;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.graph.property.JsonObject;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NamedParameterTest extends TestCase {
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
        st.execute("create vlabel person");
        create();
    }

    private void create() throws Exception {
        st.execute("CREATE (:person { 'name': 'Emil', 'from': 'Sweden', 'klout': 99 })");
        st.execute("CREATE (:person { 'name': 'Unna', 'from': 'Sweden', 'klout': 14 })");
    }

    private void dropSchema() throws Exception {
        st.execute("drop graph u cascade");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    public void testPrimitives() throws Exception {
        NamedParameterStatement npstmt = new NamedParameterStatement(con,
                "MATCH (ee:person) WHERE ee.name = $name return ee");
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Emil");
        ResultSet rs = npstmt.executeQuery(params);
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals(99, (int) n.getInt("klout"));
        }
        rs.close();

        npstmt.clearParameters();
        params.put("name", "Unna");
        rs = npstmt.executeQuery(params);
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals(14, (int) n.getInt("klout"));
        }
        rs.close();
        npstmt.close();

        npstmt.prepare("MATCH (ee:person) WHERE ee.klout::int = $klout return ee");
        params.put("klout", 99);
        rs = npstmt.executeQuery(params);
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals("Sweden", n.getString("from"));
        }
        rs.close();
        npstmt.close();

        npstmt.prepare("MATCH ( ee:person {'name': $name } ) return ee");
        params.put("name", "Emil");
        rs = npstmt.executeQuery(params);
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals("Sweden", n.getString("from"));
        }
        rs.close();
        npstmt.close();

        params.put("name", "Emil");
        rs = npstmt.executeQuery("MATCH ( ee:person {'name': $name } ) return ee", params);
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals("Sweden", n.getString("from"));
        }
        rs.close();
        npstmt.close();
    }

    public void testMultiParams() throws Exception {
        NamedParameterStatement npstmt = new NamedParameterStatement(con);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Emil");
        params.put("klout", 99);
        npstmt.prepare("MATCH (ee:person) WHERE ee.klout::int = $klout and ee.name = $name return ee");
        ResultSet rs = npstmt.executeQuery(params);
        while (rs.next()) {
            Vertex n = (Vertex) rs.getObject("ee");
            assertEquals("Sweden", n.getString("from"));
        }
        rs.close();
        npstmt.close();

        npstmt.prepare("MATCH (ee:person) WHERE ee.klout::int = $klout and ee.name = $name return $name");
        rs = npstmt.executeQuery(params);
        while (rs.next()) {
            String name = rs.getString(1);
            assertEquals("Emil", name);
        }
        rs.close();
        npstmt.close();
    }

    public void testJson() throws Exception {
        NamedParameterStatement npstmt = new NamedParameterStatement(con);
        npstmt.prepare("MATCH ( ee:person $attr ) return ee");
        JsonObject nameFilter = new JsonObject();
        nameFilter.put("name", "Emil");
        Map<String, Object> params = new HashMap<>();
        params.put("attr", nameFilter);
        ResultSet rs = npstmt.executeQuery(params);
        while (rs.next()) {
            Vertex n = (Vertex)rs.getObject("ee");
            assertEquals(99, (int)n.getProperty().getInt("klout"));
        }
        rs.close();
        npstmt.close();
    }

    public void testErrors() throws Exception {
        NamedParameterStatement npstmt = new NamedParameterStatement(con);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Emil");
        try {
            npstmt.executeQuery(params);
        }
        catch (PSQLException e) {
            assertEquals("This statement is not prepared", e.getMessage());
        }
        npstmt.prepare("MATCH (ee:person) WHERE ee.name = $nam return ee");
        try {
            npstmt.executeQuery(params);
        }
        catch (PSQLException e) {
            assertEquals("No value specified for parameter nam.", e.getMessage());
        }
        try {
            npstmt.prepare("MATCH (ee:person) WHERE ee.name = $_name return ee");
        }
        catch (PSQLException e) {
            assertEquals("Invalid parameter name _name.", e.getMessage());
        }
    }
}
