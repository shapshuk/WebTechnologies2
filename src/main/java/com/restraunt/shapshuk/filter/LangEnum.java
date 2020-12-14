package com.restraunt.shapshuk.filter;

public enum LangEnum {

    RUSSIAN("ru"),
    ENGLISH("en"),
    DEFAULT("default");

    private String value;

    LangEnum(String value) {
        this.value = value;
    }

    public static LangEnum fromString(String name) {
        final LangEnum[] values = LangEnum.values();
        for (LangEnum enumElement : values) {
            if (enumElement.value.equalsIgnoreCase(name) || enumElement.name().equalsIgnoreCase(name)) {
                return enumElement;
            }
        }
        return DEFAULT;
    }

    public String getValue() {
        return value;
    }
}
