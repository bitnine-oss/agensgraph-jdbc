package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonObject {
    JSONObject props;

    public JsonObject() {
        props = new JSONObject();
    }

    public JsonObject(String s) {
        props = (JSONObject) JSONValue.parse(s);
        if (props == null)
            throw new IllegalArgumentException("invalid json object format string");
    }

    JsonObject(JSONObject json) {
        props = json;
    }

    public static JsonObject create() {
        return new JsonObject();
    }

    public static JsonObject create(String s) {
        return new JsonObject(s);
    }

    public static JsonObject create(Map<String, ?> map) {
        if (map == null || map.isEmpty())
            return new JsonObject();

        JsonObject jobj = new JsonObject();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key == null)
                throw new IllegalArgumentException("'null' key is not allowed in JsonObject");
            Object value = entry.getValue();
            if (Jsonb.isJsonValue(value))
                jobj.put(key, value);
            else
                throw new IllegalArgumentException("invalid json value type");
        }

        return jobj;
    }

    public boolean containsKey(String name) {
        return props.containsKey(name);
    }

    public boolean containsValue(Object value) {
        return props.containsValue(value);
    }

    public boolean equals(Object o) {
        return props.equals(o);
    }

    public boolean isEmpty() {
        return props.isEmpty();
    }

    private Object get(String name) {
        return props.get(name);
    }

    public JsonObject getObject(String name) {
        return new JsonObject((JSONObject)get(name));
    }

    public JsonArray getArray(String name) {
        return new JsonArray((JSONArray)get(name));
    }

    public String getString(String name) {
        return (String)get(name);
    }

    public Boolean getBoolean(String name) {
        return (Boolean)get(name);
    }

    public Integer getInt(String name) {
        Long value = getLong(name);
        return (value == null) ? null : value.intValue();
    }

    public Long getLong(String name) {
        return (Long)get(name);
    }

    public Double getDouble(String name) {
        return (Double)get(name);
    }

    public Set<String> getNames() {
        return (Set<String>)props.keySet();
    }

    public int size() {
        return props.size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();
        for (String name : getNames()) {
            Object value = props.get(name);
            if (value instanceof JSONObject) {
                newMap.put(name, new JsonObject((JSONObject)value));
            }
            else if (value instanceof JSONArray) {
                newMap.put(name, new JsonArray((JSONArray)value));
            }
            else {
                newMap.put(name, value);
            }
        }
        return newMap;
    }

    public String toString() {
        return props.toString();
    }

    public JsonObject put(String name, boolean value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, int value) {
        props.put(name, new Long(value));
        return this;
    }

    public JsonObject put(String name, long value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, double value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, String value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, JsonObject value) {
        props.put(name, value);
        return this;
    }

    public JsonObject put(String name, JsonArray value) {
        props.put(name, value);
        return this;
    }

    public JsonObject putNull(String name) {
        props.put(name, null);
        return this;
    }

    public JsonObject put(String name, Object value) {
        if (Jsonb.isJsonValue(value))
            props.put(name, value);
        else
            throw new IllegalArgumentException("invalid json value type");
        return this;
    }
}
