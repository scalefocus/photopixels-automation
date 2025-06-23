package com.photopixels.api.dtos.objectoperations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ObjectIdsRequestDto {

    private List<String> objectIds;

}
