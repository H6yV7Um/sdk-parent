package com.jlkj.sdk.pay.wechat.utils;

import com.jlkj.sdk.pay.wechat.WeChatPayConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 16:19 2018/3/27
 * @Description 微信核心工具类
 */
public class WechatCoreUtil {

    /**
     * 生成随机字符串，不长于32位。推荐随机数生成算法
     *
     * @param length
     * @return 随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, String signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WeChatPayConstants.FIELD_SIGN)) {
                continue;
            }
            String kValue = data.get(k);
            if (null != kValue && data.get(k).trim().length() > 0)   //参数值为空，则不参与签名
                sb.append(k).append("=").append(kValue.trim()).append("&");
        }
        sb.append("key=").append(key);
        if (WeChatPayConstants.MD5.equals(signType)) {
            return SignUtil.MD5Encode(sb.toString()).toUpperCase();
        }
        else if (WeChatPayConstants.HMACSHA256.equals(signType)) {
            return SignUtil.HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, String signType) throws Exception {
        if (!data.containsKey(WeChatPayConstants.FIELD_SIGN) ) {
            System.out.println("==================在验证签名的数据里，未检测到sign");
            return false;
        }
        if(StringUtils.isBlank(signType)){
            signType = WeChatPayConstants.MD5;
        }
        String sign = data.get(WeChatPayConstants.FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 返回发送的信息
     * @param return_code
     * @param return_msg
     * @return
     */
    public static String setXml(String return_code, String return_msg) {
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("return_code", return_code);
        parameters.put("return_msg", return_msg);
        return "<xml><return_code><![CDATA[" + return_code + "]]>" +
                "</return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}
