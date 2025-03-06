package com.photopixels.api.factories.admin;

import com.photopixels.api.dtos.admin.AdjustStorageQuotaRequestDto;

public class AdjustStorageQuotaFactory {

    public AdjustStorageQuotaRequestDto createAdjustStorageQuotaRequestDto(String userId, Long quota) {
        return AdjustStorageQuotaRequestDto.builder()
                .id(userId)
                .quota(quota)
                .build();
    }
}
