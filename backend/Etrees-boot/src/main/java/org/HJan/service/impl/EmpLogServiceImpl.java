package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.EmpLog;
import org.HJan.mapper.EmpLogMapper;
import org.HJan.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author HJsir
* @description 针对表【emp_log(员工操作日志)】的数据库操作Service实现
* @createDate 2023-05-30 16:14:17
*/
@Service
public class EmpLogServiceImpl extends ServiceImpl<EmpLogMapper, EmpLog>
    implements EmpLogService{

    @Autowired
    private EmpLogMapper empLogMapper;

    @Override
    public void delete() {
        empLogMapper.deleteLog();
    }
}




