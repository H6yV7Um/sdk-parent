package com.qcloud.image.returnvo;

import java.io.Serializable;

public class AiCardRetVO implements Serializable {
    //姓名
    private String name;
    //英文姓名
    private String engName;
    //职位
    private String position;
    //英文职位
    private String engPosition;
    //部门
    private String department;
    //英文部门
    private String engDepartment;
    //公司
    private String company;
    //英文公司
    private String engCompany;
    //地址
    private String address;
    //英文地址
    private String engAddress;
    //邮编
    private String zipCode;
    //邮箱
    private String email;
    //网站
    private String website;
    //手机
    private String mobile;
    //电话
    private String tel;
    //传真
    private String fax;
    //QQ
    private String qq;
    //MSN
    private String msn;
    //微信
    private String weChat;
    //微博
    private String microBlog;
    //公司账户
    private String companyAccountNumber;
    //logo
    private String logo;
    //其它
    private String others;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEngPosition() {
        return engPosition;
    }

    public void setEngPosition(String engPosition) {
        this.engPosition = engPosition;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEngDepartment() {
        return engDepartment;
    }

    public void setEngDepartment(String engDepartment) {
        this.engDepartment = engDepartment;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEngCompany() {
        return engCompany;
    }

    public void setEngCompany(String engCompany) {
        this.engCompany = engCompany;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEngAddress() {
        return engAddress;
    }

    public void setEngAddress(String engAddress) {
        this.engAddress = engAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getMicroBlog() {
        return microBlog;
    }

    public void setMicroBlog(String microBlog) {
        this.microBlog = microBlog;
    }

    public String getCompanyAccountNumber() {
        return companyAccountNumber;
    }

    public void setCompanyAccountNumber(String companyAccountNumber) {
        this.companyAccountNumber = companyAccountNumber;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "AiCardRetVO{" +
                "name='" + name + '\'' +
                ", engName='" + engName + '\'' +
                ", position='" + position + '\'' +
                ", engPosition='" + engPosition + '\'' +
                ", department='" + department + '\'' +
                ", engDepartment='" + engDepartment + '\'' +
                ", company='" + company + '\'' +
                ", engCompany='" + engCompany + '\'' +
                ", address='" + address + '\'' +
                ", engAddress='" + engAddress + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", mobile='" + mobile + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", qq='" + qq + '\'' +
                ", msn='" + msn + '\'' +
                ", weChat='" + weChat + '\'' +
                ", microBlog='" + microBlog + '\'' +
                ", companyAccountNumber='" + companyAccountNumber + '\'' +
                ", logo='" + logo + '\'' +
                ", others='" + others + '\'' +
                '}';
    }
}
