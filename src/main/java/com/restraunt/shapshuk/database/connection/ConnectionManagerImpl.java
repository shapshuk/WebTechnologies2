package com.restraunt.shapshuk.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager {

    private final ConnectionPool connectionPool;
    private final TransactionManager transactionManager;

    public ConnectionManagerImpl(ConnectionPool connectionPool, TransactionManager transactionManager) {
        this.connectionPool = connectionPool;
        this.transactionManager = transactionManager;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (transactionManager.isEmpty()) {
            return connectionPool.getConnection();
        } else {
            return transactionManager.getConnection();
        }
    }
}
