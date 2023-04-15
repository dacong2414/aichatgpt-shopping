package com.unfbx.chatgptsteamoutput.dto;
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
public class OrderInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
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
