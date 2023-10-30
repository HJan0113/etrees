package org.HJan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Log;
import org.HJan.common.Result;
import org.HJan.entity.ShopDirectory;
import org.HJan.entity.ShopOrders;
import org.HJan.entity.ShopProduct;
import org.HJan.service.ShopDirectoryService;
import org.HJan.service.ShopOrdersService;
import org.HJan.service.ShopProductService;
import org.HJan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品信息管理Controller
 */
@RestController
@Slf4j
@RequestMapping("/product")
public class ShopController {
    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private ShopOrdersService shopOrdersService;

    @Autowired
    private ShopDirectoryService shopDirectoryService;

    //商品信息白名单（不可删除）
    private static final Integer DEL_WHITELIST[] = {
        10000018,10000019,10000020,10000021,10000022,10000023,10000024,10000025,10000026,
            10000027,10000028,10000029,10000048,10000050,10000052,10000054,10000049,10000051,
            10000053,10000055,10000033,10000035,10000037,10000039,10000034,10000036,10000038,
            10000040,10000041,10000043,10000044,10000046,10000042,10000101,10000045,10000047,
            10000102,10000045,10000047,10000102,10000103,10000104,10000105,10000106,10000107,
            10000108,10000109,10000110,10000111,10000112,10000113,10000114,10000115,10000116,
            10000117,10000118,10000119,10000120,10000121,10000122,10000123,10000124,10000125,
            10000126,10000127,10000128,10000129,10000130,10000131,10000132,10000133,10000134,
            10000135,10000136,10000137,10000138,10000139,10000140,10000141
    };
    /**
     * 添加商品信息
     * 商品名称、价格、图片地址
     *
     * @param shopProduct
     * @return
     */
    @Log(value="商城-添加商品")
    @PostMapping()
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:goods')")
    public Result<String> save(@RequestBody ShopProduct shopProduct) {
        log.info("添加商品信息：{}", shopProduct.toString());
        //得到商品的目录id
        Integer dirId = shopProduct.getDirId();
        //根据目录id设置目录名
        shopProduct.setDirName(shopDirectoryService.getById(dirId).getName());
        //新增
        shopProductService.save(shopProduct);
        RedisUtils.loadData("prodPrefix:");
        return Result.success("添加成功");
    }

    /**
     * 删除商品信息
     * @param id
     * @return
     */
    @Log(value="商城-删除商品")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:goods')")
    public Result<String> delete(@PathVariable Integer id) {
        log.info("删除商品信息：{}", id);
        for(Integer ids : DEL_WHITELIST){
            if(ids.equals(id)){
                return Result.error("该商品信息不可删除");
            }
        }
        shopProductService.removeById(id);
        RedisUtils.loadData("prodPrefix:");
        return Result.success("删除成功");
    }

    /**
     * 修改商品信息-查询回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:goods')")
    public Result<ShopProduct> updateGet(@PathVariable Integer id) {
        log.info("查询回显：{}", id);
        ShopProduct shopProduct = shopProductService.getById(id);
        if (shopProduct != null) {
            return Result.success(shopProduct);
        }
        return Result.error("找不到对应商品信息");
    }

    /**
     * 修改商品信息
     *
     * @param shopProduct
     * @return
     */
    @Log(value="商城-修改商品")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:goods')")
    public Result<String> update(@RequestBody ShopProduct shopProduct) {
        log.info("修改商品信息：{}", shopProduct.toString());
        // 查询指定 ID 的原始数据，并获取版本号
        ShopProduct originalData = shopProductService.getById(shopProduct.getId());
        Timestamp originalVersion = originalData.getVersion();
        // 将修改数据对象中的版本号设置为原始数据版本号
        shopProduct.setVersion(originalVersion);
        // 更新数据时添加版本号判断
        boolean success = shopProductService.update(shopProduct, new UpdateWrapper<ShopProduct>()
                .eq("id", shopProduct.getId())
                .eq("version", originalVersion));
        if (success) {
            RedisUtils.loadData("prodPrefix:");
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败，数据已被其他用户修改。");
        }
    }

    /**
     * 商品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @param id
     * @return
     */
    @GetMapping("/{page}/{pageSize}")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:goods')")
    public Result<IPage<ShopProduct>> page(@PathVariable int page,
                                           @PathVariable int pageSize,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) Integer id) {
        log.info("商品信息分页查询：page = {},pageSize={},name={},id={}", page, pageSize, name, id);
        //根据page和pageSize生成key
        String key = "prodPrefix:" + page + pageSize +name +id;
//        先去redis 中查找是否存在要返回的信息
        if (redisTemplate.opsForValue().get(key) != null) {
            return Result.success((IPage<ShopProduct>) redisTemplate.opsForValue().get(key));
        }
        //构造分页构造器
        Page<ShopProduct> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<ShopProduct> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤-
        queryWrapper.like(StringUtils.isNotEmpty(name), ShopProduct::getName, name);
        queryWrapper.eq(id != null, ShopProduct::getId, id);
        //排序条件
        queryWrapper.orderByAsc(ShopProduct::getId);
        //执行查询
        shopProductService.page(pageInfo, queryWrapper);
        //将emp信息存入redis , 存在时间 120分钟
        redisTemplate.opsForValue().set(key, pageInfo, 120, TimeUnit.MINUTES);
        return Result.success(pageInfo);
    }

    /**
     * 添加商品目录信息
     * @param shopDirectory
     * @return
     */
    @Log(value="商城-添加目录")
    @PostMapping("/dir")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:directory')")
    public Result<String> addDir(@RequestBody ShopDirectory shopDirectory) {
        log.info("添加商品信息：{}", shopDirectory.toString());
        shopDirectoryService.save(shopDirectory);
        return Result.success("添加成功");
    }

    /**
     * 删除商品目录信息
     * @param id
     * @return
     */
    @Log(value="商城-删除目录")
    @DeleteMapping("/dir/{id}")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:directory')")
    public Result<String> delete1(@PathVariable Integer id) {
        log.info("删除商品信息：{}", id);
        shopProductService.deleteByDirId(id);
        shopDirectoryService.removeById(id);
        RedisUtils.loadData("prodPrefix:");
        return Result.success("删除成功");
    }

    /**
     * 修改商品目录信息-查询回显
     *
     * @param id
     * @return
     */
    @GetMapping("/dir/{id}")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:directory')")
    public Result<ShopDirectory> updateGet1(@PathVariable Integer id) {
        log.info("查询回显：{}", id);
        ShopDirectory shopDirectory = shopDirectoryService.getById(id);
        if (shopDirectory != null) {
            return Result.success(shopDirectory);
        }
        return Result.error("找不到对应目录");
    }

    /**
     * 修改目录信息
     *
     * @param shopDirectory
     * @return
     */
    @Log(value="商城-修改目录")
    @PutMapping("/dir")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:directory')")
    public Result<String> update(@RequestBody ShopDirectory shopDirectory) {
        log.info("修改目录信息：{}", shopDirectoryService.toString());
        // 查询指定 ID 的原始数据，并获取版本号
        ShopDirectory originalData = shopDirectoryService.getById(shopDirectory.getId());
        Timestamp originalVersion = originalData.getVersion();
        // 将修改数据对象中的版本号设置为原始数据版本号
        shopDirectory.setVersion(originalVersion);
        // 更新数据时添加版本号判断

        boolean success = shopDirectoryService.update(shopDirectory, new UpdateWrapper<ShopDirectory>()
                .eq("id", shopDirectory.getId())
                .eq("version", originalVersion));
        if (success) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败，数据已被其他用户修改。");
        }
    }

    /**
     * 目录查询
     * @return
     */
    @GetMapping("/dir")
    @PreAuthorize("hasAnyAuthority('sys:mall:manager','mall:directory')")
    public Result<List<ShopDirectory>> page() {
        log.info("目录查询回显");
        List<ShopDirectory> shopDirectoryList = shopDirectoryService.getAll();
        return Result.success(shopDirectoryList);
    }

    /**
     * 订单分页条件查询
     *
     * @param page
     * @param pageSize
     * @param orderId
     * @param productId
     * @return
     */
    @GetMapping("/order/{page}/{pageSize}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','mall:orders')")
    public Result<IPage<ShopOrders>> page(@PathVariable int page,
                                          @PathVariable int pageSize,
                                          @RequestParam(required = false) Integer orderId,
                                          @RequestParam(required = false) Integer productId
                                          ) {
        log.info("分页查询：page = {},pageSize={}", page, pageSize);
        //根据page和pageSize生成key
        String key = "ordPrefix:" + page + pageSize +orderId + productId;
        //先去redis 中查找是否存在要返回的emp信息
        IPage<ShopOrders> odersMsg = (IPage<ShopOrders>) redisTemplate.opsForValue().get(key);
        if (odersMsg != null) {
            return Result.success(odersMsg);
        }
        //构造分页构造器
        Page<ShopOrders> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<ShopOrders> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤
        queryWrapper.eq(orderId != null, ShopOrders::getOrderId, orderId);
        queryWrapper.eq(productId != null, ShopOrders::getProductId, productId);
        //排序条件
        queryWrapper.orderByAsc(ShopOrders::getOrderId);
        //执行查询
        shopOrdersService.page(pageInfo, queryWrapper);
        //将emp信息存入redis , 存在时间 120分钟
        redisTemplate.opsForValue().set(key, pageInfo, 120, TimeUnit.MINUTES);
        return Result.success(pageInfo);
    }

    /**
     * 处理订单
     * @param id
     * @return
     */
    @Log(value="商城-订单处理")
    @PostMapping("/order/deal/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','mall:orders')")
    public Result<String> setOrderStatus(@PathVariable Integer id){
        log.info("处理订单，id={}",id);
        shopOrdersService.updateStatus(id);
        return Result.success("处理订单完成");
    }



}
