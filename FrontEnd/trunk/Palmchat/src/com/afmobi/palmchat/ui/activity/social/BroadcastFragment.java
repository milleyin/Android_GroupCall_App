package com.afmobi.palmchat.ui.activity.social;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.FacebookConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.ChangeRegionEvent;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobi.palmchat.eventbusmodel.RefreshFollowerFolloweringOrGroupListEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.network.dataparse.CommonJson;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.SoftkeyboardActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.HidingScrollListener;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.CutstomEditTextBanLineKey;
import com.afmobi.palmchat.ui.customview.EditListener;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText.IOnKeyboardStateChangedListener;
import com.afmobi.palmchat.ui.customview.videoview.CustomVideoController;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.AnimationController;
import com.afmobi.palmchat.util.AppUtils;
//import com.afmobi.palmchat.util.BaiduLocation;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.AfResponseComm.AfPulishInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
//import com.umeng.analytics.MobclickAgent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import de.greenrobot.event.EventBus;

/**
 * 5.0.2以前Broadcast是一页的，现在分为三页，BroadcastFragment变为 BroadcastTab里的一个NewAround的子页
 * 2015 1115 改为 去掉GPS获取广播的逻辑  如果是Others的 那就发Others
 */
public class BroadcastFragment extends BaseFragment implements
        OnClickListener, IXListViewListener, AfHttpResultListener, AdapterBroadcastMessages.IFragmentBroadcastListener, OnItemClickListener {

    public static final String TAG = BroadcastFragment.class.getSimpleName();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    public XListView mListView;
    private TextView tv_notice_sencond;
    private AfProfileInfo afProfileInfo;
    private AdapterBroadcastMessages adapter;
    public View send_bc_failed_notification,//广播发送失败栏
            notification;
    private LooperThread looperThread;

    boolean isSelectDB_Data = false; //是否是缓存数据
    private View lin_no_data;

    private View send_button, vImageEmotion;//,rl_hottoday;//r_discover
    private LinearLayout chatting_emoji_layout;
    private EmojjView emojjView;
    private CutstomEditTextBanLineKey vEditTextContent;
    public View chatting_options_layout, input_box_area;

    private EditListener editListener;
    private KeyboardListenRelativeLayoutEditText bc_main;

    public int softkeyboard_H = -1;
    boolean emotion_isClose = false;
    boolean SoftKeyboard_opne_close = false;

    /**
     * 没有数据的时候提示语句和选择区域
     */
    private LinearLayout mBroadcastSelectionRegion;

    /**
     * 点击here后弹出Dialog选择区域
     */
    private Dialog mDialog;

    private AreaListAdapter mAreaListAdapter;

    public static ListView mAreaListView;

    private int checkedIndex = -1;

    /**
     * 这里之所以要用LOADTYPE_WHEN_REQ_NEW 而不用isLoadMore 是因为isLoadMore是在上拉的时候就会触发，这样就会有如果先刷新 再更多  刷新返回时 isLoadMore这个状态是错的，导致会有重复广播
     */
    public static final int LOADTYPE_WHEN_REQ_NEW = 0;//请求的时候是刷新
    public static final int LOADTYPE_WHEN_REQ_LOADMORE = 1;//请求的时候是loadmore
    public static final int LOADTYPE_WHEN_REQ_DB = 2;//数据库里请求
    private AfChapterInfo mCurAfChapterInfo = null;

    /**
     * 分享facebook类型
     */
    private enum PendingAction {
        NONE, POST_PHOTO, POST_MSG
    }

    /***/
    private PendingAction mPendingAction = PendingAction.NONE;
    /**
     * 回调facebooksdk
     */
    private CallbackManager mCallbackManager;
    private LinearLayout viewNewContents;
    /**
     * 初始化刷新
     */
    private boolean mIsInitRefresh;
    /**
     * listview分割线长度
     */
    private int mDivideHeight;
    private float mDiffValue_newContents;
    private float mOriCoord_newContents;
    private State mState_newContent = State.NONE;
    private static final int SPACE = 30;

    private enum State {NONE, SHOW, HIDE}


    /**
     * ;
     * <p/>
     * 视频相关
     */
    private Rect rect;


    private Runnable mRunnable = new Runnable() {
        public void run() {
            mHandler.removeCallbacks(mRunnable);
            showShowAnimation();
        }
    };

    /**
     * 分享回调
     */
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            PalmchatLogUtils.i(TAG, "-------wx-----shareCallback---error-" + error.toString());
            Message msg = new Message();
            msg.what = DefaultValueConstant.SHAREFACEBOOK_FAILURE;
            msg.obj = error;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            PalmchatLogUtils.i(TAG, "-------wx-----shareCallback---result-" + result.toString());
            mHandler.sendEmptyMessage(DefaultValueConstant.SHAREFACEBOOK_SUCCESS);

        }
    };

    /**
     * fackbook回调
     */
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            PalmchatLogUtils.i(TAG, "--FacebookCallback---loginResult----" + loginResult.toString());
            mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_SUCCESS);

        }

        @Override
        public void onCancel() {
            PalmchatLogUtils.i(TAG, "--FacebookCallback---onCancel----");
            mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_CANCEL);
        }

        @Override
        public void onError(FacebookException exception) {
            PalmchatLogUtils.i(TAG, "--FacebookCallback---exception----" + exception.toString());
            mHandler.sendEmptyMessage(DefaultValueConstant.LOGINFACEBOOK_FAILURE);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LooperThread.INSERT_SERVER_DATA_TO_DB:
                    List<AfChapterInfo> list_aAfChapterInfos = (List<AfChapterInfo>) msg.obj;
                    int loadTypeWhenReq = msg.arg1;
                    adapter.notifyDataSetChanged(list_aAfChapterInfos, loadTypeWhenReq == LOADTYPE_WHEN_REQ_LOADMORE);
                    if (loadTypeWhenReq == LOADTYPE_WHEN_REQ_NEW) {//!isLoadMore) {
                        onStop();
                    }
                    if (!isSelectDB_Data) {
                        settingStop();
                    }
                    isSelectDB_Data = false;//服务器返回数据了  当前数据为缓存状态的标志设false
                    break;


                case LooperThread.INIT_FINISH:
                    if (looperThread != null && looperThread.looperHandler != null) {
                        looperThread.looperHandler.sendEmptyMessage(LooperThread.GET_BRD_NOTIFICATION_DATA);
//					handler.sendEmptyMessage(LooperThread.GET_BRD_FRIENDCIRCLE_NOTIFICATION_DATA );
                    }
                    break;
                case LooperThread.GET_BRD_NOTIFICATION_DATA:
                    if (!mAfterInit) {
                        break;
                    }

                    if (null == msg.obj) {
                        break;
                    }

                    @SuppressWarnings("unchecked")
                    ArrayList<BroadcastNotificationData> list = (ArrayList<BroadcastNotificationData>) msg.obj;
                    int total = list.size();
                    if (total > 0) {
                        ((MainTab) getActivity()).setBrdNotifyUnread(total);
                    } else {
                        if (getActivity() != null) {
                            ((MainTab) getActivity()).setBrdNotifyUnread(0);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    break;


                case LooperThread.TO_BRD_NOTIFY:
                    CommonUtils.cancelBrdNotification();
                    Intent intent1 = new Intent(context, BroadcastNotificationActivity.class);
                    startActivity(intent1);
                    break;

                case DefaultValueConstant.SHAREFACEBOOK_SUCCESS: {
                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.synchronous_fb_tips_success);
                    break;
                }
                case DefaultValueConstant.SHAREFACEBOOK_FAILURE: {
                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.synchronous_fb_tips);
                    break;
                }
                case DefaultValueConstant.LOGINFACEBOOK_SUCCESS: {
                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.facebook_login_success_tip);
                    handlePendingAction();
                    break;
                }
                case DefaultValueConstant.LOGINFACEBOOK_FAILURE: {
                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.facebook_login_failed_tip);
                    break;
                }
                case DefaultValueConstant.LOGINFACEBOOK_CANCEL: {
                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.facebook_login_cancel_tip);
                    break;
                }
                case DefaultValueConstant.RESETTILEPOSITION: {
                    Activity activity = getActivity();
                    if ((activity != null) && (activity instanceof MainTab)) {
                        ((MainTab) activity).resetTitlePosition();
                    }
                    break;
                }
                case DefaultValueConstant.SHOWFRESHSUCCESS: {
                    Fragment fragment = getParentFragment();
                    if (fragment != null && (fragment instanceof BroadcastTab)) {
                        ((BroadcastTab) fragment).showRefreshSuccess(0);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("zhh_2016_07_22", "BroadcastFragment" + "_onCreate ");
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, mCallback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        looperThread = new LooperThread();
        looperThread.setName(TAG);
        looperThread.start();

        findViews();
        init();
        return mMainView;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("zhh_2016_07_22", "BroadcastFragment" + "_onResume ");

        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);


        if (getUserVisibleHint() && mAfCorePalmchat != null) {
            if (clike_count == 0) {
                SoftKeyboard_opne_close = false;
                getListFormDB();
                get_resend_DB(); // select ressend data
                setSendFailedLayout();
                clike_count++;
            }
        }

//		MobclickAgent.onPageStart(TAG);
        close_inputbox(emotion_isClose);
        if (looperThread != null && looperThread.looperHandler != null) {
            looperThread.looperHandler.sendEmptyMessage(LooperThread.GET_BRD_NOTIFICATION_DATA);
        }
//		loadNewDataIfRegionChanged();

        setSendFailedLayout();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

		/*
    当用户修改city时，重新加载新数据
	*/
    /*private void loadNewDataIfRegionChanged()
    {
		if(getActivity() != null) {
				boolean isChangeCity = SharePreferenceUtils.getInstance(getActivity()).getIsChangeCity();
				if (isChangeCity) {
					afProfileInfo = CacheManager.getInstance().getMyProfile();
					setState();
					showRefresh();
					SharePreferenceUtils.getInstance(getActivity()).setIsIsChangeCity(false);
				}
		}
	}*/


    /**
     * 设置城市，按同省搜索
     */
    /*private void setState(){
        String myStateName = afProfileInfo.region;
		//判断是否设置了city及设置城市名称
		if(afProfileInfo != null && !TextUtils.isEmpty(myStateName) &&
				!myStateName.toLowerCase().equals(DefaultValueConstant.OTHER.toLowerCase())
				&& !myStateName.toLowerCase().equals(DefaultValueConstant.OTHERS.toLowerCase())){
		}
		else{
		}
	}*/
    private void setSendFailedLayout() {
        if (CacheManager.getInstance().getBC_sendFailed().size() > 0) {
        /*	if (mNewNotificationView.getVisibility() == View.GONE) {
                lin_spacing.setVisibility(View.VISIBLE);
			}else {
				lin_spacing.setVisibility(View.GONE);
			}*/
            send_bc_failed_notification.setVisibility(View.VISIBLE);
        } else {
            send_bc_failed_notification.setVisibility(View.GONE);
        }
    }


    public void findViews() {

        setContentView(R.layout.activity_broadcast_messages);

        if (editListener == null) {
            editListener = new EditListener();
        }


        viewNewContents = (LinearLayout) findViewById(R.id.view_newcontents);
        viewNewContents.setOnClickListener(this);

        chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
        send_button = findViewById(R.id.chatting_operate_one);
        input_box_area = findViewById(R.id.input_box_area);
        chatting_options_layout = findViewById(R.id.chatting_options_layout);
        vImageEmotion = findViewById(R.id.image_emotion);
        tv_notice_sencond = (TextView) findViewById(R.id.tv_notice_sencond);
        tv_notice_sencond.setVisibility(View.GONE);
//		friendsNew_ImageView =(ImageView)findViewById(R.id.friends_imageView);

//		rl_hottoday = findViewById(R.id.rl_hottoday);
//		rl_hottoday.setOnClickListener(this);
//		r_discover = findViewById(R.id.r_discover);
//		r_discover.setOnClickListener(this);
        vEditTextContent = (CutstomEditTextBanLineKey) findViewById(R.id.chatting_message_edit);
        vEditTextContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CommonUtils.TEXT_MAX)});
        vEditTextContent.setMaxLength(CommonUtils.TEXT_MAX);
        CommonUtils.setListener(vEditTextContent);
        vEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!CacheManager.getInstance().getEditTextDelete()) {
                    setEdittextListener(s);
                    CacheManager.getInstance().setEditTextDelete(false);
                } else {
                    CacheManager.getInstance().setEditTextDelete(false);
                }

                if (s.length() == CommonUtils.TEXT_MAX) {
                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.comment_long);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s != null) {
                    String string = s.toString();
                    editListener.edittext_string = string;
                    editListener.edittext_length = s.length();
                } else {
                    editListener.edittext_length = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        emojjView = new EmojjView(getActivity());
        emojjView.select(EmojiParser.SUN);
        emojjView.setScroll_parent(View.GONE);
        chatting_emoji_layout.addView(emojjView.getViewRoot());
        send_button.setOnClickListener(this);
        vImageEmotion.setOnClickListener(this);
        vEditTextContent.setOnClickListener(this);
//		friendsNew_ImageView.setOnClickListener(this);
        findViewById(R.id.send_brodacast_pic).setOnClickListener(this);
        findViewById(R.id.send_brodacast_camera).setOnClickListener(this);

        bc_main = (KeyboardListenRelativeLayoutEditText) findViewById(R.id.bc_main);
        bc_main.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

            @Override
            public void onKeyboardStateChanged(int state, int mHeight) {
                // TODO Auto-generated method stub
                switch (state) {
                    case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_HIDE://keyboard hide

//	                	close_inputbox(emotion_isClose);
                        break;
                    case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_SHOW://keyboard show
                        softkeyboard_H = mHeight;
                    default:
                        break;
                }
            }
        });
        mListView = (XListView) findViewById(R.id.listview);
        mDivideHeight = mListView.getDividerHeight();
        mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setHidingScrollListener(new HidingScrollListener() {

            @Override
            public void onMoved(float distance) {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                ((MainTab) activity).moveTitle(distance, true);
            }

            @Override
            public void onShow() {
                // TODO Auto-generated method stub
                Fragment fragment = getParentFragment();
                if (null != fragment) {
                    ((BroadcastTab) fragment).showAnimation();
                }
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


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (VedioManager.getInstance().getVideoController() != null) {
                    if (!VedioManager.getInstance().getVideoController().getVisibleRect(rect)) {
                        VedioManager.getInstance().getVideoController().stop();
                    }
                }

//                int firstItemPosition = firstVisibleItem - mListView.getHeaderViewsCount();
//                int endItemPosition = mListView.getLastVisiblePosition() - (mListView.getHeaderViewsCount() + mListView.getFooterViewsCount());
//                if (mCustomVideoController != null) {
//                    if ((currentIndex < firstItemPosition || currentIndex > endItemPosition) && mCustomVideoController.isPlaying()) {
//                                        if (mCustomVideoController != null) {
//                            mCustomVideoController.stop();
//                          }
//                    }
//                }
            }
        });

//		 lin_broadcast = (LinearLayout)this.findViewById(R.id.lin_broadcast);
//		 lin_mybroadcast = (LinearLayout)this.findViewById(R.id.lin_mybroadcast);
        lin_no_data = findViewById(R.id.header_no_data);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, CommonUtils.getRealtimeWindowHeight(getActivity()) / 8, 0, 0);
        lin_no_data.setLayoutParams(layoutParams);
        View footerView = View.inflate(context, R.layout.activity_broadcast_footerview, null);
        mBroadcastSelectionRegion = (LinearLayout) footerView.findViewById(R.id.broadcast_selection_region);
        mBroadcastSelectionRegion.setVisibility(View.GONE);
        mListView.addFooterView(footerView);

    }


    /**
     * 滚到最顶部
     */
    public void setListScrolltoTop(boolean isFromTab) {
        if (mListView != null && mListView.getCount() > 0) {
            mListView.stopListViewScroll();
            if (!isFromTab) {
                ((MainTab) getActivity()).resetTitlePosition();
            }
            mListView.setListScrolltoTop();
            if (viewNewContents.getY() > DefaultValueConstant.DISTANCE_5) {
                viewNewContents.setY(mOriCoord_newContents);
            }
        }
    }

    int clike_count = 0;

    boolean mAfterInit = false;
    String test_str = "";
    private View brdNotificationView;

    public void init() {
        if (mAfCorePalmchat == null) {
            mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        }
        mAfCorePalmchat.AfDBBrinfoGetReceiveLatestMsgid();
        afProfileInfo = CacheManager.getInstance().getMyProfile();
        /*20160414确认了pageid是再重登后无效的 所以保存也就没有意义  所以去掉
         * long[] lastRefreshData=SharePreferenceUtils.getInstance(context).getNewAroundLastTimeRefresh(afProfileInfo .afId);
		if(lastRefreshData!=null){
			timeLastRefresh=lastRefreshData[0];
			pageid=(int) lastRefreshData[1];
		} */

        brdNotificationView = mListView.getBrdNotifyView();
//		rl_hottoday = findViewById(R.id.rl_hottoday);
//		rl_hottoday.setOnClickListener(this);
//		r_discover = brdNotificationView.findViewById(R.id.r_discover);
//		r_discover.setOnClickListener(this);
//		mNewNotificationView = brdNotificationView.findViewById(R.id.new_notification);
//		lin_spacing = brdNotificationView.findViewById(R.id.lin_spacing);
        notification = brdNotificationView.findViewById(R.id.notification);


        send_bc_failed_notification = brdNotificationView.findViewById(R.id.send_bc_failed_notification);
//		mNotifyCountText = (TextView) brdNotificationView.findViewById(R.id.notiy_count);


//		mNewNotificationView.setOnClickListener(this);
        send_bc_failed_notification.setOnClickListener(this);

        new ReadyConfigXML();

        adapter = new AdapterBroadcastMessages(getActivity(), new ArrayList<AfResponseComm.AfChapterInfo>(), Consts.AFMOBI_BRMSG_INPUT, this, BroadcastDetailActivity.FROM_NEARBY);
        mListView.setAdapter(adapter);
        EventBus.getDefault().register(this);// 注册
//	    lin_broadcast.performClick();
        mAfterInit = true;
    }

    /**
     * 从数据库获得缓存数据
     */
    private void getListFormDB() {
        if (mAfCorePalmchat == null) {
            mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        }
        AfResponseComm afResponseComm = mAfCorePalmchat.AfDBBCChapterGetListEx(START_INDEX * LIMIT, LIMIT, Consts.DATA_BROADCAST_PAGE);
        if (afResponseComm != null && afResponseComm.obj != null) {
            if (afResponseComm != null) {
                AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                if (afPeoplesChaptersList != null) {
                    ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                    if (list_AfChapterInfo.size() > 0) {
                        isSelectDB_Data = true;
                        PalmchatLogUtils.println("getServer_DBData, list_AfChapterInfo.size()=" + list_AfChapterInfo.size() + "isSelectDB_Data = " + isSelectDB_Data);
                        setAdapter(list_AfChapterInfo, Consts.AFMOBI_BRMSG_INPUT, LOADTYPE_WHEN_REQ_DB);

                    }
                }
            }
        }
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (getActivity() != null && getActivity() instanceof MainTab) {
                    showRefresh();//初始化后 都要去取一次新数据
                }
            }
        }, 600);

    }

    /**
     * @param broadcastMessageList
     * @param dataType
     * @param loadTypeWhenReq      请求的时候是否是loadmore
     */
    private void setAdapter(final List<AfChapterInfo> broadcastMessageList, byte dataType, int loadTypeWhenReq) {
        switch (dataType) {
            case Consts.AFMOBI_BRMSG_INPUT:

                if (null == adapter) {
                    adapter = new AdapterBroadcastMessages(getActivity(), broadcastMessageList, Consts.AFMOBI_BRMSG_INPUT, this, BroadcastDetailActivity.FROM_NEARBY);
                    mListView.setAdapter(adapter);
                    hideBlankView();
                } else {
                    final int size = broadcastMessageList.size();
                    if (size > 0) {
                        hideBlankView();
                        showRefreshSuccess();
                        if (looperThread != null && looperThread.looperHandler != null) {
                            Message msgMessage = new Message();
                            msgMessage.obj = broadcastMessageList;
                            msgMessage.arg1 = loadTypeWhenReq;
                            msgMessage.what = LooperThread.INSERT_SERVER_DATA_TO_DB;

                            looperThread.looperHandler.sendMessage(msgMessage);
                        }
                    } else {
                        if (isAdded()) {
                            ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.no_data);
                            mListView.setPullLoadEnable(false);
                        }
                    }
                }
                break;
        }
    }

    private int pageid = 0;
    private String pageid_session;//pageid对应的session 加这个变量是因为 pageid跟session关联， 当session变化后pageid也会在服务器重新创建缓存，所以会有问题
    private int START_INDEX = 0;
    private boolean isLoadMore;
    private static final int LIMIT = 10;

    private void loadData() {
        isLoadMore = false;
        START_INDEX = 0;
        pageid = (int) System.currentTimeMillis() + new Random(10000).nextInt();
        pageid_session = PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession();
        loadDataFromServer(pageid);
        if (adapter.getCount() < 0) {
            hideBlankView();
        }
    }

    /**
     * 如果是翻到了第五页 loadmore 那判断时间有没有大于2分钟 如果是那就要检查有没新内容
     */
    private void checkNewMid() {
        if (START_INDEX >= 4 && System.currentTimeMillis() - timeLastRefresh > 120000
                && (viewNewContents != null && viewNewContents.getVisibility() != View.VISIBLE)
                && afProfileInfo != null) {
            String country = afProfileInfo.country;
            String state = afProfileInfo.region;
            if (!TextUtils.isEmpty(state)) {//省不为空 且不为others &&!DefaultValueConstant.OTHERS.equals(state)
                mAfCorePalmchat.AfHttpBCMsgCheckMId(state, country, null, this);
            }
            timeLastRefresh = System.currentTimeMillis();
        }
    }

    /**
     * 获取广播列表
     *
     * @param pageId
     */
    private void loadDataFromServer(int pageId) {
        double lat = Double.valueOf(SharePreferenceUtils.getInstance(context).getLat());
        double lon = Double.valueOf(SharePreferenceUtils.getInstance(context).getLng());
        if (!isLoadMore) {
            CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_lat_lon(lat, lon);
        }
        // 这里之所以要用LOADTYPE_WHEN_REQ_LOADMORE 而不用isLoadMore 是因为isLoadMore是在上拉的时候就会触发，这样就会有如果先刷新 再更多
        // 刷新返回时 isLoadMore这个状态是错的，导致会有重复广播
        mAfCorePalmchat.AfHttpBcgetChaptersByState(pageId, START_INDEX * LIMIT, LIMIT, isLoadMore ? LOADTYPE_WHEN_REQ_LOADMORE : LOADTYPE_WHEN_REQ_NEW, this);

    }

    /**
     * 发评论
     *
     * @param text
     */
    public void send_comment(final String text) {
        close_inputbox(true);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (adapter != null) {
                    PalmchatLogUtils.e(TAG, "data=" + text);
                    String msg = text;
                    if (!TextUtils.isEmpty(msg)) {
                        String hint_text = vEditTextContent.getHint().toString();
                        String to_afid = adapter.getCommentModel().getTo_afid();
                        if (!hint_text.equals(getString(R.string.hint_commet)) && !TextUtils.isEmpty(to_afid)) {  //@某人时，格式为 @xxxx content
                            msg = hint_text + msg;
                        }
                        adapter.getCommentModel().setMsg(msg);
                        adapter.sendComment();
                    } else {
                        ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.hint_commet);
                    }
                }
            }
        }, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        PalmchatLogUtils.e(TAG, "----onActivityResult----");
        switch (resultCode) {

            case Constants.COMMENT_FLAG:
                if (data != null) {
                    String msg = data.getExtras().getString(JsonConstant.KEY_COMMENT_COUNTENT);
                    String isSend = data.getExtras().getString("isSend");
                    if (isSend.equals("1")) {
                        send_comment(msg);
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
        //because FragmentActivity has tacked on an extra 0x10000 to determine which fragment it came from.
        //This was resolved by making sure the super activity was called during onActivityResult
        //You "Can only use lower 16 bits for requestCode"
        int sanitizedRequestCode = requestCode % 0x10000;
//	   if(null!=mCallbackManager){
//		   mCallbackManager.onActivityResult(sanitizedRequestCode, resultCode, data);
//	   }
    }

    class LooperThread extends Thread {
        private static final int GET_BRD_NOTIFICATION_DATA = 7002;
        private static final int INIT_FINISH = 7003;
        private static final int INSERT_SERVER_DATA_TO_DB = 7004;
        private static final int DELETE_DB_IS_SERVER_DATA = 7005;
        private static final int TO_BRD_NOTIFY = 7006;

        Handler looperHandler;
        Looper looper;
        private boolean isInit;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            looperHandler = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if (!isInit) {
                        isInit = true;
                    }


                    switch (msg.what) {
                        case GET_BRD_NOTIFICATION_DATA:

                            ArrayList<HashMap<String, List<BroadcastNotificationData>>> data = CommonJson.getBrdNotifyDataFromDb(BroadcastNotificationData.STATUS_UNREAD);
                            ArrayList<BroadcastNotificationData> realDisplayList = new ArrayList<BroadcastNotificationData>();

//							compute total brd notify count
//							data[0] like, data[1] comment
                            for (int i = 0; i < data.size(); i++) {
                                HashMap<String, List<BroadcastNotificationData>> map = data.get(i);
                                Iterator<Entry<String, List<BroadcastNotificationData>>> iterator = map.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    Entry<String, List<BroadcastNotificationData>> entry = iterator.next();
                                    List<BroadcastNotificationData> midBrd = entry.getValue();

                                    if (midBrd.size() > 0) {

//									same broadcasd have more than 10 like(or comment), aggregate to 1 item
									/*if (midBrd.size() > BroadcastNotificationActivity.AGGREGATE_SIZE) {

										问了啊橙 说不合并了
										BroadcastNotificationData tmp = midBrd.get(0);
										realDisplayList.add(tmp);

									} else {*/

                                        for (BroadcastNotificationData tmp2 : midBrd) {
                                            realDisplayList.add(tmp2);

                                        }

//									}

                                    }
                                }


                            }

                            mHandler.obtainMessage(GET_BRD_NOTIFICATION_DATA, realDisplayList).sendToTarget();

                            break;

                        case INSERT_SERVER_DATA_TO_DB:
                            List<AfChapterInfo> tempAfChapterInfos = new ArrayList<AfChapterInfo>();
                            ArrayList<AfChapterInfo> broadcastMessageList = (ArrayList<AfChapterInfo>) msg.obj;
                            int loadTypeWhenReq = msg.arg1;
                            int size = broadcastMessageList.size();
                            PalmchatLogUtils.println("isSelectDB_Data = " + isSelectDB_Data);
                            if (isSelectDB_Data) { //select DB data
                                tempAfChapterInfos.addAll(broadcastMessageList);
                            } else {  // service data
                                for (int i = size - 1; i >= 0; i--) {
                                    AfChapterInfo afChapterInfo = broadcastMessageList.get(i);
                                    String mid = afChapterInfo.mid;
								/*if (loadTypeWhenReq==LOADTYPE_WHEN_REQ_NEW){//!isLoadMore) {
									afChapterInfo  = BroadcastUtil.ServerData_AfDBBCChapterInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo); // insert DATA to DB
								}*/
                                    afChapterInfo.isLike = mAfCorePalmchat.AfBcLikeFlagCheck(mid); // check like
                                    tempAfChapterInfos.add(0, afChapterInfo);
                                    /**
                                     * 当刷新的列表里包含了 自己发的那些广播，那就把把发成功列表里的广播删除
                                     * 5.1版本  2015/08/01 WXL注释掉这句，因为存在Broadcast 经纬度 和State两条 不同id的问题，自己发的 会有2个mid。会存在两条。
                                     * 改为只要刷新成功就清掉所有已发广播
                                     */
                                    //CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(mid); // clear send success data
                                }
                                if (size > 0) {
                                    /**
                                     * 5.1版本  2015/08/01  WXL 因为存在Broadcast 经纬度 和State两条 不同id的问题，自己发的 会有2个mid。会存在两条。
                                     * 改为只要刷新成功就清掉所有已发广播
                                     */
                                    CacheManager.getInstance().remove_BC_RecordSendSuccessData_All();
                                }
                                if (loadTypeWhenReq == LOADTYPE_WHEN_REQ_NEW) {//!isLoadMore) {
                                    tempAfChapterInfos = ByteUtils.add_sendSuccessLocalData(tempAfChapterInfos);
                                }
                            }
                            Message msg1 = new Message();
                            msg1.obj = tempAfChapterInfos;
                            msg1.arg1 = loadTypeWhenReq;
                            msg1.what = INSERT_SERVER_DATA_TO_DB;
                            mHandler.sendMessage(msg1);

                            if (!isSelectDB_Data) { //写入数据库
                                if (loadTypeWhenReq == LOADTYPE_WHEN_REQ_NEW) {
                                    for (int i = size - 1; i >= 0; i--) {
                                        BroadcastUtil.ServerData_AfDBBCChapterInsert(Consts.DATA_BROADCAST_PAGE, broadcastMessageList.get(i)); // insert DATA to DB
                                    }
                                }
                            }
                            break;

                        case DELETE_DB_IS_SERVER_DATA:
                            isSelectDB_Data = false;
                            mAfCorePalmchat.AfDBBCChapterDeleteByType(Consts.DATA_BROADCAST_PAGE);
                            break;


                        case TO_BRD_NOTIFY:
                            mAfCorePalmchat.AfDBBCNotifyUpdataAllStatus(CacheManager.getInstance().getMyProfile().afId, BroadcastNotificationData.STATUS_UNREAD, BroadcastNotificationData.STATUS_READ);
                            mHandler.obtainMessage(TO_BRD_NOTIFY).sendToTarget();
                            break;

                        default:
                            break;
                    }
                }
            };
            if (!isInit) {
                mHandler.sendEmptyMessage(INIT_FINISH);
            }
            Looper.loop();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.view_newcontents:
                clearAndRefresh();
                hideNewMid();
                break;
            case R.id.send_bc_failed_notification:
                CommonUtils.to(getActivity(), BroadcastRetryPageActivity.class);
                break;
            case R.id.chatting_operate_one:
                close_inputbox(true);
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (adapter != null) {
                            String msg = vEditTextContent.getText().toString();
                            if (!TextUtils.isEmpty(msg)) {
                                String hint_text = vEditTextContent.getHint().toString();
                                String to_afid = adapter.getCommentModel().getTo_afid();
                                if (!hint_text.equals(getString(R.string.hint_commet)) && !TextUtils.isEmpty(to_afid)) {
                                    msg = hint_text + msg;
                                }
                                adapter.getCommentModel().setMsg(msg);
                                adapter.sendComment();
                            } else {
                                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.hint_commet);
                            }
                        }
                    }
                }, 100);
                break;
            case R.id.chatting_message_edit://onclick on eidttext
                emotion_isClose = true;
                chatting_emoji_layout.setVisibility(View.GONE);
                CommonUtils.showSoftKeyBoard(vEditTextContent);
                break;
            case R.id.image_emotion:
                emotion_isClose = false;
                CommonUtils.closeSoftKeyBoard(vEditTextContent);
                break;
            case R.id.send_brodacast_pic:  //选图片进广播
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_SD_BCM);//统计
//			MobclickAgent.onEvent(getActivity(), ReadyConfigXML.ENTRY_SD_BCM);//统计
/*			intent = new Intent(context, ActivitySendBroadcastMessage.class);
			intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE,JsonConstant.KEY_SENDBROADCAST_TYPE_GALLERY );
			startActivity(intent);*/

                intent = new Intent(context, EditBroadcastPictureActivity.class);
                startActivity(intent);
                break;
            case R.id.send_brodacast_camera: //拍照进入发广播
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_SD_BCM);//统计
//			MobclickAgent.onEvent(getActivity(), ReadyConfigXML.ENTRY_SD_BCM);//统计
                intent = new Intent(context, ActivitySendBroadcastMessage.class);
                intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, Consts.BR_TYPE_VOICE_TEXT);
//			intent.putExtra(JsonConstant.KEY_SENDBROADCAST_TYPE, JsonConstant.KEY_SENDBROADCAST_TYPE_CAMERA);//Camera
                startActivity(intent);
                break;

            case R.id.img_exit:
//			mNoteLayout.setVisibility(View.GONE);
                break;
//			new like comment
	/*	case R.id.new_notification:
			if (looperThread != null && looperThread.looperHandler != null) {
				 looperThread.looperHandler.sendEmptyMessage(LooperThread.TO_BRD_NOTIFY);
			}
			break;*/

            default:
                break;
        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("zhh_2016_07_22", "BroadcastFragment" + "_setUserVisibleHint: " + isVisibleToUser);


        if (isVisibleToUser) {
            checkNewMid();
            MainTab tab = (MainTab) getActivity();
            if (null != tab) {
                if (tab.viewpager.getCurrentItem() == tab.CONTENT_FRAGMENT_BROADCAST) {
                    tab.shouStateBtn();
                }
            }
        } else {
            MainTab tab = (MainTab) getActivity();
            if (null != tab) {
                tab.hideStateBtn();
            }
        }


    }

    /**
     * 显示新广播的提示
     */
    private void showNewMid() {
        if (viewNewContents != null) {
            Fragment fragment = getParentFragment();
            if ((fragment != null) && (fragment instanceof BroadcastTab)) {
                viewNewContents.setY(((BroadcastTab) fragment).getVTabCoord());
            }
            viewNewContents.setVisibility(View.VISIBLE);
            Activity actMain = getActivity();
            if (actMain != null && actMain instanceof MainTab) {
                ((MainTab) actMain).setBrdFriendCircleUnread(true);
            }
        }
    }

    private void hideNewMid() {
        if (viewNewContents != null && viewNewContents.getVisibility() == View.VISIBLE) {
            viewNewContents.setVisibility(View.GONE);
            Activity actMain = getActivity();
            if (actMain != null && actMain instanceof MainTab) {
                ((MainTab) actMain).setBrdFriendCircleUnread(false);
            }
        }
    }

    private long timeLastRefresh;//最后一次刷新时间
    private boolean areaboolean = false;//记录是否第一次加载数据

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
//		mRefreshView.finishRefresh();
        PalmchatLogUtils.e(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
        dismissProgressDialog();
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_BCGET_COMMENTS_BY_STATE:    //广播列表返回 按State
                case Consts.REQ_BCGET_COMMENTS_BY_GPS:
                    settingStop();
                    if (null != result) {
                        int loadTypeWhenReq = LOADTYPE_WHEN_REQ_NEW;
                        if (user_data != null) {//是 重新刷新还是LoadMore
                            loadTypeWhenReq = (Integer) user_data;
                            if (loadTypeWhenReq == LOADTYPE_WHEN_REQ_NEW) {//如果是刷新列表 需要先clear的 就需要停掉scroll滚动 方式滚动的时候getView 然后数据清了 越界
                                stopListViewScroll();
                            }
                        }
                        AfResponseComm afResponseComm = (AfResponseComm) result;
                        AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                        if (afPeoplesChaptersList != null) {
                            ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                            if (list_AfChapterInfo.size() > 0) {
                                refreshSuccess_DelelDBData(loadTypeWhenReq);
                                setAdapter(list_AfChapterInfo, Consts.AFMOBI_BRMSG_INPUT, loadTypeWhenReq);
                                mListView.setPullLoadEnable(true);
                                areaboolean = true;
                            } else {//如果没有任何广播
                                if (START_INDEX == 0) {//发表广播，广播加入缓存删除广播后把缓存也删除掉，解决了删除广播后刷新广播列表，删除的广播还在列表中显示。
                                    setListScrolltoTop(false);
                                    refreshSuccess_DelelDBData(loadTypeWhenReq);
                                    adapter.clear();
                                    adapter.notifyDataSetChanged();
                                }
                                if(isCurrentAcitity()) {
                                    ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.no_data);
                                }
                                if (!areaboolean) {
                                    mBroadcastSelectionRegion.setVisibility(View.GONE);
                                } else {
                                    mBroadcastSelectionRegion.setVisibility(View.VISIBLE);
                                    showReginTip();
                                    areaboolean = false;
                                }

                                mListView.setPullLoadEnable(false);
                                showBlankView();
                            }
                        } else {
                            showBlankView();
                        }
                    } else {
                        showBlankView();
                    }
                    if (START_INDEX == 0) {//只要是刷新成功了 那就记录下当前刷新成功的毫秒数
                        timeLastRefresh = System.currentTimeMillis();
                        hideNewMid();//隐藏显示
					/*20160414确认了pageid是再重登后无效的 所以保存也就没有意义  所以去掉
					 * if(context!=null){
						SharePreferenceUtils.getInstance(context).setNewAroundLastTimeRefresh( CacheManager.getInstance().getMyProfile().afId, timeLastRefresh,pageid);
					}*/
                    } else {//去取一下有无新内容
                        checkNewMid();
                    }
                    break;
                case Consts.REQ_BCMSG_CHECK_MID://是否有新内容
                    if (null != result) {
                        AfResponseComm afResponseComm = (AfResponseComm) result;
                        AfPulishInfo _afpublishInfo = (AfPulishInfo) afResponseComm.obj;
                        if (_afpublishInfo != null && !TextUtils.isEmpty(_afpublishInfo.mid) && adapter != null) {
                            if (adapter.getCount() < 1 || //当前列表为空
                                    !_afpublishInfo.mid.equals(adapter.getItem(0).mid)) {//最新的一条不相等
                                showNewMid();
                            }
                        }

                    }
                    break;
                case Consts.REQ_BCGET_REGION_BROADCAST:
                    if (null != result) {
                        AfResponseComm.AfBCGetRegionBroadcastResp afResponseComm = (AfResponseComm.AfBCGetRegionBroadcastResp) result;
                        if (null != afResponseComm.default_list && afResponseComm.default_list.size() > 0) {
                            PalmchatApp.getApplication().setDefaultList(afResponseComm.default_list);
                            dismissProgressDialog();
                            if (PalmchatApp.getApplication().getDefaultList() != null) {
                                StartAreaAdapter();
                            }
                        } else {//请求区域没有数据的情况下
                            dismissProgressDialog();
                            if(isCurrentAcitity()) {
                                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.select_area_not_broadcast);
                            }
                        }
                    }
                    break;
            }
        } else {
            settingStop();
            if (code == Consts.REQ_CODE_104) {//PageID失效 重新获取
                PalmchatLogUtils.e(TAG, "----code:" + code);
                clearAndRefresh();
            } else {
                if (flag == Consts.REQ_BCGET_COMMENTS_BY_STATE// Consts.REQ_BCGET_COMMENTS_BY_CITY
                        || flag == Consts.REQ_BCGET_COMMENTS_BY_GPS) {
                    if (START_INDEX > 0) {
                        START_INDEX--;
                    }
                } else if (flag == Consts.REQ_BCGET_REGION_BROADCAST) {
                    AfResponseComm.AfBCGetRegionBroadcastResp afResponseComm = (AfResponseComm.AfBCGetRegionBroadcastResp) result;
                    if (afResponseComm != null) {
                        PalmchatApp.getApplication().setDefaultList(afResponseComm.default_list);
                    }
                }
                if (isAdded() && isCurrentAcitity()) {
                    Consts.getInstance().showToast(context, code, flag, http_code, getUserVisibleHint());

                }
            }
        }
    }

    private boolean isCurrentAcitity(){
        if(getUserVisibleHint() && CommonUtils.getCurrentActivity(context) != null
                && context != null && context.getLocalClassName().equals(CommonUtils.getCurrentActivity(context))) {
            return true;
        }
        return false;
    }

    private void StartAreaAdapter() {
        mDialog = new Dialog(BroadcastFragment.this.getActivity(), R.style.Theme_Hold_Dialog_Base);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View viewdialog = inflater.inflate(R.layout.activity_broadcast_area_listview, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mDialog.setContentView(viewdialog, layoutParams);
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = m.getDefaultDisplay();

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);//居中显示
        lp.width = (int) (mDisplay.getWidth() / 1.2); // 宽度
        lp.height = (int) (mDisplay.getHeight() / 1.5); // 高度
        dialogWindow.setAttributes(lp);

        mAreaListView = (ListView) viewdialog.findViewById(R.id.area_listview);
        mAreaListAdapter = new AreaListAdapter(BroadcastFragment.this.getActivity(), PalmchatApp.getApplication().getDefaultList());
        mAreaListView.setAdapter(mAreaListAdapter);
        mAreaListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//只能有一项选中
        mAreaListView.setOnItemClickListener(BroadcastFragment.this);
        mDialog.show();
    }

    /**
     * 清空列表并重新 获取最新的
     */
    private void clearAndRefresh() {
        setListScrolltoTop(false);
//		adapter.clear();
        showRefresh();
    }

    /**
     * 看这次请求返回的之前是否是loadMore的
     *
     * @param loadTypeWhenReq isLoadMore
     */
    private void refreshSuccess_DelelDBData(int loadTypeWhenReq) {
        if (loadTypeWhenReq == LOADTYPE_WHEN_REQ_NEW) {
//				if (!isLoadMore) {
            if (looperThread != null && looperThread.looperHandler != null) {
                looperThread.looperHandler.sendEmptyMessage(LooperThread.DELETE_DB_IS_SERVER_DATA);
            }
//				}
        }
    }


    @Override
    public void onDestroy() {


        mHandler.removeCallbacks(mRunnable);


        if (null != looperThread && null != looperThread.looper) {
            looperThread.looper.quit();
        }
        EventBus.getDefault().unregister(this);//解注册

        super.onDestroy();
    }

    /**
     * 地区改变的通知
     *
     * @param event
     */
    public void onEventMainThread(ChangeRegionEvent event) {
        afProfileInfo = CacheManager.getInstance().getMyProfile();
//		setState();
        showRefresh();
    }

    public void onEventMainThread(RefreshFollowerFolloweringOrGroupListEvent event) {
        adapter.notifyDataSetChanged();
    }
    public void onEventMainThread(EventFollowNotice event) {
        adapter.notifyDataSetChanged();
    }
    /**
     * 处理广播更新事件
     *
     * @param afChapterInfo
     */
    public void onEventMainThread(AfChapterInfo afChapterInfo) {
        if (afChapterInfo.eventBus_action == Constants.UPDATE_BROADCAST_MSG) {//发广播 更新状态
            PalmchatLogUtils.println("BroadcastFragment onReceive UPDATE_BROADCAST_MSG,afChapterInfo.status=" + afChapterInfo.status);
            if (afChapterInfo.status == AfMessageInfo.MESSAGE_SENT) {
                CacheManager.getInstance().setBC_RecordSendSuccessData(afChapterInfo);
                adapter.notifyDataSetChanged(afChapterInfo, afChapterInfo._id);
                setListScrolltoTop(false);

                // 只要广播发送成功了，再判断一下要不要同步标识
                if (Consts.BR_TYPE_IMAGE_TEXT == afChapterInfo.type || Consts.BR_TYPE_IMAGE == afChapterInfo.type) {
                    PalmchatLogUtils.i("--wx--", "begin to synchronizate fb  -postPhotoAndMsg");
                    mCurAfChapterInfo = afChapterInfo;
                    postPhotoOrMsg(afChapterInfo.content, afChapterInfo.list_mfile.get(0).local_img_path);
                } else if (Consts.BR_TYPE_TEXT == afChapterInfo.type || Consts.BR_TYPE_VOICE_TEXT == afChapterInfo.type) {
                    PalmchatLogUtils.i("--wx--", "begin to synchronizate fb  -postPhotoOrMsg");
                    mCurAfChapterInfo = afChapterInfo;
                    postPhotoOrMsg(afChapterInfo.content);
                }
            } else if (afChapterInfo.status == AfMessageInfo.MESSAGE_UNSENT) {
                adapter.notifyDataSetChanged_removeBy_id(afChapterInfo._id); // Remove release added to broadcast to the list that a broadcast
                send_bc_failed_notification.setVisibility(View.VISIBLE);
				/*if (mNewNotificationView.getVisibility() == View.GONE) {
					lin_spacing.setVisibility(View.VISIBLE);
				}else {
					lin_spacing.setVisibility(View.GONE);
				}*/
            } else if (afChapterInfo.status == AfMessageInfo.MESSAGE_SENTING) {
                if (lin_no_data != null && lin_no_data.getVisibility() == View.VISIBLE) {
                    hideBlankView();
                }
                adapter.notifyDataSetChanged(afChapterInfo);//Add the broadcast to the list
                setListScrolltoTop(false);
            }
        } else if (Constants.BROADCAST_NOTIFICATION_ACTION == afChapterInfo.eventBus_action) { //消息通知
            if (looperThread != null && looperThread.looperHandler != null) {
                looperThread.looperHandler.sendEmptyMessage(LooperThread.GET_BRD_NOTIFICATION_DATA);
            }

        } else if (Constants.UPDATE_DELECT_BROADCAST == afChapterInfo.eventBus_action) {
            adapter.notifyDataSetChanged_removeBymid(afChapterInfo.mid);
            CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(afChapterInfo.mid);
        } else if (Constants.UPDATE_LIKE == afChapterInfo.eventBus_action) {
            adapter.notifyDataSetChanged_updateLikeBymid(afChapterInfo);
        }
    }

    @Override
    public void onRefresh(View view) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (!isRefreshing_mListview) {

                if (!mIsInitRefresh) {
                    mHandler.sendEmptyMessage(DefaultValueConstant.RESETTILEPOSITION);
                }
                isLoadMore = false;
                isRefreshing_mListview = true;
                START_INDEX = 0;
                loadData();
                two_minutes_Cancel_Refresh_Animation();
            }
            mIsInitRefresh = false;
            mBroadcastSelectionRegion.setVisibility(View.GONE);
        } else {
            ToastManager.getInstance().show(context, getUserVisibleHint(), R.string.network_unavailable);
            stopRefresh();
            if (!mIsInitRefresh) {
                mHandler.sendEmptyMessage(DefaultValueConstant.RESETTILEPOSITION);
            }
            mIsInitRefresh = false;
        }

    }

    @Override
    public void onLoadMore(View view) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (!isLoadingMore_mListview) {
                isLoadMore = true;
                isLoadingMore_mListview = true;
                START_INDEX++;

                if (isSelectDB_Data //如果目前的数据是从数据库本地读取的 显示更多 就等于是 需要刷新（常见于进主页面后网络不好刷新失败还是用缓存的情况）
                        || (!TextUtils.isEmpty(pageid_session)//或pageid对应的session跟之前的已经不一致了 说明后台登陆过了 也要重新刷了
                        && !pageid_session.equals(PalmchatApp.getApplication().mAfCorePalmchat.AfHttpGetServerSession()))) {
                    clearAndRefresh();
                } else {
                    loadDataFromServer(pageid);
                }
            }
        } else {
            ToastManager.getInstance().show(context, getUserVisibleHint(), R.string.network_unavailable);
            stopLoadMore();
        }
    }

    public void showRefresh() {
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (getActivity() != null) {
                int px = AppUtils.dpToPx(getActivity(), 60);
                mIsInitRefresh = true;
                mListView.performRefresh(px);
                isLoadMore = false;
                isRefreshing_mListview = true;
                START_INDEX = 0;
                hideBlankView();
            }
        } else {
            ToastManager.getInstance().show(context, getUserVisibleHint(), R.string.network_unavailable);
            stopRefresh();
        }
    }

    private boolean isRefreshing_mListview = false;
    private boolean isLoadingMore_mListview = false;


    private void stopRefresh() {
        isRefreshing_mListview = false;
        mListView.stopRefresh(true);
        mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
    }

    /**
     * 停止刷新和loadmore
     */
    public void settingStop() {
        if (isLoadingMore_mListview) {
            stopLoadMore();
        }
        if (isRefreshing_mListview) {
            stopRefresh();
        }
    }

    private void stopLoadMore() {
        isLoadingMore_mListview = false;
        mListView.stopLoadMore();
    }


    /**
     * 打开评论输入界面
     */
    public void opne_inputbox() {
        if (isAdded()) {
            notification.setVisibility(View.GONE);
            input_box_area.setVisibility(View.GONE);
            chatting_emoji_layout.setVisibility(View.GONE);
            emotion_isClose = true;
            CharSequence hint_name = null;
            hint_name = adapter.getCommentModel().getHint_name();

            vEditTextContent.setHint(hint_name);
            SoftKeyboard_opne_close = true;
            Intent intent = new Intent(getActivity(), SoftkeyboardActivity.class);
            intent.putExtra(JsonConstant.KEY_HINT_NAME, hint_name);
            this.startActivityForResult(intent, Constants.COMMENT_FLAG);
        }
    }


    // 隐藏系统键盘
    public void hideSoftInputMethod(EditText ed) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            CommonUtils.closeSoftKeyBoard(ed);
        }
    }


    public void close_inputbox(final boolean emotion_isClose) {
        if (isAdded()) {
            if (emotion_isClose) {
                hideSoftInputMethod(vEditTextContent);
                SoftKeyboard_opne_close = true;
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (chatting_emoji_layout.getVisibility() == View.VISIBLE) {
                        chatting_emoji_layout.setVisibility(View.GONE);
                    }
                    input_box_area.setVisibility(View.GONE);
                    notification.setVisibility(View.VISIBLE);
                }
            }, 200);
        }
    }

    private void setEdittextListener(CharSequence s) {
        // TODO Auto-generated method stub
        int length = s.length();
        if (editListener.edittext_length > length) {
            isDeleteSymbol();
        } else if (editListener.edittext_length < length) {

        }
    }

    private boolean isDeleteSymbol() {
        if (editListener.edittext_string != null && editListener.edittext_string.length() >= 2) {
            String str = editListener.edittext_string.toString();
            String tempStr = str.substring(str.length() - 1);
            if ("]".equals(tempStr)) {
                String ttt = str.substring(str.lastIndexOf("["), str.lastIndexOf("]") + 1);
                if (!TextUtils.isEmpty(ttt) && ttt.length() > 2) {
                    boolean isDelete = EmojiParser.getInstance(PalmchatApp.getApplication()).isDefaultEmotion(vEditTextContent, ttt);
                    return isDelete;
                }
            }
        }

        return false;
    }


    public void setFace(int id, String data) {
        Drawable drawable;
        if (this.isAdded()) {
            if (id != 0 && !CommonUtils.isDeleteIcon(id, vEditTextContent)) {
                drawable = getResources().getDrawable(id);
                drawable.setBounds(0, 0, CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
                ImageSpan span = new ImageSpan(drawable);
                SpannableString spannableString = new SpannableString(data);
                spannableString.setSpan(span, 0, data.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.e("ttt->", spannableString.length() + "->" + vEditTextContent.length());
                int length = spannableString.length() + vEditTextContent.length();
                if (length <= CommonUtils.TEXT_MAX) {
                    vEditTextContent.append(spannableString);
                } else {
                    if (!ToastManager.getInstance().isShowing()) {
                        ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.comment_long);
                    }
                }
            }
        }
    }


    /**
     * resend db data
     */
    public void get_resend_DB() {
        String afid = CacheManager.getInstance().getMyProfile().afId;
        AfResponseComm afResponseComm = mAfCorePalmchat.AfDBBCChapterFindByAFIDStatus(afid, AfMessageInfo.MESSAGE_UNSENT);
        if (afResponseComm != null) {
            AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
            if (afPeoplesChaptersList != null) {
                ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                for (AfChapterInfo afChapterInfo : list_AfChapterInfo) {
                    PalmchatLogUtils.println("getResend_DB,afChapterInfo._id =" + afChapterInfo._id);
                    BroadcastUtil.saveCache(afChapterInfo);
                    adapter.notifyDataSetChanged_removeBy_id(afChapterInfo._id);
                }
            }
        }
    }

    public void emojj_del() {
        // TODO Auto-generated method stub
        if (vEditTextContent != null) {
            CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
        }
    }

    public void two_minutes_Cancel_Refresh_Animation() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (isRefreshing_mListview) {
                    settingStop();
                    ToastManager.getInstance().show(context, getUserVisibleHint(), R.string.network_unavailable);
                }
                PalmchatLogUtils.e(TAG, "---two_minutes_Cancel_Refresh_Animation");

            }
        }, Constants.TWO_MINUTER);
    }

    /**
     * 刷新成功的提示
     */
    public void showRefreshSuccess() {
        if (!isSelectDB_Data && !isLoadMore) {
            mHandler.sendEmptyMessage(DefaultValueConstant.SHOWFRESHSUCCESS);
        }
    }


    @Override
    public void interface_open_inputbox() {
        opne_inputbox();
    }


    @Override
    public void interface_dismissProgressDialog() {
        dismissProgressDialog();
    }


    @Override
    public boolean interface_isAdded() {
        return isAdded();
    }


    @Override
    public Activity interface_getActivity() {
        return getActivity();
    }


    @Override
    public void interface_showProgressDialog() {
        showProgressDialog();
    }


    @Override
    public XListView interface_getmListView() {
        return mListView;
    }

    /**
     * 发送文字消息
     *
     * @param msg
     */
    private void postPhotoOrMsg(String msg) {

        if (PalmchatApp.getApplication().isFacebookShareClose()) {
            PalmchatLogUtils.i("--wx--", "----postPhotoAndMsg----isFacebookShareClose is close");
            return;
        }
        mPendingAction = PendingAction.POST_MSG;
        if (!CommonUtils.hasPublishPermission()) {
            // We need to get new permissions, then complete the action when we get called back.
            LoginManager.getInstance().logInWithPublishPermissions(
                    this, Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
        } else {
            mPendingAction = PendingAction.NONE;
            ShareOpenGraphObject graphObject = new ShareOpenGraphObject.Builder()
                    .putString("og:type", FacebookConstant.OG_OBJECT_TYPE)
                    .putString("og:title", FacebookConstant.SHARETITLE)
                    .build();

            ShareOpenGraphAction playAction = new ShareOpenGraphAction.Builder()
                    .putString("og:type", FacebookConstant.OG_ACTION_TYPE)
                    .putBoolean("fb:explicitly_shared", true)
                    .putObject(FacebookConstant.OG_OBJECTCONTENT, graphObject)
                    .build();
            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                    .setAction(playAction)
                    .setPreviewPropertyName(FacebookConstant.OG_OBJECTCONTENT)
                    .build();
            ShareApi.share(content, shareCallback, msg);
        }
    }


    /**
     * 分享图或者图文
     *
     * @param msg
     * @param path
     */
    private void postPhotoOrMsg(String msg, String path) {
        if (PalmchatApp.getApplication().isFacebookShareClose()) {
            PalmchatLogUtils.i("--wx--", "----postPhotoAndMsg----isFacebookShareClose is close");
            return;
        }
        mPendingAction = PendingAction.POST_PHOTO;
        if (!CommonUtils.hasPublishPermission()) {
            // We need to get new permissions, then complete the action when we get called back.
            LoginManager.getInstance().logInWithPublishPermissions(
                    this, Arrays.asList(FacebookConstant.PUBLISH_ACTIONS));
        } else {
            mPendingAction = PendingAction.NONE;
            PalmchatLogUtils.i("--wx--", "----postPhotoAndMsg----isFacebookShareClose is open");
            File fPic = null;
            if (!CommonUtils.isEmpty(path)) {
                fPic = new File(path);
            }

            if (null == fPic) {
                ToastManager.getInstance().show(getActivity(), getUserVisibleHint(), R.string.picisnotexist);
            } else {
                if (TextUtils.isEmpty(msg)) {
                    postPhoto(fPic);
                } else {
                    postMsgAndPhoto(fPic, msg);
                }
            }
        }
    }

    /**
     * 发送图文消息
     *
     * @param fPic
     * @param msg
     */
    private void postMsgAndPhoto(File fPic, String msg) {

        Pair<File, Integer> fileAndMinDimemsion = null;

        Log.i("--wx--", "------null != photo-----");

        SharePhoto.Builder sharePhotoBuilder = new SharePhoto.Builder();
        Bitmap bitmap = BitmapFactory.decodeFile(fPic.getAbsolutePath());
        sharePhotoBuilder.setBitmap(bitmap);
        fileAndMinDimemsion = ImageUtil.getImageFileAndMinDimension(fPic);

        sharePhotoBuilder.setUserGenerated(fileAndMinDimemsion.second >= FacebookConstant.USER_GENERATED_MIN_SIZE);
        SharePhoto gesturePhoto = sharePhotoBuilder.build();

        ShareOpenGraphObject graphObject = createGraphObject(gesturePhoto);
        ShareOpenGraphAction playAction = createPlayActionWithGame(graphObject);
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setAction(playAction)
                .setPreviewPropertyName(FacebookConstant.OG_OBJECTCONTENT)
                .build();
        ShareApi.share(content, shareCallback, msg);
    }

    /**
     * 发送纯图片
     *
     * @param photo
     */
    private void postPhoto(File photo) {
        Bitmap image = BitmapFactory.decodeFile(photo.getAbsolutePath());
        SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
        ArrayList<SharePhoto> photos = new ArrayList<SharePhoto>();
        photos.add(sharePhoto);

        SharePhotoContent sharePhotoContent =
                new SharePhotoContent.Builder().setPhotos(photos).build();
        ShareApi.share(sharePhotoContent, shareCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ListView lv = (ListView) parent;
        String country = PalmchatApp.getApplication().getDefaultList().get(position).country;
        String state = PalmchatApp.getApplication().getDefaultList().get(position).state;

        if (checkedIndex != position) {
            {  //定位到现在处于点击状态的radio
                int childId = checkedIndex - lv.getFirstVisiblePosition();
                if (childId >= 0) {  //如果checked =true的radio在显示的窗口内，改变其状态为false
                    View item = lv.getChildAt(childId);
                    if (item != null) {
                        RadioButton rb = (RadioButton) item.findViewById(checkedIndex);
                        if (rb != null)
                            rb.setChecked(false);
                    }
                }
                //将当前点击的radio的checked变为true
                RadioButton rb1 = (RadioButton) view.findViewById(position);
                if (rb1 != null)
                    rb1.setChecked(true);
                checkedIndex = position;
            }
        }
        mDialog.dismiss();
        if (NetworkUtils.isNetworkAvailable(context)) {
            Intent intent = new Intent();
            intent.putExtra("country", country);
            intent.putExtra("state", state);
            intent.putExtra("checkedIndex", position + "");
            intent.setClass(context, BroadcastAreaActivity.class);
            startActivity(intent);
        } else {
            ToastManager.getInstance().show(context, R.string.network_unavailable);
        }
    }

    /**
     * 创建graph对象
     *
     * @param gesturePhoto
     * @return
     */
    private ShareOpenGraphObject createGraphObject(SharePhoto gesturePhoto) {
        return new ShareOpenGraphObject.Builder()
                .putString("og:type", FacebookConstant.OG_OBJECT_TYPE)
                .putString("og:title", FacebookConstant.SHARETITLE)
                .putPhoto("og:image", gesturePhoto)
                .build();
    }

    /**
     * 创建action对象
     *
     * @param game
     * @return
     */
    private ShareOpenGraphAction createPlayActionWithGame(ShareOpenGraphObject game) {
        return new ShareOpenGraphAction.Builder()
                .putString("og:type", FacebookConstant.OG_ACTION_TYPE)
                .putBoolean("fb:explicitly_shared", true)
                .putObject(FacebookConstant.OG_OBJECTCONTENT, game)
                .build();
    }

    private void handlePendingAction() {
        switch (mPendingAction) {
            case NONE: {
                break;
            }
            case POST_PHOTO: {
                postPhotoOrMsg(mCurAfChapterInfo.content, mCurAfChapterInfo.list_mfile.get(0).local_img_path);
                break;
            }
            case POST_MSG: {
                postPhotoOrMsg(mCurAfChapterInfo.content);
                break;
            }
            default:
                break;
        }
    }

    private void hideBlankView() {
        if (lin_no_data != null) {
            lin_no_data.setVisibility(View.GONE);
            mListView.setDividerHeight(mDivideHeight);
            mListView.setPullLoadEnable(true);
        }
    }

    private void showBlankView() {
        if (adapter.getCount() < 1) {
            if (lin_no_data != null) {
                lin_no_data.setVisibility(View.VISIBLE);
                mListView.setDividerHeight(0);
                mListView.setPullLoadEnable(false);
                mBroadcastSelectionRegion.setVisibility(View.GONE);
                mListView.setPullLoadEnable(false);
                showReginTip();
            }
        } else {

        }

    }

    /**
     * 显示区域提示
     */
    private void showReginTip() {
        if (!SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).getNewAroudEndFlagByAccount()) {
            Activity activity = getActivity();
            if(getUserVisibleHint()){
                if ((activity != null) && (activity instanceof MainTab)) {
                    ((MainTab) getActivity()).resetTitlePosition();
                    ((MainTab) getActivity()).showRegionSelectTip(true);
                }
            }

            SharePreferenceUtils.getInstance(PalmchatApp.getApplication().getApplicationContext()).setNewAroundEndFlagByAccount();
        }
    }

    @SuppressLint("NewApi")
    public void setListViewOffSet(float offSet) {
        if (null != mListView) {
            if (mListView.getScrollY() < offSet) {
                mListView.setBlankViewHeight(offSet);
            }
        }
    }

    @SuppressLint("NewApi")
    public void resetListViewPosition() {
        if (null != mListView) {
            mListView.setBlankViewHeight(0);
        }
    }

    /**
     * 列表停止滑动
     */
    public void stopListViewScroll() {
        if (null != mListView) {
            mListView.stopListViewScroll();
        }
    }

    /**
     * 初始化差值
     *
     * @param tabsYCoord
     */
    public void initDiffValue(float tabsYCoord) {
        if(viewNewContents!=null) {
            mDiffValue_newContents = viewNewContents.getY() - tabsYCoord;
            mOriCoord_newContents = viewNewContents.getY();
        }
    }

    /**
     * 手动滑动时的显示
     *
     * @param tabCoord
     * @param yValue
     */
    public void showContents(float tabCoord, float yValue) {
        if(viewNewContents==null){
            return;
        }
        float goalCoord = 2 * yValue + viewNewContents.getY();
        if (goalCoord > mOriCoord_newContents) {
            viewNewContents.setY(mOriCoord_newContents);
        } else if ((viewNewContents.getY() - tabCoord) >= mDiffValue_newContents) {
            viewNewContents.setY(yValue + viewNewContents.getY());
        } else {
            viewNewContents.setY(goalCoord);
        }
        if ((viewNewContents.getY() > SPACE) && (mState_newContent != State.SHOW)) {
            mState_newContent = State.SHOW;
        }
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * 隐藏newContent随手滑动
     *
     * @param yValue
     */
    public void hideContents(float yValue) {
        if(viewNewContents==null){
            return;
        }
        yValue = viewNewContents.getY() + 2 * yValue;
        if (yValue > 0) {
            viewNewContents.setY(yValue);
        } else {
            viewNewContents.setY(0);
        }
        if (mOriCoord_newContents - viewNewContents.getY() >= SPACE) {
            mState_newContent = State.HIDE;
        }
        mHandler.removeCallbacks(mRunnable);
    }

    /**
     * 外部调用显示动画
     */
    public void showAnimation() {
        if (mState_newContent == State.SHOW) {
            if (viewNewContents.getY() > DefaultValueConstant.DISTANCE_5) {
                showShowAnimation();
            } else {
                mHandler.postDelayed(mRunnable, DefaultValueConstant.DURATION_STATIC_2S);
            }
        } else if (mState_newContent == State.HIDE) {
            Fragment fragment = getParentFragment();
            if ((fragment != null) && (fragment instanceof BroadcastTab)) {
                float tabCoord = ((BroadcastTab) fragment).getVTabCoord();
                if ((viewNewContents.getY() - tabCoord) < (mDiffValue_newContents - DefaultValueConstant.DISTANCE_30)) {
                    hideShowAnimation();
                } else {
                    showShowAnimation();
                }
            }

        }
    }

    /**
     * 弹出动画
     */
    private void showShowAnimation() {
        float tabCoord = 0;
        Fragment fragment = getParentFragment();
        if ((null != fragment) && (fragment instanceof BroadcastTab)) {
            tabCoord = ((BroadcastTab) fragment).getVTabCoord();
        }
        AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_300, viewNewContents, viewNewContents.getY(), tabCoord + mDiffValue_newContents).start();
    }

    /**
     * 隐藏动画
     */
    private void hideShowAnimation() {

        ObjectAnimator objectAnimator = AnimationController.createTranslationYAnimation(DefaultValueConstant.DURATION_ANIMATION_300, viewNewContents, viewNewContents.getY(), 0);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                PalmchatLogUtils.i(TAG, "---objectAnimator---onAnimationEnd----");
                mHandler.postDelayed(mRunnable, DefaultValueConstant.DURATION_STATIC_2S);
            }
        });
        objectAnimator.start();

    }


}
