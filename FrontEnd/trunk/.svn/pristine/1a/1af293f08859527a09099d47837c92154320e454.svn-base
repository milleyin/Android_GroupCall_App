package com.afmobi.palmchat.ui.activity.social;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.eventbusmodel.ShareBroadcastResultEvent;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.profile.MyProfileDetailActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.CutstomPopwindowEditText;
import com.afmobi.palmchat.ui.customview.CutstomPopwindowEditText.OnPopItemClickListener;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.greenrobot.event.EventBus;

public class ShareToBroadcastActivity extends BaseActivity implements OnClickListener {
	private CutstomPopwindowEditText mMessageEdit;
	private EmojjView emojjView;
	private LinearLayout chatting_emoji_layout;

	private RelativeLayout chatting_options_layout;
	private ImageView vImageEmotion, vImageKeyboard;
	private Button vImageSend;
	TextView mTitleTxt;
	private TextView mSizeTxt;
	private EditListener editListener;
	// private MyScrollView scrollview;
	KeyboardListenRelativeLayout relativeLayout;
	private boolean isClick = false;
	private ImageView broadcast_pic_imageView;
	private TextView broadcast_name_textView, broadcast_content_textView;
	private ArrayList<String> mDictTagSearch = new ArrayList<String>();
	private AfPalmchat mAfCorePalmchat;
	/** zhh 文本框中输入的内容 用于控制改变文本框颜色后重新赋值不会造成死循环 */
	private String edtCurText = "";
	private int color_tag_normal;
	private LooperThread looperThread;
	private RelativeLayout mSelectOptionsLayout;
	private String mCurTag = "";
	private static final int SHOWPOPWINDOW = 1;
	private static final int HIDEPOPWINDOW = 2;
    /**在这个界面获取到chatting_options 的Y坐标有问题*/
	private int mchatting_optionsYdiff;
	
	private Handler mHandlert = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LooperThread.DEAL_POPWINDOW_TAGSDICT: {
				if (SHOWPOPWINDOW == msg.arg1) {
					mDictTagSearch.clear();
					if (null != msg.obj) {
						try {
							mDictTagSearch = (ArrayList<String>) msg.obj;
							mMessageEdit.updateData(mDictTagSearch);							
							if(View.VISIBLE==chatting_options_layout.getVisibility()){
								//表情输入栏
								int chatting_options=(int)chatting_options_layout.getY()-mchatting_optionsYdiff;
								//pop坐标
								int popY = (int)mMessageEdit.getY()+mMessageEdit.getHeight()+mMessageEdit.getPopYoffset();
								//超出输入栏 大小
								int diff =popY+mMessageEdit.getPopdefaultHeight()-chatting_options;
								if(diff>0){
									int height = mMessageEdit.getPopdefaultHeight()-diff;
									mMessageEdit.setPopHeight(height);
								} else {
									mMessageEdit.setPopDefaultHeight();
								}
							} else{
								mMessageEdit.setPopDefaultHeight();
							}
							mMessageEdit.showInfoTip();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} else if (HIDEPOPWINDOW == msg.arg1) {
					mDictTagSearch.clear();
					mMessageEdit.hideInfoTip();
				}
				break;
			}
			default:
				break;
			}

		};
	};

	@SuppressWarnings("deprecation")
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_share_broadcast);
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		broadcast_pic_imageView = (ImageView) findViewById(R.id.broadcast_pic);
		broadcast_name_textView = (TextView) findViewById(R.id.broadcast_name_textView);
		broadcast_content_textView = (TextView) findViewById(R.id.broadcast_content_textView);
		mTitleTxt = (TextView) findViewById(R.id.title_text);
		mTitleTxt.setText(R.string.broadcast_share);
		/* mSizeTxt = (TextView) findViewById(R.id.txt_size); */
		mSelectOptionsLayout = (RelativeLayout) findViewById(R.id.select_options_layout);
		findViewById(R.id.back_button).setOnClickListener(this);
		vImageSend = (Button) findViewById(R.id.btn_post);
		vImageSend.setVisibility(View.VISIBLE);
		vImageSend.setText(R.string.send_broadcast_post);
		//vImageSend.setBackgroundResource(R.drawable.broadcast_send_selector);
		vImageSend.setOnClickListener(this);
		mMessageEdit = (CutstomPopwindowEditText) findViewById(R.id.message_edits);
		mMessageEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
		mMessageEdit.setOnClickListener(this);
		setListener(mMessageEdit);

		mMessageEdit.setOnPopItemClickListener(new OnPopItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position > mDictTagSearch.size()) {
					return;
				}
				int index = mMessageEdit.getSelectionStart();
				String msgTxt = mMessageEdit.getText().toString();
				String insertText = mDictTagSearch.get(position).toString() + " ";
				if (null != msgTxt && (msgTxt.length() > 0)) {
					String msgPre = msgTxt.substring(0, index);
					int indexpre = msgPre.lastIndexOf("#");
					mMessageEdit.getText().replace( indexpre,index,insertText);
				} else {
					mMessageEdit.setText(insertText);
					mMessageEdit.setSelection(insertText.length());
				}
				mMessageEdit.hideInfoTip();

			}

		});

		mMessageEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				PalmchatLogUtils.println("Chatting  onFocusChange  " + hasFocus);
				if (hasFocus) {
					chatting_emoji_layout.setVisibility(View.GONE);
				}
			}
		});

		mMessageEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!CacheManager.getInstance().getEditTextDelete()) {
					setEdittextListener(s);
					CacheManager.getInstance().setEditTextDelete(false);
				} else {
					CacheManager.getInstance().setEditTextDelete(false);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				String str = s.toString().replace(DefaultValueConstant.CR, DefaultValueConstant.BLANK);
				if (str != null) {
					String string = str;
					editListener.edittext_string = string;
					editListener.edittext_length = s.length();
				} else {
					editListener.edittext_length = 0;
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				String strText = s.toString().replace(DefaultValueConstant.CR, DefaultValueConstant.BLANK);
				if (!edtCurText.equals(strText)) { // 当文本内容发生改变时才去设置tag颜色
					edtCurText = strText;
					parseEdtText(strText);
					doWithPopwidow();
				}
			}
		});
		emojjView = new EmojjView(this);
		emojjView.select(EmojiParser.SUN);
		vImageEmotion = (ImageView) findViewById(R.id.image_emotion);
		vImageKeyboard = (ImageView) findViewById(R.id.image_keyboard);
		vImageEmotion.setOnClickListener(this);
		vImageKeyboard.setOnClickListener(this);
		chatting_options_layout = (RelativeLayout) findViewById(R.id.chatting_options_layout);
		chatting_options_layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
		chatting_emoji_layout.addView(emojjView.getViewRoot());
		relativeLayout = (KeyboardListenRelativeLayout) findViewById(R.id.keyboardRelativeLayout);
		relativeLayout.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

			public void onKeyboardStateChanged(int state) {
				// showOrDisAble();
				switch (state) {
				case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE:// keyboard
					mSelectOptionsLayout.setVisibility(View.INVISIBLE);
					isClick = false;
					break;
				case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW:// keyboard
																		// show
					chatting_emoji_layout.setVisibility(View.GONE);
					isClick = false;
					chatting_options_layout.setVisibility(View.VISIBLE);
					mSelectOptionsLayout.setVisibility(View.VISIBLE);
					// chatting_options_layout.setPadding(0, 0, 0, bottom)
					break;
				default:
					break;
				}
				vImageEmotion.setVisibility(View.VISIBLE);
				vImageKeyboard.setVisibility(View.GONE);
			}

		});
		if (editListener == null) {
			editListener = new EditListener();
		}
		color_tag_normal = getResources().getColor(R.color.log_blue);
	}

	private AfChapterInfo afChapterInfo;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		looperThread = new LooperThread();
		looperThread.start();
		Bundle bundle = getIntent().getExtras();
		mchatting_optionsYdiff = getResources().getDimensionPixelSize(R.dimen.chatting_optionsydiff);
		if (bundle != null) {
			afChapterInfo = (AfChapterInfo) bundle.getSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO);
		}
		if (afChapterInfo != null) {
			if (afChapterInfo.profile_Info != null) {
				broadcast_name_textView.setText(afChapterInfo.profile_Info.name);
			}
			broadcast_content_textView.setText(EmojiParser.getInstance(this).parse(afChapterInfo.content));
			if (afChapterInfo.getType() == Consts.BR_TYPE_IMAGE_TEXT || afChapterInfo.getType() == Consts.BR_TYPE_IMAGE) {
				final ArrayList<AfMFileInfo> al_afFileInfos = afChapterInfo.list_mfile;
				if (!al_afFileInfos.isEmpty()) {
					final AfMFileInfo mfileInfo = al_afFileInfos.get(0);
					String thumb_url = mfileInfo.thumb_url;
					if (!TextUtils.isEmpty(thumb_url)) {
						thumb_url = CacheManager.getInstance().getThumb_url(thumb_url, false, afChapterInfo.pic_rule);
						final String img_url = mfileInfo.url;
						ImageManager.getInstance().DisplayImage(broadcast_pic_imageView, thumb_url, null);
					}
				}
			}else if (afChapterInfo.getType() == Consts.BR_TYPE_VIDEO_TEXT || afChapterInfo.getType() == Consts.BR_TYPE_VIDEO) {
				broadcast_pic_imageView.setImageResource(R.drawable.image_broadcast_sharevideo);

			}
		}
		EventBus.getDefault().register(this);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK ==keyCode ){
			if(mMessageEdit.isPopShow()){
				mMessageEdit.hideInfoTip();
				return true;
			} else {
				return super.onKeyUp(keyCode, event);
			}

		} else{			
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void setEdittextListener(CharSequence s) {
		// TODO Auto-generated method stub
		int length = s.length();
		if (editListener.edittext_length > length) {
			isDeleteSymbol();
		} else if (editListener.edittext_length < length) {

		}

	}

	/**
	 * 处理输入tag联想的问题
	 */
	private void doWithPopwidow() {
		int position = mMessageEdit.getSelectionStart();
		String msg = mMessageEdit.getText().toString().replace(DefaultValueConstant.CR, DefaultValueConstant.BLANK);
		position = mMessageEdit.getSelectionStart();
		String message = msg.substring(0, position);
		int _tagIndex = message.lastIndexOf("#");
		Message msgg = Message.obtain();
		if (_tagIndex >= 0) {
     		String	tagName = message.substring(_tagIndex);
			/* 解析tag */
			Pattern patternTag = Pattern.compile(DefaultValueConstant.SEARCHTAGCONDITION); // 判断是否是tag的表达式
			Matcher matcherTag = patternTag.matcher(tagName);
			if (matcherTag.matches()) {
				if ((null != looperThread) && (null != looperThread.handler)) {
					msgg.what = LooperThread.DEAL_POPWINDOW_TAGSDICT;
					msgg.obj = tagName;
					looperThread.handler.sendMessage(msgg);
				}
			} else {
				mMessageEdit.hideInfoTip();
			}
		} else {
			mMessageEdit.hideInfoTip();
		}
	}

	private boolean isDeleteSymbol() {
		int  selectIndex=mMessageEdit.getSelectionStart();
		if (editListener.edittext_string != null && editListener.edittext_string.length() >= 2&&selectIndex>0&&selectIndex+1<=editListener.edittext_string.toString().length()) {
			String str = editListener.edittext_string.toString().substring(0,selectIndex+1);
			String tempStr = str.substring(str.length() - 1);
			if ("]".equals(tempStr)) {
				int sIndex=str.lastIndexOf("[");
				int eIndex=str.lastIndexOf("]") + 1;
				String ttt = str.substring(sIndex, eIndex);
				if (!TextUtils.isEmpty(ttt) && ttt.length() > 2) {
					boolean isDelete = EmojiParser.getInstance(PalmchatApp.getApplication()).isDefaultEmotion(mMessageEdit,  ttt);
					 return isDelete;
				}
			}
		}

		return false;
	}

	/**
	 * 添加表情
	 * 
	 * @param id
	 * @param data
	 */
	public void setFace(int id, String data) {
		int length = data.length() + mMessageEdit.length();
		if (length <= Constants.BROADCAST_TEXTMAXCOUNT) {
			/* 获取之前文本框中内容 */
//			String mStr = mMessageEdit.getText().toString();
			/* 获取光标所在位置 */
			int _sIndex = mMessageEdit.getSelectionStart();
			/*String text = mStr.substring(0, _sIndex) + data + mStr.substring(_sIndex);
			mMessageEdit.setText(text);
			mMessageEdit.setSelection(_sIndex + data.length());*/
			mMessageEdit.getText().insert(_sIndex, data);
		}
  
//		Drawable drawable;
//		if (id != 0 && !CommonUtils.isDeleteIcon(id, mMessageEdit)) {
//			drawable = getResources().getDrawable(id);
//			drawable.setBounds(0, 0, CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
//			ImageSpan span = new ImageSpan(drawable);
//			SpannableString spannableString = new SpannableString(data);
//			spannableString.setSpan(span, 0, data.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			Log.e("ttt->", spannableString.length() + "->" + mMessageEdit.length());
//			int length = spannableString.length() + mMessageEdit.length();
//			if (length <= Constants.BROADCAST_TEXTMAXCOUNT) {
//				mMessageEdit.append(spannableString);
//			}
//		}
	}

	public void emojj_del() {
		// TODO Auto-generated method stub
		CommonUtils.isDeleteIcon(R.drawable.emojj_del, mMessageEdit);
	}

	/**
	 * 设置禁止換行监听器
	 */
	private void setListener(EditText editText) {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null) {
					return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE);
				}
				return true;
			}
		});
	}

	private void onBack() {
		if (chatting_emoji_layout != null && chatting_emoji_layout.getVisibility() == View.VISIBLE) {
			chatting_emoji_layout.setVisibility(View.GONE);
		} else {
			String message = mMessageEdit.getText().toString();
			if (!TextUtils.isEmpty(message)) {
				AppDialog appDialog = new AppDialog(this);
				appDialog.createConfirmDialog(this, null, getString(R.string.exit_dialog_content), null, getString(R.string.exit), new OnConfirmButtonDialogListener() {
					@Override
					public void onRightButtonClick() {
						finish();
					}

					@Override
					public void onLeftButtonClick() {

					}
				});
				appDialog.show();
			} else {
				finish();
			}
		}
	}

	/**
	 * 发送广播
	 */
	private void sendMessage() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				CommonUtils.closeSoftKeyBoard(mMessageEdit);
			}
		}, 100);
		String message = mMessageEdit.getText().toString();
		String tag_string = null;
		if (!TextUtils.isEmpty(message)) {
			tag_string = ActivitySendBroadcastMessage.getTags(message);// 获取tags
		}
		BroadcastUtil.getInstance().forwardBroadcast(context, afChapterInfo, message, Consts.BC_PURVIEW_ALL, tag_string);

		showProgressDialog(R.string.please_wait);
	}

	/**
	 * 返回分享广播的结果 并显示
	 * 
	 * @param shareBroadcastResultEvent
	 */
	public void onEventMainThread(ShareBroadcastResultEvent shareBroadcastResultEvent) {
		dismissProgressDialog();
		Intent intent = new Intent();
		if (shareBroadcastResultEvent.errorCode == Consts.REQ_CODE_201 || shareBroadcastResultEvent.errorCode == Consts.REQ_CODE_BROADCAST_SHARE_ERROR) {
			intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.the_broadcast_doesnt_exist));

		} else if (shareBroadcastResultEvent.errorCode == Consts.REQ_CODE_UNNETWORK) {// 如果是无网络的
																						// 那就提示网络失败
			intent.putExtra(JsonConstant.KEY_SEND_BROADCAST_DELETE_OR_TAG_ILLEGAL, getResources().getString(R.string.network_unavailable));
		}
		intent.putExtra(JsonConstant.KEY_SEND_IS_SEND_SUCCESS, shareBroadcastResultEvent.isSuccess);
		 intent.putExtra(JsonConstant.KEY_SHARE_OR_SEND_FRIENDS_ERROR_CODE, shareBroadcastResultEvent.errorCode);
		 setResult(RESULT_OK, intent);
		 finish();
	}

	private Handler mHandler = new Handler();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_button:
			boolean hasKeyBoard = relativeLayout.hasKeyboard();
			if (hasKeyBoard) {
				CommonUtils.closeSoftKeyBoard(mMessageEdit);
				return;
			}
			onBack();
			break;
		case R.id.btn_post:
			if (check_completProfile(this)) {
				/*
				 * if (!is_cliek_send) { is_cliek_send = true; sendMessage(); }
				 */sendMessage();
			}
			break;
		case R.id.message_edit:
			chatting_emoji_layout.setVisibility(View.GONE);
			vImageEmotion.setVisibility(View.VISIBLE);
			vImageKeyboard.setVisibility(View.GONE);
			break;
		case R.id.image_keyboard:
			chatting_emoji_layout.setVisibility(View.GONE);
			isClick = false;
			mHandler.postDelayed(new Runnable() {
				public void run() {
					vImageEmotion.setVisibility(View.VISIBLE);
					vImageKeyboard.setVisibility(View.GONE);
				}
			}, 300);
			mMessageEdit.requestFocus();
			CommonUtils.showSoftKeyBoard(mMessageEdit);
			break;
		case R.id.image_emotion:
			if (!isClick) {
				mHandler.postDelayed(new Runnable() {// 这里加delay是为了键盘切换为表情时
														// 能延后弹出
					public void run() {
						mSelectOptionsLayout.setVisibility(View.VISIBLE);
						chatting_emoji_layout.setVisibility(View.VISIBLE);
						vImageEmotion.setVisibility(View.GONE);
						vImageKeyboard.setVisibility(View.VISIBLE);
					}
				}, 300);
			}
			isClick = true;
			CommonUtils.closeSoftKeyBoard(mMessageEdit);
			break;

		}
	}

	/*
	 * @Override public void onScrollChanged(MyScrollView scrollView, int x, int
	 * y, int oldx, int oldy) { // TODO Auto-generated method stub if (y > 0) {
	 * chatting_emoji_layout.setVisibility(View.GONE);
	 * CommonUtils.closeSoftKeyBoard(mMessageEdit); } }
	 */

	/**
	 * 发广播前检查是否有region
	 * 
	 * @param context
	 * @return
	 */
	private boolean check_completProfile(final Context context) {

		// if
		// (!SharePreferenceUtils.getInstance(context).getRegion_is_null_Tips_Key())
		// {
		AfProfileInfo afProfileInfo = CacheManager.getInstance().getMyProfile();
		if (TextUtils.isEmpty(afProfileInfo.region) || afProfileInfo.region.equals(DefaultValueConstant.OTHERS)) {
			// SharePreferenceUtils.getInstance(context).setRegion_is_null_Tips_Key(false);
			AppDialog appDialog = new AppDialog(context);
			String msg = context.getResources().getString(R.string.region_null_tips);
			appDialog.createConfirmDialog(context, msg, new OnConfirmButtonDialogListener() {

				@Override
				public void onRightButtonClick() {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, MyProfileDetailActivity.class);
					Bundle data = new Bundle();
					data.putInt(JsonConstant.KEY_FORM_CITY_BC, MyProfileDetailActivity.FROM_CITY_BC);
					intent.putExtras(data);
					context.startActivity(intent);
				}

				@Override
				public void onLeftButtonClick() {// wxl 2016 0229修改，但不去改Profile时
													// 也可以发
					// TODO Auto-generated method stub
					sendMessage();
				}
			});
			appDialog.show();
		} else {
			// SharePreferenceUtils.getInstance(context).setRegion_is_null_Tips_Key(true);
			return true;
		}
		return false;
		// }

		// return true;
	}

	public class CommaTokenizer implements MultiAutoCompleteTextView.Tokenizer {

		/**
		 *
		 * 在文本框中每輸入任何一個字符都會調用這個方法 ，返回的是每一個單詞輸入的開始位置（從0開始），cursor是最後面的之字符位置。
		 */
		private char charS;
		private String mSTring;

		public CommaTokenizer(char charS) {
			this.charS = charS;
			mSTring = String.valueOf(charS);
		}

		public int findTokenStart(CharSequence text, int cursor) {
			int i = cursor;

			while (i > 0 && text.charAt(i - 1) != charS) {
				i--;
			}
			while (i < cursor && text.charAt(i) == ' ') {
				i++;
			}
			PalmchatLogUtils.i("SENDBORAD", "findTokenStart");
			return i;
		}

		public int findTokenEnd(CharSequence text, int cursor) {
			int i = cursor;
			int len = text.length();

			while (i < len) {
				if (text.charAt(i) == charS) {
					return i;
				} else {
					i++;
				}
			}

			return len;
		}

		public CharSequence terminateToken(CharSequence text) {
			int i = text.length();

			while (i > 0 && text.charAt(i - 1) == charS) {
				i--;
			}

			if (i > 0 && text.charAt(i - 1) == ' ') {
				return text;
			} else {
				if (text instanceof Spanned) {
					SpannableString sp = new SpannableString(text);
					TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
					return sp;
				} else {
					return text;
				}
			}
		}
	}

	/**
	 * add by zhh 设置文本输入框中tag颜色
	 * 
	 * @param strText
	 */
	private void parseEdtText(String strText) {
		int _sIndex = mMessageEdit.getSelectionStart();
//		CharSequence text = 
				EmojiParser.getInstance(context).parseEdtEmojAndTag(context, strText,mMessageEdit,_sIndex);
//		mMessageEdit.setText(text);
//		mMessageEdit.setSelection(_sIndex);
	}

	/**
	 * 
	 */
	private void selectPopTagsFromDict(Object obj) {
		String tagName = "";
		if (null != obj) {
			try {
				tagName = (String) obj;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Message msg = Message.obtain();
		msg.what = LooperThread.DEAL_POPWINDOW_TAGSDICT;

		ArrayList<String> dictTagsResult = new ArrayList<String>();
		AfResponseComm.AfTagGetLangPackageResp adtag = mAfCorePalmchat.AfDbBCLangPackageListSearch(tagName, 0, DefaultValueConstant.SEARCHTAGSFROMDICT);
		if (adtag != null) {
			ArrayList<AfResponseComm.AfTagLangPackageItem> list = adtag.db_list;
			if (null != list) {
				mDictTagSearch.clear();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).tag_name != null && list.get(i).tag_name.startsWith("#")) {
						String abc = list.get(i).tag_name;
						PalmchatLogUtils.i("SENDBORAD", "abc:" + abc);
						dictTagsResult.add(abc);
					}
				}
				msg.obj = dictTagsResult;
				msg.arg1 = SHOWPOPWINDOW;

			}
		} else {
			msg.arg1 = HIDEPOPWINDOW;
		}
		mHandlert.sendMessage(msg);
	}

	class LooperThread extends Thread {
		private static final int SEND_IMAGE_LOOPER = 9999;
		private static final int DEAL_TAGS_DICT = 9998;
		private static final int DEAL_POPWINDOW_TAGSDICT = 9997;
		Handler handler;
		Looper looper;

		public void run() {
			Looper.prepare();
			looper = Looper.myLooper();
			handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case DEAL_POPWINDOW_TAGSDICT: {
						selectPopTagsFromDict(msg.obj);
						break;
					}
					default:
						break;
					}
				}
			};
			Looper.loop();
		}
	}

}
