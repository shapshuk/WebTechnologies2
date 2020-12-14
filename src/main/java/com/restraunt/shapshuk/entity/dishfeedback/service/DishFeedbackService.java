package com.restraunt.shapshuk.entity.dishfeedback.service;

import com.restraunt.shapshuk.core.service.GenericService;
import com.restraunt.shapshuk.database.connection.ConnectionException;
import com.restraunt.shapshuk.entity.dishfeedback.model.DishFeedback;

import java.sql.SQLException;

public interface DishFeedbackService extends GenericService<DishFeedback> {

    DishFeedback getByUserIdAndDishId(Long userId, Long dishId) throws ConnectionException, SQLException;
}
