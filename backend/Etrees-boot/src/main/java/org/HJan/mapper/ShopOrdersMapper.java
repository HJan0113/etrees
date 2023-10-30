package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.ShopOrders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author HJsir
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2023-05-20 16:07:08
* @Entity org.HJan.entity.Orders
*/
@Mapper
public interface ShopOrdersMapper extends BaseMapper<ShopOrders> {

    @Update("UPDATE shop_orders SET status='已处理' WHERE order_id = #{id}")
    void setStatus(Integer id);

    //查询当天记录数
    @Select("select count(*) " +
            "from shop_orders " +
            "where date_format(create_time,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')")
    long selectToday();

    @Select("Select count(*) from shop_orders where status = '已下单'")
    long getNum();

    @Select("SELECT product_id FROM shop_orders GROUP BY product_id ORDER BY COUNT(*) DESC LIMIT 3")
    List<Integer> selectOnNum();

    @Select("SELECT COUNT(*) AS count FROM shop_orders WHERE product_id = #{id}")
    Integer getCount(Integer id);

    @Select("SELECT name FROM shop_product WHERE id = #{id}")
    String getNameById(Integer id);
}




