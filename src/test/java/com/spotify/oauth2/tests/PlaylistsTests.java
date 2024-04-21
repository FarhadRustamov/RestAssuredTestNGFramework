package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTests {

    public Playlist payloadBuilder(String name, String description, boolean _public) {
        return Playlist.builder().name(name).description(description)._public(_public).build();
    }

    public void assertPlaylistEqual(Playlist payload, Playlist playlistResponse) {
        assertThat(playlistResponse.getName(), equalTo(payload.getName()));
        assertThat(playlistResponse.getDescription(), equalTo(payload.getDescription()));
        assertThat(playlistResponse.get_public(), equalTo(payload.get_public()));
    }

    public void assertErrorEqual(ErrorRoot errorRootResponse, int expectedStatusCode, String expectedMessage) {
        assertThat(errorRootResponse.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(errorRootResponse.getError().getMessage(), equalTo(expectedMessage));
    }

    public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }


    @Test
    public void createPlaylistTest() {
        Playlist payload = payloadBuilder("Farhad's playlist", "Farhad's playlist description", false);

        Response response = PlaylistApi.post(payload);

        assertStatusCode(response.statusCode(), 201);
        assertPlaylistEqual(payload, response.as(Playlist.class));
    }

    @Test
    public void getPlaylistTest() {
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        Playlist playlistResponse = response.as(Playlist.class);

        assertThat(response.statusCode(), equalTo(200));
        assertThat(playlistResponse.getName(), equalTo("Farhad's playlist"));
        assertThat(playlistResponse.getDescription(), equalTo("Farhad&#x27;s playlist description"));
        assertThat(playlistResponse.get_public(), equalTo(true));
    }

    @Test
    public void updatePlaylistTest() {
        Playlist payload = payloadBuilder("Updated playlist Name - Farhad", "Updated playlist description - Farhad", false);

        Response response = PlaylistApi.put(DataLoader.getInstance().getUpdatePlaylistId(), payload);

        assertThat(response.statusCode(), equalTo(200));
    }

    @Test
    public void createPlaylistWithoutNameTest() {
        Playlist payload = payloadBuilder("", "Farhad's playlist description", false);

        Response response = PlaylistApi.post(payload);

        assertStatusCode(response.statusCode(), 400);
        assertErrorEqual(response.as(ErrorRoot.class), 400, "Missing required field: name");
    }

    @Test
    public void createPlaylistWithExpiredTokenTest() {
        String invalidToken = "12345";
        Playlist payload = payloadBuilder("Farhad's playlist", "Farhad's playlist description", false);

        Response response = PlaylistApi.post(payload, invalidToken);

        assertStatusCode(response.statusCode(), 400);
        assertErrorEqual(response.as(ErrorRoot.class), 400, "Only valid bearer authentication supported");
    }
}
