package com.yc.property.Config;


import com.yc.property.annotation.CurrentUser;
import com.yc.property.Bean.Owner;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 自定义参数解析器（解析user）
 * @author
 * @date 2019/02/18
 * 增加方法注入，将含有 @CurrentUser 注解的方法参数注入当前登录用户
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.getParameterType().isAssignableFrom(Owner.class) //判断是否能转换成User类型
                && parameter.hasParameterAnnotation(CurrentUser.class); //是否有CurrentUser注解
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Owner owner = (Owner) webRequest.getAttribute("CurrentUser", RequestAttributes.SCOPE_REQUEST);
        if (owner != null) {
            return owner;
        }
        throw new MissingServletRequestPartException("CurrentUser");
    }
}