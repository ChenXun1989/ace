package wiki.chenxun.ace.admin.service;

import wiki.chenxun.ace.admin.entity.ConfigEntity;
import wiki.chenxun.ace.admin.entity.Page;

/**
 * @Description: Created by chenxun on 2017/4/23.
 */
public interface ConfigManageService {

    Page<ConfigEntity> page();
}
