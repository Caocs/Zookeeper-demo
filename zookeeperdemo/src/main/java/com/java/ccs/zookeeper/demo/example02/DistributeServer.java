package com.java.ccs.zookeeper.demo.example02;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author caocs
 * @date 2021/11/13
 * <p>
 * 模拟服务器动态上下线
 * 1.首先需要在Zookeeper中创建/servers节点。
 */
public class DistributeServer {
    private static String connectString = "localhost:2181,localhost:2182,localhost:2183";
    private static int sessionTimeout = 2000;
    private ZooKeeper zk = null;
    private String parentNode = "/servers";

    // 创建到 zk 的客户端连接
    public void getConnect() throws IOException {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
    }

    /**
     * 注册服务器
     *
     * @param hostname 主机名称
     * @throws Exception
     */
    public void registerServer(String hostname) throws Exception {
        /**
         * EPHEMERAL_SEQUENTIAL:临时有顺序的节点
         */
        String create = zk.create(
                parentNode + "/server",
                hostname.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
        System.out.println(hostname + " is online: " + create);
    }

    /**
     * 模拟执行业务逻辑，实际上就是sleep
     *
     * @param hostname
     * @throws Exception
     */
    public void business(String hostname) throws Exception {
        System.out.println(hostname + " is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * @param args 输入服务器名称
     */
    public static void main(String[] args) throws Exception {
        // 1 获取 zk 连接
        DistributeServer server = new DistributeServer();
        server.getConnect();
        // 2 利用 zk 连接注册服务器信息
        server.registerServer(args[0]);
        // 3 启动业务功能
        server.business(args[0]);
    }
}
