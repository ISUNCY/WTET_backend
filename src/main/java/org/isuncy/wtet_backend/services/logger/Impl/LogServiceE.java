package org.isuncy.wtet_backend.services.logger.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.isuncy.wtet_backend.annotation.LogType;
import org.isuncy.wtet_backend.annotation.OperLog;
import org.isuncy.wtet_backend.entities.pojo.OperationLogger;
import org.isuncy.wtet_backend.mapper.logger.LogMapper;
import org.isuncy.wtet_backend.services.logger.LogServiceI;
import org.isuncy.wtet_backend.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LogServiceE implements LogServiceI {

    @Autowired
    private LogMapper logMapper;
    @Autowired
    private HttpServletRequest httpServletRequest;
//    @Autowired
//    private UserMapper userMapper;

    @Override
    public void logOperation(JoinPoint joinPoint, Object returning) {
        MethodSignature signature=(MethodSignature)joinPoint.getSignature();
        Method method=signature.getMethod();

        OperLog operLog=method.getAnnotation(OperLog.class);
        OperationLogger logger=new OperationLogger();
        if (operLog!=null)
        {
            try {
                logger.setId(IdUtil.simpleUUID());
                logger.setModule(operLog.module());
                logger.setLogType(operLog.logType());
                logger.setComment(operLog.comment());
                logger.setIpaddr(getRemoteIP(httpServletRequest));



                //日志信息配置
                basicLogger(joinPoint, httpServletRequest, method, logger);

                logger.setWebsite(logger.getRequestBody().contains("webshow") && logger.getUserId().equals("VISITOR"));

                logger.setReturning(returning.toString());

                logMapper.logOperation(logger);
            } catch (Exception e) {
                logError(joinPoint,e);
            }
        }
    }

    @Override
    public void logError(JoinPoint joinPoint, Throwable e) {
        try {
            MethodSignature signature=(MethodSignature)joinPoint.getSignature();
            Method method=signature.getMethod();

            OperationLogger logger=new OperationLogger();

            //日志错误信息配置
            basicLogger(joinPoint, httpServletRequest, method, logger);

            logger.setId(IdUtil.simpleUUID());
            logger.setModule("Error_Handler");
            logger.setLogType(LogType.ERROR);
            logger.setComment("ERROR!");
            logger.setIpaddr(getRemoteIP(httpServletRequest));

            logger.setWebsite(false);
            logger.setReturning(e.toString());

            logMapper.logOperation(logger);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //用以获取用户ip，避开代理服务器ip
    private String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private void basicLogger(JoinPoint joinPoint, HttpServletRequest httpServletRequest, Method method, OperationLogger logger) {
        Claims claims= JwtHelper.getAuthentication(httpServletRequest);
        if (claims!=null)
        {
            logger.setUserId(claims.get("id", String.class));
        }
        else
        {
            logger.setUserId("VISITOR");
        }

        logger.setClassname(joinPoint.getTarget().getClass().getName());
        logger.setMethod(method.getName());
        logger.setOpertime(LocalDateTime.now());

        String requestInfo = buildRequestInfo(joinPoint,httpServletRequest);

        logger.setRequestBody(requestInfo);

    }

    /**
     * 构建请求信息（在AOP层）
     */
    private String buildRequestInfo(JoinPoint joinPoint, HttpServletRequest request) {
        // 创建JSON对象
        Map<String, Object> jsonMap = new LinkedHashMap<>();

        try {
            // 1. 基础请求信息
            if (request != null) {
                jsonMap.put("timestamp", System.currentTimeMillis());
                jsonMap.put("httpMethod", request.getMethod());
                jsonMap.put("requestURI", request.getRequestURI());
                jsonMap.put("queryString", request.getQueryString());
                jsonMap.put("userAgent", request.getHeader("User-Agent"));
                jsonMap.put("contentType", request.getContentType());

                // 2. URL参数
                Map<String, String> params = new HashMap<>();
                Enumeration<String> paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    params.put(paramName, request.getParameter(paramName));
                }
                if (!params.isEmpty()) {
                    jsonMap.put("urlParams", params);
                }

                // 3. 请求头（主要记录重要的）
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", request.getHeader("Accept"));
                headers.put("Authorization", maskSensitiveInfo(request.getHeader("Authorization")));
                headers.put("Content-Type", request.getHeader("Content-Type"));
                headers.put("Referer", request.getHeader("Referer"));
                jsonMap.put("headers", headers);

                // 4. 尝试获取请求体
                String requestBody = getRequestBody(request);
                if (requestBody != null && !requestBody.trim().isEmpty()) {
                    // 对请求体进行敏感信息脱敏
                    String safeRequestBody = maskSensitiveInfo(requestBody);
                    jsonMap.put("requestBody", safeRequestBody);
                }
            }

            // 5. 方法参数信息
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Map<String, Object> methodArgs = new HashMap<>();
                String[] paramNames = getParameterNames(joinPoint);

                for (int i = 0; i < args.length; i++) {
                    String paramName = (paramNames != null && i < paramNames.length)
                            ? paramNames[i] : "arg" + i;

                    Object arg = args[i];
                    if (arg == null) {
                        methodArgs.put(paramName, null);
                    } else if (arg instanceof HttpServletRequest) {
                        methodArgs.put(paramName, "HttpServletRequest");
                    } else if (arg instanceof HttpServletResponse) {
                        methodArgs.put(paramName, "HttpServletResponse");
                    } else if (arg instanceof MultipartFile) {
                        methodArgs.put(paramName, "MultipartFile: " + ((MultipartFile) arg).getOriginalFilename());
                    } else if (isSimpleValueType(arg)) {
                        methodArgs.put(paramName, arg);
                    } else {
                        // 对复杂对象进行脱敏处理
                        String jsonValue = maskSensitiveInfo(JSON.toJSONString(arg));
                        methodArgs.put(paramName, jsonValue);
                    }
                }
                jsonMap.put("methodArgs", methodArgs);
            }

            // 6. 方法信息
            jsonMap.put("methodName", joinPoint.getSignature().getName());
            jsonMap.put("className", joinPoint.getTarget().getClass().getName());

        } catch (Exception e) {
            jsonMap.put("error", "构建请求信息失败: " + e.getMessage());
        }

        // 转换为JSON字符串
        return JSON.toJSONString(jsonMap);
    }

    /**
     * 获取方法参数名
     */
    private String[] getParameterNames(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            return signature.getParameterNames();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 敏感信息脱敏处理
     */
    private String maskSensitiveInfo(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 脱敏密码字段
        String result = input.replaceAll("(?i)(\"password\"\\s*:\\s*)\"[^\"]*\"", "$1\"***\"");
        result = result.replaceAll("(?i)(\"pwd\"\\s*:\\s*)\"[^\"]*\"", "$1\"***\"");
        result = result.replaceAll("(?i)(\"token\"\\s*:\\s*)\"[^\"]*\"", "$1\"***\"");
        result = result.replaceAll("(?i)(\"authorization\"\\s*:\\s*)\"[^\"]*\"", "$1\"***\"");
        result = result.replaceAll("(?i)(\"secret\"\\s*:\\s*)\"[^\"]*\"", "$1\"***\"");
        result = result.replaceAll("(?i)(\"key\"\\s*:\\s*)\"[^\"]*\"", "$1\"***\"");

        // 对JWT token进行脱敏
        if (input.contains("Bearer ")) {
            result = result.replaceAll("Bearer\\s+[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+",
                    "Bearer ***");
        }

        return result;
    }

    /**
     * 判断是否为简单类型
     */
    private boolean isSimpleValueType(Object value) {
        return (value == null ||
                value instanceof String ||
                value instanceof Number ||
                value instanceof Boolean ||
                value instanceof Character ||
                value.getClass().isPrimitive());
    }

    /**
     * 获取请求体
     */
    private String getRequestBody(HttpServletRequest request) {

        // 从属性中获取（过滤器设置的）
        Object bodyAttr = request.getAttribute("requestBody");
        if (bodyAttr != null) {
            return bodyAttr.toString();
        }

        return null;
    }


}
