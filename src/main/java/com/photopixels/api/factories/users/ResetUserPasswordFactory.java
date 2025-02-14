package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.ResetUserPasswordRequestDto;

public class ResetUserPasswordFactory {

    public ResetUserPasswordRequestDto createResetUserPasswordRequestDto(String code, String password, String email) {
        return ResetUserPasswordRequestDto.builder()
                .code(code)
                .password(password)
                .email(email)
                .build();
    }
}
