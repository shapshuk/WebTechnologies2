package com.restraunt.shapshuk.entity.order.constants;

public enum OrderStatus {

    BUILD_UP("build_up"),
    SUBMITTED("submitted"),
    START_TO_PROCESS("start"),
    IN_PROCESS("process"),
    END_TO_PROCESS("end"),
    DEFAULT("default");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public static OrderStatus fromString(String name) {
        final OrderStatus[] values = OrderStatus.values();
        for (OrderStatus orderStatus : values) {
            if (orderStatus.value.equalsIgnoreCase(name) || orderStatus.name().equalsIgnoreCase(name)) {
                return orderStatus;
            }
        }
        return DEFAULT;
    }

    public String getValue() {
        return value;
    }

}
