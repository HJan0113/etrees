package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.DataTips;
import org.HJan.mapper.DataTipsMapper;
import org.HJan.service.DataTipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HJsir
* @description 针对表【data_tips(健康提示)】的数据库操作Service实现
* @createDate 2023-05-24 15:53:45
*/
@Service
public class DataTipsServiceImpl extends ServiceImpl<DataTipsMapper, DataTips>
    implements DataTipsService{

    @Autowired
    private DataTipsMapper dataTipsMapper;

    @Override
    public List<DataTips> getAll() {
        return dataTipsMapper.getAll();
    }

    @Override
    public List<DataTips> getTips() {
        return dataTipsMapper.getTips();
    }
}




