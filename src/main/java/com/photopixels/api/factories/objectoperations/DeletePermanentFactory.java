package com.photopixels.api.factories.objectoperations;

import com.photopixels.api.dtos.objectoperations.DeletePermanentRequestDto;

import java.util.List;

public class DeletePermanentFactory {

    public DeletePermanentRequestDto createDeletePermanentRequestDto(List<String> objectIds) {
        return DeletePermanentRequestDto.builder()
                .objectIds(objectIds)
                .build();
    }
}