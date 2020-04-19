package com.venues.lt.demo.controller;


import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.demo.service.LoginService;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.demo.util.PubUtil;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
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
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @PostMapping("/login/pwd")
    @ResponseBody
    public ResponseData login(HttpServletRequest request,HttpServletResponse response,@RequestParam("userId") String userId, @RequestParam("password") String password) {
        if (loginService.checkUser(userId, password)) {

            return ResponseData.success(  userService.getUserInfo(userId));
        }

        return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
    }
    /**
     * 注销登录
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpSession session){

        return "logout";
    }
}
