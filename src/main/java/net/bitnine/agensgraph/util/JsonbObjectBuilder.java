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

import org.json.simple.JSONObject;

/**
 * A builder for creating Jsonb object.
 * This class initializes a Jsonb object and provides methods to add name/value pairs to the object
 * and to return the resulting object.
 * The methods in this class can be chained to add multiple name/value pairs to the object.
 */
public class JsonbObjectBuilder {
    private JSONObject obj;

    /**
     * Initializes a Jsonb object.
     */
    public JsonbObjectBuilder() {
        this.obj = new JSONObject();
    }

    /**
     * Returns the current object.
     *
     * @return the current object
     */
    JSONObject getObject() {
        return obj;
    }

    /**
     * Adds a name/string pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with the specified value.
     *
     * @param name  name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, String value) {
        obj.put(name, value);
        return this;
    }

    /**
     * Adds a name/long pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with the specified value.
     *
     * @param name  name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, long value) {
        obj.put(name, value);
        return this;
    }

    /**
     * Adds a name/double pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with the specified value.
     *
     * @param name  name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, double value) {
        obj.put(name, value);
        return this;
    }

    /**
     * Adds a name/boolean pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with the specified value.
     *
     * @param name  name in the name/value pair
     * @param value value in the name/value pair
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, boolean value) {
        obj.put(name, value);
        return this;
    }

    /**
     * Adds a name/null pair to the Jsonb object associated with this object builder where the value is null.
     * If the object contains a mapping for the specified name, this method replaces the old value with null.
     *
     * @param name  name in the name/value pair
     * @return this object builder
     */
    public JsonbObjectBuilder addNull(String name) {
        obj.put(name, null);
        return this;
    }

    /**
     * Adds a name/Jsonb pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with the Jsonb.
     *
     * @param name  name in the name/value pair
     * @param j the value is the object associated with Jsonb
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, Jsonb j) {
        obj.put(name, j.getJsonValue());
        return this;
    }

    /**
     * Adds a name/object pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with the object.
     *
     * @param name  name in the name/value pair
     * @param value the value is the object
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, Object value) {
        value = JsonbUtil.filterValueType(value);
        obj.put(name, value);
        return this;
    }

    /**
     * Adds a name/JsonbArray pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with JsonbArray.
     *
     * @param name  name in the name/value pair
     * @param builder the value is the object array with this builder
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, JsonbArrayBuilder builder) {
        obj.put(name, builder.getArray());
        return this;
    }

    /**
     * Adds a name/JsonbObject pair to the Jsonb object associated with this object builder.
     * If the object contains a mapping for the specified name, this method replaces the old value with JsonbObject.
     *
     * @param name  name in the name/value pair
     * @param builder the value is the object associated with this builder
     * @return this object builder
     */
    public JsonbObjectBuilder add(String name, JsonbObjectBuilder builder) {
        obj.put(name, builder.getObject());
        return this;
    }

    /**
     * Returns the Jsonb object associated with this object builder.
     *
     * @return the Jsonb object that is being built
     */
    public Jsonb build() {
        return new Jsonb(obj);
    }
}
