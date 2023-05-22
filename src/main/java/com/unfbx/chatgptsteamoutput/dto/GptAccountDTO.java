package com.unfbx.chatgptsteamoutput.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yangsong
 * @Date: 2022/12/27 13:58
 **/
@Data
public class GptAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /*`id` bigint(20) NOT NULL,
  `account_name` varchar(100) NOT NULL,
  `account_pwd` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `email_pwd` varchar(100) DEFAULT NULL,
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0正常  1.删除',
  `send_lag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0.未发送  1.已发送  指的是否发送给支付1美元的用户',
  `create_time` datetime NOT NULL,
  `create_by` varchar(100) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `account_type` varchar(100) NOT NULL DEFAULT '3.5' COMMENT '3.5 或者4.0',
  `sales_flag` tinyint(4) DEFAULT '0' COMMENT '0.未出售  1.已出售  这个是4.0的标识是否已经出售',*/
    private Long id;
    private String accountName;
    private String accountPwd;
    private String email;
    private String emailPwd;
    private Integer delFlag;
    private Integer sendFlag;
    private String accountType;
    private Integer salesFlag;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private String token;

}
