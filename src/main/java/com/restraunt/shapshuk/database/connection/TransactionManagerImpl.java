package com.restraunt.shapshuk.database.connection;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManagerImpl implements TransactionManager {

    private static final Logger LOGGER = Logger.getLogger(TransactionManagerImpl.class);
    private final ConnectionPool connectionPool;
    private final ThreadLocal<Connection> currentConnection = new ThreadLocal<>();

    public TransactionManagerImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() {
        return currentConnection.get();
    }

    @Override
    public void begin() throws ConnectionException {
        if (this.isEmpty()) {
            try {
                Connection connection = connectionPool.getConnection();
                currentConnection.set(connection);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("Exception during beginning transaction: " + e);
                throw new ConnectionException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void commit() throws ConnectionException {
        try {
            Connection connection = currentConnection.get();
            connection.commit();
            this.close();
        } catch (SQLException e) {
            LOGGER.error("Exception during committing transaction: " + e);
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void rollback() throws ConnectionException {
        try {
            Connection connection = currentConnection.get();
            connection.rollback();
            this.close();
        } catch (SQLException e) {
            LOGGER.error("Exception during rolling back transaction: " + e);
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    private void close() throws ConnectionException {
        try {
            Connection connection = currentConnection.get();
            connection.setAutoCommit(true);
            connection.close();
            currentConnection.remove();
        } catch (SQLException e) {
            LOGGER.error("Exception during closing of transaction: " + e);
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isEmpty() {

        Connection connection = currentConnection.get();
        return connection == null;
    }
}
