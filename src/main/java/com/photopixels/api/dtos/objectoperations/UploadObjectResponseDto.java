package com.photopixels.api.dtos.objectoperations;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UploadObjectResponseDto {

    private String id;
    private Integer revision ;
    private Long quota ;
    private Long usedQuota ;

}
