package com.zhw.util;

import java.io.Serializable;
import java.util.Map;

public class DataVO implements Serializable {
    private boolean success;
    private Object data;
    private String msg;
    private Map<String, Object> map;

    DataVO() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public DataVO(Object data) {
        this.success = true;
        this.data = data;
        this.msg = "操作成功";
    }

    public DataVO(Object data, String message) {
        this.success = true;
        this.data = data;
        this.msg = message;
    }


    public void setFailMsg(String msg) {
        this.msg = msg;
        this.success = false;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
