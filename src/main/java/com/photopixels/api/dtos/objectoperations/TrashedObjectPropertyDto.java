package com.photopixels.api.dtos.objectoperations;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TrashedObjectPropertyDto {

    private String id;
    private String dateCreated;
    private String dateTrashed;
    private String mediaType;
    private Boolean isFavorite;

}
