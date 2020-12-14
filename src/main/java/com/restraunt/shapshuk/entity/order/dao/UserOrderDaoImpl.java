package com.restraunt.shapshuk.entity.order.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.order.constants.OrderStatus;
import com.restraunt.shapshuk.entity.order.model.UserOrder;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;

public class UserOrderDaoImpl extends GenericDao<UserOrder> implements UserOrderDao {

    private static final Logger LOGGER = Logger.getLogger(UserOrderDaoImpl.class.getName());

    private static final String SELECT_BUILDING_UP_ORDER_BY_USER_ID_QUERY = "" +
            "SELECT *\n" +
            "FROM {0}\n" +
            "WHERE {0}.{1} = ?\n" +
            "  AND {0}.{2} LIKE ?";
    private final Lock connectionLock = new ReentrantLock();
    private final ConnectionManager connectionManager;

    public UserOrderDaoImpl(ConnectionManager connectionManager) {
        super(UserOrderTableConstants.USER_ORDER_TABLE_NAME, getUserOrderRowMapper(), connectionManager);
        this.connectionManager = connectionManager;
    }

    private static IdentifiedRowMapper<UserOrder> getUserOrderRowMapper() {

        return new IdentifiedRowMapper<UserOrder>() {

            @Override
            public UserOrder map(ResultSet resultSet) throws SQLException {
                UserOrder userOrder = new UserOrder();
                userOrder.setId(resultSet.getLong(UserOrderTableConstants.ID));
                LocalDateTime localDateTime = resultSet.getObject(UserOrderTableConstants.TIME_OF_DELIVERY, LocalDateTime.class);
                userOrder.setTimeOfDelivery(localDateTime);
                userOrder.setOrderStatus(OrderStatus.fromString(resultSet.getString(UserOrderTableConstants.ORDER_STATUS)));
                userOrder.setUserId(resultSet.getLong(UserOrderTableConstants.USER_ACCOUNT_ID));
                userOrder.getDeliveryAddress().setId(resultSet.getLong(UserOrderTableConstants.DELIVERY_ADDRESS_ID));
                userOrder.setCustomerName(resultSet.getString(UserOrderTableConstants.CUSTOMER_NAME));
                userOrder.setCustomerPhoneNumber(resultSet.getString(UserOrderTableConstants.CUSTOMER_PHONE_NUMBER));
                return userOrder;
            }

            @Override
            public List<String> getColumnNames() {
                return Arrays.asList(UserOrderTableConstants.TIME_OF_DELIVERY,
                        UserOrderTableConstants.ORDER_STATUS,
                        UserOrderTableConstants.USER_ACCOUNT_ID,
                        UserOrderTableConstants.DELIVERY_ADDRESS_ID,
                        UserOrderTableConstants.CUSTOMER_NAME,
                        UserOrderTableConstants.CUSTOMER_PHONE_NUMBER);
            }

            @Override
            public void populateStatement(PreparedStatement statement, UserOrder entity) throws SQLException {

                statement.setObject(1, entity.getTimeOfDelivery());
                statement.setString(2, entity.getOrderStatus().getValue());
                statement.setLong(3, entity.getUserId());
                statement.setLong(4, entity.getDeliveryAddress().getId());
                statement.setString(5, entity.getCustomerName());
                statement.setString(6, entity.getCustomerPhoneNumber());
            }
        };
    }

    @Override
    public UserOrder findBuildingUpUserOrder(Long id) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_ID_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, id));

        AtomicReference<UserOrder> result = new AtomicReference<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(SELECT_BUILDING_UP_ORDER_BY_USER_ID_QUERY, UserOrderTableConstants.USER_ORDER_TABLE_NAME, UserOrderTableConstants.USER_ACCOUNT_ID, UserOrderTableConstants.ORDER_STATUS);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.setString(2, OrderStatus.BUILD_UP.getValue());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    try {
                        result.set(getUserOrderRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                        return null;
                    }
                } else {
                    LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get()));
                    return null;
                }
            }
            LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get().toString()));
            return result.get();
        } finally {
            connectionLock.unlock();
        }
    }
}
