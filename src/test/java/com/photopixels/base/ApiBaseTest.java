package com.photopixels.base;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.admin.GetUsersSteps;
import com.photopixels.api.steps.users.DeleteUserSteps;
import com.photopixels.api.steps.users.PostLoginSteps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ApiBaseTest extends BaseTest{

    protected String getUserToken() {
        return getToken(inputData.getUsername(), inputData.getPassword());
    }

    protected String getAdminToken() {
        return getToken(inputData.getUsernameAdmin(), inputData.getPasswordAdmin());
    }

    protected String getToken(String username, String password) {
        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(username, password);

        return loginResponseDto.getTokenType() + " " + loginResponseDto.getAccessToken();
    }

    protected String getUserId(String username) {
        GetUsersSteps getUsersSteps = new GetUsersSteps(getAdminToken());
        GetUserResponseDto[] getUserResponseDtos = getUsersSteps.getUsers();

        return Arrays.stream(getUserResponseDtos)
                .filter(user -> user.getEmail().equals(username)).findFirst().get().getId();
    }

    protected void deleteRegisteredUsers(Map<String, String> registeredUsers) {
        if (!registeredUsers.isEmpty()) {
            for (Map.Entry<String, String> entry : registeredUsers.entrySet()) {
                String token = getToken(entry.getKey(), entry.getValue());

                DeleteUserSteps deleteUserSteps = new DeleteUserSteps(token);
                deleteUserSteps.deleteUser(entry.getValue());
            }
        }
    }
}
