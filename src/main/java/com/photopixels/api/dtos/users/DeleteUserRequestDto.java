package com.photopixels.api.dtos.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DeleteUserRequestDto {

    private String password;
}
