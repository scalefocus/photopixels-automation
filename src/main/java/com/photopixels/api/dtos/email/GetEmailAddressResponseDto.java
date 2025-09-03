package com.photopixels.api.dtos.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetEmailAddressResponseDto {

    @JsonProperty("email_addr")
    private String emailAddress;
    @JsonProperty("sid_token")
    private String sidToken;
}
