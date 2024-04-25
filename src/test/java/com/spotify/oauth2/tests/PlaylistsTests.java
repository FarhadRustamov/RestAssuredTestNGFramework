package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import com.spotify.oauth2.utils.FakerUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Epichik")
public class PlaylistsTests {

    public Playlist payloadBuilder(String name, String description, boolean _public) {
        return Playlist.builder().name(name).description(description)._public(_public).build();
    }

    @Step()
    public void assertPlaylistEqual(Playlist payload, Playlist playlistResponse) {
        assertThat(playlistResponse.getName(), equalTo(payload.getName()));
        assertThat(playlistResponse.getDescription(), equalTo(payload.getDescription()));
        assertThat(playlistResponse.get_public(), equalTo(payload.get_public()));
    }

    @Step
    public void assertErrorEqual(ErrorRoot errorRootResponse, StatusCode statusCode) {
        assertThat(errorRootResponse.getError().getStatus(), equalTo(statusCode.getCode()));
        assertThat(errorRootResponse.getError().getMessage(), equalTo(statusCode.getMsg()));
    }

    @Step("assertStatusCode: {actualStatusCode} and {expectedStatusCode}")
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode) {
        assertThat(actualStatusCode, equalTo(statusCode.getCode()));
    }

    @Story("Storichik")
    @Feature("Featurichik")
    @Link(name = "nameOfLink", url = "yandex.com")
    @Issue(value = "1111")
    @TmsLink(value = "2222")
    @Description(value = "This is createPlaylistTest's description")
    @Severity(SeverityLevel.CRITICAL)
    @Owner(value = "Farhad Rustamov")
    @Test(description = "Farhad")
    public void createPlaylistTest() {
        Playlist payload = payloadBuilder(FakerUtils.generateName(), FakerUtils.generateDescription(), false);

        Response response = PlaylistApi.post(payload);

        assertStatusCode(response.statusCode(), StatusCode.CODE_201);
        assertPlaylistEqual(payload, response.as(Playlist.class));
    }

    @Description("This is getPlaylistTest's description")
    @Test
    public void getPlaylistTest() {
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        Playlist playlistResponse = response.as(Playlist.class);

        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
        assertThat(playlistResponse.getName(), equalTo("Farhad's playlist"));
        assertThat(playlistResponse.getDescription(), equalTo("Farhad&#x27;s playlist description"));
        assertThat(playlistResponse.get_public(), equalTo(true));
    }

    @Description("This is updatePlaylistTest's description")
    @Test
    public void updatePlaylistTest() {
        Playlist payload = payloadBuilder(FakerUtils.generateName(), FakerUtils.generateDescription(), false);

        Response response = PlaylistApi.put(DataLoader.getInstance().getUpdatePlaylistId(), payload);

        assertStatusCode(response.statusCode(), StatusCode.CODE_200);
    }

    @Description("This is createPlaylistWithoutNameTest's description")
    @Test
    public void createPlaylistWithoutNameTest() {
        Playlist payload = payloadBuilder("", FakerUtils.generateDescription(), false);

        Response response = PlaylistApi.post(payload);

        assertStatusCode(response.statusCode(), StatusCode.CODE_400);
        assertErrorEqual(response.as(ErrorRoot.class), StatusCode.CODE_400);
    }

    @Description("This is createPlaylistWithExpiredTokenTest's description")
    @Test
    public void createPlaylistWithExpiredTokenTest() {
        String invalidToken = "12345";
        Playlist payload = payloadBuilder(FakerUtils.generateName(), FakerUtils.generateDescription(), false);

        Response response = PlaylistApi.post(payload, invalidToken);

        assertStatusCode(response.statusCode(), StatusCode.CODE_401);
        assertErrorEqual(response.as(ErrorRoot.class), StatusCode.CODE_401);
    }
}
