package com.photopixels.api.factories.objectoperations;

import com.photopixels.api.dtos.objectoperations.ObjectIdsRequestDto;

import java.util.List;

public class ObjectIdsFactory {

    public ObjectIdsRequestDto createDeletePermanentRequestDto(List<String> objectIds) {
        return ObjectIdsRequestDto.builder()
                .objectIds(objectIds)
                .build();
    }
}