package com.photopixels.api.dtos.objectoperations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PropertiesResponseDto {
    private String id;
    private String dateCreated;
    private String mediaType;
	/**
	 * The API returns the boolean field as "isFavorite".
	 * Due to JavaBeans conventions, a boolean accessor named isFavorite() maps to the logical property "favorite".
	 * Lombok generates isFavorite() and setFavorite(boolean), so without @JsonProperty Jackson would expect
	 * the JSON key "favorite" and would treat "isFavorite" as unknown.
	 * We explicitly bind the JSON key to "isFavorite" to match the payload.
	 */
	@JsonProperty("isFavorite")
	private boolean isFavorite;
}
