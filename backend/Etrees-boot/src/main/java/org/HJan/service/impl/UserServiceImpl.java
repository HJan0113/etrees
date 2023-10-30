package org.HJan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.User;
import org.HJan.mapper.UserMapper;
import org.HJan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User getByUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(username!=null,User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    /**
     * 判断是否有重名用户
     * @param username
     * @return
     */
    @Override
    public boolean check(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,username);
        if(userMapper.selectOne(wrapper)==null){
            return true;
        }
        return false;
    }

    @Override
    public Integer getIdByUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(username!=null,User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        Integer id = Math.toIntExact(user.getId());
        return id;
    }

    @Override
    public User selectOne(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public void updateAuth(Integer id) {
        userMapper.auth(id);
    }

    @Override
    public boolean checkAuth(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user.getIsRealname()==1){
            return true;
        }
        return false;
    }

}
