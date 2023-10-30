package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.PermissionEmp;

/**
* @author HJsir
* @description 针对表【permission_emp】的数据库操作Service
* @createDate 2023-05-16 15:01:39
*/
public interface PermissionEmpService extends IService<PermissionEmp> {


    boolean findPerms(Integer thisPerm, Integer empId);

    void deleteInfo(Integer id);

    void deleteByEmpId(Integer id);
}
