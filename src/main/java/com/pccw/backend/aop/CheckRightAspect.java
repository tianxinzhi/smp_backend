package com.pccw.backend.aop;

import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.exception.BaseException;
import com.pccw.backend.util.Session;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


/**
 * LastUpdatedBy: KEN
 * LastUpdatedAt: 5/12/2019
 * Desc: 验证是否用户是否拥有此权限
 */
@Aspect
@Component
@Slf4j
public class CheckRightAspect {

    @Autowired
    Session session;

    //execution表达式自行搜索引擎
    @Pointcut("execution(* com.pccw.backend.ctrl.*.*(..))  && !@target(com.pccw.backend.annotation.NoAuthorized) && !@annotation(com.pccw.backend.annotation.NoAuthorized)")
    public void pointcut() {}

    @Before("pointcut()&&@annotation(requestMapping)")
    public void printParam(JoinPoint joinPoint, RequestMapping requestMapping) throws BaseException {
        //获取请求的方法
        Signature sig = joinPoint.getSignature();
        String method = joinPoint.getTarget().getClass().getName() + "." + sig.getName();

        //获取请求的参数
        Object[] args = joinPoint.getArgs();
        //fastjson转换
        String params = JSONObject.toJSONString(args);

        //打印请求参数
        log.info(method + ":" + params);
        // 【1】获取session关于account相关right
        Map<String,Map<String, List<String>>> map = (Map<String,Map<String, List<String>>>)session.getUser();
        Map<String, List<String>> rightMap = map.get("right");
//        // 【2】获取ctrl名称（模块名称）和ctrl方法的路由（模块按钮）
        String className = joinPoint.getTarget().getClass().getName();
        String[] split = className.split("\\.");
        String module = split[split.length - 1];
        //获取权限标识
        String[] value = requestMapping.value();
        String[] path = requestMapping.path();
        String identifier = path.length > 0 ? path[0] : value[0];
//        // 【3】判断right
        if (rightMap.containsKey(module)){
            List<String> buttons = rightMap.get(module);
            if(buttons.size()>0 && !buttons.contains(identifier)){
                throw BaseException.getNoRightException();
            }
        }else {
            throw BaseException.getNoRightException();
        }
        // 【4】抛异常
        // throw BaseException.getNoLoginException();

    }

    /**
     * 方法执行时间
     * @return
     *//*
    @Around("pointcut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint){
        long beginTime = System.currentTimeMillis();

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String loggerType = className + "." + methodName;

        Object result = null;
        try {
            //执行目标方法
            result = joinPoint.proceed();
//            System.out.println("【返回通知】：the method 【" + loggerType + "】 ends with " + result);
        } catch (Throwable e) {
            System.out.println("【异常通知】：the method 【" + loggerType + "】 occurs exception " + e);
        }

        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        System.out.println("【后置通知】：the method 【" + loggerType + "】-----------------end.----------------- time:" + time+"ms");
        return result;
    }*/
}
