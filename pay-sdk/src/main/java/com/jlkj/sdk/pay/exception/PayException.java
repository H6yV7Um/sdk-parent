package com.jlkj.sdk.pay.exception;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 21:02 2018/3/29
 * @Description 支付异常类
 */
public class PayException extends Exception {
    private String            errCode;
    private String            errMsg;

    /**
     *
     */
    public PayException() {
        super();
    }

    /**
     *
     * @param message
     * @param cause
     */
    public PayException(String message, Throwable cause) {
        super(message, cause);
    }

    public PayException(String message) {
        super(message);
    }


    public PayException(Throwable cause) {
        //
        super(cause);
    }

    /**
     *
     * @param errCode
     * @param errMsg
     */
    public PayException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }
}
