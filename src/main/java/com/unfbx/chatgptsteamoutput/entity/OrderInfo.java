package com.unfbx.chatgptsteamoutput.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Author: yangsong
 * @Date: 2022/12/27 13:58
 **/
@Data
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;
/*  `id` bigint(20) NOT NULL,
  `order_code` varchar(100) DEFAULT NULL,
  `login_name` varchar(100) DEFAULT NULL,
  `amount` decimal(20,2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `del_flag` tinyint(4) DEFAULT NULL,,*/
    private Long id;
    private String orderCode;
    private String loginName;
    private BigDecimal amount;
    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;
    private Integer delFlag;

}
