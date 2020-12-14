package com.restraunt.shapshuk.entity.category.service;

import com.restraunt.shapshuk.core.service.GenericService;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.model.DishCategory;

import java.sql.SQLException;

public interface DishCategoryService extends GenericService<DishCategory> {

    void deleteByName(String name) throws SQLException, ConnectionException;
}
