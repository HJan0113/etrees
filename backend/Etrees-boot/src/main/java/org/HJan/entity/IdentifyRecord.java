package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 识别记录
 * @TableName identify_record
 */
@TableName(value ="identify_record")
@Data
public class IdentifyRecord implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 病种信息
     */
    @TableField(value = "name")
    private String name;

    /**
     * 识别设备
     */
    @TableField(value = "equipment")
    private String equipment;

    /**
     * 
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 识别图片
     */
    @TableField(value = "img_url")
    private String imgUrl;

}