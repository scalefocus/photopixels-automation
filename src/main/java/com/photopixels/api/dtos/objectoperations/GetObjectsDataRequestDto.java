package com.photopixels.api.dtos.objectoperations;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetObjectsDataRequestDto {

    private List<String> objectIds;

}
