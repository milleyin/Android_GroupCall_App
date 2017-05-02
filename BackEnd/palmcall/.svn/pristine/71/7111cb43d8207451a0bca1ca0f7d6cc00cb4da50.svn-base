package com.afmobi.palmcall.outerApi.request;

import com.jtool.codegenannotation.CodeGenField;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FetchCallableListRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("afid,用于查询返回剩余分钟数")
    private String afid;

    @DecimalMin("0")
    @CodeGenField("是否赠送分钟数，第一次进palmcall赠送请传1,已赠送则不在送，不赠送请忽略不传")
    private Integer gift = 0;

    @Size(min = 1, max = 10)
    @CodeGenField("国家码，区别不同国家赠送分钟数,没有国家码可不传")
    private String countrycode = "00000";

    @CodeGenField("统计需要，国家,如China, 默认为unknown")
    private String country = "unknown";

    @CodeGenField("统计需要，平台,如IOS9.0, 默认为unknown")
    private String platform = "unknown";

    @CodeGenField("统计需要，Palmchat版本,如5.4, 默认为unknown")
    private String version = "unknown";

    @CodeGenField("统计需要，性别, 默认为F")
    private String sex = "F";

    @CodeGenField("统计需要，年龄, 默认为22")
    private String age = "22";

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public Integer getGift() {
        return gift;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "FetchCallableListRequest{" +
                "afid='" + afid + '\'' +
                ", gift=" + gift +
                ", countrycode='" + countrycode + '\'' +
                ", country='" + country + '\'' +
                ", platform='" + platform + '\'' +
                ", version='" + version + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

}
