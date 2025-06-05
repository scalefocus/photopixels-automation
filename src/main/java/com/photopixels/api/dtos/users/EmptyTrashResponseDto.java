package com.photopixels.api.dtos.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class EmptyTrashResponseDto {

    @JsonProperty("isSuccess") // Jackson can't map "isSuccess" automatically → use @JsonProperty to avoid deserialization error
    private boolean isSuccess;
}