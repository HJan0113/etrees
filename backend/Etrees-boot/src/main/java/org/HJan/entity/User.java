package org.HJan.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
public class User extends BaseEntity implements Serializable {

    private String userName;
    private String password;
    private String phone;
    private Integer age;
    private Integer isRealname;//实名状态  ’0‘：未实名  ‘1’：已实名
    private Integer status;
    private Integer gender;
    private String address;
    private String imgUrl;//用户头像


}
