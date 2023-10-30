package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户反馈表
 * @TableName maintain_feedback
 */
@Data
@TableName(value ="maintain_feedback")
public class MaintainFeedback implements Serializable {
    /**
     * 反馈表自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 反馈用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 主题
     */
    @TableField(value = "theme")
    private String theme;

    /**
     * 反馈内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 反馈时间
     */
    @TableField(value = "create_time")
    private Date createTime;

}