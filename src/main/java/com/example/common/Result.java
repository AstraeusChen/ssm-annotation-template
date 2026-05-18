package com.example.common;

import lombok.Getter;

/**
 * 【企业规范】统一响应结构，所有 REST 接口返回此格式。
 */
@Getter
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private String traceId;  // 用于全链路追踪

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.traceId = TraceContext.getTraceId();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}