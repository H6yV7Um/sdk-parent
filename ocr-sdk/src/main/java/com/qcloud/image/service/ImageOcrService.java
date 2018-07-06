package com.qcloud.image.service;

import com.qcloud.image.returnvo.AiCardRetVO;

import java.io.File;


public interface ImageOcrService {

    /**
     * 接收图片并解析
     */
    AiCardRetVO analysis(File file, String fileName, String appId, String secretId, String secretKey, String bucketName);

    /**
     * 按图片路径解析
     */
    AiCardRetVO analysis(String file, String appId, String secretId, String secretKey, String bucketName);

    /**
     * 接收图片并解析
     *//*
    AiCardRetVO analysis(MultipartFile file, String fileName, String appId, String secretId, String secretKey, String bucketName);*/
}
