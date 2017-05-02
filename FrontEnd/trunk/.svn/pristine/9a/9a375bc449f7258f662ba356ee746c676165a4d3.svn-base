package com.afmobi.palmchat.ui.activity.chattingroom;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.ui.customview.CutstomEditText;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.listener.OnItemLongClick;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chattingroom.adapter.ChattingRoomMessageAdapter;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.helper.HelpManager;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.ProfileActivity;
import com.afmobi.palmchat.ui.customview.AppDialog.OnResendDialogListener;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.LimitTextWatcher;
import com.afmobi.palmchat.ui.customview.MyTextView;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StartTimer;
import com.afmobi.palmchat.util.TipHelper;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfAttachImageInfo;
import com.core.AfChatroomEntryInfo;
import com.core.AfFriendInfo;
import com.core.AfGrpProfileInfo;
import com.core.AfMessageInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.param.AfChatroomSendMsgParam;
//import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SuppressLint("ValidFragment")
public class ChattingRoomMainFragment extends BaseFragment implements AfHttpResultListener 
				,OnEditorActionListener, IXListViewListener,OnItemLongClick,OnResendDialogListener,StartTimer.TimerComplete{
	public final static int ACTION_INSERT = 0;
	public final static int ACTION_UPDATE = 1;
	private static final int MSG_TEXT = 6;
	private static final int MAX_MESSAGE_LENGTH = 140;
	public final static int ACTION_LONG_CLICK = 7;
	private boolean mIsNoticefy = true;
	
	private XListView vListView;
	private ChattingRoomMessageAdapter adapterListView;
	private ArrayList<AfMessageInfo> listChats = new ArrayList<AfMessageInfo>();
	private ArrayList<AfMessageInfo> listAter = new ArrayList<AfMessageInfo>();
	
	
	private TextView vTextViewOtherMessageToast, vTextViewOtherMessageColon,
	vTextViewOtherMessageName;

	private RelativeLayout relativeLayoutPop;
	
	private View viewFrameToast,mRowArrow;
	private String mFriendAfid;
	private String bm;

	private static CutstomEditText vEditTextContent;
	private ImageView vImageViewRight,vImageEmotion,vButtonSend,btn_keyboard;
	
	public EmojjView emojjView;
	private LinearLayout chatting_emoji_layout,chatting_options_layout;	
	
	private TextView vTextTitle;
	private AfChatroomEntryInfo afChatroomEntry;
	private String aterAfid,aterName;

	private byte aterSex;
	private long lastSoundTime;
	public AfChatroomEntryInfo getAfChatroomEntry() {
		return afChatroomEntry;
	}

	private boolean mIsStop;

	public void setAfChatroomEntry(AfChatroomEntryInfo afChatroomDetail) {
		this.afChatroomEntry = afChatroomDetail;
	}

	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
	private boolean isBottom = false;

	LinearLayout top_message;
	TextView vTextViewFromName;//tv_name
	TextView vMyTextViewFromMsg;//msg
	TextView vMyTextViewMessageColon;
	/**
	 * 获取数据偏移量
	 */
	private int mOffset = 0;

	/**
	 * 每次取得数据条数
	 */
	private int mCount = 20;
	private Handler mainHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(vListView != null){
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			switch (msg.what) {
				case MSG_TEXT://send text
				{
					vEditTextContent.setText("");
					AfMessageInfo msgInfoText = (AfMessageInfo)msg.obj;
					send(msgInfoText.msg, msgInfoText);
					break;
				}

				default:
					break;
			}
		}
	};

	public ChattingRoomMainFragment(){
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.activity_chatting_room_message);
		initViews();
		return mMainView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if(afChatroomEntry != null) {
			mAfCorePalmchat.AfChatroomSetServerOpr(Consts.CHATROOM_OPR_TYPE_SESSION, afChatroomEntry.ctoken, afChatroomEntry.cptoken, afChatroomEntry.cid, PalmchatApp.getOsInfo().getCountry(context));
			vTextTitle.setText(afChatroomEntry.cname);
			mFriendAfid = afChatroomEntry.cid;
			bm = afChatroomEntry.bm;
		}
		PalmchatLogUtils.println("ChattingRoomMainFragment  bm  " + bm);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }

    private boolean isClickForAll = false;
	private void initViews() {
		registerReceiver();
		if(getActivity() != null) {
			getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_chatting)));
		}
		relativeLayoutPop = (RelativeLayout)findViewById(R.id.relativelayout_title);
		relativeLayoutPop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
 
				if (!isClickForAll) {//click for @me
					isClickForAll = true;
					setAdapterForAter();
				} else {//click for all
					isClickForAll = false;
					setAdapterForAll();
				}
			}
		});
		vTextViewOtherMessageName= (TextView)findViewById(R.id.textview_name);
		vTextViewOtherMessageColon= (TextView)findViewById(R.id.textview_colon);
		vTextViewOtherMessageToast = (TextView)findViewById(R.id.textview_message);
		
		viewFrameToast = findViewById(R.id.view_frame);
		vListView = (XListView)findViewById(R.id.listview);
		vListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (OnScrollListener.SCROLL_STATE_FLING == scrollState || OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState || OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
					vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
					CommonUtils.closeSoftKeyBoard(vEditTextContent);
					emojjView.getViewRoot().setVisibility(View.GONE);
				}
				PalmchatLogUtils.println("scrollState  " + scrollState);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Top  true");
				}
				PalmchatLogUtils.println("GroupChatActivity  visibleItemCount  " + visibleItemCount
								+ "firstVisibleItem  " + firstVisibleItem
								+ "totalItemCount  " + totalItemCount
								+ "  vListView.getLastVisiblePosition()  " + vListView.getLastVisiblePosition()
				);

				if (visibleItemCount + firstVisibleItem >= totalItemCount - 1) {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Bottom  true");
					isBottom = true;
				} else {
					PalmchatLogUtils.println("GroupChatActivity  onScroll  Bottom  false");
					isBottom = false;
				}
			}
		});
		vListView.setPullLoadEnable(true);
		vListView.setXListViewListener(this);
		vEditTextContent = (CutstomEditText)findViewById(R.id.chatting_message_edit);
		vEditTextContent.setOnClickListener(this);
		vEditTextContent.setMaxLength(CommonUtils.TEXT_MAX);
		vEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					closeEmotions();
				}
			}

		});
		vEditTextContent.addTextChangedListener(new LimitTextWatcher(vEditTextContent, MAX_MESSAGE_LENGTH));

		vEditTextContent.setOnEditorActionListener(this);
		vEditTextContent.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
					PalmchatLogUtils.e("onKey", "keyCode==KEYCODE_ENTER==" + keyCode);
				}
				return false;
			}
		});
		vTextTitle = ((TextView) findViewById(R.id.title_text));
		
		vImageViewRight = (ImageView) findViewById(R.id.op2);
		vImageViewRight.setVisibility(View.VISIBLE);
		vImageViewRight.setBackgroundResource(R.drawable.group_detail);
		vImageViewRight.setOnClickListener(this);
		vButtonSend = (ImageView) findViewById(R.id.chatting_send_button);
		vButtonSend.setOnClickListener(this);
		btn_keyboard = (ImageView) findViewById(R.id.btn_keyboard);
		btn_keyboard.setOnClickListener(this);

		chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
		emojjView = new EmojjView(getActivity());
		emojjView.select(EmojiParser.SUN);
		emojjView.getViewRoot().findViewById(R.id.scroll_parent).setVisibility(View.GONE);

		
		emojjView.getViewRoot().setVisibility(View.GONE);
		chatting_emoji_layout.addView(emojjView.getViewRoot());
		vImageEmotion = (ImageView) findViewById(R.id.image_emotion);
		vImageEmotion.setOnClickListener(this);
		
		ImageView imageViewBack = (ImageView) findViewById(R.id.back_button);
		imageViewBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});
		ImageView change_list_img = (ImageView)findViewById(R.id.change_list_img);
		change_list_img.setVisibility(View.VISIBLE);
		change_list_img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.chatting_room_title_drop_down));
		chatting_options_layout = (LinearLayout)findViewById(R.id.chatting_options_layout);
		
//		menu.getmViewAbove().setViewPager(emojjView.mViewPager);
		mRowArrow = findViewById(R.id.row_arrow);
		setAdapter(null);
		adapterListView.setOnItemClick(this);

		adapterListView.setOnResendDialogListener(this);
		
		top_message = (LinearLayout) findViewById(R.id.top_message);
		vTextViewFromName = (TextView)findViewById(R.id.tv_name);
		vMyTextViewFromMsg = (TextView)findViewById(R.id.msg);
		vMyTextViewMessageColon = (TextView)findViewById(R.id.textview_top_colon);

		vEditTextContent.setOnTouchListener(new View.OnTouchListener() {
			float actionx = 0,actiony = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				switch (action) {
					// 当按下的时候
					case (MotionEvent.ACTION_DOWN):
						actionx = event.getX();
						actiony = event.getY();
						break;
					// 当按上的时候
					case (MotionEvent.ACTION_UP):
						if(Math.abs(actionx - event.getX()) > 10d || Math.abs(actiony - event.getY()) > 10d){
							closeEmotions();
						}
						break;
				}
				return false;
			}
		});
	}

	/**
	 * 返回
	 */
	private void back(){
		mAfCorePalmchat.AfDbMsgClear(AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM, afChatroomEntry.cid);
		mAfCorePalmchat.AfChatroomSetServerOpr(Consts.CHATROOM_OPR_TYPE_CLEAR, null, null, null,null);
		fragmentActivity.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.op2: {//右上角
				CommonUtils.closeSoftKeyBoard(vEditTextContent);
				ChattingRoomMemberFragment chattingRoomMemberFragment = new ChattingRoomMemberFragment();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				tx.hide(this);
				tx.add(R.id.slidingmenu_layout, chattingRoomMemberFragment);
				tx.addToBackStack(null);
				tx.commit();
				break;
			}
			case R.id.chatting_send_button: {//send button
				sendTextOrEmotion();
				break;
			}
			case R.id.image_emotion://emotion button
			{
				CommonUtils.closeSoftKeyBoard(vEditTextContent);
				emojjView.getViewRoot().setVisibility(View.VISIBLE);
				emojjView.getViewRoot().setFocusableInTouchMode(true);
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				btn_keyboard.setVisibility(View.VISIBLE);
				vImageEmotion.setVisibility(View.GONE);
				mainHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						vListView.setSelection(adapterListView.getCount());
					}
				},20);
				break;
			}	
			case R.id.chatting_message_edit://onclick on eidttext
			{
				closeEmotions();
				break;
			}
			case R.id.btn_keyboard:
				btn_keyboard.setVisibility(View.GONE);
				emojjView.getViewRoot().setVisibility(View.GONE);
				vEditTextContent.requestFocus();
				CommonUtils.showSoftKeyBoard(vEditTextContent);
				vImageEmotion.setVisibility(View.VISIBLE);
				break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unRegisterReceiver();
	}
	
	public void closeEmotions() {
		emojjView.getViewRoot().setVisibility(View.GONE);
		btn_keyboard.setVisibility(View.GONE);
		vImageEmotion.setVisibility(View.VISIBLE);
		vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		vListView.setSelection(adapterListView.getCount());
	}
	
	
	private String getEditTextContent(){
		return vEditTextContent.getText().toString();
	}
	
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (actionId) {
		case EditorInfo.IME_ACTION_SEND:
			sendTextOrEmotion();
			break;
		case EditorInfo.IME_NULL:
			if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				EditText edit = (EditText) v;
				//获取光标位置
				int index = edit.getSelectionStart();
				String str = edit.getText().insert(index,"\n").toString();
				CharSequence text = EmojiParser.getInstance(getActivity()).parse(str);
				v.setText(text);

				if (str.length() >= ReplaceConstant.MAX_SIZE) {
					edit.setSelection(ReplaceConstant.MAX_SIZE);//将光标移至文字末�?
				} else {
					edit.setSelection(index + 1);//移到光标处
				}
			}
			break;
		}

		return true;
	}

	/**
	 * 获取@me的消息
	 */
	private List<AfMessageInfo>  getDataForAter(){
			if(afChatroomEntry != null) {
				AfMessageInfo[] arraysAter = mAfCorePalmchat.AfDbRecentMsgGetRecord((AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM | AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD),
						afChatroomEntry.cid, mOffset, mCount);
				if (arraysAter != null && arraysAter.length > 0) {
					List<AfMessageInfo> list = Arrays.asList(arraysAter);
					return list;
				}
			}
		return null;
	}
	
	//send textOrEmotion
	private void sendTextOrEmotion() {
		String content = getEditTextContent();
		if(content.length() > 0){
			getMessageInfoForText(AfMessageInfo.MESSAGE_CHATROOM_NORMAL,null,0,0,mainHandler,MSG_TEXT,ACTION_INSERT,null);
		}
	}

	
	public void resendTextOrEmotion(AfMessageInfo afMessageInfo){
		getMessageInfoForText(AfMessageInfo.MESSAGE_CHATROOM_NORMAL,null,0,0,mainHandler,MSG_TEXT,ACTION_UPDATE,afMessageInfo);
	}

	
	private void getMessageInfoForText(final int msgType,final String fileName,
			final int fileSize,final int length,final Handler handler,final int handler_MsgType,final int action,final AfMessageInfo afMessageInfo){
		PalmchatLogUtils.println("getMessageInfoForText ");
		
		final AfMessageInfo messageInfo = new AfMessageInfo();
		new Thread(new Runnable() {
			public void run() {
				switch (msgType) {
				case AfMessageInfo.MESSAGE_CHATROOM_NORMAL:
					if(action == ACTION_INSERT){
						String content = getEditTextContent();
						boolean isTopMessage = CommonUtils.isTopMessage(content, bm);
						messageInfo.isTopMessage = isTopMessage;
						messageInfo.client_time = System.currentTimeMillis();
						messageInfo.fromAfId = mFriendAfid;
//						messageInfo.toAfId = CacheManager.getInstance().getMyProfile().afId;
						
						messageInfo.msg = content;
						if(isAter(messageInfo.msg)){
							if(!TextUtils.isEmpty(aterName)){
								messageInfo.name = "@"+aterName+":";
							}
							messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM | AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD;
						}else{
							messageInfo.type =  AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM | AfMessageInfo.MESSAGE_CHATROOM_NORMAL;
						}

						messageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						messageInfo._id = mAfCorePalmchat.AfDbCrMsgInsert(messageInfo);
						if(isAter(messageInfo.msg)){
							CommonUtils.getRealList(listAter,messageInfo);
						}
					}else if(action == ACTION_UPDATE){
						boolean isTopMessage = CommonUtils.isTopMessage(afMessageInfo.msg, bm);
						afMessageInfo.isTopMessage = isTopMessage;
						afMessageInfo.client_time = System.currentTimeMillis();
						afMessageInfo.status = AfMessageInfo.MESSAGE_SENTING;
						mAfCorePalmchat.AfDbCrMsgUpdate(afMessageInfo);
					}
					break;
				}
				if(action == ACTION_INSERT){
					handler.obtainMessage(handler_MsgType, messageInfo).sendToTarget();
				}else if(action == ACTION_UPDATE){
					handler.obtainMessage(handler_MsgType, afMessageInfo).sendToTarget();
				}
			}
		}).start();
	}
	
	
	private void send(String content, AfMessageInfo msgInfo) {
		listChats.add(msgInfo);
		setAdapter(null);
		AfChatroomSendMsgParam afChatroomSendMsgParam = new AfChatroomSendMsgParam();
		if(isAter(content)){
			PalmchatLogUtils.println("ChattinngRoomMainFragment  send  ater  ");
			afChatroomSendMsgParam.command = Consts.MSG_CMMD_CHATROOM_ADD;
			afChatroomSendMsgParam.recv_afid = aterAfid;
			afChatroomSendMsgParam.recv_name = aterName;
			String str = "@"+aterName+":";
			content = content.replace(str, "");
			afChatroomSendMsgParam.msg = content;	
		}else{
			PalmchatLogUtils.println("ChattinngRoomMainFragment  send  normal  ");
			afChatroomSendMsgParam.command = Consts.MSG_CMMD_CHATROOM_NORMAL;
			afChatroomSendMsgParam.msg = content;	
		}
		
		// heguiming 2013-12-04
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.S_M_TEXT);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.P_NUM);
		new ReadyConfigXML().saveReadyInt(ReadyConfigXML.C_NUM);
		
//		MobclickAgent.onEvent(context, ReadyConfigXML.S_M_TEXT);
//		MobclickAgent.onEvent(context, ReadyConfigXML.P_NUM);
//		MobclickAgent.onEvent(context, ReadyConfigXML.C_NUM);
		
		afChatroomSendMsgParam.sid =  System.currentTimeMillis();
		msgInfo.mSex = aterSex;
		mAfCorePalmchat.AfHttpChatroomSendMsg(afChatroomSendMsgParam, msgInfo , this);
	}
	
	
	private boolean isAter(String content) {
		// TODO Auto-generated method stub
		if(content != null && aterAfid != null && content.trim().startsWith("@"+aterName+":")){
			return true;
		}
		return false;
	}



	private void setAdapter(AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		if(adapterListView == null){
			adapterListView = new ChattingRoomMessageAdapter(context,listChats);
			vListView.setAdapter(adapterListView);  
		}else{
//			adapterListView.setList(listChats);
			if(afMessageInfo != null && MessagesUtils.ACTION_UPDATE == afMessageInfo.action){
				PalmchatLogUtils.println("ChattingRoomMainFragemnt  setAdapter  afMessageInfo.action ACTION_UPDATE");
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
			}else if(afMessageInfo != null){
				PalmchatLogUtils.println("ChattingRoomMainFragemnt  setAdapter  afMessageInfo.action "+afMessageInfo.action);
				vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			adapterListView.notifyDataSetChanged();
//			vListView.setSelection(adapterListView.getCount());
			if ((mIsNoticefy && 0 < adapterListView.getCount()) || isBottom) {
				vListView.setSelection(adapterListView.getCount());
				mIsNoticefy = false;
			}
		}
	}


	private void setAdapterForAter() {
		// TODO Auto-generated method stub
		vTextTitle.setText(context.getString(R.string.ater_me));
		chatting_options_layout.setVisibility(View.GONE);
		emojjView.getViewRoot().setVisibility(View.GONE);
		
		adapterListView.setList(listAter);
		adapterListView.notifyDataSetChanged();
		
		CommonUtils.closeSoftKeyBoard(vEditTextContent);
		vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		vListView.setSelection(adapterListView.getCount());
	}
	
	private void setAdapterForAll() {
		// TODO Auto-generated method stub
		vTextTitle.setText(afChatroomEntry.cname);
		chatting_options_layout.setVisibility(View.VISIBLE);
		
		adapterListView.setList(listChats);
		adapterListView.notifyDataSetChanged();
		
		vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		vListView.setSelection(adapterListView.getCount());
	}
	

	
	public void setFace(int id,String data) {
		Drawable drawable;
		if (id != 0 && !CommonUtils.isDeleteIcon(id,vEditTextContent)) {
			drawable = getResources().getDrawable(id);
			drawable.setBounds(0, 0, CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
			ImageSpan span = new ImageSpan(drawable);
			SpannableString spannableString = new SpannableString(data);
			spannableString.setSpan(span, 0, data.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			Log.e("ttt->", spannableString.length()+"->" + vEditTextContent.length());
			int length = spannableString.length() + vEditTextContent.length();
			if (length < MAX_MESSAGE_LENGTH) {
				int index = vEditTextContent.getSelectionStart();
				Editable editable = vEditTextContent.getText();
				editable.insert(index, spannableString);
			}
		}
	}
	

	//收到消息刷新列表
	public void noticefy(final AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("ChattingRoomMainFragment  noticefy  "+afMessageInfo);
		if(afMessageInfo != null){
			PalmchatLogUtils.println("ChattingRoomMainFragment  noticefy  afMessageInfo  " + afMessageInfo.toString());
		}
		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		if(mFriendAfid.equals(afMessageInfo.toAfId)){
			if(msgType == AfMessageInfo.MESSAGE_FRIEND_REQ || msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS){
				friendRequest(afMessageInfo);
			}else{
				int type = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
				switch (type) {
				case AfMessageInfo.MESSAGE_CHATROOM_NORMAL:
				case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD:
				case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT:
				case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ENTRY:
						if(type == AfMessageInfo.MESSAGE_CHATROOM_NORMAL || type == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD
								|| type == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT) {
							new Thread(new Runnable() {
								public void run() {
									mAfCorePalmchat.AfDbMsgSetStatusEx(AfMessageInfo.MESSAGE_TYPE_MASK_CHATROOM,afChatroomEntry.cid,
											AfMessageInfo.MESSAGE_UNREAD,AfMessageInfo.MESSAGE_READ);
								}
							}).start();
							if(type == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD
									|| type == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_INNER_AT){
								CommonUtils.getRealList(listAter,afMessageInfo);
							}
						}
						CommonUtils.getRealList(listChats,afMessageInfo);
						showVibrate();
						
						setAdapter(null);
					break;
				case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_AD: //add or update top message
					showTopMessage(afMessageInfo);
					break;
					
				case AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_AD_CANCEL: //cancel top message
					disAbleTopMessage();
					break;
				}
			}
			
		}else{
			if(MessagesUtils.isGroupSystemMessage(afMessageInfo.type)){
				PalmchatLogUtils.println("ChattingRoomMainFragment return "+afMessageInfo.msg);
				return;
			}
			viewFrameToast.setVisibility(View.GONE);
			if(MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isGroupChatMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type)){

				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
					vTextViewOtherMessageToast.setBackgroundDrawable(null);
				}else{
					vTextViewOtherMessageToast.setBackground(null);
				}
				switch (msgType) {
				case AfMessageInfo.MESSAGE_CHATROOM_NORMAL:
				case AfMessageInfo.MESSAGE_EMOTIONS:
				case AfMessageInfo.MESSAGE_TEXT:
					String msg = afMessageInfo.msg;
					final CharSequence content = EmojiParser.getInstance(context).parse(msg);
					vTextViewOtherMessageToast.setText(content);
					break;
				case AfMessageInfo.MESSAGE_STORE_EMOTIONS:
					vTextViewOtherMessageToast.setText(getString(R.string.msg_custom_emoticons));
					break;
				case AfMessageInfo.MESSAGE_VOICE:
					vTextViewOtherMessageToast.setText(fragmentActivity.getString(R.string.voice));
					break;
				case AfMessageInfo.MESSAGE_IMAGE:
					vTextViewOtherMessageToast.setText("");
					vTextViewOtherMessageToast.setBackgroundResource(R.drawable.pop_default);
					break;
				case AfMessageInfo.MESSAGE_CARD:
					vTextViewOtherMessageToast.setText(R.string.name_card);
					break;
				case AfMessageInfo.MESSAGE_FRIEND_REQ:
				case AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS:
					top_message.setVisibility(View.GONE);
					friendRequest(afMessageInfo);
					return;
				}
				top_message.setVisibility(View.GONE);
				final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.fromAfId);
				PalmchatLogUtils.println("GroupChatActivity  handleMessage  afFriendInfo  "+afFriendInfo);
				if(afFriendInfo != null){
					vTextViewOtherMessageName.setText(afFriendInfo.name);
					vTextViewOtherMessageColon.setVisibility(View.VISIBLE);
					StartTimer startTimer = StartTimer.startTimer(afMessageInfo, viewFrameToast);
					startTimer.setOnItemClick(this);
					StartTimer.timerHandler.post(startTimer);
					viewFrameToast.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(MessagesUtils.isPrivateMessage(afMessageInfo.type) || MessagesUtils.isSystemMessage(afMessageInfo.type)){
								toChatting(afFriendInfo,afFriendInfo.afId,afFriendInfo.name);
							}else if(MessagesUtils.isGroupChatMessage(afMessageInfo.type)){
								//fragmentActivity.finish();
								toGroupChatting(afMessageInfo.toAfId);
							}
						}
					});
				}
				
			}else{
				PalmchatLogUtils.println("ChattingRoomFragment Chatroom message");
			}
		}
	}
	
	void toGroupChatting(String groupId){
		AfGrpProfileInfo groupListItem = null;
		groupListItem = CacheManager.getInstance().searchGrpProfileInfo(groupId);
		Bundle bundle = new Bundle();
		bundle.putInt(JsonConstant.KEY_STATUS, groupListItem.status);
		if (groupListItem != null && !"".equals(groupListItem.afid)
				|| null != groupListItem.afid) {
			bundle.putString(
					GroupChatActivity.BundleKeys.ROOM_ID,
					groupListItem.afid);
		}
		if (groupListItem != null && !"".equals(groupListItem.name)
				|| null != groupListItem.name) {
			bundle.putString(
					GroupChatActivity.BundleKeys.ROOM_NAME,
					groupListItem.name);
		}
		bundle.putBoolean(JsonConstant.KEY_FLAG, true);
		HelpManager.getInstance(context).jumpToPage(
				GroupChatActivity.class, bundle, false, 0,
				false);
	}

	
	private void toChatting(AfFriendInfo infos, String afid, String name) {
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("toChatting");
		Intent intent = new Intent(context, Chatting.class);
		intent.putExtra(JsonConstant.KEY_FROM_UUID, afid);
		intent.putExtra(JsonConstant.KEY_FROM_NAME, name);
		intent.putExtra(JsonConstant.KEY_FRIEND, infos);
		intent.putExtra(JsonConstant.KEY_FROM_ALIAS, infos.alias);
		intent.putExtra(JsonConstant.KEY_FLAG, true);
		context.startActivity(intent);
		//fragmentActivity.finish();
	}
	
	
	private void showVibrate() {
		if(app.getSettingMode().isVibratio()){
			if((System.currentTimeMillis() - lastSoundTime) > 4000 ||  lastSoundTime == 0) {
				TipHelper.vibrate(context, 200L);
			}
			lastSoundTime = System.currentTimeMillis();
			PalmchatLogUtils.println("ChattingRoomMainFragment  handleMessageFromServer  end  lastSoundTime  "+lastSoundTime);
		}
	}
	
	private void friendRequest(final AfMessageInfo afMessageInfo) {
/*		final int msgType = AfMessageInfo.MESSAGE_TYPE_MASK & afMessageInfo.type;
		if(msgType == AfMessageInfo.MESSAGE_FRIEND_REQ || msgType == AfMessageInfo.MESSAGE_CHATROOM_SYSTEM_ADD){
			final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
			vTextViewOtherMessageToast.setBackgroundDrawable(null);
			String showStr = "";
			if(afFriendInfo != null){
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, 
						afFriendInfo.name, getString(R.string.want_to_be_friend_ignored));
			}else{
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, 
						afMessageInfo.name, getString(R.string.want_to_be_friend_ignored));
			}
			vTextViewOtherMessageColon.setVisibility(View.GONE);
			vTextViewOtherMessageToast.setText(showStr);
			StartTimer startTimer = StartTimer.startTimer(afMessageInfo, viewFrameToast);
			StartTimer.timerHandler.post(startTimer);
			startTimer.setOnItemClick(this);
			viewFrameToast.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
			 	}
			});
		}else if(msgType == AfMessageInfo.MESSAGE_FRIEND_REQ_SUCCESS){
			final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
			vTextViewOtherMessageToast.setBackgroundDrawable(null);
			String showStr = "";
			if(afFriendInfo != null){
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, 
						afFriendInfo.name, getString(R.string.frame_toast_friend_req_success));
			}else{
				showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, 
						afMessageInfo.name, getString(R.string.frame_toast_friend_req_success));
			}*/
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
				vTextViewOtherMessageToast.setBackgroundDrawable(null);
			}else{
				vTextViewOtherMessageToast.setBackground(null);
			}
			final AfFriendInfo afFriendInfo = CacheManager.getInstance().searchAllFriendInfo(afMessageInfo.getKey());
			vTextViewOtherMessageToast.setText(CommonUtils.getFriendRequestContent(PalmchatApp.getApplication().getApplicationContext(),afMessageInfo));
			vTextViewOtherMessageColon.setVisibility(View.GONE);
			StartTimer.timerHandler.post(StartTimer.startTimer(afMessageInfo,viewFrameToast));
			viewFrameToast.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(afFriendInfo != null) {
						toChatting(afFriendInfo, afFriendInfo.afId, afFriendInfo.name);
					}
			 	}
			});
		//}
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(MessagesUtils.CHATTING_ROOM_TEXT_MESSAGE);
		filter.addAction(MessagesUtils.CHATTING_ROOM_IMAGE_MESSAGE);
		filter.addAction(MessagesUtils.CHATTING_ROOM_VOICE_MESSAGE);
		filter.addAction(MessagesUtils.CHATTING_ROOM_CAMERA_MESSAGE);
		
		fragmentActivity.registerReceiver(requestReceiver, filter);
	}
	
	private void unRegisterReceiver() {
		if(requestReceiver != null){
			fragmentActivity.unregisterReceiver(requestReceiver);
		}
		requestReceiver = null;
	}
	
	BroadcastReceiver requestReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String action = intent.getAction();
				int msg_id  = intent.getIntExtra(JsonConstant.KEY_MESSAGE_ID, -1);
				int status = intent.getIntExtra(JsonConstant.KEY_STATUS,-1);
				if (MessagesUtils.CHATTING_ROOM_TEXT_MESSAGE.equals(action) || MessagesUtils.CHATTING_ROOM_VOICE_MESSAGE.equals(action)) {
					updateStatus(msg_id, status);
				} else if (MessagesUtils.CHATTING_ROOM_IMAGE_MESSAGE.equals(action)) {
					int progress = intent.getIntExtra(JsonConstant.KEY_PROGRESS, 0);
					updateImageStautsByBroadcast(msg_id,status,progress);
				} else if(MessagesUtils.CHATTING_ROOM_CAMERA_MESSAGE.equals(action)){
					AfMessageInfo afMessageInfo = CacheManager.getInstance().getAfMessageInfo();
					mainHandler.obtainMessage(MessagesUtils.SEND_IMAGE, afMessageInfo).sendToTarget();
				}
			}
		}
	};
	
	
	public void updateImageStautsByBroadcast(final int msgId, int status,int progress){
		int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
		if(msgId != -1 && index == -1){
			AfMessageInfo msg = CacheManager.getInstance().getAfMessageInfo();
			if(msg != null && mFriendAfid.equals(msg.toAfId)){
				if(msg != null && msgId == msg._id){
					PalmchatLogUtils.println("Chatting  updateImageStautsByBroadcast  ");
					CommonUtils.getRealList(adapterListView.getLists(), msg);
					CacheManager.getInstance().setAfMessageInfo(null);
					
					updateImageStautsByBroadcast(msgId, status, progress);
				}else{
					new Thread(new Runnable() {
						public void run() {
							PalmchatLogUtils.println("Chatting  Thread  updateImageStautsByBroadcast  ");
							AfMessageInfo msg = mAfCorePalmchat.AfDbMsgGet(msgId);
							if(msg != null){
								CommonUtils.getRealList(adapterListView.getLists(), msg);
								mainHandler.obtainMessage(MessagesUtils.MSG_CAMERA_NOTIFY,msg).sendToTarget();
							}
						}
					}).start();
				}
			}
		}else if (index != -1 && index < adapterListView.getCount()) {
			AfMessageInfo afMessageInfo = adapterListView.getItem(index);
			if(afMessageInfo != null && mFriendAfid.equals(afMessageInfo.toAfId)){
				PalmchatLogUtils.println("updateImageStatusByBroadcast  afMessageInfo  "+afMessageInfo);
				afMessageInfo.status = status;
				AfAttachImageInfo afAttachImageInfo = (AfAttachImageInfo) afMessageInfo.attach;
				afAttachImageInfo.progress = progress;
				adapterListView.notifyDataSetChanged();
			}
		}
	}
	
	
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		// TODO Auto-generated method stub
		PalmchatLogUtils.println("ChattingRoom  code "+code+"  flag  "+flag+"  result  "+result +"  user_data  "+user_data);
		int msg_id;
		AfMessageInfo messageInfo = (AfMessageInfo)user_data;
		if(code == Consts.REQ_CODE_SUCCESS){
			switch (flag) {
			case Consts.REQ_CHATROOM_MSG_SEND:
			{
				msg_id = messageInfo != null ? messageInfo._id : DefaultValueConstant.LENGTH_0;
				PalmchatLogUtils.println("onresult msg_id  "+msg_id);
				if(msg_id > DefaultValueConstant.LENGTH_0){
					if(messageInfo.isTopMessage){
						String content = messageInfo.msg;
						content = content.substring(1,content.length());
						if(!TextUtils.isEmpty(content)){
//							messageInfo.msg = content;
							showTopMessage(messageInfo);
						}else{
							disAbleTopMessage();
						}
					}
					sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_SENT);
				}else{
					updateStatus(messageInfo._id, AfMessageInfo.MESSAGE_UNSENT);
					ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
					PalmchatLogUtils.println("case Consts.REQ_MSG_SEND code "+code+"  flag  "+flag);
				}
				break;
			}

			default:
				break;
			}
		}else{
			switch (flag) {
			case Consts.REQ_CHATROOM_MSG_SEND:
				AfMessageInfo afMessageInfo = (AfMessageInfo) user_data;
				if(code == Consts.REQ_CODE_NOT_IN_CHATROOM){
					//ToastManager.getInstance().show(context, context.getString(R.string.exit_chatting_room));
					if(afMessageInfo != null){
						String sex = afMessageInfo.mSex == Consts.AFMOBI_SEX_MALE ? fragmentActivity.getString(R.string.he) : fragmentActivity.getString(R.string.she);
						String showStr = CommonUtils.replace(DefaultValueConstant.TARGET_NAME,sex, fragmentActivity.getString(R.string.has_left_room));
						ToastManager.getInstance().show(context,showStr);
						sendMsgFailure(flag, code, afMessageInfo._id);
					}
//					else{
//						sendMsgFailure(flag, code, user_data);
//					}
				}else{
					if(!mIsStop) {
						PalmchatLogUtils.i("ChattingRoomMainFragment","NetWork Error");
						Consts.getInstance().showToast(context, code, flag, http_code);
					}
					PalmchatLogUtils.i("ChattingRoomMainFragment","Show NetWork Error");
					sendMsgFailure(flag, code, afMessageInfo._id);
				}
				break;
			}
			if((code == Consts.REQ_CODE_65 
					|| code == Consts.REQ_CODE_NOT_ENTRY_CHATROOM 
					|| code == Consts.REQ_CODE_GROUP_NOT_EXIST)
					&& CommonUtils.isChattingRoom(context)){
				PalmchatLogUtils.println("ChattingRoom  Consts.REQ_CODE_NOT_IN_CHATROOM  code "+code+"  flag  "+flag+"  result  "+result +"  user_data  "+user_data);
				ToastManager.getInstance().show(context, context.getString(R.string.exit_chatting_room));
				context.finish();
				return;
			}
			if(code == Consts.REQ_CODE_BLOCK_PEOPLE){
				ToastManager.getInstance().show(context, R.string.beblocked);
			}
		}
	}
	

	private void showTopMessage(final AfMessageInfo entity) {
		// TODO Auto-generated method stub
		String msg = entity.msg;
		if(!TextUtils.isEmpty(msg) && msg.startsWith(DefaultValueConstant.LABEL)){
			msg = msg.substring(1,msg.length());
		}
		if(TextUtils.isEmpty(msg)){
			disAbleTopMessage();
			return;
		}

		top_message.setVisibility(View.VISIBLE);
		mRowArrow.setVisibility(View.GONE);
		final int status = entity.status;
		String fromAfid = entity.fromAfId;
		String name = entity.name;
		int age = entity.age;
		byte sex = entity.sex;
		PalmchatLogUtils.println("showTopMessage  status  " + status + "  fromAfid  " +
				fromAfid + "  name  " + name + "  age  " + age+"  sex  "+sex+"  msg  "+msg);

		CharSequence text = EmojiParser.getInstance(context).parse(msg);
	/*	if(TextUtils.isEmpty(name)){*/
		vMyTextViewMessageColon.setVisibility(View.GONE);
		//}
		//vTextViewFromName.setText(name);
		vTextViewFromName.setVisibility(View.GONE);
		vMyTextViewFromMsg.setText(text);

		 afChatroomEntry.fromAfId = fromAfid;
		 afChatroomEntry.name = name;
		 afChatroomEntry.age = (byte) age;
		 afChatroomEntry.sex = sex;
		 afChatroomEntry.msg = msg;
	}

	private void disAbleTopMessage(){
		top_message.setVisibility(View.GONE);
		afChatroomEntry.msg = DefaultValueConstant.LABEL;
	}

	/**send message error*/
	private void sendMsgFailure(int flag, int code, Object user_data) {
		int msg_id;
		msg_id = user_data != null ? Integer.parseInt(user_data.toString()) : DefaultValueConstant.LENGTH_0;
		if(msg_id > DefaultValueConstant.LENGTH_0){
			sendBroadcastForTextOrVoice(msg_id, AfMessageInfo.MESSAGE_UNSENT);
		}else{
			updateStatus(msg_id, AfMessageInfo.MESSAGE_UNSENT);
			ToastManager.getInstance().show(context, getString(R.string.unkonw_error));
			PalmchatLogUtils.println("ChattingRoom  else Consts.REQ_MSG_SEND else code "+code+"  flag  "+flag);
		}
	}
	




	void sendBroadcastForTextOrVoice(int msg_id,int status){
		Intent intent = new Intent(MessagesUtils.CHATTING_ROOM_TEXT_MESSAGE);
		intent.putExtra(JsonConstant.KEY_MESSAGE_ID,msg_id);
		intent.putExtra(JsonConstant.KEY_STATUS, status);
		fragmentActivity.sendBroadcast(intent);
		if(!CommonUtils.isChattingRoom(fragmentActivity)){
			updateStatus(msg_id, status);
		}
	}
	


	public void updateStatus(final int msgId, final int status) {

		int index = ByteUtils.indexOf(adapterListView.getLists(), msgId);
		for (AfMessageInfo af : adapterListView.getLists()) {
			PalmchatLogUtils.println("af.id  "+af._id);
		}
		PalmchatLogUtils.println("index  "+index+"  msgId  "+msgId+"  status  "+status);
		if (index != -1 && index < adapterListView.getCount()) {
			AfMessageInfo afMessageInfo = adapterListView.getItem(index);
			afMessageInfo.status = status;
			PalmchatLogUtils.println("afMessageInfo.msgId  "+afMessageInfo._id+"  afMessageInfo.status  "+afMessageInfo.status);
			adapterListView.notifyDataSetChanged();
		}
		new StatusThead(msgId,status).start();
		
	}

	@Override
	public void timerComplete(boolean isVisible) {
		if(isVisible){
			if(!TextUtils.isEmpty(afChatroomEntry.msg)){
				viewFrameToast.setVisibility(View.GONE);
				top_message.setVisibility(View.VISIBLE);
			}
		}
		else{
			top_message.setVisibility(View.GONE);
		}
	}


	public class StatusThead extends Thread{
		private int msgId;
		private int status;
		public StatusThead(int msgId, int status){
			this.msgId = msgId;
			this.status = status;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int msg_Id = mAfCorePalmchat.AfDbCrMsgSetStatus(msgId, status);
			PalmchatLogUtils.println("update status msg_id "+msg_Id);
		}
	}

	//执行异步的操作
    private class GetDataTask extends AsyncTask<Void, Void, List<AfMessageInfo>> {

		//是否为初始化数据，true为初始化数据，false为下拉刷新数据
		boolean isInit;

		public GetDataTask(boolean isInit) {
			this.isInit = isInit;
		}
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			listHeader.setVisibility(View.VISIBLE);
//			AnimUtils.getInstance(Chatting.this).start(listHeaderProgress);
		}
    	
    	
        @Override
        protected List<AfMessageInfo> doInBackground(Void... params) {
			// Simulates a background job.
			List<AfMessageInfo> recentData = null;
			try {
				if(isClickForAll || isInit){
					recentData = getDataForAter();
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return recentData;
		}

        @Override
        protected void onPostExecute(List<AfMessageInfo> result) {
			AfMessageInfo afMessageInfo;
			if (result != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					afMessageInfo = result.get(i);
					if(isInit) {
						CommonUtils.getRealList(listChats, afMessageInfo);
					}
					CommonUtils.getRealList(listAter, result.get(i));
				}
				if(isInit) {
					adapterListView.setList(listChats);
				}
				else{
				if(!isInit)
					adapterListView.setList(listAter);
				}
			}
			setAdapter(null);
			stopRefresh();
			if((!isClickForAll && !isInit) || (isClickForAll && (result == null || result.size() == 0))){
				ToastManager.getInstance().show(context, R.string.already_top);
			}
			super.onPostExecute(result);
		}


    }

    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
		mIsStop = false;
		if(isClickForAll) {
			isClickForAll = false;
			setAdapterForAll();
		}
    	mAfCorePalmchat.AfChatroomPollSetStatus(Consts.POLLING_STATUS_RUNNING);//chatroom polling
//    	MobclickAgent.onPageStart("ChattingRoomMainFragment");//ChattingRoomMainAct.class.getName().equals(CommonUtils.getCurrentActivity(context)) || MyActivityManager.getScreenManager().getCurrentActivity() instanceof ChattingRoomMainAct
    	
    	if(afChatroomEntry!=null && !TextUtils.isEmpty(afChatroomEntry.msg)){
			AfMessageInfo messageInfo = new AfMessageInfo();
			messageInfo.fromAfId = afChatroomEntry.fromAfId;
			messageInfo.name = afChatroomEntry.name;
			messageInfo.age = afChatroomEntry.age;
			messageInfo.sex = afChatroomEntry.sex;
			messageInfo.msg = afChatroomEntry.msg;
			
			showTopMessage(messageInfo);
		}
		new GetDataTask(true).execute();
    }
    
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
		mIsStop = true;
    	super.onPause();
		mOffset = 0;
//    	MobclickAgent.onPageEnd("ChattingRoomMainFragment");
    }
    
    @Override
    public void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
		mIsStop = true;
    	mAfCorePalmchat.AfChatroomPollSetStatus(Consts.POLLING_STATUS_PAUSE);//chatroom polling
    }

    private void stopRefresh() {
    	// TODO Auto-generated method stub
    	vListView.stopRefresh();
		vListView.stopLoadMore();
		vListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
    }
	
	@Override
	public void onRefresh(View view) {
		// TODO Auto-generated method stub
			mOffset = listAter.size();
			vListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
			new GetDataTask(false).execute();
	}

	@Override
	public void onLoadMore(View view) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onItemClick(int position, String name,String afid,byte sex) {
		// TODO Auto-generated method stub
		closeEmotions();
		aterName = name;
		aterAfid = afid;
		aterSex = sex;
		String str = getEditTextContent();
		str = str + "@"+name+":";
		vEditTextContent.setText(EmojiParser.getInstance(getActivity().getApplicationContext()).parse(str));
		
		vEditTextContent.requestFocus();
		if(!TextUtils.isEmpty(str)){
			CharSequence text = vEditTextContent.getText();
			if (text instanceof Spannable) {
				Spannable spanText = (Spannable) text;
				Selection.setSelection(spanText, text.length());
			}
			//vEditTextContent.setSelection(str.length());
		}
		CommonUtils.showSoftKeyBoard(vEditTextContent);
		
		vTextTitle.setText(afChatroomEntry.cname);
		chatting_options_layout.setVisibility(View.VISIBLE);
		if(isClickForAll){
			isClickForAll = false;
			setAdapterForAll();
		}
		emojjView.getViewRoot().setVisibility(View.GONE);
		btn_keyboard.setVisibility(View.GONE);
		vImageEmotion.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResendButtonClick(AfMessageInfo afMessageInfo) {
		// TODO Auto-generated method stub
		resendTextOrEmotion(afMessageInfo);
	}

	public void emojj_del() {
		// TODO Auto-generated method stub
		CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
	}

}
