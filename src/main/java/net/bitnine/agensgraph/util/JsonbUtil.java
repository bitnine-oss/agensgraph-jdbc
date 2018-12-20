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

import java.util.Map;

/**
 * This class provides static methods to create Jsonb values.
 */
public class JsonbUtil {
    private JsonbUtil() {
    }

    /**
     * Returns a Jsonb of the given string value.
     *
     * @param value the given value
     * @return a Jsonb of the string value
     */
    public static Jsonb create(String value) {
        return new Jsonb(value);
    }

    /**
     * Returns a Jsonb of the given integer value.
     *
     * @param value the given value
     * @return a Jsonb of the integer value
     */
    public static Jsonb create(int value) {
        return create((long) value);
    }

    /**
     * Returns a Jsonb of the given long value.
     *
     * @param value the given value
     * @return a Jsonb of the long value
     */
    public static Jsonb create(long value) {
        return new Jsonb(value);
    }

    /**
     * Returns a Jsonb of the given double value.
     *
     * @param value the given value
     * @return a Jsonb of the double value
     */
    public static Jsonb create(double value) {
        return new Jsonb(value);
    }

    /**
     * Returns a Jsonb of the given boolean value.
     *
     * @param value the given value
     * @return a Jsonb of the boolean value
     */
    public static Jsonb create(boolean value) {
        return new Jsonb(value);
    }

    /**
     * Returns a Jsonb of null value.
     *
     * @return a Jsonb of null value
     */
    public static Jsonb createNull() {
        return new Jsonb(null);
    }

    /**
     * Returns a JsonbArrayBuilder to build an array.
     *
     * @return a JsonbArrayBuilder to build an array
     */
    public static JsonbArrayBuilder createArrayBuilder() {
        return new JsonbArrayBuilder();
    }

    /**
     * Returns a Jsonb of an empty array.
     *
     * @return a Jsonb of an empty array
     */
    public static Jsonb createArray() {
        return createArrayBuilder().build();
    }

    /**
     * Returns a Jsonb of an array of the given strings.
     *
     * @param values a list of the given strings
     * @return a jsonb of an array of the strings
     */
    public static Jsonb createArray(String... values) {
        JsonbArrayBuilder builder = createArrayBuilder();
        for (String value : values)
            builder.add(value);

        return builder.build();
    }

    /**
     * Returns a Jsonb of an array of the given objects.
     *
     * @param values a list of the given objects
     * @return a jsonb of an array of the strings
     */
    public static Jsonb createArray(Object... values) {
        JsonbArrayBuilder builder = createArrayBuilder();
        for (Object value : values)
            builder.add(value);

        return builder.build();
    }

    /**
     * Returns a Jsonb of an array that has elements from the given iterable.
     *
     * @param values a list of the given iterable
     * @return a jsonb of an array of the iterable
     */
    public static Jsonb createArray(Iterable<?> values) {
        JsonbArrayBuilder builder = createArrayBuilder();
        for (Object value : values)
            builder.add(value);

        return builder.build();
    }

    /**
     * Returns a JsonbObjectBuilder to builder an object.
     *
     * @return a JsonbObjectBuilder to builder an object
     */
    public static JsonbObjectBuilder createObjectBuilder() {
        return new JsonbObjectBuilder();
    }

    /**
     * Returns a Jsonb of an empty object.
     *
     * @return a Jsonb of an empty object
     */
    public static Jsonb createObject() {
        return createObjectBuilder().build();
    }

    /**
     * Returns a Jsonb of an object that has properties from the given map.
     *
     * @param map the given map
     * @return a Jsonb of an object that has properties from the given map
     */
    public static Jsonb createObject(Map<String, ?> map) {
        JsonbObjectBuilder builder = createObjectBuilder();
        for (Map.Entry<String, ?> entry : map.entrySet())
            builder.add(entry.getKey(), entry.getValue());

        return builder.build();
    }

    /**
     * Returns the object value if the value is string, integer, double, boolean, Jsonb, Map, Iterable
     * or throws IllegalArgumentException.
     *
     * @param value the given value
     * @return the object value if the value is string, integer, double, boolean, Jsonb, Map, Iterable
     * or throws IllegalArgumentException
     * @throws IllegalArgumentException invalid Json value type
     */
    static Object filterValueType(Object value) throws IllegalArgumentException {
        if (value == null)
            return null;
        else if (value instanceof String)
            return value;
        else if (value instanceof Integer)
            return ((Integer) value).longValue();
        else if (value instanceof Long)
            return value;
        else if (value instanceof Double)
            return value;
        else if (value instanceof Boolean)
            return value;
        else if (value instanceof Jsonb)
            return ((Jsonb) value).getJsonValue();
        else if (value instanceof Map)
            return JsonbUtil.createObject((Map<String, ?>) value);
        else if (value instanceof Iterable)
            return JsonbUtil.createArray((Iterable<?>) value);
        else
            throw new IllegalArgumentException("Invalid json value type");
    }
}
