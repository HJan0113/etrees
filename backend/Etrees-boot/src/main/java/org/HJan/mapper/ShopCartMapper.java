package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.ShopCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
* @author HJsir
* @description 针对表【shop_cart】的数据库操作Mapper
* @createDate 2023-06-01 11:21:30
* @Entity org.HJan.entity.ShopCart
*/
@Mapper
public interface ShopCartMapper extends BaseMapper<ShopCart> {

    @Delete("DELETE FROM shop_cart WHERE id=#{id} and user_id = #{userId}")
    void deleteByIds(Integer id, Integer userId);

    @Update("UPDATE shop_cart SET quantity=#{num} WHERE id=#{cartId}")
    void updateQuantity(Integer cartId, Integer num);
}




