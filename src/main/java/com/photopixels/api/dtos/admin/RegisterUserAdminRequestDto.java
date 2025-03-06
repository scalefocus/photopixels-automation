package com.photopixels.api.dtos.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterUserAdminRequestDto {

    private String name;
    private String email;
    private String password;
    private Integer role;
}
