package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);

    List<User> SelectAllUsers();
}
