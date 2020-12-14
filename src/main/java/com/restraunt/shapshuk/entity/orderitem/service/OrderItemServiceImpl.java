package com.restraunt.shapshuk.entity.orderitem.service;

import com.restraunt.shapshuk.core.service.GenericServiceImpl;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.Transactional;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import com.restraunt.shapshuk.entity.dish.service.DishService;
import com.restraunt.shapshuk.entity.orderitem.dao.OrderItemDao;
import com.restraunt.shapshuk.entity.orderitem.model.OrderItem;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class OrderItemServiceImpl extends GenericServiceImpl<OrderItem> implements OrderItemService {

    private static final Logger LOGGER = Logger.getLogger(OrderItemServiceImpl.class.getName());

    private final OrderItemDao orderItemDao;
    private final DishService dishService;

    public OrderItemServiceImpl(OrderItemDao orderItemDao, DishService dishService) {
        super(orderItemDao);
        this.orderItemDao = orderItemDao;
        this.dishService = dishService;
    }

    @Transactional
    @Override
    public List<OrderItem> findAllItemsByOrderId(Long orderId) throws SQLException, ConnectionException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String
            .format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));

        List<OrderItem> orderItems = orderItemDao.findAllItemsByOrderId(orderId);
        findOrderItemsDish(orderItems);

        return orderItems;
    }

    private void findOrderItemsDish(List<OrderItem> orderItems) throws SQLException, ConnectionException {

        for (OrderItem orderItem : orderItems) {

            Long itemDishId = orderItem.getDish().getId();
            Dish dish = dishService.getById(itemDishId);
            orderItem.setDish(dish);
        }

        LOGGER.info("All items got there dishes");
    }

    @Override
    public void addNewOrderItem(OrderItem orderItem) throws SQLException, ConnectionException {

        BigDecimal dishCost = orderItem.getDish().getCost();
        Integer dishAmount = orderItem.getDishAmount();
        BigDecimal itemCost = dishCost.multiply(BigDecimal.valueOf(dishAmount));
        LOGGER.info(format("Item cost calculated, it's value = [%d]", itemCost.longValue()));

        orderItem.setItemCost(itemCost);
        LOGGER.info("Total cost has been set to item");

        this.save(orderItem);
    }

    @Override
    public OrderItem getFromCurrentOrderByDishId(Long dishId, Long userOrderId) throws ConnectionException, SQLException {

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String
            .format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));
        return orderItemDao.getFromCurrentOrderByDishId(dishId, userOrderId);
    }

    @Transactional
    @Override
    public List<OrderItem> getFromCurrentOrderByDishCategoryName(String categoryName, Long userOrderId) throws ConnectionException, SQLException {

        List<OrderItem> orderItems = orderItemDao.getFromCurrentOrderByDishCategoryName(categoryName, userOrderId);

        if (orderItems.isEmpty()) {
            return new ArrayList<>();
        }

        for (OrderItem orderItem : orderItems) {
            Long dishId = orderItem.getDish().getId();
            orderItem.setDish(dishService.getById(dishId));
        }

        return orderItems;
    }

    @Override
    public List<OrderItem> findAll() throws SQLException, ConnectionException {

        return super.findAll();
    }

    @Override
    public void deleteById(Long id) throws SQLException, ConnectionException {

        super.deleteById(id);
    }

    @Override
    public void deleteByOrderId(Long orderId) throws SQLException, ConnectionException {

        orderItemDao.deleteByOrderId(orderId);
    }
}
