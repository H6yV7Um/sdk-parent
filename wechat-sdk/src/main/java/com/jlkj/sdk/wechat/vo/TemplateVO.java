package com.jlkj.sdk.wechat.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author MaXD(金蝉子)
 * @Date Create Time 11:53 2018/5/23
 * @Description 发送模板实体类
 */
public class TemplateVO {
    /**
     * openId
     */
    private String touser;

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 点击模板需要跳转的页面,为空说明不需要进入小程序
     */
    private String page;

    /**
     *表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     */
    private String form_id;

    /**
     * 模板内容，不填则下发空模板
     */
    private JSONObject data;

    /**
     * 模板需要放大的关键词，不填则默认无放大。
     */
    private String emphasis_keyword;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getEmphasis_keyword() {
        return emphasis_keyword;
    }

    public void setEmphasis_keyword(String emphasis_keyword) {
        this.emphasis_keyword = emphasis_keyword;
    }
}
