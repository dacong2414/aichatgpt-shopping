package com.unfbx.chatgptsteamoutput.dto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @Author: yangsong
 * @Date: 2022/12/27 13:58
 **/
@Data
public class OpenaiKeyDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String openaikey;
    private Integer delFlag;
    private Date createTime;
    private Date updateTime;
}
