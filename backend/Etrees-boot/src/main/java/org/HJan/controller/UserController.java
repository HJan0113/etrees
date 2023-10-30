package org.HJan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Log;
import org.HJan.common.Result;
import org.HJan.entity.User;
import org.HJan.service.UserService;
import org.HJan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


/**
 * 用户管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户信息分页查询
     * @param page
     * @param pageSize
     * @param userName
     */
    @PreAuthorize("hasAnyAuthority('sys:user:manager','user:list')")
    @GetMapping("/{page}/{pageSize}")
    public Result<IPage<User>> getMsgByLimit(@PathVariable Integer page,
                                             @PathVariable Integer pageSize,
                                             @RequestParam(required = false) String userName,
                                             @RequestParam(required = false) Integer id) {
        log.info("分页查询：page = {},pageSize={},name={},empId={}", page, pageSize, userName, id);
        //根据page和pageSize生成key
        String key = "userPrefix:" + page + pageSize + userName + id;
        //先去redis 中查找
        IPage<User> userMsg = (IPage<User>) redisTemplate.opsForValue().get(key);
        if (userMsg != null) {
            return Result.success(userMsg);
        }
        //构造分页构造器
        IPage<User> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤
        queryWrapper.like(StringUtils.isNotEmpty(userName), User::getUserName, userName);
        queryWrapper.eq(id != null, User::getId, id);
        //排序条件
        queryWrapper.orderByDesc(User::getId);
        //执行查询
        userService.page(pageInfo, queryWrapper);
        //将数据存入redis , 有效期 120分钟
        redisTemplate.opsForValue().set(key,pageInfo,120,TimeUnit.MINUTES);
        return Result.success(pageInfo);
    }

    /**
     * 后台 启用/禁用 用户
     * @param id
     * @return
     */
    @Log(value="用户-启/禁用用户")
    @PostMapping("/status/{id}")
    @PreAuthorize("hasAnyAuthority('sys:user:manager','user:list')")
    public Result<String> setStatus(@PathVariable Integer id) {
        log.info("更改用户:{} 账号状态",id);
        User user = userService.getById(id);
        if(user.getStatus()==1){
            user.setStatus(0);
        }else {
            user.setStatus(1);
        }
        log.info("更改状态{}",user);
        userService.updateById(user);
        RedisUtils.loadData("userPrefix:");
        return Result.success("修改成功");
    }


}
