package cn.chper.vvdo.server.controller;

import cn.chper.vvdo.server.config.OssConfig;
import cn.chper.vvdo.server.entity.Video;
import cn.chper.vvdo.server.form.SimpleResponse;
import cn.chper.vvdo.server.mapper.VideoMapper;
import cn.chper.vvdo.server.util.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    OssConfig ossConfig;

    @GetMapping("/my")
    public SimpleResponse getMyVideos(@RequestParam String token) {
        String username = UserController.tokens.get(token);
        if (username == null) {
            return SimpleResponse.fail();
        }
        List<Video> videos = videoMapper.selectByNickname(username);
        Collections.reverse(videos);
        return SimpleResponse.success().put("videos", videos);
    }

    @GetMapping("/list")
    public SimpleResponse getVideoList() {
        List<Video> videos = videoMapper.selectAll();
        Collections.reverse(videos);
        return SimpleResponse.success().put("videos", videos);
    }

    @GetMapping("/like")
    public SimpleResponse getLikecount(@RequestParam int id) {
        Video video = videoMapper.selectById(id);
        if (video == null) {
            return SimpleResponse.fail();
        }
        return SimpleResponse.success().put("likecount", video.getLikecount());
    }

    @PostMapping("/like")
    public SimpleResponse likeVideo(@RequestParam int id) {
        videoMapper.updateLikeById(id);
        Video video = videoMapper.selectById(id);
        if (video == null) {
            return SimpleResponse.fail();
        }
        return SimpleResponse.success().put("likecount", video.getLikecount());
    }

    @PostMapping("/upload")
    public SimpleResponse upload(@RequestParam("video") MultipartFile file, @RequestParam String token, @RequestParam String description) {
        String username = UserController.tokens.get(token.replaceAll("\"", ""));
        if (username == null) {
            return SimpleResponse.fail();
        }
        String url = AliOssUtil.upload(file, "video", ossConfig);
        if (url == null) {
            return SimpleResponse.fail();
        }
        Video video = new Video();
        video.setFeedurl(url);
        video.setDescription(description.substring(1, description.length() - 1));
        video.setAvatar("https://vvdo.oss-cn-hangzhou.aliyuncs.com/image/p.png");
        video.setNickname(username);
        video.setLikecount(0);
        videoMapper.insert(video);
        return SimpleResponse.success();
    }

}
