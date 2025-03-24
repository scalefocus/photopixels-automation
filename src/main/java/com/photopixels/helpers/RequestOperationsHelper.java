package com.photopixels.helpers;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class RequestOperationsHelper {

	private Response sendRequest(RequestSpecification requestSpecification, ResponseSpecification responseSpecification,
			Method method) {

		// @formatter:off

		Response response = given().filter(new AllureRestAssured()).spec(requestSpecification).when().request(method);

		response.then().spec(responseSpecification);

		// @formatter:on

		return response;
	}

	public Response sendRequest(RequestSpecification requestSpecification, Method method) {
		return sendRequest(requestSpecification, given().then(), method);
	}

	public Response sendGetRequest(RequestSpecification requestSpecification) {
		return sendRequest(requestSpecification, Method.GET);
	}

	public Response sendPutRequest(RequestSpecification requestSpecification) {
		return sendRequest(requestSpecification, Method.PUT);
	}

	public Response sendPatchRequest(RequestSpecification requestSpecification) {
		return sendRequest(requestSpecification, Method.PATCH);
	}

	public Response sendDeleteRequest(RequestSpecification requestSpecification) {
		return sendRequest(requestSpecification, Method.DELETE);
	}

	public Response sendPostRequest(RequestSpecification requestSpecification) {
		return sendRequest(requestSpecification, Method.POST);
	}

	public Response sendHeadRequest(RequestSpecification requestSpecification) {
		return sendRequest(requestSpecification, Method.HEAD);
	}
}
