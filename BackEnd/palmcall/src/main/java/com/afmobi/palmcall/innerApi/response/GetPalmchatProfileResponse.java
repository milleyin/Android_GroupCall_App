package com.afmobi.palmcall.innerApi.response;

import com.alibaba.fastjson.annotation.JSONField;

public class GetPalmchatProfileResponse {

    private String code;

    private String phone;

    @JSONField(name="is_bind_phone")
    private String isBindPhone;

    private String phoneCountryCode;

    private String sex;

    @JSONField(name="local_img_path")
    private String localImgPath;

    private String name;

    private String birthdate;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsBindPhone() {
        return isBindPhone;
    }

    public void setIsBindPhone(String isBindPhone) {
        this.isBindPhone = isBindPhone;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocalImgPath() {
        return localImgPath;
    }

    public void setLocalImgPath(String localImgPath) {
        this.localImgPath = localImgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "GetPalmchatProfileResponse{" +
                "code='" + code + '\'' +
                ", phone='" + phone + '\'' +
                ", isBindPhone='" + isBindPhone + '\'' +
                ", phoneCountryCode='" + phoneCountryCode + '\'' +
                ", sex='" + sex + '\'' +
                ", localImgPath='" + localImgPath + '\'' +
                ", name='" + name + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }

}
