package org.isuncy.wtet_backend.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {
    String module() default "";
    LogType logType() default LogType.NONE;
    String comment() default "";
}