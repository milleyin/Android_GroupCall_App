package com.afmobi.palmcall.outerApi.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.jtool.annotation.AvailableValues;
import com.jtool.codegenannotation.CodeGenField;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UploadSourceRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @CodeGenField("用户session资料")
    private String session;

    @NotNull
    @Size(min = 1, max = 50)
    @CodeGenField("palmcall令牌")
    private String pctoken;

    @NotNull
    @AvailableValues(values={"0", "1"})
    @CodeGenField("0：语音<br/>1：图片<br/>上传资源的类型,图片可用格式为：jpg,音频可用格式为：amr")
    private String type;

    @NotNull
    @CodeGenField("上传文件, multipart/form-data格式上传文件;")
    @JSONField(serialize=false)
    private MultipartFile file;

    @DecimalMin("0")
    @CodeGenField("语音时长")
    private Integer duration = 0;

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPctoken() {
        return pctoken;
    }

    public void setPctoken(String pctoken) {
        this.pctoken = pctoken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "UploadSourceRequest{" +
                "session='" + session + '\'' +
                ", pctoken='" + pctoken + '\'' +
                ", type='" + type + '\'' +
                ", file=" + file +
                ", duration=" + duration +
                '}';
    }

}
