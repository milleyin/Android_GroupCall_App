package com.afmobi.palmchat.ui.activity.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.qrcode.activity.CaptureActivity;
import com.afmobi.palmchat.ui.customview.RoundImageView;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.universalimageloader.core.assist.FailReason;
import com.afmobi.palmchat.util.universalimageloader.core.listener.ImageLoadingListener;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Hashtable;

/**
 * Created by heguiming on 2015/11/2.
 */
public class MyQrCodeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 自己的个人信息
     */
    public AfProfileInfo afProfileInfo;

    /**
     * 跳转到扫描框的按钮
     */
    private Button mBtnQrcodeReader;

    /**
     * 退出界面按钮
     */
    private ImageView back_button;
    /**
     * 显示二维码
     */
    private ImageView QrImg;
    private RoundImageView headImageView;
    /**
     * 图片宽度的一般
      */
    private static final int IMAGE_HALFWIDTH = 40;
    /*二维码区域尺寸*/
    private static final int QR_SIZE=400;
    /**
     * 插入到二维码里面的图片对象
     */
    private Bitmap mIcon;
    /**
     * 前景色
     */
    int FOREGROUND_COLOR=0xff000000;
    /**
     * 背景色
     */
    int BACKGROUND_COLOR=0x00000000;

    /**
     * 保存自己的afid
     */
    private String afid;

    private TextView mTitleText;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void findViews() {
        setContentView(R.layout.show_qrcode);
        afProfileInfo = CacheManager.getInstance().getMyProfile();

        afid = afProfileInfo.afId.replace("a","");
        mBtnQrcodeReader =(Button)findViewById(R.id.btn_qrcode_reader);
        mBtnQrcodeReader.setOnClickListener(this);
        back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnClickListener(this);
        mTitleText = (TextView)findViewById(R.id.title_text);
        mTitleText.setText(R.string.my_qrcode);

        QrImg = (ImageView) findViewById(R.id.qr_imageview);
        headImageView=(RoundImageView) findViewById(R.id.head_imageview);
        setHeadImage();
        QrCodeGenerated();
    }

    /**
     * 设置二维码中间的头像
     */
    private void setHeadImage(){
        byte sex = afProfileInfo.sex;
        float qr_layoutSize=getResources().getDimension(R.dimen.qr_img_width);
        /**获取自己本地头像的方法*/
        String imgpath = RequestConstant.IMAGE_UIL_CACHE +  PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.
                getAvatarUrlKey(afProfileInfo.getServerUrl(), afProfileInfo.afId, afProfileInfo.getSerialFromHead(), Consts.AF_HEAD_MIDDLE ));

        Bitmap imgHead=  ImageManager.getInstance().loadLocalImageSync(imgpath,false);
        if(imgHead!=null){
            headImageView.setImageBitmap(imgHead);
        }else{
            if ( sex == Consts.AFMOBI_SEX_MALE) {
                headImageView.setImageResource( R.drawable.head_male2 ) ;
            }else{
                headImageView.setImageResource( R.drawable.head_female2 ) ;
            }
        }
        headImageView.getLayoutParams().width=(int)(  2*IMAGE_HALFWIDTH*qr_layoutSize/QR_SIZE);// (int)(zW*headImageView.getWidth()/2);
        headImageView.getLayoutParams().height= (int)(  2*IMAGE_HALFWIDTH*qr_layoutSize/QR_SIZE);// (int)(zW*headImageView.getHeight()/2);/

    }
    private    Bitmap getCroppedBitmap(Bitmap bmp) {
        int radius = Math.min(bmp.getHeight() / 2, bmp.getWidth() / 2);
        bmp = getCroppedRoundBitmap(bmp, radius);
        return bmp ;
    }
    /**
     * 获取裁剪后的圆形图片
     *
     * @param radius
     *            半径
     */
    private   Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;

        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
                    squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
                    diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
	        bmp.recycle();
	        squareBitmap.recycle();
	        scaledSrcBmp.recycle();
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }
    /**
     * 点击事件
     * @param v
     */
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_qrcode_reader:
                startActivity(new Intent(MyQrCodeActivity.this, CaptureActivity.class));
                finish();
                break;
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void init() {

    }

    /**
     * 生成二维码
     */
    private void QrCodeGenerated() {
        String edit = afid;
        if(null != edit && !edit.equals("")){
            try {
                String afid = "afid"+edit;
                String contents = "content";
                JSONObject obj = new JSONObject();
                obj.put("cmd","1002");
                obj.put(contents,afid);
                Bitmap bitmap = createQRBitmap(new String(obj.toString().getBytes()) );

              /*  Bitmap bitmaps = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                        .getHeight(), bitmap.getConfig());
                Bitmap icons = MyQrCodeActivity.zoomBitmap(mIcon, IMAGE_HALFWIDTH);
                Canvas canvas = new Canvas(bitmaps);
                //二维码
                canvas.drawBitmap(bitmap, 0,0, null);
                Paint mPaint = new Paint();
                mPaint.setColor(Color.WHITE);
                mPaint.setAntiAlias(true);
                //绘制白色描边
                canvas.drawCircle(bitmap.getWidth() / 2,bitmap.getHeight()
                        / 2 , (icons.getWidth()  / 2) +ImageUtil.px2dip(this,5),mPaint);
                //logo绘制在二维码中央

                canvas.drawBitmap(icons, bitmap.getWidth() / 2
                        - icons.getWidth() / 2, bitmap.getHeight()
                        / 2 - icons.getHeight() / 2, null);*/
                QrImg.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成二维码 中间插入小图片
     *
     * @param str
     *            内容
     * @return Bitmap
     * @throws WriterException
     */
    public Bitmap createQRBitmap(String str ) throws WriterException {

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);//修改生成二维码白色区域的大小最大是4
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = FOREGROUND_COLOR;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    /**
     *  缩放图片
     * @param icon
     * @param h
     * @return
     */
    public static Bitmap  zoomBitmap(Bitmap icon,int h){
        // 缩放图片
        Matrix m = new Matrix();
        float sx = (float) 2 * h / icon.getWidth();
        float sy = (float) 2 * h / icon.getHeight();
        m.setScale(sx, sy);
        // 重新构造一个2h*2h的图片
        return Bitmap.createBitmap(icon, 0, 0,icon.getWidth(), icon.getHeight(), m, false);
    }

}
