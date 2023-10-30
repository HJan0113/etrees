package org.HJan.common.security;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.HJan.common.Result;
import org.HJan.entity.Emp;
import org.HJan.entity.Menu;
import org.HJan.entity.Permission;
import org.HJan.entity.PermissionEmp;
import org.HJan.service.EmpService;
import org.HJan.service.MenuService;
import org.HJan.service.PermissionEmpService;
import org.HJan.service.PermissionService;
import org.HJan.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private EmpService empService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionEmpService permissionEmpService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");//响应数据格式
        ServletOutputStream outputStream = response.getOutputStream();
        Map<String, Object> claims = new HashMap<>();
        //得到登录用户名，生成jwt
        String empName = authentication.getName();
        Integer id =  empService.getByUsername(empName).getId();
        claims.put("id", id);
        String token = JwtUtils.generateJwt(claims);
        //获取当前登录角色信息
        Emp currentEmp = empService.getByUsername(empName);
        //将信息存入redis
        redisTemplate.opsForValue().set(token, currentEmp);
        //设置员工信息在redis的存储有效期 2小时
        redisTemplate.expire(token, 240,TimeUnit.HOURS);

        List<PermissionEmp> permissionEmpList = permissionEmpService.list(new QueryWrapper<PermissionEmp>().eq("emp_id", currentEmp.getId()));
        List<Integer> permissionIds = permissionEmpList.stream().map(PermissionEmp::getPermissionId).collect(Collectors.toList());
        //代码基于权限ID（permissionIds）查询对应的所有菜单（Menu），并将这些菜单ID（menuIds）存储在集合中
        if(permissionIds.isEmpty()){
            outputStream.write(JSONUtil.toJsonStr(Result.error("该用户无权限")).getBytes());
            outputStream.flush();
            outputStream.close();
        }
        List<Permission> permissionList = permissionService.list(new QueryWrapper<Permission>().in("id", permissionIds));
        Set<Integer> menuIds = permissionList.stream().map(Permission::getMenuId).collect(Collectors.toSet());
        //用菜单ID（menuIds）查询菜单信息（Menu），并按照order_num字段进行升序排列。最终生成菜单列表（menuList）。
        List<Menu> menuList = menuService.list(new QueryWrapper<Menu>().in("id", menuIds).orderByAsc("order_num"));
        //转菜单树
        List<Menu> menulist1 = menuService.buildTreeMenu(menuList);
        //创建list副本用于删除多余menulist
        List<Menu> menulist2 = new ArrayList<>(menulist1);
        for(Menu menu:menulist2){
            if(menu.getPid()!=0){
                menulist1.remove(menu);
            }
        }
        //登陆成功，返回Jwt和权限菜单列表
        outputStream.write(JSONUtil.toJsonStr(Result.success(token).add("menuList",menulist1 )).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
