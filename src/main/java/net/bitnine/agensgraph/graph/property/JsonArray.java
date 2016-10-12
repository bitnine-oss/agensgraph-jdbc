/*
 * Copyright (c) 2014-2016, Bitnine Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bitnine.agensgraph.graph.property;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends Jsonb {
    private JSONArray array;

    public JsonArray() {
        array = new JSONArray();
        setJsonValue(this);
    }

    public JsonArray(String s) {
        array = (JSONArray) JSONValue.parse(s);
        if (array == null)
            throw new IllegalArgumentException("invalid json array format string");
        setJsonValue(this);
    }

    protected JsonArray(JSONArray json) {
        array = json;
        setJsonValue(this);
    }

    public static JsonArray create() {
        return new JsonArray();
    }

    public static JsonArray create(String s) {
        return new JsonArray(s);
    }

    public static JsonArray create(Object... values) {
        JsonArray array = new JsonArray();
        for (Object value : values) {
            if (Jsonb.isJsonValue(value))
                array.add(value);
            else
                throw new IllegalArgumentException("invalid json value type: " + value.getClass());
        }
        return array;
    }

    public static JsonArray create(List<?> list) {
        JsonArray array = JsonArray.create();
        if (list == null || list.isEmpty())
            return array;

        Iterator<?> iter = list.iterator();
        while (iter.hasNext()) {
            Object value = iter.next();

            if (value == null)
                array.addNull();
            else if (isJsonValue(value))
                array.add(value);
            else
                throw new IllegalArgumentException("invalid json value type: " + value.getClass());
        }

        return array;
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
        return new JsonObject((JSONObject) get(index));
    }

    public JsonArray getArray(int index) {
        return new JsonArray((JSONArray) get(index));
    }

    public String getString(int index) {
        return (String) get(index);
    }

    public Boolean getBoolean(int index) {
        return (Boolean) get(index);
    }

    public Integer getInt(int index) {
        Long value = getLong(index);
        return (value == null) ? null : value.intValue();
    }

    public Long getLong(int index) {
        return (Long) get(index);
    }

    public Double getDouble(int index) {
        return (Double) get(index);
    }

    public Iterator<Object> iterator() {
        ArrayList<Object> l = new ArrayList<>();
        for (Object elem : array) {
            if (elem instanceof JSONObject) {
                l.add(new JsonObject((JSONObject) elem));
            } else if (elem instanceof JSONArray) {
                l.add(new JsonArray((JSONArray) elem));
            } else {
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

    public JsonArray add(boolean value) {
        array.add(value);
        return this;
    }

    public JsonArray add(int value) {
        array.add(new Long(value));
        return this;
    }

    public JsonArray add(long value) {
        array.add(value);
        return this;
    }

    public JsonArray add(double value) {
        array.add(value);
        return this;
    }

    public JsonArray add(String value) {
        array.add(value);
        return this;
    }

    public JsonArray add(JsonArray value) {
        array.add(value);
        return this;
    }

    public JsonArray add(JsonObject value) {
        array.add(value);
        return this;
    }

    public JsonArray addNull() {
        array.add(null);
        return this;
    }

    public JsonArray add(Object value) {
        if (isJsonValue(value))
            array.add(value);
        else
            throw new IllegalArgumentException("invalid json value type");
        return this;
    }
}