package com.restraunt.shapshuk.entity.orderitem.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.category.dao.DishCategoryTableConstants;
import com.restraunt.shapshuk.entity.dish.dao.DishTableConstants;
import com.restraunt.shapshuk.entity.order.dao.UserOrderTableConstants;
import com.restraunt.shapshuk.entity.orderitem.model.OrderItem;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;

public class OrderItemDaoImpl extends GenericDao<OrderItem> implements OrderItemDao {

    private static final Logger LOGGER = Logger.getLogger(OrderItemDaoImpl.class.getName());

    private static final String SELECT_BY_DISH_ID_AND_USER_ORDER_ID_QUERY = "" +
            "SELECT {0}.*\n" +
            "FROM {0}\n" +
            "WHERE {0}.{1} = ?\n" +
            "  AND {0}.{2} = ?";
    private static final String SELECT_BY_DISH_CATEGORY_NAME_QUERY = "" +
            "SELECT {0}.*\n" +
            "FROM {0},\n" +
            "     {3},\n" +
            "     {6}\n" +
            "WHERE {0}.{1} = {3}.{4}\n" +
            "  AND {3}.{5} = {6}.{7}\n" +
            "  AND {6}.{8} = ?\n" +
            "  AND {0}.{2} = ?";
    private static final String SELECT_ALL_BY_ORDER_ID_QUERY = "" +
            "SELECT {0}.*\n" +
            "FROM {0},\n" +
            "     {2}\n" +
            "WHERE {0}.{1} = {2}.{3}\n" +
            "  AND {2}.{3} = ?";
    private static final String DELETE_BY_ORDER_ID_QUERY = "" +
            "DELETE FROM {0}\n" +
            "WHERE {0}.{1} = ?";
    private final Lock connectionLock = new ReentrantLock();
    private final ConnectionManager connectionManager;

    public OrderItemDaoImpl(ConnectionManager connectionManager) {
        super(OrderItemTableConstants.ORDER_ITEM_TABLE_NAME, getOrderItemRowMapper(), connectionManager);
        this.connectionManager = connectionManager;
    }

    private static IdentifiedRowMapper<OrderItem> getOrderItemRowMapper() {

        return new IdentifiedRowMapper<OrderItem>() {

            @Override
            public OrderItem map(ResultSet resultSet) throws SQLException {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(resultSet.getLong(OrderItemTableConstants.ID));
                orderItem.setDishAmount(resultSet.getInt(OrderItemTableConstants.DISH_AMOUNT));
                orderItem.setItemCost(resultSet.getBigDecimal(OrderItemTableConstants.ITEM_COST));
                orderItem.getDish().setId(resultSet.getLong(OrderItemTableConstants.DISH_ID));
                orderItem.setUserOrderId(resultSet.getLong(OrderItemTableConstants.USER_ORDER_ID));
                return orderItem;
            }

            @Override
            public List<String> getColumnNames() {
                return Arrays.asList(OrderItemTableConstants.DISH_AMOUNT,
                        OrderItemTableConstants.ITEM_COST,
                        OrderItemTableConstants.DISH_ID,
                        OrderItemTableConstants.USER_ORDER_ID);
            }

            @Override
            public void populateStatement(PreparedStatement statement, OrderItem entity) throws SQLException {

                statement.setInt(1, entity.getDishAmount());
                statement.setBigDecimal(2, entity.getItemCost());
                statement.setLong(3, entity.getDish().getId());
                statement.setLong(4, entity.getUserOrderId());
            }
        };
    }

    @Override
    public List<OrderItem> findAllItemsByOrderId(Long orderId) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));

        List<OrderItem> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {

            String sql = MessageFormat.format(SELECT_ALL_BY_ORDER_ID_QUERY,
                    OrderItemTableConstants.ORDER_ITEM_TABLE_NAME, OrderItemTableConstants.USER_ORDER_ID,
                    UserOrderTableConstants.USER_ORDER_TABLE_NAME, UserOrderTableConstants.ID);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, orderId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    try {
                        result.add(getOrderItemRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
            return result;
        } finally {
            connectionLock.unlock();
        }
    }

    @Override
    public OrderItem getFromCurrentOrderByDishId(Long dishId, Long orderId) throws ConnectionException, SQLException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format("%s called %s method for entity with Order id = [%s] and Dish id = [%s]", this.getClass().getSimpleName(), nameOfCurrentMethod, orderId, dishId));

        AtomicReference<OrderItem> result = new AtomicReference<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(SELECT_BY_DISH_ID_AND_USER_ORDER_ID_QUERY, OrderItemTableConstants.ORDER_ITEM_TABLE_NAME, OrderItemTableConstants.DISH_ID, OrderItemTableConstants.USER_ORDER_ID);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, dishId);
                statement.setLong(2, orderId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    try {
                        result.set(getOrderItemRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
            return result.get();
        } finally {
            connectionLock.unlock();
        }
    }

    @Override
    public List<OrderItem> getFromCurrentOrderByDishCategoryName(String categoryName, Long orderId) throws ConnectionException, SQLException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format("%s called %s method for entity with Order id = [%s] and Category name = [%s]", this.getClass().getSimpleName(), nameOfCurrentMethod, orderId, categoryName));

        List<OrderItem> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {

            String sql = MessageFormat.format(SELECT_BY_DISH_CATEGORY_NAME_QUERY,
                    OrderItemTableConstants.ORDER_ITEM_TABLE_NAME, OrderItemTableConstants.DISH_ID, OrderItemTableConstants.USER_ORDER_ID,
                    DishTableConstants.DISH_TABLE_NAME, DishTableConstants.ID, DishTableConstants.DISH_CATEGORY_ID,
                    DishCategoryTableConstants.DISH_CATEGORY_TABLE_NAME, DishCategoryTableConstants.ID, DishCategoryTableConstants.CATEGORY_NAME);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, categoryName);
                statement.setLong(2, orderId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    try {
                        result.add(getOrderItemRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
            return result;
        } finally {
            connectionLock.unlock();
        }
    }

    @Override
    public void deleteByOrderId(Long orderId) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format("%s called %s method for entity with Order id = [%s]", this.getClass().getSimpleName(), nameOfCurrentMethod, orderId));

        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(DELETE_BY_ORDER_ID_QUERY, OrderItemTableConstants.ORDER_ITEM_TABLE_NAME, OrderItemTableConstants.USER_ORDER_ID);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, orderId);
                statement.executeUpdate();
            }
        } finally {
            connectionLock.unlock();
        }
    }
}
