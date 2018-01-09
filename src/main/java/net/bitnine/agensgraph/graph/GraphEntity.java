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

package net.bitnine.agensgraph.graph;

import net.bitnine.agensgraph.util.Jsonb;
import net.bitnine.agensgraph.util.JsonbObject;
import org.postgresql.util.PGobject;

import java.io.Serializable;

public abstract class GraphEntity extends PGobject implements JsonbObject, Serializable, Cloneable {
    private String label;
    private GraphId gid;
    private Jsonb props;

    void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    void setGraphId(GraphId gid) {
        this.gid = gid;
    }

    public GraphId getGraphId() {
        return gid;
    }

    void setProperties(Jsonb props) {
        this.props = props;
    }

    public Jsonb getProperties() {
        return props;
    }

    @Override
    public boolean containsKey(String key) {
        return props.containsKey(key);
    }

    @Override
    public String getString(String key) {
        return props.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return props.getString(key, defaultValue);
    }

    @Override
    public int getInt(String key) {
        return props.getInt(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return props.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key) {
        return props.getLong(key);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return props.getLong(key, defaultValue);
    }

    @Override
    public double getDouble(String key) {
        return props.getDouble(key);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return props.getDouble(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key) {
        return props.getBoolean(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return props.getBoolean(key, defaultValue);
    }

    @Override
    public Jsonb getArray(String key) {
        return props.getArray(key);
    }

    @Override
    public Jsonb getObject(String key) {
        return props.getObject(key);
    }

    @Override
    public boolean isNull(String key) {
        return props.isNull(key);
    }
}
