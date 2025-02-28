package com.photopixels.api.factories.admin;

import com.photopixels.api.dtos.admin.ResetPasswordAdminRequestDto;

public class ResetPasswordAdminFactory {

    public ResetPasswordAdminRequestDto createResetPasswordAdminRequestDto(String password, String email) {
        return ResetPasswordAdminRequestDto.builder()
                .password(password)
                .email(email)
                .build();
    }
}
