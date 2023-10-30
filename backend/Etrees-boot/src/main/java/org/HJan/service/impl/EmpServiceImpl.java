package org.HJan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.HJan.entity.Emp;
import org.HJan.entity.Menu;
import org.HJan.entity.Permission;
import org.HJan.mapper.EmpMapper;
import org.HJan.mapper.MenuMapper;
import org.HJan.mapper.PermissionMapper;
import org.HJan.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public boolean check(Emp emp) {
        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(emp!=null,Emp::getEmpName,emp.getName());
        Emp emp2 = empMapper.selectOne(wrapper);
        if(emp2==null){
            return true;
        }
        return false;
    }

    @Override
    public String getEmpPassword(Integer empId) {
        return empMapper.getPassword(empId);
    }

    @Override
    @Transactional
    public boolean isRegister(String phone) {
        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Emp::getPhone,phone);
        if(empMapper.selectOne(wrapper)!=null)return true;
        else return false;

    }

    @Override
    public Emp getEmp(String empName) {
        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(empName),Emp::getEmpName,empName);
        return empMapper.selectOne(wrapper);
    }

    @Override
    public Emp getEmpByPhone(String phone) {
        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(phone),Emp::getPhone,phone);
        return empMapper.selectOne(wrapper);
    }

    @Override
    public Emp getByUsername(String empName) {
        return getOne(new QueryWrapper<Emp>().eq("emp_name",empName));
    }

    @Override
    @Transactional
    public String getUserAuthorityInfo(Integer empId) {
        StringBuffer authority = new StringBuffer();
        //根据id获取权限(去重)
        List<Permission> permissionList = permissionMapper.selectList(new QueryWrapper<Permission>().inSql("id","SELECT permission_id FROM permission_emp WHERE emp_id =" + empId));
        Set<String> menuCodeSet = new HashSet<>();
        //根据permission获取对应菜单权限
        for(Permission permission:permissionList){
            List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().inSql("id","SELECT menu_id FROM permission WHERE id ="+permission.getId()));
            for(Menu menu:menuList){
                String perms = menu.getPerms();
                if(StringUtils.isNotEmpty(perms)){
                    menuCodeSet.add(perms);
                }
            }
        }
        //返回权限列表
        if(menuCodeSet.size()>0){
            String menuCodeStrs = menuCodeSet.stream().collect(Collectors.joining(","));
            authority.append(menuCodeStrs);
        }
        System.out.println("authority:"+authority.toString());
        return authority.toString();
    }

    @Override
    public void deleteInfo(List<String> ids) {
        empMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        empMapper.deleteInfo(id);
    }

    @Override
    public Emp myGetById(Integer id) {
        Emp emp = empMapper.getById(id);
        return emp;
    }


}
