package com.wzd.port.constant;

public enum TypeEnum {
    SERIES("series"), POINT("point");
    String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
