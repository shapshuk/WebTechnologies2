package com.restraunt.shapshuk.entity.orderitem.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.orderitem.model.OrderItem;

import java.sql.SQLException;
import java.util.List;

public interface OrderItemDao extends CrudDao<OrderItem> {

    List<OrderItem> findAllItemsByOrderId(Long orderId) throws SQLException, ConnectionException;

    OrderItem getFromCurrentOrderByDishId(Long dishId, Long userOrderId) throws ConnectionException, SQLException;

    List<OrderItem> getFromCurrentOrderByDishCategoryName(String categoryName, Long userOrderId) throws ConnectionException, SQLException;

    void deleteByOrderId(Long orderId) throws SQLException, ConnectionException;
}
