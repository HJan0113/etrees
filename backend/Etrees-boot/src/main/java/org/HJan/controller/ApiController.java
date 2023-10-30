package org.HJan.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Result;
import org.HJan.dto.Base64Dto;
import org.HJan.dto.OrderAppDto;
import org.HJan.dto.UserDto;
import org.HJan.entity.*;
import org.HJan.service.*;
import org.HJan.utils.AliOSSUtils;
import org.HJan.utils.JwtUtils;
import org.HJan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private DataTipsService dataTipsService;
    @Autowired
    private DataDirectoryService dataDirectoryService;
    @Autowired
    private DataInfoService dataInfoService;
    @Autowired
    private ShopDirectoryService shopDirectoryService;
    @Autowired
    private ShopProductService shopProductService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private ShopOrdersService shopOrdersService;
    @Autowired
    private IdentifyRecordService identifyRecordService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private MaintainFeedbackService maintainFeedbackService;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register/{code}")
    public Result<String> save(@RequestBody User user, @PathVariable Integer code) {
        log.info("用户注册");
        //md5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //获取传来的user的Username
        String username = user.getUserName();
        //获取phone
        String phone = user.getPhone();
        //获取redis中存储的验证码
        Integer verificode = Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get("verifi:" + phone)));

        //判断用户输入的验证码是否和有效验证码相同
        if (!verificode.equals(code)) {
            return Result.error("验证码输入错误");
        }
        //1.进数据库判断是否存在当前用户名，防止重名
        if (!userService.check(username)) {
            return Result.error("用户名已存在，请重新输入");
        }
        //执行注册
        userService.save(user);
        return Result.success("注册成功");
    }

    /**
     * 密码登录
     *
     * @param user
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        log.info("登入验证");
        //将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);
        // 将拿到的user的userName去数据库中查找是否是该用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, user.getUserName());
        User one = userService.getOne(wrapper);
        //   如果查到有该用户
        if (one != null) {
            //校验密码是否也相同
            if (!Objects.equals(one.getPassword(), user.getPassword())) {
                return Result.error("密码错误");
            }
            //如果密码也相同,根据相应字段生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userName", one.getUserName());
            //生成jwt
            String jwt = JwtUtils.generateAppJwt(claims);
            //登录成功，将员工信息存入redis
            UserDto userDto = BeanUtil.copyProperties(one, UserDto.class);
            Integer id = userService.getIdByUserName(user.getUserName());
            Map<String, Object> map = new HashMap<>();
            map.put("token", jwt);
            map.put("id", id);
            redisTemplate.opsForValue().set(jwt, userDto);
            //设置用户信息在redis的存储有效期 120小时
            redisTemplate.expire(jwt, 120, TimeUnit.HOURS);

            return Result.success(map);
        }
        //登录失败, 返回错误信息
        return Result.error("用户名不存在");
    }


//    /**
//     * 手机号登录
//     * @param phone
//     * @param code
//     * @return
//     */
//    @PostMapping("/loginBySms")
//    public Result<Map<String, Object>> loginBySms(@RequestParam String phone, @RequestParam String code) {
//        log.info("手机登录");
//        String msg = stringRedisTemplate.opsForValue().get("verifi:"+phone);
//        if(code.equals(msg)){
//            User user = userService.selectOne(phone);
//            if(user!=null){
//                Map<String, Object> claims = new HashMap<>();
//                claims.put("userName", user.getUserName());
//                //生成jwt
//                String jwt = JwtUtils.generateAppJwt(claims);
//                //登录成功，将员工信息存入redis
//                UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
//                Integer id = user.getId();
//                Map<String, Object> map = new HashMap<>();
//                map.put("token", jwt);
//                map.put("id", id);
//                redisTemplate.opsForValue().set(jwt, userDto);
//                //设置用户信息在redis的存储有效期 120小时
//                redisTemplate.expire(jwt, 120, TimeUnit.HOURS);
//                return Result.success(map);
//            }
//            return Result.error("用户不存在");
//        }
//        return Result.error("验证码错误");
//    }


    /**
     * 用户基本信息回显
     *
     * @param token
     * @return
     */
    @GetMapping
    public Result<UserDto> getMsg(@RequestHeader("Authorization") String token) {
        //取得请求头中的token,以token为key , 获取相应的对象数据
        UserDto userDto = (UserDto) redisTemplate.opsForValue().get(token);
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        //如果缓存中没有该数据
        if (userDto == null) {
            //根据id从数据库中查找
            User user = userService.getByUserName(username);
            if (user == null) {
                return Result.error("未能找到该id的用户");
            }
            userDto = BeanUtil.copyProperties(user, UserDto.class);
        }
        return Result.success(userDto);
    }

    /**
     * 修改用户基本信息
     *
     * @param token
     * @param user
     * @return
     */
    @PutMapping("/personal")
    public Result<String> changeMsg(@RequestHeader("Authorization") String token, @RequestBody User user) {
        if (user == null) {
            return Result.error("修改失败");
        }
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer id = userService.getIdByUserName(username);
        user.setId(id);
        boolean b = userService.updateById(user);
        if (!b) {
            return Result.error("修改失败");
        }
        User user1 = userService.getById(user.getId());
        UserDto userDto = BeanUtil.copyProperties(user1, UserDto.class);
        //更新缓存
        redisTemplate.opsForValue().set(token, userDto);
        return Result.success("修改成功");
    }

    /**
     * 用户修改密码
     *
     * @param data
     * @return
     */
    @PutMapping("/pwd")
    public Result<String> changePwd(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> data) {
        //新密码不能和原密码相同
        if (Objects.equals(data.get("password"), data.get("newpass"))) {
            return Result.error("新密码不能和旧密码相同");
        }
        //获得用户id
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        //从数据库查找该用户信息
        User user = userService.getByUserName(username);
        //校验原密码是否正确
        String password = (String) data.get("password");
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!user.getPassword().equals(password)) {
            return Result.error("旧密码输入错误");
        }
        //密码校验成功,将新密码加密
        String s = (String) data.get("newpass");
        String newpass = DigestUtils.md5DigestAsHex(s.getBytes());
        user.setPassword(newpass);
        //允许修改密码
        boolean b = userService.updateById(user);
        if (!b) {
            return Result.error("修改失败");
        }
        return Result.success("密码修改成功");
    }

    /**
     * 换绑手机号
     *
     * @param data
     * @return
     */
    @PostMapping("/phone")
    public Result<String> changePhone(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> data) {
        //如果换绑手机号和输入原手机号相同
        if (Objects.equals(data.get("phone"), data.get("newphone"))) {
            return Result.error("换绑手机不能和原手机号相同");
        }
        //获得用户对象
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        User user = userService.getByUserName(username);
        if (!user.getPhone().equals(data.get("phone").toString())) {
            return Result.error("手机号输入错误");
        }
        //如果相同,校验验证码
        String verificode = stringRedisTemplate.opsForValue().get("verifi:" + data.get("newphone").toString());
        if (verificode == null) {
            return Result.error("未发送验证码或验证码已过期");
        }
        if (!verificode.equals(data.get("check").toString())) {
            return Result.error("验证码输入错误");
        }
        //验证码正确,允许修改密码
        user.setPhone(data.get("newphone").toString());
        boolean b = userService.updateById(user);
        if (!b) {
            return Result.error("密码修改失败");
        }
        //清缓存
        RedisUtils.loadData(token);
        return Result.success("修改成功");
    }

    /**
     * app-首页健康提示
     *
     * @return
     */
    @GetMapping("/getTips")
    public Result<List<DataTips>> getTips() {
        log.info("获取健康提示");
        return Result.success(dataTipsService.getTips());
    }

    /**
     * 图鉴搜索
     *
     * @param info
     * @return
     */
    @GetMapping("/data/search")
    public Result<List<DataInfo>> search(@RequestParam String info) {
        log.info("用户搜索：{}", info);
        List<DataInfo> list = dataInfoService.searchByInfo(info);
        return Result.success(list);
    }

    /**
     * 图鉴目录
     *
     * @return
     */
    @GetMapping("/data/getDir")
    public Result<List<DataDirectory>> getDir() {
        log.info("获取图鉴目录");
        return Result.success(dataDirectoryService.getDir());
    }

    /**
     * 常见病害
     *
     * @return
     */
    @GetMapping("/data/frequent")
    public Result<List<DataInfo>> getFrequent() {
        log.info("得到常见病害");
        List<DataInfo> list = dataInfoService.getFrequent();
        return Result.success(list);
    }


    /**
     * 图鉴二级目录病种概览
     *
     * @param dirId
     * @return
     */
    @GetMapping("/data/getChiDir/{dirId}")
    public Result<List<DataInfo>> getChiDirInfo(@PathVariable Integer dirId) {
        log.info("获取二级目录");
        return Result.success(dataInfoService.getInfo(dirId));
    }

    /**
     * 获取图鉴详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/data/getInfo/{id}")
    public Result<DataInfo> getInfo(@PathVariable Integer id) {
        log.info("获取图鉴信息");
        DataInfo info = dataInfoService.getInfoById(id);
        return Result.success(info);
    }

    /**
     * 商城首页获取商品目录及最新上架信息
     *
     * @return
     */
    @GetMapping("/shop/getShop")
    public Result<List<ShopDirectory>> getShop() {
        log.info("商城首页");
        //得到所有商品目录
        List<ShopDirectory> dirList = shopDirectoryService.list();
        //得到十条最新上架商品
        List<ShopProduct> prodList = shopProductService.listByTime();
        return Result.success(dirList).add("prodList", prodList);
    }

    /**
     * 商城二级目录概览
     *
     * @param dirId
     * @return
     */
    @GetMapping("/shop/getChiDir/{dirId}")
    public Result<List<ShopProduct>> getChiDir(@PathVariable Integer dirId) {
        log.info("得到商品二级目录概览");
        LambdaQueryWrapper<ShopProduct> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dirId != null, ShopProduct::getDirId, dirId);
        return Result.success(shopProductService.list(queryWrapper));
    }

    /**
     * 商品详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/shop/getInfo/{id}")
    public Result<ShopProduct> getProduct(@PathVariable Integer id) {
        log.info("得到商品详细信息");
        return Result.success(shopProductService.getById(id));
    }

    /**
     * 添加购物车
     *
     * @param token
     * @param shopCart
     * @return
     */
    @PostMapping("/shop/setCart")
    public Result<String> setCart(@RequestHeader("Authorization") String token, @RequestBody ShopCart shopCart) {
        log.info("添加购物车:{}", shopCart.toString());
        //得到当前用户姓名
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        shopCart.setUserId(userId);
        //查找数据库中是否有重复商品
        if (shopCartService.isExist(shopCart)) {
            ShopCart newShopCart = shopCartService.getIdByInfo(shopCart);
            shopCart.setId(newShopCart.getId());
            Integer num = newShopCart.getQuantity();
            shopCart.setQuantity(num + 1);
            shopCartService.updateById(shopCart);
            return Result.success("商品已存在，数量+1");
        }
        shopCart.setUserId(userId);
        shopCartService.save(shopCart);
        return Result.success("添加购物车成功");
    }

    /**
     * 删除购物车信息
     *
     * @param token
     * @param ids
     * @return
     */
    @PostMapping("/shop/cart")
    public Result<String> deleteCart(@RequestHeader("Authorization") String token, @RequestBody List<String> ids) {
        log.info("删除商品");
        //据购物车id删除记录
        shopCartService.delete(ids);
        return Result.success("删除成功");
    }

    /**
     * 购物车页面回显
     *
     * @param token
     * @return
     */
    @GetMapping("/shop/getCart")
    public Result<List<ShopCart>> getCart(@RequestHeader("Authorization") String token) {
        log.info("购物车页面批量回显");
        //得到用户id
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        //根据id查询记录
        LambdaQueryWrapper<ShopCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopCart::getUserId, userId);
        List<ShopCart> list = shopCartService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 购物车商品修改数量
     *
     * @param cartId
     * @param num
     * @return
     */
    @PostMapping("/shop/cart/setNum/{cartId}")
    public Result<String> setNum(@PathVariable Integer cartId, @RequestParam Integer num) {
        log.info("购物车商品修改数量:{}", num);
        shopCartService.updateQuantity(cartId, num);
        return Result.success("修改成功");
    }


    /**
     * 购物车单条购买记录回显
     *
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/shop/getCart/{id}")
    public Result<ShopCart> getCartById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        log.info("购物车页面购买记录回显");
        return Result.success(shopCartService.getById(id));
    }

    /**
     * 购物车商品支付
     *
     * @param idList
     * @param address
     * @param phone
     * @return
     */
    @PostMapping("/shop/cart/buy")
    public Result<String> buyCart(@RequestHeader("Authorization") String token,
                                  @RequestBody List<Integer> idList,
                                  @RequestParam String name,
                                  @RequestParam String address,
                                  @RequestParam String phone) {
        log.info("购物车处理订单：{}", idList.toString());
        //得到用户id
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        //判断是否实名
        if (!userService.checkAuth(userId)) {
            return Result.error("用户未完成实名，请先进行实名认证");
        }
        for (Integer id : idList) {
            ShopCart shopCart = shopCartService.getById(id);
            ShopOrders shopOrders = new ShopOrders(shopCart);
            shopOrders.setName(name);
            shopOrders.setAddress(address);
            shopOrders.setPhone(phone);
            try {
                shopOrdersService.save(shopOrders);
                shopCartService.removeById(id);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("结算失败");
            }
        }
        return Result.success("结算成功");
    }

    /**
     * 立即购买
     *
     * @param token
     * @param shopOrders
     * @return
     */
    @PostMapping("/shop/buy")
    public Result<String> buyNow(@RequestHeader("Authorization") String token, @RequestBody ShopOrders shopOrders) {
        log.info("立即购买:{}", shopOrders.toString());
        //得到当前用户姓名
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        //判断是否实名
        if (!userService.checkAuth(userId)) {
            return Result.error("用户未完成实名，请先进行实名认证");
        }

        shopOrders.setUserId(userId);

        shopOrdersService.save(shopOrders);
        return Result.success("订单提交成功");
    }

    /**
     * 商城搜索
     *
     * @param info
     * @return
     */
    @GetMapping("/shop/search")
    public Result<List<ShopProduct>> searchProduct(@RequestParam String info) {
        log.info("用户搜索：{}", info);
        List<ShopProduct> list = shopProductService.searchByInfo(info);
        return Result.success(list);
    }

    /**
     * 用户识别记录回显
     *
     * @param token
     * @return
     */
    @GetMapping("/person/record")
    public Result<List<IdentifyRecord>> getRecord(@RequestHeader("Authorization") String token) {
        log.info("用户识别记录回显");
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        LambdaQueryWrapper<IdentifyRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdentifyRecord::getUserId, userId);
        queryWrapper.orderByDesc(IdentifyRecord::getCreateTime);
        List<IdentifyRecord> list = identifyRecordService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 添加收货地址
     *
     * @param token
     * @param userAddress
     * @return
     */
    @PostMapping("/person/addr")
    public Result<String> addAddr(@RequestHeader("Authorization") String token, @RequestBody UserAddress userAddress) {
        log.info("添加收货地址");
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        userAddress.setUserId(userId);
        userAddressService.save(userAddress);
        return Result.success("添加成功");
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @return
     */
    @DeleteMapping("/person/addr/{id}")
    public Result<String> deleteAddr(@PathVariable Integer id) {
        log.info("删除收货地址id:{}", id);
        userAddressService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 收获地址回显
     *
     * @param token
     * @return
     */
    @GetMapping("/person/addr")
    public Result<List<UserAddress>> getAddr(@RequestHeader("Authorization") String token) {
        log.info("收货地址回显");
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId);
        queryWrapper.orderByDesc(UserAddress::getId);
        List<UserAddress> list = userAddressService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 反馈问题
     *
     * @param token
     * @param maintainFeedback
     * @return
     */
    @PostMapping("/feedback")
    public Result<String> sendFeedback(@RequestHeader("Authorization") String token, @RequestBody MaintainFeedback maintainFeedback) {
        log.info("发送反馈内容");
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        maintainFeedback.setUserId(userId);
        maintainFeedbackService.save(maintainFeedback);
        return Result.success("反馈成功，我们会继续改进！");
    }

    /**
     * 订单查询
     *
     * @param token
     * @return
     */
    @GetMapping("/getOrder")
    public Result<List<OrderAppDto>> getOrder(@RequestHeader("Authorization") String token) {
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        log.info("个人订单查询:{}", userId);
        //构建wrapper，得到对象list
        LambdaQueryWrapper<ShopOrders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShopOrders::getUserId, userId);
        queryWrapper.orderByDesc(ShopOrders::getCreateTime);
        List<ShopOrders> list = shopOrdersService.list(queryWrapper);
        List<OrderAppDto> dtoList = new ArrayList<>();
        //遍历list，把图片放进dto
        for (ShopOrders shopOrders : list) {
            OrderAppDto orderAppDto = new OrderAppDto(shopOrders);
            //据产品id得到产品图片
            Integer prodId = shopOrders.getProductId();
            ShopProduct prod = shopProductService.getById(prodId);
            String imgUrl = prod.getImgUrl();
            String prodName = prod.getName();
            //设置dto中imgUrl
            orderAppDto.setProductName(prodName);
            orderAppDto.setImgUrl(imgUrl);
            dtoList.add(orderAppDto);
        }
        return Result.success(dtoList);
    }

    /**
     * 简化版用户实名认证
     *
     * @return
     */
    @PostMapping("/user/auth")
    public Result<String> setRealName(@RequestHeader("Authorization") String token) {
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        userService.updateAuth(userId);
        RedisUtils.loadData(token);
        return Result.success("实名成功");
    }

    /**
     * 查询实名信息
     *
     * @param token
     * @return
     */
    @GetMapping("/user/getAuth")
    public Result<Integer> getRealName(@RequestHeader("Authorization") String token) {
        String username = (String) JwtUtils.parseJWT(token).get("userName");
        Integer userId = userService.getIdByUserName(username);
        User user = userService.getById(userId);
        Integer status = user.getIsRealname();
        return Result.success(status);
    }

    /**
     * 存入识别记录
     */
    @PostMapping("/setRec")
    public Result<String> saveRec(@RequestHeader(value = "Authorization",required = false) String token,
                                  @RequestParam String name,
                                  @RequestParam String equipment,
                                  @RequestBody Base64Dto base64Dto) {

        log.info("添加识别记录");
        IdentifyRecord identifyRecord = new IdentifyRecord();
        identifyRecord.setName(name);
        identifyRecord.setEquipment(equipment);
        if(StringUtils.isEmpty(token)){
            //游客id设置为888
            identifyRecord.setUserId(888);
        }
        else {
            String username = (String) JwtUtils.parseJWT(token).get("userName");
            Integer userId = userService.getIdByUserName(username);
            identifyRecord.setUserId(userId);
        }
        //解析base64为图片，存入识别记录图片
        String url = aliOSSUtils.uploadBytes(base64Dto.getCode());
        identifyRecord.setImgUrl(url);
        identifyRecordService.save(identifyRecord);
        return Result.success("识别记录存入成功");
    }



    /**
     * 查商品和图鉴
     */
    @PostMapping("/getX")
    public Result<List<Map<String, DataInfo>>> getX(@RequestParam String text) {
        log.info("查询图鉴信息和推荐商品");
        if(text.contains("健康")){
            return Result.error("植物健康，无需查询图鉴及商品");
        }
        List<Map<String, DataInfo>> list = new ArrayList<>();
        List<Map<String, List<ShopProduct>>> mapList = new ArrayList<>();
        String[] str = text.split("、");
        for(String name:str){
            //查图鉴
            Map<String,DataInfo> map = new HashMap<>();
            DataInfo dataInfo = dataInfoService.getDataInfo(name);
            map.put(name,dataInfo);
            list.add(map);
            //查商品
            Map<String, List<ShopProduct>> shopMap = new HashMap<>();
            List<ShopProduct> shopProductList = shopProductService.list(new LambdaQueryWrapper<ShopProduct>()
                    .eq(ShopProduct::getPushKey,name));
            shopMap.put(name,shopProductList);
            mapList.add(shopMap);
        }
        return Result.success(list).add(mapList);
    }


}
