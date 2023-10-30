package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author HJsir
* @description 针对表【permission(权限列表)】的数据库操作Mapper
* @createDate 2023-05-16 15:01:39
* @Entity org.HJan.entity.Permission
*/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT id from permission where menu_id = #{thisMenuId}")
    Integer getPermIdByMenuId(Integer thisMenuId);

    @Select("SELECT menu_id from permission where id = #{permId}")
    Integer getMenuIdByPermId(Integer permId);
}




