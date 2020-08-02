package cn.chper.vvedo.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {

    @GET("video/list")
    Call<SimpleResponse> getVideos();

    @GET("video/like")
    Call<SimpleResponse> getVideoLikes(@Query("id") int vid);

    @FormUrlEncoded
    @POST("video/like")
    Call<SimpleResponse> likeVideo(@Field("id") int vid);

    @Multipart
    @POST("video/upload")
    Call<SimpleResponse> uploadVideo(@PartMap Map<String, RequestBody> data);

    @POST("user/login")
    Call<SimpleResponse> login(@Body LoginForm loginForm);

    @GET("video/my")
    Call<SimpleResponse> getMyVideos();

    class LoginForm {
        @SerializedName("username")
        String username;

        @SerializedName("password")
        String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public LoginForm(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

}
