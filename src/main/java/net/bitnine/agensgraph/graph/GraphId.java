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

import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * This class defines the type graph id.
 */
public class GraphId extends PGobject implements Serializable, Cloneable {
    private long id;

    public GraphId() {
        setType("graphid");
    }

    public GraphId(long id) {
        this();

        long labid = (id >> (16 + 32)) & 0x000000000000ffffL;
        long locid = id & 0x0000ffffffffffffL;

        value = labid + "." + locid;

        this.id = id;
    }

    @Override
    public void setValue(String value) throws SQLException {
        String[] ids = value.split("\\.");
        if (ids.length != 2)
            throw new PSQLException("Parsing graphid failed", PSQLState.DATA_ERROR);

        super.setValue(value);

        long labid = Long.parseLong(ids[0]);
        long locid = Long.parseLong(ids[1]);
        this.id = (labid << (32 + 16)) | locid;
    }

    /**
     * Returns the id of the GraphId.
     *
     * @return the id of the GraphId
     */
    public long getId() {
        return id;
    }
}
