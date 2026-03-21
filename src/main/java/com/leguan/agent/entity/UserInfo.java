package com.leguan.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_info")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户id
    @TableField(value = "user_id")
    private Long userId;

    // 用户名称
    @TableField(value = "user_name")
    private String userName;

    // 创建时间
    @TableField(value = "create_time")
    private Date createTime;

    // 更新时间
    @TableField(value = "update_time")
    private Date updateTime;

    // 逻辑删除:0=未删,1=删除
    @TableField(value = "is_delete")
    private Integer isDelete;
}
