package com.jlkj.sdk.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jlkj.sdk.wechat.vo.TemplateVO;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 14:53 2018/3/22
 * @Description 微信操作工具类
 */
public class WechatUtil {

    /**
     * 日志类
     */
    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取openId的微信请求url
     */
    private static final String url = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     *
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 发送模板消息URL,发送的地方服务通知
     */
    private static final String SEND_MESSAGE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";

    /**
     * 获取帐号下已存在的模板列表
     */
    private static final String GET_ACCOUNT_TEMPLATE_LIST_URL = "https://api.weixin.qq.com/cgi-bin/wxopen/template/list?access_token=";


    /**
     * code 换取 sessionKey & openid & 满足一定条件可以获取unionId
     *
     * @param appId
     * @param appSecret
     * @param code      前段调用微信获取到的微信code
     * @return
     */
    public static Map<String, String> getSessionKeyAndOpenId(String appId, String appSecret, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("js_code", code);
        String response = HttpUtil.get(url, params);
        Map<String, String> respMap = JsonUtil.parseJson(response, Map.class);
        logger.info("getWxAccessToken, response : {}", response);
        return respMap;
    }

    /**
     * 通过获取微信“AccessToken"
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static Map<String, String> getAccessToken(String appId, String appSecret) {
        //创建请求参数
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("grant_type", "client_credential"));
        pairs.add(new BasicNameValuePair("appid", appId));
        pairs.add(new BasicNameValuePair("secret", appSecret));
        String response = HttpUtil.doGetSSL(ACCESS_TOKEN_URL, pairs);
        logger.info("=============>>获取微信全局TOKEN,请求参数:{}", response);
        Map<String, String> respMap = JsonUtil.parseJson(response, Map.class);
        logger.info("=============>>获取微信全局TOKEN,请求结果:{}", respMap);
        return respMap;
    }

    /**
     * 获取账号在微信绑定过得模板列表
     * @param accessToken
     * @param offset
     * @param count
     * @return
     */
    public static Map<String, Object> getAccountTemplate(String accessToken,int offset,int count) {
        String URL = GET_ACCOUNT_TEMPLATE_LIST_URL + accessToken;
        logger.info("=============>>获取绑定的模板列表URL:{}", URL);
        logger.info("=============>>获取绑定的模板列表参数:offset：{},count：{}", offset,count);

        //封装请求参数
        JSONObject object = new JSONObject();
        object.put("offset",offset);
        object.put("count",count);
        String response = HttpUtil.post(URL, object);
        Map<String, Object> respMap = JsonUtil.parseJson(response, Map.class);
        logger.info("=============>>发送服务通知请求结果:{}", respMap);
        return respMap;
    }


    /**
     * 发送模板信息 文档参考地址：https://developers.weixin.qq.com/miniprogram/dev/api/notice.html#%E5%8F%91%E9%80%81%E6%A8%A1%E6%9D%BF%E6%B6%88%E6%81%AF
     *
     * @param accessToken
     * @return
     */
    public static Map<String, Object> sendMessageTemplate(String accessToken,TemplateVO templateVO) {
        String URL = SEND_MESSAGE_TEMPLATE_URL + accessToken;
        logger.info("=============发送服务通知URL:{}", URL);

        String jsonObject = JsonUtil.toJson(templateVO);
        logger.info("=============发送服务通知数据信息:{}", jsonObject);

        String response = HttpUtil.post(URL, JSON.parseObject(jsonObject));
        Map<String, Object> respMap = JsonUtil.parseJson(response, Map.class);
        logger.info("=============发送服务通知请求结果:{}", respMap);

        return respMap;
    }


}
