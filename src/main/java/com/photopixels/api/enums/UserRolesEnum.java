package com.photopixels.api.enums;

import lombok.Getter;

@Getter
public enum UserRolesEnum {
    USER(1), ADMIN(0);

    final Integer value;

     UserRolesEnum(Integer value) {
         this.value = value;
     }
}
