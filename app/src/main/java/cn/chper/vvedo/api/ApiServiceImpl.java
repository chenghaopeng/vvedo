package cn.chper.vvedo.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceImpl {

    public static final ApiServiceImpl instance = new ApiServiceImpl();

    public ApiService api;

    public String token;

    public String username;

    ApiServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ali.chper.cn:7947/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.api = retrofit.create(ApiService.class);
    }

}
