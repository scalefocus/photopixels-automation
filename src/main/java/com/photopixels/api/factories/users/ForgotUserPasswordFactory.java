package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.ChangeUserPasswordRequestDto;
import com.photopixels.api.dtos.users.ForgotUserPasswordRequestDto;

public class ForgotUserPasswordFactory {

    public ForgotUserPasswordRequestDto createForgotUserPasswordRequestDto(String email) {
        return ForgotUserPasswordRequestDto.builder()
                .email(email)
                .build();
    }
}
