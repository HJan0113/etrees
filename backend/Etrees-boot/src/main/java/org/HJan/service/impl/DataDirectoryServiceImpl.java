package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.DataDirectory;
import org.HJan.mapper.DataDirectoryMapper;
import org.HJan.service.DataDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HJsir
* @description 针对表【data_directory(图鉴目录)】的数据库操作Service实现
* @createDate 2023-05-24 15:53:45
*/
@Service
public class DataDirectoryServiceImpl extends ServiceImpl<DataDirectoryMapper, DataDirectory>
    implements DataDirectoryService{

    @Autowired
    private DataDirectoryMapper dataDirectoryMapper;

    @Override
    public List<DataDirectory> getAll() {
        return dataDirectoryMapper.getAll();
    }

    @Override
    public List<DataDirectory> getDir() {
        return dataDirectoryMapper.getDir();
    }
}




