package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonArray {
    private JSONArray array;

    public JsonArray(String s) {
        array = (JSONArray)JSONValue.parse(s);
    }

    protected JsonArray(JSONArray json) {
        array = json;
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public boolean equals(Object o) {
        return array.equals(o);
    }

    private Object get(int index) {
        return array.get(index);
    }

    public JsonObject getObject(int index) {
        return new JsonObject((JSONObject)get(index));
    }

    public JsonArray getArray(int index) {
        return new JsonArray((JSONArray)get(index));
    }

    public String getString(int index) {
        return (String)get(index);
    }

    public Boolean getBoolean(int index) {
        return (Boolean)get(index);
    }

    public Integer getInt(int index) {
        Long value = getLong(index);
        return (value == null) ? null : value.intValue();
    }

    public Long getLong(int index) {
        return (Long)get(index);
    }

    public Double getDouble(int index) {
        return (Double)get(index);
    }

    public Iterator<Object> iterator() {
        ArrayList<Object> l = new ArrayList<>();
        for (Object elem : array) {
            if (elem instanceof JSONObject) {
                l.add(new JsonObject((JSONObject)elem));
            }
            else if (elem instanceof JSONArray) {
                l.add(new JsonArray((JSONArray)elem));
            }
            else {
                l.add(elem);
            }
        }
        return l.iterator();
    }

    public int size() {
        return array.size();
    }

    public String toString() {
        return array.toString();
    }
}
