package com.afmobi.palmchat.ui.activity.social;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.db.SharePreferenceService;
import com.afmobi.palmchat.eventbusmodel.ChangeRegionEvent;
import com.afmobi.palmchat.eventbusmodel.LocalsFilterEvent;
import com.afmobi.palmchat.eventbusmodel.UploadHeadEvent;
import com.afmobi.palmchat.location.PLBSCallback;
import com.afmobi.palmchat.location.PLBSEngine;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.chats.PreviewImageActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.HidingScrollListener;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BitmapUtils;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ClippingPicture;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.FileUtils;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.UploadPictureUtils;
import com.afmobi.palmchat.util.UploadPictureUtils.IUploadHead;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfData;
import com.core.AfData.GisData;
import com.core.AfDatingInfo;
import com.core.AfMessageInfo;
import com.core.AfProfileInfo;
import com.core.AfRespAvatarInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeopleInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpProgressListener;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import com.afmobi.palmchat.util.BaiduLocation;
import de.greenrobot.event.EventBus;

public class HomeFragment extends BaseFragment implements AfHttpResultListener, AfHttpProgressListener, IXListViewListener {

    public static final String TAG = HomeFragment.class.getCanonicalName();
//	public static final int SELECT_CRITERIA = 9001;//筛选过滤条件 性别、是否在线等
//	private static final int RESULT_SET_GPS = 9002;//根据定位找附近的人  

    //	private static final int AF_INTENT_ACTION_CAMERA = 111;//拍照用于上传头像
//	private static final int AF_INTENT_ACTION_PICTURE = 222;//相册图片上传头像
    private static final int AF_ACTION_HEAD = 444;//上传完头像后需要执行的操作
    private static final int SHOW_DIALOG = 555;//头像上传时显示上传中的转圈框
    private static final int EARN_FLOWER_COUNT = 20;//上传头像可以获得20朵花

    private static final String IS_ONLINE = "isOnline";//过滤条件 是否在线
    private static final String IS_NEARBY = "isNearby";//过滤条件 是否用定位
    private static final String M_SEX = "mSex";//过滤条件  性别

    private LooperThread looperThread;//处理消息队列

    public AfProfileInfo mProfileInfo;  //自己的Profile
    private PLBSEngine engine; //定位类

    private byte mSex = Consts.AFMOBI_SEX_FEMALE_AND_MALE;
    private boolean mIsOnline, mIsNearby;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private View mNoteParentLayout; //mNoPeopleLayout
    private ViewGroup rl_no_data;
    private TextView tv_no_data;
    private XListView mListView;
    private HomeGridAdapter mAdapterGrid;

    private String sCameraFilename;
    private File fCurrentFile;

//	private TextView //vTextViewGetGiftMessage,
//	mTextNoteUploadPhoto;

    private int PAGE_ID = 0;
    private String pageid_session;//pageid对应的session 加这个变量是因为 pageid跟session关联， 当session变化后pageid也会在服务器重新创建缓存，所以会有问题

    private int START_INDEX = 0;
    private static final int LIMIT = 30;
    int touchSlop = 10;
    private boolean isLoadMore;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    //当前用户是否设置了State
    private boolean isSetState;
    private String myStateName;

    boolean isSelectDB_Data = false;//从是DB里取的缓存数据
    private Button mBtnUpload;
    private ImageView mMyPhoto1;
    private View rl_main;//用到的整个布局文件 RelativeLayout
    private View mV_HeaderView;
    private boolean mBl_init = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();
        mProfileInfo = CacheManager.getInstance().getMyProfile();

        setContentView(R.layout.fragment_home);
        initViews();
        return mMainView;

    }


    private void initViews() {
        rl_main = findViewById(R.id.rl_main);
        rl_no_data = (ViewGroup) findViewById(R.id.rl_no_data);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);

        mV_HeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.homefragmemt_headerview, null);

        mNoteParentLayout = mV_HeaderView.findViewById(R.id.note_layout);
        mBtnUpload = (Button) mV_HeaderView.findViewById(R.id.btn_upload);
//        mNoPeopleLayout = mV_HeaderView.findViewById(R.id.no_people_layout);
//        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        layoutParam.setMargins(10, PalmchatApp.getApplication().getWindowHeight() / 3, 10, 0);
//
//        mNoPeopleLayout.setLayoutParams(layoutParam);
        mListView = (XListView) findViewById(R.id.home_list);
        mListView.addHeaderView(mV_HeaderView, null, true);
        mListView.setHeaderDividersEnabled(false);
        mMyPhoto1 = (ImageView) findViewById(R.id.img_myphoto1);
        mMyPhoto1.setImageResource(mProfileInfo.sex == Consts.AFMOBI_SEX_FEMALE ? R.drawable.head_female2 : R.drawable.head_male2);
        touchSlop = (int) (ViewConfiguration.get(HomeFragment.this.getActivity()).getScaledTouchSlop() * 0.9);//滚动过多少距离后才开始计算是否隐藏/显示头尾元素。这里用了默认touchslop的0.9倍。
        setShowHead(null);
        findViewById(R.id.img_exit).setOnClickListener(this);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mBtnUpload.setOnClickListener(this);
        /*mTextNoteUploadPhoto = (TextView) findViewById(R.id.text_upload_photo_note);
        String uploadPhotoNote = getFragString(R.string.upload_photo_earn_flowers);//.replace("{$targetName}", String.valueOf(EARN_FLOWER_COUNT));
 		mTextNoteUploadPhoto.setText(uploadPhotoNote);*/
        mAdapterGrid = new HomeGridAdapter(fragmentActivity);
        mListView.setAdapter(mAdapterGrid);
        mListView.setHidingScrollListener(new HidingScrollListener() {

            @Override
            public void onMoved(float distance) {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                if (null != activity) {
                    ((MainTab) activity).moveTitle(distance, false);
                }
            }

            @Override
            public void onShow() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onHide() {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                if ((null != activity) && (activity instanceof MainTab)) {
                    ((MainTab) activity).resetTitlePosition();
                }
            }
        });

        setState();
//		vTextViewGetGiftMessage = (TextView) findViewById(R.id.textview_message);
        initLocationEngine();
        EventBus.getDefault().register(this);// 注册
        getServer_DBData(); //获取DB缓存
    }

    /**
     * 设置城市，按同省搜索
     */
    private void setState() {
        myStateName = mProfileInfo.region;
        //判断是否设置了State及设置城市名称
        if (mProfileInfo != null && !TextUtils.isEmpty(myStateName) &&
                !myStateName.toLowerCase().equals(DefaultValueConstant.OTHER.toLowerCase())
                && !myStateName.toLowerCase().equals(DefaultValueConstant.OTHERS.toLowerCase())) {
            isSetState = true;
        } else {
            myStateName = DefaultValueConstant.OTHERS;
            isSetState = false;
        }
    }

//	/**
//	 * if upload photo layout or enter phone number layout should be shown
//	 */
//	private void dispatchNoteLayout() {
//		//	photo is null,show upload photo layout
//		if(shouldUploadPhoto()) {
//			mNoteParentLayout.setVisibility(View.VISIBLE);
//			if (getActivity() != null && getActivity() instanceof MainTab) {
//				((MainTab)getActivity()).updateHead();
//			}
//		} else {
//			mNoteParentLayout.setVisibility(View.GONE);
//		}
//	}

//	/**
//	 * judge if user should upload photo 
//	 */
//	private boolean shouldUploadPhoto() {
//		PalmchatLogUtils.i("head_img_path",CacheManager.getInstance().getMyProfile().head_img_path);
//		if(SharePreferenceUtils.getInstance(getActivity()).getFunctionTips_Key(RequestConstant.IS_SET_HEAD)){
//			SharePreferenceUtils.getInstance(getActivity()).setFunctionTips_Key(RequestConstant.IS_SET_HEAD,false);
//			return false;
//		}
//		return TextUtils.isEmpty(CacheManager.getInstance().getMyProfile().head_img_path);
//	}

    /**
     * 是否弹出修改头像对话框
     *
     * @return
     */
    private void isShowUploadHeadDialog() {

        if ((CacheManager.getInstance().getMyProfile()) != null) {
            if (TextUtils.isEmpty(CacheManager.getInstance().getMyProfile().head_img_path))
                mNoteParentLayout.setVisibility(View.VISIBLE);
            else
                mNoteParentLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
//		MobclickAgent.onPageStart(TAG);
//		loadNewDataIfRegionIsChanged(); //返回的时候判断地区有无改动 有则需要加重新获取列表数据
//		getGiftInAuto();//每天首次登录会送花
//		dispatchNoteLayout();//如果没头像就显示上传头像的提示框，并更新头像
        isShowUploadHeadDialog(); //是否显示头衔上传对话框
    }

	/*
	当用户修改State时，重新加载新数据
	*/
/*	private void loadNewDataIfRegionIsChanged()
	{
		if(getActivity() != null) { 
				boolean isChangeState = SharePreferenceUtils.getInstance(getActivity()).getIsChangeCityForNearBy();
				if (isChangeState) {//当用户改了Profile里的地区时  重新加载数据
					mProfileInfo = CacheManager.getInstance().getMyProfile();
					mAdapterGrid.resetMyProfile();
					setState(); 
					showRefresh();
					SharePreferenceUtils.getInstance(getActivity()).setIsChangeCityForNearBy(false);
				}  
		}
	}*/

    /**
     * 过滤条件设置
     *
     * @param event
     */
    public void onEventMainThread(LocalsFilterEvent event) {
        mListView.setSelection(0);
        isLoadMore = false;
        mSex = event.getSex();
        mIsOnline = event.isOnline();
        mIsNearby = event.isNearBy();
        if (mSex == Consts.AFMOBI_SEX_FEMALE) {
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FILT_FM);
        } else if (mSex == Consts.AFMOBI_SEX_MALE) {
            new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FILT_M);
        }
        rl_no_data.setVisibility(View.GONE);
//        mNoPeopleLayout.setVisibility(View.GONE);
        mListView.setPullLoadEnable(true);

        tv_no_data.setText(getResources().getString(R.string.no_conform_search));

        showRefresh();
    }

    /**
     * 地区改变的通知
     *
     * @param event
     */
    public void onEventMainThread(ChangeRegionEvent event) {
        mProfileInfo = CacheManager.getInstance().getMyProfile();
        mAdapterGrid.resetMyProfile();
        setState();
        showRefresh();
    }

    /**
     * 完整资料界面 改了头像后 进MainTab  后 头像才上传完成  收到这个通知隐藏头像上传框
     *
     * @param event
     */
    public void onEventMainThread(UploadHeadEvent event) {
        isShowUploadHeadDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
//		MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解注册
        if (null != looperThread && null != looperThread.looper) {
            looperThread.looper.quit();
            if (null != looperThread.looperHandler) {
                looperThread.looperHandler.removeCallbacksAndMessages(this);
            }

            looperThread = null;
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(this);
        }
        if (engine != null) {
            engine.exitGeoLoaction();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
		/*case AF_INTENT_ACTION_CAMERA:
		{
//			startPhotoZoom(Uri.fromFile(fCurrentFile), 150);
			if (null != fCurrentFile && resultCode == Activity.RESULT_OK) {
				Bitmap bm = UploadPictureUtils.getInit().smallImage(fCurrentFile);
				UploadPictureUtils.getInit().showClipPhoto(bm, mNoteParentLayout//mOpenGpsView
						, getActivity(), sCameraFilename, new IUploadHead() {
					
					@Override
					public void onUploadHead(String path, String filename) {
						 add by zhh 魅族、HTC等显示手机状态栏适配 
 						CommonUtils.showStatus(getActivity(), rl_main);
						uploadHead(path, filename);
					}

					@Override
					public void onCancelUpload() {
						 add by zhh 魅族、HTC等显示手机状态栏适配 
 						CommonUtils.showStatus(getActivity(), rl_main);
					}
				});
			}
			break;
		}*/

            case MessagesUtils.PICTURE://上传头像
            {
                if (data != null) {//&& resultCode == Activity.RESULT_OK) {
                    if (null != fCurrentFile) {
					/*Bitmap bm = null ;
					if (data.getData() != null) {
						File file = UploadPictureUtils.getInit().getFileFromUri(data.getData(),getActivity());
						bm = UploadPictureUtils.getInit().smallImage(file);  
					}*/
                        Bitmap bm = null;
                        ArrayList<String> arrAddPiclist = data.getStringArrayListExtra("photoLs");
                        String cameraFilename = data.getStringExtra("cameraFilename");
                        String path = null;
                        if (arrAddPiclist != null && arrAddPiclist.size() > 0) {
                            path = arrAddPiclist.get(0);
                        } else if (cameraFilename != null) {
                            path = RequestConstant.CAMERA_CACHE + cameraFilename;
                        }
                        if (path != null) {
                            String largeFilename = Uri.decode(path);
                            File f = new File(largeFilename);
                            File f2 = FileUtils.copyToImg(largeFilename);//copy到palmchat/camera
                            if (f2 != null) {
                                f = f2;
                            }
                            cameraFilename = f2.getAbsolutePath();// f.getName();///mnt/sdcard/DCIM/Camera/1374678138536.jpg
                            cameraFilename = RequestConstant.IMAGE_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.AfResGenerateFileName(AfMessageInfo.MESSAGE_IMAGE);
                            cameraFilename = BitmapUtils.imageCompressionAndSave(f.getAbsolutePath(), cameraFilename);
                            if (CommonUtils.isEmpty(cameraFilename)) {
                                return;
                            }
                            bm = ImageManager.getInstance().loadLocalImageSync(cameraFilename, false);//ImageUtil.getBitmapFromFile(cameraFilename,true);
////					/* add by zhh 魅族、HTC等手机状态栏适配 */
// 					CommonUtils.hideStatus(getActivity(), rl_main);
                            UploadPictureUtils.getInit().showClipPhoto(bm, mNoteParentLayout//mOpenGpsView
                                    , getActivity(), sCameraFilename, new IUploadHead() {

                                        @Override
                                        public void onUploadHead(String path, String filename) {
							/* add by zhh 魅族、HTC等显示手机状态栏适配 */
                                            CommonUtils.showStatus(getActivity(), rl_main);
                                            uploadHead(path, filename);
                                        }

                                        @Override
                                        public void onCancelUpload() {
//							/* add by zhh 魅族、HTC等显示手机状态栏适配 */
                                            CommonUtils.showStatus(getActivity(), rl_main);
                                        }
                                    });
                        }
                    }
                }
                break;
            }
            default:
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 拍照或从相册选照片后 调用上传头像到服务器接口
     *
     * @param path
     * @param filename
     */
    private void uploadHead(String path, String filename) {
        mHandler.sendEmptyMessage(SHOW_DIALOG);
        mAfCorePalmchat.AfHttpHeadUpload(path, filename, Consts.AF_AVATAR_FORMAT, null, this, this);
        setShowHead(path);
        if (getActivity() != null) {
            ((MainTab) getActivity()).display_uploadHead(path);
        }
    }


    /**
     * 开始定位 并处理定位结果
     */
    private void initLocationEngine() {
        if (engine == null) {
            engine = new PLBSEngine(fragmentActivity, new PLBSCallback() {
                @Override
                public void onLocationNotification(int result) {
                    if (result == PLBSCallback.RESULT_LOCATION_SUCCESS) {
                    }
                }
            });

            engine.startLocation();
        }
    }


    /**
     * refresh home list
     *
     * @param chapters
     * @param action   REFRESH_AND_INSERT_DB or
     *                 REFRESH_ONLY
     */
    private void refreshHomeData(AfPeoplesChaptersList chapters, int action) {

        PalmchatLogUtils.println("refreshHomeData chapters.list_chapters " + chapters.list_chapters.size()
                + " chapters.list_peoples size  " + chapters.list_peoples.size());
        if (null != looperThread) {
            Handler handler = looperThread.looperHandler;
            if (null != handler) {
                handler.obtainMessage(LooperThread.REFRESH_HOME_LIST, action, 0, chapters).sendToTarget();
            }
        }

    }


    /**
     * 从服务器获取新的附近或同省的人的列表。用新的PageID
     */
    private void loadData() {
        PAGE_ID = (int) System.currentTimeMillis() + new Random(10000).nextInt();
        pageid_session = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
        isLoadMore = false;
        START_INDEX = 0;
        loadDataFromServer();
    }

    /**
     * 从服务器请求数据
     */
    private void loadDataFromServer() {
        // TODO Auto-generated method stub
        double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
        double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());

        int pageStart = START_INDEX * LIMIT;

        PalmchatLogUtils.e(TAG, "HomeFragment loadDataFromServer pageStart " + pageStart);
        if (!mIsNearby && isSetState) {// mIsNearby:true按照GPS查找，isSetState：按同城搜索
            PalmchatLogUtils.e(TAG, "getHomeData by state");
            mAfCorePalmchat.AfHttpBcNearbyStatePeoples(mSex, PAGE_ID, pageStart, LIMIT, mIsOnline, null, this);
        } else {
            if ((lat == 0.0d && lon == 0.0d) || (lat == 4.9E-324 || lon == 4.9E-324)) {
                PalmchatLogUtils.e(TAG, "getHomeData by state");
                mAfCorePalmchat.AfHttpBcNearbyStatePeoples//AfHttpBcgetPeoplesChaptersByCity
                        (mSex, PAGE_ID, pageStart, LIMIT, mIsOnline, null, this);
            } else {
                PalmchatLogUtils.e(TAG, "getHomeData by gps lat=" + lat + "  lon=" + lon);
                mAfCorePalmchat.AfHttpBcgetPeoplesChaptersByGPS(mSex, PAGE_ID, pageStart, LIMIT, lon, lat, mIsOnline, false, null, this);
            }
        }
    }


    private static final int REFRESH_AND_INSERT_DB = 1;
    private static final int REFRESH_ONLY = 2;

    class LooperThread extends Thread {

        private static final int REFRESH_HOME_LIST = 7002;
        private static final int INSERT_SERVER_DATA_TO_DB = 7004;
        private static final int DELETE_DB_IS_SERVER_DATA = 7005;
        public Handler looperHandler;
        Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();

            looperHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case REFRESH_HOME_LIST:

                            AfPeoplesChaptersList data = (AfPeoplesChaptersList) msg.obj;
                            ArrayList<HomeAdapterData> list = new ArrayList<HomeAdapterData>();


                            for (AfPeopleInfo info1 : data.list_peoples) {
                                HomeAdapterData item = new HomeAdapterData();
                                item.mAfPeopleInfo = info1;
                                list.add(item);
                            }

                            for (AfChapterInfo info2 : data.list_chapters) {
                                HomeAdapterData item2 = new HomeAdapterData();
                                item2.mAfChapterInfo = info2;
                                list.add(item2);
                            }

                            Collections.sort(list, new Comparator<Object>() {

                                public int compare(Object o1, Object o2) {
                                    HomeAdapterData s1 = (HomeAdapterData) o1;
                                    HomeAdapterData s2 = (HomeAdapterData) o2;

                                    int s1Pos = 0;
                                    int s2Pos = 0;

                                    if (s1.mAfChapterInfo != null) {
                                        s1Pos = s1.mAfChapterInfo.pos;
                                    } else if (s1.mAfPeopleInfo != null) {
                                        s1Pos = s1.mAfPeopleInfo.pos;
                                    }

                                    if (s2.mAfChapterInfo != null) {
                                        s2Pos = s2.mAfChapterInfo.pos;
                                    } else if (s2.mAfPeopleInfo != null) {
                                        s2Pos = s2.mAfPeopleInfo.pos;
                                    }

                                    BigDecimal data1 = new BigDecimal(s1Pos);
                                    BigDecimal data2 = new BigDecimal(s2Pos);

                                    return data1.compareTo(data2);
                                }

                            });

                            if (msg.arg1 == REFRESH_AND_INSERT_DB) {
                                refreshSuccess_DelelDBData();

                                Handler handler = looperThread.looperHandler;
                                if (!isLoadMore) {
                                    if (null != handler) {
                                        handler.obtainMessage(LooperThread.INSERT_SERVER_DATA_TO_DB, data).sendToTarget();
                                    }
                                }
                            }


                            mHandler.obtainMessage(REFRESH_HOME_LIST, list).sendToTarget();

                            break;

                        case INSERT_SERVER_DATA_TO_DB:

                            AfPeoplesChaptersList dataServer = (AfPeoplesChaptersList) msg.obj;

//						people
                            ArrayList<AfPeopleInfo> peopleList = dataServer.list_peoples;
                            int size1 = peopleList.size();
                            for (int i = size1 - 1; i >= 0; i--) {
                                AfPeopleInfo people = peopleList.get(i);
                                mAfCorePalmchat.AfDBPepolesInsert(Consts.DATA_HOME_PAGE, people.distance, people.logout_time, people.pos, AfProfileInfo.NearByPeople2Profile(people));
                            }

//						broadcast

//						List<AfChapterInfo> tempAfChapterInfos = new ArrayList<AfChapterInfo>();
                            ArrayList<AfChapterInfo> broadcastMessageList = dataServer.list_chapters;
                            int size = broadcastMessageList.size();
                            PalmchatLogUtils.println("isSelectDB_Data = " + isSelectDB_Data);
                            if (isSelectDB_Data) { //select DB data
                            } else {  // service data
                                for (int i = size - 1; i >= 0; i--) {
                                    AfChapterInfo afChapterInfo = broadcastMessageList.get(i);
                                    if (!isLoadMore) {
                                        afChapterInfo = BroadcastUtil.ServerData_AfDBBCChapterInsert(Consts.DATA_HOME_PAGE, afChapterInfo); // insert DATA to DB
                                    }
                                    String mid = afChapterInfo.mid;
                                    afChapterInfo.isLike = mAfCorePalmchat.AfBcLikeFlagCheck(mid); // check like

                                }
                            }

                            refreshHomeData(dataServer, REFRESH_ONLY);
                            break;
                        case DELETE_DB_IS_SERVER_DATA:
                            isSelectDB_Data = false;
                            int code = mAfCorePalmchat.AfDBPeoplesChaptersDeleteByType(Consts.DATA_HOME_PAGE);
                            PalmchatLogUtils.println("AfDBBCChapterDeleteByType(Consts.DATA_HOME_PAGE) code = " + code);

                        default:
                            break;
                    }
                }
            };

            Looper.loop();
        }

    }

    /**
     * 从DB获取缓存数据
     */
    private void getServer_DBData() {
        AfResponseComm afResponseComm = mAfCorePalmchat.AfDBPeoplesChaptersList(START_INDEX * LIMIT, LIMIT, Consts.DATA_HOME_PAGE);
        if (afResponseComm != null && afResponseComm.obj != null) {
            if (afResponseComm != null) {
                AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                if (afPeoplesChaptersList != null) {
//					ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                    if (afPeoplesChaptersList.list_chapters.size() > 0 || afPeoplesChaptersList.list_peoples.size() > 0) {
                        isSelectDB_Data = true;
                        PalmchatLogUtils.println("HomeFragment getServer_DBData, list_chapters.size()=" + afPeoplesChaptersList.list_chapters.size() + " list_peoples.size() =" + afPeoplesChaptersList.list_peoples.size() + " isSelectDB_Data = " + isSelectDB_Data);

                        refreshHomeData(afPeoplesChaptersList, REFRESH_ONLY);

//						mHandler.postDelayed(new Runnable() {
//
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								if(getActivity()!=null){
//									boolean isFromLogin = ((MainTab)getActivity()).getIsLogin();
//									PalmchatLogUtils.println("--ccddd HomeFragment isFromLogin = " + isFromLogin);
////									if (isFromLogin) {
//										showRefresh();//只要初始化完成 就要去刷一次数据
////									}
//								}
//							}
//						}, 600);
                    } else {
                        //showRefresh();
                    }
                }
            }
        } else {
            showRefresh();
        }
    }


    /**
     * 再加入新数据到DB前 先删除当前的
     */
    private void refreshSuccess_DelelDBData() {
        if (!isLoadMore) {
            Handler handler = looperThread.looperHandler;
            if (null != handler) {
                handler.sendEmptyMessage(LooperThread.DELETE_DB_IS_SERVER_DATA);
            }
        }

    }


    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LooperThread.REFRESH_HOME_LIST:
                    ArrayList<HomeAdapterData> listTmp = (ArrayList<HomeAdapterData>) msg.obj;
                    setAdapter(listTmp);
                    settingStop();
                    break;

                case AF_ACTION_HEAD://上传完头像后 更新本地头像
                    if (fragmentActivity != null) {
                        fragmentActivity.dismissProgressDialog();
                    }
                    if (fCurrentFile != null) {
                        fCurrentFile.delete();
                    }

                    Object result = msg.obj;
                    StringBuffer head_image_path = new StringBuffer();
                    if (((AfRespAvatarInfo) (result)).afid != null) {
                        head_image_path.append(((AfRespAvatarInfo) (result)).afid);
                    }
                    if (((AfRespAvatarInfo) (result)).serial != null) {
                        head_image_path.append(",");
                        head_image_path.append(((AfRespAvatarInfo) (result)).serial);
                    }
                    if (((AfRespAvatarInfo) (result)).host != null) {
                        head_image_path.append(",");
                        head_image_path.append(((AfRespAvatarInfo) (result)).host);
                    }

                    AfProfileInfo profile = CacheManager.getInstance().getMyProfile();
                    if (mProfileInfo != null) {
                        mProfileInfo.head_img_path = head_image_path.toString();
                        profile.head_img_path = head_image_path.toString();
                        byte sex = 0;
                        //WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
                        ImageManager.getInstance().DisplayAvatarImage_PhotoWall(mMyPhoto1, mProfileInfo.getServerUrl()
                                , mProfileInfo.afId, Consts.AF_HEAD_MIDDLE, sex, mProfileInfo.getSerialFromHead(), true, true, null);
                    }


                    PalmchatLogUtils.i(TAG, "head_image_path = " + head_image_path.toString());
                    mAfCorePalmchat.AfDbProfileSaveOrUpdate(Consts.AFMOBI_DB_TYPE_PROFILE, mProfileInfo);

                    EventBus.getDefault().post(new UploadHeadEvent());//通知maintab更新头像
//				dispatchNoteLayout();
//				MainTab mainTab =(MainTab)getActivity();
//				if (mainTab != null) {
//					((MainTab)getActivity()).updateHead();
//				}
                    break;

                case SHOW_DIALOG://显示上传头像对话框
                    fragmentActivity.showProgressDialog(getFragString(R.string.uploading));
                    break;
            }
        }


    };


    /**
     * 当两次点击Local时 回到顶部
     */
    public void setListScrolltoTop() {
        if (mListView != null && mListView.getCount() > 0) {
            mListView.smoothScrollToPosition(0);
        }
        mListView.setListScrolltoTop();
    }

    //TopDialog dialog;
    private int GIS_SEARCH = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_upload:
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.L_SET_LH);
//			MobclickAgent.onEvent(context, ReadyConfigXML.L_SET_LH);

                alertMenu();
                break;
            //exit upload photo layout
            case R.id.img_exit:
                mNoteParentLayout.setVisibility(View.GONE);
            default:
                break;
        }

    }

    //upload photo methods
    private void alertMenu() {
		/*DialogItem[] items = new DialogItem[] {
				new DialogItem(R.string.camera,
						R.layout.custom_dialog_normal),
				new DialogItem(R.string.gallary,
						R.layout.custom_dialog_normal),
				new DialogItem(R.string.cancel,
						R.layout.custom_dialog_cancel) };
		ListDialog dialog = new ListDialog(context, Arrays.asList(items));
		dialog.setItemClick(HomeFragment.this);
		dialog.show();*/
        if (getActivity() != null) {
            createLocalFile();
            Intent mIntent = new Intent(getActivity(), PreviewImageActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt("size", 1);
            mIntent.putExtras(mBundle);
            startActivityForResult(mIntent, MessagesUtils.PICTURE);
        }

    }

    private void setAdapter(List<HomeAdapterData> nearPeopleList) {
        rl_no_data.setVisibility(View.GONE);
//        mNoPeopleLayout.setVisibility(View.GONE);
        mListView.setPullLoadEnable(true);
        mListView.setVisibility(View.VISIBLE);
        for (int i = 0; i < nearPeopleList.size(); i++) {
            AfChapterInfo afChapterInfo = nearPeopleList.get(i).mAfChapterInfo;
            if (afChapterInfo != null) {
                nearPeopleList.get(i).mAfChapterInfo.isLike = mAfCorePalmchat.AfBcLikeFlagCheck(afChapterInfo.mid);
            }

        }
        mAdapterGrid.notifyDataSetChanged(nearPeopleList, isLoadMore, false);
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.e(TAG, "--HomeFragment--AfOnResult:--flag:" + flag + "----code:" + code);

        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_BCGET_PEOPLES_BY_STATE://通过省获取
                    AfResponseComm responseComm = (AfResponseComm) result;
                    AfPeoplesChaptersList chapters = (AfPeoplesChaptersList) responseComm.obj;
                    if (chapters != null && chapters.list_peoples != null && chapters.list_peoples.size() > 0) {
                        mListView.setPullLoadEnable(true);
                        refreshHomeData(chapters, REFRESH_AND_INSERT_DB);
                    } else {
                        if (START_INDEX < 1) {
                            settingStop();
                            mAdapterGrid.notifyDataSetChanged(null, false, false);
                            rl_no_data.setVisibility(View.VISIBLE);


//                            mNoPeopleLayout.setVisibility(View.VISIBLE);
                            mListView.setPullLoadEnable(false);
                            mAfCorePalmchat.AfDBPeoplesChaptersDeleteByType(Consts.DATA_HOME_PAGE);
                        } else if (isAdded()) {
                            ToastManager.getInstance().show(getActivity(), mFragmentVisible, R.string.no_data);
                            mListView.setPullLoadEnable(false);
                            settingStop();
                        }
                    }
                    break;

                case Consts.REQ_BCGET_PEOPLES_COMMENTS_BY_GPS://通过经纬度获取
                    AfResponseComm responseComm2 = (AfResponseComm) result;
                    AfPeoplesChaptersList chapters2 = (AfPeoplesChaptersList) responseComm2.obj;
                    if (chapters2 != null && chapters2.list_peoples != null && chapters2.list_peoples.size() > 0) {
                        mListView.setPullLoadEnable(true);
                        refreshHomeData(chapters2, REFRESH_AND_INSERT_DB);
                    } else {
                        if (START_INDEX < 1) {
                            settingStop();
                            mAdapterGrid.notifyDataSetChanged(null, false, false);
                            rl_no_data.setVisibility(View.VISIBLE);

//                            mNoPeopleLayout.setVisibility(View.VISIBLE);
                            mListView.setPullLoadEnable(false);
                            mAfCorePalmchat.AfDBPeoplesChaptersDeleteByType(Consts.DATA_HOME_PAGE);
                        } else if (isAdded()) {
                            ToastManager.getInstance().show(getActivity(), mFragmentVisible, R.string.no_data);
                            mListView.setPullLoadEnable(false);
                            settingStop();
                        }
                    }
                    break;
                case Consts.REQ_FLAG_GET_GIS_LOCATION:
                    AfData afData = (AfData) result;
                    if (afData != null) {
                        int pageStart = START_INDEX * LIMIT;
                        GisData gisData = afData.mGisData;
                        double lat = gisData.lat;
                        double lon = gisData.lng;
                        if (!mIsNearby && isSetState) {
                            PalmchatLogUtils.e(TAG, "getHomeData by city2");
                            mAfCorePalmchat.AfHttpBcNearbyStatePeoples(mSex, PAGE_ID, pageStart, LIMIT, mIsOnline, null, this);
                        } else {
                            if ((lat == 0.0d && lon == 0.0d) || (lat == 4.9E-324 || lon == 4.9E-324)) {
                                PalmchatLogUtils.e(TAG, "getHomeData by city2");
                                mAfCorePalmchat.AfHttpBcNearbyStatePeoples(mSex, PAGE_ID, pageStart, LIMIT, mIsOnline, null, this);
                            } else {
                                PalmchatLogUtils.e(TAG, "getHomeData by gps2 lat=" + lat + "  lon=" + lon);
                                SharePreferenceUtils.getInstance(context).setLatitudeAndLongitude(lat, lon);
                                mAfCorePalmchat.AfHttpBcgetPeoplesChaptersByGPS(mSex, PAGE_ID, pageStart, LIMIT, lon, lat, mIsOnline, false, GIS_SEARCH, this);
                            }
                        }
                    }
                    break;


                //upload photo
                case Consts.REQ_AVATAR_UPLOAD: {
                    new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LSET_LH_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.LSET_LH_SUCC);
                    //上传成功
                    PalmchatLogUtils.println("---cccaaa avatar upload success!!!" + user_data + "->result = " + result);
                    copy(AF_ACTION_HEAD, result);
//				mAfCorePalmchat.AfHttpGiveGifts(Consts.GIVE_GITFS_TYPE_PHOTO, Consts.GIVE_GITFS_TYPE_PHOTO, this);
                    break;
                }
                default:
                    break;
            }

        } else {
            if (flag == Consts.REQ_AVATAR_UPLOAD) {
                if (fragmentActivity != null) {
                    fragmentActivity.dismissProgressDialog();
                }
                setShowHead(null);
                EventBus.getDefault().post(new UploadHeadEvent());//通知maintab更新头像
//				if (getActivity() != null) {
//					((MainTab)getActivity()).updateHead();
//				}
            }
            switch (flag) {
                case Consts.REQ_BCGET_PEOPLES_BY_STATE:
                case Consts.REQ_BCGET_PEOPLES_COMMENTS_BY_GPS:
//					pageid out of date
                    if (code == Consts.REQ_CODE_104) {
                        loadData();
                    } else {
                        settingStop();
                        if (START_INDEX > 0) {
                            START_INDEX--;
                        }
                        if (isAdded()) {
                            Consts.getInstance().showToast(context, code, flag, http_code, mFragmentVisible);

                        }

                    }
                    break;


                case Consts.REQ_GIFT_GIVE_BY_SIGN:
                    if (code == Consts.REQ_CODE_106) {
                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.GET_GIFT_ERR);
//						MobclickAgent.onEvent(context, ReadyConfigXML.GET_GIFT_ERR);
                    }
                    break;

            }
        }
    }

    /**
     * 设置自己的头像
     *
     * @param filepath
     */
    public void setShowHead(String filepath) {
        if (TextUtils.isEmpty(filepath)) {
			/*AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
			//WXl 20151013 调UIL的显示头像方法, 替换原来的AvatarImageInfo.统一图片管理
			 ImageManager.getInstance().DisplayAvatarImage( mMyPhoto1,  profileInfo.getServerUrl() 
						  ,profileInfo.getAfidFromHead() ,Consts.AF_HEAD_MIDDLE,  profileInfo.sex,profileInfo.getSerialFromHead(),false ,null); */
        } else {
			/*Bitmap bitmap = ImageManager.getInstance().loadLocalImageSync(filepath,false);//ImageUtil.getBitmapFromFile(filepath);
			mMyPhoto1.setImageBitmap(ImageUtil.toRoundCorner(bitmap));*/
            ImageManager.getInstance().displayLocalImage_circle(mMyPhoto1, filepath, 0, 0, null);
        }
    }

    /**
     * 拷贝头像文件到缓存目录
     *
     * @param type
     * @param result
     */
    private void copy(final int type, final Object result) {
        String path = fCurrentFile.getAbsolutePath();
        AfRespAvatarInfo info = (AfRespAvatarInfo) (result);
		/*String fileName = CacheManager.getInstance().getMyProfile().afId + Consts.AF_HEAD_MIDDLE + info.serial;
		File outFile = new File(RequestConstant.AVATAR_CACHE
				+ Consts.AF_HEAD_MIDDLE, fileName);*/
        AfProfileInfo profileInfo = CacheManager.getInstance().getMyProfile();
        if (profileInfo != null) {
            String pathDestination = RequestConstant.IMAGE_UIL_CACHE + PalmchatApp.getApplication().mAfCorePalmchat.getMD5_removeIpAddress(CommonUtils.
                    getAvatarUrlKey(profileInfo.getServerUrl(), profileInfo.afId, info.serial, Consts.AF_HEAD_MIDDLE));
            File outFile = new File(pathDestination);
            FileUtils.copyToImg(path, outFile.getAbsolutePath());

            mHandler.obtainMessage(type, result).sendToTarget();
            if (fCurrentFile != null) {
                fCurrentFile.delete();
            }
        }
    }

    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (!isRefreshing) {
                if (mBl_init) {
                    ((MainTab) getActivity()).resetTitlePosition();
                }
                mBl_init = true;
                mListView.hideFooter();
                isRefreshing = true;
                isLoadMore = false;
                START_INDEX = 0;
                loadData();
                PalmchatLogUtils.println("--gggdd home onRefresh");
                two_minutes_Cancel_Refresh_Animation();
            }
        } else {
            ToastManager.getInstance().show(context, mFragmentVisible, R.string.network_unavailable);
            stopRefresh();
            if (mBl_init) {
                ((MainTab) getActivity()).resetTitlePosition();
            }
            mBl_init = true;
        }
    }


    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (!isLoadingMore) {
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LOOK_MORE);
//				MobclickAgent.onEvent(context, ReadyConfigXML.LOOK_MORE); 
                isLoadMore = true;
                isLoadingMore = true;
                isRefreshing = false;
                START_INDEX++;
                if (isSelectDB_Data //如果目前的数据是从数据库本地读取的 显示更多 就等于是 需要刷新（常见于进主页面后网络不好刷新失败还是用缓存的情况）
                        || (!TextUtils.isEmpty(pageid_session)//或pageid对应的session跟之前的已经不一致了 说明后台登陆过了 也要重新刷了
                        && !pageid_session.equals(PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession()))) {
                    showRefresh();
                } else {
                    loadDataFromServer();
                }

                PalmchatLogUtils.println("--gggdd home onLoadMore");
            }
        } else {
            ToastManager.getInstance().show(context, mFragmentVisible, context.getString(R.string.network_unavailable));
            stopLoadMore();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((!mBl_init) && (isVisibleToUser) && mListView != null) {
            PalmchatLogUtils.i(TAG, "----wx------isVisibleToUser----showRefresh");
            showRefresh();
        }
    }

    public void settingStop() {
        PalmchatLogUtils.println("--cccddss home settingStop isRefreshing = " + isRefreshing);
        if (isRefreshing) {
            stopRefresh();
        }

        if (isLoadingMore) {
            stopLoadMore();
        }

    }

    private void stopRefresh() {
        // TODO Auto-generated method stub
        isRefreshing = false;
        mListView.stopRefresh2();
        mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));

    }


    private void stopLoadMore() {
        isLoadingMore = false;
        mListView.stopLoadMore();
    }


    /**
     * call this method will trigger onRefresh(View view) callback
     */
    private void showRefresh() {
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (getActivity() != null && mListView != null) {
                int px = AppUtils.dpToPx(getActivity(), 60);
                mListView.performRefresh(px);
            }
        } else {
            ToastManager.getInstance().show(context, mFragmentVisible, context.getString(R.string.network_unavailable));
            stopRefresh();
        }
    }

    /**
     * 刷新2分钟没响应 停止刷新动画
     */
    public void two_minutes_Cancel_Refresh_Animation() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (isRefreshing) {
                    settingStop();
                    PalmchatLogUtils.e(TAG, "---two_minutes_Cancel_Refresh_Animation");
                    ToastManager.getInstance().show(context, mFragmentVisible, context.getString(R.string.network_unavailable));
                }
            }
        }, Constants.TWO_MINUTER);
    }

    /**
     * 为图片剪切创建文件
     */
    private void createLocalFile() {
        sCameraFilename = ClippingPicture.getCurrentFilename();
        PalmchatLogUtils.e("camera->", sCameraFilename);
        SharePreferenceService.getInstance(context).savaFilename(sCameraFilename);
        fCurrentFile = new File(RequestConstant.CAMERA_CACHE, sCameraFilename);
        CacheManager.getInstance().setCurFile(fCurrentFile);
    }
/*	private void startCamearPicCut() { 
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		createLocalFile();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fCurrentFile));
		startActivityForResult(intent, AF_INTENT_ACTION_CAMERA);
	}
	
	private void startImageCaptrue() {
		createLocalFile();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, AF_INTENT_ACTION_PICTURE);

	}*/

	/*@Override
	public void onItemClick(DialogItem item) {
		switch (item.getTextId()) {
		case R.string.camera: {
			startCamearPicCut();
			break;
		}
		case R.string.gallary: {
		 startImageCaptrue();
			break;
		}
		default:
			break;
		}
	}*/


    @Override
    public void AfOnProgress(int httpHandle, int flag, int progress, Object user_data) {
        // TODO Auto-generated method stub

    }

    /**
     * 列表停止滑动
     */
    public void stopListViewScroll() {
        if (null != mListView) {
            mListView.stopListViewScroll();
        }
    }

}

