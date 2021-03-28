package com.von.config;

import com.von.interceptor.AdminLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MyBlogWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/admin为前缀的url路径
        //登陆页面以及部分静态资源文件也是以 /admin 开头，所以需要将这些路径排除
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/admin/**").
                excludePathPatterns("/admin/login").
                excludePathPatterns("/admin/dist/**").
                excludePathPatterns("/admin/plugins/**");
    }

    //文件的上传后存储的路径和拦截路径都是 **/home/project/upload,这个路径可以根据实际情况进行修改
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:/home/project/upload/");
    }
}
