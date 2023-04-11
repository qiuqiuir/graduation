package com.cslg.graduation.filter.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:没有访问权限
 * @author: maxiao1
 * @date: 2019/9/13 17:41
 */
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Map<String,String> map=new HashMap<>();
        map.put("message","没有访问权限");
        map.put("status","406");
        //String reason = "统一处理，原因：" + "没有访问权限";//e.getMessage();
        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(map));
    }
}
