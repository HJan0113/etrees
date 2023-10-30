package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.DataInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
* @author HJsir
* @description 针对表【data_info(图鉴信息)】的数据库操作Mapper
* @createDate 2023-05-24 15:53:45
* @Entity org.HJan.entity.DataInfo
*/
@Mapper
public interface DataInfoMapper extends BaseMapper<DataInfo> {

    @Select("SELECT name from data_info")
    Set<String> getNameSet();

    @Select("SELECT * FROM data_info WHERE dir_id = #{dirId}")
    List<DataInfo> getInfo(Integer dirId);

    @Select("SELECT * FROM data_info WHERE id=#{id}")
    DataInfo getInfoById(Integer id);

    @Select("SELECT * FROM data_info WHERE status = 1")
    List<DataInfo> getFrequent();

    @Select("SELECT * FROM data_info WHERE CONCAT(dir_name,name) = #{name}")
    DataInfo getDataInfo(String name);

    @Select("SELECT * FROM data_info WHERE CONCAT(dir_name,name) like CONCAT('%', #{info}, '%')")
    List<DataInfo> selectDataInfo(String info);
}




