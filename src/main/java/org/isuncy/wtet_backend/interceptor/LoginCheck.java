package org.isuncy.wtet_backend.interceptor;

import ch.qos.logback.core.model.processor.AppenderRefModelHandler;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.isuncy.wtet_backend.entities.statics.Result;
import org.isuncy.wtet_backend.services.logger.LogServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Component
public class LoginCheck implements HandlerInterceptor {
    @Autowired
    private LogServiceI logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if (url.startsWith("/api/")){
            return userCheck(url, request, response, handler);
        }
        return true;
    }

    private boolean userCheck(String url, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        log.info("url:{}", url);
        if (url.contains("/login") || url.contains("/register") || url.contains("doc.html")) {
            return true;
        }
        String jwt = request.getHeader("Authentication");
        if (!StringUtils.hasLength(jwt)) {
            String notLogin = JSONObject.toJSONString(new Result<>().error("NOT_LOGIN"));
            response.getWriter().write(notLogin);
            return false;
        }
        try {
            Claims claims = JwtHelper.parseJWT(jwt);
            if (claims == null) {
                String notLogin = JSONObject.toJSONString(new Result<>().error("NOT_LOGIN"));
                response.getWriter().write(notLogin);
                return false;
            }
        } catch (Exception e) {
            String notLogin = JSONObject.toJSONString(new Result<>().error("NOT_LOGIN"));
            response.getWriter().write(notLogin);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
