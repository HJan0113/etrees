package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.Menu;

import java.util.List;

/**
* @author HJsir
* @description 针对表【menu】的数据库操作Service
* @createDate 2023-05-16 15:01:39
*/
public interface MenuService extends IService<Menu> {

    List<Menu> buildTreeMenu(List<Menu> menuList);


    String getPId(String menuId);
}
