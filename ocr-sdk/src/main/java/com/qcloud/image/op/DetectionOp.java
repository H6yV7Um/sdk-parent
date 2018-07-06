/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qcloud.image.op;

import com.qcloud.image.ClientConfig;
import com.qcloud.image.exception.AbstractImageException;
import com.qcloud.image.http.*;
import com.qcloud.image.request.GeneralOcrRequest;
import com.qcloud.image.request.IdcardDetectRequest;
import com.qcloud.image.request.NamecardDetectRequest;
import com.qcloud.image.sign.Credentials;
import com.qcloud.image.sign.Sign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.qcloud.image.ClientConfig.OCR_GENERAL;

/**
 *
 * @author jusisli 此类封装了图片识别操作
 */
public class DetectionOp extends BaseOp {
    private static final Logger LOG = LoggerFactory.getLogger(DetectionOp.class);

    public DetectionOp(ClientConfig config, Credentials cred, AbstractImageHttpClient client) {
        super(config, cred, client);
    }
    

    /**
     * 身份证识别请求
     * 
     * @param request 标签识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String idcardDetect(IdcardDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudImageDomain() + this.config.getDetectionIdcard();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());

        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.CARD_TYPE, String.valueOf(request.getCardType()));
        
        httpRequest.setMethod(HttpMethod.POST);
        if (request.isUrl()) {
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
            httpRequest.addParam(RequestBodyKey.URL_LIST, (request.getUrlList())); 
        } else {
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
            httpRequest.setImageList(request.getImageList());
            httpRequest.setKeyList(request.getKeyList());
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
    
     /**
     * 名片识别请求
     * 
     * @param request 标签识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String namecardDetect(NamecardDetectRequest request) throws AbstractImageException {
        request.check_param();
        
        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudOcrDomain() + this.config.getDetectionNamecard();
        
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addHeader(RequestHeaderKey.USER_AGENT, this.config.getUserAgent());
        httpRequest.setMethod(HttpMethod.POST);
        
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        httpRequest.addParam(RequestBodyKey.RET_IMAGE, String.valueOf(request.getRetImage()));              
        if (request.isUrl()) {
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL_LIST, (request.getUrlList())); 
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON); 
        } else {
            httpRequest.setImageList(request.getImageList());
            httpRequest.setKeyList(request.getKeyList());
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
        }
              
        return httpClient.sendHttpRequest(httpRequest);
    }
     /**
     * 名片识别请求
     * 
     * @param request 标签识别请求参数
     * @return JSON格式的字符串, 格式为{"code":$code, "message":"$mess"}, code为0表示成功, 其他为失败,
     *         message为success或者失败原因
     * @throws AbstractImageException SDK定义的Image异常, 通常是输入参数有误或者环境问题(如网络不通)
     */  
    public String generalOcr(GeneralOcrRequest request) throws AbstractImageException {
        request.check_param();

        String sign = Sign.appSign(cred, request.getBucketName(), this.config.getSignExpired());
        String url = "http://" + this.config.getQCloudOcrDomain()+ OCR_GENERAL;

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setMethod(HttpMethod.POST);
        httpRequest.setUrl(url);
        httpRequest.addHeader(RequestHeaderKey.Authorization, sign);
        httpRequest.addParam(RequestBodyKey.APPID, String.valueOf(cred.getAppId()));
        httpRequest.addParam(RequestBodyKey.BUCKET, request.getBucketName());
        if (request.isUrl()) {
            httpRequest.setContentType(HttpContentType.APPLICATION_JSON);
            httpRequest.addHeader(RequestHeaderKey.Content_TYPE, String.valueOf(HttpContentType.APPLICATION_JSON));
            httpRequest.addParam(RequestBodyKey.URL, request.getUrl());
        } else {
            httpRequest.setContentType(HttpContentType.MULTIPART_FORM_DATA);
            httpRequest.setImage(request.getImage());
        }
        return httpClient.sendHttpRequest(httpRequest);

    }
    
}
