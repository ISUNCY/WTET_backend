package org.isuncy.wtet_backend.services.logger;

import org.aspectj.lang.JoinPoint;

public interface LogServiceI {
    void logOperation(JoinPoint joinPoint, Object returning);

    void logError(JoinPoint joinPoint, Throwable e);
}
