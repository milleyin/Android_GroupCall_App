package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.MyActivityManager;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.SoftkeyboardActivity;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chats.PopMessageActivity;
import com.afmobi.palmchat.ui.activity.chattingroom.ChattingRoomMainAct;
import com.afmobi.palmchat.ui.activity.groupchat.EditGroupActivity;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.ui.activity.groupchat.PopGroupMessageActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileDetailActivity;
import com.afmobi.palmchat.ui.activity.social.ActivitySendBroadcastMessage;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.activity.social.BroadcastFragment;
import com.afmobi.palmchat.ui.activity.social.ShareToBroadcastActivity;
import com.afmobi.palmchat.ui.activity.store.StoreFragmentActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

public class EmojjView {

	public Activity chatting;
	public CustomViewPager mViewPager;
	GridView gridView;
	ArrayList<View> views;
	ArrayList<ImageView> imageViews;
	private ArrayList<HashMap<String, Object>> emotionData;
	private MySimpleAdapter emotionAdapter;
	private ImageView imageView;
//	private int row = 3;
	private int column = 6;
//	private int screen;
	LayoutInflater mInflater;
	private ViewGroup viewRoot;
	private ImageView emojjTypeOne;
	private ImageView emojjChatting_emotion_type;
	private ImageView emojjTypeTwo;
	private LinearLayout pageLayout;
	private LinearLayout viewpageLayout;
	private LinearLayout chatting_emotion_type_layout;
	private View frameEmojjTypeOne, frameEmojjChatting_emotion_type, frameEmojjTypeTwo,framelayout_add;
	FaceFooterView faceFooterView;
	private RelativeLayout r_download,r_progress;
	private View linear_btn,new_store;
	private ProgressBar progress;
	private TextView text_progress;
	private View scroll_parent;
	private RelativeLayout emojj_rl;
	int softkeyboard_H =-1;
	
	public void setSoftkeyboard_H(int softkeyboard_H){
		this.softkeyboard_H = softkeyboard_H;
		if (softkeyboard_H > -1) {
			viewpageLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,softkeyboard_H));
			emojj_rl.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,softkeyboard_H));
		}
	}
    public EmojjView(){}
	public EmojjView(final Activity chatting) {
		this.chatting = chatting;
		imageViews = new ArrayList<ImageView>();
		mInflater = LayoutInflater.from(chatting);
		viewRoot = (ViewGroup) mInflater.inflate(R.layout.chatting_grid_emotion, null);
		pageLayout = (LinearLayout) viewRoot.findViewById(R.id.page_layout);
		chatting_emotion_type_layout = (LinearLayout) viewRoot.findViewById(R.id.chatting_emotion_type_layout);
		emojj_rl = (RelativeLayout) viewRoot.findViewById(R.id.emojj_rl);
		viewpageLayout = (LinearLayout) viewRoot.findViewById(R.id.viewpager_layout);
		
		frameEmojjTypeOne = viewRoot.findViewById(R.id.frame_chatting_emotion_sun);
		frameEmojjChatting_emotion_type = viewRoot.findViewById(R.id.frame_chatting_emotion_type);
		frameEmojjTypeTwo = viewRoot.findViewById(R.id.frame_chatting_emotion_face);
		emojjTypeOne = (ImageView) viewRoot.findViewById(R.id.chatting_emotion_sun);
		emojjChatting_emotion_type = (ImageView) viewRoot.findViewById(R.id.chatting_emotion_type);
		emojjTypeTwo = (ImageView) viewRoot.findViewById(R.id.chatting_emotion_face);
		framelayout_add = viewRoot.findViewById(R.id.framelayout_add);
		mViewPager = (CustomViewPager) viewRoot.findViewById(R.id.viewpager);
		scroll_parent = viewRoot.findViewById(R.id.scroll_parent);
		new_store = viewRoot.findViewById(R.id.new_store);
		// pop message activity can't send custom emoji
		if (chatting instanceof PopMessageActivity || chatting instanceof PopGroupMessageActivity) {
			
			frameEmojjTypeOne = viewRoot.findViewById(R.id.pop_bar1);
			emojjTypeOne = (ImageView) viewRoot.findViewById(R.id.pop_img1);
			frameEmojjChatting_emotion_type = viewRoot.findViewById(R.id.pop_bar2);
			emojjChatting_emotion_type = (ImageView) viewRoot.findViewById(R.id.pop_img2);
			
			frameEmojjTypeTwo.setVisibility(View.GONE);
			framelayout_add.setVisibility(View.GONE);
		    viewRoot.findViewById(R.id.scroll_parent).setVisibility(View.GONE);
		    viewRoot.findViewById(R.id.pop_msg_bar).setVisibility(View.GONE);
		}
		
		if(chatting instanceof ActivitySendBroadcastMessage|| 
				chatting instanceof ShareToBroadcastActivity) {
			viewRoot.findViewById(R.id.scroll_parent).setVisibility(View.GONE);
		}

		frameEmojjTypeOne.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(faceFooterView != null){
					faceFooterView.mSelectItemIndex = -1;
				}
				select(EmojiParser.SUN);//
			}
		});
		frameEmojjChatting_emotion_type.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(EmojiParser.TYPE);//
			}
		});
		frameEmojjTypeTwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(EmojiParser.FACE);//
			}
		});
		framelayout_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.EM_T_STO);
//				MobclickAgent.onEvent(chatting, ReadyConfigXML.EM_T_STO);
//				CommonUtils.backTofragment(chatting, MainLeftFragment.ACTION_TO_STORE);
				chatting.startActivity(new Intent(chatting, StoreFragmentActivity.class));
			}
		});
		if (chatting instanceof ChattingRoomMainAct) {// || chatting instanceof
														// PopupMessageActivity){
			frameEmojjTypeTwo.setVisibility(View.GONE);
		}
		
		r_download = (RelativeLayout) viewRoot.findViewById(R.id.r_download);

		linear_btn = viewRoot.findViewById(R.id.linear_btn_parent);

		viewRoot.findViewById(R.id.linear_btn).setOnClickListener(new DownloadStoreListener());
		
		r_progress = (RelativeLayout) viewRoot.findViewById(R.id.r_progress);
		progress = (ProgressBar)viewRoot.findViewById(R.id.progress);
		text_progress = (TextView)viewRoot.findViewById(R.id.text_progress);
		
	}

	/** 初始化root布局以及部分view（由于在聊天页面延迟加载此类，可能会现在闪退，所以先初始化部分View）
	 * @param chatting
	 */
	public void setView(final Activity chatting){
		this.chatting = chatting;
		mInflater = LayoutInflater.from(chatting);
		viewRoot = (ViewGroup) mInflater.inflate(R.layout.chatting_grid_emotion, null);
		pageLayout = (LinearLayout) viewRoot.findViewById(R.id.page_layout);
		r_download = (RelativeLayout) viewRoot.findViewById(R.id.r_download);
		linear_btn = viewRoot.findViewById(R.id.linear_btn_parent);
		r_progress = (RelativeLayout) viewRoot.findViewById(R.id.r_progress);
		progress = (ProgressBar)viewRoot.findViewById(R.id.progress);
		text_progress = (TextView)viewRoot.findViewById(R.id.text_progress);
		new_store = viewRoot.findViewById(R.id.new_store);
	}

	/**
	 * 延迟加载剩余布局
	 */
	public void initViews(){
		imageViews = new ArrayList<ImageView>();

		chatting_emotion_type_layout = (LinearLayout) viewRoot.findViewById(R.id.chatting_emotion_type_layout);
		emojj_rl = (RelativeLayout) viewRoot.findViewById(R.id.emojj_rl);
		viewpageLayout = (LinearLayout) viewRoot.findViewById(R.id.viewpager_layout);

		frameEmojjTypeOne = viewRoot.findViewById(R.id.frame_chatting_emotion_sun);
		frameEmojjChatting_emotion_type = viewRoot.findViewById(R.id.frame_chatting_emotion_type);
		frameEmojjTypeTwo = viewRoot.findViewById(R.id.frame_chatting_emotion_face);
		emojjTypeOne = (ImageView) viewRoot.findViewById(R.id.chatting_emotion_sun);
		emojjChatting_emotion_type = (ImageView) viewRoot.findViewById(R.id.chatting_emotion_type);
		emojjTypeTwo = (ImageView) viewRoot.findViewById(R.id.chatting_emotion_face);
		framelayout_add = viewRoot.findViewById(R.id.framelayout_add);
		mViewPager = (CustomViewPager) viewRoot.findViewById(R.id.viewpager);
		scroll_parent = viewRoot.findViewById(R.id.scroll_parent);
		// pop message activity can't send custom emoji
		if (chatting instanceof PopMessageActivity || chatting instanceof PopGroupMessageActivity) {

			frameEmojjTypeOne = viewRoot.findViewById(R.id.pop_bar1);
			emojjTypeOne = (ImageView) viewRoot.findViewById(R.id.pop_img1);
			frameEmojjChatting_emotion_type = viewRoot.findViewById(R.id.pop_bar2);
			emojjChatting_emotion_type = (ImageView) viewRoot.findViewById(R.id.pop_img2);

			frameEmojjTypeTwo.setVisibility(View.GONE);
			framelayout_add.setVisibility(View.GONE);
			viewRoot.findViewById(R.id.scroll_parent).setVisibility(View.GONE);
			viewRoot.findViewById(R.id.pop_msg_bar).setVisibility(View.GONE);
		}

		if(chatting instanceof ActivitySendBroadcastMessage||
				chatting instanceof ShareToBroadcastActivity){
			viewRoot.findViewById(R.id.scroll_parent).setVisibility(View.GONE);
		}

		frameEmojjTypeOne.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(faceFooterView != null){
					faceFooterView.mSelectItemIndex = -1;
				}
				select(EmojiParser.SUN);//
			}
		});
		frameEmojjChatting_emotion_type.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(EmojiParser.TYPE);//
			}
		});
		frameEmojjTypeTwo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select(EmojiParser.FACE);//
			}
		});
		framelayout_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.EM_T_STO);
//				MobclickAgent.onEvent(chatting, ReadyConfigXML.EM_T_STO);
//				CommonUtils.backTofragment(chatting, MainLeftFragment.ACTION_TO_STORE);
				chatting.startActivity(new Intent(chatting, StoreFragmentActivity.class));
			}
		});
		if (chatting instanceof ChattingRoomMainAct) {// || chatting instanceof
			frameEmojjTypeTwo.setVisibility(View.GONE);
		}

		viewRoot.findViewById(R.id.linear_btn).setOnClickListener(new DownloadStoreListener());
		if(CacheManager.getInstance().hasNewStoreProInfo()){
			new_store.setVisibility(View.VISIBLE);
		}
		else {
			new_store.setVisibility(View.GONE);
		}
	}

	public RelativeLayout getR_download() {
		return r_download;
	}
	
	public RelativeLayout getR_progress() {
		return r_progress;
	}
	
	public View getFrameEmojjTypeTwo() {
		return frameEmojjTypeTwo;
	}

	public int getRow() {
//		return row;
		return 1;
	}

	void sun() {
		setFaceFooterViewUnSelected();
		emojjTypeTwo.setSelected(false);
		emojjTypeOne.setSelected(true);
		emojjChatting_emotion_type.setSelected(false);
		emojjTypeOne.setImageResource(R.drawable.face_normal);//face_pressed);
//		emojjTypeTwo.setImageResource(R.drawable.sun_normal);
//		emojjChatting_emotion_type.setImageResource(R.drawable.emoticon_typeico03_normal);

		frameEmojjTypeTwo.setSelected(false);
		frameEmojjTypeOne.setSelected(true);
		frameEmojjChatting_emotion_type.setSelected(false);
	}

	private void type() {
		setFaceFooterViewUnSelected();
		emojjTypeTwo.setSelected(false);
		emojjTypeOne.setSelected(false);
		emojjChatting_emotion_type.setSelected(true);
		emojjTypeOne.setImageResource(R.drawable.face_normal);
//		emojjTypeTwo.setImageResource(R.drawable.sun_normal);
//		emojjChatting_emotion_type.setImageResource(R.drawable.emoticon_typeico03_pressed);

		frameEmojjTypeTwo.setSelected(false);
		frameEmojjTypeOne.setSelected(false);
		frameEmojjChatting_emotion_type.setSelected(true);

	}

	void face() {
		setFaceFooterViewUnSelected();
		emojjTypeOne.setSelected(false);
		emojjTypeTwo.setSelected(true);
		emojjChatting_emotion_type.setSelected(false);
//		emojjTypeTwo.setImageResource(R.drawable.sun_pressed);
		emojjTypeOne.setImageResource(R.drawable.face_normal);
//		emojjChatting_emotion_type.setImageResource(R.drawable.emoticon_typeico03_normal);

		frameEmojjTypeOne.setSelected(false);
		frameEmojjTypeTwo.setSelected(true);
		frameEmojjChatting_emotion_type.setSelected(false);

	}

	private void setFaceFooterViewUnSelected() {
		if (faceFooterView != null) {
			faceFooterView.setUnSelectedOrInternalSeletecd(null);
		}
	}

	public void setUnSelected() {
		emojjTypeTwo.setSelected(false);
		emojjTypeOne.setSelected(false);
		emojjChatting_emotion_type.setSelected(false);
		emojjTypeOne.setImageResource(R.drawable.face_normal);
//		emojjTypeTwo.setImageResource(R.drawable.sun_normal);
//		emojjChatting_emotion_type.setImageResource(R.drawable.emoticon_typeico03_normal);

		frameEmojjTypeTwo.setSelected(false);
		frameEmojjTypeOne.setSelected(false);
		frameEmojjChatting_emotion_type.setSelected(false);
	}

	public ViewPager getViewPager() {
		return mViewPager;
	}

	public View getViewRoot() {
		return viewRoot;
	}

	private EditText emojjEdit;

	public EditText getEmojjEdit() {
		return emojjEdit;
	}

	public void setEmojjEdit(EditText emojjEdit) {
		this.emojjEdit = emojjEdit;
	}

	public MyPagerAdapter pagerAdapter;

	public void select(final String type) {
		mViewPager.setVisibility(View.VISIBLE);
		pageLayout.setVisibility(View.GONE);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewpageLayout.getLayoutParams();
		if (EmojiParser.SUN.equals(type)) {
//			row = 3;
			column = 6;
			lp.height = CommonUtils.dip2px(chatting, 150);
			sun();
		} else if (EmojiParser.FACE.equals(type)) {
//			row = 2;
			column = 4;
			lp.height = CommonUtils.dip2px(chatting, 145);
			face();
		} else if (EmojiParser.TYPE.equals(type)) {
//			row = 3;
			column = 6;
			lp.height = CommonUtils.dip2px(chatting, 150);
			type();
		}

		viewpageLayout.setLayoutParams(lp);

		views = new ArrayList<View>();
		imageViews.clear();
		emotionData = EmojiParser.getInstance(chatting).getEmojj().get(type);
		int total = 0;
		if (emotionData != null) {
			total = emotionData.size();
		}

		PalmchatLogUtils.e("EmojjView", "emotion_size->" + total);
//		int oneScreen = row * column;
//		screen = ((total % oneScreen) == 0) ? total / oneScreen : total / oneScreen + 1;
		
//		MyOnPageChangeListener pageListener = new MyOnPageChangeListener(chatting, screen, pageLayout);
//		mViewPager.setOnPageChangeListener(pageListener);
//		for (int i = 0; i < 1; i++) {
//			int end = Math.min((i + 1) * row * column, emotionData.size());
			gridView = createGridView(column);
//			emotionAdapter = new MySimpleAdapter(chatting, emotionData.subList(i * row * column, end), type);
			emotionAdapter = new MySimpleAdapter(chatting, emotionData, type);
			gridView.setAdapter(emotionAdapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
					Map<String, Object> item = (Map<String, Object>) arg0.getAdapter().getItem(position);
					int id = (Integer) item.get("IMAGE");
					String value = (String) item.get("VALUE");
					if (EmojiParser.FACE.equals(type)) {
						// 直接发送
						PalmchatLogUtils.e("EmojjView", value);
						if (chatting instanceof Chatting) {
							Chatting chatting = (Chatting) EmojjView.this.chatting;
							chatting.sendDefaultImage(value, -1, -1, true);
							//chatting.closeEmotions();
						} else if (chatting instanceof GroupChatActivity) {
							GroupChatActivity chatting = (GroupChatActivity) EmojjView.this.chatting;
							chatting.sendDefaultImage(value, -1, -1, true);
							//chatting.closeEmotions();
						} else if (chatting instanceof PopMessageActivity) {
							PopMessageActivity chatting = (PopMessageActivity) EmojjView.this.chatting;
							chatting.sendDefaultImage(value, -1, -1, true);
							//chatting.closeEmotions();
						} else if (chatting instanceof PopGroupMessageActivity) {
							PopGroupMessageActivity chatting = (PopGroupMessageActivity) EmojjView.this.chatting;
							chatting.sendDefaultImage(value, -1, -1, true);
							//chatting.closeEmotions();
						}

						// //横屏
						// if(CommonUtils.getOrientation(chatting) ==
						// chatting.getResources().getConfiguration().ORIENTATION_LANDSCAPE){
						// chatting.closeEmotions();
						// }
					} else {
						if (chatting instanceof Chatting) {
							Chatting chatting = (Chatting) EmojjView.this.chatting;
							chatting.setFace(id, value);
						} else if (chatting instanceof ChattingRoomMainAct) {
							ChattingRoomMainAct chatting = (ChattingRoomMainAct) EmojjView.this.chatting;
							chatting.mainFragment.setFace(id, value);
						} else if (chatting instanceof GroupChatActivity) {
							GroupChatActivity chatting = (GroupChatActivity) EmojjView.this.chatting;
							chatting.setFace(id, value);
						} else if (chatting instanceof PopMessageActivity) {
							PopMessageActivity chatting = (PopMessageActivity) EmojjView.this.chatting;
							chatting.setFace(id, value);
						} else if (chatting instanceof PopGroupMessageActivity) {
							PopGroupMessageActivity chatting = (PopGroupMessageActivity) EmojjView.this.chatting;
							chatting.setFace(id, value);
						} else if(chatting instanceof ActivitySendBroadcastMessage){
							ActivitySendBroadcastMessage chatting = (ActivitySendBroadcastMessage) EmojjView.this.chatting;
							chatting.setFace(id, value);
						}else if(chatting instanceof ShareToBroadcastActivity){
							ShareToBroadcastActivity chatting = (ShareToBroadcastActivity) EmojjView.this.chatting;
							chatting.setFace(id, value);
						} 
						else if(chatting instanceof MainTab){
							MainTab chatting = (MainTab) EmojjView.this.chatting;
							if (chatting != null) {
								chatting.listFragments.get(0);
								BroadcastFragment broadcastFragment = (BroadcastFragment) chatting.getFragment(MainTab.CONTENT_FRAGMENT_BROADCAST);
								if (broadcastFragment == null) {
									broadcastFragment = (BroadcastFragment) MyActivityManager.getScreenManager().getFragment(BroadcastFragment.class.getName());
								}
								if (broadcastFragment != null) {
									broadcastFragment.setFace(id, value);
								}
							}
						} else if(chatting instanceof BroadcastDetailActivity){
							BroadcastDetailActivity chatting = (BroadcastDetailActivity) EmojjView.this.chatting;
							chatting.setFace(id, value);
						} else if(chatting instanceof SoftkeyboardActivity){
							SoftkeyboardActivity chatting = (SoftkeyboardActivity) EmojjView.this.chatting;
							chatting.setFace(id, value);
						}
					}
					if (null != emojjEdit) {
						Drawable drawable;
						int size = ImageUtil.dip2px(chatting, 18);
						if (id != 0 && !CommonUtils.isDeleteIcon(id,emojjEdit)) {
							drawable = chatting.getResources().getDrawable(id);
							drawable.setBounds(0, 0, size, size);
							ImageSpan span = new ImageSpan(drawable);
							SpannableString spannableString = new SpannableString(value);
							spannableString.setSpan(span, 0, value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							int length = spannableString.length() + emojjEdit.length();
							if (length < 100) {
								int index = emojjEdit.getSelectionStart();
								Editable editable = emojjEdit.getText();
								editable.insert(index, spannableString);
							}
						}
					}
				}
			});

			gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
					// TODO Auto-generated method stub
					// int[] location = new int[2];
					// // view.getLocationInWindow(location);
					// view.getLocationOnScreen(location);
					// LogUtils.e("gridView onLongClick",
					// "onLongClick  x  "+location[0]+"  y  "+location[1]);
					// 横屏
					if (CommonUtils.getOrientation(chatting) == chatting.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
						return false;
					}
					Map<String, Object> item = (Map<String, Object>) arg0.getAdapter().getItem(position);
					int id = (Integer) item.get("IMAGE");
					String value = (String) item.get("VALUE");
					String localName = (String) item.get("LOCALNAME");
					if (EmojiParser.FACE.equals(type)) {
						// //直接发送
						// LogUtils.e("EmojjView", value, LogUtils.T_OPEN);
						// if(chatting instanceof Chatting){
						// Chatting chatting = (Chatting)
						// EmojjView.this.chatting;
						// chatting.sendDefaultImage(value, -1, -1, true);
						// chatting.closeEmotions();
						// }else if(chatting instanceof
						// ChattingRoomMessagesActivity){
						// ChattingRoomMessagesActivity chatting =
						// (ChattingRoomMessagesActivity)
						// EmojjView.this.chatting;
						// chatting.sendDefaultImage(value, -1, -1, true);
						// chatting.closeEmotions();
						// }else if(chatting instanceof PopupMessageActivity){
						//
						// }

						// //横屏
						// if(CommonUtils.getOrientation(chatting) ==
						// chatting.getResources().getConfiguration().ORIENTATION_LANDSCAPE){
						// chatting.closeEmotions();
						// }
					} else {
						// if(chatting instanceof Chatting){
						// Chatting chatting = (Chatting)
						// EmojjView.this.chatting;
						// chatting.showDiscribtion(id, localName);
						// }
						// else if(chatting instanceof
						// ChattingRoomMessagesActivity){
						// ChattingRoomMessagesActivity chatting =
						// (ChattingRoomMessagesActivity)
						// EmojjView.this.chatting;
						// chatting.showDiscribtion(id, localName);
						// }else if(chatting instanceof PopupMessageActivity){
						// PopupMessageActivity chatting =
						// (PopupMessageActivity) EmojjView.this.chatting;
						// chatting.showDiscribtion(id, localName);
						// }
					}

					return true;
				}

			});
			
			View v =  LayoutInflater.from(chatting).inflate(R.layout.default_emojj_layout, null);
			LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);
			ImageView emojj_del = (ImageView) v.findViewById(R.id.emojj_del);
			emojj_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					CommonUtils.isDeleteIcon(R.drawable.emojj_del, editText);
					if (chatting instanceof Chatting) {
						Chatting chatting = (Chatting) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if (chatting instanceof ChattingRoomMainAct) {
						ChattingRoomMainAct chatting = (ChattingRoomMainAct) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if (chatting instanceof GroupChatActivity) {
						GroupChatActivity chatting = (GroupChatActivity) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if (chatting instanceof PopMessageActivity) {
						PopMessageActivity chatting = (PopMessageActivity) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if (chatting instanceof PopGroupMessageActivity) {
						PopGroupMessageActivity chatting = (PopGroupMessageActivity) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if(chatting instanceof ActivitySendBroadcastMessage){
						ActivitySendBroadcastMessage chatting = (ActivitySendBroadcastMessage) EmojjView.this.chatting;
						chatting.emojj_del();
					}else if(chatting instanceof ShareToBroadcastActivity){
						ShareToBroadcastActivity chatting = (ShareToBroadcastActivity) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if(chatting instanceof MainTab){
						MainTab chatting = (MainTab) EmojjView.this.chatting;
						if (chatting != null) {
							BroadcastFragment broadcastFragment = chatting.getbroadcastFragment();
							if (broadcastFragment != null) {
								broadcastFragment.emojj_del();
							}
						}
					} else if(chatting instanceof BroadcastDetailActivity){
						BroadcastDetailActivity chatting = (BroadcastDetailActivity) EmojjView.this.chatting;
						chatting.emojj_del();
					} else if(chatting instanceof SoftkeyboardActivity){
						SoftkeyboardActivity chatting = (SoftkeyboardActivity) EmojjView.this.chatting;
						chatting.emojj_del();
					} 
					else if(chatting instanceof EditGroupActivity){
						if(emojjEdit!=null){
						CommonUtils.isDeleteIcon(R.drawable.emojj_del, emojjEdit);
						}
					} else if (chatting instanceof MyProfileDetailActivity) {
						if(emojjEdit!=null){
							CommonUtils.isDeleteIcon(R.drawable.emojj_del, emojjEdit);
							}
					} 
					
				}
			});
			
			layout.addView(gridView);
			views.add(v);
			
			pagerAdapter = new MyPagerAdapter(views, type);
			mViewPager.setAdapter(pagerAdapter);
			pagerAdapter.notifyDataSetChanged();
//		}

//		pagerAdapter.notifyDataSetChanged();

//		pageListener.initImageView(0, imageViews);

	}

	public ArrayList<View> getPageViews() {
		return views;
	}

	public MyPagerAdapter getMyPagerAdapter() {
		return pagerAdapter;
	}

	// public class MyPagerAdapter extends PagerAdapter {
	// ArrayList<View> views;
	// public MyPagerAdapter(ArrayList<View> views,String type) {
	// super();
	// this.views = views;
	// }
	//
	// @Override
	// public int getItemPosition(Object object) {
	// // TODO Auto-generated method stub
	// return POSITION_NONE;
	// }
	//
	// @Override
	// public boolean isViewFromObject(View arg0, Object arg1) {
	// return arg0 == arg1;
	// }
	//
	// @Override
	// public int getCount() {
	// return views.size();
	// }
	//
	// @Override
	// public void destroyItem(View container, int position, Object object) {
	// ((ViewPager)container).removeView(views.get(position));
	// }
	//
	// @Override
	// public Object instantiateItem(View container, int position) {
	// ((ViewPager)container).addView(views.get(position));
	// return views.get(position);
	// }
	// }

	// public class MyOnPageChangeListener implements OnPageChangeListener {
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// for (int i = 0; i < screen; i++) {
	// if (arg0 == i) {
	// ((ImageView)(pageLayout.getChildAt(i))).setImageResource(R.drawable.page_now);
	// } else {
	// ((ImageView)(pageLayout.getChildAt(i))).setImageResource(R.drawable.page);
	// }
	// }
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// }
	// }

	// private void initImageView(int curScreen) {
	// pageLayout.removeAllViews();
	// for (int i = 0; i < screen; i++) {
	// ImageView view = new ImageView(chatting);
	// android.widget.LinearLayout.LayoutParams lp = new
	// android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	// lp.topMargin = CommonUtils.dip2px(chatting, 2);
	// lp.bottomMargin = CommonUtils.dip2px(chatting, 3);
	// lp.leftMargin = CommonUtils.dip2px(chatting, 10);
	// view.setLayoutParams(lp);
	// if (curScreen == i) {
	// view.setImageResource(R.drawable.page_now);
	// } else {
	// view.setImageResource(R.drawable.page);
	// }
	// pageLayout.addView(view, i);
	// imageViews.add(view);
	// }
	// }

	private GridView createGridView(int number) {
		final GridView view = new GridView(chatting);
		view.setNumColumns(number);
		view.setHorizontalSpacing(0);
		view.setVerticalSpacing(ImageUtil.dip2px(chatting, 10));
//		view.setPadding(CommonUtils.dip2px(chatting, 7), 0, CommonUtils.dip2px(chatting, 7), 0);
		// view.setBackgroundResource(R.drawable.profile_photobg);
		view.setSelector(color.transparent);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setGravity(Gravity.CENTER);
		view.setVerticalScrollBarEnabled(false);
		return view;
	}

	// public class MySimpleAdapter extends BaseAdapter {
	// Context context;
	// List<HashMap<String, Object>> dataList;
	// LayoutInflater inflater;
	// String type;
	// ArrayList<ImageFolderInfo> gifImageFolder;
	// public MySimpleAdapter(Context context ,List<HashMap<String, Object>>
	// dataList,String type) {
	// this.context = context;
	// this.dataList = dataList;
	// this.inflater = LayoutInflater.from(context);
	// this.type = type;
	// }
	//
	// public MySimpleAdapter(Context context,ArrayList<ImageFolderInfo>
	// gifImageFolder, String type) {
	// this.context = context;
	// this.gifImageFolder = gifImageFolder;
	// this.inflater = LayoutInflater.from(context);
	// this.type = type;
	//
	// }
	//
	// @Override
	// public int getCount() {
	// return dataList.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return dataList.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// HashMap<String, Object> dataItem = null;
	// if(EmojiParser.GIF.equals(type)){
	//
	// }else{
	// dataItem = dataList.get(position);
	// }
	// ImageView image = null;
	// if(convertView == null){
	// convertView = inflater.inflate(R.layout.chatting_grid_emotion_items,
	// null);
	// image = (ImageView)convertView.findViewById(R.id.chatting_emotion_img);
	// convertView.setTag(image);
	// } else {
	// image = (ImageView)convertView.getTag();
	// }
	//
	// if(EmojiParser.GIF.equals(type)){
	//
	// }else{
	// if (type != null && type.equals(EmojiParser.FACE)) {
	// String value = (String) dataItem.get("VALUE");
	// String [] s = value.split(":");
	// if (s != null && s.length >= 2) {
	// try {
	// image.getLayoutParams().height = CommonUtils.dip2px(context, 48);
	//
	// Bitmap bitmap = ImageUtil.readBitMap(context.getAssets().open("default/"
	// + s[1] + "L.png"));
	// if (null != bitmap) {
	// image.setImageBitmap(bitmap);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// image.setImageBitmap(null);
	// }
	// } else {
	// image.setImageBitmap(null);
	// }
	//
	// } else if(type != null && type.equals(EmojiParser.GIF)){
	//
	// } else{
	// image.setImageResource((Integer) dataItem.get("IMAGE"));
	// }
	// }
	//
	// return convertView;
	// }
	//
	// }

	public LinearLayout getChatting_emotion_type_layout() {
		return chatting_emotion_type_layout;
	}

	public void setChatting_emotion_type_layout(LinearLayout chatting_emotion_type_layout) {
		this.chatting_emotion_type_layout = chatting_emotion_type_layout;
	}

	public LinearLayout getPageLayout() {
		return pageLayout;
	}

	public void setPageLayout(LinearLayout pageLayout) {
		this.pageLayout = pageLayout;
	}

	public ArrayList<ImageView> getImageViews() {
		return imageViews;
	}

	public void setImageViews(ArrayList<ImageView> imageViews) {
		this.imageViews = imageViews;
	}

	public LinearLayout getViewpageLayout() {
		return viewpageLayout;
	}

	public void setViewpageLayout(LinearLayout viewpageLayout) {
		this.viewpageLayout = viewpageLayout;
	}

	public void setFaceFooterView(FaceFooterView faceFooterView) {
		this.faceFooterView = faceFooterView;
		emojjTypeOne.setImageResource(R.drawable.face_normal);//face_pressed);
		frameEmojjTypeOne.setSelected(true);
//		int selectedIndex = CacheManager.getInstance().getSelectedPosition();
//		chatting_emotion_type_layout.getChildAt(selectedIndex).performClick();
	}
	
	public ProgressBar getProgress() {
		return progress;
	}
	public TextView getText_progress() {
		return text_progress;
	}
	public View getLinearBtn() {
		return linear_btn;
	}

	
	
	class DownloadStoreListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.linear_btn:
//				linear_btn.setVisibility(View.GONE);
//				r_progress.setVisibility(View.VISIBLE);
				if(chatting instanceof Chatting){
					String item_id = (String) linear_btn.getTag();
					((Chatting)chatting).downloadFace(item_id);
				} else if (chatting instanceof GroupChatActivity){
					String item_id = (String) linear_btn.getTag();
					((GroupChatActivity)chatting).downloadFace(item_id);
				}
				break;

			default:
				break;
			}
		}
		
	}
	
	/**
	 * 设置表情选择区域的visibility
	 * @param visibility
	 */
	public void setScroll_parent(int visibility){
		scroll_parent.setVisibility(visibility);
	}
	
}
