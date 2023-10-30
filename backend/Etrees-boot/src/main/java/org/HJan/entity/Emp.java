package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 员工实体类
 */
@Data
@TableName(value ="emp")
public class Emp extends BaseEntity implements Serializable {

    private String empName;

    private String name;

    private String password;

    private String phone;

    private Integer status;

    @Version
    private Timestamp version;
}
