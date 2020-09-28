package com.pccw.backend.bean;


import com.pccw.backend.exception.BaseException;
import com.pccw.backend.exception.ExceptionLog;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;


/**
 * JsonResult is customer specifiaction of json of all api to return to client
 */
@Data
@AllArgsConstructor
public class JsonResult<T> {

    private String state;

    private String code;

    private String msg;

    private List<T> data;

    /**
     * quick method to return a JsonResult when SUCESSED
     * @param <T>
     * @param data
     * @return
     */
    public static <T> JsonResult<T> success(List<T> data) {
        return new JsonResult<T>("success", "000","", data);
    }

    public static <T> JsonResult<T> success(List<T> data,long totalSize) {
        return new JsonResult<T>("success", "000",totalSize+"", data);
    }
    /**
     * quick method to return a JsonResult when FAILED
     * @return
     */
    public static <T> JsonResult<T> fail(Exception e){
        BaseException baseException = BaseException.getRuntimeException();
        baseException.setMsg(e.getMessage());
        try{
            ExceptionLog.exceptionLogHandle(baseException);
            return new JsonResult<T>("failed", baseException.getCode(),baseException.getMsg(), Arrays.asList());
        }catch (Exception e1){
            return new JsonResult<T>("failed", "888",e1.getMessage(), Arrays.asList());
        }
    }
    /**
     * quick method to return a JsonResult when FAILED
     * @return
     */
    public static <T> JsonResult<T> fail(BaseException e){
        try {
            ExceptionLog.exceptionLogHandle(e);
            return new JsonResult<T>("failed", e.getCode(),e.getMsg(), Arrays.asList());
        }catch (Exception e1){
            return new JsonResult<T>("failed", "888",e1.getMessage(), Arrays.asList());
        }

    }

    /**
     * quick method to return a JsonResult when FAILED
     * @return
     */
    public static <T> JsonResult<T> fail(String error){
        try {
            return new JsonResult<T>("failed","888", error, Arrays.asList());
        }catch (Exception e1){
            return new JsonResult<T>("failed", "888",e1.getMessage(), Arrays.asList());
        }

    }

}
