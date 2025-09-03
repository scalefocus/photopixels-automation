package com.photopixels.api.steps.email;

import com.photopixels.api.dtos.email.FetchEmailResponseDto;
import com.photopixels.api.dtos.email.GetEmailAddressResponseDto;
import com.photopixels.api.dtos.email.GetEmailListResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class GetEmailListSteps {

    private static final String EMAIL_URI = "https://api.guerrillamail.com/ajax.php";
    private static final Pattern SIX_DIGIT_CODE = Pattern.compile("\\b\\d{6}\\b");
    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetEmailListSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();
        requestSpecification.addBaseUri(EMAIL_URI);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

    }

    public long getMailIdFromSender(String expectedSender, String sidToken, int maxRetries, int delayMillis) {
        for (int i = 0; i < maxRetries; i++) {
            GetEmailListResponseDto response = getEmailList(sidToken);

            if (response.getList() != null) {
                Optional<FetchEmailResponseDto> mailId = response.getList().stream().filter(email -> {
                    String from = Optional.ofNullable(email.getMailFrom()).orElse("");
                    return expectedSender.equalsIgnoreCase(from);
                }).findFirst();
                if (mailId.isPresent()) {
                    return mailId.get().getMailId();
                }
            }
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for email", e);
            }
        }
        throw new RuntimeException("Email from " + expectedSender + " not found after " + maxRetries + " retries");
    }

    public String getResetCodeFromMail(String sidToken, long mailId) {
        FetchEmailResponseDto response = fetchEmail(sidToken, mailId);

        return SIX_DIGIT_CODE.matcher(response.getMailBody()).results().map(MatchResult::group).findFirst().orElseThrow(() ->
                new RuntimeException("No code found"));
    }

    public GetEmailAddressResponseDto getEmailAddress() {
        Response response = getEmailAddressResponse();

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetEmailAddressResponseDto.class);
    }

    public GetEmailListResponseDto getEmailList(String sidToken) {
        Response response = getEmailListResponse(sidToken);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetEmailListResponseDto.class);
    }

    public FetchEmailResponseDto fetchEmail(String sidToken, long mailId) {
        Response response = getEmailDetailsResponse(sidToken, mailId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(FetchEmailResponseDto.class);
    }

    private Response getEmailAddressResponse() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("f", "get_email_address");
        requestSpecification.addQueryParams(queryParams);

        return requestOperationsHelper.sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }

    private Response getEmailListResponse(String sidToken) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("f", "get_email_list");
        queryParams.put("offset", String.valueOf(0));
        queryParams.put("sid_token", sidToken);
        requestSpecification.addQueryParams(queryParams);

        return requestOperationsHelper.sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }

    private Response getEmailDetailsResponse(String sidToken, long mailId) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("f", "fetch_email");
        queryParams.put("sid_token", sidToken);
        queryParams.put("email_id", String.valueOf(mailId));
        requestSpecification.addQueryParams(queryParams);

        return requestOperationsHelper.sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
