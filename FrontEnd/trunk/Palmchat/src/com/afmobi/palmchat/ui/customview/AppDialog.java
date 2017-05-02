package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.util.AppUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.AfMessageInfo;
import com.core.cache.CacheManager; 

/**
 * Dialog Util
 * 
 * @author heguiming 2013-11-13
 * 
 */
public class AppDialog extends Dialog {

	public final static int GIVE_SCORE_SURE = 0;
	public final static int GIVE_SCORE_NOT_NOW = 1;
	public final static int GIVE_SCORE_NO_THANKS = 2;
	private EditText editText;
	public AppDialog(Context context) {
		super(context, R.style.Theme_Hold_Dialog_Base);
		setCanceledOnTouchOutside(true);
		// setCancelable(false);
	}

	public AppDialog(Context context, boolean isCanBack) {
		super(context, R.style.Theme_Hold_Dialog_Base);
		setCanceledOnTouchOutside(false);
		setCancelable(isCanBack);
	}

	/**
	 * 设置相关监听器
	 */
	private void setListener(EditText editText) {
		editText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null)
					return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
				return false;
			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(editText != null){
			CommonUtils.closeSoftKeyBoard(editText);
		}
		return super.onTouchEvent(event);
	}

	/**
	 * Select button Dialog
	 * 
	 * @param context
	 * @param title
	 * @param contentAry
	 * @param selectBtnListener
	 */
	public void createSelectBtnDialog(Context context, String title, String[] contentAry, final OnSelectButtonDialogListener selectBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_select_button_layout, null);
		TextView titleText = (TextView) view.findViewById(R.id.title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		LinearLayout selectLayout = (LinearLayout) view.findViewById(R.id.select_btn_layout);

		if (null != contentAry) {
			int length = contentAry.length;
			for (int i = 0; i < length; i++) {
				Button btn = new Button(context);
				btn.setBackgroundResource(R.drawable.set_box_mid_btn_selector);
				btn.setText(contentAry[i]);
				btn.setTextColor(context.getResources().getColor(R.color.black));
				btn.setTextSize(16f);
				btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				btn.setPadding(CommonUtils.dip2px(context, 18), 0, 0, 0);
				selectLayout.addView(btn, new LayoutParams(LayoutParams.FILL_PARENT, CommonUtils.dip2px(context, 48)));
				final int index = i;
				btn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
						if (null != selectBtnListener) {
							selectBtnListener.onCancelButtonClick(index);
						}
					}
				});
			}
		}

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	public void createLogOutDialog(Context context, final OnSelectButtonDialogListener selectBtnListener) {
		setCanceledOnTouchOutside(true);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_logout_layout, null);
		View item1 = view.findViewById(R.id.linear1);
		View item2 = view.findViewById(R.id.linear2);

		item1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (null != selectBtnListener) {
					selectBtnListener.onCancelButtonClick(0);
				}
			}
		});

		item2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (null != selectBtnListener) {
					selectBtnListener.onCancelButtonClick(1);
				}
			}
		});

		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	/**
	 * Confirm Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createConfirmDialog(Context context, String title, String msg, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_confim_title_layout, null);
		TextView titleView = (TextView) view.findViewById(R.id.dialog_title_text);
		titleView.setText(title);
		TextView message = (TextView) view.findViewById(R.id.dialog_message_text);
		message.setText(msg);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));

		setContentView(view);
	}

	/**
	 * Confirm Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createConfirmDialog(Context context, String msg, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_confim_layout, null);
		TextView message = (TextView) view.findViewById(R.id.dialog_message_text);
		message.setText(msg);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));

		setContentView(view);
	}

	/**
	 * add by zhh 创建确认对话框
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param leftStr
	 * @param rightStr
	 * @param confirmBtnListener
	 */
	public void createConfirmDialog(Context context, String title, String msg, String leftStr, String rightStr, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_confim_layout, null);
		TextView message = (TextView) view.findViewById(R.id.dialog_message_text);
		if (msg.equals(context.getString(R.string.sms_code_back_wait))) {
			message.setGravity(Gravity.CENTER);
		}
		message.setText(msg);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		if (!TextUtils.isEmpty(leftStr))
			leftButton.setText(leftStr);
		if (!TextUtils.isEmpty(rightStr))
			rightButton.setText(rightStr);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));

		setContentView(view);
	}

	public void createChangePhoneDialog(Context context, String msg, String cty_code, String new_phone_number, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_change_phone_layout, null);
		TextView dialog_cty_code_text = (TextView) view.findViewById(R.id.dialog_cty_code_text);
		dialog_cty_code_text.setText(cty_code);
		TextView message = (TextView) view.findViewById(R.id.dialog_message_text);
		message.setText(msg);
		TextView tv_new_phone_number = (TextView) view.findViewById(R.id.dialog_content_text);
		tv_new_phone_number.setText(new_phone_number);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));

		setContentView(view);
	}

	/**
	 * zhh 添加左右按钮文字 可改变
	 * 
	 * @param context
	 * @param msg
	 * @param cty_code
	 * @param new_phone_number
	 * @param leftBtn
	 * @param rightBtn
	 * @param confirmBtnListener
	 */
	public void createChangePhoneDialog(Context context, String msg, String cty_code, String new_phone_number, String leftBtn, String rightBtn, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_change_phone_layout, null);
		TextView dialog_cty_code_text = (TextView) view.findViewById(R.id.dialog_cty_code_text);
		dialog_cty_code_text.setText(cty_code);
		TextView message = (TextView) view.findViewById(R.id.dialog_message_text);
		message.setText(msg);
		TextView tv_new_phone_number = (TextView) view.findViewById(R.id.dialog_content_text);
		tv_new_phone_number.setText(new_phone_number);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		leftButton.setText(leftBtn);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);
		rightButton.setText(rightBtn);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));

		setContentView(view);
	}

	public void createOKDialog(Context context, String title, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_ok_layout, null);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_message_text);
		titleText.setText(title);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	public void createSuccessDialog(Context context, String title, String message, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_success_layout, null);
		TextView titleTextView = (TextView) view.findViewById(R.id.dialog_message_title);
		TextView text = (TextView) view.findViewById(R.id.dialog_message_text);
		titleTextView.setText(title);
		text.setText(message);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	/**
	 * clear data
	 * 
	 * @param context
	 * @param title
	 * @param confirmBtnListener
	 */
	public void createClearDialog(Context context, String title, final OnConfirmButtonDialogListener confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_confim_layout, null);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_message_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	@Override
	public void dismiss() {
		if (null != emojjView) {
			emojjView.setEmojjEdit(null);
		}
		super.dismiss();
	}

	/**
	 * Edit Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createEditpEmotionDialog(Activity context, String title, String inputMsg, final OnInputDialogListener inputDialogListener) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_emotion_input_layout, null);
		final CutstomEditTextBanLineKey editInputText = (CutstomEditTextBanLineKey) view.findViewById(R.id.dialog_text_field);
		editText = editInputText;
		editInputText.addTextChangedListener(new LimitTextWatcher(editInputText, ReplaceConstant.MAX_SIGN_SIZE, new Handler(), MessagesUtils.EDIT_TEXT_CHANGE));
		editInputText.setMaxLength(ReplaceConstant.MAX_SIGN_SIZE);
		setListener(editInputText);
		CharSequence text = EmojiParser.getInstance(context).parse(inputMsg, ImageUtil.dip2px(context, 18));
		editInputText.setText(text);
		editInputText.setSelection(editInputText.length());
		TextView titleText = (TextView) view.findViewById(R.id.dialog_title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);
		final ImageView emotionImg = (ImageView) view.findViewById(R.id.image_emotion);
		final ImageView keyboardImg =(ImageView) view.findViewById(R.id.btn_keyboard);
		final LinearLayout emotionLayout = (LinearLayout) view.findViewById(R.id.emotion_layout);
		emojjView = new EmojjView(context);
		emojjView.select(EmojiParser.SUN);
		chattingOptions = createOptionsGridView(context, 2);
		emotionLayout.addView(chattingOptions);
		emotionLayout.addView(emojjView.getViewRoot());
		chattingOptions.setVisibility(View.GONE);
		emojjView.getViewRoot().setVisibility(View.GONE);
		emojjView.getFrameEmojjTypeTwo().setVisibility(View.GONE);
		emojjView.setEmojjEdit(editInputText);
		final String orgContent = editInputText.getText().toString();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		emotionImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				emojjView.getViewRoot().setVisibility(View.VISIBLE);
				chattingOptions.setVisibility(View.GONE);
				emotionLayout.setVisibility(View.VISIBLE);
				CommonUtils.closeSoftKeyBoard(editInputText);
				emotionImg.setVisibility(View.GONE);
				keyboardImg.setVisibility(View.VISIBLE);
			}
		});
		keyboardImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				emojjView.getViewRoot().setVisibility(View.GONE);
				chattingOptions.setVisibility(View.GONE);
				emotionLayout.setVisibility(View.GONE);
				emotionImg.setVisibility(View.VISIBLE);
				keyboardImg.setVisibility(View.GONE);
				CommonUtils.showSoftKeyBoard(editInputText);
			}
		});

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.closeSoftKeyBoard(editInputText);
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onLeftButtonClick();
				}

			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if(!orgContent.equals(editInputText.getText().toString())) {
					if (inputDialogListener != null) {
						inputDialogListener.onRightButtonClick(editInputText.getText().toString());
					}
				}
			}
		});
		editInputText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				emojjView.getViewRoot().setVisibility(View.GONE);
				chattingOptions.setVisibility(View.GONE);
				emotionLayout.setVisibility(View.GONE);
				if(keyboardImg.getVisibility() == View.VISIBLE){
					keyboardImg.setVisibility(View.GONE);
					emotionImg.setVisibility(View.VISIBLE);
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);

		emojjView.getViewRoot().findViewById(R.id.scroll_parent).setVisibility(View.GONE);
	}

	private EmojjView emojjView;
	private GridView chattingOptions;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != emojjView && emojjView.getViewRoot().getVisibility() != View.GONE) {
				emojjView.getViewRoot().setVisibility(View.GONE);
				return false;
			} else if (null != chattingOptions && chattingOptions.getVisibility() != View.GONE) {
				chattingOptions.setVisibility(View.GONE);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private GridView createOptionsGridView(Context context, int number) {
		final GridView view = new GridView(context);
		view.setNumColumns(number);
		view.setHorizontalSpacing(0);
		view.setVerticalSpacing(0);
		view.setSelector(android.R.color.transparent);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		view.setBackgroundResource(R.drawable.tab_bg);
		return view;
	}

	private GridView createGridView(Context context, int number) {
		final GridView view = new GridView(context);
		view.setNumColumns(number);
		view.setHorizontalSpacing(0);
		view.setVerticalSpacing(0);
		view.setPadding(10, 0, 10, 0);
		view.setBackgroundColor(Color.rgb(219, 225, 238));
		view.setSelector(android.R.color.transparent);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setGravity(Gravity.CENTER);
		return view;
	}

	/**
	 * Edit Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createEditDialog(Context context, String title, String inputMsg, int limit, final OnInputDialogListener inputDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_input_layout, null);
		final EditText editInputText = (EditText) view.findViewById(R.id.dialog_text_field);
		editInputText.setText(inputMsg);
		editInputText.setSelection(editInputText.length());
		editInputText.addTextChangedListener(new LimitTextWatcherAndSoftKeyboardEmojiFilter(editInputText, limit));
		// if
		// (title.equals(context.getResources().getString(R.string.set_alias)))
		// {
		// editInputText.setSingleLine(true);
		// editInputText.setMaxLines(1);
		setListener(editInputText);
		// }
		if (title.equals(context.getResources().getString(R.string.password_label))) {
		}
		if (title.equals(context.getResources().getString(R.string.set_phone))) {
			editInputText.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		TextView titleText = (TextView) view.findViewById(R.id.dialog_title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onRightButtonClick(editInputText.getText().toString());
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	/**
	 * Edit Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createEditDialog(Context context, String title, String inputMsg, final OnInputDialogListener inputDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_input_layout, null);
		final EditText editInputText = (EditText) view.findViewById(R.id.dialog_text_field);
		editInputText.setText(inputMsg);
		editInputText.setSelection(editInputText.length());
		editText = editInputText;
//		editInputText.
		// if
		// (title.equals(context.getResources().getString(R.string.set_alias)))
		// {
		// editInputText.setSingleLine(true);
		// editInputText.setMaxLines(1);
		setListener(editInputText);
		// }
		if (title.equals(context.getResources().getString(R.string.password_label))) {
		}
		if (title.equals(context.getResources().getString(R.string.set_phone))) {
			editInputText.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		TextView titleText = (TextView) view.findViewById(R.id.dialog_title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*add by zhh  隐藏输入法*/
				CommonUtils.closeSoftKeyBoard(editInputText);
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*add by zhh  隐藏输入法*/
				CommonUtils.closeSoftKeyBoard(editInputText);
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onRightButtonClick(editInputText.getText().toString());
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	/**
	 * Edit Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createEditPSWDialog(Context context, String title, String inputMsg, final OnInputDialogListener inputDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_input_psw_layout, null);
		final EditText editInputText = (EditText) view.findViewById(R.id.dialog_text_field);
		editInputText.setText(inputMsg);
		// if
		// (title.equals(context.getResources().getString(R.string.set_alias)))
		// {
		// editInputText.setSingleLine(true);
		// editInputText.setMaxLines(1);
		setListener(editInputText);
		// }
		if (title.equals(context.getResources().getString(R.string.password_label))) {
		}
		if (title.equals(context.getResources().getString(R.string.set_phone))) {
			editInputText.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
		TextView titleText = (TextView) view.findViewById(R.id.dialog_title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onRightButtonClick(editInputText.getText().toString());
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	public void createEditNumberDialog(Context context, String title, String inputMsg, final OnInputDialogListener inputDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_input_layout, null);
		final EditText editInputText = (EditText) view.findViewById(R.id.dialog_text_field);
		editInputText.setInputType(InputType.TYPE_CLASS_NUMBER);
		editInputText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(30) });
		editInputText.setText(inputMsg);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onRightButtonClick(editInputText.getText().toString());
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	/**
	 * RadioButton Dialog
	 * 
	 * @param context
	 * @return
	 */
	public void createRadioButtonDialog(Context context, String title, int index, String[] contentAry, final OnRadioButtonDialogListener radioButtonDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_redio_group_layout, null);
		TextView titleText = (TextView) view.findViewById(R.id.title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_gender);
		if (null != contentAry) {
			int length = contentAry.length;

			if (length >= 6) {
				int dip = 320;
				if (ImageUtil.DISPLAYH >= 1280) {
					dip = ImageUtil.px2dip(context, ImageUtil.DISPLAYH - 550);
				} else {
					dip = ImageUtil.px2dip(context, ImageUtil.DISPLAYH - 250);
				}
				ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroller);
				scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dip));
			}
			for (int i = 0; i < length; i++) {
				RadioButton radioBtn = (RadioButton) inflater.inflate(R.layout.dialog_redio_button, null);
				radioBtn.setText(contentAry[i]);
				radioBtn.setId(i);
				radioGroup.addView(radioBtn, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				if (index == i) {
					radioGroup.check(i);
				}
			}
		}

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (radioButtonDialogListener != null) {
					radioButtonDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (radioButtonDialogListener != null) {
					radioButtonDialogListener.onRightButtonClick(radioGroup.getCheckedRadioButtonId());
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}
	
	public void createRadioButtonDialogEx(Context context, String title, int index, Map<Object, String> contentMap, final OnRadioButtonDialogListenerEx radioButtonDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_redio_group_layout, null);
		TextView titleText = (TextView) view.findViewById(R.id.title_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_gender);
		if (null != contentMap) {
			Object keySet[] = contentMap.keySet().toArray();
			int length = keySet.length;

			if (length >= 6) {
				int dip = 320;
				if (ImageUtil.DISPLAYH >= 1280) {
					dip = ImageUtil.px2dip(context, ImageUtil.DISPLAYH - 550);
				} else {
					dip = ImageUtil.px2dip(context, ImageUtil.DISPLAYH - 250);
				}
				ScrollView scrollView = (ScrollView) view.findViewById(R.id.scroller);
				scrollView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip));
			}
			for (int i = 0; i < length; i++) {
				RadioButton radioBtn = (RadioButton) inflater.inflate(R.layout.dialog_redio_button, null);
				radioBtn.setId(i);
				radioBtn.setText(contentMap.get(keySet[i]));
				radioBtn.setTag(keySet[i]);
				radioGroup.addView(radioBtn, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				if (index == i) {
					radioGroup.check(i);
				}
			}
		}

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (radioButtonDialogListener != null) {
					radioButtonDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (radioButtonDialogListener != null&&radioGroup!=null) {
					int rbID = radioGroup.getCheckedRadioButtonId();
					RadioButton rb = (RadioButton) findViewById(rbID);
					if(rb!=null){
						radioButtonDialogListener.onRightButtonClick(radioGroup.getCheckedRadioButtonId(), rb.getTag());
					}
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	public void createResendRandomCodeDialog(Context context, String title, final OnConfirmButtonDialogListener confirmBtnListener, String countryCode, String phoneNum) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_resend_random_code_layout, null);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_message_text);
		titleText.setText(title);
		TextView cty_code = (TextView) view.findViewById(R.id.cty_code);
		cty_code.setText(countryCode);
		EditText edit_phone = (EditText) view.findViewById(R.id.edit_phone);
		edit_phone.setText(phoneNum);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	public View createDownloadApkDialog(final Context context, String title, final String url, final String incUrl,final boolean isIncrease ,final String version, final int layoutId, final OnConfirmButtonDialogListenerForDownLoadAPK confirmBtnListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(layoutId, null);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_message_text);
		titleText.setText(title);
		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick(version, url,incUrl,isIncrease);
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
		return view;
	}

	public View createResend(Context context, final AfMessageInfo afMessageInfo, final OnResendDialogListener onResendDialogListener) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_resend, null);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_message_text);

		titleText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				if (onResendDialogListener != null) {
					onResendDialogListener.onResendButtonClick(afMessageInfo);
				}
			}
		});
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);

		return view;
	}

	public View createGiveScore(Context context, final OnGiveScoreListener onGiveScoreListener) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_give_score, null);
		TextView titleText = (TextView) view.findViewById(R.id.dialog_message_sure);

		titleText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				if (onGiveScoreListener != null) {
					onGiveScoreListener.OnGiveScore(GIVE_SCORE_SURE);
				}
			}
		});
		TextView titleTextNotNow = (TextView) view.findViewById(R.id.dialog_message_not_now);

		titleTextNotNow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				if (onGiveScoreListener != null) {
					onGiveScoreListener.OnGiveScore(GIVE_SCORE_NOT_NOW);
				}
			}
		});
		Button button = (Button) view.findViewById(R.id.confim_button_no_thanks);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (onGiveScoreListener != null) {
					onGiveScoreListener.OnGiveScore(GIVE_SCORE_NO_THANKS);
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);

		return view;
	}

	public void createSendGiftDialog(Context context, String title, String left, int number, final OnInputDialogListener inputDialogListener) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_send_gift, null);
		final EditText editInputText = (EditText) view.findViewById(R.id.dialog_text_field);
		final int gift_flower = CacheManager.getInstance().getMyProfile().dating.gift_flower;
		final TextView titleLeft = (TextView) view.findViewById(R.id.tv_left);
		titleLeft.setText(gift_flower + " left");
		if (number > 0) {
			editInputText.setText(number + "");
			// titleLeft.setText((wealthFlower-number)+"");
		}
		editInputText.setSelection(editInputText.length());
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String number = editInputText.getText().toString();
				if (TextUtils.isEmpty(number)) {
					// editInputText.setText("");
					// titleLeft.setText(wealthFlower+" left");
				} else {
					long num = Long.parseLong(number);
					if (num <= 0) {
						 editInputText.setText("");
						// titleLeft.setText(wealthFlower+" left");
					} else {
						// titleLeft.setText((wealthFlower-num)+" left");
					}
				}
			}
		};
		editInputText.addTextChangedListener(new LimitTextWatcher(editInputText, 10, handler, MessagesUtils.EDIT_TEXT_CHANGE));
		TextView titleText = (TextView) view.findViewById(R.id.dialog_title_text);
		titleText.setText(title);
		TextView titleSubtract = (TextView) view.findViewById(R.id.tv_subtract);
		titleSubtract.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// int wealthFlower =
				// CacheManager.getInstance().getMyProfile().dating.gift_flower;
				String number = editInputText.getText().toString();
				if (!TextUtils.isEmpty(number)) {
					long num = Long.parseLong(number);
					if (num > 1) {
						num--;
						editInputText.setText(num + "");
						editInputText.setSelection(editInputText.length());
					}
					// titleLeft.setText((wealthFlower-num)+" left");
				} else {
					editInputText.setText("1");
					editInputText.setSelection(editInputText.length());
					// titleLeft.setText((wealthFlower-1)+" left");
				}

			}
		});
		TextView titleAdd = (TextView) view.findViewById(R.id.tv_add);
		titleAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// int wealthFlower =
				// CacheManager.getInstance().getMyProfile().dating.gift_flower;
				String number = editInputText.getText().toString();
				if (!TextUtils.isEmpty(number)) {
					long num = Long.parseLong(number);
					num++;
					editInputText.setText(num + "");
					editInputText.setSelection(editInputText.length());
					// titleLeft.setText((wealthFlower-num)+" left");
				} else {
					editInputText.setText("1");
					editInputText.setSelection(editInputText.length());
					// titleLeft.setText((wealthFlower-1)+" left");
				}

			}
		});

		Button leftButton = (Button) view.findViewById(R.id.cancel_button);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (inputDialogListener != null) {
					inputDialogListener.onLeftButtonClick();
				}
			}
		});
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (inputDialogListener != null) {
					inputDialogListener.onRightButtonClick(editInputText.getText().toString());
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}

	public void createMissNigeriaDialog(Context context, int titleIconId, String title, String message, final OnConfirmButtonDialogListener confirmBtnListener, boolean isShowTitleIcon) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_miss_nigeria_layout, null);

		// TextView titleTextView = (TextView)
		// view.findViewById(R.id.dialog_message_title);
		// titleTextView.setText(title);

		ImageView titleIcon = (ImageView) view.findViewById(R.id.title_icon);
		TextView titleText = (TextView) view.findViewById(R.id.title_text);

		if (isShowTitleIcon) {
			titleIcon.setVisibility(View.VISIBLE);
			titleIcon.setBackgroundResource(titleIconId);
		} else {
			titleIcon.setVisibility(View.GONE);
		}

		titleText.setText(title);

		TextView text = (TextView) view.findViewById(R.id.dialog_message_text);
		text.setText(message);
		Button rightButton = (Button) view.findViewById(R.id.confim_button);

		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				if (confirmBtnListener != null) {
					confirmBtnListener.onRightButtonClick();
				}
			}
		});
		view.setMinimumWidth(AppUtils.getWidthFromPercent(context, 0.8f));
		setContentView(view);
	}
	
	 

	public interface OnConfirmButtonDialogListener {
		void onLeftButtonClick();

		void onRightButtonClick();
	}

	public interface OnConfirmButtonDialogListenerForDownLoadAPK {
		void onLeftButtonClick();

		void onRightButtonClick(String version, String url,String incUrl,boolean isIncrease);
	}

	public interface OnInputDialogListener {
		void onLeftButtonClick();

		void onRightButtonClick(String inputStr);
	}

	public interface OnRadioButtonDialogListener {
		void onLeftButtonClick();

		void onRightButtonClick(int selectIndex);
	}

	public interface OnRadioButtonDialogListenerEx {
		void onLeftButtonClick();

		void onRightButtonClick(int selectIndex);
		void onRightButtonClick(int selectIndex, Object tag);
	}
	
	public interface OnSelectButtonDialogListener {
		void onCancelButtonClick(int selectIndex);
	}

	public interface OnResendDialogListener {
		void onResendButtonClick(AfMessageInfo afMessageInfo);
	}

	public interface OnGiveScoreListener {
		void OnGiveScore(int type);
	}

}
