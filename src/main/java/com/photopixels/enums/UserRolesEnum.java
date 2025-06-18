package com.photopixels.enums;

import lombok.Getter;

@Getter
public enum UserRolesEnum {
    USER(1, "User"), ADMIN(0, "Admin");

    final Integer value;
    final String text;

    UserRolesEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }
}
