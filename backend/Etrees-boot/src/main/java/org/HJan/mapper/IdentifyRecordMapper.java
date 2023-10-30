package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.IdentifyRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author HJsir
* @description 针对表【identify_record(识别记录)】的数据库操作Mapper
* @createDate 2023-05-29 16:34:04
* @Entity org.HJan.entity.IdentifyRecord
*/
@Mapper
public interface IdentifyRecordMapper extends BaseMapper<IdentifyRecord> {

    //查询当天记录数
    @Select("select count(*) " +
            "from identify_record " +
            "where date_format(create_time,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')")
    long selectToday();
}




