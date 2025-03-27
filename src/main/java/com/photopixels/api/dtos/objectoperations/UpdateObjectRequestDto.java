package com.photopixels.api.dtos.objectoperations;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateObjectRequestDto {

    private String appleCloudId;
    private String androidCloudId;
}
