package com.photopixels.api.dtos.objectoperations;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetTrashedObjectsResponseDto {
    private String lastId;
    private List<TrashedObjectPropertyDto> properties;
}
