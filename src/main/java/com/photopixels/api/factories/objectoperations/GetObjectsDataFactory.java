package com.photopixels.api.factories.objectoperations;

import com.photopixels.api.dtos.objectoperations.GetObjectsDataRequestDto;

import java.util.List;

public class GetObjectsDataFactory {

    public GetObjectsDataRequestDto createGetObjectsDataRequestDto(List<String> objectIds) {
        return GetObjectsDataRequestDto.builder()
                .objectIds(objectIds)
                .build();
    }
}
