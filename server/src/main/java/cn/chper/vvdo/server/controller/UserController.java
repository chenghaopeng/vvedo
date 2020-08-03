package cn.chper.vvdo.server.controller;

import cn.chper.vvdo.server.entity.User;
import cn.chper.vvdo.server.form.LoginForm;
import cn.chper.vvdo.server.form.SimpleResponse;
import cn.chper.vvdo.server.mapper.UserMapper;
import cn.chper.vvdo.server.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final HashMap<String, String> tokens = new HashMap<>();

    @Autowired
    UserMapper userMapper;

    @PostMapping("/login")
    public SimpleResponse login(@RequestBody LoginForm loginForm) {
        if (loginForm.getUsername() == null || loginForm.getPassword() == null || loginForm.getUsername().isEmpty() || loginForm.getPassword().isEmpty()) {
            return SimpleResponse.fail();
        }
        User user = userMapper.selectByUsername(loginForm.getUsername());
        if (user == null) {
            userMapper.insert(loginForm.getUsername(), loginForm.getPassword());
        }
        else if (!user.getPassword().equals(loginForm.getPassword())) {
            return SimpleResponse.fail();
        }
        for (String token : tokens.keySet()) {
            if (tokens.get(token).equals(loginForm.getUsername())) {
                tokens.remove(token);
                break;
            }
        }
        String token = Utils.getRandomString();
        tokens.put(token, loginForm.getUsername());
        return SimpleResponse.success().put("token", token);
    }

}
