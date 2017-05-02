package com.afmobi.palmcall.controller;

import com.afmobi.palmcall.dao.OperatorDAO;
import com.afmobi.palmcall.exception.*;
import com.afmobi.palmcall.innerApi.api.CheckPCTokenApi;
import com.afmobi.palmcall.innerApi.api.UpdatePCTokenApi;
import com.afmobi.palmcall.innerApi.response.CheckPCTokenResponse;
import com.afmobi.palmcall.innerApi.response.UpdatePCTokenResponse;
import com.afmobi.palmcall.log.write.CallUploadLogService;
import com.afmobi.palmcall.outerApi.request.UploadSourceRequest;
import com.afmobi.palmcall.outerApi.response.UploadSourceResponse;
import com.alibaba.fastjson.JSON;
import com.jtool.codegenannotation.CodeGenApi;
import com.jtool.codegenannotation.CodeGenRequest;
import com.jtool.codegenannotation.CodeGenResponse;
import com.jtool.support.log.LogHelper;
import com.jtool.validator.ParamBeanValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Controller
public class UploadSourceController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CheckPCTokenApi checkPCTokenApi;

    @Resource
    private UpdatePCTokenApi updatePCTokenApi;

    @Resource
    private String uploadAudioPath;

    @Resource
    private String uploadCoverImgPath;

    @Resource
    private OperatorDAO operatorDAO;

    @Resource
    private CallUploadLogService callUploadLogService;

    @CodeGenApi(name = "(外)上传音频或封面图片", docSeq = 8.0, description = "上传音频简介或者封面图片")
    @CodeGenRequest(UploadSourceRequest.class)
    @CodeGenResponse(UploadSourceResponse.class)
    @RequestMapping(value = "/uploadSource", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String uploadSource(UploadSourceRequest uploadSourceRequest)
            throws ParamException, NotFindSessionException, NotValidePCtokenException,
            NotValideSessionException, UploadedFileException, BackEndException, IOException {

        log.debug("上传音频或封面图片request：" + JSON.toJSONString(uploadSourceRequest));

        //检查请求参数
        validateRequestParam(uploadSourceRequest);

        // 检查权限
        String session = uploadSourceRequest.getSession();
        String pctoken = uploadSourceRequest.getPctoken();
        CheckPCTokenResponse checkPCTokenResponse = checkPCTokenApi.sent(session, pctoken);

        // 传输上传的文件 以afid.amr 或 afid.jpg命名
        String afid = checkPCTokenResponse.getAfid();
        LogHelper.setLogUserId(afid);
        String type = uploadSourceRequest.getType();
        String ext = getFileFormatValid(uploadSourceRequest.getFile());
        int duration = uploadSourceRequest.getDuration();

        String ts = System.currentTimeMillis() + "";
        if ("0".equals(type) && "amr".equals(ext)) { //上传语音
            callUploadLogService.writeLog(null, null, afid, 3, 0, null); //记录日志

            if (duration <= 0) { //语音时长
                throw new ParamException(uploadSourceRequest.toString());
            }

            String amrTemp =  afid + "_temp.amr";
            String amrOld = afid + ".amr";
            File uploadedFile = new File(uploadAudioPath, amrTemp);
            processUploadFile(uploadAudioPath, uploadedFile, uploadSourceRequest.getFile());

            operatorDAO.updateAudiopath(afid, amrOld, ts, duration);
            deleteOldFileAndRenameTempFile(uploadAudioPath, amrOld, amrTemp);
        } else if ("1".equals(type) && "jpg".equals(ext)) { //上传图片
            callUploadLogService.writeLog(null, null, afid, 2, 0, null); //记录日志

            String jpgTemp =  afid + "_temp.jpg";
            String jpgOld = afid + ".jpg";
            File uploadedFile = new File(uploadCoverImgPath, jpgTemp);
            processUploadFile(uploadCoverImgPath, uploadedFile, uploadSourceRequest.getFile());

            operatorDAO.updateCoverImgPath(afid, jpgOld, ts);
            deleteOldFileAndRenameTempFile(uploadCoverImgPath, jpgOld, jpgTemp);
        } else {
            throw new ParamException(uploadSourceRequest.toString());
        }

        UploadSourceResponse uploadSourceResponse = new UploadSourceResponse();
        uploadSourceResponse.setCode("0");
        uploadSourceResponse.setTs(ts);

        //更新bctoken,下一次需要新的
        UpdatePCTokenResponse updatePCTokenResponse = updatePCTokenApi.sent(session, pctoken);
        uploadSourceResponse.setPctoken(updatePCTokenResponse.getPctoken());

        log.debug("上传音频或封面图片response：" + JSON.toJSONString(uploadSourceResponse));
        return JSON.toJSONString(uploadSourceResponse);
    }

    //删除旧文件 把临时文件重命名
    private void deleteOldFileAndRenameTempFile(String path, String oldFile, String tempFile) {
        File oldF = new File(path, oldFile);
        if(oldF.isFile() && oldF.exists()) {
            oldF.delete();
        }

        File tempF = new File(path, tempFile);
        tempF.renameTo(oldF);
    }

    private void processUploadFile(String uploadPath, File uploadedFile, MultipartFile file) throws IOException {
        makeUploadingFileDir(uploadPath);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(file.getInputStream(), uploadedFile);
    }

    private void makeUploadingFileDir(String uploadingPath) throws IOException {
        File file = new File(uploadingPath);
        if (!file.isDirectory()) {
            FileUtils.forceMkdir(file);
        }
    }

    private void validateRequestParam(UploadSourceRequest uploadSourceRequest) throws ParamException {
        if (ParamBeanValidator.isNotValid(uploadSourceRequest)) {
            throw new ParamException(uploadSourceRequest.toString());
        }
    }

    private String getFileFormatValid(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        log.debug("要上传的文件名：" + fileName);

        String ext = FilenameUtils.getExtension(fileName);
        ext = ext == null ? "" : ext.toLowerCase();

        return ext;
    }

}
