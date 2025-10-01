package com.photopixels.api.dtos.objectoperations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FavoritesResponseDto {

    @JsonProperty("isSuccess")
    private boolean isSuccess;

}
