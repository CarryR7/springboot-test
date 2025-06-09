package com.focus.springboottest.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class NavicatConnectionParser {

    public static void main(String[] args) {
        try {
            // 读取 XML 文件（替换为实际文件路径）
            File inputFile = new File("C:\\Users\\luo\\Desktop\\connections.ncx");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // 获取所有 <Connection> 节点
            NodeList connectionList = doc.getElementsByTagName("Connection");

            for (int i = 0; i < connectionList.getLength(); i++) {
                Node node = connectionList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    ConnectionInfo info = parseConnectionElement(element);
                    printConnectionInfo(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析单个 <Connection> 节点
     */
    private static ConnectionInfo parseConnectionElement(Element element) throws Exception {
        ConnectionInfo info = new ConnectionInfo();
        info.setName(element.getAttribute("ConnectionName"));
        info.setHost(element.getAttribute("Host"));
        info.setPort(element.getAttribute("Port"));
        info.setUsername(element.getAttribute("UserName"));
        info.setPassword(NavicatPasswordDecryptor.decrypt(element.getAttribute("Password")));
        return info;
    }

    /**
     * 打印连接信息
     */
    private static void printConnectionInfo(ConnectionInfo info) {
        System.out.println("---------------------------");
        System.out.println("连接名称: " + info.getName());
        System.out.println("主机: " + info.getHost());
        System.out.println("端口: " + info.getPort());
        System.out.println("用户名: " + info.getUsername());
        System.out.println("密码（加密）: " + info.getPassword());
        System.out.println("---------------------------");
    }

    /**
     * 连接信息实体类
     */
    static class ConnectionInfo {
        private String name;
        private String host;
        private String port;
        private String username;
        private String password;

        // Getter 和 Setter
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        public String getPort() { return port; }
        public void setPort(String port) { this.port = port; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
