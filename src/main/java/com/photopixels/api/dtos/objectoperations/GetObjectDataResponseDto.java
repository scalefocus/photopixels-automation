package com.photopixels.api.dtos.objectoperations;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetObjectDataResponseDto {

    private String id;
    private String thumbnail;
    private String contentType;
    private String hash;
    private String originalHash; // Added field to support BE change task-#72
    private String appleCloudId;
    private String androidCloudId;
    private int width;
    private int height;
    private String dateCreated;

}
