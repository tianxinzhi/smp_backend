package com.pccw.backend.annotation;



/**
 * PredicateType
 */

public enum PredicateType{
    EQUEL(0, "="),
    LIKE(1,"%?%"),
    BETWEEN(2,"[?,?]"),
    GREATER_THAN(3,">"),
    LESS_THAN(4,"<"),
    GREATERTHAN_OR_EQUEL(5,">="),
    LESSTHAN_OR_EQUEL(6,"<="),
    IN(7,"(?,?)")
    ;
    private int code;
    private String message;

     PredicateType(int code,String message) {
         this.code = code;
         this.message = message;
    }
    public int getCode(){
        return code;
    }
    public static PredicateType getByValue(int code) {
        for (PredicateType predicateType : values()) {
            if (predicateType.getCode() == code) {
                return predicateType;
            }
        }
        return null;
    }
}
