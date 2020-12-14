package com.restraunt.shapshuk.entity.order.model;

import com.restraunt.shapshuk.core.dao.IdentifiedRow;
import com.restraunt.shapshuk.entity.deliveryaddress.model.DeliveryAddress;
import com.restraunt.shapshuk.entity.order.constants.OrderStatus;
import com.restraunt.shapshuk.validation.MaxLength;
import com.restraunt.shapshuk.validation.MinLength;
import com.restraunt.shapshuk.validation.NotEmpty;
import com.restraunt.shapshuk.validation.PhoneNumber;
import com.restraunt.shapshuk.validation.ValidBean;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;

import java.time.LocalDateTime;

@ValidBean("userOrder")
public class UserOrder implements IdentifiedRow {

    private Long id;
    private LocalDateTime timeOfDelivery;
    private OrderStatus orderStatus;
    private Long userId;
    private DeliveryAddress deliveryAddress;
    @MinLength(5)
    @MaxLength(20)
    private String customerName;
    @NotEmpty
    @PhoneNumber(regex = CommonAppConstants.PHONE_NUMBER_PATTERN)
    private String customerPhoneNumber;

    public UserOrder() {
        this.deliveryAddress = new DeliveryAddress();
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "id=" + id +
                ", timeOfDelivery=" + timeOfDelivery +
                ", orderStatus=" + orderStatus +
                ", user=" + userId +
                ", deliveryAddress=" + deliveryAddress +
                ", customerName='" + customerName + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimeOfDelivery() {
        return timeOfDelivery;
    }

    public void setTimeOfDelivery(LocalDateTime timeOfDelivery) {
        this.timeOfDelivery = timeOfDelivery;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }
}
