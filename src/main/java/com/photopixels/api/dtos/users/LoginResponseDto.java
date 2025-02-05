package com.photopixels.api.dtos.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginResponseDto {

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String tokenType;
    private String userId;
}
