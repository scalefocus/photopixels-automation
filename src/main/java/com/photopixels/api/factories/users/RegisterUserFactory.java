package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.RegisterUserRequestDto;

public class RegisterUserFactory {

    public RegisterUserRequestDto createRegisterNewUserDto(String name, String email, String password) {
        return RegisterUserRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}
