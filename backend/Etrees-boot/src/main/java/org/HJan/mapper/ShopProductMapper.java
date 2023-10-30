package org.HJan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.HJan.entity.ShopProduct;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author HJsir
* @description 针对表【product(商品信息表)】的数据库操作Mapper
* @createDate 2023-05-20 16:07:08
* @Entity org.HJan.entity.Product
*/
@Mapper
public interface ShopProductMapper extends BaseMapper<ShopProduct> {

    @Delete("DELETE FROM shop_product WHERE dir_id = #{id}")
    void deleteByDirId(Integer id);

    @Select("SELECT * FROM shop_product ORDER BY update_time DESC LIMIT 10")
    List<ShopProduct> getByTime();
}




