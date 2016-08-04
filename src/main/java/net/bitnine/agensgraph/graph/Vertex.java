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
    private static final Pattern _pattern;
    public GID vid;
    public String properties;

    static {
        _pattern = Pattern.compile("\\[(\\d+):(\\d+)\\](.*)");
    }

    public Vertex() {
        setType("vertex");
    }

    public Vertex(GID vid, String properties) {
        this.vid = vid;
        this.properties = properties;
    }

    public void setValue(String s) throws SQLException {
        Matcher m = _pattern.matcher(s);
        if (m.find()) {
            vid = new GID(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            properties = m.group(3);
        } else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, s}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }
    }

    public String getValue() {
        return "vertex ID:" + vid.toString() + ", properties:" + properties;
    }

    @Override
    public void close() throws IOException { }
}
