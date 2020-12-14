package com.restraunt.shapshuk.entity.order.service;

import com.restraunt.shapshuk.core.service.GenericService;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.order.model.UserOrder;
import com.restraunt.shapshuk.entity.user.model.User;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface UserOrderService extends GenericService<UserOrder> {

    UserOrder createNewOrder(User user) throws SQLException, ConnectionException;

    BigDecimal getOrderCost(UserOrder order) throws ConnectionException, SQLException;

    UserOrder getBuildingUpUserOrder(String sessionId) throws SQLException, ConnectionException;
}
