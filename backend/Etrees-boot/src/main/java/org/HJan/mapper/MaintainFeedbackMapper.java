package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.MaintainFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author HJsir
* @description 针对表【maintain_feedback(用户反馈表)】的数据库操作Mapper
* @createDate 2023-05-28 14:26:28
* @Entity org.HJan.entity.MaintainFeedback
*/
@Mapper
public interface MaintainFeedbackMapper extends BaseMapper<MaintainFeedback> {

    //查询当天记录数
    @Select("select count(*) " +
            "from maintain_feedback " +
            "where date_format(create_time,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')")
    long selectToday();
}




