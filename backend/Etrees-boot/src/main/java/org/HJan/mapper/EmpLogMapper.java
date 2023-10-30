package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.EmpLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HJsir
* @description 针对表【emp_log(员工操作日志)】的数据库操作Mapper
* @createDate 2023-05-30 16:14:17
* @Entity org.HJan.entity.EmpLog
*/
@Mapper
public interface EmpLogMapper extends BaseMapper<EmpLog> {

    @Delete("DELETE FROM emp_log WHERE id NOT IN " +
            "(SELECT id from (SELECT id FROM emp_log ORDER BY create_time DESC LIMIT 100 ) as tbl)")
    void deleteLog();
}




