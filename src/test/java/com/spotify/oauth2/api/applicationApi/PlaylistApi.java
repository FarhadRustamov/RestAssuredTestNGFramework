package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;


public class PlaylistApi {

    private static final String accessToken = "Bearer BQCDtGuMo8qc6QJIlJ5noP0hwdzuz4TkKMWuVU9fb6eN4R_dKgXhkyXEqnAUi5Sr3rwQv5u-aFPwaTPQRFc3T-VpUyE4WDxyO8AwGAgzLb-rBQtP_8cvupifsFwTVeHIHp01h34bCn0K5QzAycjhmycOlsZQLvNqNgllU66vhOJ3WkLq2a1sL4u0bd03HQxwI2U8TuztKpKGrMSW7MJr6lYSpFqiRXGAqv03hL9XxasX8RDg4j5n8f0kE9FkCEWmUXviqhSLF_Pp";

    public static Response post(Playlist playlistRequest) {
        return RestResource.post("users/pr2jfo79b4fe7ylho07cocy9g/playlists", accessToken, playlistRequest);
    }

    public static Response post(Playlist playlistRequest, String accessToken) {
        return RestResource.post("users/pr2jfo79b4fe7ylho07cocy9g/playlists", accessToken, playlistRequest);
    }

    public static Response get(String playlistId) {
        return RestResource.get("/playlists/" + playlistId, accessToken);
    }

    public static Response put(String playlistId, Playlist playlistRequest) {
        return RestResource.put("/playlists/" + playlistId, accessToken, playlistRequest);
    }
}
