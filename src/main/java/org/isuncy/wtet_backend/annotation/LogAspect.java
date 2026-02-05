package org.isuncy.wtet_backend.annotation;

import org.isuncy.wtet_backend.services.logger.LogServiceI;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private LogServiceI logServiceI;

    //操作日志记录
    @Pointcut("@annotation(org.isuncy.wtet_backend.annotation.OperLog)")
    public void logCut(){}

    //异常日志记录
    @Pointcut("execution(* org.isuncy.wtet_backend.controllers..*.*(..))")
    public void operExceptionLogCut() {}

    @AfterReturning(value = "logCut()",returning = "returning")
    public void operLog(JoinPoint joinPoint,Object returning)
    {
        logServiceI.logOperation(joinPoint,returning);//调用日志服务
    }

    @AfterThrowing(pointcut = "operExceptionLogCut()",throwing = "e")
    public void errorLog(JoinPoint joinPoint,Throwable e)
    {
        logServiceI.logError(joinPoint, e);//记录错误日志
    }


}
