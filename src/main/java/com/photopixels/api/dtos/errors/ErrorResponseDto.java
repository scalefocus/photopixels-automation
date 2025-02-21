package com.photopixels.api.dtos.errors;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorResponseDto {

	private String type;
	private String title;
	private int status;
	private JsonNode errors;
	private String traceId;

	public String extractErrorMessageByKey(String key) {
		return this.getErrors().get(key).get(0).asText();
	}
}
