package com.eqosoftware.financeiropessoal.config.multitenant;

import liquibase.integration.spring.MultiTenantSpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter({ JpaRepositoriesAutoConfiguration.class })
@PropertySource("classpath:tenants-spring-default.properties")
public class TenantsAutoConfiguration {

    private final Environment env;

    public TenantsAutoConfiguration(Environment env) {
        this.env = env;
        log.info("Created");
    }

    @Bean
    public MultiTenantSpringLiquibase liquibaseMts(DataSource dataSource, LiquibaseProperties liquibaseProperties)
            throws SQLException {
        MultiTenantSpringLiquibase multiTenantSpringLiquibase = new MultiTenantSpringLiquibase();
        multiTenantSpringLiquibase.setDataSource(dataSource);
        ArrayList<String> schemas = new ArrayList<>();
        ResultSet listaDeSchemas = dataSource.getConnection().getMetaData().getSchemas();
        while (listaDeSchemas.next()) {
            String schema = listaDeSchemas.getString("TABLE_SCHEM");

            log.info("SCHEMA: {}", schema);

            if (!StringUtils.isEmpty(schema) && (schema.contains("tenant_"))) {
                schemas.add(schema);
            }
        }
        multiTenantSpringLiquibase.setSchemas(schemas);
        multiTenantSpringLiquibase.setChangeLog("classpath:db/changelog-master.xml");
        multiTenantSpringLiquibase.setDatabaseChangeLogLockTable("database_changelog_lock");
        multiTenantSpringLiquibase.setDatabaseChangeLogTable("database_changelog");
        multiTenantSpringLiquibase.setContexts(liquibaseProperties.getContexts());

        log.debug("Configuring contexts liquibase - " + liquibaseProperties.getContexts());
        if (env.acceptsProfiles(Profiles.of("no-liquibase"))) {
            multiTenantSpringLiquibase.setShouldRun(false);
        }
        else {
            multiTenantSpringLiquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.debug("Configuring Liquibase Multitenancy - " + schemas.size() + " Clientes");
        }

        return multiTenantSpringLiquibase;
    }

}
