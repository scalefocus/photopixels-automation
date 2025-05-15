package com.photopixels.api.factories.objectoperations;

import com.photopixels.api.dtos.objectoperations.UpdateObjectTrashRemoveRequestDto;

public class UpdateObjectTrashRemoveFactory {

    public UpdateObjectTrashRemoveRequestDto createUpdateObjectTrashRemoveRequestDto(String objectId) {
        UpdateObjectTrashRemoveRequestDto requestDto = new UpdateObjectTrashRemoveRequestDto();
        requestDto.setId(objectId);
        return requestDto;
    }
}