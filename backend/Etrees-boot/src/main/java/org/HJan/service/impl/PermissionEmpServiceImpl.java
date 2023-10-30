package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.HJan.entity.PermissionEmp;
import org.HJan.mapper.PermissionEmpMapper;
import org.HJan.service.PermissionEmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author HJsir
* @description 针对表【permission_emp】的数据库操作Service实现
* @createDate 2023-05-16 15:01:39
*/
@Service
@Slf4j
public class PermissionEmpServiceImpl extends ServiceImpl<PermissionEmpMapper, PermissionEmp>
    implements PermissionEmpService{

    @Autowired
    private PermissionEmpMapper permissionEmpMapper;


    @Override
    public boolean findPerms(Integer thisPerm,Integer empId) {
        if(permissionEmpMapper.findPerms(thisPerm,empId)==null){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteInfo(Integer id) {
        // 若当前用户有权限，就删除
        List<PermissionEmp> permissionEmpList = permissionEmpMapper.getPerms(id);
        if(!permissionEmpList.isEmpty()){
           for(PermissionEmp permissionEmp:permissionEmpList) {
               permissionEmpMapper.deleteById(permissionEmp);
           }
        }
    }

    @Override
    public void deleteByEmpId(Integer id) {
        permissionEmpMapper.deleteByEmpId(id);
    }
}




