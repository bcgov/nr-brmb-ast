package ca.bc.gov.farms.service.api.v1.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class BaseServiceImpl {

    private DataSource dataSource;

    public BaseServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }
}
