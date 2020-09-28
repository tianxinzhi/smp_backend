package com.pccw.backend.exception;

import com.pccw.backend.entity.DbResLogException;
import com.pccw.backend.repository.ResExceptionLogRepository;
import com.pccw.backend.util.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Author: LIUJIE
 * Date: 31/12/2019
 * Desc: 异常日志处理
 */
@Component
public class ExceptionLog{

    @Autowired
    private ResExceptionLogRepository relr;
    private static ResExceptionLogRepository resExceptionLogRepo;

    @Autowired
    private Session ss;
    private static Session<Map> session;

    @Autowired
    private HttpServletRequest rq;
    private static  HttpServletRequest request;

    @PostConstruct
    public void init(){
        resExceptionLogRepo = this.relr;
        session = this.ss;
        request = this.rq;
    }

    /**
     * 将异常日志插入日志表中
     * @param e
     */
    public static void exceptionLogHandle(BaseException e){
            Map user = session.getUser();
            Long accountId = null;
            if(Objects.nonNull(user)){
                accountId = Long.parseLong(user.get("account").toString());
            }
            DbResLogException dbResLogException = new DbResLogException(null,e.getCode(),e.getMsg(),request.getRequestURI(),new Date().getTime(),accountId);
            resExceptionLogRepo.saveAndFlush(dbResLogException);
    }

}
