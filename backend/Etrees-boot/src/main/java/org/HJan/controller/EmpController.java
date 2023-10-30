package org.HJan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Log;
import org.HJan.common.Result;
import org.HJan.entity.Emp;
import org.HJan.entity.Permission;
import org.HJan.entity.PermissionEmp;
import org.HJan.service.EmpService;
import org.HJan.service.MenuService;
import org.HJan.service.PermissionEmpService;
import org.HJan.service.PermissionService;
import org.HJan.utils.JwtUtils;
import org.HJan.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 员工管理Controller
 */
@RestController
@Slf4j
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionEmpService permissionEmpService;



    /**
     * 分页条件查询
     * @param page     页码
     * @param pageSize 每页查询记录数
     * @param empName     条件查询-姓名
     * @param id    条件查询-工号
     */
    @GetMapping("/{page}/{pageSize}")
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    public Result<IPage<Emp>> page(@PathVariable int page,
                                   @PathVariable int pageSize,
                                   @RequestParam(required = false) String empName,
                                   @RequestParam(required = false) Integer id) {
        log.info("分页查询：page = {},pageSize={},name={},empId={}", page, pageSize, empName, id);
        //根据page和pageSize生成key
        String key = "empPrefix:" + page + pageSize + empName + id;
//        先去redis 中查找是否存在要返回的emp信息
        IPage<Emp> empMsg = (IPage<Emp>) redisTemplate.opsForValue().get(key);
        if (empMsg != null) {
            return Result.success(empMsg);
        }
        //构造分页构造器
        Page<Emp> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Emp> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤-
        queryWrapper.like(StringUtils.isNotEmpty(empName), Emp::getEmpName, empName);
        queryWrapper.eq(id != null, Emp::getId, id);
        //排序条件
        queryWrapper.orderByAsc(Emp::getId);
        //执行查询
        empService.page(pageInfo, queryWrapper);
        //将emp信息存入redis , 存在时间 120分钟
//        redisTemplate.opsForValue().set(key,pageInfo,120,TimeUnit.MINUTES);
        return Result.success(pageInfo);
    }

    /**
     * 新增员工
     * @param emp
     * @return string
     */
    @PostMapping
    @Log(value="添加员工")
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    public Result<String> save(@RequestBody Emp emp) {
        log.info("添加员工：{}", emp.toString());
        //添加默认密码
        emp.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间
        emp.setRegisterTime(LocalDateTime.now());

        empService.save(emp);
        //清空redis分页缓存
        RedisUtils.loadData("empPrefix");
        return Result.success("添加成功");
    }

    /**
     * 删除员工
     *
     * @param
     * @return
     */
    @Log(value="删除员工")
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        log.info("根据id删除员工：{}", id);
        permissionEmpService.deleteByEmpId(id);
        empService.delete(id);
        RedisUtils.loadData("empPrefix:");
        return Result.success("删除成功");
    }

    /**
     * 批量删除员工
     * @param ids
     * @return
     */
    @Log(value = "批量删除员工")
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    @PostMapping("/delete")
    public Result<String> deleteEmp(@RequestBody List<String> ids) {
        log.info("删除员工");
        //据购物车id删除记录
        empService.deleteInfo(ids);
        return Result.success("删除成功");
    }

    /**
     * 修改员工信息-查询回显
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    @GetMapping("/{id}")
    public Result<Emp> getById(@PathVariable Integer id) {
        log.info("根据id查询回显：{id}");
        Emp emp = empService.myGetById(id);
        if (emp != null) {
            return Result.success(emp);
        }
        return Result.error("找不到员工信息");
    }

    /**
     * 获取员工个人信息
     * @param token
     * @return
     */
    @GetMapping("/self")
    public Result<Emp> getInfo(@RequestHeader("Authorization") String token) {
        //根据token只可从redis中获取信息
        Emp emp =(Emp)redisTemplate.opsForValue().get(token);
        return Result.success(emp);
    }

    /**
     * 修改员工信息
     *
     * @param emp
     * @return
     */
    @Log(value="修改员工")
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    @PutMapping
    public Result<String> update(@RequestBody Emp emp) throws InterruptedException {
        log.info("修改信息：{}", emp);
        //检查是否重名
        if (!empService.check(emp)) {
            return Result.error("用户名已存在");
        }
        // 查询指定 ID 的原始数据，并获取版本号
        Emp originalData = empService.getById(emp.getId());
        Timestamp originalVersion = originalData.getVersion();
        // 将修改数据对象中的版本号设置为原始数据版本号
        emp.setVersion(originalVersion);
        // 更新数据时添加版本号判断
        boolean success = empService.update(emp, new UpdateWrapper<Emp>()
                .eq("id", emp.getId())
                .eq("version", originalVersion));
        if (success) {
            RedisUtils.loadData("empPrefix:");
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败，数据已被其他用户修改。");
        }
    }

    /**
     * 启用/禁用员工
     * @param id
     * @return
     */
    @Log(value="启用/禁用员工")
    @PreAuthorize("hasAnyAuthority('sys:emp:manager','emp:list')")
    @PostMapping("/status/{id}")
    public Result<String> setStatus(@PathVariable Integer id) {
        log.info("更改用户:{} 账号状态",id);
        Emp emp = empService. getById(id);
        if(emp.getStatus()==1){
            emp.setStatus(0);
        }else {
            emp.setStatus(1);
        }
        log.info("更改状态{}",emp);
        empService.updateById(emp);
        RedisUtils.loadData("empPrefix:");
        return Result.success("修改成功");
    }


    /**
     * 修改密码
     * @param data
     * @return
     */
    @Log(value="修改密码")
    @PreAuthorize("hasAnyAuthority('sys:home')")
    @PostMapping("/password")//传入历史密码进行比对，如果一致，则允许修改为新密码
    public Result<String> updatePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> data) {
        log.info("修改密码");
        if (Objects.equals(data.get("password"), data.get("newpass"))) {
            return Result.error("新密码不能和旧密码相同");
        }
        //获得当前登录状态用户ID
        Integer id = (Integer) JwtUtils.parseJWT(token).get("id");
        //从数据库查找该员工信息
        Emp emp = empService.getById(id);
        //校验原密码是否正确
        String password = (String) data.get("password");
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!emp.getPassword().equals(password)) {
            return Result.error("原密码输入错误");
        }
        //密码校验成功,将新密码加密
        String s = (String) data.get("newpass");
        String newpass = DigestUtils.md5DigestAsHex(s.getBytes());
        emp.setPassword(newpass);
        //允许修改密码
        boolean b = empService.updateById(emp);
        if (!b) {
            return Result.error("修改失败");
        }
        return Result.success("密码修改成功");
    }

    /**
     * 找回密码
     * @param map
     * @return
     */
    @Log(value="找回密码")
    @PreAuthorize("hasAnyAuthority('sys:home')")
    @PostMapping("/findPassword")
    public Result<String> findPassword(@RequestHeader("Authorization") String token, @RequestBody Map map) {
        log.info("找回密码:" + map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //获取新密码
        String newPassword = map.get("newPassword").toString();
        //从redis中获取保存的验证码
        String saveCode = stringRedisTemplate.opsForValue().get("verifi:" + phone);
        if (code != null && saveCode.equals(code)) {

            Integer id = (Integer) JwtUtils.parseJWT(token).get("id");
            Emp emp = empService.getById(id);
            emp.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
            empService.updateById(emp);
            return Result.success("修改密码成功");
        }
        return Result.error("验证码错误");
    }

    /**
     * 用户权限查询
     * @param id
     * @return
     */
    @GetMapping("/perms/{id}")
    @PreAuthorize("hasAuthority('emp:perms')")
    public Result<String> getPerms(@PathVariable Integer id){
        log.info("返回权限列表");
        //根据id获得所有权限列表
        if(id == 10001){
            return Result.error("不可查询admin权限");
        }
        List<Permission> permissionList = permissionService.list(new QueryWrapper<Permission>().inSql("id","SELECT permission_id FROM permission_emp WHERE emp_id =" + id));

        //根据permission获取对应菜单权限
        Set<Integer> menuIdSet = new HashSet<>();
        for(Permission permission:permissionList){
            Integer permId = permission.getId();
            Integer menuId = permissionService.getIdList(permId);
            menuIdSet.add(menuId);
        }
        List<Integer> menuList = new ArrayList<>(menuIdSet);
        //排序
        Collections.sort(menuList);
        return Result.success(menuList.toString());
    }

    /**
     * 更新权限
     * @param id
     * @param menuIds
     * @return
     */
    @Log(value="更新权限")
    @PutMapping("/addPerms/{id}")
    @PreAuthorize("hasAuthority('emp:perms')")
    public Result<String> updatePermsByid(@PathVariable Integer id ,@RequestBody List<String> menuIds) {
        log.info("设置权限为：{}", menuIds.toString());
        //删除原有权限
        permissionEmpService.deleteInfo(id);
        Set<String> menuIdSet = new HashSet<>(menuIds);
        for(String menuId: menuIds){
            Integer pId = Integer.valueOf(menuService.getPId(menuId));
            if(pId!=0) {
                menuIdSet.add(String.valueOf(pId));
            }
        }
        //设置权限
        for (String menuId : menuIdSet) {
            Integer thisMenuId = Integer.parseInt(menuId);
            Integer permId = permissionService.getPermByMenuId(thisMenuId);
            //若不存在该权限，则添加
            if(permissionEmpService.findPerms(permId,id)) {
                //新建PermissionEmp对象用于添加进表
                PermissionEmp permissionEmp = new PermissionEmp();
                //设置EmpId字段
                permissionEmp.setEmpId(id);
                permissionEmp.setPermissionId(permId);
                permissionEmpService.save(permissionEmp);
            }
        }
        return Result.success("添加成功");
    }


}







