package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.ChangeUserPasswordRequestDto;

public class ChangeUserPasswordFactory {

    public ChangeUserPasswordRequestDto createChangeUserPasswordRequestDto(String oldPassword, String newPassword) {
        return ChangeUserPasswordRequestDto.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
    }
}
