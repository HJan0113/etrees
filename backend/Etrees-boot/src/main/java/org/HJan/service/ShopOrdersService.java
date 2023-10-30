package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.ShopOrders;

import java.util.List;
import java.util.Map;

/**
* @author HJsir
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-05-20 16:07:08
*/
public interface ShopOrdersService extends IService<ShopOrders> {

    void updateStatus( Integer id);

    long selectToday();

    long getNum();

    List<Map<String, Map<String, Object>>> getTrend();
}
