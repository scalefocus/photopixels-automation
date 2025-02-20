package com.photopixels.api.factories.users;

import com.photopixels.api.dtos.users.RefreshTokenRequestDto;

public class RefreshTokenFactory {

    public RefreshTokenRequestDto createRefreshTokenRequestDto(String refreshToken) {
        return RefreshTokenRequestDto.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
