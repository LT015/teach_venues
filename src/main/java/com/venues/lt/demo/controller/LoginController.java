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
@RequestMapping("/venues")
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
    public ResponseData login(HttpServletResponse response, @RequestParam("userId") String userId, @RequestParam("password") String password) {
        if (loginService.checkUser(userId, password)) {
            String sessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("TJUFE-SESSION-ID", sessionId);
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
            stringRedisTemplate.opsForValue().set(sessionId, userId, 7, TimeUnit.DAYS);
            return ResponseData.success(userService.getUserInfo(userId));
        }
        return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
    }

    /**
     * 登出
     *
     * @param request  用于获取cookie
     * @param response 用于写回信息和删除cookie
     */
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = PubUtil.getSessionId(request);
        String stuId = stringRedisTemplate.opsForValue().get(sessionId);
        if (StringUtils.isEmpty(stuId)) {
            PubUtil.responseUnAuthorizedError(response);
            return;
        }
        Cookie cookie = new Cookie("TJUFE-SESSION-ID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        stringRedisTemplate.delete(sessionId);
    }

    /**
     * 确认是否登录
     *
     * @param request  用于获取cookie
     * @param response 用于写回信息
     * @return 用户是否处于登录状态
     */
    @GetMapping("/checkLogin")
    public ResponseData checkLogin(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = PubUtil.getSessionId(request);
        String stuId = stringRedisTemplate.opsForValue().get(sessionId);
        if (StringUtils.isEmpty(stuId)) {
            PubUtil.responseUnAuthorizedError(response);
            return null;
        }

        return ResponseData.success("已登录");
    }
}
