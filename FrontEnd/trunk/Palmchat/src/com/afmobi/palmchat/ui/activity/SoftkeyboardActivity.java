package com.afmobi.palmchat.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.customview.CutstomEditTextBanLineKey;
import com.afmobi.palmchat.ui.customview.EditListener;
import com.afmobi.palmchat.ui.customview.EmojjView;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText;
import com.afmobi.palmchat.ui.customview.KeyboardListenRelativeLayoutEditText.IOnKeyboardStateChangedListener;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobi.palmchat.util.ToastManager;
import com.core.cache.CacheManager;

public class SoftkeyboardActivity extends BaseActivity implements OnClickListener {
	private EmojjView emojjView;
	CutstomEditTextBanLineKey vEditTextContent;
	private KeyboardListenRelativeLayoutEditText softkey_main;
	private View send_button, vImageEmotion;
	private LinearLayout chatting_emoji_layout;
	private EditListener editListener;
	private boolean emotion_isClose = true;
	/* 是否弹出表情框 */
	private boolean open_emotion = false;
	public View chatting_options_layout, input_box_area;
	private int softkeyboard_H = -1;
	private int txtMark;
	private boolean isSend;
	private ImageView vButtonKeyboard;
	private Handler handler = new Handler();

	@Override
	public void findViews() {
		setContentView(R.layout.softkeyboard);
		if (editListener == null) {
			editListener = new EditListener();
		}

		vEditTextContent = (CutstomEditTextBanLineKey) findViewById(R.id.chatting_message_edit);
		send_button = findViewById(R.id.chatting_operate_one);
		CommonUtils.setListener(vEditTextContent);
		chatting_emoji_layout = (LinearLayout) findViewById(R.id.chatting_emoji_layout);
		send_button = findViewById(R.id.chatting_operate_one);
		input_box_area = findViewById(R.id.input_box_area);
		chatting_options_layout = findViewById(R.id.chatting_options_layout);
		vImageEmotion = findViewById(R.id.image_emotion);
		softkey_main = (KeyboardListenRelativeLayoutEditText) findViewById(R.id.softkey_main);
		vButtonKeyboard = (ImageView) findViewById(R.id.btn_keyboard);
		vButtonKeyboard.setOnClickListener(this);

		// 设置输入框最大字数
		vEditTextContent.setFilters(new InputFilter[] { new InputFilter.LengthFilter(CommonUtils.TEXT_MAX) });
		vEditTextContent.setOnClickListener(this); // 评论框点击事件
		vEditTextContent.setMaxLength(CommonUtils.TEXT_MAX);
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
					ToastManager.getInstance().show(SoftkeyboardActivity.this, R.string.comment_long);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
		vEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					chatting_emoji_layout.setVisibility(View.GONE);
				}
			}
		});

		/* 键盘状态发生改变事件（隐藏/显示） */
		softkey_main.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

			@Override
			public void onKeyboardStateChanged(int state, int mHeight) {
				PalmchatLogUtils.e("onKeyboardStateChanged", "state=" + state + ",mHeight" + mHeight);
				switch (state) {
				case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_HIDE:// keyboardhide
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (emotion_isClose) { // 如果标情框已关闭
								if (!PalmchatApp.getOsInfo().getUa().equals("A0001")) { // 过滤一加
									close(); // 关闭当前页面
								}
							}
						}
					}, 50);
					break;
				case KeyboardListenRelativeLayoutEditText.KEYBOARD_STATE_SHOW:// keyboardshow
					softkeyboard_H = mHeight - input_box_area.getHeight();
					PalmchatLogUtils.e("onKeyboardStateChanged", "softkeyboard_H=" + softkeyboard_H + ",input_box_area.getHeight()=" + input_box_area.getHeight());
					SharePreferenceUtils.getInstance(SoftkeyboardActivity.this).setSoftkey_h(softkeyboard_H);
				default:
					break;
				}
			}
		});

		input_box_area.setVisibility(View.VISIBLE);
		emojjView = new EmojjView(this);
		emojjView.select(EmojiParser.SUN);
		emojjView.setScroll_parent(View.GONE);
		chatting_emoji_layout.addView(emojjView.getViewRoot());

		send_button.setOnClickListener(this);
		vImageEmotion.setOnClickListener(this);
	}

	@Override
	public void init() {
		String hint_name = getIntent().getStringExtra(JsonConstant.KEY_HINT_NAME);
		String txtMsg = getIntent().getStringExtra("txtMessage");
		open_emotion = getIntent().getBooleanExtra(JsonConstant.KEY_OPNE_EMOTION, false);
		if (open_emotion) { // 弹出标情框
			show_emotion();
		} else { // 打开软键盘
			show_softkeyboard();
		}
		PalmchatLogUtils.e("SoftkeyboardActivity", "opne_emotion = " + open_emotion + ",emotion_isClose=" + emotion_isClose);
		if (!TextUtils.isEmpty(hint_name)) {
			vEditTextContent.setHint(hint_name);
		}
		if (!TextUtils.isEmpty(txtMsg)) {
			/*将表情进行转义*/
			CharSequence text = EmojiParser.getInstance(this).parse(txtMsg);
			vEditTextContent.setText(text);
			vEditTextContent.setSelection(txtMsg.length());
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			close();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		input_box_area.setVisibility(View.GONE);
		CommonUtils.closeSoftKeyBoard(vEditTextContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chatting_operate_one: // 发送按钮
			txtMark = 1;
			close();
			break;
		case R.id.chatting_message_edit:// onclick on eidttext
		case R.id.btn_keyboard: // 键盘输入法图标
			show_softkeyboard();
			break;
		case R.id.image_emotion: // 表情图标
			show_emotion();
			break;
		default:
			break;
		}
	}

	
	@Override
	public void onBackPressed() {
		close();
	}
	/**
	 * 显示软键盘
	 */
	private void show_softkeyboard() {
		emotion_isClose = true;
		open_emotion = false;
		vEditTextContent.requestFocus();
		vEditTextContent.setFocusable(true);
		vEditTextContent.requestFocusFromTouch();
		chatting_emoji_layout.setVisibility(View.GONE);
		input_box_area.setVisibility(View.VISIBLE);
		vButtonKeyboard.setVisibility(View.GONE);
		vImageEmotion.setVisibility(View.VISIBLE);
		CommonUtils.showSoftKeyBoard(vEditTextContent);
	}

	/**
	 * 显示表情框
	 */
	private void show_emotion() {
		emotion_isClose = false;
		input_box_area.setVisibility(View.VISIBLE);
		CommonUtils.closeSoftKeyBoard(vEditTextContent);
		vButtonKeyboard.setVisibility(View.VISIBLE);
		vImageEmotion.setVisibility(View.GONE);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				chatting_emoji_layout.setVisibility(View.VISIBLE);
			}
		}, 100);
	}

	private void setEdittextListener(CharSequence s) {
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

	/**
	 * 输入表情
	 * 
	 * @param id
	 * @param data
	 */
	public void setFace(int id, String data) {
		Drawable drawable;
		boolean isActivie = true;
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {// 17以上的版本才支持isDestroyed方法
			isActivie = !isDestroyed();
		} else {
			isActivie = !isFinishing();
		}
		if (isActivie) {
			if (id != 0 && !CommonUtils.isDeleteIcon(id, vEditTextContent)) {
				drawable = getResources().getDrawable(id);
				drawable.setBounds(0, 0, CommonUtils.emoji_w_h(context), CommonUtils.emoji_w_h(context));
				ImageSpan span = new ImageSpan(drawable);
				SpannableString spannableString = new SpannableString(data);
				spannableString.setSpan(span, 0, data.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				Log.e("ttt->", spannableString.length() + "->" + vEditTextContent.length());
				int length = spannableString.length() + vEditTextContent.length();
				if (length <= CommonUtils.TEXT_MAX) {
					int index = vEditTextContent.getSelectionStart();
					Editable editable = vEditTextContent.getText();
					editable.insert(index, spannableString);
				} else {
					if (!ToastManager.getInstance().isShowing()) {
						ToastManager.getInstance().show(this, R.string.comment_long);
					}
				}
			}
		}
	}

	/**
	 * 关闭当前页面
	 */
	private void close() {
		PalmchatLogUtils.e("SoftkeyboardActivity", "--colse--");
		input_box_area.setVisibility(View.GONE);
		if (txtMark != 1) { // 未点击发送按钮
			String msg = vEditTextContent.getText().toString();
			Intent data = new Intent();
			data.putExtra(JsonConstant.KEY_COMMENT_COUNTENT, msg);
			data.putExtra("isSend", "2");
			setResult(Constants.COMMENT_FLAG, data);
		} else if (txtMark == 1) { // 点击发送按钮
			String msg = vEditTextContent.getText().toString();
			if(!TextUtils.isEmpty(msg.trim())) {
				Intent data = new Intent();
				data.putExtra(JsonConstant.KEY_COMMENT_COUNTENT, msg);
				data.putExtra("isSend", "1");
				setResult(Constants.COMMENT_FLAG, data);
			}
		}
		CommonUtils.closeSoftKeyBoard(vEditTextContent);
		SoftkeyboardActivity.this.finish();

	}

	/**
	 * 删除表情
	 */
	public void emojj_del() {
		if (vEditTextContent != null) {
			CommonUtils.isDeleteIcon(R.drawable.emojj_del, vEditTextContent);
		}
	}

}
