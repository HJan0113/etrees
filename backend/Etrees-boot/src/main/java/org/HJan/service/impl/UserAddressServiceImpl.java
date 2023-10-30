package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.UserAddress;
import org.HJan.mapper.UserAddressMapper;
import org.HJan.service.UserAddressService;
import org.springframework.stereotype.Service;

/**
* @author HJsir
* @description 针对表【user_address】的数据库操作Service实现
* @createDate 2023-06-05 20:51:40
*/
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress>
    implements UserAddressService{

}




