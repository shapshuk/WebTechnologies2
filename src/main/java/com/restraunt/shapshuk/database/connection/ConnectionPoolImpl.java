package com.restraunt.shapshuk.database.connection;

import com.restraunt.shapshuk.database.connection.constants.PropertyName;
import org.apache.log4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool {

    private static final Logger LOGGER = Logger.getLogger(ConnectionPoolImpl.class.getName());
    private static final Lock connectionLockStatic = new ReentrantLock();
    private static Map<String, String> properties;
    private static int poolCapacity;
    private static ConnectionPool basicConnectionPool;
    private final Lock connectionLock = new ReentrantLock();
    private final Condition emptyPool = connectionLock.newCondition();

    private BlockingQueue<Connection> availableConnections;
    private BlockingQueue<Connection> usedConnections;

    private ConnectionPoolImpl() {

        properties = PropertyHolder.getProperties();
        registerDriver();

        poolCapacity = Integer.parseInt(properties.get(PropertyName.POOL_CAPACITY));

        availableConnections = new ArrayBlockingQueue<>(poolCapacity);
        usedConnections = new ArrayBlockingQueue<>(poolCapacity);

        for (int i = 0; i < poolCapacity; i++) {
            availableConnections.add(createConnection());
        }
    }

    public static ConnectionPool getInstance() {
        connectionLockStatic.lock();
        try {
            if (basicConnectionPool == null) {
                basicConnectionPool = new ConnectionPoolImpl();
            }
            return basicConnectionPool;
        } finally {
            connectionLockStatic.unlock();
        }
    }

    private static Connection createConnection() {
        try {
            String url = properties.get(PropertyName.URL);
            String username = properties.get(PropertyName.USERNAME);
            String password = properties.get(PropertyName.PASSWORD);
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            LOGGER.error("Connection can't be created:" + e);
            throw new InternalServerErrorException(e);
        }
    }

    private void registerDriver() {
        try {
            String driver = properties.get(PropertyName.DRIVER);
            Class.forName(driver);
            LOGGER.info("Driver is registered");
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Driver can't be registered");
            throw new InternalServerErrorException(ex);
        }
    }

    private Connection createProxyConnection(Connection connection) {

        return (Connection) Proxy.newProxyInstance(connection.getClass().getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        releaseConnection(connection);
                        return null;
                    } else if ("hashCode".equals(method.getName())) {
                        return connection.hashCode();
                    } else {
                        return method.invoke(connection, args);
                    }
                });
    }

    @Override
    public Connection getConnection() {

        connectionLock.lock();
        Connection proxyConnection = null;
        try {

            if (availableConnections.isEmpty() && usedConnections.size() == poolCapacity) {
                try {
                    emptyPool.await();
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                    throw new IllegalThreadStateException(e.getMessage());
                }
            }

            if (availableConnections.isEmpty() && usedConnections.size() < poolCapacity) {
                String url = properties.get(PropertyName.URL);
                String username = properties.get(PropertyName.USERNAME);
                String password = properties.get(PropertyName.PASSWORD);
                Connection connection = DriverManager.getConnection(url, username, password);
                availableConnections.add(connection);
            } else if (availableConnections.isEmpty()) {
                String message = "No available connections";
                LOGGER.error(message);
                throw new IllegalStateException(message);
            }
            Connection connection = availableConnections.remove();
            usedConnections.add(connection);
            proxyConnection = createProxyConnection(connection);
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            connectionLock.unlock();
        }
        return proxyConnection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        try {
            connectionLock.lock();
            if (availableConnections.size() >= poolCapacity) {
                return;
            }
            usedConnections.remove(connection);
            availableConnections.add(connection);
            emptyPool.signal();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            connectionLock.unlock();
        }
    }

    @Override
    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection connection : availableConnections) {
            connection.close();
        }
        availableConnections.clear();
        LOGGER.info("Connection pool shutdown done");
    }
}
