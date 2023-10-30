package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.EmpLog;

/**
* @author HJsir
* @description 针对表【emp_log(员工操作日志)】的数据库操作Service
* @createDate 2023-05-30 16:14:17
*/
public interface EmpLogService extends IService<EmpLog> {

    void delete();
}
