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

package net.bitnine.agensgraph.test;

import org.postgresql.PGProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for JDBC tests
 */
public class TestUtil {
    /*
     * Returns the Test database JDBC URL
     */
    public static String getURL() {
        return getURL(AbstractAGDockerizedTest.getServer(),
                AbstractAGDockerizedTest.getPort());
    }

    public static String getURL(String server, int port) {
        String logLevel = "";
        if (getLogLevel() != null && !getLogLevel().equals("")) {
            logLevel = "&loggerLevel=" + getLogLevel();
        }

        String logFile = "";
        if (getLogFile() != null && !getLogFile().equals("")) {
            logFile = "&loggerFile=" + getLogFile();
        }

        String protocolVersion = "";
        if (getProtocolVersion() != 0) {
            protocolVersion = "&protocolVersion=" + getProtocolVersion();
        }

        String binaryTransfer = "";
        if (getBinaryTransfer() != null && !getBinaryTransfer().equals("")) {
            binaryTransfer = "&binaryTransfer=" + getBinaryTransfer();
        }

        String receiveBufferSize = "";
        if (getReceiveBufferSize() != -1) {
            receiveBufferSize = "&receiveBufferSize=" + getReceiveBufferSize();
        }

        String sendBufferSize = "";
        if (getSendBufferSize() != -1) {
            sendBufferSize = "&sendBufferSize=" + getSendBufferSize();
        }

        String ssl = "";
        if (getSSL() != null) {
            ssl = "&ssl=" + getSSL();
        }

        return String.format(
                "jdbc:agensgraph://%s:%d/%s?ApplicationName=Driver Tests%s%s%s%s%s%s%s",
                server,
                port,
                AbstractAGDockerizedTest.getDatabase(),
                logLevel,
                logFile,
                protocolVersion,
                binaryTransfer,
                receiveBufferSize,
                sendBufferSize,
                ssl);
    }

    /*
     * Returns the server side prepared statement threshold.
     */
    public static int getPrepareThreshold() {
        return Integer.parseInt(System.getProperty("preparethreshold", "5"));
    }

    public static int getProtocolVersion() {
        return Integer.parseInt(System.getProperty("protocolVersion", "0"));
    }

    /*
     * Returns the log level to use
     */
    public static String getLogLevel() {
        return System.getProperty("loggerLevel");
    }

    /*
     * Returns the log file to use
     */
    public static String getLogFile() {
        return System.getProperty("loggerFile");
    }

    /*
     * Returns the binary transfer mode to use
     */
    public static String getBinaryTransfer() {
        return System.getProperty("binaryTransfer");
    }

    public static int getSendBufferSize() {
        return Integer.parseInt(System.getProperty("sendBufferSize", "-1"));
    }

    public static int getReceiveBufferSize() {
        return Integer.parseInt(System.getProperty("receiveBufferSize", "-1"));
    }

    public static String getSSL() {
        return System.getProperty("ssl");
    }

    static {
        try {
            initDriver();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize driver", e);
        }
    }

    private static boolean initialized = false;

    public static Properties loadPropertyFiles(String... names) {
        Properties p = new Properties();
        for (String name : names) {
            for (int i = 0; i < 1; i++) {
                // load x.properties, then x.local.properties
                File f = getFile(name);
                if (!f.exists()) {
                    System.out.println("Configuration file " + f.getAbsolutePath()
                            + " does not exist. Consider adding it to specify test db host and login");
                    continue;
                }
                try {
                    p.load(Files.newInputStream(f.toPath()));
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
        return p;
    }

    public static void initDriver() throws Exception {
        synchronized (TestUtil.class) {
            if (initialized) {
                return;
            }

            Properties p = loadPropertyFiles("build.properties");
            p.putAll(System.getProperties());
            System.getProperties().putAll(p);

            initialized = true;
        }
    }

    /**
     * Resolves file path with account of {@code build.properties.relative.path}. This is a bit tricky
     * since during maven release, maven does a temporary checkout to {@code core/target/checkout}
     * folder, so that script should somehow get {@code build.local.properties}
     *
     * @param name original name of the file, as if it was in the root pgjdbc folder
     * @return actual location of the file
     */
    public static File getFile(String name) {
        if (name == null) {
            throw new IllegalArgumentException("null file name is not expected");
        }
        if (name.startsWith("/")) {
            return new File(name);
        }
        return new File(System.getProperty("build.properties.relative.path", "./"), name);
    }


    /**
     * Helper - opens a connection.
     *
     * @return connection
     */
    public static Connection openDB() throws Exception {
        return openDB(new Properties());
    }

    /*
     * Helper - opens a connection with the allowance for passing additional parameters, like
     * "compatible".
     */
    public static Connection openDB(Properties props) throws Exception {
        initDriver();

        // Allow properties to override the user name.
        String user = props.getProperty("username");
        if (user == null) {
            user = AbstractAGDockerizedTest.getUser();
        }
        if (user == null) {
            throw new IllegalArgumentException(
                    "user name is not specified. Please specify 'username' property via -D or build.properties");
        }
        props.setProperty("user", user);
        String password = AbstractAGDockerizedTest.getPassword();
        if (password == null) {
            password = "";
        }
        props.setProperty("password", password);
        if (!props.containsKey(PGProperty.PREPARE_THRESHOLD.getName())) {
            PGProperty.PREPARE_THRESHOLD.set(props, getPrepareThreshold());
        }
        if (!props.containsKey(PGProperty.PREFER_QUERY_MODE.getName())) {
            String value = System.getProperty(PGProperty.PREFER_QUERY_MODE.getName());
            if (value != null) {
                props.put(PGProperty.PREFER_QUERY_MODE.getName(), value);
            }
        }

        return DriverManager.getConnection(getURL(), props);
    }

    /*
     * Helper - closes an open connection.
     */
    public static void closeDB(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }
}
