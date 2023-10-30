package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.DataDirectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author HJsir
* @description 针对表【data_directory(图鉴目录)】的数据库操作Mapper
* @createDate 2023-05-24 15:53:45
* @Entity org.HJan.entity.DataDirectory
*/
@Mapper
public interface DataDirectoryMapper extends BaseMapper<DataDirectory> {

    @Select("SELECT * FROM data_directory")
    List<DataDirectory> getAll();

    @Select("SELECT * FROM data_directory")
    List<DataDirectory> getDir();
}




