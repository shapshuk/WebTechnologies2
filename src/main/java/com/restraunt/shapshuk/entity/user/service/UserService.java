package com.restraunt.shapshuk.entity.user.service;

import com.restraunt.shapshuk.core.service.GenericService;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.user.model.User;

import java.sql.SQLException;

public interface UserService extends GenericService<User> {

    void register(User user) throws ConnectionException, SQLException;

    User login(String email, String password) throws ConnectionException, SQLException;

    User getByEmail(String email) throws ConnectionException, SQLException;
}
