package cn.chper.vvedo.api;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class SimpleResponse {

    @SerializedName("code")
    public int code;

    @SerializedName("data")
    public HashMap<String, Object> data;

}
