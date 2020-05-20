package com.webmaster.end.config;

import com.webmaster.end.Utils.JwtUtil;
import com.webmaster.end.Utils.LoginAccess;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.print.DocFlavor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

public class MyInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Credentials","true");
        HandlerMethod method=(HandlerMethod)handler;
        LoginAccess methodAnnotation = method.getMethodAnnotation(LoginAccess.class);
        if(methodAnnotation==null||!(methodAnnotation.value()))
            return true;
        else{
                Enumeration enumeration=request.getHeaderNames();
                while(enumeration.hasMoreElements()) {
                    String name=(String)enumeration.nextElement();
                    if("token".equals(name)) {
                        String value=request.getHeader(name);
                        Map<String, Object> map = JwtUtil.decode(value);
                        if((boolean)map.get("isSuccess"))
                            return true;
                    }
            }
            PrintWriter writer = response.getWriter();
            writer.print("{\"result\":0}");
            return false;
        }
    }
}
