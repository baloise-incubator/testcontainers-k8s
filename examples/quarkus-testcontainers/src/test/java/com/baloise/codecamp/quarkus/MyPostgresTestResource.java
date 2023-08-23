package com.baloise.codecamp.quarkus;

import java.util.HashMap;
import java.util.Map;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static java.util.Collections.singletonMap;

public class MyPostgresTestResource implements QuarkusTestResourceLifecycleManager {

    BitnamiPostgreSQLContainer<?> postgres;

    @Override
    public Map<String, String> start() {
        postgres = new BitnamiPostgreSQLContainer<>("bitnami/postgresql:15")
          .withDatabaseName("quarkus")
          .withUsername("quarkus")
          .withPassword("quarkus")
          //.withInitScript("init_script.sql")
          // Container can have tmpfs mounts for storing data in host memory.
          // This is useful if you want to speed up your database tests.
          .withTmpFs(singletonMap("/var/lib/postgresql/data", "rw"));

        postgres.start();
        Map<String, String> conf = new HashMap<>();
        conf.put("quarkus.datasource.jdbc.url", postgres.getJdbcUrl());
        conf.put("quarkus.datasource.username", postgres.getUsername());
        conf.put("quarkus.datasource.password", postgres.getPassword());
        return conf;
    }

    @Override
    public void stop() {
        postgres.stop();
    }
    
}
