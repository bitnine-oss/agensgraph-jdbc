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

public class Edge extends PGobject implements Serializable, Closeable {
    private static Pattern _pattern;
    public GID vid;
    public String label;
    public String properties;

    {
        _pattern = Pattern.compile(":(.+)\\[(\\d+):(\\d+)\\](.*)");
    }

    public Edge() {
        setType("edge");
    }

    public void setValue(String s) throws SQLException {
        Matcher m = _pattern.matcher(s);
        if (m.find()) {
            label = m.group(1);
            vid = new GID(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            properties = m.group(4);
        }
        else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, s}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }
    }

    public String getValue() {
        return "edge ID:" + vid.toString() + ", label:" + label + " properties:" + properties;
    }

    @Override
    public void close() throws IOException {

    }
}
