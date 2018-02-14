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
