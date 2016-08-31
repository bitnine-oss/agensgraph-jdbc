package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.postgresql.util.PGobject;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

public class Jsonb extends PGobject implements Serializable, Closeable {

    private Object jsonValue;
    private JsonType type;

    public Jsonb() {
        setType("jsonb");
    }

    @Override
    public void setValue(String value) throws SQLException {
        if (value == null) {
            jsonValue = null;
            type = JsonType.NULL;
            return;
        }
        Object o = JSONValue.parse(value);
        if (o instanceof JSONObject) {
            jsonValue = new JsonObject((JSONObject)o);
            type = JsonType.OBJECT;
        }
        else if (o instanceof JSONArray) {
            jsonValue = new JsonArray((JSONArray)o);
            type = JsonType.ARRAY;
        }
        else {
            jsonValue = o;
            if (o instanceof String) {
                type = JsonType.STRING;
            }
            else if (o instanceof Long) {
                type = JsonType.LONG;
            }
            else if (o instanceof Double) {
                type = JsonType.DOUBLE;
            }
            else if (o instanceof Boolean) {
                type = JsonType.BOOLEAN;
            }
            else {
                throw new SQLException("invalid json string");
            }
        }
    }

    @Override
    public String getValue() {
        return jsonValue.toString();
    }

    @Override
    public void close() throws IOException {

    }

    public JsonObject jo() {
        return (JsonObject)getJsonValue();
    }

    public JsonArray ja() {
        return (JsonArray)getJsonValue();
    }

    public JsonObject getJsonObject() {
        return jo();
    }

    public JsonArray getJsonArray() {
        return ja();
    }

    public String getString() {
        return (String)getJsonValue();
    }

    public Integer getInt() {
        Long value = getLong();
        return (value == null) ? null : value.intValue();
    }

    public Long getLong() {
        return (Long)getJsonValue();
    }

    public Double getDouble() {
        return (Double)getJsonValue();
    }

    public Object getJsonValue() {
        return jsonValue;
    }

    public JsonType getJsonType() {
        return type;
    }
}
