package com.venues.lt.demo.controller;


import com.venues.lt.demo.model.User;
import com.venues.lt.demo.model.dto.RoomDto;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    @GetMapping("/list/name")
    @ApiOperation(value = "根据名字查询", notes = "根据名字查询")
    public ResponseData userInfoList(@RequestParam("name") String name){
        List<UserDto> userDtos = userService.selectByName(name);
        if(userDtos!=null){
            return ResponseData.success(userDtos);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }

    }

    @ResponseBody
    @GetMapping("/getlist")
    @ApiOperation(value = "获取用户列表", notes = "根据筛选条件获取所有的用户信息  先选部门再显示用户")
    public ResponseData getUserList(@RequestParam(value = "deptId")  int deptId,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "position", required = false) String position){
        return ResponseData.success(userService.getUserList(deptId, title, position));
    }

    @ResponseBody
    @GetMapping("/getTitle")
    @ApiOperation(value = "获取职称列表", notes = "获取所有的用户职称")
    public ResponseData getTitleList(){
        return ResponseData.success(userService.getTitleList());
    }

    @ResponseBody
    @GetMapping("/getPosition")
    @ApiOperation(value = "获取职位列表", notes = "获取所有的用户职位")
    public ResponseData getPositionList(){
        return ResponseData.success(userService.getPositionList());
    }


    @ResponseBody
    @PostMapping("/update/image")
    @ApiIgnore
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
    public ResponseData updateUser(@RequestBody User user) {
        if(userService.updateUser(user) != null){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }
    }

    @ResponseBody
    @PostMapping("/update/password")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    public ResponseData updatePassWord(@RequestParam(value = "userId") String userId,
                                       @RequestParam(value = "oldPassword") String oldPassword,
                                       @RequestParam(value = "newPassword") String newPassword) {
        int result = userService.updatePassWord(userId,oldPassword, newPassword);
        if(result != 0){
            return ResponseData.success();
        }else if( result == 1){
            return ResponseData.fail(ResponseCode.FAIL, "原密码错误");
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }
    }

    @ResponseBody
    @PostMapping("/update/phoneNum")
    @ApiOperation(value = "修改手机号", notes = "修改手机号")
    public ResponseData updatePhoneNum(@RequestParam(value = "userId") String userId,
                                       @RequestParam(value = "phoneNum") String phoneNum,
                                       @RequestParam(value = "code") String code) {
        int result = userService.updatePhoneNum(userId,phoneNum,code);
        if(result == 0){
            return ResponseData.success("修改成功，请登录");
        }else if(result == 1){
            return ResponseData.fail(ResponseCode.FAIL, "验证码已过期");
        }else if(result == 2){
            return ResponseData.fail(ResponseCode.FAIL, "修改失败，验证码输入有误");
        }
        return null;
    }

    @ResponseBody
    @PostMapping("/update/email")
    @ApiOperation(value = "修改邮箱", notes = "修改邮箱")
    public ResponseData updateEmail(@RequestParam(value = "userId") String userId,
                                    @RequestParam(value = "email") String email,
                                    @RequestParam(value = "code") String code) {
        int result = userService.updateEmail(userId,email,code);
        if(result == 0){
            return ResponseData.success("邮箱修改成功");
        }else if(result == 1){
            return ResponseData.fail(ResponseCode.FAIL, "验证码已过期");
        }else if(result == 2){
            return ResponseData.fail(ResponseCode.FAIL, "修改失败，验证码输入有误");
        }
        return null;
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
    @ResponseBody
    @DeleteMapping("/delete/{userId:.+}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public ResponseData deleteUser(@PathVariable String userId) {
        int result = userService.deleteUser(userId);
        if(result != 0){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }

    }
}
