package com.jlkj.sdk.pay.wechat.service;

import com.jlkj.sdk.pay.wechat.WeChatPayConstants;
import com.jlkj.sdk.pay.wechat.request_vo.UnifiedOrderReqVO;
import com.jlkj.sdk.pay.exception.PayException;
import com.jlkj.sdk.pay.wechat.utils.HttpUtil;
import com.jlkj.sdk.pay.wechat.utils.WechatCoreUtil;
import com.jlkj.sdk.pay.wechat.utils.XmlDom4jParserUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 19:21 2018/3/29
 * @Description 境内普通商户微信支付服务实现类，统一下单请求不重要证书
 */
public class WeChatPay {

    /**
     * 统一下单，支持公众号
     * @param unifiedOrderReqVO 订单请求参数。
     * @param key
     * @return
     * @throws PayException
     */
    public static Map<String,String> unifiedOrder(UnifiedOrderReqVO unifiedOrderReqVO,String key) throws PayException {

        //检查必传字段是否为空
        if(StringUtils.isBlank(unifiedOrderReqVO.getAppId())){
            throw new PayException("appId不能为空！");
        }
        if(StringUtils.isBlank(unifiedOrderReqVO.getMchId())){
            throw new PayException("macId不能为空！");
        }
        if(StringUtils.isBlank(unifiedOrderReqVO.getNonceStr())){
            throw new PayException("nonceStr不能为空！");
        }
        if(StringUtils.isBlank(unifiedOrderReqVO.getBody())){
            throw new PayException("body不能为空！");
        }
        if(StringUtils.isBlank(unifiedOrderReqVO.getOutTradeNo())){
            throw new PayException("outTradeNo不能为空！");
        }
        if(null == unifiedOrderReqVO.getTotalFee()){
            throw new PayException("totalFee不能为空！");
        }
        if(StringUtils.isBlank(unifiedOrderReqVO.getNotifyUrl())){
            throw new PayException("notifyUrl不能为空！");
        }
        if(StringUtils.isBlank(unifiedOrderReqVO.getTradeType())){
            throw new PayException("tradeType不能为空！");
        }

        //组装预支付下单请求参数
        String signType = unifiedOrderReqVO.getSignType();
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("appid",unifiedOrderReqVO.getAppId());
        requestMap.put("mch_id",unifiedOrderReqVO.getMchId());
        requestMap.put("device_info",unifiedOrderReqVO.getDeviceInfo());
        requestMap.put("nonce_str",unifiedOrderReqVO.getNonceStr());
        requestMap.put("sign_type",signType);
        requestMap.put("body",unifiedOrderReqVO.getBody());
        requestMap.put("detail",unifiedOrderReqVO.getDetail());
        requestMap.put("attach",unifiedOrderReqVO.getAttach());
        requestMap.put("out_trade_no",unifiedOrderReqVO.getOutTradeNo());
        requestMap.put("fee_type",unifiedOrderReqVO.getFeeType());
        requestMap.put("total_fee",unifiedOrderReqVO.getTotalFee().toString());
        requestMap.put("spbill_create_ip",unifiedOrderReqVO.getSpBillCreateIp());
        requestMap.put("time_start",unifiedOrderReqVO.getTimeStart());
        requestMap.put("time_expire",unifiedOrderReqVO.getTimeExpire());
        requestMap.put("goods_tag",unifiedOrderReqVO.getGoodsTag());
        requestMap.put("notify_url",unifiedOrderReqVO.getNotifyUrl());
        requestMap.put("trade_type",unifiedOrderReqVO.getTradeType());
        requestMap.put("product_id",unifiedOrderReqVO.getProductId());
        requestMap.put("limit_pay",unifiedOrderReqVO.getLimitPay());
        requestMap.put("openid",unifiedOrderReqVO.getOpenId());
        requestMap.put("scene_info",unifiedOrderReqVO.getSceneInfo());

        //数据签名并返回生成带有 sign 的 XML 格式字符串
        String requestData;
        try{
            requestData = XmlDom4jParserUtil.generateSignedXml(requestMap,key,signType);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PayException("微信生成统一下单请求参数失败！");
        }
        System.out.println("=========微信统一下单请求参数：\n" + requestData);

        //发起下单请求
        String reposeData;
        try{
            reposeData = HttpUtil.requestWithoutCert(WeChatPayConstants.UNIFIEDORDER_URL,requestData);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PayException("微信统一下单请求发送失败！");
        }
        System.out.println("=========微信统一下单请求结果：\n" + reposeData);

        //将微信返回结果转换为Map
        Map<String,String> returnMap = XmlDom4jParserUtil.xmlToMap(reposeData);
        if(null == returnMap){
            throw new PayException("解析微信统一下单请求返回结果发生错误！");
        }
        //判断请求结果
        if(returnMap.get("return_code").equals(WeChatPayConstants.FAIL)){
            throw new PayException(String.format("微信统一下单失败[错误原因：%s]",returnMap.get("return_msg")));
        }
        //判断业务处理结果
        if(returnMap.get("result_code").equals(WeChatPayConstants.FAIL)){
            throw new PayException(String.format("微信统一下单业务处理失败[错误代码：%s,错误描述:%s",
                    returnMap.get("err_code"),returnMap.get("err_code_des")));
        }
        //验证预订单请求返回的sign是否有效
        try{
            boolean isValid = WechatCoreUtil.isSignatureValid(returnMap,key,signType);
            if(!isValid){
                throw new PayException("统一下单返回结果集的签名无效！请检查相关信息");
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PayException("验证统一下单返回结果集签名是否有效发生异常！");
        }
        return returnMap;
    }

    /**
     * 获取公众号与小程序支付的预支付结果
     * @param appId
     * @param key
     * @param signType
     * @param prepayId
     * @return
     * @throws PayException
     */
    public static Map<String, String> getPublicAccountsPayJSSDKAndSmallProcedureRequestParameter(String appId, String key, String signType, String prepayId) throws PayException {
        //检查参数
        if(StringUtils.isBlank(appId)){
            throw new PayException("appId不能为空！");
        }
        if(StringUtils.isBlank(key)){
            throw new PayException("key不能为空！");
        }
        if(StringUtils.isBlank(prepayId)){
            throw new PayException("prepayId不能为空！");
        }
        HashMap<String, String> back = new HashMap<>();
        //1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。
        String time = Long.toString(System.currentTimeMillis() / 1000);
        back.put("appId", appId);
        back.put("timeStamp",time);
        back.put("nonceStr", WechatCoreUtil.getRandomStringByLength(32));
        back.put("package", "prepay_id=" + prepayId);
        back.put("signType", signType);  //签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致

        //获取签名sign
        String paySign;
        try{
            paySign = WechatCoreUtil.generateSignature(back,key,signType);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PayException("公众号获取二次签名发生错误");
        }
        back.put("paySign", paySign);
        return back;
    }

    /**
     * 获取APP支付的预支付结果
     * @param mchId
     * @param prepayId
     * @param key
     * @param signType
     * @return
     * @throws PayException
     */
    public static Map<String, String> getAppPayRequestParameter(String mchId,String prepayId,String key,String signType) throws PayException {
        if(StringUtils.isBlank(mchId)){
            throw new PayException("mchId不能为空！");
        }
        if(StringUtils.isBlank(prepayId)){
            throw new PayException("prepayId不能为空！");
        }
        if(StringUtils.isBlank(prepayId)){
            throw new PayException("key不能为空！");
        }
        HashMap<String, String> back = new HashMap<>();
        //1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。
        String time = Long.toString(System.currentTimeMillis() / 1000);
        //微信商户号
        back.put("partnerId", mchId);
        back.put("prepayId", prepayId);
        back.put("package", "Sign=WXPay");
        back.put("nonceStr", WechatCoreUtil.getRandomStringByLength(32));
        back.put("timeStamp", time);

        //得到签名
        String sign;
        try{
            sign = WechatCoreUtil.generateSignature(back,key,signType);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new PayException("APP支付获取二次签名发生错误");
        }
        back.put("sign", sign);
        return back;
    }

    /**
     * 获取H5支付页面，一定要读的文档：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_4
     * @return
     */
    public static Map<String, String> getH5PayRequestParameter(String mWebUrl,String redirectUrl) throws PayException{

        HashMap<String, String> back = new HashMap<>();
        //检查参数
        if(StringUtils.isBlank(mWebUrl)){
            throw new PayException("mWebUrl不能为空！");
        }
        if(StringUtils.isBlank(redirectUrl)){
            back.put("mweb_url",mWebUrl);
            return back;
        }
        //对redirect_url进行urlencode处理
        String newRedirectUrl;
        try {
            newRedirectUrl = URLEncoder.encode(redirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new PayException("redirectUrl urlEncode处理异常");
        }
        //重新拼接后返回
        back.put("mweb_url",String.format( mWebUrl + "%s","&redirect_url=" + newRedirectUrl));
        return back;
    }

    /**
     * 微信回调处理
     * @param request
     * @param response
     * @return
     */
    public static Map<String, String> getCallBackParameterMap(HttpServletRequest request, HttpServletResponse response) throws PayException{
        String notifyXml = "";
        String inputLine;
        try{
            while ((inputLine = request.getReader().readLine()) != null) {
                notifyXml += inputLine;
            }
            request.getReader().close();
        }catch (Exception ex){
            throw new PayException("微信支付回调参数获取失败");
        }
        if(StringUtils.isBlank(notifyXml)){
            throw new PayException("微信支付回调参数为空");
        }
        return XmlDom4jParserUtil.xmlToMap(notifyXml);
    }

}
