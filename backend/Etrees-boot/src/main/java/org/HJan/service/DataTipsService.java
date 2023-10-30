package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.DataTips;

import java.util.List;

/**
* @author HJsir
* @description 针对表【data_tips(健康提示)】的数据库操作Service
* @createDate 2023-05-24 15:53:45
*/
public interface DataTipsService extends IService<DataTips> {

    List<DataTips> getAll();

    List<DataTips> getTips();
}
