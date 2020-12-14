package com.restraunt.shapshuk.entity.role.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.role.model.UserRole;
import com.restraunt.shapshuk.entity.user.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRoleDao extends CrudDao<UserRole> {

    UserRole getByName(String roleName) throws SQLException, ConnectionException;

    List<UserRole> getUserRoles(User user) throws SQLException, ConnectionException;

    void setUserRoles(User user) throws SQLException, ConnectionException;
}
