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

import net.bitnine.agensgraph.Driver;
import net.bitnine.agensgraph.util.DriverInfo;
import org.postgresql.PGProperty;
import org.postgresql.ds.PGPoolingDataSource;

import java.util.Properties;

public class AGPoolingDataSource extends PGPoolingDataSource {
    /**
     * Ensure the driver is loaded as JDBC Driver might be invisible to Java's ServiceLoader.
     * Usually, {@code Class.forName(...)} is not required as {@link DriverManager} detects JDBC drivers
     * via {@code META-INF/services/java.sql.Driver} entries. However there might be cases when the driver
     * is located at the application level classloader, thus it might be required to perform manual
     * registration of the driver.
     */
    static {
        try {
            Class.forName("net.bitnine.agensgraph.Driver");
        } catch (ClassNotFoundException ce) {
            throw new IllegalStateException("AGPoolingDataSource is unable to load net.bitnine.agensgraph.Driver. Please check if you hava proper AgensGraph JDBC Driver jar on the classpath", ce);
        }
    }

    @Override
    public String getDescription() {
        return "Pooling DataSource " + super.dataSourceName + " from " + DriverInfo.DRIVER_FULL_NAME;
    }

    @Override
    public void setUrl(String url) {
        Properties p = Driver.parseURL(url, null);

        for (PGProperty property : PGProperty.values()) {
            super.setProperty(property, property.get(p));
        }
    }

    @Override
    public AGConnectionPoolDataSource createConnectionPool() { return new AGConnectionPoolDataSource(); }
}
