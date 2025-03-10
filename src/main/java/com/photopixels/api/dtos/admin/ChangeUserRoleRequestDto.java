package com.photopixels.api.dtos.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChangeUserRoleRequestDto {

    private String id;
    private Integer role;
}
