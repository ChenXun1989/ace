package wiki.chenxun.ace.admin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wiki.chenxun.ace.admin.entity.ApplicationInfo;
import wiki.chenxun.ace.admin.entity.Page;
import wiki.chenxun.ace.admin.service.ZookeeperManageService;
import wiki.chenxun.ace.core.base.annotations.AceService;
import wiki.chenxun.ace.core.base.annotations.Get;
import wiki.chenxun.ace.core.base.common.AceApplicationConfig;
import wiki.chenxun.ace.core.base.config.Config;
import wiki.chenxun.ace.core.base.register.RegisterConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: Created by chenxun on 2017/4/23.
 */
@AceService(path = "/zookeeper/manage")
@Component
public class ZookeeperManageServiceImpl implements ZookeeperManageService {

    @Autowired
    private RegisterConfig registerConfig;


    @Get
    @Override
    public Page<ApplicationInfo> queryAllManageNodes() {

        Page<ApplicationInfo> page = new Page<>();
        ZooKeeper zooKeeper = null;
        try {
            String path = Config.ROOT_PATH + AceApplicationConfig.PREFIX;
            zooKeeper = new ZooKeeper(registerConfig.getUrl(), registerConfig.getTimeout(), null);
            List<String> list = zooKeeper.getChildren(path, true);
            Collections.sort(list);
            page.setTotal(list.size());
            ObjectMapper objectMapper = new ObjectMapper();
            // TODO: 分页
            List<ApplicationInfo> applicationInfoList = new ArrayList<>();
            for (String str : list) {
                byte[] data = zooKeeper.getData(path + "/" + str, true, new Stat());
                AceApplicationConfig aceApplicationConfig = objectMapper.readValue(data, AceApplicationConfig.class);
                ApplicationInfo applicationInfo = new ApplicationInfo();
                applicationInfo.setName(aceApplicationConfig.getName());
                applicationInfo.setIp(str.split(":")[0]);
                applicationInfo.setPort(str.split(":")[1]);
                applicationInfoList.add(applicationInfo);
            }
            page.setRows(applicationInfoList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } finally {
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return page;


    }
}
