package com.pccw.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResultParamMapAnnotation {
    String param1() default "";
    String param2() default "";
    String param3() default "";
    String param4() default "";
}
