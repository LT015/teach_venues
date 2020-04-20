package com.venues.lt.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.venues.lt.demo.mapper.UserMapper;
import com.venues.lt.demo.model.Department;
import com.venues.lt.demo.model.User;
import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.demo.service.DepartmentService;
import com.venues.lt.demo.service.UserService;
import com.venues.lt.framework.general.service.BaseServiceImpl;
import com.venues.lt.framework.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    DepartmentService departmentService;

    @Override
    //@Cacheable(value = "user", key = "'user:'+#userId", unless = "#result==null")
    public UserDto getUserInfo(String userId) {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        UserDto userDto;
//        if(valueOperations.get("first"+userId)!= null){
//            String user = valueOperations.get("first"+userId).toString();
//            userDto = JSON.parseObject(user,UserDto.class);
//        }else{
//            User user = this.selectByKey(userId);
//            valueOperations.set("first"+userId,JSONObject.toJSON(userDto));
//        }
        User user = userMapper.selectByKey(userId);
        return handleUser(user);

    }

    @Override
    public List<UserDto> getUserList() {
        List<User> userList = userMapper.selectAll();

        return userList.stream().map(this::handleUser).collect(Collectors.toList());
    }

    public int uploadUser( MultipartFile file) {
        String name = file.getOriginalFilename();
        if(name.length() < 6 || !name.substring(name.length()-5).equals(".xlsx")){
            List<Object> li = new ArrayList<>();
            li.add("文件格式错误");
            return 0;
        }
        List<List<String>> list = null;
        try {
            list = ExcelUtil.excelToObjectList(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list != null){
            parseUser(list);
        }

        return 1;
    }

    public UserDto handleUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUserName(user.getUserName());
        userDto.setIdNumber(user.getIdNumber());
        userDto.setSex(user.getSex());
        userDto.setDeptId(user.getDeptId());
        userDto.setUserTitle(user.getUserTitle());
        userDto.setPosition(user.getPosition());
        userDto.setPhone(user.getPhone());
        userDto.setEmail(user.getEmail());
        userDto.setWechat(user.getWechat());
        userDto.setStatus(user.getStatus());
        return userDto;
    }

    public void parseUser(List<List<String>> list){
        List<Integer> indexList = new ArrayList<>(14);
        List<String> titleList = list.get(0);//标题栏
        titleList.forEach(title ->{
            if(title.equals("职工号")){
                indexList.set(0,titleList.indexOf(title));
            }else if(title.equals("用户姓名")){
                indexList.set(1,titleList.indexOf(title));
            }else if(title.equals("身份证号")){
                indexList.set(3,titleList.indexOf(title));
            }else if(title.equals("性别")){
                indexList.set(4,titleList.indexOf(title));
            }else if(title.equals("部门")){
                indexList.set(5,titleList.indexOf(title));
            }else if(title.equals("职称")){
                indexList.set(6,titleList.indexOf(title));
            }else if(title.equals("职务")){
                indexList.set(7,titleList.indexOf(title));
            }else if(title.equals("手机号")){
                indexList.set(8,titleList.indexOf(title));
            }else if(title.equals("邮箱")){
                indexList.set(9,titleList.indexOf(title));
            }else if(title.equals("微信")){
                indexList.set(10,titleList.indexOf(title));
            }else if(title.equals("状态")){
                indexList.set(11,titleList.indexOf(title));
            }
        });
        List<User> userList = new ArrayList<>();
        for(int i = 1;i < list.size();i++){
            User user = new User();
            List<String> stringList = list.get(i);
            user.setUserId(stringList.get(indexList.get(0)));
            user.setUserName(stringList.get(indexList.get(1)));
            user.setPassword(stringList.get(indexList.get(3)).substring(12));
            user.setIdNumber(stringList.get(indexList.get(3)));
            user.setSex(stringList.get(indexList.get(4)));
            if(!stringList.get(indexList.get(5)).equals("")){
                Department department =  departmentService.creatQuery()
                        .andEqualTo("dept_name",stringList.get(indexList.get(5)))
                        .list().get(0);
                user.setDeptId(indexList.get(5));
            }
            user.setUserTitle(stringList.get(indexList.get(6)));
            user.setPosition(stringList.get(indexList.get(7)));
            user.setPhone(stringList.get(indexList.get(8)));
            user.setEmail(stringList.get(indexList.get(9)));
            user.setWechat(stringList.get(indexList.get(10)));
            user.setStatus(indexList.get(11));
            userList.add(user);
        }
        saveUser(userList);

    }

    @Async
    public void saveUser(List<User> list){
        this.insertList(list);
    }
}
