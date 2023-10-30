package org.HJan.dto;

import lombok.Data;
import org.HJan.entity.BaseEntity;

import java.io.Serializable;

@Data
public class UserDto extends BaseEntity implements Serializable {
    private String userName;
//    private Integer id;
    private Integer gender;
    private String address;
//    private LocalDateTime registerTime;
    private Integer isRealname;
    private String phone;
    private Integer age;
    private String imgUrl;
}
