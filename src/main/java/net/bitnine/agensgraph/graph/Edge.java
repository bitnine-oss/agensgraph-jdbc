package net.bitnine.agensgraph.graph;

import net.bitnine.agensgraph.graph.property.JsonObject;
import org.postgresql.util.*;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edge extends PGobject implements Serializable, Closeable {
    private static Pattern _pattern;
    static {
        _pattern = Pattern.compile("(.+)\\[(\\d+)\\.(\\d+)\\]\\[(\\d+)\\.(\\d+),(\\d+)\\.(\\d+)\\](.*)");
    }

    private GID eid;
    private GID startVid;
    private GID endVid;
    private String label;
    private String properties;
    private JsonObject props;

    @SuppressWarnings("WeakerAccess")
    public Edge() {
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
            eid = new GID(m.group(2), m.group(3));
            startVid = new GID(m.group(4), m.group(5));
            endVid = new GID(m.group(6), m.group(7));
            String properties = m.group(8);
            if (properties == null)
                props = null;
            else
                props = new JsonObject(properties);
        }
        else {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[]{type, s}),
                    PSQLState.DATA_TYPE_MISMATCH);
        }
    }

    public String getValue() {
        return label + eid.toString()
                + "[" + PGtokenizer.removeBox(startVid.toString()) + ","
                + PGtokenizer.removeBox(endVid.toString()) + "]"
                + ((props == null) ? "" : props.toString());
    }

    @Override
    public void close() throws IOException {

    }

    public JsonObject getProperty() {
        return props;
    }
}
