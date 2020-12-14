package com.restraunt.shapshuk.entity.category.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.category.model.DishCategory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.restraunt.shapshuk.core.constants.LoggerConstants.*;
import static java.lang.String.format;

public class DishCategoryDaoImpl extends GenericDao<DishCategory> implements DishCategoryDao {

    private static final Logger LOGGER = Logger.getLogger(DishCategoryDaoImpl.class.getName());

    private static final String SELECT_BY_NAME = "" +
            "SELECT *\n" +
            "FROM {0}\n" +
            "WHERE {1} = ?";
    private static final String DELETE_BY_NAME_QUERY = "" +
            "DELETE\n" +
            "FROM {0}\n" +
            "WHERE {1} = ?";
    private final Lock connectionLock = new ReentrantLock();
    private final ConnectionManager connectionManager;

    public DishCategoryDaoImpl(ConnectionManager connectionManager) {
        super(DishCategoryTableConstants.DISH_CATEGORY_TABLE_NAME, getDishCategoryRowMapper(), connectionManager);
        this.connectionManager = connectionManager;
    }

    private static IdentifiedRowMapper<DishCategory> getDishCategoryRowMapper() {

        return new IdentifiedRowMapper<DishCategory>() {

            @Override
            public DishCategory map(ResultSet resultSet) throws SQLException {
                DishCategory dishCategory = new DishCategory();
                dishCategory.setId(resultSet.getLong(DishCategoryTableConstants.ID));
                dishCategory.setCategoryName(resultSet.getString(DishCategoryTableConstants.CATEGORY_NAME));
                return dishCategory;
            }

            @Override
            public List<String> getColumnNames() {
                return Collections.singletonList(DishCategoryTableConstants.CATEGORY_NAME);
            }

            @Override
            public void populateStatement(PreparedStatement statement, DishCategory entity) throws SQLException {

                statement.setString(1, entity.getCategoryName());
            }
        };
    }

    @Override
    public DishCategory getByName(String categoryName) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format(CLASS_INVOKED_METHOD_FOR_ENTITY_NAME_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, categoryName));

        String sql = MessageFormat.format(SELECT_BY_NAME, DishCategoryTableConstants.DISH_CATEGORY_TABLE_NAME, DishCategoryTableConstants.CATEGORY_NAME);

        AtomicReference<DishCategory> result = new AtomicReference<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                try {
                    result.set(getDishCategoryRowMapper().map(resultSet));
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            } else {
                LOGGER.info(format(CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get()));
                return null;
            }
            LOGGER.info(format(CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get().toString()));
            return result.get();
        } catch (ConnectionException e) {
            LOGGER.error(e.getMessage());
            throw e;
        } finally {
            connectionLock.unlock();
        }
    }

    @Override
    public void deleteByName(String name) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format(CLASS_INVOKED_METHOD_FOR_ENTITY_ID_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, name));

        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(DELETE_BY_NAME_QUERY, DishCategoryTableConstants.DISH_CATEGORY_TABLE_NAME, DishCategoryTableConstants.CATEGORY_NAME);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.executeUpdate();
            }
        } finally {
            connectionLock.unlock();
        }
    }
}
