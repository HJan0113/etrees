package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品目录
 * @TableName shop_directory
 */
@TableName(value ="shop_directory")
@Data
public class ShopDirectory implements Serializable {
    /**
     * 商品目录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 目录名称
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