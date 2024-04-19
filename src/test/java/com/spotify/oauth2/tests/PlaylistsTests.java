package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTests {

    @Test
    public void createPlaylistTest() {
        Playlist playlistRequest = new Playlist().setName("Farhad's playlist")
                .setDescription("Farhad's playlist description")
                .setPublic(false);

        Response response = PlaylistApi.post(playlistRequest);
        Playlist playlistResponse = response.as(Playlist.class);

        assertThat(response.statusCode(), equalTo(201));
        assertThat(playlistResponse.getName(), equalTo(playlistRequest.getName()));
        assertThat(playlistResponse.getDescription(), equalTo(playlistRequest.getDescription()));
        assertThat(playlistResponse.getPublic(), equalTo(playlistRequest.getPublic()));
    }

    @Test
    public void getPlaylistTest() {
        Response response = PlaylistApi.get("0YBkdwfzXqI0JKPDmwFWIv");
        Playlist playlistResponse = response.as(Playlist.class);

        assertThat(response.statusCode(), equalTo(200));
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

        Response response = PlaylistApi.put("7CKg8CGgG0G9cD9xeWkCRg", playlistRequest);

        assertThat(response.statusCode(), equalTo(200));
    }

    @Test
    public void createPlaylistWithoutNameTest() {
        Playlist playlistRequest = new Playlist()
                .setName("")
                .setDescription("Farhad's playlist description")
                .setPublic(false);

        Response response = PlaylistApi.post(playlistRequest);
        ErrorRoot errorRootResponse = response.as(ErrorRoot.class);

        assertThat(response.statusCode(), equalTo(400));
        assertThat(errorRootResponse.getError().getStatus(), equalTo(400));
        assertThat(errorRootResponse.getError().getMessage(), equalTo("Missing required field: name"));
    }

    @Test
    public void createPlaylistWithExpiredTokenTest() {
        String invalidToken = "12345";
        Playlist playlistRequest = new Playlist()
                .setName("Farhad's playlist")
                .setDescription("Farhad's playlist description")
                .setPublic(false);

        Response response = PlaylistApi.post(playlistRequest, invalidToken);
        ErrorRoot errorRootResponse = response.as(ErrorRoot.class);

        assertThat(response.statusCode(), equalTo(400));
        assertThat(errorRootResponse.getError().getStatus(), equalTo(400));
        assertThat(errorRootResponse.getError().getMessage(), equalTo("Only valid bearer authentication supported"));
    }
}
