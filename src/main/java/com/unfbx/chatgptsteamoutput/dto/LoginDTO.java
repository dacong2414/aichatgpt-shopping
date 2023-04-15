package com.unfbx.chatgptsteamoutput.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginDTO implements Serializable {
    private Long id;
    private String passwd;
    private String loginName;
    private String nickName;
    private String shoppingKey;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private Integer delFlag;
    private String fromLoginName;
    private Integer enableTime;
    private Integer totalTime;
}
