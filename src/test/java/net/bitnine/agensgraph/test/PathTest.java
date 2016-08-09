package net.bitnine.agensgraph.test;

import net.bitnine.agensgraph.graph.Edge;
import net.bitnine.agensgraph.graph.Path;
import net.bitnine.agensgraph.graph.Vertex;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class PathTest extends TestCase {

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

	public void testPath() throws Exception {
		ResultSet rs = st.executeQuery("MATCH p=()-[]->() RETURN p");
		while (rs.next()) {
			Path p = (Path)rs.getObject("p");
			System.out.println("path: " + p.toString());
			System.out.println("|- start node: " + p.start().toString());
			for (Vertex v : p.vertexs()) {
				System.out.println("|- vertex: " + v.toString());
			}
			for (Edge v : p.edges()) {
				System.out.println("|- edge: " + v.toString());
			}
			System.out.println("`- length: " + p.length());
		}
		rs.close();
	}
}
