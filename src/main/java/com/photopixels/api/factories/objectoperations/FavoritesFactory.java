package com.photopixels.api.factories.objectoperations;

import com.photopixels.api.dtos.objectoperations.FavoritesRequestDto;

import java.util.List;

public class FavoritesFactory {

    public FavoritesRequestDto createFavoritesRequestDto(List<String> objectIds, int favoriteActionType) {
        return FavoritesRequestDto.builder()
                .objectIds(objectIds)
                .favoriteActionType(favoriteActionType)
                .build();
    }
}
