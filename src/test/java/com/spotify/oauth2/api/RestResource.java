package com.spotify.oauth2.api;

import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response postAccount(HashMap<String, String> formParams) {
        return given()
                .spec(SpecBuilder.getAccountRequestSpec())
                .formParams(formParams)
                .when()
                .post(Route.API + Route.TOKEN)
                .then()
                .spec(SpecBuilder.responseSpec())
                .extract()
                .response();
    }

    public static Response post(String endpoint, String accessToken, Object payload) {
        return given()
                .spec(SpecBuilder.requestSpec())
                .header("Authorization", accessToken)
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .spec(SpecBuilder.responseSpec())
                .extract()
                .response();
    }

    public static Response get(String endpoint, String accessToken) {
        return given()
                .spec(SpecBuilder.requestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(endpoint)
                .then()
                .spec(SpecBuilder.responseSpec())
                .extract()
                .response();
    }

    public static Response put(String endpoint, String accessToken, Object payload) {
        return given()
                .spec(SpecBuilder.requestSpec())
                .header("Authorization", accessToken)
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .spec(SpecBuilder.responseSpec())
                .extract()
                .response();
    }
}
