package com.von.controller.admin;

import com.von.entity.AdminUser;
import com.von.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;

    @GetMapping("/login")
    public String login(){
        //http://localhost:8080/admin/login.html
        return "admin/login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session){
        //判断验证码，测试阶段先注释掉
/*        //判断参数是否为空
        if(StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg", "用户名或密码不能为空");
        }
        //判断验证码是否正确,先取出sesssion中正确的验证码
        String kaptchCode = session.getAttribute("verifyCode") + "";
        if(StringUtils.isEmpty(kaptchCode) || !verifyCode.equals(kaptchCode)){
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }*/

        AdminUser adminUser = adminUserService.login(userName, password);
        if(adminUser != null){
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            ////session过期时间设置为一天
            session.setMaxInactiveInterval(60 * 60 * 24);
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg", "登录失败");
            return "admin/login";
        }
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(){
        return "admin/index";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
        if(adminUser == null){
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    //修改密码
    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request,
                                 @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword){
        if (org.springframework.util.StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(loginUserId, originalPassword, newPassword)){
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        }else {
            return "修改失败";
        }
    }


    //修改用户名，原理同上
    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
























