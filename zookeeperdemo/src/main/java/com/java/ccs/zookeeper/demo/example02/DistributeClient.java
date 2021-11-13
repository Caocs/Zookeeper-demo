package com.java.ccs.zookeeper.demo.example02;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caocs
 * @date 2021/11/13
 */
public class DistributeClient {

    private static String connectString = "localhost:2181,localhost:2182,localhost:2183";
    private static int sessionTimeout = 2000;
    private ZooKeeper zk = null;
    private String parentNode = "/servers";

    // 创建到 zk 的客户端连接
    public void getConnect() throws IOException {
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    watchServerNodeList(); // 再次启动监听
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 监听父节点下的节点变化
     * 如果发生变化，则获取新的服务器列表信息，存放在servers列表中并打印
     */
    public void watchServerNodeList() throws Exception {
        // 1 获取服务器子节点信息，并且对父节点进行监听
        List<String> children = zk.getChildren(parentNode, true);
        // 2 存储服务器信息列表
        ArrayList<String> servers = new ArrayList<>();
        // 3 遍历所有节点，获取节点中的主机名称信息
        for (String child : children) {
            byte[] data = zk.getData(parentNode + "/" + child,
                    false, null);
            servers.add(new String(data));
        }
        // 4 打印服务器列表信息
        System.out.println(servers);
    }

    /**
     * 模拟业务代码，sleep。
     */
    public void business() throws Exception {
        System.out.println("client is working ...");
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        DistributeClient client = new DistributeClient();
        // 1 获取 zk 连接
        client.getConnect();
        // 2 获取 servers 的子节点信息，从中获取服务器信息列表
        client.watchServerNodeList();
        // 3 业务进程启动
        client.business();
    }
}

