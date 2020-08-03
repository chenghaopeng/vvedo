package cn.chper.vvdo.server.entity;

import lombok.Data;

@Data
public class Video {

    int id;

    String feedurl;

    String nickname;

    String avatar;

    String description;

    int likecount;

}
