package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @TableName menu
 */
@TableName(value ="menu")
@Data
public class Menu implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Integer id;

    /**
     *
     */
    @TableField(value = "name")
    private String name;/**

     /**
     *
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 
     */
    @TableField(value = "path")
    private String path;

    /**
     * 
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 
     */
    @TableField(value = "pid")
    private Integer pid;

    /**
     * 
     */
    @TableField(value = "icon")
    private String icon;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
}