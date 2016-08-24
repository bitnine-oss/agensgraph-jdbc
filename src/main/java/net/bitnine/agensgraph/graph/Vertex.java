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

public class Vertex extends PGobject implements Serializable, Closeable {
    private static final Pattern _pattern;
    static {
        _pattern = Pattern.compile("(.+)\\[(\\d+)\\.(\\d+)\\](.*)");
    }

    private String label;
    private GID vid;
    private JsonObject props;

    @SuppressWarnings("WeakerAccess")
    public Vertex() {
        setType("vertex");
    }

    Vertex(String s) throws SQLException {
        this();
        setValue(s);
    }

    @Override
    public void setValue(String s) throws SQLException {
        Matcher m = _pattern.matcher(s);
        if (m.find()) {
            label = m.group(1);
            vid = new GID(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            String property = m.group(4);
            if (property == null)
                props = null;
            else
                props = new JsonObject(property);
        } else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, s}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }
    }

    public String getValue() {
        return label + vid.toString() + ((props == null) ? "" : props.toString());
    }

    @Override
    public void close() throws IOException {
    }

    public String getLabel() {
        return label;
    }

    public JsonObject getProperty() {
        return props;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
