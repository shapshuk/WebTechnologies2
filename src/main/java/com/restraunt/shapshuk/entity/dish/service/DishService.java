package com.restraunt.shapshuk.entity.dish.service;

import com.restraunt.shapshuk.core.service.GenericService;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dish.model.Dish;

import java.sql.SQLException;
import java.util.List;

public interface DishService extends GenericService<Dish> {

    List<Dish> getByCategory(String categoryName) throws ConnectionException, SQLException;

    List<Dish> findAll(int currentPage, int recordsPerPage) throws SQLException, ConnectionException;

}
