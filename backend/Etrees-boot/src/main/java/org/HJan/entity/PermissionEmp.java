package org.HJan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName permission_emp
 */
@TableName(value ="permission_emp")
@Data
public class PermissionEmp implements Serializable {
    /**
     * 
     */
    @TableId(value = "emp_id")
    private Integer empId;

    private Integer permissionId;

}