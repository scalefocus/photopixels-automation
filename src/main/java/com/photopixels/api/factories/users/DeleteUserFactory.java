package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.DeleteUserRequestDto;

public class DeleteUserFactory {

    public DeleteUserRequestDto createDeleteUserDto(String password) {
        return DeleteUserRequestDto.builder()
                .password(password)
                .build();
    }
}
