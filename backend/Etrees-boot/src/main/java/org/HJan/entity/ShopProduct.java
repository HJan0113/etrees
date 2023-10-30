package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 商品信息表
 * @TableName product
 */
@TableName(value ="shop_product")
@Data
public class ShopProduct implements Serializable {
    /**
     * 商品编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 目录编号
     */
    @TableField(value = "dir_id")
    private Integer dirId;

    /**
     * 目录编号
     */
    @TableField(value = "dir_name")
    private String dirName;

    /**
     * 商品名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商品介绍
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 价格,两位小数
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 状态 在售：1  /  禁售：0
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 商品图片存储地址
     */
    @TableField(value = "img_url")
    private String imgUrl;

    /**
     * 商品推荐
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 商品推荐
     */
    @TableField(value = "push_key")
    private String pushKey;

    /**
     * 版本号
     */
    @Version
    private Timestamp version;
}