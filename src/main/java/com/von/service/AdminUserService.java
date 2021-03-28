package com.von.service;

import com.von.entity.AdminUser;

public interface AdminUserService {
    AdminUser login(String userName, String password);

    /**
     * 获取用户信息
     */
    AdminUser getUserDetailById(Integer loginUserId);

    //修改当前登录用户的密码
    Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);


    //修改当前登录用户的名字
    Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

}
