package com.photopixels.api.dtos.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResetUserPasswordRequestDto {

    private String code;
    private String password;
    private String email;

}
