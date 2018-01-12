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

/**
 * This interface defines the JsonbObject.
 */
public interface JsonbObject {
    /**
     * Returns a set of keys.
     *
     * @return a set of keys
     */
    Iterable<String> getKeys();

    /**
     * Returns true if the given key is contained.
     *
     * @param key the given key
     * @return true if the given key is contained
     */
    boolean containsKey(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the string value stored at the key
     */
    String getString(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the string value stored at the key
     */
    String getString(String key, String defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the int value stored at the key
     */
    int getInt(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the int value stored at the key
     */
    int getInt(String key, int defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the long value stored at the key
     */
    long getLong(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the long value stored at the key
     */
    long getLong(String key, long defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the double value stored at the key
     */
    double getDouble(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the double value stored at the key
     */
    double getDouble(String key, double defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the boolean value stored at the key
     */
    boolean getBoolean(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @param defaultValue returned the default value if the value stored at the key is null
     * @return the boolean value stored at the key
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the array value stored at the key
     */
    Jsonb getArray(String key);

    /**
     * Returns the value stored at the key.
     *
     * @param key the given key
     * @return the object value stored at the key
     */
    Jsonb getObject(String key);

    /**
     * Returns true if the value stored at the key is null.
     *
     * @param key the given key
     * @return true if the value stored at the key is null
     */
    boolean isNull(String key);
}
