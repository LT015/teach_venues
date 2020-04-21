package com.venues.lt.demo.controller;


import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.demo.service.LoginService;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.demo.util.PubUtil;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.ResponseMessage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
@Api(value = "登录",description = "登录登出api")
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @PostMapping("/login/pwd")
    @ResponseBody
    @ApiOperation(value = "登录", notes = "根据用户名和密码登录")
    public ResponseData login(@RequestParam("userId") String userId, @RequestParam("password") String password) {
        if (loginService.checkUser(userId, password)) {

            return ResponseData.success(userService.getUserInfo(userId));
        }

        return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
    }
    /**
     * 注销登录
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ResponseData logout(){

        return ResponseData.success();
    }
}
