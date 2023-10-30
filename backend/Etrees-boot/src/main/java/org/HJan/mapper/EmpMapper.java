package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.Emp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 员工管理Mapper
 */
@Mapper
public interface EmpMapper extends BaseMapper<Emp> {


    @Delete("delete from emp where id=#{empId}")
    void deleteInfo(@Param("empId") Integer empId);

    @Select("select * from emp where id=#{id}")
    Emp getById(@Param("id") Integer id);

    @Select("select password from emp where id = #{empId}")
    String getPassword(@Param("empId") Integer empId);
}
