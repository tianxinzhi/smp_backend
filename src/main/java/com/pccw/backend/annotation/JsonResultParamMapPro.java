package com.pccw.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  JsonResultParamMapAnnotation的增强版，可以随意的扩展字段
 *  作用在实体上，将实体字段的值映射到与其字段名字不相同的Map的指定key上
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResultParamMapPro {
    /**
     *
     * @return 指定当前类字段与要映射的Map的key
     * 格式：{目标字段名1=当前类字段名1，目标字段名2=当前类字段名2}
     */
    String[] fieldMapping();

    /**
     * 目标字段：要映射的类的字段名。注意：sourceField要与targetField一一对应
     * @return
     */
//    String[] targetField();

    /**
     * 目标类：要映射的类
     * @return
     */
//    Class<?> targetClass() default Void.class;

    /**
     * 源字段顺序是否与目标类字段顺序相同。
     * 注意：默认不相同，如设置为相同，则sourceField字段顺序要与目标类需要映射的字段名顺序保持一致，
     * 此时不需要设置targetField属性
     * @return
     */
//    boolean order() default false;
}
