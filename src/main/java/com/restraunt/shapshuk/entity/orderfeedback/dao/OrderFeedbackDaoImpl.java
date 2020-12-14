package com.restraunt.shapshuk.entity.orderfeedback.dao;

import com.restraunt.shapshuk.core.dao.GenericDao;
import com.restraunt.shapshuk.core.dao.IdentifiedRowMapper;
import com.restraunt.shapshuk.database.connection.ConnectionManager;
import com.restraunt.shapshuk.entity.orderfeedback.model.OrderFeedback;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class OrderFeedbackDaoImpl extends GenericDao<OrderFeedback> implements OrderFeedbackDao {

    public OrderFeedbackDaoImpl(ConnectionManager connectionManager) {
        super(OrderFeedbackTableConstants.ORDER_FEEDBACK_TABLE_NAME, getOrderFeedbackRowMapper(), connectionManager);
    }

    private static IdentifiedRowMapper<OrderFeedback> getOrderFeedbackRowMapper() {

        return new IdentifiedRowMapper<OrderFeedback>() {

            @Override
            public OrderFeedback map(ResultSet resultSet) throws SQLException, IOException {
                OrderFeedback orderFeedback = new OrderFeedback();
                orderFeedback.setId(resultSet.getLong(OrderFeedbackTableConstants.ID));
                orderFeedback.setOrderRating(resultSet.getInt(OrderFeedbackTableConstants.ORDER_RATING));
                orderFeedback.setOrderComment(resultSet.getString(OrderFeedbackTableConstants.ORDER_COMMENT));
                return orderFeedback;
            }

            @Override
            public List<String> getColumnNames() {
                return Arrays.asList(OrderFeedbackTableConstants.ORDER_RATING,
                        OrderFeedbackTableConstants.ORDER_COMMENT);
            }

            @Override
            public void populateStatement(PreparedStatement statement, OrderFeedback entity) throws SQLException {

                statement.setInt(1, entity.getOrderRating());
                statement.setString(2, entity.getOrderComment());
            }
        };
    }
}
