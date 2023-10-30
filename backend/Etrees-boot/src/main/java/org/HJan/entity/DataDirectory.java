package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图鉴目录
 * @TableName data_directory
 */
@TableName(value ="data_directory")
@Data
public class DataDirectory implements Serializable {
    /**
     * 目录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 目录名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 目录图标
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 版本号
     */
    @Version
    private Timestamp version;

}