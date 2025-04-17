package com.photopixels.api.dtos.tus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumableUploadDto {
    private String fileId;
    private long byteOffset;
    private long fileSize;
    private ResumableUploadMetadataDto metadata;
    private String creation;
    private String expiration;
}
