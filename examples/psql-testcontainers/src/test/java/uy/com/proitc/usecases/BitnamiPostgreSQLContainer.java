package uy.com.proitc.usecases;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public class BitnamiPostgreSQLContainer<SELF extends BitnamiPostgreSQLContainer<SELF>> extends JdbcDatabaseContainer<SELF> {
    public static final String NAME = "postgresql";
    public static final String IMAGE = "bitnami/postgresql";
    public static final String DEFAULT_TAG = "9.6.12";
    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("bitnami/postgresql");
    public static final Integer POSTGRESQL_PORT = 5432;
    static final String DEFAULT_USER = "test";
    static final String DEFAULT_PASSWORD = "test";
    private String databaseName;
    private String username;
    private String password;
    private static final String FSYNC_OFF_OPTION = "fsync=off";

    /** @deprecated */
    @Deprecated
    public BitnamiPostgreSQLContainer() {
        this(DEFAULT_IMAGE_NAME.withTag("9.6.12"));
    }

    public BitnamiPostgreSQLContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public BitnamiPostgreSQLContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        this.databaseName = "test";
        this.username = "test";
        this.password = "test";
        dockerImageName.assertCompatibleWith(new DockerImageName[]{DEFAULT_IMAGE_NAME});
        this.waitStrategy = (new LogMessageWaitStrategy()).withRegEx(".*database system is ready to accept connections.*\\s").withTimes(1).withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS));
        // this.setCommand(new String[]{"/opt/bitnami/scripts/postgresql/run.sh"});
        this.addExposedPort(POSTGRESQL_PORT);
    }

    protected @NotNull Set<Integer> getLivenessCheckPorts() {
        return Collections.singleton(this.getMappedPort(POSTGRESQL_PORT));
    }

    protected void configure() {
        //this.withUrlParam("loggerLevel", "INFO");
        this.addEnv("POSTGRESQL_DATABASE", this.databaseName);
        this.addEnv("POSTGRESQL_USERNAME", this.username);
        this.addEnv("POSTGRESQL_PASSWORD", this.password);
        this.addEnv("POSTGRESQL_EXTRA_FLAGS", "-c fsync=off");
    }

    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    public String getJdbcUrl() {
        String additionalUrlParams = this.constructUrlParameters("?", "&");
        return "jdbc:postgresql://" + this.getHost() + ":" + this.getMappedPort(POSTGRESQL_PORT) + "/" + this.databaseName + additionalUrlParams;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getTestQueryString() {
        return "SELECT 1";
    }

    public SELF withDatabaseName(String databaseName) {
        this.databaseName = databaseName;
        return this.self();
    }

    public SELF withUsername(String username) {
        this.username = username;
        return this.self();
    }

    public SELF withPassword(String password) {
        this.password = password;
        return this.self();
    }

    protected void waitUntilContainerStarted() {
        this.getWaitStrategy().waitUntilReady(this);
    }
}
