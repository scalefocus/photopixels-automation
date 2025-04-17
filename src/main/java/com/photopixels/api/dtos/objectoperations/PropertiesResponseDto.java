package com.photopixels.api.dtos.objectoperations;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertiesResponseDto {

    private String id;
    private String dateCreated;
    private String mediaType;

}
