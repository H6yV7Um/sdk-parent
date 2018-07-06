package com.jlkj.sdk.pay.wechat;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 16:50 2018/3/27
 * @Description 微信支付常量类
 */
public class WeChatPayConstants {

    public static final String FAIL = "FAIL";

    public static final String SUCCESS = "SUCCESS";

    public static final String HMACSHA256 = "HMAC-SHA256";

    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";

    public static final String FIELD_SIGN_TYPE = "sign_type";

    /**
     * 交易类型:JSAPI--公众号支付 小程序
     */
    public static final String TRADE_TYPE_JSAPI  = "JSAPI";

    /**
     * 交易类型:NATIVE--原生扫码支付
     */
    public static final String TRADE_TYPE_NATIVE = "NATIVE";

    /**
     * 交易类型:APP--app支付
     */
    public static final String TRADE_TYPE_APP    = "APP";

    /**
     * UTF-8字符集
     */
    public static final String CHARSET_UTF8      = "UTF-8";

    /**
     * 微信统一下单地址
     */
    public static final String UNIFIEDORDER_URL  = "https://api.mch.weixin.qq.com/pay/unifiedorder";

}
