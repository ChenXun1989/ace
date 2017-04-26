package wiki.chenxun.ace.admin.service;

import wiki.chenxun.ace.admin.entity.ApplicationInfo;
import wiki.chenxun.ace.admin.entity.Page;

import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/23.
 */
public interface ZookeeperManageService {

    Page<ApplicationInfo> queryAllManageNodes();
}
