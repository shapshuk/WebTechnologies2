package com.restraunt.shapshuk.validation.cost;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinCost {

    String value();

    boolean inclusive();
}
