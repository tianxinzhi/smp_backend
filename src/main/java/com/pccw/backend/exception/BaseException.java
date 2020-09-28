package com.pccw.backend.exception;

import lombok.Data;

/**
 * BaseException
 */

@Data
public class BaseException extends RuntimeException {

    public static final String EXCEPTION_CODE_RUNTIMEEXCEPTION = "888";
    public static final String EXCEPTION_CODE_ACCANDPWDERROREXCEPTION = "003";
    public static final String EXCEPTION_CODE_NOLOGINEXCEPTION = "002";
    public static final String EXCEPTION_CODE_NORIGHTEXCEPTION = "001";
    public static final String EXCEPTION_CODE_ARGUMENTNOTVALIDEXCEPTION = "004";
    public static final String EXCEPTION_CODE_DATAUSEDEXCEPTION = "005";//数据被使用
    public static final String EXCEPTION_CODE_REDISEXCEPTION = "006";//redis异常

    private String code;

    // private Exception exception;

    private String msg;

    public static BaseException getException(String code){
        BaseException e = new BaseException();
        e.setCode(code);
        return e;
    }
    // public static BaseException getException(String code,Exception exception){
    //     BaseException e = new BaseException();
    //     e.setCode(code);
    //     e.setException(exception);
    //     return e;
    // }
    public static BaseException getException(String code, String msg){
        BaseException e = new BaseException();
        e.setCode(code);
        e.setMsg(msg);
        return e;
    }
    public static BaseException getRuntimeException() {
        return BaseException.getException(BaseException.EXCEPTION_CODE_RUNTIMEEXCEPTION);
    }

    public static BaseException getAccAndPwdException() {
        return BaseException.getException(BaseException.EXCEPTION_CODE_ACCANDPWDERROREXCEPTION);
    }
    public static BaseException getArgumentNotValidException() {
        return BaseException.getException(BaseException.EXCEPTION_CODE_ARGUMENTNOTVALIDEXCEPTION);
    }
    public static BaseException getNoLoginException() {
        return BaseException.getException(BaseException.EXCEPTION_CODE_NOLOGINEXCEPTION);
    }
    public static BaseException getNoRightException() {
        return BaseException.getException(BaseException.EXCEPTION_CODE_NORIGHTEXCEPTION," Authentication failed ");
    }
    public static BaseException getDataUsedException(long id) {
        return BaseException.getException(BaseException.EXCEPTION_CODE_DATAUSEDEXCEPTION,id+" is used,can not disable!");
    }
    public static BaseException getRedisException() {
        return BaseException.getException(BaseException.EXCEPTION_CODE_REDISEXCEPTION," Redis operation failed");
    }
}
