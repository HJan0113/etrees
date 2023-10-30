package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.ShopCart;

import java.util.List;

/**
* @author HJsir
* @description 针对表【shop_cart】的数据库操作Service
* @createDate 2023-06-01 11:21:30
*/
public interface ShopCartService extends IService<ShopCart> {


    void updateQuantity(Integer cartId, Integer num);

    void delete(List<String> ids);

    boolean isExist(ShopCart shopCart);

    ShopCart getIdByInfo(ShopCart shopCart);
}
