package com.jlkj.sdk.pay.wechat.utils;


import com.jlkj.sdk.pay.wechat.WeChatPayConstants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 16:33 2018/3/27
 * @Description XML工具类
 */
public class XmlDom4jParserUtil {

    /**
     * 只有一级子节点xml数据解析，并转换成Map
     * 例如:
     * <notify>
     * <payment_type>1</payment_type>
     * <subject>收银台{1283134629741}</subject>
     * </notify>
     *
     * @param xmlStr
     * @return
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
        Map<String, String> rstMap = new HashMap<>();
        try {
            Document document = DocumentHelper.parseText(xmlStr.trim());
            Element root = document.getRootElement();
            // 获取根节点下的子节点
            Iterator<?> iter = root.elementIterator();
            while (iter.hasNext()) {
                Element child_1 = (Element) iter.next();
                rstMap.put(child_1.getName(), child_1.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return rstMap;
    }

    /**
     * map转变为xml
     * @param data
     * @return
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名类型
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key, String signType) throws Exception {
        String sign = WechatCoreUtil.generateSignature(data, key, signType);
        data.put(WeChatPayConstants.FIELD_SIGN, sign);
        return mapToXml(data);
    }

}

