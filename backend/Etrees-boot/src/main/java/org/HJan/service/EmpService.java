package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.Emp;

import java.util.List;


public interface EmpService extends IService<Emp> {



    void delete(Integer id);

    Emp myGetById(Integer id);

    boolean check(Emp emp);

    String getEmpPassword(Integer empId);

    boolean isRegister(String phone);

    Emp getEmp(String empName);

    Emp getEmpByPhone(String phone);

    Emp getByUsername(String username);

    String getUserAuthorityInfo(Integer empId);

    void deleteInfo(List<String> ids);
}
