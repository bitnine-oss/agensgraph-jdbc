package net.bitnine.agensgraph.graph;

import org.postgresql.util.GT;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vertex extends PGobject implements Serializable, Closeable {
	private static Pattern _pattern;
	public GID vid;
	public String properties;

	{
		_pattern = Pattern.compile("\\[(\\d+):(\\d+)\\](.*)");
	}

	public Vertex() {
		setType("vertex");
	}

	public Vertex(GID vid, String properties) {
		this();
		this.vid = vid;
		this.properties = properties;
	}

	public Vertex(String s) throws SQLException {
		this();
		setValue(s);
	}

	@Override
	public void setValue(String s) throws SQLException {
		Matcher m = _pattern.matcher(s);
		if (m.find()) {
			vid = new GID(m.group(1), m.group(2));
			properties = m.group(3);
		}
		else {
			throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, s}),
					PSQLState.DATA_TYPE_MISMATCH);
		}
	}

	@Override
	public String getValue() {
		return "vertex ID:" + vid.toString() + ", properties:" + properties;
	}

	@Override
	public void close() throws IOException {

	}
}
