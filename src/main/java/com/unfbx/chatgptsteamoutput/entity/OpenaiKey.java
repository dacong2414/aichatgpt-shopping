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
public class OpenaiKey implements Serializable {

    private static final long serialVersionUID = 1L;
/*  `id` bigint(20) NOT NULL,
  `openai_key` varchar(200) CHARACTER SET latin1 NOT NULL,
  `del_flag` tinyint(4) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,*/
    private Long id;
    private String openaikey;
    private Integer delFlag;
    private Date createTime;
    private Date updateTime;
}
