package com.jlkj.sdk.pay.wechat.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 10:08 2018/3/30
 * @Description 发送请求工具类
 */
public class HttpUtil {


    /**
     * 连接超时时间，单位是毫秒
     */
    public final static int CONNECT_TIMEOUT = 10000;

    /**
     * 读取超时时间，单位是毫秒
     */
    public final static int READ_TIMEOUT = 30000;

    /**
     * 不需要证书的请求
     *
     * @param strUrl           String
     * @param reqData          向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public static String requestWithoutCert(String strUrl, String reqData) throws Exception {
        String UTF8 = "UTF-8";
        URL httpUrl = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
        httpURLConnection.setReadTimeout(READ_TIMEOUT);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqData.getBytes(UTF8));

        //获取请求内容
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        final StringBuffer stringBuffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }
        String resp = stringBuffer.toString();

        //对流的操作，记得关闭
        if (stringBuffer != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }


}
