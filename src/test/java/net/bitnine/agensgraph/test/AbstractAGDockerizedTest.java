package net.bitnine.agensgraph.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractAGDockerizedTest {
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "postgres";
    private static final String POSTGRES_DB = "postgres";
    private static final int POSTGRES_PORT = 5432;

    static final GenericContainer<?> AgensGraphContainer;

    static {
        AgensGraphContainer = new GenericContainer<>(DockerImageName.parse("bitnine/agensgraph:v2.1.3"));
        AgensGraphContainer
                .withEnv("POSTGRES_USER", POSTGRES_USER)
                .withEnv("POSTGRES_PASSWORD", POSTGRES_PASSWORD)
                .withEnv("POSTGRES_DB", POSTGRES_DB)
                .withExposedPorts(POSTGRES_PORT);
        AgensGraphContainer.start();
    }

    /*
     * Returns the Test server
     */
    public static String getServer() {
        return "localhost";
    }

    /*
     * Returns the Test port
     */
    public static int getPort() {
        return AgensGraphContainer.getMappedPort(POSTGRES_PORT);
    }

    /*
     * Returns the Postgresql username
     */
    public static String getUser() {
        return POSTGRES_USER;
    }

    /*
     * Returns the user's password
     */
    public static String getPassword() {
        return POSTGRES_PASSWORD;
    }

    /*
     * Returns the Test database
     */
    public static String getDatabase() {
        return POSTGRES_DB;
    }
}
