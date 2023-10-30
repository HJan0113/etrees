package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.DataTips;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author HJsir
* @description 针对表【data_tips(健康提示)】的数据库操作Mapper
* @createDate 2023-05-24 15:53:45
* @Entity org.HJan.entity.DataTips
*/
@Mapper
public interface DataTipsMapper extends BaseMapper<DataTips> {

    @Select("SELECT * FROM data_tips")
    List<DataTips> getAll();

    @Select("SELECT * FROM data_tips ORDER BY RAND() LIMIT 4")
    List<DataTips> getTips();
}




