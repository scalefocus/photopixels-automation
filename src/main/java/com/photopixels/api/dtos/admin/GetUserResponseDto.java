package com.photopixels.api.dtos.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetUserResponseDto {

    private String id;
    private String name;
    private String email;
    private String userName;
    private String dateCreated;
    private Long quota;
    private Long usedQuota;
    private Integer role;
}
