package com.venues.lt.demo.controller;


import com.venues.lt.demo.model.User;
import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
import com.venues.lt.framework.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/venues/user")
@Api(value = "我的User层",description = "用户操作api")
public class UserController {

    @Value("${image.save.path}")
    private String imageSavePath;

    @Autowired
    UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/get")
    @ApiOperation(value = "获取一个用户", notes = "根据用户id获取一个用户")
    @ApiResponses({
            @ApiResponse(code=100,message = "请求参数有错"),
            @ApiResponse(code=101,message = "未授权"),
            @ApiResponse(code=103,message = "禁止访问"),
            @ApiResponse(code=104,message = "请求路径不存在"),
            @ApiResponse(code=105,message = "服务器内部错误"),
    })
    public UserDto getUserById(@RequestParam(value = "userId", required = false) String userId){
        UserDto userDto = userService.getUserInfo(userId);
        return userDto;
    }

    @ResponseBody
    @GetMapping("/getlist")
    @ApiOperation(value = "获取用户列表", notes = "获取所有的用户信息")
    public List<UserDto> getUserList(){
        return userService.getUserList();
    }

    @GetMapping("/getRedis")
    @ResponseBody
    public String getRedisTest(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("first","hellow word");
        redisTemplate.boundSetOps("nameset").add("曹操");
        redisTemplate.boundSetOps("nameset").add("刘备");
        redisTemplate.boundSetOps("nameset").add("孙权");
        Set members = redisTemplate.boundSetOps("nameset").members();
        return valueOperations.get("first").toString();
    }

    @ResponseBody
    @PostMapping("/update/image")
    public String updateImage(@RequestParam MultipartFile file){

        String originalFilename = file.getOriginalFilename(); //file.getOriginalFilename()是得到上传时的文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //String fileName = stuId + suffix;
        File newFile = new File(imageSavePath + suffix);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @PutMapping(value = "/list")
    @ApiOperation(value = "添加用户文件", notes = "添加用户文件")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) {
        int result = userService.uploadUser(file);
        if(result == 1){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.LOGIN_FAIL);
        }
    }

    @ResponseBody
    @PostMapping("/update")
    @ApiOperation(value = "更新用户基本信息", notes = "更新用户基本信息 不包含角色")
    public ResponseData updateBuilding(@RequestBody User user) {
        if(userService.updateUser(user) != null){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }
    }

    @ResponseBody
    @PostMapping("/update/role/{userId:.+}/{roleId:.+}")
    @ApiOperation(value = "修改用户角色/权限", notes = "修改用户角色/权限  1-4是角色 5是设为管理员 7是取消授权管理员")
    public ResponseData updateDescription(@PathVariable String userId, @PathVariable int roleId) {
        UserDto userDto = userService.updateRole(userId, roleId);
        if(userDto != null){
            return ResponseData.success(userDto);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }

    }

}
