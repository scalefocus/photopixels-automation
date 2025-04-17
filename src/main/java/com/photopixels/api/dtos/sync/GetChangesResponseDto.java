package com.photopixels.api.dtos.sync;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetChangesResponseDto {

    private String id;
    private Integer version;
    private JsonNode added;
    private JsonNode addedTime;
    private JsonNode trashed;
    private List<String> deleted;
}
