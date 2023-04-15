package com.unfbx.chatgptsteamoutput.config;

import lombok.Data;


@Data
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
    }

    public ServiceException(ErrorCodeEnum errorCodeEnum, String message) {
        super(message);
        this.code = errorCodeEnum.getCode();
    }

    public ServiceException(Result result) {
        super(result.getMessage());
        this.code = result.getCode();
    }


    @Override
    public String toString() {
        return this.getCode()+"【"+getMessage()+"】";
    }

}
