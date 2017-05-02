package com.afmobi.palmchat.ui.activity.qrcode.activity;

import java.io.IOException;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.MyQrCodeActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.activity.publicaccounts.InnerNoAbBrowserActivity;
import com.afmobi.palmchat.ui.activity.qrcode.decoding.DecodeThread;
import com.afmobi.palmchat.ui.activity.qrcode.utils.BeepManager;
import com.afmobi.palmchat.ui.activity.qrcode.utils.InactivityTimer;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.google.zxing.Result;
import com.afmobi.palmchat.ui.activity.qrcode.camera.CameraManager;
import com.afmobi.palmchat.ui.activity.qrcode.utils.CaptureActivityHandler;

import android.graphics.Rect;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback ,AfHttpResultListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private Context mContext;
    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private TextView mTitletext; //标题名

    private AfPalmchat mAfCorePalmchat;

    private Dialog dialog;

    private ImageView mBackButton;

    private static Button mBtnMyqrcode;

    public AfProfileInfo afProfileInfo;

    private Rect mCropRect = null;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private boolean isHasSurface = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        mContext = this;
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        afProfileInfo = CacheManager.getInstance().getMyProfile();
        mAfCorePalmchat = ((PalmchatApp)getApplication()).mAfCorePalmchat;
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        mTitletext = (TextView)findViewById(R.id.title_text);
        mTitletext.setText(R.string.qrcode_reader);
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        isHasSurface = false;
        //移动动画
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
        mBtnMyqrcode = (Button)findViewById(R.id.btn_myqrcode);
        mBackButton = (ImageView)findViewById(R.id.back_button);
        mBackButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                finish();
            }

        });
        mBtnMyqrcode.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                startActivity(new Intent(CaptureActivity.this, MyQrCodeActivity.class));
                finish();
            }

        });
    }

    @Override
    public void init() {

    }

    @Override
    public void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        handler = null;
        if (isHasSurface) {
            initCamera(scanPreview.getHolder());
        } else {
            scanPreview.getHolder().addCallback(this);
        }
        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult
     *            The contents of the barcode.
     *
     * @param bundle
     *            The extras
     */
	public void handleDecode(Result rawResult, Bundle bundle) {
        String str;
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
    /*viewfinderView.drawResultBitmap(barcode);//画结果图片*/
 //扫描后的结果图片不显示
        //playBeepSoundAndVibrate();//启动声音效果
        str = rawResult.getText();
        if(str.startsWith("http://") || str.startsWith("https://")) {

            /*Intent intent = new Intent(Intent.ACTION_VIEW,
                    ( Uri.parse(str))
            ).addCategory(Intent.CATEGORY_BROWSABLE)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/

            Intent intent1 = new Intent();
            intent1.putExtra(IntentConstant.RESOURCEURL, str);
            intent1.setClass(context, InnerNoAbBrowserActivity.class);
            startActivity(intent1);
            finish();
        }else if(str.startsWith("{")){
            try {
                JSONObject jsonObject = new JSONObject(str);
                String afid = jsonObject.getString("content");
                int cmd = jsonObject.getInt("cmd");
                String id = afid.replace("afid","");//切割前面的afid只保留afid号
                PalmchatLogUtils.d("==", "id是多少:" + id);
                if(TextUtils.isDigitsOnly(id)){//判断是否全是数字
                    showProgressDialog();
                    mAfCorePalmchat.AfHttpGetInfo(new String[]{id}, Consts.REQ_GET_INFO_EX,null,0, this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Intent intent = new Intent();
            intent.putExtra("text",str);//传递信息给二维码扫描显示界面
            intent.setClass(CaptureActivity.this,QrcodeScanningTextActivity.class);
            startActivity(intent);
            finish();
        }
	}

    /**
     * 显示dialog
     */

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(this,R.style.Theme_LargeDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_loading);
            ((TextView) dialog.findViewById(R.id.textview_tips)).setText(R.string.searching);

        }
        dialog.show();
    }

    /**
     * 关闭dialog
     */

    private void dismissDialog() {
        if (null != dialog && dialog.isShowing()){
            try {
                dialog.cancel();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            displayFrameworkBugMessageAndExit();
        }
    }
    /**
     * 摄像头有问题的情况下执行
     * */
    private void displayFrameworkBugMessageAndExit() {
        AppDialog dialog  = new AppDialog(this);
        dialog.createOKDialog(this, getString(R.string.canera_without_permission), new AppDialog.OnConfirmButtonDialogListener() {
            @Override
            public void onRightButtonClick() {
                CaptureActivity.this.finish();
            }
            @Override
            public void onLeftButtonClick() {

            }
        });
        dialog.show();
        return;
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /*
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;
        PalmchatLogUtils.d("==","camerawidth:"+cameraWidth +"===="+"cameraheight:"+cameraHeight);
        // 获取布局中扫描框的位置信息
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        // 获取布局容器的宽高
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        // 计算最终截取的矩形的左上角顶点x坐标
        int x = cropLeft * cameraWidth / containerWidth;
        // 计算最终截取的矩形的左上角顶点y坐标
        int y = cropTop * cameraHeight / containerHeight;

        // 计算最终截取的矩形的宽度
        int width = cropWidth * cameraWidth / containerWidth;
        // 计算最终截取的矩形的高度
        int height = cropHeight * cameraHeight / containerHeight;

        // 生成最终的截取的矩形
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void toProfile(AfFriendInfo afFriendInfo) {
        Intent intent = new Intent(this, ProfileActivity.class);
        AfProfileInfo info = AfFriendInfo.friendToProfile(afFriendInfo);
        intent.putExtra(JsonConstant.KEY_PROFILE, info);
        intent.putExtra(JsonConstant.KEY_FLAG, true);
        intent.putExtra(JsonConstant.KEY_AFID, info.afId);
        intent.putExtra(JsonConstant.KEY_USER_MSISDN, info.user_msisdn);
        startActivity(intent);
    }

    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
        dismissDialog();//关闭搜索Dialog
        finish();//关闭扫描界面
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                // search afid on net
                case Consts.REQ_GET_INFO_EX:
                    if (result != null) {
                        AfFriendInfo afFriendInfo = (AfFriendInfo) result;
                        if (CacheManager.getInstance().getMyProfile().afId.equals(afFriendInfo.afId)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(JsonConstant.KEY_AFID, afFriendInfo.afId);
                            Intent intent = new Intent(this, MyProfileActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            toProfile(afFriendInfo);
                        }

                    } else {
                        ToastManager.getInstance().show(mContext, R.string.afid_not_found);
                    }

                    break;

            }
        } else {

            switch (flag) {
                // search afid on net
                case Consts.REQ_GET_INFO_EX:

                    if (code == Consts.REQ_CODE_UNNETWORK) {
                        ToastManager.getInstance().show(mContext, R.string.network_unavailable);
                    } else {
                        ToastManager.getInstance().show(mContext, R.string.afid_not_found);
                    }

                    break;

            }

        }

    }
}
