package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.Permission;
import org.HJan.mapper.PermissionMapper;
import org.HJan.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author HJsir
* @description 针对表【permission(权限列表)】的数据库操作Service实现
* @createDate 2023-05-16 15:01:39
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Integer getIdList(Integer permId) {
        return permissionMapper.getMenuIdByPermId(permId);
    }

    @Override
    public Integer getPermByMenuId(Integer thisMenuId) {
        return permissionMapper.getPermIdByMenuId(thisMenuId);
    }



}




