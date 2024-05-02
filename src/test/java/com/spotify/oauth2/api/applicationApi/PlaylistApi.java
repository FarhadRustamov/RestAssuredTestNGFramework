package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.GeneralMethods;
import com.spotify.oauth2.api.Route;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class PlaylistApi {

    @Step("post = {payload}")
    public static Response post(Playlist payload) {
        return GeneralMethods.post(Route.USERS + "/" + ConfigLoader.getInstance().getUserId() + Route.PLAYLISTS, TokenManager.getToken(), payload);
    }

    @Step("post = {payload}, {accessToken}")
    public static Response post(Playlist payload, String accessToken) {
        return GeneralMethods.post(Route.USERS + "/" + ConfigLoader.getInstance().getUserId() + Route.PLAYLISTS, accessToken, payload);
    }

    @Step("get = {playlistId}")
    public static Response get(String playlistId) {
        return GeneralMethods.get(Route.PLAYLISTS + "/" + playlistId, TokenManager.getToken());
    }

    @Step("put = {playlistId}, {payload}")
    public static Response put(String playlistId, Playlist payload) {
        return GeneralMethods.put(Route.PLAYLISTS + "/" + playlistId, TokenManager.getToken(), payload);
    }
}
