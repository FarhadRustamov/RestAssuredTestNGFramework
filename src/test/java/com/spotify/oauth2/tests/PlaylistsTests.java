package com.spotify.oauth2.tests;

import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTests {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String accessToken = "Bearer BQACyT6x_x1KuILif9SecA1q_NKjPMw-r4tAG6PzwzCUhTZiVwK0dDDz-a7sRr6hQy8oO_vklsy5xX-TFLk7D7ZGC23yOeZ4SVmbOSbLCMBXT1jaTR272QNgq7PVNalEG9Ku5dJlQfFtSeEhHGuDrI1OKVMpta8PQZ7myg7uNm7fO5dHbuU2xb1rxEuP6BRVF2nBn7Lei7ppQzlLHSXWF3Vyj8gGIRiNaV50fiUiqe8gXCop0tDBUe50UPiRBM6bX2rFt6VWdWj9";

    @BeforeClass
    public void beforeClass() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.spotify.com")
                .setBasePath("/v1")
                .addHeader("Authorization", accessToken)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void createPlaylistTest() {
        Playlist playlistRequest = new Playlist().setName("Farhad's playlist")
                .setDescription("Farhad's playlist description")
                .setPublic(false);

        Playlist playlistResponse = given()
                .spec(requestSpecification)
                .body(playlistRequest)
                .when()
                .post("users/pr2jfo79b4fe7ylho07cocy9g/playlists")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(201)
                .extract()
                .response()
                .as(Playlist.class);

        assertThat(playlistResponse.getName(), equalTo(playlistRequest.getName()));
        assertThat(playlistResponse.getDescription(), equalTo(playlistRequest.getDescription()));
        assertThat(playlistResponse.getPublic(), equalTo(playlistRequest.getPublic()));
    }

    @Test
    public void getPlaylistTest() {
        Playlist playlistResponse = given()
                .spec(requestSpecification)
                .when()
                .get("/playlists/0YBkdwfzXqI0JKPDmwFWIv")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .as(Playlist.class);

        assertThat(playlistResponse.getName(), equalTo("Farhad's playlist"));
        assertThat(playlistResponse.getDescription(), equalTo("Farhad&#x27;s playlist description"));
        assertThat(playlistResponse.getPublic(), equalTo(true));
    }

    @Test
    public void updatePlaylistTest() {
        Playlist playlistRequest = new Playlist()
                .setName("Updated playlist Name - Farhad")
                .setDescription("Updated playlist description - Farhad")
                .setPublic(false);

        given()
                .spec(requestSpecification)
                .body(playlistRequest)
                .when()
                .put("/playlists/7CKg8CGgG0G9cD9xeWkCRg")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void createPlaylistWithoutNameTest() {
        Playlist playlistRequest = new Playlist()
                .setName("")
                .setDescription("Farhad's playlist description")
                .setPublic(false);

        ErrorRoot errorRootResponse = given()
                .spec(requestSpecification)
                .body(playlistRequest)
                .when()
                .post("users/pr2jfo79b4fe7ylho07cocy9g/playlists")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(400)
                .extract()
                .response()
                .as(ErrorRoot.class);

        assertThat(errorRootResponse.getError().getStatus(), equalTo(400));
        assertThat(errorRootResponse.getError().getMessage(), equalTo("Missing required field: name"));
    }

    @Test
    public void createPlaylistWithExpiredTokenTest() {
        Playlist playlistRequest = new Playlist()
                .setName("Farhad's playlist")
                .setDescription("Farhad's playlist description")
                .setPublic(false);

        ErrorRoot errorRootResponse = given()
                .baseUri("https://api.spotify.com")
                .basePath("/v1")
                .header("Authorization", "12345")
                .contentType(ContentType.JSON)
                .log().all()
                .body(playlistRequest)
                .when()
                .post("users/pr2jfo79b4fe7ylho07cocy9g/playlists")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(400)
                .extract()
                .response()
                .as(ErrorRoot.class);

        assertThat(errorRootResponse.getError().getStatus(), equalTo(400));
        assertThat(errorRootResponse.getError().getMessage(), equalTo("Only valid bearer authentication supported"));
    }
}
