/*
 * Copyright (c) 2014-2018, Bitnine Inc.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is for a Jsonb value which is returned from server or will be sent to the server.
 */
public class Jsonb extends PGobject implements JsonbObject, Serializable, Cloneable {
    private Object jsonValue = null;

    /**
     * This constructor should not be used directly.
     */
    public Jsonb() {
        setType("jsonb");
    }

    /**
     * This constructor should not be used directly.
     */
    Jsonb(Object obj) {
        this();

        jsonValue = obj;
    }

    /**
     * This method should not be used directly.
     */
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

    /**
     * Returns a string representation of the Json value.
     *
     * @return a string representation of the Json value
     */
    @Override
    public String getValue() {
        if (value == null)
            value = JSONValue.toJSONString(jsonValue);

        return value;
    }

    /**
     * Returns the internal structure (from json-simple 1.1.1) of the Json value.
     *
     * @return the internal structure (from json-simple 1.1.1) of the Json value
     */
    public Object getJsonValue() {
        return jsonValue;
    }

    private String getString(Object obj) {
        if (obj instanceof String)
            return (String) obj;

        throw new UnsupportedOperationException("Not a string: " + obj);
    }

    private boolean isInteger(long l) {
        return (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE);
    }

    private int getInt(Object obj) {
        if (obj instanceof Long) {
            long l = (Long) obj;
            if (isInteger(l))
                return (int) l;

            throw new IllegalArgumentException("Bad value for type int: " + l);
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

    /**
     * Returns the string value if the Json value is string or returns null.
     *
     * @return the string value if the Json value is string or returns null
     */
    public String tryGetString() {
        if (jsonValue instanceof String)
            return (String) jsonValue;

        return null;
    }

    /**
     * Returns the string value if the Json value is string or throws UnsupportedOperationException.
     *
     * @return the string value if the Json value is string or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not a string
     */
    public String getString() {
        return getString(jsonValue);
    }

    /**
     * Returns the integer value if the Json value is integer or throws UnsupportedOperationException.
     *
     * @return the integer value if the Json value is integer or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an int
     */
    public int getInt() {
        return getInt(jsonValue);
    }

    /**
     * Returns the long value if the Json value is long or throws UnsupportedOperationException.
     *
     * @return the long value if the Json value is long or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not a long
     */
    public long getLong() {
        return getLong(jsonValue);
    }

    /**
     * Returns the double value if the Json value is double or throws UnsupportedOperationException.
     *
     * @return the double value if the Json value is double or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not a double
     */
    public double getDouble() {
        return getDouble(jsonValue);
    }

    /**
     * Returns the boolean value if the Json value is boolean.
     *
     * @return the boolean value if the Json value is boolean
     */
    public boolean getBoolean() {
        return getBoolean(jsonValue);
    }

    /**
     * Returns true if the Json value is null.
     *
     * @return true if the Json value is null
     */
    public boolean isNull() {
        return jsonValue == null;
    }

    /**
     * Returns a Jsonb that wraps the array value if the Json value is array or throws UnsupportedOperationException.
     *
     * @return a Jsonb that wraps the array value if the Json value is array or throws UnsupportedOperationException
     */
    public Jsonb getArray() {
        return getArray(jsonValue);
    }

    /**
     * Returns a Jsonb that wraps the object value if the Json value is object or throws UnsupportedOperationException.
     *
     * @return a Jsonb that wraps the object value if the Json value is object or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an object
     */
    public Jsonb getObject() {
        return getObject(jsonValue);
    }

    /**
     * Returns the string value if the value at index of the array is string or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return the string value if the value at index of the array is string or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public String getString(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getString(a.get(index));
    }

    /**
     * Returns the integer value if the value at index of the array is integer or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return the integer value if the value at index of the array is integer or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public int getInt(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getInt(a.get(index));
    }

    /**
     * Returns the long value if the value at index of the array is long or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return the long value if the value at index of the array is long or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public long getLong(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getLong(a.get(index));
    }

    /**
     * Returns the double value if the value at index of the array is double or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return the double value if the value at index of the array is double or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public double getDouble(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getDouble(a.get(index));
    }

    /**
     * Returns the boolean value if the value at index of the array is boolean or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return the boolean value if the value at index of the array is boolean or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public boolean getBoolean(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getBoolean(a.get(index));
    }

    /**
     * Returns a Jsonb that wraps the array value if the value at index of the array is array
     * or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return a Jsonb that wraps the array value if the value at index of the array is array
     * or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public Jsonb getArray(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getArray(a.get(index));
    }

    /**
     * Returns a Jsonb that wraps the object value if the value at index of the array is object
     * or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return a Jsonb that wraps the object value if the value at index of the array is object
     * or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public Jsonb getObject(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return getObject(a.get(index));
    }

    /**
     * Returns true if the value at index of the array is null or throws UnsupportedOperationException.
     *
     * @param index the index of the array
     * @return true if the value at index of the array is null or throws UnsupportedOperationException
     * @throws UnsupportedOperationException not an array
     */
    public boolean isNull(int index) {
        if (!(jsonValue instanceof JSONArray))
            throw new UnsupportedOperationException("Not an array: " + jsonValue);

        JSONArray a = (JSONArray) jsonValue;
        return a.get(index) == null;
    }

    @Override
    public Iterable<String> getKeys() {
        if (!(jsonValue instanceof JSONObject))
            throw new UnsupportedOperationException("Not an object: " + jsonValue);

        JSONObject o = (JSONObject) jsonValue;
        ArrayList<String> keys = new ArrayList<>(o.size());
        for (Object k : o.keySet())
            keys.add((String) k);
        return keys;
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
        return containsKey(key) ? getString(key) : defaultValue;
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
        return containsKey(key) ? getInt(key) : defaultValue;
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

    /**
     * Returns the size of Json value if the value is array or object.
     *
     * @return the size of Json value if the value is array or object
     */
    public int size() {
        if (jsonValue instanceof JSONArray)
            return ((JSONArray) jsonValue).size();
        else if (jsonValue instanceof JSONObject)
            return ((JSONObject) jsonValue).size();
        else
            throw new UnsupportedOperationException("Not an array or an object: " + jsonValue);
    }

    private Object getTypedValue(Object obj) {
        if (obj instanceof Long) {
            long l = (Long) obj;
            if (isInteger(l))
                return (int) l;
            else
                return l;
        } else if (obj instanceof JSONArray) {
            JSONArray a = (JSONArray) obj;
            ArrayList<Object> newa = new ArrayList<>(a.size());
            for (Object e : a)
                newa.add(getTypedValue(e));
            return newa;
        } else if (obj instanceof JSONObject) {
            JSONObject o = (JSONObject) obj;
            HashMap<String, Object> newo = new HashMap<>(o.size());
            for (Object _e : o.entrySet()) {
                Map.Entry e = (Map.Entry) _e;
                newo.put((String) e.getKey(), getTypedValue(e.getValue()));
            }
            return newo;
        } else {
            return obj;
        }
    }

    /**
     * Returns an object that wraps automatically the value by type.
     *
     * @return an object that wraps automatically the value by type
     */
    public Object getTypedValue() {
        return getTypedValue(jsonValue);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
