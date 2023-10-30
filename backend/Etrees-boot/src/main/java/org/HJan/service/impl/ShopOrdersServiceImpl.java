package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.ShopOrders;
import org.HJan.mapper.ShopOrdersMapper;
import org.HJan.service.ShopOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author HJsir
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2023-05-20 16:07:08
*/
@Service
public class ShopOrdersServiceImpl extends ServiceImpl<ShopOrdersMapper, ShopOrders>
    implements ShopOrdersService {

    @Autowired
    private ShopOrdersMapper shopOrdersMapper;

    @Override
    @Transactional
    public void updateStatus(Integer id) {
        shopOrdersMapper.setStatus(id);
    }

    @Override
    public long selectToday() {
        return shopOrdersMapper.selectToday();
    }

    @Override
    public long getNum() {
        return shopOrdersMapper.getNum();
    }

    @Override
    public List<Map<String, Map<String, Object>>> getTrend() {
        //获取商品销售排行 - 总销售排行
        List<Integer> list = shopOrdersMapper.selectOnNum();
        int num = 1;
        List<Map<String, Map<String,Object>>> list1 = new ArrayList<>();
        for (Integer id :list){
            Map<String, Map<String,Object>> map = new HashMap<>();
            Map<String, Object> map1 = new HashMap<>();
            Integer count = shopOrdersMapper.getCount(id);
            String name = shopOrdersMapper.getNameById(id);
            map1.put("count",count);
            map1.put("name",name);
            map.put(Integer.toString(num),map1);
            num++;
            list1.add(map);
        }
        return list1;
    }
}




