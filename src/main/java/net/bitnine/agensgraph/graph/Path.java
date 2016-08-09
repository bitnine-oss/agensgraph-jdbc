package net.bitnine.agensgraph.graph;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.bitnine.agensgraph.util.TopCommaTokenizer;
import org.postgresql.util.GT;
import org.postgresql.util.PGobject;
import org.postgresql.util.PGtokenizer;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

public class Path 
		extends PGobject 
		implements Serializable, Closeable {

	private List<Vertex> vertexs;
	private List<Edge> edges;
	private String s;

    public Path() {
        setType("path");
    }

	@Override
	public void setValue(String s) throws SQLException {
		this.s = s;
		String p = new String(s);
		vertexs = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		p = PGtokenizer.removeBox(p);
		TopCommaTokenizer t;
		try {
			t = new TopCommaTokenizer(p);
		}
		catch (Exception e) {
			throw new PSQLException(GT.tr(
						"Conversion to type {0} failed: {1}."
						, new Object[]{type, s})
					, PSQLState.DATA_TYPE_MISMATCH);
		}
		for (int i = 0; i < t.getSize(); ++i) {
			if (i % 2 == 0)
				vertexs.add(new Vertex(t.getToken(i)));
			else
				edges.add(new Edge(t.getToken(i)));
		}
	}

	@Override
	public String getValue() {
		return s;
	}

    @Override
    public void close() throws IOException {
    }

	public Vertex start() {
		return vertexs.get(0);
	}

	public Vertex end() {
		int size = vertexs.size();
		if (0 == size)
			return null;
		else
			return vertexs.get(size - 1);
	}

	public int length() {
		return edges.size();
	}

	public Iterable<Vertex> vertexs() {
		return (Iterable<Vertex>)vertexs;
	}

	public Iterable<Edge> edges() {
		return (Iterable<Edge>)edges;
	}
}
