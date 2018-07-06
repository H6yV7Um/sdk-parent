package com.qcloud.image;

import com.qcloud.image.exception.AbstractImageException;
import com.qcloud.image.exception.UnknownException;
import com.qcloud.image.http.AbstractImageHttpClient;
import com.qcloud.image.http.DefaultImageHttpClient;
import com.qcloud.image.op.DetectionOp;
import com.qcloud.image.request.AbstractBaseRequest;
import com.qcloud.image.request.GeneralOcrRequest;
import com.qcloud.image.request.IdcardDetectRequest;
import com.qcloud.image.request.NamecardDetectRequest;
import com.qcloud.image.sign.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Proxy;

/**
 * @author chengwu 封装Image JAVA SDK暴露给用户的接口函数
 */
public class ImageClient implements Image {

    private static final Logger LOG = LoggerFactory.getLogger(ImageClient.class);

    private ClientConfig config;
    private Credentials cred;
    private AbstractImageHttpClient client;

    private DetectionOp detectionOp;

    public ImageClient(String appId, String secretId, String secretKey) {
        this(new Credentials(appId, secretId, secretKey));
    }

    public ImageClient(Credentials cred) {
        this(new ClientConfig(), cred);
    }

    public void setConfig(ClientConfig config) {
        this.config = config;
        this.detectionOp.setConfig(config);
        this.client.shutdown();
        this.client = new DefaultImageHttpClient(config);
        this.detectionOp.setHttpClient(this.client);
    }

    public void setCred(Credentials cred) {
        this.cred = cred;
        this.detectionOp.setCred(cred);
    }

    public void setProxy(Proxy proxy) {
        this.config.setProxy(proxy);
    }

    public ImageClient(ClientConfig config, Credentials cred) {
        this.config = config;
        this.cred = cred;
        this.client = new DefaultImageHttpClient(config);
        detectionOp = new DetectionOp(this.config, this.cred, this.client);
    }

    private void recordException(String methodName, AbstractBaseRequest request, String message) {
        LOG.error(methodName + "occur a exception, request:{}, message:{}", request, message);
    }


    @Override
    public String idcardDetect(IdcardDetectRequest request) {
        try {
            return detectionOp.idcardDetect(request);
        } catch (AbstractImageException e) {
            recordException("idcardDetect", request, e.toString());
            return e.toString();
        } catch (Exception e) {
            UnknownException e1 = new UnknownException(e.toString());
            recordException("idcardDetect", request, e1.toString());
            return e1.toString();
        }
    }
    
    @Override
    public String namecardDetect(NamecardDetectRequest request) {
        try {
            return detectionOp.namecardDetect(request);
        } catch (AbstractImageException e) {
            recordException("namecardDetect", request, e.toString());
            return e.toString();
        } catch (Exception e) {
            UnknownException e1 = new UnknownException(e.toString());
            recordException("namecardDetect", request, e1.toString());
            return e1.toString();
        }
    }


    @Override
    public String generalOcr(GeneralOcrRequest request) {
        try {
            return detectionOp.generalOcr(request);
        } catch (AbstractImageException e) {
            recordException("generalOcr", request, e.toString());
            return e.toString();
        } catch (Exception e) {
            UnknownException e1 = new UnknownException(e.toString());
            recordException("generalOcr", request, e1.toString());
            return e1.toString();
        }
    }


    @Override
    public void shutdown() {
        this.client.shutdown();
    }

}
