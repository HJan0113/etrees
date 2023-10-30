package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HJsir
* @description 针对表【user_address】的数据库操作Mapper
* @createDate 2023-06-05 20:51:40
* @Entity org.HJan.entity.UserAddress
*/
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {

}




