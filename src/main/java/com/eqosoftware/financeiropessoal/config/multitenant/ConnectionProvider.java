package com.eqosoftware.financeiropessoal.config.multitenant;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Component
public class ConnectionProvider implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    @Autowired
    DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection("public");
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String schema) throws SQLException {
        final Connection connection = dataSource.getConnection();
        connection.setSchema(schema);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema("public");
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        throw new UnsupportedOperationException("Can't unwrap this.");
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}
