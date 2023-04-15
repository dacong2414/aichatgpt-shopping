package com.unfbx.chatgptsteamoutput.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: yangsong
 * @Date: 2022/12/27 13:58
 **/
@Data
@TableName("login")
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    /*id` bigint(20) NOT NULL,
      `password` varchar(100) DEFAULT NULL,
      `login_name` varchar(100) DEFAULT NULL,
      `nick_name` varchar(100) DEFAULT NULL,
      `key` varchar(150) DEFAULT NULL,
      `create_time` datetime DEFAULT NULL,
                `create_by` varchar(100) DEFAULT NULL,
      `update_time` datetime DEFAULT NULL,
                `update_by` varchar(255) DEFAULT NULL,
      `del_flag` tinyint(4) DEFAULT '0' COMMENT '0.正常 1.删除',
                `from_login_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '介绍人',
                `enable_time` bigint(10) DEFAULT '0' COMMENT '剩余时长（小时）',
                `total_time` bigint(10) DEFAULT '0' COMMENT '这个用户的总时长',*/
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
