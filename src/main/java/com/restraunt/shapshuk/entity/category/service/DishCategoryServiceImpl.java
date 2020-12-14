package com.restraunt.shapshuk.entity.category.service;

import com.restraunt.shapshuk.core.service.GenericServiceImpl;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.category.dao.DishCategoryDao;
import com.restraunt.shapshuk.entity.category.model.DishCategory;

import java.sql.SQLException;
import java.util.List;

public class DishCategoryServiceImpl extends GenericServiceImpl<DishCategory> implements DishCategoryService {

    private DishCategoryDao dishCategoryDao;

    public DishCategoryServiceImpl(DishCategoryDao dao) {
        super(dao);
        this.dishCategoryDao = dao;
    }

    @Override
    public List<DishCategory> findAll() throws SQLException, ConnectionException {

        return super.findAll();
    }

    @Override
    public Long save(DishCategory entity) throws SQLException, ConnectionException {

        return super.save(entity);
    }

    @Override
    public void deleteByName(String name) throws SQLException, ConnectionException {

        dishCategoryDao.deleteByName(name);
    }
}
