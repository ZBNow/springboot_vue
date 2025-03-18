package com.zbn.exception;

/**
自定义异常

 */
public class CustomerException extends RuntimeException {
    private String code;
    private String msg;
    private Object data;

    public CustomerException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CustomerException(String msg){
        this.code = "500";
        this.msg = msg;
    }
    public CustomerException(Object data){
        this.data = data;

    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
