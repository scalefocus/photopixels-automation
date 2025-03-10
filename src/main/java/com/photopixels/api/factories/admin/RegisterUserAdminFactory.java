package com.photopixels.api.factories.admin;

import com.photopixels.api.dtos.admin.RegisterUserAdminRequestDto;

public class RegisterUserAdminFactory {

    public RegisterUserAdminRequestDto createRegisterUserAdminDto(String name, String email, String password, Integer role) {
        return RegisterUserAdminRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}
