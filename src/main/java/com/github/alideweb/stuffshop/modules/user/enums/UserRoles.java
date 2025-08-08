package com.github.alideweb.stuffshop.modules.user.enums;

public enum UserRoles {
    ADMIN,
    USER;

    public static UserRoles getRole(String value) {
        if (value == null) return null;

        try {
            return UserRoles.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
