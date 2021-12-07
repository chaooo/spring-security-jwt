package com.example.jwt.entity;

import lombok.Data;
import java.io.Serializable;

/**
 * 全局API返回JSON
 *
 * @author : Charles
 * @date : 2021/12/2
 */
@Data
public class ResponseJson<T> implements Serializable {
    /** 自定义状态码 */
    private int code;
    /** 提示信息 */
    private String msg;
    /** 返回数据 */
    private T data;

    private ResponseJson(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseJson<Void> success() {
        return new ResponseJson<>(0, "操作成功", null);
    }

    public static<T> ResponseJson<T> success(T data) {
        return new ResponseJson<>(0, "操作成功", data);
    }

    public static<T> ResponseJson<T> success(String msg, T data) {
        return new ResponseJson<>(0, msg, data);
    }

    public static<T> ResponseJson<T> success(int code, String msg, T data) {
        return new ResponseJson<>(code, msg, data);
    }

    public static ResponseJson<Void> error() {
        return new ResponseJson<>(-1, "操作失败", null);
    }

    public static ResponseJson<Void> error(String msg) {
        return new ResponseJson<>(-1, msg, null);
    }

    public static ResponseJson<Void> error(int code, String msg) {
        return new ResponseJson<>(code, msg, null);
    }

    public static<T> ResponseJson<T> error(String msg, T data) {
        return new ResponseJson<>(-1, msg, data);
    }

    public static<T> ResponseJson<T> error(int code, String msg, T data) {
        return new ResponseJson<>(code, msg, data);
    }

    @Override
    public String toString() {
        return "ResponseJson{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }

    private static final long serialVersionUID = 1L;
}
