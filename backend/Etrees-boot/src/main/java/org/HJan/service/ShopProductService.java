package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.ShopProduct;

import java.util.List;

/**
* @author HJsir
* @description 针对表【product(商品信息表)】的数据库操作Service
* @createDate 2023-05-20 16:07:08
*/
public interface ShopProductService extends IService<ShopProduct> {

    void deleteByDirId(Integer id);

    List<ShopProduct> listByTime();

    List<ShopProduct> searchByInfo(String info);
}
