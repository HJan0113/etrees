package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.DataInfo;
import org.HJan.mapper.DataInfoMapper;
import org.HJan.service.DataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
* @author HJsir
* @description 针对表【data_info(图鉴信息)】的数据库操作Service实现
* @createDate 2023-05-24 15:53:45
*/
@Service
public class DataInfoServiceImpl extends ServiceImpl<DataInfoMapper, DataInfo>
    implements DataInfoService{

    @Autowired
    private DataInfoMapper dataInfoMapper;

    @Override
    public Set<String> getNameSet() {
        return dataInfoMapper.getNameSet();
    }

    @Override
    public List<DataInfo> getInfo(Integer dirId) {
        return dataInfoMapper.getInfo(dirId);
    }

    @Override
    public DataInfo getInfoById(Integer id) {
        return dataInfoMapper.getInfoById(id);
    }

    @Override
    public List<DataInfo> searchByInfo(String info) {
        return dataInfoMapper.selectDataInfo(info);
    }

    @Override
    public List<DataInfo> getFrequent() {
        return dataInfoMapper.getFrequent();
    }

    @Override
    public DataInfo getDataInfo(String name) {
        return dataInfoMapper.getDataInfo(name);
    }
}




