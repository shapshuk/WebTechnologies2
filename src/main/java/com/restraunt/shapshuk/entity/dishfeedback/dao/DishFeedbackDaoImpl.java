package com.restraunt.shapshuk.entity.dishfeedback.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.dishfeedback.model.DishFeedback;
import com.restraunt.shapshuk.core.constants.LoggerConstants;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.String.format;

public class DishFeedbackDaoImpl extends GenericDao<DishFeedback> implements DishFeedbackDao {

    private static final Logger LOGGER = Logger.getLogger(DishFeedbackDaoImpl.class.getName());

    private static final String SELECT_FEEDBACK_BY_USER_ID_AND_DISH_ID = "" +
            "SELECT *\n" +
            "FROM {0}\n" +
            "WHERE {0}.{1} = ?\n" +
            "  AND {0}.{2} = ?";
    private final Lock connectionLock = new ReentrantLock();
    private final ConnectionManager connectionManager;

    public DishFeedbackDaoImpl(ConnectionManager connectionManager) {
        super(DishFeedbackTableConstants.DISH_FEEDBACK_TABLE_NAME, getDishFeedbackRowMapper(), connectionManager);
        this.connectionManager = connectionManager;
    }

    private static IdentifiedRowMapper<DishFeedback> getDishFeedbackRowMapper() {

        return new IdentifiedRowMapper<DishFeedback>() {

            @Override
            public DishFeedback map(ResultSet resultSet) throws SQLException {
                DishFeedback dishFeedback = new DishFeedback();
                dishFeedback.setId(resultSet.getLong(DishFeedbackTableConstants.ID));
                dishFeedback.setDishRating(resultSet.getInt(DishFeedbackTableConstants.DISH_RATING));
                dishFeedback.setDishComment(resultSet.getString(DishFeedbackTableConstants.DISH_COMMENT));
                dishFeedback.setUserId(resultSet.getLong(DishFeedbackTableConstants.USER_ACCOUNT_ID));
                dishFeedback.setDishId(resultSet.getLong(DishFeedbackTableConstants.DISH_ID));
                return dishFeedback;
            }

            @Override
            public List<String> getColumnNames() {
                return Arrays.asList(DishFeedbackTableConstants.DISH_RATING,
                        DishFeedbackTableConstants.DISH_COMMENT,
                        DishFeedbackTableConstants.USER_ACCOUNT_ID,
                        DishFeedbackTableConstants.DISH_ID);
            }

            @Override
            public void populateStatement(PreparedStatement statement, DishFeedback entity) throws SQLException {

                statement.setInt(1, entity.getDishRating());
                statement.setString(2, entity.getDishComment());
                statement.setLong(3, entity.getUserId());
                statement.setLong(4, entity.getDishId());
            }
        };
    }

    @Override
    public DishFeedback getByUserIdAndDishId(Long userId, Long dishId) throws ConnectionException, SQLException {

        connectionLock.lock();

        String nameOfCurrentMethod = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();

        LOGGER.info(format("%s called %s method for entity with User id = [%s] and Dish id = [%s]", this.getClass().getSimpleName(), nameOfCurrentMethod, userId, dishId));

        AtomicReference<DishFeedback> result = new AtomicReference<>();
        try (Connection connection = connectionManager.getConnection()) {
            String sql = MessageFormat.format(SELECT_FEEDBACK_BY_USER_ID_AND_DISH_ID, DishFeedbackTableConstants.DISH_FEEDBACK_TABLE_NAME, DishFeedbackTableConstants.DISH_ID, DishFeedbackTableConstants.USER_ACCOUNT_ID);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, dishId);
                statement.setLong(2, userId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    try {
                        result.set(getDishFeedbackRowMapper().map(resultSet));
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                } else {
                    LOGGER.info(String.format(LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get()));
                    return null;
                }
            }
            LOGGER.info(format(LoggerConstants.CLASS_INVOKED_METHOD_AND_GOT_MESSAGE, this.getClass().getSimpleName(), nameOfCurrentMethod, result.get().toString()));
            return result.get();
        } finally {
            connectionLock.unlock();
        }
    }
}
