package com.pccw.backend.aop;

import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.exception.BaseException;
import com.pccw.backend.util.Session;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-02 14:52
 **/
@Aspect
@Component
@Slf4j
public class CheckExternalAspect {

    @Autowired
    Session session;

    @Autowired
    HttpServletRequest request;

    @Pointcut("execution(* com.pccw.backend.ctrl.api_external.*.*(..))")
    public void pointcut() {}

    @Before("pointcut()")
    public void printParam(JoinPoint joinPoint) throws BaseException {


//        String token = session.getToken();
//        log.info(token);

        //TODO Basic Authentication加密验证

        String auth = request.getHeader("Authorization");
        log.info(auth);
        log.info("auth encoded in base64 is " + getFromBASE64(auth));
        if ((auth != null) && (auth.length() > 6)) {
            auth = auth.substring(6, auth.length());

            String decodedAuth = getFromBASE64(auth);
            log.info("auth decoded from base64 is " + decodedAuth);

            request.getSession().setAttribute("auth", decodedAuth);

        }else{
            throw BaseException.getNoRightException();
        }


    }

    private String getFromBASE64(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }


}
