package com.qcloud.image.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.image.ImageClient;
import com.qcloud.image.request.NamecardDetectRequest;
import com.qcloud.image.returnvo.AiCardRetVO;
import com.qcloud.image.service.ImageOcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;

public class ImageOcrServiceImpl implements ImageOcrService {
    private static final Logger logger = LoggerFactory.getLogger(ImageOcrServiceImpl.class);

    @Override
    public AiCardRetVO analysis(File file, String fileName, String appId, String secretId, String secretKey, String bucketName) {
        ImageClient imageClient = new ImageClient(appId, secretId, secretKey);
        String returnStr = ocrNameCard(imageClient, bucketName, file, fileName);
        AiCardRetVO aiCardRetVO = new AiCardRetVO();
        if (returnStr != null) {
            aiCardRetVO = stringToAicard(returnStr);
        }
        return aiCardRetVO;
    }

    @Override
    public AiCardRetVO analysis(String imageURL, String appId, String secretId, String secretKey, String bucketName) {
        ImageClient imageClient = new ImageClient(appId, secretId, secretKey);
        String returnStr = ocrNameCard(imageClient, bucketName, imageURL);
        AiCardRetVO aiCardRetVO = new AiCardRetVO();
        if (returnStr != null) {
            aiCardRetVO = stringToAicard(returnStr);
        }
        return aiCardRetVO;
    }

    /**
     * 名片ocr识别操作
     */
    private String ocrNameCard(ImageClient imageClient, String bucketName, String imageURL) {
        String[] nameCardUrlList = new String[2];
        nameCardUrlList[0] = imageURL;
        NamecardDetectRequest nameReq = new NamecardDetectRequest(bucketName, nameCardUrlList, 0);
        return imageClient.namecardDetect(nameReq);
    }


    /**
     * 名片ocr识别操作
     */
    private String ocrNameCard(ImageClient imageClient, String bucketName, File file, String fileName) {
        String ret;
        String[] nameCardNameList = new String[1];
        File[] nameCardImageList = new File[1];
        nameCardNameList[0] = fileName;

        /*try {
            //获取文件名
            String fileName = file.getOriginalFilename();
            //获取文件后缀
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            //用uuid作为文件名，防止生成的临时文件重复
            final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
            file.transferTo(excelFile);

            nameCardImageList[0] = excelFile;
            //删除零时文件
            deleteFile(excelFile);*/
        nameCardImageList[0] = file;
       /* } catch (Exception e) {
            logger.error("MultipartFile转File失败",e);
            e.printStackTrace();
        }*/
        NamecardDetectRequest nameReq = new NamecardDetectRequest(bucketName, nameCardNameList, nameCardImageList, 0);
        ret = imageClient.namecardDetect(nameReq);
        return ret;
    }

    /**
     * 删除
     *
     * @param files
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private AiCardRetVO stringToAicard(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONArray result_list = jsonObject.getJSONArray("result_list");
        if (result_list == null || result_list.size() < 0) {
            return null;
        }
        JSONObject result = result_list.getJSONObject(0);
        Integer code = (Integer) result.get("code");
        if (code != 0) {
            return null;
        }
        String message = (String) result.get("message");
        String url = (String) result.get("url");
        JSONArray datas = result.getJSONArray("data");
        AiCardRetVO acVo = new AiCardRetVO();
        for (int j = 0; j < datas.size(); j++) {
            JSONObject data = datas.getJSONObject(j);
            String item = (String) data.get("item");
            BigDecimal confidence = (BigDecimal) data.get("confidence");
            String value = (String) data.get("value");
            if ("姓名".equals(item)) {
                acVo.setName(value);
            } else if ("职位".equals(item)) {
                acVo.setPosition(value);
            } else if ("公司".equals(item)) {
                acVo.setCompany(value);
            } else if ("地址".equals(item)) {
                acVo.setAddress(value);
            } else if ("邮箱".equals(item)) {
                acVo.setEmail(value);
            } else if ("手机".equals(item)) {
                if (value != null) {
                    if (acVo.getMobile() == null) {
                        acVo.setMobile(value.replaceAll("[^\\d]+",""));
                    } else {
                        if (acVo.getMobile().replaceAll("[^\\d]+","").matches("^1\\d{10}$")) {
                            continue;
                        } else if (value.replaceAll("[^\\d]+","").matches("^1\\d{10}$")) {
                            acVo.setMobile(value);
                        }
                    }

                }
            } else if ("QQ".equals(item)) {
                acVo.setQq(value);
            } else if ("微信".equals(item)) {
                acVo.setWeChat(value);
            } else if ("电话".equals(item)) {
                acVo.setTel(value);
            }
        }
        return acVo;
    }
}
