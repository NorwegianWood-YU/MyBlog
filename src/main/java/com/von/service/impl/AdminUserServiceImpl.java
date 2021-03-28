package com.von.service.impl;

import com.von.dao.AdminUserMapper;
import com.von.entity.AdminUser;
import com.von.service.AdminUserService;
import com.von.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
//业务层
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, passwordMd5);
    }

    @Override
    public AdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }


    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser =adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空
        if(adminUser != null){
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            //比较原密码是否正确
            logger.info("adminUser.getLoginPassword ==" + adminUser.getLoginPassword());
            logger.info("originalPasswordMd5 ==" + originalPasswordMd5);

            if(originalPasswordMd5.equals(adminUser.getLoginPassword())){
                //设置新密码并修改
                adminUser.setLoginPassword(newPasswordMd5);
                if(adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0){
                    //修改成功
                    return true;
                }
            }
        }
        return false;
    }

    //修改名字，和上面同理
    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {

        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if(adminUser != null){
            //设置新密码
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if(adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0){
                return  true;
            }
        }
        return null;
    }
}
