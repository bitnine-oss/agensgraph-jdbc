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

import java.util.Map;

public class JsonbUtil {
    private JsonbUtil() {
    }

    public static Jsonb create(String value) {
        return new Jsonb(value);
    }

    public static Jsonb create(int value) {
        return create((long) value);
    }

    public static Jsonb create(long value) {
        return new Jsonb(value);
    }

    public static Jsonb create(double value) {
        return new Jsonb(value);
    }

    public static Jsonb create(boolean value) {
        return new Jsonb(value);
    }

    public static Jsonb createNull() {
        return new Jsonb(null);
    }

    public static JsonbArrayBuilder createArrayBuilder() {
        return new JsonbArrayBuilder();
    }

    public static Jsonb createArray() {
        return createArrayBuilder().build();
    }

    public static Jsonb createArray(String... values) {
        JsonbArrayBuilder builder = createArrayBuilder();
        for (String value : values)
            builder.add(value);

        return builder.build();
    }

    public static Jsonb createArray(Object... values) {
        JsonbArrayBuilder builder = createArrayBuilder();
        for (Object value : values)
            builder.add(value);

        return builder.build();
    }

    public static Jsonb createArray(Iterable<?> values) {
        JsonbArrayBuilder builder = createArrayBuilder();
        for (Object value : values)
            builder.add(value);

        return builder.build();
    }

    public static JsonbObjectBuilder createObjectBuilder() {
        return new JsonbObjectBuilder();
    }

    public static Jsonb createObject() {
        return createObjectBuilder().build();
    }

    public static Jsonb createObject(Map<String, ?> map) {
        JsonbObjectBuilder builder = createObjectBuilder();
        for (Map.Entry<String, ?> entry : map.entrySet())
            builder.add(entry.getKey(), entry.getValue());

        return builder.build();
    }

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
