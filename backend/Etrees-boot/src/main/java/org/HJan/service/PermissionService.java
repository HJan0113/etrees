package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.Permission;

/**
* @author HJsir
* @description 针对表【permission(权限列表)】的数据库操作Service
* @createDate 2023-05-16 15:01:39
*/
public interface PermissionService extends IService<Permission> {

    Integer getIdList(Integer permId);

    Integer getPermByMenuId(Integer thisMenuId);

}
