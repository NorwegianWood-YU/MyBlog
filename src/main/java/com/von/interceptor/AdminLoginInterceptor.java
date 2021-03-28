package com.von.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    /*在请求的预处理过程中读取当前 session 中是否存在 loginUser 对象，
    如果不存在则返回 false 并跳转至登录页面，如果已经存在则返回 true，继续做后续处理流程。
   */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if(uri.startsWith("/admin") && request.getSession().getAttribute("loginUser") == null){
            request.getSession().setAttribute("errorMsg", "请先登录");
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return  false;
        }else{
            request.getSession().removeAttribute("errorMsg");
            return  true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
