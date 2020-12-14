package com.restraunt.shapshuk.entity.category.model;

import com.restraunt.shapshuk.core.dao.IdentifiedRow;
import com.restraunt.shapshuk.validation.MinLength;
import com.restraunt.shapshuk.validation.ValidBean;

@ValidBean("dishCategory")
public class DishCategory implements IdentifiedRow {

    private Long id;
    @MinLength(3)
    private String categoryName;

    @Override
    public String toString() {
        return "DishCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
