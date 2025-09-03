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
public class FetchEmailResponseDto {

    @JsonProperty("mail_from")
    private String mailFrom;
    @JsonProperty("mail_id")
    private long mailId;
    @JsonProperty("mail_body")
    private String mailBody;

}
