package com.java.ccs.zookeeper.demo.example01;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author caocs
 * @date 2021/11/13
 * <p>
 * 客户端API
 */
public class ZkClient {


    private ZooKeeper zkClient = null;

    /**
     * 初始化Zookeeper客户端
     */
    @Before
    public void init() throws Exception {
        // 注意：逗号前后不能有空格
        String connectString = "localhost:2181,localhost:2182,localhost:2183";
        int sessionTimeout = 2000;

        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

            public void process(WatchedEvent watchedEvent) {
                // 收到事件通知后的回调函数（用户的业务逻辑）
                System.out.println(watchedEvent.getType() + "--" + watchedEvent.getPath());
                // 再次启动监听
                try {
                    List<String> children = zkClient.getChildren("/", true);
                    for (String child : children) {
                        System.out.println(child);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 测试创建节点
     */
    @Test
    public void testCreateNode() throws KeeperException, InterruptedException {
        /**
         * 参数 1：要创建的节点的路径；
         * 参数 2：节点数据；
         * 参数 3：节点权限；
         * 参数 4：节点的类型
         */
        String nodeCreated = zkClient.create("/ccs", "Shuai".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 获取子节点
     */
    @Test
    public void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        // 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 判断 ZNode 是否存在
     */
    @Test
    public void exist() throws Exception {
        Stat stat = zkClient.exists("/atguigu", false);
        System.out.println(stat == null ? "not exist" : "exist");
    }


}




