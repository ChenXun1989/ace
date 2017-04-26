package wiki.chenxun.ace.admin.service.impl;

import org.springframework.stereotype.Component;
import wiki.chenxun.ace.admin.entity.ConfigEntity;
import wiki.chenxun.ace.admin.entity.Page;
import wiki.chenxun.ace.admin.service.ConfigManageService;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/23.
 */
@AceService(path = "/config")
@Component
public class ConfigManageServiceImpl implements ConfigManageService {


    @Get
    @Override
    public Page<ConfigEntity> page() {
        Page<ConfigEntity> page = new Page<>();
        page.setTotal(20);
        List<ConfigEntity> list = new ArrayList<>();
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setName("test");
        configEntity.setTime(new Date());
        list.add(configEntity);
        page.setRows(list);
        return page;
    }


}
