package com.unfbx.chatgptsteamoutput.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    /**
     * 返回消息体
     */
    private T data;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误描述
     */
    private String message;

    private Result(T data) {
        this.data = data;
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        this.message = ErrorCodeEnum.SUCCESS.getMessage();
    }

    private Result() {
        this.code = ErrorCodeEnum.SUCCESS.getCode();
        this.message = ErrorCodeEnum.SUCCESS.getMessage();
    }

    private Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private Result(ErrorCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public static Result fail(ErrorCodeEnum resultCodeEnum) {
        return new Result(resultCodeEnum);
    }

    public static Result fail(ServiceException serviceException) {
        return new Result(serviceException.getCode(), serviceException.getMessage());
    }

    public static Result fail(String code, String message) {
        return new Result(code, message);
    }

    public static Result fail(ErrorCodeEnum errorCodeEnum, String message) {
        return new Result(errorCodeEnum.getCode(), message);
    }

    public static Result success(Object data) {
        return new Result(data);
    }

    public static Result success() {
        return new Result();
    }

    public Result<T> check() {
        if (!ErrorCodeEnum.SUCCESS.getCode().equals(this.code)) {
            throw new ServiceException(this);
        }
        return this;
    }

    /**
     * 判断结果是否成功
     *
     * @return true成功 false
     */
    public boolean isSuccess() {
        return ErrorCodeEnum.SUCCESS.getCode().equals(this.code);
    }

    public void checkDataEmpty(ErrorCodeEnum errorCodeEnum) {
        check();

        if (this.data == null) {
            throw new ServiceException(errorCodeEnum);
        }
    }
}
