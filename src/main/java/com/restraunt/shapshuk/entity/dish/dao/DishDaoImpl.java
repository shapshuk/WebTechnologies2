package com.restraunt.shapshuk.entity.dish.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.category.dao.DishCategoryTableConstants;
import com.restraunt.shapshuk.entity.dish.model.Dish;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;

public class DishDaoImpl extends GenericDao<Dish> implements DishDao {

    private static final Logger LOGGER = Logger.getLogger(DishDaoImpl.class.getName());

    private static final String SELECT_BY_DISH_CATEGORY_QUERY = "" +
            "SELECT {0}.*\n" +
            "FROM {0},\n" +
            "     {2}\n" +
            "WHERE {0}.{1} = {2}.{3}\n" +
            "AND {2}.{4} = ?";
    private static final String SELECT_ALL_LIMIT_QUERY = "SELECT * FROM {0} LIMIT ?, ?";

    private static ConnectionManager connectionManager;
    private final Lock connectionLock = new ReentrantLock();

    public DishDaoImpl(ConnectionManager connectionManager) {
        super(DishTableConstants.DISH_TABLE_NAME, getDishRowMapper(), connectionManager);
        this.connectionManager = connectionManager;
    }

    private static IdentifiedRowMapper<Dish> getDishRowMapper() {

        return new IdentifiedRowMapper<Dish>() {

            @Override
            public Dish map(ResultSet resultSet) throws SQLException, IOException {
                Dish dish = new Dish();
                dish.setId(resultSet.getLong(DishTableConstants.ID));
                dish.setName(resultSet.getString(DishTableConstants.DISH_NAME));
                dish.setCost(resultSet.getBigDecimal(DishTableConstants.DISH_COST));
                dish.setDescription(resultSet.getString(DishTableConstants.DISH_DESCRIPTION));
                dish.setPicture(convertBlobToString(resultSet));
                dish.getDishCategory().setId(resultSet.getLong(DishTableConstants.DISH_CATEGORY_ID));
                return dish;
            }

            @Override
            public List<String> getColumnNames() {
                return Arrays.asList(DishTableConstants.DISH_NAME,
                        DishTableConstants.DISH_COST,
                        DishTableConstants.DISH_DESCRIPTION,
                        DishTableConstants.DISH_PICTURE,
                        DishTableConstants.DISH_CATEGORY_ID);
            }

            @Override
            public void populateStatement(PreparedStatement statement, Dish entity) throws SQLException, ConnectionException {

                statement.setString(1, entity.getName());
                statement.setBigDecimal(2, entity.getCost());
                statement.setString(3, entity.getDescription());
                statement.setBlob(4, convertStringToBlob(entity.getPicture()));
                statement.setLong(5, entity.getDishCategory().getId());
            }
        };
    }

    private static Blob convertStringToBlob(String stringPicture) throws SQLException, ConnectionException {

        byte[] byteData = stringPicture.getBytes(StandardCharsets.UTF_8);
        Connection connection = connectionManager.getConnection();
        Blob blobPicture = connection.createBlob();
        blobPicture.setBytes(1, byteData);
        return blobPicture;
    }

    private static String convertBlobToString(ResultSet resultSet) throws SQLException, IOException {

        Blob blob = resultSet.getBlob(DishTableConstants.DISH_PICTURE);
        InputStream inputStream = blob.getBinaryStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        String stringPicture = outputStream.toString();

        inputStream.close();
        outputStream.close();

        return stringPicture;
    }

    @Override
    public List<Dish> getByCategoryName(String categoryName) throws ConnectionException, SQLException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_FOR_ENTITY_NAME_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, categoryName));

        List<Dish> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(SELECT_BY_DISH_CATEGORY_QUERY, DishTableConstants.DISH_TABLE_NAME, DishTableConstants.DISH_CATEGORY_ID,
                    DishCategoryTableConstants.DISH_CATEGORY_TABLE_NAME, DishCategoryTableConstants.ID, DishCategoryTableConstants.CATEGORY_NAME);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, categoryName);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    try {
                        result.add(getDishRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                        return new ArrayList<>();
                    }
                }
            }
            return result;
        } finally {
            connectionLock.unlock();
        }
    }

    @Override
    public List<Dish> findAll(int startRecord, int recordsPerPage) throws SQLException, ConnectionException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod));

        List<Dish> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(SELECT_ALL_LIMIT_QUERY, DishTableConstants.DISH_TABLE_NAME);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, startRecord);
                statement.setInt(2, recordsPerPage);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    try {
                        result.add(getDishRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                        return new ArrayList<>();
                    }
                }
            }
            return result;
        } finally {
            connectionLock.unlock();
        }
    }
}
