package com.bjpowernode.crm.settings.service.Impl;

import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        User user = userMapper.SelectUserByLoginActAndPwd(map);
        return user;
    }

    @Override
    public List<User> SelectAllUsers() {
        List<User> userList= userMapper.SelectAllUsers();
        return userList;
    }
}
