package com.restraunt.shapshuk.entity.orderitem.service;

import com.restraunt.shapshuk.core.service.GenericService;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.orderitem.model.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemService extends GenericService<OrderItem> {

    List<OrderItem> findAllItemsByOrderId(Long orderId) throws SQLException, ConnectionException;

    void addNewOrderItem(OrderItem orderItem) throws SQLException, ConnectionException;

    OrderItem getFromCurrentOrderByDishId(Long dishId, Long userOrderId) throws ConnectionException, SQLException;

    List<OrderItem> getFromCurrentOrderByDishCategoryName(String categoryName, Long userOrderId) throws ConnectionException, SQLException;

    void deleteByOrderId(Long orderId) throws SQLException, ConnectionException;
}
