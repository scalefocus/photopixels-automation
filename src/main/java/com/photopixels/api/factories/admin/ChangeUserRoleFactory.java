package com.photopixels.api.factories.admin;

import com.photopixels.api.dtos.admin.ChangeUserRoleRequestDto;

public class ChangeUserRoleFactory {

    public ChangeUserRoleRequestDto createChangeUserRoleRequestDto(String userId, Integer role) {
        return ChangeUserRoleRequestDto.builder()
                .id(userId)
                .role(role)
                .build();
    }
}
