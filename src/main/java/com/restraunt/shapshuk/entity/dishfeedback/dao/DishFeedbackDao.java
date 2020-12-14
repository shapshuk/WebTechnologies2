package com.restraunt.shapshuk.entity.dishfeedback.dao;

import com.restraunt.shapshuk.core.dao.CrudDao;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dishfeedback.model.DishFeedback;

import java.sql.SQLException;

public interface DishFeedbackDao extends CrudDao<DishFeedback> {

    DishFeedback getByUserIdAndDishId(Long userId, Long dishId) throws ConnectionException, SQLException;
}
