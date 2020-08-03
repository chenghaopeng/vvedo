package cn.chper.vvdo.server.form;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class SimpleResponse {

    private int code;

    private JSONObject data;

    private SimpleResponse(int code) {
        this.code = code;
        this.data = new JSONObject();
    }

    private SimpleResponse(int code, JSONObject data) {
        this.code = code;
        this.data = data;
    }

    public static SimpleResponse success() {
        return new SimpleResponse(0);
    }

    public static SimpleResponse success(JSONObject data) {
        return new SimpleResponse(0, data);
    }

    public static SimpleResponse fail() {
        return new SimpleResponse(-1);
    }

    public static SimpleResponse fail(int code) {
        return new SimpleResponse(code);
    }

    public static SimpleResponse fail(JSONObject data) {
        return new SimpleResponse(-1, data);
    }

    public static SimpleResponse fail(int code, JSONObject data) {
        return new SimpleResponse(code, data);
    }

    public static SimpleResponse unauthorized() {
        return new SimpleResponse(254);
    }

    public static SimpleResponse notLoggedIn() {
        return new SimpleResponse(255);
    }

    public SimpleResponse put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
