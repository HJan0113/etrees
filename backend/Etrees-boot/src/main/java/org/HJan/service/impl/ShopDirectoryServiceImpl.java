package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.ShopDirectory;
import org.HJan.mapper.ShopDirectoryMapper;
import org.HJan.service.ShopDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HJsir
* @description 针对表【shop_directory(商品目录)】的数据库操作Service实现
* @createDate 2023-05-24 20:36:00
*/
@Service
public class ShopDirectoryServiceImpl extends ServiceImpl<ShopDirectoryMapper, ShopDirectory>
    implements ShopDirectoryService{

    @Autowired
    private ShopDirectoryMapper shopDirectoryMapper;

    @Override
    public List<ShopDirectory> getAll() {
        return shopDirectoryMapper.getAll();
    }
}




