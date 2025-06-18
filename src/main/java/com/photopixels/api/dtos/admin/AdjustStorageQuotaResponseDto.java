package com.photopixels.api.dtos.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdjustStorageQuotaResponseDto {

    private Long quota;
    private Long usedQuota;
}
