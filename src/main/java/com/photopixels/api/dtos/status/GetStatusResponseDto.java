package com.photopixels.api.dtos.status;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetStatusResponseDto {

    private String registration;
    private String serverVersion;

}
