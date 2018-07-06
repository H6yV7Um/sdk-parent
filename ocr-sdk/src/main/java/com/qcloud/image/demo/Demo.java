/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.qcloud.image.demo;

import com.qcloud.image.ImageClient;
import com.qcloud.image.request.GeneralOcrRequest;
import com.qcloud.image.request.IdcardDetectRequest;
import com.qcloud.image.request.NamecardDetectRequest;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author serenazhao image Demo代码
 */
public class Demo {

    public static void main(String[] args) {

        String appId = "1256070434";
        String secretId = "AKID8A10tcZkWBlGblFBSYhkhT8D9tX1opU4";
        String secretKey = "Y0H1e6NXYxUvGQUN50qV0PdgcNyOo7GQ";
        String bucketName = "jlkj";

        ImageClient imageClient = new ImageClient(appId, secretId, secretKey);

        /*设置代理服务器*/
        //Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("dev-proxy.oa.com", 8080));
        //imageClient.setProxy(proxy);

        /*文字识别系列 ( OCR )*/
        //身份证
//        ocrIdCard(imageClient, bucketName);
        //名片
        ocrNameCard(imageClient, bucketName);
        //通用
//        ocrGeneral(imageClient, bucketName);


    }



    /**
     * 名片ocr识别操作
     */
    private static void ocrNameCard(ImageClient imageClient, String bucketName) {
        String ret;
        // 1. url方式
        System.out.println("====================================================");
        String[] namecardUrlList = new String[2];
        namecardUrlList[0] = "https://www.i31.com/oss/img/userPhoto/1cb5a94f48f942679d56de801c40f452.jpeg";
 //       namecardUrlList[1] = "http://youtu.qq.com/app/img/experience/char_general/ocr_namecard_02.jpg";
        NamecardDetectRequest nameReq = new NamecardDetectRequest(bucketName, namecardUrlList, 0);

        ret = imageClient.namecardDetect(nameReq);
        System.out.println("namecard detect ret:" + ret);

        /*//2. 图片内容方式
        System.out.println("====================================================");
        String[] namecardNameList = new String[2];
        File[] namecardImageList = new File[2];
        try {
            namecardNameList[0] = "ocr_namecard_01.jpg";
            namecardImageList[0] = new File("assets", namecardNameList[0]);
            namecardNameList[1] = "ocr_namecard_02.jpg";
            namecardImageList[1] = new File("assets", namecardNameList[1]);
        } catch (Exception ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
        nameReq = new NamecardDetectRequest(bucketName, namecardNameList, namecardImageList, 0);
        ret = imageClient.namecardDetect(nameReq);
        System.out.println("namecard detect ret:" + ret);*/
    }

    /**
     * 通用印刷体OCR
     */
    private static void ocrGeneral(ImageClient imageClient, String bucketName) {
        String ret;
        // 1. url方式
        System.out.println("====================================================");
        GeneralOcrRequest request = new GeneralOcrRequest(bucketName, "http://youtu.qq.com/app/img/experience/char_general/ocr_common09.jpg");
        ret = imageClient.generalOcr(request);
        System.out.println("ocrGeneral:" + ret);

        //2. 图片内容方式
        System.out.println("====================================================");
        request = new GeneralOcrRequest(bucketName, new File("assets", "ocr_common09.jpg"));
        ret = imageClient.generalOcr(request);
        System.out.println("ocrGeneral:" + ret);
    }

    /**
     * 身份证ocr识别操作
     */
    private static void ocrIdCard(ImageClient imageClient, String bucketName) {
        String ret;
        // 1. url方式,识别身份证正面
        System.out.println("====================================================");
        String[] idcardUrlList = new String[2];
        idcardUrlList[0] = "http://youtu.qq.com/app/img/experience/char_general/icon_id_01.jpg";
        idcardUrlList[1] = "http://youtu.qq.com/app/img/experience/char_general/icon_id_02.jpg";
        IdcardDetectRequest idReq = new IdcardDetectRequest(bucketName, idcardUrlList, 0);
        ret = imageClient.idcardDetect(idReq);
        System.out.println("idcard detect ret:" + ret);
        //识别身份证反面
        idcardUrlList[0] = "https://gss0.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/314e251f95cad1c89e04bea2763e6709c83d51f3.jpg";
        idcardUrlList[1] = "http://image2.sina.com.cn/dy/c/2004-03-29/U48P1T1D3073262F23DT20040329135445.jpg";
        idReq = new IdcardDetectRequest(bucketName, idcardUrlList, 1);
        ret = imageClient.idcardDetect(idReq);
        System.out.println("idcard detect ret:" + ret);

        //2. 图片内容方式,识别身份证正面
        System.out.println("====================================================");
        String[] idcardNameList = new String[2];
        File[] idcardImageList = new File[2];
        try {
            idcardNameList[0] = "icon_id_01.jpg";
            idcardImageList[0] = new File("assets", idcardNameList[0]);
            idcardNameList[1] = "icon_id_02.jpg";
            idcardImageList[1] = new File("assets", idcardNameList[1]);
        } catch (Exception ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
        idReq = new IdcardDetectRequest(bucketName, idcardNameList, idcardImageList, 0);
        ret = imageClient.idcardDetect(idReq);
        System.out.println("idcard detect ret:" + ret);
        //识别身份证反面
        try {
            idcardNameList[0] = "icon_id_03.jpg";
            idcardImageList[0] = new File("assets", idcardNameList[0]);
            idcardNameList[1] = "icon_id_04.jpg";
            idcardImageList[1] = new File("assets", idcardNameList[1]);
        } catch (Exception ex) {
            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
        }
        idReq = new IdcardDetectRequest(bucketName, idcardNameList, idcardImageList, 1);
        ret = imageClient.idcardDetect(idReq);
        System.out.println("idcard detect ret:" + ret);
    }


}
