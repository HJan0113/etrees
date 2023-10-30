package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.DataInfo;

import java.util.List;
import java.util.Set;

/**
* @author HJsir
* @description 针对表【data_info(图鉴信息)】的数据库操作Service
* @createDate 2023-05-24 15:53:45
*/
public interface DataInfoService extends IService<DataInfo> {

    Set<String> getNameSet();

    List<DataInfo> getInfo(Integer dirId);

    DataInfo getInfoById(Integer id);

    List<DataInfo> searchByInfo(String info);

    List<DataInfo> getFrequent();

    DataInfo getDataInfo(String name);
}
