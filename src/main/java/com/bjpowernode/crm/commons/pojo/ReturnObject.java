package com.bjpowernode.crm.commons.pojo;

public class ReturnObject {
    private String code;
    private String message;
    private Object retData;//其他数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    public String getMessage() {
        return message;
    }

    public Object getRetData() {
        return retData;
    }
}
