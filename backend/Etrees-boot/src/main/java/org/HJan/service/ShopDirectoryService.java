package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.ShopDirectory;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author HJsir
* @description 针对表【shop_directory(商品目录)】的数据库操作Service
* @createDate 2023-05-24 20:36:00
*/
public interface ShopDirectoryService extends IService<ShopDirectory> {

    @Select("SELECT * FROM shop_directory")
    List<ShopDirectory> getAll();
}
