package com.photopixels.api.dtos.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FetchLocalEmailResponseDto {

    @JsonProperty("items")
    private JsonNode items;

    public String getMailBody() {
        return items.get(0).path("Content").path("Body").asText("");
    }
}
