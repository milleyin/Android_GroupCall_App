package com.afmobi.palmchat.ui.activity.social;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
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
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import de.greenrobot.event.EventBus;

/**
 * 朋友圈页面。
 */
public class FriendCircleFragment extends BaseFragment implements
        OnClickListener, IXListViewListener, AfHttpResultListener, AdapterBroadcastMessages.IFragmentBroadcastListener {

    public static final String TAG = FriendCircleFragment.class.getCanonicalName();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    public XListView mListView;

    private AdapterBroadcastMessages adapter;
    private LooperThread looperThread;

    boolean isSelectDB_Data = false;

    private View send_button, vImageEmotion;
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
     * 初始化刷新
     */
    private boolean mIsInitRefresh;
    /**
     * 空视图
     */
    private View mV_EmptyView;
    /**
     * 列表行距
     */
    private int mDivideHeight;

    private Rect rect;

    VideoView fda;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LooperThread.INSERT_SERVER_DATA_TO_DB:
                    List<AfChapterInfo> list_aAfChapterInfos = (List<AfChapterInfo>) msg.obj;
                    int loadTypeWhenReq = msg.arg1;
                    adapter.notifyDataSetChanged(list_aAfChapterInfos, loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE);
                    showHideEmptyView();
                    if (loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) {//!isLoadMore) {
                        onStop();
                    }
                    if (!isSelectDB_Data) {
                        settingStop();
                    }
                    isSelectDB_Data = false;//服务器返回数据了  当前数据为缓存状态的标志设false

                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("zhh_2016_07_22", "FriendCircleFragment" + "_onCreate ");
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
//		MobclickAgent.onPageStart(TAG);

        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);

        close_inputbox(emotion_isClose);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    int _start_index;
    int _end_index;

    public void findViews() {
        setContentView(R.layout.bc_common);

        if (editListener == null) {
            editListener = new EditListener();
        }
        findViewById(R.id.top_title).setVisibility(View.GONE);
        View headerView = LayoutInflater.from(context).inflate(R.layout.no_content_layout, null);
        mV_EmptyView = headerView.findViewById(R.id.friendcircle_lin_no_data);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, CommonUtils.getRealtimeWindowHeight(getActivity()) / 5 + 38, 0, 0);
        mV_EmptyView.setLayoutParams(layoutParams);
        chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
        send_button = findViewById(R.id.chatting_operate_one);
        input_box_area = findViewById(R.id.input_box_area);
        chatting_options_layout = findViewById(R.id.chatting_options_layout);
        vImageEmotion = findViewById(R.id.image_emotion);
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
                    ToastManager.getInstance().show(getActivity(), R.string.comment_long);
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
        bc_main = (KeyboardListenRelativeLayoutEditText) findViewById(R.id.bc_main);
        bc_main.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

            @Override
            public void onKeyboardStateChanged(int state, int mHeight) {
                // TODO Auto-generated method stub
                switch (state) {
                    case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_HIDE://keyboard hide
                        break;
                    case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_SHOW://keyboard show
                        softkeyboard_H = mHeight;
                    default:
                        break;
                }
            }
        });

        mListView = (XListView) findViewById(R.id.listview);

        mListView.addHeaderView(headerView, null, true);
        mListView.setHeaderDividersEnabled(false);
        mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        //mListView.setDividerHeight(0);
        mListView.setHidingScrollListener(new HidingScrollListener() {

            @Override
            public void onMoved(float distance) {
                // TODO Auto-generated method stub
                Activity activity = getActivity();
                if (null != activity) {
                    ((MainTab) activity).moveTitle(distance, true);
                }
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
                if(activity!=null&&(activity instanceof  MainTab)){
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


            }
        });
    }

    /**
     * 从数据库获得缓存数据
     */
    private void getListFromDB() {
        if (mAfCorePalmchat == null) {
            mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        }
        AfResponseComm afResponseComm = mAfCorePalmchat.AfDBBCChapterGetListEx(START_INDEX * LIMIT, LIMIT, Consts.DATA_BROADCAST_FOLLOWING);
        if (afResponseComm != null && afResponseComm.obj != null) {
            if (afResponseComm != null) {
                AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                if (afPeoplesChaptersList != null) {
                    ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                    if (list_AfChapterInfo.size() > 0) {
                        isSelectDB_Data = true;
                        setAdapter(list_AfChapterInfo, Consts.AFMOBI_BRMSG_INPUT, BroadcastFragment.LOADTYPE_WHEN_REQ_DB);
                        mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                if (getActivity() != null && getActivity() instanceof MainTab) {
                                    showRefresh(); //初始化后 都要去取一次新数据
                                }
                            }
                        }, 600);
                    } else {
                        showRefresh();
                    }
                }
            }
        } else {
            showRefresh();
        }
    }

    /**
     * 双击底部 变顶部
     */
    public void setListScrolltoTop(boolean isFromTab) {
        if (mListView != null && mListView.getCount() > 0) {
            if (!isFromTab) {
                ((MainTab) getActivity()).resetTitlePosition();
            }
            mListView.setListScrolltoTop();
        }
    }

    int clike_count = 0;
    private boolean isLoadedFromDB;//每次程序运行期间只取一次数据库 并获取一次列表

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("zhh_2016_07_22", "FriendCircleFragment" + "_setUserVisibleHint");
        if (isVisibleToUser && (!isLoadedFromDB || adapter == null) && mListView != null) {
            getListFromDB();
            isLoadedFromDB = true;
        }
        MainTab tab = (MainTab) getActivity();
        if (null != tab) {
            tab.hideStateBtn();
        }


    }

    ;


    public void init() {
        if (mAfCorePalmchat == null) {
            mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        }
        mDivideHeight = mListView.getDividerHeight();
        CacheManager.getInstance().getMyProfile();

        TextView mTitleTxt = (TextView) findViewById(R.id.title_text);
        mTitleTxt.setText(R.string.broadcast_messages);

        adapter = new AdapterBroadcastMessages(getActivity(), new ArrayList<AfResponseComm.AfChapterInfo>(), Consts.AFMOBI_BRMSG_INPUT, this, BroadcastDetailActivity.FROM_FRIENDCIRCLE);
        mListView.setAdapter(adapter);


        EventBus.getDefault().register(this);//注册事件总线进行消息接收

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
                    adapter = new AdapterBroadcastMessages(getActivity(), broadcastMessageList, Consts.AFMOBI_BRMSG_INPUT, this, BroadcastDetailActivity.FROM_FRIENDCIRCLE);
                    mListView.setAdapter(adapter);
                } else {
                    final int size = broadcastMessageList.size();
                    if (size > 0) {
                        showRefreshSuccess();
                        Handler handler = looperThread.looperHandler;
                        if (null != handler) {
                            Message msgMessage = new Message();
                            msgMessage.obj = broadcastMessageList;
                            msgMessage.arg1 = loadTypeWhenReq;
                            msgMessage.what = LooperThread.INSERT_SERVER_DATA_TO_DB;

                            handler.sendMessage(msgMessage);
                        }
                    } else {
                        if (isAdded()) {
                            ToastManager.getInstance().show(getActivity(), mFragmentVisible, R.string.no_data);
                            mListView.setPullLoadEnable(false);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private int pageid = 0;
    private int START_INDEX = 0;
    private boolean isLoadMore;
    private static final int LIMIT = 10;

    private void loadData() {
        isLoadMore = false;
        START_INDEX = 0;
        pageid = (int) System.currentTimeMillis() + new Random(10000).nextInt();
        //这里之所以要用loadType 而不用isLoadMore 是因为isLoadMore是在上拉的时候就会触发，这样就会有如果先刷新 再更多  刷新返回时 isLoadMore这个状态是错的，导致会有重复广播
        mAfCorePalmchat.AfHttpGetFrdConnListPage(pageid, START_INDEX * LIMIT, LIMIT, isLoadMore ? BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE : BroadcastFragment.LOADTYPE_WHEN_REQ_NEW, this);

    }

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
                        if (!hint_text.equals(getString(R.string.hint_commet)) && !TextUtils.isEmpty(to_afid)) {
                            msg = hint_text + msg;
                        }
                        adapter.getCommentModel().setMsg(msg);
                        adapter.sendComment();
                    } else {
                        ToastManager.getInstance().show(getActivity(), R.string.hint_commet);
                    }
                }
            }
        }, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
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
    }

    class LooperThread extends Thread {

        private static final int INSERT_SERVER_DATA_TO_DB = 7004;
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
                                    afChapterInfo.isLike = mAfCorePalmchat.AfBcLikeFlagCheck(mid); // check like
                                    tempAfChapterInfos.add(0, afChapterInfo);
//								CacheManager.getInstance().remove_BC_RecordSendSuccessDataBy_mid(mid); // clear send success data
                                }
                            }
                            Message msg1 = new Message();
                            msg1.obj = tempAfChapterInfos;
                            msg1.arg1 = loadTypeWhenReq;
                            msg1.what = INSERT_SERVER_DATA_TO_DB;
                            mHandler.sendMessage(msg1);
                            if (!isSelectDB_Data && loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) { //写入数据库
                                mAfCorePalmchat.AfDBBCChapterDeleteByType(Consts.DATA_BROADCAST_FOLLOWING);
                                for (int i = size - 1; i >= 0; i--) {
                                    BroadcastUtil.ServerData_AfDBBCChapterInsert(Consts.DATA_BROADCAST_FOLLOWING, broadcastMessageList.get(i)); // insert DATA to DB
                                }
                            }
                            break;

                    }
                }
            };
            Looper.loop();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                                ToastManager.getInstance().show(getActivity(), R.string.hint_commet);
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
        }

    }

    public void setImage_emotionClink() {
        if (chatting_emoji_layout.getVisibility() == View.VISIBLE) {
            chatting_emoji_layout.setVisibility(View.GONE);
            CommonUtils.showSoftKeyBoard(vEditTextContent);
        } else {
            CommonUtils.closeSoftKeyBoard(vEditTextContent);
            chatting_emoji_layout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_CONNECTIONS_GET_LIST_BY_PAGE:
                    settingStop();
                    boolean isEmpty = false;
                    int loadTypeWhenReq = BroadcastFragment.LOADTYPE_WHEN_REQ_NEW;
                    if (null != result) {
                        if (user_data != null) {
                            loadTypeWhenReq = (Integer) user_data;
                            if (loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) {//如果是刷新列表 需要先clear的 就需要停掉scroll滚动 方式滚动的时候getView 然后数据清了 越界
                                stopListViewScroll();
                            }
                        }

                        AfResponseComm afResponseComm = (AfResponseComm) result;
                        AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                        if (afPeoplesChaptersList != null) {
                            ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                            if (list_AfChapterInfo.size() > 0) {
                                setAdapter(list_AfChapterInfo, Consts.AFMOBI_BRMSG_INPUT, loadTypeWhenReq);
                            } else {
                                isEmpty = true;
                            }
                        } else {
                            isEmpty = true;
                        }
                    } else {
                        isEmpty = true;
                    }
                    if (isEmpty) {//刷新的时候 没有数据  要清空
                        if (loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) {
                            if (isAdded() && adapter != null) {
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                showHideEmptyView();
                            }
                            mAfCorePalmchat.AfDBBCChapterDeleteByType(Consts.DATA_BROADCAST_FOLLOWING);
                        }
                        if (isAdded() && adapter != null) {
                            ToastManager.getInstance().show(getActivity(), mFragmentVisible, R.string.no_data);
                        }
                        mListView.setPullLoadEnable(false);
                    } else {
                        mListView.setPullLoadEnable(true);
                    }
                    break;

            }
        } else {
            settingStop();
            if (code == Consts.REQ_CODE_104) {
                PalmchatLogUtils.e(TAG, "----code:" + code);
                adapter.clear();
                showRefresh();
            } else {
                if (flag == Consts.REQ_BCGET_COMMENTS_BY_STATE// Consts.REQ_BCGET_COMMENTS_BY_CITY
                        || flag == Consts.REQ_BCGET_COMMENTS_BY_GPS) {
                    if (START_INDEX > 0) {
                        START_INDEX--;
                    }
                }

                if (isAdded()) {
                    Consts.getInstance().showToast(context, code, flag, http_code, mFragmentVisible);
                }
            }
            showHideEmptyView();
        }
    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();


        if (null != looperThread && null != looperThread.looper) {
            looperThread.looper.quit();
        }
        EventBus.getDefault().unregister(this);//注册事件总线进行消息接收
    }

    public void onEventMainThread(AfChapterInfo afChapterInfo) {
        if (afChapterInfo.eventBus_action == Constants.UPDATE_BROADCAST_MSG) {//发广播 更新状态
            if (afChapterInfo.status == AfMessageInfo.MESSAGE_SENT) {
                adapter.notifyDataSetChanged(afChapterInfo); //sent by myself , insert to friend circle list .
                showHideEmptyView();
            }
        } else if (Constants.UPDATE_DELECT_BROADCAST == afChapterInfo.eventBus_action) {
            adapter.notifyDataSetChanged_removeBymid(afChapterInfo.mid);
            if (adapter.getCount() == 0) {//如果删到空了 就显示
                mAfCorePalmchat.AfDBBCChapterDeleteByType(Consts.DATA_BROADCAST_FOLLOWING);
                showHideEmptyView();
            }
        } else if (Constants.UPDATE_LIKE == afChapterInfo.eventBus_action) {

            adapter.notifyDataSetChanged_updateLikeBymid(afChapterInfo);
        }
    }

    @Override
    public void onRefresh(View view) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            onPause();
            if (!isRefreshing_mListview) {
                if (!mIsInitRefresh) {
                    ((MainTab) getActivity()).resetTitlePosition();
                }
                isLoadMore = false;
                isRefreshing_mListview = true;
                START_INDEX = 0;
                PalmchatLogUtils.e(TAG, "----onRefresh startEngine----11111");
                loadData();
                two_minutes_Cancel_Refresh_Animation();
            }
            mIsInitRefresh = false;
        } else {
            ToastManager.getInstance().show(context, mFragmentVisible, R.string.network_unavailable);
            stopRefresh();
            showHideEmptyView();
            if (!mIsInitRefresh) {
                ((MainTab) getActivity()).resetTitlePosition();
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
                if (isSelectDB_Data) {
                    loadData();
                } else {
                    //这里之所以要用loadType 而不用isLoadMore 是因为isLoadMore是在上拉的时候就会触发，这样就会有如果先刷新 再更多  刷新返回时 isLoadMore这个状态是错的，导致会有重复广播
                    mAfCorePalmchat.AfHttpGetFrdConnListPage(pageid, START_INDEX * LIMIT, LIMIT, isLoadMore ? BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE : BroadcastFragment.LOADTYPE_WHEN_REQ_NEW, this);

                }
            }
        } else {
            ToastManager.getInstance().show(context, mFragmentVisible, R.string.network_unavailable);
            stopLoadMore();
        }
    }

    public void showRefresh() {
        if (NetworkUtils.isNetworkAvailable(PalmchatApp.getApplication())) {
            if (getActivity() != null) {
                int px = AppUtils.dpToPx(getActivity(), 60);
                mIsInitRefresh = true;
                mListView.performRefresh(px);
                isLoadMore = false;
                isRefreshing_mListview = true;
                START_INDEX = 0;
            }
        } else {
            ToastManager.getInstance().show(context, mFragmentVisible,
                    getResources().getString(R.string.network_unavailable));
            stopRefresh();
        }
    }

    private boolean isRefreshing_mListview = false;
    private boolean isLoadingMore_mListview = false;


    private void stopRefresh() {
        // TODO Auto-generated method stub
        isRefreshing_mListview = false;
        mListView.stopRefresh(true);
        mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
    }

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

    public void opne_inputbox() {
        if (isAdded()) {

//			 mHandler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						 if(mNoteLayout.getVisibility()==View.VISIBLE){
//							 mNoteLayout.setVisibility(View.GONE);
//						 }
//						 notification.setVisibility(View.GONE);
            input_box_area.setVisibility(View.GONE);
            chatting_emoji_layout.setVisibility(View.GONE);
            emotion_isClose = true;
            CharSequence hint_name = null;
            hint_name = adapter.getCommentModel().getHint_name();

            vEditTextContent.setHint(hint_name);
            SoftKeyboard_opne_close = true;
            if (getActivity() != null && getActivity() instanceof MainTab) {
                MainTab _maintab = (MainTab) getActivity();

                Intent intent = new Intent(_maintab.mContext, SoftkeyboardActivity.class);
                intent.putExtra(JsonConstant.KEY_HINT_NAME, hint_name);
                this.startActivityForResult(intent, Constants.COMMENT_FLAG);
            }
//					}
//			 },100);
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
//			WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//			params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
                SoftKeyboard_opne_close = true;
//			 mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					}
//			 	}, 200);
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (chatting_emoji_layout.getVisibility() == View.VISIBLE) {
                        chatting_emoji_layout.setVisibility(View.GONE);
                    }
                    input_box_area.setVisibility(View.GONE);
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
                        ToastManager.getInstance().show(getActivity(), R.string.comment_long);
                    }
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
                    ToastManager.getInstance().show(context, mFragmentVisible, R.string.network_unavailable);
                }
                PalmchatLogUtils.e(TAG, "---two_minutes_Cancel_Refresh_Animation");

            }
        }, Constants.TWO_MINUTER);
    }

    public void showRefreshSuccess() {
        if (!isSelectDB_Data && !isLoadMore) {
            Fragment fragment = getParentFragment();
            ((BroadcastTab) fragment).showRefreshSuccess(2);
        }
    }

    @Override
    public void interface_open_inputbox() {
        // TODO Auto-generated method stub
        opne_inputbox();
    }


    @Override
    public void interface_dismissProgressDialog() {
        // TODO Auto-generated method stub
        dismissProgressDialog();
    }


    @Override
    public boolean interface_isAdded() {
        // TODO Auto-generated method stub
        return isAdded();
    }


    @Override
    public Activity interface_getActivity() {
        // TODO Auto-generated method stub
        return getActivity();
    }


    @Override
    public void interface_showProgressDialog() {
        // TODO Auto-generated method stub
        showProgressDialog();
    }


    @Override
    public XListView interface_getmListView() {
        // TODO Auto-generated method stub
        return mListView;
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
     * 判断是否显示空视图
     */
    private void showHideEmptyView() {
        if (null != adapter) {
            if (adapter.getCount() > 0) {
                mListView.setDividerHeight(mDivideHeight);
                mV_EmptyView.setVisibility(View.GONE);
            } else {
                mListView.setDividerHeight(0);
                mV_EmptyView.setVisibility(View.VISIBLE);
                mListView.setPullLoadEnable(false);
            }
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


}
