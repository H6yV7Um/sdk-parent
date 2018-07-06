package com.jlkj.sdk.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.jlkj.sdk.wechat.util.JsonUtil;
import com.jlkj.sdk.wechat.util.WechatUtil;
import com.jlkj.sdk.wechat.vo.TemplateInfoVO;
import com.jlkj.sdk.wechat.vo.TemplateVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 11:57 2018/5/23
 * @Description
 */
public class WechatService {

    /**
     * 日志类
     */
    private static final Logger logger = LoggerFactory.getLogger(WechatService.class);

    /**
     * 获取微信AccessToken的值
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getWxAccessToken(String appId, String appSecret){
        if(StringUtils.isBlank(appId)) {
            throw new RuntimeException("[appId]不能为空");
        }
        if(StringUtils.isBlank(appSecret)) {
            throw new RuntimeException("[appSecret]不能为空");
        }
        Map<String, String> resultMap = WechatUtil.getAccessToken(appId,appSecret);
        if(null == resultMap){
            return null;
        }
        return resultMap.get("access_token");
    }

    /**
     * 获取用户账号微信绑定的模板列表
     * @param accessToken
     * @param offset
     * @param count
     * @return
     */
    public static List<TemplateInfoVO>  getListAccountTemplate(String accessToken,int offset,int count) {
        if(StringUtils.isBlank(accessToken)) {
            throw new RuntimeException("[accessToken]不能为空");
        }
        Map<String, Object> resultMap = WechatUtil.getAccountTemplate(accessToken,offset,count);
        if (String.valueOf(resultMap.get("errcode")).equals("0")) {
            logger.info("========>>获取账号模板列表成功！");
        } else {
            logger.info("========>>获取账号模板列表失败，失败原因：{}", resultMap);
            throw new RuntimeException("获取账号模板列表失败");
        }
        //获取模板列表
        String jsonString = JsonUtil.toJson(resultMap.get("list"));
        List<TemplateInfoVO> templateList = JSONObject.parseArray(jsonString, TemplateInfoVO.class);
        if(null == templateList || templateList.isEmpty()) {
            return null;
        }
        return templateList;
    }

    /**
     * 发送模板信息
     * @param accessToken
     */
    public static Boolean sendAccountTemplate(String accessToken, TemplateVO templateVO) {
        if(null == templateVO){
            throw new RuntimeException("[templateVO]不能为空");
        }
        if(StringUtils.isBlank(accessToken)) {
            throw new RuntimeException("[accessToken]不能为空");
        }
        //发送模板信息
        Map<String, Object> resultMap = WechatUtil.sendMessageTemplate(accessToken,templateVO);
        if (String.valueOf(resultMap.get("errcode")).equals("0")) {
            logger.info("==================模板发送成功！");
            return true;
        } else {
            logger.info("==================模板发送失败!原因：{}", resultMap);
            return false;
        }

    }
}
