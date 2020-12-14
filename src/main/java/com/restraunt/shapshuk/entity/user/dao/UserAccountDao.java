package com.restraunt.shapshuk.entity.user.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.user.model.User;

import java.sql.SQLException;

public interface UserAccountDao extends CrudDao<User> {

    User getByName(String name) throws SQLException, ConnectionException;

    User getByEmail(String name) throws SQLException, ConnectionException;
}
