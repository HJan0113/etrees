package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.User;

public interface UserService extends IService<User> {

    User getByUserName(String username);

    boolean check(String username);

    Integer getIdByUserName(String username);

    User selectOne(String phone);

    void updateAuth(Integer id);

    boolean checkAuth(Integer userId);
}
