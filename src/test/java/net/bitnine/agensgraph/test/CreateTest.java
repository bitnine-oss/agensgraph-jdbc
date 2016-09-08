package net.bitnine.agensgraph.test;

import junit.framework.TestCase;
import net.bitnine.agensgraph.graph.property.JsonObject;
import net.bitnine.agensgraph.graph.property.Jsonb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CreateTest extends TestCase {

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
        st.execute("create vlabel person");
    }

    private void dropSchema() throws Exception {
        st.execute("drop vlabel person");
    }

    public void tearDown() throws Exception {
        dropSchema();
        st.close();
        TestUtil.closeDB(con);
    }

    public void testCreateWithBind() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE ( :person { 'name': 'XXX', 'from': 'Sweden', 'klout': ? } )");
        pstmt.setInt(1, 99);
        boolean inserted = pstmt.execute();
        assertFalse(inserted);
    }

    public void testCreateWithBind_WholeProp() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE ( :person ? )");
        JsonObject ktlee = new JsonObject();
        ktlee.put("name", "ktlee");
        ktlee.put("from", "Korea");
        ktlee.put("klout", 17);
        pstmt.setObject(1, ktlee);
        boolean inserted = pstmt.execute();
        assertFalse(inserted);
    }

    public void testCreateWithBind_PrimitiveProp() throws Exception {
        PreparedStatement pstmt = con.prepareStatement("CREATE ( :person ? )");
        pstmt.setObject(1, new Jsonb(10));
        try {
            pstmt.execute();
            assertTrue(false);
        }
        catch (Exception e) {
            assertEquals("ERROR: jsonb object is expected for property map", e.getMessage());
        }
    }
}
