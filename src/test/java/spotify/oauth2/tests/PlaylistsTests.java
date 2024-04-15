package spotify.oauth2.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTests {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String accessToken = "Bearer BQDFGijo1qGBuAMQIdqClwfkEJt6-iHvohMmYv0yhcImmU4n_LxkvpgIxOCVUjDH8WCHjaehIc20gHB6F29Cd2damT2Gs1QzTMK-8YSvgCio41W7QkGOFKVFthi3LVlcaZxe0D1B-MqenJzd0lop3h9VsaUuOCK7D4BQwHdazisbU2eLqLLBwm2XqQ6ojlLg8cnKHEolcjXg_YeSrjJuH1UIt7g4ISmXuJAxlVz8p6N5k6hUvpmiNXdbc95FDNVAPM3iIAZZVCoB";

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
        String payload = "{\n" +
                "    \"name\": \"Farhad's Playlist\",\n" +
                "    \"description\": \"Farhad's playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .post("users/pr2jfo79b4fe7ylho07cocy9g/playlists")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(201)
                .body("name", equalTo("Farhad's Playlist"),
                        "description", equalTo("Farhad's playlist description"),
                        "public", equalTo(false));
    }

    @Test
    public void getPlaylistTest() {
        given()
                .spec(requestSpecification)
                .when()
                .get("/playlists/5cKfXcrynDdcRhtMbSaY8b")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("Farhad's Playlist"),
                        "description", equalTo("Farhad&#x27;s playlist description"),
                        "public", equalTo(true));
    }

    @Test
    public void updatePlaylistTest() {
        String  payload = "{\n" +
                "    \"name\": \"Updated Playlist Name - Farhad\",\n" +
                "    \"description\": \"Updated playlist description - Farhad\",\n" +
                "    \"public\": false\n" +
                "}";
        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .put("/playlists/7CKg8CGgG0G9cD9xeWkCRg")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void createPlaylistWithoutNameTest() {
        String payload = "{\n" +
                "    \"name\": \"\",\n" +
                "    \"description\": \"Farhad's playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given()
                .spec(requestSpecification)
                .body(payload)
                .when()
                .post("users/pr2jfo79b4fe7ylho07cocy9g/playlists")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(400)
                .body("error.status", equalTo(400),
                        "error.message", equalTo("Missing required field: name"));
    }

    @Test
    public void createPlaylistWithExpiredTokenTest() {
        String payload = "{\n" +
                "    \"name\": \"Farhad's Playlist\",\n" +
                "    \"description\": \"Farhad's playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given()
                .baseUri("https://api.spotify.com")
                .basePath("/v1")
                .header("Authorization", "12345")
                .contentType(ContentType.JSON)
                .log().all()
                .body(payload)
                .when()
                .post("users/pr2jfo79b4fe7ylho07cocy9g/playlists")
                .then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(401)
                .body("error.status", equalTo(401),
                        "error.message", equalTo("Invalid access token"));
    }
}
