package com.photopixels.api.dtos.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdjustStorageQuotaRequestDto {

    private String id;
    private Long quota;
}
