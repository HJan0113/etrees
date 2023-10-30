package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图鉴信息
 * @TableName data_info
 */
@TableName(value ="data_info")
@Data
public class DataInfo implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属目录
     */
    @TableField(value = "dir_id")
    private Integer dirId;

    /**
     * 所属目录
     */
    @TableField(value = "dir_name")
    private String dirName;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 概述
     */
    @TableField(value = "overview")
    private String overview;

    /**
     * 病发季节
     */
    @TableField(value = "season")
    private String season;

    /**
     * 病发原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 治疗方案
     */
    @TableField(value = "treatment")
    private String treatment;

    /**
     * 常见：1  /  默认：0
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 图片
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 版本号
     */
    @Version
    private Timestamp version;
}