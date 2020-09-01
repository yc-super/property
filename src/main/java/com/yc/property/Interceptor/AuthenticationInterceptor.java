package com.yc.property.Interceptor;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.yc.property.annotation.PassToken;
import com.yc.property.annotation.UserLoginToken;
import com.yc.property.util.TokenUtil;
import com.yc.property.Bean.Owner;
import com.yc.property.Dao.OwnerDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 拦截器，拦截token
 * @author
 * @date 2019/02/18
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    OwnerDao ownerDao;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object object){
        //设置允许哪些域名应用进行ajax访问
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, content-Type, Accept, Authorization");
        httpServletResponse.setHeader("Access-Control-Max-Age","3600");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out=null;
        //获取请求头的token
        String token=httpServletRequest.getHeader("Authorization");
        System.out.println("interceptor,token:"+token);
        //如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod) object;
        Method method=handlerMethod.getMethod();
        //检查是否有passToken注释，有则跳过验证
        if(method.isAnnotationPresent(PassToken.class)){
            System.out.println("有passToken注解！");
            PassToken passToken=method.getAnnotation(PassToken.class);
            if(passToken.required()){
                return true;
            }
        }
        //检查是否有需要用户权限的注解
//        if(method.isAnnotationPresent(UserLoginToken.class)){
            System.out.println("需要用户权限");
            UserLoginToken userLoginToken=method.getAnnotation(UserLoginToken.class);
//            if(userLoginToken.required()){
                //执行认证
                if(StrUtil.isNullOrUndefined(token)||token.isEmpty()){
                    try{
                        System.out.println("进入try!");
                        JSONObject res=new JSONObject();
                        res.put("state",-2);
                        out=httpServletResponse.getWriter();
                        out.append(res.toString());
                        return false;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
//                        throw new RuntimeException("无token，请重新登录");
                    /*认证失败：
                    *{
                    "timestamp": "2020-03-18T13:41:51.625+0000",
                    "status": 500,
                    "error": "Internal Server Error",
                    "message": "无token，请重新登录",
                    "path": "/ApplyProperty/getAllType"
                }
                    *
                    * */
                }
//                else {
//                    //获取token中的用户信息
//                    Claims claims;
//                    try{
//                        claims= TokenUtil.parseJWT(token);
//
//                    }catch (ExpiredJwtException e){
//                        throw new RuntimeException("401,token失效");
//                    }
//                    String phone=claims.getId();
//                    Owner owner=ownerDao.getOwnerByPhone(phone);
//                    if(owner==null){
//                        throw new RuntimeException("用户不存在，请重新登录");
//                    }
//                    httpServletRequest.setAttribute("CurrentUser",owner);
//                }
//            }
//        }
        return true;
    }
    // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e)throws Exception{

    }
}