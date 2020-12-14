package com.restraunt.shapshuk.entity.order.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.order.model.UserOrder;

import java.sql.SQLException;

public interface UserOrderDao extends CrudDao<UserOrder> {

    UserOrder findBuildingUpUserOrder(Long id) throws SQLException, ConnectionException;
}
