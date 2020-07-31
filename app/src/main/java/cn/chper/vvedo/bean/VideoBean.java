package cn.chper.vvedo.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoBean implements Serializable {

    @SerializedName("id")
    Integer id;

    @SerializedName("feedurl")
    String feedurl;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("description")
    String description;

    @SerializedName("likecount")
    Integer likecount;

    @SerializedName("avatar")
    String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeedurl() {
        return feedurl;
    }

    public void setFeedurl(String feedurl) {
        this.feedurl = feedurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLikecount() {
        return likecount;
    }

    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
