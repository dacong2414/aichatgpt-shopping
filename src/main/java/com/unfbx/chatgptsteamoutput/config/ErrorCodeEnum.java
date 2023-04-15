package com.unfbx.chatgptsteamoutput.config;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
    SUCCESS("200", "success"),
    ERROR("500", "error")
    ;

    private String code;
    private String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
