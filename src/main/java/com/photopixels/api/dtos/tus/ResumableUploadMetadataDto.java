package com.photopixels.api.dtos.tus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumableUploadMetadataDto {
    private String fileExtension;
    private String fileName;
    private String fileHash;
    private String fileSize;
    private String appleId;
    private String androidId;
    private String userId;

}
