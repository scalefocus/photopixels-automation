package com.photopixels.api.dtos.objectoperations;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FavoritesRequestDto {

    private List<String> objectIds;
    private int favoriteActionType;

}
