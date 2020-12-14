package com.restraunt.shapshuk.entity.category.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;

import java.sql.SQLException;

public interface DishCategoryDao extends CrudDao<DishCategory> {

    DishCategory getByName(String categoryName) throws SQLException, ConnectionException;

    void deleteByName(String name) throws SQLException, ConnectionException;
}
