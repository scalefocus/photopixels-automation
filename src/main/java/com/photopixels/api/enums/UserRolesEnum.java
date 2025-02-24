package com.photopixels.api.enums;

import lombok.Getter;

@Getter
public enum UserRolesEnum {
    USER("1"), ADMIN("0");

    String value;

     UserRolesEnum(String value) {
         this.value = value;
     }
}
