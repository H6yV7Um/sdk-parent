package com.jlkj.sdk.wechat.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HttpUtil {

    /**
     * 日志类
     */
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;

    /**
     * 获取连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 10000;

    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 400;

    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 10000;

    /**
     * 读取超时时间
     */
    public final static int READ_TIMEOUT = 30000;

    private static HttpClient httpClient;
    private static HttpParams httpParams;
    private static ClientConnectionManager connectionManager;
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;

    static {
        try {
            httpParams = new BasicHttpParams();
            // 设置最大连接数
            ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
            // 设置获取连接的最大等待时间
            ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);
            // 设置每个路由最大连接数
            ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_ROUTE_CONNECTIONS);
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
            // 设置连接超时时间
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
            // 设置读取超时时间
            HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            connectionManager = new ThreadSafeClientConnManager(httpParams, registry);

            SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
            sslSocketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
            registry.register(new Scheme("https", sslSocketFactory, 443));
            httpClient = new DefaultHttpClient(connectionManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String get(String url, Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            pairs.add(nameValuePair);
        }
        return get(url, pairs);
    }

    /**
     * Get请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, List<NameValuePair> params) {
        String body = null;
        try {
            // Get请求
            HttpGet httpget = new HttpGet(url);
            // 设置参数
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
            httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
            // 发送请求
            HttpResponse httpresponse = httpClient.execute(httpget);
            // 获取返回数据
            HttpEntity entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity);
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return body;
    }


    public static String post(String url, JSONObject object) {
        String body = null;
        try {
            // Post请求
            HttpPost httppost = new HttpPost(url);
            // 设置参数
            httppost.setEntity(new StringEntity(object.toString(), ContentType.APPLICATION_JSON));
            // 发送请求
            HttpResponse httpresponse = httpClient.execute(httppost);
            // 获取返回数据
            HttpEntity entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity);
            entity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }


    public static HttpEntity responseFromPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
                .setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");//解决中文乱码问题
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                log.error("===============SSL请求失败: {}", response.getStatusLine().getReasonPhrase());
                return null;
            }
            return response.getEntity();
        } catch (IOException e) {
            log.error("===============发送SSL请求异常", e);
            return null;
        }

    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
        } catch (GeneralSecurityException e) {
            log.error("", e);
        }
        return sslsf;
    }

    /**
     * 发送 SSL GET 请求（HTTPS），K-V形式
     *
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doGetSSL(String apiUrl, List<NameValuePair> params) {
        CloseableHttpClient httpClient =
                HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
                        .setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();

        HttpGet httpGet = new HttpGet(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpGet.setConfig(requestConfig);
            // 设置参数
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
            httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + str));

            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        return httpStr;
    }

    /**
     * post请求
     *
     * @param url
     *            功能和操作
     * @param body
     *            要post的数据
     * @return
     * @throws IOException
     */
    public static String post(String url, String body)
    {
        System.out.println("url:" + System.lineSeparator() + url);
        System.out.println("body:" + System.lineSeparator() + body);

        String result = "";
        try
        {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();

            // 设置连接参数
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(20000);

            // 提交数据
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(body);
            out.flush();

            // 读取返回数据
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            boolean firstLine = true; // 读第一行不加换行符
            while ((line = in.readLine()) != null)
            {
                if (firstLine)
                {
                    firstLine = false;
                } else
                {
                    result += System.lineSeparator();
                }
                result += line;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}