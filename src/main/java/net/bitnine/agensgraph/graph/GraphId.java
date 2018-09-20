/*
 * Copyright (c) 2014-2018 Bitnine, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
