package com.restraunt.shapshuk.entity.dish.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dish.model.Dish;

import java.sql.SQLException;
import java.util.List;

public interface DishDao extends CrudDao<Dish> {

    List<Dish> getByCategoryName(String categoryName) throws ConnectionException, SQLException;

    List<Dish> findAll(int startRecord, int recordsPerPage) throws SQLException, ConnectionException;

}
