package com.venues.lt.demo.service.impl;

import com.venues.lt.demo.mapper.UserMapper;
import com.venues.lt.demo.model.User;
import com.venues.lt.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public boolean checkUser(String userId, String password) {
        User user = userMapper.selectByKey(userId);
        String userPassword = user.getPassword();
        return password.equals(userPassword);
    }
}
