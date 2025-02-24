package com.photopixels.api.factories.admin;

import com.photopixels.api.dtos.admin.DisableRegistrationRequestDto;

public class DisableRegistrationFactory {

    public DisableRegistrationRequestDto createDisableRegistrationRequestDto(Boolean value) {
        return DisableRegistrationRequestDto.builder()
                .value(value)
                .build();
    }
}
