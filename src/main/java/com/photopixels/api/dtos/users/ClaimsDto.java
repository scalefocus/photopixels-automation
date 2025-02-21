package com.photopixels.api.dtos.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClaimsDto {

    private String id;
    private String email;
    private String fullName;
    private String role;
    @JsonProperty("AspNet.Identity.SecurityStamp")
    private String securityStamp;
    private String amr;
}
