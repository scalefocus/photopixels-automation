package com.photopixels.api.dtos.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterUserRequestDto {

    private String name;
    private String email;
    private String password;
}
