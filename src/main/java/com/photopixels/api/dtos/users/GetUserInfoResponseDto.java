package com.photopixels.api.dtos.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetUserInfoResponseDto {

    private String email;
    private boolean isEmailConfirmed;
    private Long quota;
    private Long usedQuota;
    private ClaimsDto claims;

    // Need to define the getter for boolean because of Jackson parsing
    public boolean getIsEmailConfirmed() {
        return isEmailConfirmed;
    }
}
