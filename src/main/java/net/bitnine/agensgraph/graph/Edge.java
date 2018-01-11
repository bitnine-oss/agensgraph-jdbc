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
import org.json.simple.JSONObject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edge extends GraphEntity {
    private static final Pattern edgePattern;

    private GraphId start;
    private GraphId end;

    static {
        edgePattern = Pattern.compile("(.+?)\\[(.+?)\\]\\[(.+?),(.+?)\\](.*)");
    }

    public Edge() {
        setType("edge");
    }

    @Override
    public void setValue(String value) throws SQLException {
        Matcher m = edgePattern.matcher(value);
        if (m.find()) {
            setLabel(m.group(1));

            GraphId gid = new GraphId();
            gid.setValue(m.group(2));
            setGraphId(gid);

            gid = new GraphId();
            gid.setValue(m.group(3));
            start = gid;

            gid = new GraphId();
            gid.setValue(m.group(4));
            end = gid;

            Jsonb props = new Jsonb();
            props.setValue(m.group(5));
            if (!(props.getJsonValue() instanceof JSONObject))
                throw new PSQLException("Parsing edge failed", PSQLState.DATA_ERROR);
            setProperties(props);
        } else {
            throw new PSQLException("Parsing edge failed", PSQLState.DATA_ERROR);
        }

        super.setValue(value);
    }

    public GraphId getEdgeId() {
        return getGraphId();
    }

    public GraphId getStartVertexId() {
        return start;
    }

    public GraphId getEndVertexId() {
        return end;
    }
}
