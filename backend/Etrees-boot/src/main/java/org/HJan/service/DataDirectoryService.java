package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.DataDirectory;

import java.util.List;

/**
* @author HJsir
* @description 针对表【data_directory(图鉴目录)】的数据库操作Service
* @createDate 2023-05-24 15:53:45
*/
public interface DataDirectoryService extends IService<DataDirectory> {

    List<DataDirectory> getAll();

    List<DataDirectory> getDir();
}
