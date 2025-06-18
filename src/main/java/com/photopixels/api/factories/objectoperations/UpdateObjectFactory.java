package com.photopixels.api.factories.objectoperations;

import com.photopixels.api.dtos.objectoperations.UpdateObjectRequestDto;

public class UpdateObjectFactory {

    public UpdateObjectRequestDto createUpdateObjectRequestDto(String appleCloudId, String androidCloudId) {
        return UpdateObjectRequestDto.builder()
                .appleCloudId(appleCloudId)
                .androidCloudId(androidCloudId)
                .build();
    }
}
