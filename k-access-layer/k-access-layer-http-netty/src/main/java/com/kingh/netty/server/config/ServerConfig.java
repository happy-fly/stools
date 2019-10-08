package com.kingh.netty.server.config;

import com.kingh.config.env.EnvProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器核心配置
 *
 * @author 孔冠华
 */
public class ServerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    /**
     * 配置文件路径
     */
    private static final String CONFIG_FILE = EnvProperties.getEnv().getServerConfig();

    private static Map<String, ActionBean> ACTIONS = new HashMap<>();

    static {
        // 解析server.xml
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(ServerConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            int len = nodeList.getLength();
            for (int i = 0; i < len; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String elementName = node.getNodeName();

                    // 处理actions标签
                    if ("actions".equals(elementName)) {
                        buildActions((Element) node);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Map<String, ActionBean> getActions() {
        return ACTIONS;
    }

    private static void buildActions(Element actions) {
        NodeList nodeList = actions.getChildNodes();
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String elementName = node.getNodeName();
                if ("action".equals(elementName)) {
                    Element action = (Element) node;
                    String name = action.getAttribute("name");
                    String url = action.getAttribute("url");
                    String className = action.getAttribute("class");
                    ACTIONS.put(url, new ActionBean(name, url, className));
                    logger.info("Url:" + url + " Mapped:" + className);
                }
            }
        }
    }

    public static void main(String[] args) {
        new ServerConfig();
    }

    public static void init() {
        try {
            Class.forName(ServerConfig.class.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
