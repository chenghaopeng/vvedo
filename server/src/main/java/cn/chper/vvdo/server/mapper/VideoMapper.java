package cn.chper.vvdo.server.mapper;

import cn.chper.vvdo.server.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VideoMapper {

    List<Video> selectByNickname(String nickname);

    List<Video> selectAll();

    Video selectById(Integer id);

    void updateLikeById(Integer id);

    int insert(Video video);

}
