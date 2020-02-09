package com.haojing.enums;

public enum UserTypeEnum {
    NORNAL_USER(1,"普通用户"),
    ADMIN(2,"管理员");
    private final int type;
    private final String msg;

    UserTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
