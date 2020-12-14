package com.restraunt.shapshuk.validation;

import java.lang.reflect.Field;

public interface FieldValidator {

    BrokenField validate(Object entity, Field field);
}
