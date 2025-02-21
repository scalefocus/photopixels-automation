package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.LoginRequestDto;

public class LoginFactory {

    public LoginRequestDto createLoginDto(String email, String password) {
        return LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
