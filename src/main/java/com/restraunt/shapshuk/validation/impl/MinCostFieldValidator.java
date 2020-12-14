package com.restraunt.shapshuk.validation.impl;

import com.restraunt.shapshuk.validation.BrokenField;
import com.restraunt.shapshuk.validation.FieldValidator;
import com.restraunt.shapshuk.validation.ValidationException;
import com.restraunt.shapshuk.validation.cost.MinCost;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class MinCostFieldValidator implements FieldValidator {

    @Override
    public BrokenField validate(Object entity, Field field) {

        if (BigDecimal.class.equals(field.getType())) {

            MinCost minCost = field.getAnnotation(MinCost.class);
            try {
                BigDecimal fieldValue = (BigDecimal) field.get(entity);

                if (fieldValue != null) {
                    BigDecimal annotationValue = new BigDecimal(minCost.value());
                    if (
                            (minCost.inclusive() && fieldValue.compareTo(annotationValue) < 0)
                                    || (!minCost.inclusive() && fieldValue.compareTo(annotationValue) <= 0)
                    ) {
                        String annotationArg = String.format("Min cost in field %s is %s", field.getName(), minCost.value());
                        return new BrokenField(field.getName(), fieldValue, "minCost", annotationArg);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new ValidationException(e);
            }
        }
        return null;
    }
}
