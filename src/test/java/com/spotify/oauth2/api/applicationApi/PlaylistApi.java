package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.api.Route;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;


public class PlaylistApi {

    public static Response post(Playlist playlistRequest) {
        return RestResource.post(Route.USERS + "/pr2jfo79b4fe7ylho07cocy9g" + Route.PLAYLISTS, TokenManager.getToken(), playlistRequest);
    }

    public static Response post(Playlist playlistRequest, String accessToken) {
        return RestResource.post(Route.USERS + "/pr2jfo79b4fe7ylho07cocy9g" + Route.PLAYLISTS, accessToken, playlistRequest);
    }

    public static Response get(String playlistId) {
        return RestResource.get(Route.PLAYLISTS + "/" + playlistId, TokenManager.getToken());
    }

    public static Response put(String playlistId, Playlist playlistRequest) {
        return RestResource.put(Route.PLAYLISTS + "/" + playlistId, TokenManager.getToken(), playlistRequest);
    }
}
