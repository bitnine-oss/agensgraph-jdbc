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

package net.bitnine.agensgraph.ds;

import net.bitnine.agensgraph.util.DriverInfo;
import org.postgresql.PGProperty;
import org.postgresql.ds.PGConnectionPoolDataSource;

import java.util.Properties;

public class AGConnectionPoolDataSource extends PGConnectionPoolDataSource {

    @Override
    public String getDescription() {
        return "ConnectionPoolDataSource from " + DriverInfo.DRIVER_FULL_NAME;
    }

    @Override
    public  String getUrl() {
        StringBuilder url = new StringBuilder(100);
        url.append("jdbc:agensgraph://");
        url.append(super.getServerName());
        if (getPortNumber() != 0) {
            url.append(":").append(super.getPortNumber());
        }
        url.append("/").append(super.getDatabaseName());

        StringBuilder query = new StringBuilder(100);
        Properties properties = new Properties();
        for (PGProperty property : PGProperty.values()) {
            String value = super.getProperty(property);
            if (value != null) {
                properties.setProperty(property.getName(), value);
            }
        }
        for (PGProperty property : PGProperty.values()) {
            if (property.isPresent(properties)) {
                if (query.length() != 0) {
                    query.append("&");
                }
                query.append(property.getName());
                query.append("=");
                query.append(property.get(properties));
            }
        }
        if(query.length() > 0) {
            url.append("?");
            url.append(query);
        }

        return url.toString();
    }
}
