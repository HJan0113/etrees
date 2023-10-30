package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HJsir
* @description 针对表【menu】的数据库操作Mapper
* @createDate 2023-05-16 15:01:39
* @Entity org.HJan.entity.Menu
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}




