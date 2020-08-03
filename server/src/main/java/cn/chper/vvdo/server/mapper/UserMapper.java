package cn.chper.vvdo.server.mapper;

import cn.chper.vvdo.server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User selectByUsername(String username);

    int insert(String username, String password);

}
