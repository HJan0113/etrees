package org.HJan.common.security;


import cn.hutool.json.JSONUtil;
import org.HJan.common.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");//响应数据格式
        ServletOutputStream outputStream = response.getOutputStream();
        //认证失败
        String msg = exception.getMessage();//得到报错信息
        if(exception instanceof BadCredentialsException){
            msg = "用户名或密码报错";
        }

        outputStream.write(JSONUtil.toJsonStr(Result.error(msg)).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
