/*
 * Copyright (c) 2014-2017, Bitnine Inc.
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

package net.bitnine.agensgraph.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.io.Serializable;
import java.sql.SQLException;

public class Jsonb extends PGobject implements JsonbObject, Serializable, Cloneable {
    private Object jsonValue = null;

    public Jsonb() {
        setType("jsonb");
    }

    Jsonb(Object obj) {
        this();

        jsonValue = obj;
    }

    @Override
    public void setValue(String value) throws SQLException {
        Object obj;
        try {
            obj = JSONValue.parseWithException(value);
        } catch (Exception e) {
            throw new PSQLException("Parsing jsonb failed", PSQLState.DATA_ERROR, e);
        }

        super.setValue(value);

        this.jsonValue = obj;
    }

    @Override
    public String getValue() {
        if (value == null)
            value = JSONValue.toJSONString(jsonValue);

        return value;
    }

    public Object getJsonValue() {
        return jsonValue;
    }

    private String getString(Object obj) {
        if (obj instanceof String)
            return (String) obj;

        throw new UnsupportedOperationException("Not a string: " + obj);
    }

    private int getInt(Object obj) {
        if (obj instanceof Long) {
            long l = (Long) obj;
            if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE)
                throw new IllegalArgumentException("Bad value for type int: " + l);

            return (int) l;
        }

        throw new UnsupportedOperationException("Not an int: " + obj);
    }

    private long getLong(Object obj) {
        if (obj instanceof Long)
            return (Long) obj;

        throw new UnsupportedOperationException("Not a long: " + obj);
    }

    private double getDouble(Object obj) {
        if (obj instanceof Double)
            return (Double) obj;

        throw new UnsupportedOperationException("Not a double: " + obj);
    }

    private boolean getBoolean(Object obj) {
        if (obj instanceof Boolean)
            return (Boolean) obj;

        if (obj instanceof String)
            return ((String) obj).length() > 0;
        else if (obj instanceof Long)
            return (Long) obj != 0L;
        else if (obj instanceof Double)
            return (Double) obj != 0.0;
        else if (obj instanceof JSONArray)
            return ((JSONArray) obj).size() > 0;
        else if (obj instanceof JSONObject)
            return ((JSONObject) obj).size() > 0;
        else
            return false;
    }

    private Jsonb getArray(Object obj) {
        if (obj instanceof JSONArray)
            return new Jsonb(obj);

        throw new UnsupportedOperationException("Not an array: " + obj);
    }

    private Jsonb getObject(Object obj) {
        if (obj instanceof JSONObject)
            return new Jsonb(obj);

        throw new UnsupportedOperationException("Not an object: " + obj);
    }

    public String tryGetString() {
        if (jsonValue instanceof String)
            return (String) jsonValue;

        return null;
    }

    public String getString() {
        return getString(jsonValue);
    }

    public int getInt() {
        return getInt(jsonValue);
    }

    public long getLong() {
        return getLong(jsonValue);
    }

    public double getDouble() {
        return getDouble(jsonValue);
    }

    public boolean getBoolean() {
        return getBoolean(jsonValue);
    }

    public boolean isNull() {
        return jsonValue == null;
    }

    public Jsonb getArray() {
        return getArray(jsonValue);
    }

    public Jsonb getObject() {
        return getObject(jsonValue);
    }

    public String getString(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getString(a.get(index));
    }

    public int getInt(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getInt(a.get(index));
    }

    public long getLong(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getLong(a.get(index));
    }

    public double getDouble(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getDouble(a.get(index));
    }

    public boolean getBoolean(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getBoolean(a.get(index));
    }

    public Jsonb getArray(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getArray(a.get(index));
    }

    public Jsonb getObject(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getObject(a.get(index));
    }

    public boolean isNull(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return a.get(index) == null;
    }

    @Override
    public boolean containsKey(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return o.containsKey(key);
    }

    @Override
    public String getString(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return getString(o.get(key));
    }

    @Override
    public String getString(String key, String defaultValue) {
        String v = getString(key);
        return v == null ? defaultValue : v;
    }

    private Object getIntObject(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return o.get(key);
    }

    @Override
    public int getInt(String key) {
        return getInt(getIntObject(key));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        JSONObject o = (JSONObject) jsonValue;
        Object v = o.get(key);
        return v == null ? defaultValue : getInt(v);
    }

    @Override
    public long getLong(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return getLong(o.get(key));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return containsKey(key) ? getLong(key) : defaultValue;
    }

    @Override
    public double getDouble(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return getDouble(o.get(key));
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return containsKey(key) ? getDouble(key) : defaultValue;
    }

    @Override
    public boolean getBoolean(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return getBoolean(o.get(key));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return containsKey(key) ? getBoolean(key) : defaultValue;
    }

    @Override
    public Jsonb getArray(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return getArray(o.get(key));
    }

    @Override
    public Jsonb getObject(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return getObject(o.get(key));
    }

    @Override
    public boolean isNull(String key) {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        return o.get(key) == null;
    }

    public int size() {
        if (jsonValue instanceof JSONArray)
            return ((JSONArray) jsonValue).size();
        else if (jsonValue instanceof JSONObject)
            return ((JSONObject) jsonValue).size();
        else
            throw new UnsupportedOperationException("Not an array or an object: " + jsonValue);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
