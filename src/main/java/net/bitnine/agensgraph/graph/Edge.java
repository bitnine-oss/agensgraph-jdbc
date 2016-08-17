package net.bitnine.agensgraph.graph;

import net.bitnine.agensgraph.graph.property.JsonObject;
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
    private GID vid;
    private String label;
    private String properties;
    private JsonObject props;

    static {
        _pattern = Pattern.compile(":(.+)\\[(\\d+):(\\d+)\\](.*)");
    }

    private Edge() {
        setType("edge");
    }

    Edge(String s) throws SQLException {
        this();
        setValue(s);
    }

    public void setValue(String s) throws SQLException {
        Matcher m = _pattern.matcher(s);
        if (m.find()) {
            label = m.group(1);
            vid = new GID(m.group(2), m.group(3));
            properties = m.group(4);
            props = new JsonObject(properties);
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

    public JsonObject getProperty() {
        return props;
    }
}
