package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.PermissionEmp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HJsir
 * @description 针对表【permission_emp】的数据库操作Mapper
 * @createDate 2023-05-16 15:01:39
 * @Entity org.HJan.entity.PermissionEmp
 */
@Mapper
public interface PermissionEmpMapper extends BaseMapper<PermissionEmp> {


    @Select("select * from permission_emp where permission_id=#{thisPerm} and emp_id = #{empId}")
    PermissionEmp findPerms(Integer thisPerm, Integer empId);


    @Select("SELECT * FROM permission_emp WHERE emp_id = #{id}")
    List<PermissionEmp> getPerms(Integer id);

    @Delete("DELETE FROM permission_emp where emp_id = #{id}")
    void deleteByEmpId(Integer id);
}




