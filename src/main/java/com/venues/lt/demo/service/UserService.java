package com.venues.lt.demo.service;

import com.venues.lt.demo.model.User;
import com.venues.lt.demo.model.dto.UserDto;
import com.venues.lt.framework.general.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends BaseService<User> {

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserDto getUserInfo(String userId);

    List<UserDto> getUserList(int deptId, String title, String position);

    int uploadUser( MultipartFile file);

    User updateUser(User user);

    int updatePassWord(String userId, String oldPassword, String newPassword);

    int updatePhoneNum(String userId, String phone,String code);

    int updateEmail(String userId, String email,String code);

    int deleteUser( String userId);

    UserDto updateRole(String userId,Integer roleId);

    List<UserDto> selectByName(String name);

    List<String> getTitleList();

    List<String> getPositionList();
}
