package com.jlkj.sdk.pay.wechat.request_vo;

import com.jlkj.sdk.pay.wechat.utils.WechatCoreUtil;

import java.io.Serializable;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 16:12 2018/3/27
 * @Description 统一下单请求参数封装类，只要是微信统一下单请求，都可以使用这个实体类。
 * 请求参数参考连接地址:
 * 小程序境内普通商户统一下单参数：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1
 */
public class UnifiedOrderReqVO  implements Serializable{

    /**
     * 微信开放平台审核通过的应用APPID (app,public)
     */
    private String appId;

    /**
     * 商户号,微信支付分配的商户号 (app,public)
     */
    private String mchId;

    /**
     * 随机字符串  (app,public)
     */
    private String nonceStr;

    /**
     * 商品描述 (app,public)
     */
    private String body;

    /**
     * 商户订单号 (app,public)
     */
    private String outTradeNo;

    /**
     * 订单总金额 (app,public)
     */
    private Integer totalFee;

    /**
     * 终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。 (app,public)
     */
    private String spBillCreateIp;

    /**
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。（app,public)
     */
    private String notifyUrl;

    /**
     * JSAPI--公众号支付、小程序、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
     * MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口 (app,public)
     */
    private String tradeType;

    //////////////////////可以为空字段//////////////////////////////
    /**
     * 用户标识,trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。(public)
     */
    private String openId;

    /**
     * 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB" （app,public)
     */
    private String deviceInfo;

    /**
     * 签名类型，不传默认为MD5. (app,public)
     */
    private String signType = "MD5";

    /**
     * 商品详情（app,public）
     */
    private String detail;

    /**
     * 附加数据（app,public)
     */
    private String attach;

    /**
     *标价币种 （app,public）
     */
    private String feeType;

    /**
     * 交易起始时间,订单生成时间，格式为yyyyMMddHHmmss,(app,public)
     *
     */
    private String timeStart;

    /**
     * 交易结束时间 (app,public)
     */
    private String timeExpire;

    /**
     * 订单优惠标记，使用代金券或立减优惠功能时需要的参数。（app,public)
     */
    private String goodsTag;

    /**
     *商品ID,trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。(public)
     */
    private String productId;

    /**
     * 指定支付方式,上传此参数no_credit--可限制用户不能使用信用卡支付 (app,public)
     */
    private String limitPay;

    /**
     * 场景信息 H5支付必传。(app,public)
     * APP内：该字段用于统一下单时上报场景信息，目前支持上报实际门店信息。{"store_id": "", //门店唯一标识，选填，String(32)"store_name":"”//门店名称，选填，String(64)}
     */
    private String sceneInfo;


    //必传字段
    public UnifiedOrderReqVO(String appId,String mchId,String body,String outTradeNo,
                             Integer totalFee,String spBillCreateIp,String notifyUrl,String tradeType){
        this.appId = appId;
        this.mchId = mchId;
        this.body = body;
        this.outTradeNo = outTradeNo;
        this.totalFee = totalFee;
        this.spBillCreateIp = spBillCreateIp;
        this.notifyUrl = notifyUrl;
        this.tradeType = tradeType;
        this.nonceStr = WechatCoreUtil.getRandomStringByLength(32); //获取一个32位的随机字符串
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpBillCreateIp() {
        return spBillCreateIp;
    }

    public void setSpBillCreateIp(String spBillCreateIp) {
        this.spBillCreateIp = spBillCreateIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(String sceneInfo) {
        this.sceneInfo = sceneInfo;
    }
}
