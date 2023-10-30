package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.ShopDirectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author HJsir
* @description 针对表【shop_directory(商品目录)】的数据库操作Mapper
* @createDate 2023-05-24 20:36:00
* @Entity org.HJan.entity.ShopDirectory
*/
@Mapper
public interface ShopDirectoryMapper extends BaseMapper<ShopDirectory> {

    @Select("SELECT * FROM shop_directory")
    List<ShopDirectory> getAll();
}




