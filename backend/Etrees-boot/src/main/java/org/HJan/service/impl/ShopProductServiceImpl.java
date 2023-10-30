package org.HJan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.ShopProduct;
import org.HJan.mapper.ShopProductMapper;
import org.HJan.service.ShopProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HJsir
* @description 针对表【product(商品信息表)】的数据库操作Service实现
* @createDate 2023-05-20 16:07:08
*/
@Service
public class ShopProductServiceImpl extends ServiceImpl<ShopProductMapper, ShopProduct>
    implements ShopProductService {

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Override
    public void deleteByDirId(Integer id) {
        shopProductMapper.deleteByDirId(id);
    }

    @Override
    public List<ShopProduct> listByTime() {
        return shopProductMapper.getByTime();
    }

    @Override
    public List<ShopProduct> searchByInfo(String info) {
        LambdaQueryWrapper<ShopProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(info),ShopProduct::getName,info);
        return shopProductMapper.selectList(queryWrapper);
    }
}




