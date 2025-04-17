package com.photopixels.api.dtos.tus;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumableUploadsResponseDto {
    private List<ResumableUploadDto> userUploads;
}