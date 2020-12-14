package com.restraunt.shapshuk.database;

import com.restraunt.shapshuk.database.connection.ConnectionPool;
import com.restraunt.shapshuk.database.connection.ConnectionPoolImpl;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConnectionPoolImplTest {

// 1. getting a connection when the pool is empty gives you a connection
// 2. getting a connection when a connection has already been got and not released gives you another, different connection
// 3. releasing a connection doesn't throw any exception

    private static final Logger LOGGER = Logger.getLogger(ConnectionPoolImplTest.class.getName());
    private ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

    private Connection connection1 = null;
    private Connection connection2 = null;

    //    getting a connection when the pool is empty gives you a connection
    @Test
    @Order(1)
    void shouldProvideConnectionWhenPoolEmpty() {

        LOGGER.info("1");
        try {
            connection1 = connectionPool.getConnection();
        } catch (SQLException e) {
            LOGGER.error("can't provide connection");
        }

        Assert.assertNotNull(connection1);
    }

    //    getting a connection when a connection has already been got and not released gives you another, different connection
    @Test
    @Order(2)
    void shouldProvideDifferentConnectionWhenAlreadyGotConnectionAndNotReleased() {

        LOGGER.info("2");
        try {
            connection1 = connectionPool.getConnection();
            connection2 = connectionPool.getConnection();
        } catch (SQLException e) {
            LOGGER.error("can't provide connection");
        }

        Assert.assertNotEquals(connection1, connection2);
    }

    //    releasing a connection doesn't throw any exception
    @Test
    @Order(3)
    void shouldReleaseConnectionWithoutException() {

        LOGGER.info("3");
        try {
            connection1 = connectionPool.getConnection();
            connection2 = connectionPool.getConnection();
        } catch (SQLException e) {
            LOGGER.error("can't provide connection");
        }
        connectionPool.releaseConnection(connection1);
        connectionPool.releaseConnection(connection2);
    }

}