package com.yc.property.Config;

import com.yc.property.Interceptor.LoginInteceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        InterceptorRegistration registration=registry.addInterceptor(new LoginInteceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns(
          "/Owner/ownerLogin",
          "/*",
          "/**/*.html",
           "/**/*.js",
            "/**/*.css"
        );
    }
}
