package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.Menu;
import org.HJan.mapper.MenuMapper;
import org.HJan.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HJsir
 * @description 针对表【menu】的数据库操作Service实现
 * @createDate 2023-05-16 15:01:39
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    @Transactional
    public List<Menu> buildTreeMenu(List<Menu> menuList) {
        List<Menu> resultMenuList = new ArrayList<>();
        for (Menu menu : menuList) {
            //寻找子节点
            for(Menu e:menuList){
                if(e.getPid()==menu.getId()){
                    menu.getChildren().add(e);
                }
            }
            if (menu.getPid() == 0) {
                resultMenuList.add(menu);
            }
        }
        return menuList;
    }

    @Override
    public String getPId(String menuId) {
        Menu menu = menuMapper.selectById(menuId);
        return String.valueOf(menu.getPid());
    }
}




