package com.afmobi.palmchat.ui.activity.social;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import android.widget.AdapterView.OnItemClickListener;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.IntentConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.SoftkeyboardActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.CutstomEditTextBanLineKey;
import com.afmobi.palmchat.ui.customview.EditListener;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText.IOnKeyboardStateChangedListener;
import com.afmobi.palmchat.ui.customview.list.CircleAdapter;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.NetworkUtils;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.core.AfPalmchat;
import com.core.AfResponseComm;
import com.core.Consts;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeoplesChaptersList;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;

/**
 * 选择其他城市的广播
 */
public class BroadcastAreaActivity extends BaseActivity implements
        OnClickListener, IXListViewListener, AfHttpResultListener, AdapterBroadcastMessages.IFragmentBroadcastListener, OnItemClickListener {


    //	public static final String TAG = FriendCircleFragment.class.getCanonicalName();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");//用于显示刷新时间

    public XListView mListView;
    private AdapterBroadcastMessages adapter;
    public View top_refresh;//显示刷新成功 显示update_successful


    private LinearLayout lin_no_data;
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
    private final int SETLIST = 1;

    /**
     * 保存国家
     */
    private String country;
    /**
     * 保存城市
     */
    private String state;
    /**
     * 选择区域的按钮
     */
    private ImageView mBroadcastAreaSelect;
    /**
     * 点击here后弹出Dialog选择区域
     */
    private Dialog mDialog;

    private BroadcastAreaListAdapter mAreaListAdapter;

    private ListView mAreaListView;
    /**
     * 保存选择的值
     */
    private int checkedIndex = -1;

    private ImageView mBackButtonArea;

    /**
     * 标题
     */
    private TextView mAreaTextView;
    /**
     * 保存上次选择的值
     */
    private int Index;

    private int mDivideHeight;

    /**
     * ;
     * <p/>
     * 视频相关
     */
    private Rect rect;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETLIST:
                    List<AfChapterInfo> list_aAfChapterInfos = (List<AfChapterInfo>) msg.obj;
                    int loadTypeWhenReq = msg.arg1;
                    adapter.notifyDataSetChanged(list_aAfChapterInfos, loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE);
                    if (loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) {//!isLoadMore) {
                        onStop();
                    }
                    settingStop();
                    break;
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);


        close_inputbox(emotion_isClose);
    }


    @Override
    public void onPause() {
        super.onPause();
        //如果有语音正在播放则停止
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }

        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().pause();
    }

    int _start_index;
    int _end_index;

    public void findViews() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.broadcast_area_activity);
        if (editListener == null) {
            editListener = new EditListener();
        }
        Intent intent = getIntent();
        country = intent.getStringExtra("country");
        state = intent.getStringExtra("state");
        String checkedIndexs = intent.getStringExtra("checkedIndex");
        if (checkedIndexs != null) {
            Index = Integer.parseInt(checkedIndexs);
        }
        chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
        mAreaTextView = (TextView) findViewById(R.id.area_textView);
        mAreaTextView.setText(state);
        mAreaTextView.setVisibility(View.VISIBLE);
        send_button = findViewById(R.id.chatting_operate_one);
        input_box_area = findViewById(R.id.input_box_area);
        chatting_options_layout = findViewById(R.id.chatting_options_layout);
        vImageEmotion = findViewById(R.id.image_emotion);
        mBroadcastAreaSelect = (ImageView) findViewById(R.id.broadcast_area_select);
        mBroadcastAreaSelect.setVisibility(View.VISIBLE);
        mBroadcastAreaSelect.setOnClickListener(this);
        mBackButtonArea = (ImageView) findViewById(R.id.back_button_area);
        mBackButtonArea.setOnClickListener(this);
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
                    ToastManager.getInstance().show(BroadcastAreaActivity.this, R.string.comment_long);
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

        emojjView = new EmojjView(this);
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
        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        close_inputbox(emotion_isClose);
                        SoftKeyboard_opne_close = false;
                        break;
                    default:
                        break;
                }
                return false;
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
        lin_no_data = (LinearLayout) this.findViewById(R.id.lin_no_data_area);
    }

    public void setListScrolltoTop() {
        if (mListView != null && mListView.getCount() > 0) {
            mListView.setSelection(0);
        }
    }

    int clike_count = 0;

    private AfPalmchat mAfCorePalmchat;

    public void init() {
        if (mAfCorePalmchat == null) {
            mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        }
        top_refresh = findViewById(R.id.top_refresh_area);

        adapter = new AdapterBroadcastMessages(this, new ArrayList<AfResponseComm.AfChapterInfo>(), Consts.AFMOBI_BRMSG_INPUT, this, BroadcastDetailActivity.FROM_FAR_FAR_AWAY);
        mListView.setAdapter(adapter);
        EventBus.getDefault().register(this);

        showRefresh();//刷新状态 请求数据
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
                    adapter = new AdapterBroadcastMessages(this, broadcastMessageList, Consts.AFMOBI_BRMSG_INPUT, this, BroadcastDetailActivity.FROM_BROADCAST);
                    mListView.setAdapter(adapter);
                    if (lin_no_data != null) {
                        lin_no_data.setVisibility(View.GONE);
                        mListView.setDividerHeight(mDivideHeight);
                    }
                } else {
                    final int size = broadcastMessageList.size();
                    if (size > 0) {
                        if (lin_no_data != null) {
                            lin_no_data.setVisibility(View.GONE);
                            mListView.setDividerHeight(mDivideHeight);
                        }
                        showRefreshSuccess();

                        Message msgMessage = new Message();
                        msgMessage.obj = broadcastMessageList;
                        msgMessage.arg1 = loadTypeWhenReq;
                        msgMessage.what = SETLIST;
                        mHandler.sendMessage(msgMessage);
                    } else {

                        ToastManager.getInstance().show(this, R.string.no_data);
                        mListView.setPullLoadEnable(false);

                    }
                }
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
        loadDataFromServer(pageid);
        if (adapter.getCount() < 0) {
            if (lin_no_data != null) {
                lin_no_data.setVisibility(View.GONE);
                mListView.setDividerHeight(mDivideHeight);
            }
        }
    }

    private void loadDataFromServer(int pageId) {
        int loadType = isLoadMore ? BroadcastFragment.LOADTYPE_WHEN_REQ_LOADMORE : BroadcastFragment.LOADTYPE_WHEN_REQ_NEW;
        //这里之所以要用loadType 而不用isLoadMore 是因为isLoadMore是在上拉的时候就会触发，这样就会有如果先刷新 再更多  刷新返回时 isLoadMore这个状态是错的，导致会有重复广播
        TransmitParameter transmitParameter = new TransmitParameter();
        transmitParameter.setLoadtype(loadType);
        transmitParameter.setState(state);
        mAfCorePalmchat.AfHttpAfBcgetByStateOrCity(pageId, START_INDEX * LIMIT, LIMIT, country, state, null,
                transmitParameter, this);

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
                        ToastManager.getInstance().show(BroadcastAreaActivity.this, R.string.hint_commet);
                    }
                }
            }
        }, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {//这是requestCode  非resultCode
            case IntentConstant.SHARE_BROADCAST:
                if (data != null) {
                    boolean isSuccess = data.getBooleanExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, false);
                    String tempTipContent = data.getStringExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL);
                    String tipContent;
                    if (isSuccess) {
                        tipContent = getResources().getString(R.string.share_friend_success);
                    } else {
                        if (!TextUtils.isEmpty(tempTipContent)) {
                            tipContent = tempTipContent;
                        } else {
                            tipContent = getResources().getString(R.string.share_friend_failed);
                        }
                    }
                    ToastManager.getInstance().showShareBroadcast(this, DefaultValueConstant.SHARETOASTTIME, isSuccess, tipContent);
                }
                break;
        }
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
    /*class LooperThread extends Thread {

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
						int loadTypeWhenReq=msg.arg1;
						int size =  broadcastMessageList.size();
						for (int i = size  - 1; i >= 0 ; i--) {
								AfChapterInfo afChapterInfo = broadcastMessageList.get(i);
								String mid = afChapterInfo.mid; 
								afChapterInfo.isLike = mAfCorePalmchat.AfBcLikeFlagCheck(mid); // check like
								tempAfChapterInfos.add(0, afChapterInfo); 
						} 
						 
						Message msg1 = new Message();
						msg1.obj = tempAfChapterInfos;
						msg1.arg1=loadTypeWhenReq ;
						msg1.what = INSERT_SERVER_DATA_TO_DB;
						mHandler.sendMessage(msg1); 
						break;  
				 
					}
				}
			};
			
			
			if (!isInit) {
//				mHandler.sendEmptyMessage(INIT_FINISH);
			}
			
			Looper.loop();
		}
	}*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_bc_failed_notification:
                CommonUtils.to(this, BroadcastRetryPageActivity.class);
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
                                ToastManager.getInstance().show(BroadcastAreaActivity.this, R.string.hint_commet);
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

            case R.id.broadcast_area_select:
                if (null == PalmchatApp.getApplication().getDefaultList()) {
                    mAfCorePalmchat.AfHttpAfBcgetRegionBroadcast(BroadcastAreaActivity.this);
                    showProgressDialog(R.string.please_wait);
                } else {
                    StartAreaAdapter();
                }
                break;
            case R.id.back_button_area:
                finish();
                break;
        }

    }

    private void StartAreaAdapter() {
        mDialog = new Dialog(BroadcastAreaActivity.this, R.style.Transparent);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View viewdialog = inflater.inflate(R.layout.activity_broadcast_area_listview, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

        mAreaListAdapter = new BroadcastAreaListAdapter(context, PalmchatApp.getApplication().getDefaultList());
        mAreaListView.setAdapter(mAreaListAdapter);
        mAreaListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//只能有一项选中
        mAreaListView.setOnItemClickListener(BroadcastAreaActivity.this);
        mDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ListView lv = (ListView) parent;
        String countrys = PalmchatApp.getApplication().getDefaultList().get(position).country;
        String states = PalmchatApp.getApplication().getDefaultList().get(position).state;

        country = countrys;
        state = states;

        if (checkedIndex != position) {

            int ids = checkedIndex - lv.getFirstVisiblePosition();
            if (ids >= 0) {

                View item = lv.getChildAt(ids);
                if (item != null) {
                    RadioButton rb = (RadioButton) item.findViewById(checkedIndex);
                    if (rb != null) {
                        rb.setChecked(false);
                    }
                }
            }

            RadioButton rb1 = (RadioButton) view.findViewById(position);
            if (rb1 != null) {
                rb1.setChecked(true);
                checkedIndex = position;
            }

        }
        Index = position;
        mDialog.dismiss();
        if (countrys != null && states != null) {
            showRefresh();
        }
        if (mListView.getFirstVisiblePosition() > 1) {
            mListView.setSelection(0);
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
                case Consts.REQ_BCGET_COMMENTS_BY_STATE_OR_CITY:
                    settingStop();
                    if (null != result) {
                        if (VoiceManager.getInstance().isPlaying()) {/**从新请求数据了就暂停语音*/
                            VoiceManager.getInstance().pause();
                        }

                        if (VedioManager.getInstance().getVideoController() != null)
                            VedioManager.getInstance().getVideoController().stop();

                        mAreaTextView.setText(state);
                        String resultState = "";
                        int loadTypeWhenReq = BroadcastFragment.LOADTYPE_WHEN_REQ_NEW;
                        if (user_data != null) {
                            BroadcastAreaActivity.TransmitParameter parameter = (BroadcastAreaActivity.TransmitParameter) user_data;
                            loadTypeWhenReq = parameter.getLoadtype();//获取是否刷新还是加载的状态
                            resultState = parameter.getState();//获取传递过来的城市
                            if (loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) {//如果是刷新列表 需要先clear的 就需要停掉scroll滚动 方式滚动的时候getView 然后数据清了 越界
                                if (null != mListView) {
                                    mListView.stopListViewScroll();
                                }
                            }
                        }
                        if (!resultState.equals(state)) {
                            showRefresh();
                        }
                        AfResponseComm afResponseComm = (AfResponseComm) result;
                        AfPeoplesChaptersList afPeoplesChaptersList = (AfPeoplesChaptersList) afResponseComm.obj;
                        if (afPeoplesChaptersList != null) {
                            ArrayList<AfChapterInfo> list_AfChapterInfo = afPeoplesChaptersList.list_chapters;
                            if (list_AfChapterInfo.size() > 0) {
                                setAdapter(list_AfChapterInfo, Consts.AFMOBI_BRMSG_INPUT, loadTypeWhenReq);
                            } else {
                                ToastManager.getInstance().show(this, R.string.no_data);
                                mListView.setPullLoadEnable(false);
                                if (loadTypeWhenReq == BroadcastFragment.LOADTYPE_WHEN_REQ_NEW) {
                                    //只有当时刷新的时候无数据 才清空 并显示无数据 如果是loadmore的 就不用了
                                    adapter.clear();
                                    adapter.notifyDataSetChanged();
                                    if (adapter.getCount() < 1) {
                                        if (lin_no_data != null) {
                                            lin_no_data.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                        } else {

                            if (adapter.getCount() < 1) {
                                if (lin_no_data != null) {
                                    lin_no_data.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    } else {
                        if (adapter.getCount() < 1) {
                            if (lin_no_data != null) {
                                lin_no_data.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                    break;
                case Consts.REQ_BCGET_REGION_BROADCAST:
                    if (null != result) {
                        AfResponseComm.AfBCGetRegionBroadcastResp afResponseComm = (AfResponseComm.AfBCGetRegionBroadcastResp) result;
                        if (afResponseComm != null && afResponseComm.default_list.size() > 0) {
                            PalmchatApp.getApplication().setDefaultList(afResponseComm.default_list);
                            dismissProgressDialog();
                            if (PalmchatApp.getApplication().getDefaultList() != null) {
                                StartAreaAdapter();
                            }
                        } else {//请求区域没有数据的情况下
                            dismissProgressDialog();
                            ToastManager.getInstance().show(this, R.string.select_area_not_broadcast);
                        }
                    }
                    break;
            }
        } else {
            settingStop();
            if (code == Consts.REQ_CODE_104) {
                adapter.clear();
                showRefresh();
            } else {
                if (flag == Consts.REQ_BCGET_COMMENTS_BY_STATE_OR_CITY
                        ) {
                    if (START_INDEX > 0) {
                        START_INDEX--;
                    }
                }
                Consts.getInstance().showToast(context, code, flag, http_code);

            }
            if (flag == Consts.REQ_BCGET_COMMENTS_BY_STATE_OR_CITY
                    && code == Consts.REQ_CODE_UNNETWORK) {
				/*finish();//根据交互需求  当无网络的时候  是需要关闭当前页面的*/
            }
        }
    }


    private Toast toast;

    public void showToast(int resId) {
        if (null != toast) {
            toast.setText(resId);
            toast.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (VedioManager.getInstance().getVideoController() != null)
            VedioManager.getInstance().getVideoController().stop();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
	 
		/*if(mAfCorePalmchat != null){
			mAfCorePalmchat.AfHttpCancel(handle);
		} */
        EventBus.getDefault().unregister(this);
        //如果有语音正在播放则停止
        if (VoiceManager.getInstance().isPlaying()) {
            VoiceManager.getInstance().pause();
        }
    }

    public void onEventMainThread(AfChapterInfo afChapterInfo) {
        if (Constants.UPDATE_LIKE == afChapterInfo.eventBus_action) {
            adapter.notifyDataSetChanged_updateLikeBymid(afChapterInfo);
        } else if (Constants.UPDATE_DELECT_BROADCAST == afChapterInfo.eventBus_action) {
            adapter.notifyDataSetChanged_removeBymid(afChapterInfo.mid);
        }
    }


    @Override
    public void onRefresh(View view) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            onPause();
            if (!isRefreshing_mListview) {
                isLoadMore = false;
                isRefreshing_mListview = true;
                START_INDEX = 0;
                loadData();
                if (lin_no_data != null) {
                    lin_no_data.setVisibility(View.GONE);
                    mListView.setPullLoadEnable(true);
                }
                two_minutes_Cancel_Refresh_Animation();
            }
        } else {
            ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
            stopRefresh();
			/*finish();//根据交互需求  当无网络的时候  是需要关闭当前页面的*/
        }

    }

    @Override
    public void onLoadMore(View view) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (!isLoadingMore_mListview) {
                isLoadMore = true;
                isLoadingMore_mListview = true;
                START_INDEX++;

//				if (isSelectDB_Data  ) { 
//					loadData();
//				}else {
                loadDataFromServer(pageid);
//				}
            }
        } else {
            ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
            stopLoadMore();
        }
    }

    public void showRefresh() {
        if (NetworkUtils.isNetworkAvailable(context)) {
            int px = AppUtils.dpToPx(this, 60);
            mListView.performRefresh(px);
            isLoadMore = false;
            isRefreshing_mListview = true;
            START_INDEX = 0;
            if (lin_no_data != null) {
                lin_no_data.setVisibility(View.GONE);
                mListView.setDividerHeight(mDivideHeight);
            }

        } else {
            ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
            stopRefresh();
			/*finish();//根据交互需求  当无网络的时候  是需要关闭当前页面的*/
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

        input_box_area.setVisibility(View.GONE);
        chatting_emoji_layout.setVisibility(View.GONE);
        emotion_isClose = true;
        CharSequence hint_name = null;
        hint_name = adapter.getCommentModel().getHint_name();

        vEditTextContent.setHint(hint_name);
        SoftKeyboard_opne_close = true;
        Intent intent = new Intent(this, SoftkeyboardActivity.class);
        intent.putExtra(JsonConstant.KEY_HINT_NAME, hint_name);
        this.startActivityForResult(intent, Constants.COMMENT_FLAG);


    }


    // 隐藏系统键盘
    public void hideSoftInputMethod(EditText ed) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        if (emotion_isClose) {
//			vEditTextContent.setInputType(false);
//			CommonUtils.closeSoftKeyBoard(vEditTextContent);
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
                    ToastManager.getInstance().show(this, R.string.comment_long);
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
                    ToastManager.getInstance().show(context, context.getString(R.string.network_unavailable));
                }

            }
        }, Constants.TWO_MINUTER);
    }

    public void showRefreshSuccess() {
        if (!isLoadMore) {
            top_refresh.getBackground().setAlpha(100);
            top_refresh.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    top_refresh.setVisibility(View.GONE);
                    PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_REFRESH);
                }
            }, 2000);
        }
    }

    public void onEventMainThread(EventFollowNotice eventFollowNotice){
        adapter.notifyDataSetChanged();
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
        return true;
    }

    @Override
    public Activity interface_getActivity() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public void interface_showProgressDialog() {
        // TODO Auto-generated method stub
        showProgressDialog(R.string.please_wait);
    }

    @Override
    public XListView interface_getmListView() {
        // TODO Auto-generated method stub
        return mListView;
    }

    public class BroadcastAreaListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<AfResponseComm.AfBCRegionBroadcast> beans;
        // 用于记录每个RadioButton的状态，并保证只可选一个
        private int checkedIndex = -1;

        class ViewHolder {

            TextView tvName, tvStateName, tyPostNumName;
            RadioButton rb_state;
        }

        public BroadcastAreaListAdapter(Context context, ArrayList<AfResponseComm.AfBCRegionBroadcast> default_list) {
            // TODO Auto-generated constructor stub
            this.beans = default_list;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return beans.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return beans.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            // 页面
            final ViewHolder holder;
            LayoutInflater inflater = LayoutInflater.from(context);
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.broadcast_arealist_item, null);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView
                        .findViewById(R.id.tv_device_country_name);
                holder.tvStateName = (TextView) convertView.findViewById(R.id.tv_device_state_name);
                holder.tyPostNumName = (TextView) convertView.findViewById(R.id.tv_device_postnum_name);
                holder.rb_state = (RadioButton) convertView.findViewById(R.id.rb_light);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String country = beans.get(position).country;
            holder.tvName.setText(country);
            String state = beans.get(position).state;
            holder.tvStateName.setText(state);
            holder.tyPostNumName.setText("(" + beans.get(position).post_num + ")");
            holder.rb_state.setFocusable(false);
            holder.rb_state.setId(position);
            holder.rb_state.setChecked(position == checkedIndex);
            if (position == Index) {
                holder.rb_state.setChecked(true);
            }
            return convertView;
        }

    }

    public class TransmitParameter {

        private int loadtype;//保存状态

        private String state;//保存城市

        public void setLoadtype(int loadtype) {
            this.loadtype = loadtype;
        }

        public int getLoadtype() {
            return loadtype;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }
    }

}