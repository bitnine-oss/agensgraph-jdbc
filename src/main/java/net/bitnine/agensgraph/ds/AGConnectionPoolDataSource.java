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
