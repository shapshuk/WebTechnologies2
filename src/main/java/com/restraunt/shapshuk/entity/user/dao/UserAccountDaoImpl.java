package com.restraunt.shapshuk.entity.user.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.user.model.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.restraunt.shapshuk.core.constants.LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE;
import static com.restraunt.shapshuk.core.constants.LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_NAME_MESSAGE;
import static com.restraunt.shapshuk.entity.user.dao.UserAccountTableConstants.*;
import static java.lang.String.format;

public class UserAccountDaoImpl extends GenericDao<User> implements UserAccountDao {

    private static final Logger LOGGER = Logger.getLogger(UserAccountDaoImpl.class.getName());

    private static final String SELECT_BY_NAME = "" +
            "SELECT {0}.*\n" +
            "FROM {0}\n" +
            "WHERE {0}.{1} = ?";
    private static final String SELECT_BY_EMAIL = "" +
            "SELECT {0}.*\n" +
            "FROM {0}\n" +
            "WHERE {0}.{1} = ?";
    private final Lock connectionLock = new ReentrantLock();
    private final ConnectionManager connectionManager;

    public UserAccountDaoImpl(ConnectionManager connectionManager) {
        super(USER_ACCOUNT_TABLE_NAME, getUserRowMapper(), connectionManager);
        this.connectionManager = connectionManager;
    }

    private static IdentifiedRowMapper<User> getUserRowMapper() {

        return new IdentifiedRowMapper<User>() {

            @Override
            public User map(ResultSet resultSet) throws SQLException {
                User user = new User();
                user.setId(resultSet.getLong(ID));
                user.setName(resultSet.getString(USER_NAME));
                user.setPassword(resultSet.getString(USER_PASSWORD));
                user.setEmail(resultSet.getString(USER_EMAIL));
                user.setActive(resultSet.getBoolean(IS_ACTIVE));
                user.setPhoneNumber(resultSet.getString(USER_PHONE_NUMBER));
                user.getLoyalty().setId(resultSet.getLong(LOYALTY_POINTS_ID));
                user.getWallet().setId(resultSet.getLong(WALLET_ID));
                user.getUserAddress().setId(resultSet.getLong(USER_ADDRESS_ID));
                return user;
            }

            @Override
            public List<String> getColumnNames() {
                return Arrays.asList(USER_NAME,
                        USER_PASSWORD,
                        USER_EMAIL,
                        IS_ACTIVE,
                        USER_PHONE_NUMBER,
                        LOYALTY_POINTS_ID,
                        WALLET_ID,
                        USER_ADDRESS_ID);
            }

            @Override
            public void populateStatement(PreparedStatement statement, User entity) throws SQLException {

                statement.setString(1, entity.getName());
                statement.setString(2, entity.getPassword());
                statement.setString(3, entity.getEmail());
                statement.setBoolean(4, entity.isActive());
                statement.setString(5, entity.getPhoneNumber());
                statement.setLong(6, entity.getLoyalty().getId());
                statement.setLong(7, entity.getWallet().getId());
                statement.setLong(8, entity.getUserAddress().getId());
            }
        };
    }

    @Override
    public User getByName(String name) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format(CLASS_INVOKED_METHOD_FOR_ENTITY_NAME_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, name));

        return getByStringParam(SELECT_BY_NAME, USER_NAME, name);
    }

    @Override
    public User getByEmail(String email) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format(CLASS_INVOKED_METHOD_FOR_ENTITY_NAME_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, email));

        return getByStringParam(SELECT_BY_EMAIL, USER_EMAIL, email);
    }

    private User getByStringParam(String query, String columnName, String param) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        AtomicReference<User> result = new AtomicReference<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(query, USER_ACCOUNT_TABLE_NAME, columnName);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, param);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    try {
                        result.set(getUserRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                } else {
                    LOGGER.info(format(CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get()));
                    return null;
                }
            }
            LOGGER.info(format(CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get().toString()));
            return result.get();
        } finally {
            connectionLock.unlock();
        }
    }
}
