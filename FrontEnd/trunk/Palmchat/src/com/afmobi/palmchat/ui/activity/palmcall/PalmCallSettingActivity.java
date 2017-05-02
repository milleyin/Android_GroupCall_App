package com.afmobi.palmchat.ui.activity.palmcall;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.datepicker.STTimePicker;
import com.afmobi.palmchat.ui.customview.datepicker.STTimePickerDialog;
import com.afmobi.palmchat.ui.customview.switchbutton.SwitchButton;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.UploadPictureUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmCallResp;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.core.Consts.MEDIA_TYPE_JPG;


/**
 * Created by mqy on 2016/7/4.
 */
public class PalmCallSettingActivity extends BaseActivity implements View.OnClickListener,AfHttpResultListener {

    private ImageView mIvCallSettingBack;//后退按钮
    //存储自己个人信息
    private AfProfileInfo afProfileInfo;
    private AfPalmchat mAfPalmchat;
    private AfPalmCallResp.AfPalmCallHotListItem afPalmCallHotListItem;
    private AfPalmCallResp.AfPalmCallGetAlonePeriodResp afPalmCallGetAlonePeriodResp;
    //标题
    private TextView mTitleText;

    /**头像view*/
    private RelativeLayout mPalmcall_photo;
    private ImageView mPalmcall_photo_value;

    /**录音自己介绍view*/
    private RelativeLayout mRl_VoiceIntroduction;
    /**应答开关*/
    private RelativeLayout mRl_AnsweringSwitch;
    /**应答checkbox*/
    private SwitchButton mChk_AnsweringSwitch;

    /**开始时间*/
    private RelativeLayout mRl_StartTime;
    private  TextView StartTime_value;
    /**结束时间*/
    private RelativeLayout mRl_EndTime;
    private  TextView EndTime_value;
    /**保存按钮*/
    private Button mBtn_Save;
    /**应答开关*/
    private CheckBox mChx_AnsweringSwitch;

    private String local_Headimg_path;

    /**点击repeat后弹出Dialog选择日期*/
    private int StartTime;
    private int EndTime;
    private String sCameraFilename;
    private File fCurrentFile;
    private LargeImageDialog largeImageDialog;

    private int isopen=0;
    private String Stext="",Etext="";  //防盗扰初始值
    private boolean ischangeS=false;
    private boolean ischangeE=false;
    private boolean ischangeO=false;

    @Override
    public void findViews() {
        afProfileInfo=CacheManager.getInstance().getMyProfile();
        mAfPalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
        AfPalmCallResp afPalmCallRespSelf = mAfPalmchat.AfDbPalmCallInfoGet(afProfileInfo.afId);
        if (afPalmCallRespSelf!=null)     //判断数据库存有自己的信息 有就直接加载
        {
            try{
                if (afPalmCallRespSelf.respobj!=null) {
                    afPalmCallHotListItem = ((ArrayList<AfPalmCallResp.AfPalmCallHotListItem>) afPalmCallRespSelf.respobj).get(0);
                }
            } catch (Exception e){
                PalmchatLogUtils.i(TAG,"---------afPalmCallHotListItem1======NULLL--------"+e.toString());
                e.printStackTrace();
            }
        }
        if (afPalmCallHotListItem==null) { //没有就请求自己的信息
            mAfPalmchat.AfHttpPalmCallGetInfo(afProfileInfo.afId, null, this);
            showProgressDialog(R.string.please_wait);
        }
        setContentView(R.layout.activity_palmcall_setting);
        mPalmcall_photo = (RelativeLayout) findViewById(R.id.Palmcall_photo);
        mPalmcall_photo_value=(ImageView) findViewById(R.id.plamcall_photo_value);
        mPalmcall_photo.setOnClickListener(this);
        mPalmcall_photo_value.setOnClickListener(this);
        mPalmcall_photo_value.setImageResource(afProfileInfo.sex == Consts.AFMOBI_SEX_FEMALE ? R.drawable.head_palmcall_female : R.drawable.head_palmcall_male);
        mIvCallSettingBack=(ImageView) findViewById(R.id.back_button);
        mIvCallSettingBack.setOnClickListener(this);
        mRl_VoiceIntroduction = (RelativeLayout) findViewById(R.id.btn_VoiceIntroduction);
        mRl_VoiceIntroduction.setOnClickListener(this);
        mRl_AnsweringSwitch = (RelativeLayout) findViewById(R.id.btn_Answering_switch);
        mChk_AnsweringSwitch = (SwitchButton) findViewById(R.id.checkbox_call_answering_switch);
        mChk_AnsweringSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                int check=0;
                if (isChecked) {
                    check=1;
                    mRl_StartTime.setVisibility(View.VISIBLE);
                    mRl_EndTime.setVisibility(View.VISIBLE);
                } else {
                    mRl_StartTime.setVisibility(View.GONE);
                    mRl_EndTime.setVisibility(View.GONE);
                }
                if (isopen==check)   //isopen是服务器下来的 如果最后选的和服务器的 不一致 就表示改变了
                {
                    ischangeO=false;
                    if (!ischangeE && !ischangeS && !ischangeO)
                    {
                        setChange(false);
                    }
                } else
                {
                    ischangeO=true;
                    setChange(true);
                }
            }

        });
        mRl_StartTime= (RelativeLayout)findViewById(R.id.btn_start_time);
        StartTime_value=(TextView) findViewById(R.id.palmcall_strattime_value);
        mRl_StartTime.setOnClickListener(this);
        mRl_EndTime =  (RelativeLayout)findViewById(R.id.btn_end_time);
        mRl_EndTime.setOnClickListener(this);
        EndTime_value=(TextView) findViewById(R.id.palmcall_endtime_value);
        mChx_AnsweringSwitch=(CheckBox)findViewById(R.id.checkbox_call_answering_switch);
        mBtn_Save = (Button)findViewById(R.id.btn_save);
        mBtn_Save.setOnClickListener(this);
        mBtn_Save.setClickable(false);
        //显示头像
        showPalmCallHead();
        mAfPalmchat.AfHttpPalmCallGetAlonePeriod(null, this);
        showProgressDialog(R.string.please_wait);
//        if (SharePreferenceUtils.getInstance(getApplicationContext()).getPalmCallEndTime(afProfileInfo.afId)!=-1 )
//        {
//            int EndTimetz = SharePreferenceUtils.getInstance(getApplicationContext()).getPalmCallEndTime(afProfileInfo.afId);
//            int StartTimetz = SharePreferenceUtils.getInstance(getApplicationContext()).getPalmCallStartTime(afProfileInfo.afId);
//            StartTime = changeTimeZone(StartTimetz, TimeZone.getTimeZone("GMT00:00"), TimeZone.getDefault());
//            EndTime = changeTimeZone(EndTimetz, TimeZone.getTimeZone("GMT00:00"), TimeZone.getDefault());
//            if (SharePreferenceUtils.getInstance(this).getPalmCallAnsweringSwitch(afProfileInfo.afId))
//            {
//                isopen=1;
//            } else
//            {
//                isopen=0;
//            }
//            if (SharePreferenceUtils.getInstance(getApplicationContext()).getPalmCallAnsweringSwitch(afProfileInfo.afId)) {
//                mChk_AnsweringSwitch.setChecked(true);
//            } else
//            {
//                StartTime=23;
//                EndTime=7;
//                mChk_AnsweringSwitch.setChecked(false);
//                mRl_StartTime.setVisibility(View.GONE);
//                mRl_EndTime.setVisibility(View.GONE);
//            }
//            StartTime_value.setText(getFormatedTime(StartTime));
//            Stext=getFormatedTime(StartTime);
//            EndTime_value.setText(getFormatedTime(EndTime));
//            Etext=getFormatedTime(EndTime);
//        } else {
//        mAfPalmchat.AfHttpPalmCallGetAlonePeriod(null, this);
//        showProgressDialog(R.string.please_wait);
//        }

    }

    /**
     * 改变保存按钮状态
     * @param bool 是否修改
     */
    private void setChange(boolean bool)
    {
        if (bool) {
            mBtn_Save.setBackgroundResource(R.drawable.login_button_selector);
            mBtn_Save.setTextColor(Color.WHITE);
            mBtn_Save.setClickable(true);
        } else {
            mBtn_Save.setBackgroundResource(R.drawable.btn_blue_d);
            mBtn_Save.setTextColor(getResources().getColor(R.color.guide_text_color));
            mBtn_Save.setClickable(false);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void onBackPressed() {
        //判断是否需要更新设置
        IschangeSave();
    }

    //判断是否需要更新语音
    private  void  IschangeSave()
    {
        if (mBtn_Save.isClickable()) {
           showConfirmDialog();
        } else
        {
            this.finish();
        }
    }

    /**
     * 显示确认对话框
     */
    private void showConfirmDialog() {
        final AppDialog confirmDialog = new AppDialog(this);
        confirmDialog.createConfirmDialog(this, getString(R.string.exit_dialog_content), new AppDialog.OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                PalmCallSettingActivity.this.finish();
//                SetAlonePeriod();
            }

            @Override
            public void onLeftButtonClick() {
                confirmDialog.dismiss();
//                PalmCallSettingActivity.this.finish();
            }
        });
        confirmDialog.show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                IschangeSave();
                break;
            //我的录音介绍
            case R.id.btn_VoiceIntroduction:
                Intent mIntent = new Intent(this, PalmCallSendVoiceActivity.class);
                context.startActivity(mIntent);

                break;
            case R.id.btn_start_time:
                if (CommonUtils.isFastClick()) {
                    return;
                }
                final Calendar dateAndTime = Calendar.getInstance(Locale.getDefault());
                STTimePickerDialog pickerDialog = new STTimePickerDialog(context, new STTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onDateSet(STTimePicker view, int hour, int minute, int am_pm, String formatedDate) {
                        Calendar c = Calendar.getInstance();
                        // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        if (CommonUtils.compareDate(String.valueOf(hour), String.valueOf(minute), String.valueOf(am_pm), view)) {
                            dateAndTime.set(Calendar.HOUR, hour);
                            dateAndTime.set(Calendar.MINUTE, minute);
                            dateAndTime.set(Calendar.AM_PM, am_pm);
                            if (am_pm == 1) {          //12PM 是中午12点
                                if (hour==12)
                                {
                                    hour=0;
                                }
                                StartTime = hour + 12;
                            } else {                 //12AM 是0点
                                if (hour==12)
                                {
                                    hour=0;
                                }
                                StartTime = hour;
                            }
                            if (!formatedDate.equals(Stext))
                            {
                                ischangeS=true;
                                setChange(true);
                            } else
                            {
                                ischangeS=false;
                                if (!ischangeE && !ischangeS && !ischangeO)
                                {
                                    setChange(false);
                                }
                            }
                            StartTime_value.setText(formatedDate);
                        } else {
                            //
                            ToastManager.getInstance().show(context, R.string.choose_right_birthday);
                        }
                    }
                }, dateAndTime.get(Calendar.HOUR), dateAndTime.get(Calendar.MINUTE), dateAndTime.get(Calendar.AM_PM), "Start Time");
                pickerDialog.show();
                break;
            case R.id.btn_end_time:
                if (CommonUtils.isFastClick()) {
                    return;
                }
                final Calendar dateAndTimeend = Calendar.getInstance(Locale.getDefault());
                STTimePickerDialog pickerDialogend = new STTimePickerDialog(context, new STTimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onDateSet(STTimePicker view, int hour, int minute, int am_pm, String formatedDate) {
                        Calendar c = Calendar.getInstance();
                        // 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        if (CommonUtils.compareDate(String.valueOf(hour), String.valueOf(minute), String.valueOf(am_pm), view)) {
                            dateAndTimeend.set(Calendar.HOUR, hour);
                            dateAndTimeend.set(Calendar.MINUTE, minute);
                            dateAndTimeend.set(Calendar.AM_PM, am_pm);
                            if (am_pm == 1) {          //12PM 是中午12点
                                if (hour==12)
                                {
                                    hour=0;
                                }
                                EndTime = hour + 12;
                            } else {
                                if (hour==12)          //12AM 是0点
                                {
                                    hour=0;
                                }
                                EndTime = hour;
                            }
                            if (!formatedDate.equals(Etext))
                            {
                                ischangeE=true;
                                setChange(true);
                            } else
                            {
                                ischangeE=false;
                                if (!ischangeE && !ischangeS && !ischangeO)
                                {
                                    setChange(false);
                                }
                            }
                            EndTime_value.setText(formatedDate);
                        } else {
                            //
                            ToastManager.getInstance().show(context, R.string.choose_right_birthday);
                        }
                    }
                }, dateAndTimeend.get(Calendar.HOUR), dateAndTimeend.get(Calendar.MINUTE), dateAndTimeend.get(Calendar.AM_PM), "End Time");
                pickerDialogend.show();
                break;
            case R.id.btn_save:
                SetAlonePeriod();
                break;

            case R.id.Palmcall_photo:
                alertMenu();
                break;
            case R.id.plamcall_photo_value: {
                if (afPalmCallHotListItem!=null && !TextUtils.isEmpty(afPalmCallHotListItem.coverUrl))
                {
                    largeImageDialog = new LargeImageDialog(PalmCallSettingActivity.this, CommonUtils.splicePalcallCoverUrl(afPalmCallHotListItem.coverUrl,afPalmCallHotListItem.afid), afProfileInfo.afId, LargeImageDialog.TYPE_PALMCALL_AVATAR);
                    largeImageDialog.show();
                    largeImageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                } else
                {
                    alertMenu();
                }
                break;
            }
            default:
                break;
        }

    }


    /**
     * 创建本地本件
     */
    private void createLocalFile() {
        sCameraFilename = ClippingPicture.getCurrentFilename();
        PalmchatLogUtils.e("camera->", sCameraFilename);
        SharePreferenceService.getInstance(PalmCallSettingActivity.this).savaFilename(sCameraFilename);
        fCurrentFile = new File(RequestConstant.CAMERA_CACHE, sCameraFilename);
        CacheManager.getInstance().setCurFile(fCurrentFile);
    }

    /**
     * 点击头像后弹出的dialog
     */
    public void alertMenu() {

        createLocalFile();// 先创建裁切图片存放路径
        Intent mIntent = new Intent(this, PreviewImageActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt("size", 1);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent, MessagesUtils.PICTURE);
    }


    /**
     * 保存防打扰时间
     */
    private void  SetAlonePeriod()
    {
        int open=0;
        if (mChx_AnsweringSwitch.isChecked())
        {
            open=1;
        } else //如果是关闭的设置默认时间
        {
            if (StartTime == EndTime)
            {
                StartTime=23;
                EndTime=7;
            }
        }
        if (StartTime == EndTime) {
            ToastManager.getInstance().show(context, R.string.call_Setting_SameTime);
            return;
        }
        showProgressDialog(R.string.please_wait);
        //转换成服务器0时区时间
        int StartTimetz=changeTimeZone(StartTime,TimeZone.getDefault(),TimeZone.getTimeZone("GMT00:00"));
        int EndTimetz=changeTimeZone(EndTime,TimeZone.getDefault(),TimeZone.getTimeZone("GMT00:00"));
        mAfPalmchat.AfHttpPalmCallSetAlonePeriod(open,StartTimetz, EndTimetz,null,PalmCallSettingActivity.this);
    }

//    private void SetAlonePeriodLocal() {
//        SharePreferenceUtils.getInstance(getApplicationContext()).setPalmCallAnsweringSwitch(afProfileInfo.afId, mChx_AnsweringSwitch.isChecked());
//        int StartTimetz = changeTimeZone(StartTime, TimeZone.getDefault(), TimeZone.getTimeZone("GMT00:00"));
//        int EndTimetz = changeTimeZone(EndTime, TimeZone.getDefault(), TimeZone.getTimeZone("GMT00:00"));
//        SharePreferenceUtils.getInstance(getApplicationContext()).setPalmCallStartTime(afProfileInfo.afId, StartTimetz);
//        SharePreferenceUtils.getInstance(getApplicationContext()).setPalmCallEndTime(afProfileInfo.afId, EndTimetz);
//    }


    /**
     * 获取更改时区后的时间
     * @param hour 时间
     * @param oldZone 旧时区
     * @param newZone 新时区
     * @return 时间
     */
    public static int changeTimeZone(int hour, TimeZone oldZone, TimeZone newZone)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour);
        Date date=cal.getTime();
        Date dateTmp = null;
        if (date != null)
        {
            int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
            dateTmp = new Date(date.getTime() - timeOffset);
        }
        cal.setTime(dateTmp);
        return cal.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 上传头像
     *
     * @param path
     * @param filename
     */
    private void uploadPalmCallHead(String path, String filename) {
//        ImageManager.getInstance().DisplayLocalImage(mPalmcall_photo_value,path ,0,0,null);
        local_Headimg_path=path;
        showProgressDialog(R.string.uploading);
        mAfPalmchat.AfHttpPalmCallUploadIntroMedia("",path, MEDIA_TYPE_JPG,0,null,PalmCallSettingActivity.this);
    }

    /*
    * 显示头像
     */

    private void showPalmCallHead()
    {
        if (afPalmCallHotListItem!=null &&  mPalmcall_photo_value != null &&  !TextUtils.isEmpty(afPalmCallHotListItem.coverUrl )) {
            ImageManager.getInstance().DisplayImage(mPalmcall_photo_value, CommonUtils.splicePalcallCoverUrl(afPalmCallHotListItem.coverUrl,afPalmCallHotListItem.afid), 0, false, null);
        } else
        {
            mPalmcall_photo_value.setImageResource(afProfileInfo.sex == Consts.AFMOBI_SEX_FEMALE ? R.drawable.head_palmcall_female : R.drawable.head_palmcall_male);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MessagesUtils.PICTURE:// 相册
                PalmchatLogUtils.e("MQY", "PalmCall Setting RESULT_OK fCurrentFile="+fCurrentFile);
                if (data != null) {
                    if (null != fCurrentFile) {
                        Bitmap bm = null;
                        ArrayList<String> arrAddPiclist = data.getStringArrayListExtra("photoLs");
                        String cameraFilename = data.getStringExtra("cameraFilename");
                        PalmchatLogUtils.e("MQY", "PalmCall Setting RESULT_OK cameraFilename="+cameraFilename);
                        String path = null;
                        if (arrAddPiclist != null && arrAddPiclist.size() > 0) {
                            path = arrAddPiclist.get(0);
                        } else if (cameraFilename != null) {
                            path = RequestConstant.CAMERA_CACHE + cameraFilename;
                        }
                        if (path != null) {
                            PalmchatLogUtils.e("MQY", "PalmCall Setting path "+path);
                            String largeFilename = Uri.decode(path);
                            File f = new File(largeFilename);
                            File f2 = FileUtils.copyToImg(largeFilename);// copy到palmchat/camera
                            if (f2 != null) {
                                f = f2;
                            }
                            cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                            cameraFilename = RequestConstant.IMAGE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                            cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
                            if (CommonUtils.isEmpty(cameraFilename)) {
                                return;
                            }
                            bm = ImageManager.getInstance().loadLocalImageSync(cameraFilename,false);//ImageUtil.getBitmapFromFile(cameraFilename, true);
                            UploadPictureUtils.getInit().showClipPhoto_squre(bm, mPalmcall_photo, PalmCallSettingActivity.this, sCameraFilename, new UploadPictureUtils.IUploadHead() {
                                @Override
                                public void onUploadHead(String path, String filename) {
                                    uploadPalmCallHead(path, filename);
                                }
                                @Override
                                public void onCancelUpload() {

                                }
                            });
                        }
                    }
                }
                break;
        }

    }

    /**
     * 获取时间界面展现格式
     * @param hour 防打扰小时
     * @return
     */
    private String getFormatedTime(int hour)
    {
        String formatedDate="";
        String am_pm="AM";
        int iHour=hour;
        if (hour==12)
        {
            am_pm="PM";
        } else if (hour==0)
        {
            iHour=12;
        } else   if (hour>12)
        {
            iHour=hour-12;
            am_pm="PM";
        }
        formatedDate=iHour+" "+ am_pm;
        return  formatedDate;
    }




    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        if (code == Consts.REQ_CODE_SUCCESS) {
            PalmchatLogUtils.i(TAG, "flag = " + (flag == Consts.REQ_UPDATE_INFO));
            if (flag == Consts.REQ_PALM_CALL_UPLOAD_INTRO_MEDIA) {
                // 头像上传成功
                PalmchatLogUtils.i(TAG, "avatar upload success!!!" + user_data + "->result = " + result);
                // 将图片copy到 avatar目录下
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SET_H_SUCC);
                ToastManager.getInstance().show(context, R.string.call_Voice_SaveSucceed);
                if (result!=null) {
                    String _url=(String)result;
                    if (afPalmCallHotListItem!=null) {
                        afPalmCallHotListItem.coverUrl = _url;
                        mAfPalmchat.AfDbPalmCallInfoUpdate(afPalmCallHotListItem);
                    }
                    String pathOriginalDestination = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.splicePalcallCoverUrl(_url,afPalmCallHotListItem.afid));
                    File outFile_original = new File(pathOriginalDestination);
                    if (!TextUtils.isEmpty(local_Headimg_path)) {
                        FileUtils.copyToImg(local_Headimg_path, outFile_original.getAbsolutePath());
                    }
                    showPalmCallHead();
                    local_Headimg_path="";
                }
                dismissProgressDialog();
            } else if (flag == Consts.REQ_PALM_CALL_GET_INFO)
            {
                dismissProgressDialog();
                if (result != null)
                {
                    AfPalmCallResp mAfPalmCallResp=(AfPalmCallResp)result;
                    if  (mAfPalmCallResp.respobj!=null) {
                        ArrayList mArrayList = (ArrayList) mAfPalmCallResp.respobj;
                        if (mArrayList != null && mArrayList.size() > 0) {
                            afPalmCallHotListItem=(AfPalmCallResp.AfPalmCallHotListItem) mArrayList.get(0);
                            //判断数据库有没有，有就更新，没有就插入
                            if (mAfPalmchat.AfDbPalmCallInfoGet(afProfileInfo.afId)!=null) {
                                mAfPalmchat.AfDbPalmCallInfoUpdate(afPalmCallHotListItem);
                            } else {
                                mAfPalmchat.AfDbPalmCallInfoInsert(afPalmCallHotListItem);
                            }
                            //显示头像地址
                            showPalmCallHead();
                        }
                    }
                }
            } else  if (flag == Consts.REQ_PALM_CALL_SET_ALONE_PERIOD)
            {
                dismissProgressDialog();
//                SetAlonePeriodLocal();//保存成功把开关 起始时间存本地
                this.finish();
            } else if (flag == Consts.REQ_PALM_CALL_GET_ALONE_PERIOD) {
                dismissProgressDialog();
                if (result != null)
                {
                    AfPalmCallResp mAfPalmCallResp=(AfPalmCallResp)result;
                    if  (mAfPalmCallResp.respobj!=null) {
                        afPalmCallGetAlonePeriodResp=(AfPalmCallResp.AfPalmCallGetAlonePeriodResp)mAfPalmCallResp.respobj;
                        if (afPalmCallGetAlonePeriodResp!=null) {
                            StartTime = changeTimeZone(afPalmCallGetAlonePeriodResp.startTime,TimeZone.getTimeZone("GMT00:00"),TimeZone.getDefault());
                            EndTime= changeTimeZone(afPalmCallGetAlonePeriodResp.endTime,TimeZone.getTimeZone("GMT00:00"),TimeZone.getDefault());
                            if (afPalmCallGetAlonePeriodResp.open==1)
                            {
                                isopen=1;
                                mChk_AnsweringSwitch.setChecked(true);
                            } else
                            {
                                mRl_StartTime.setVisibility(View.GONE);
                                mRl_EndTime.setVisibility(View.GONE);
                                StartTime=23;
                                EndTime=7;
                                isopen=0;
                            }
                            StartTime_value.setText(getFormatedTime(StartTime));
                            Stext=getFormatedTime(StartTime);
                            EndTime_value.setText(getFormatedTime(EndTime));
                            Etext=getFormatedTime(StartTime);
                        }
                    }
                }

            }
        } else {
            dismissProgressDialog();
            if (flag == Consts.REQ_PALM_CALL_UPLOAD_INTRO_MEDIA) {
                if (code ==Consts.REQ_CODE_UNNETWORK || code ==Consts.REQ_CODE_SERVER_MAINTENANCE || code==Consts.REQ_CODE_REDISPATCH)
                {
                    ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
                } else
                {
                    ToastManager.getInstance().show(context, context.getString(R.string.save_to_phone_failed));
                }
                showPalmCallHead();
            } else
            {
                Consts.getInstance().showToast(context, code, flag, http_code);
            }
        }
    }

}
