package org.HJan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.ShopCart;
import org.HJan.mapper.ShopCartMapper;
import org.HJan.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author HJsir
* @description 针对表【shop_cart】的数据库操作Service实现
* @createDate 2023-06-01 11:21:30
*/
@Service
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper, ShopCart>
    implements ShopCartService{

    @Autowired
    private ShopCartMapper shopCartMapper;


    @Override
    public void updateQuantity(Integer cartId, Integer num) {
        shopCartMapper.updateQuantity(cartId,num);
    }

    @Override
    public void delete(List<String> ids) {
        shopCartMapper.deleteBatchIds(ids);
    }

    @Override
    public boolean isExist(ShopCart shopCart) {
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getProductId,shopCart.getProductId());
        queryWrapper.eq(ShopCart::getUserId,shopCart.getUserId());
        ShopCart shopCart1 = shopCartMapper.selectOne(queryWrapper);
        //若表中无记录，返回false
        if (shopCart1==null){
            return false;
        }
        return true;
    }

    @Override
    public ShopCart getIdByInfo(ShopCart shopCart) {
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getProductId,shopCart.getProductId());
        queryWrapper.eq(ShopCart::getUserId,shopCart.getUserId());
        ShopCart shopCart1 = shopCartMapper.selectOne(queryWrapper);
        return shopCart1;
    }
}




