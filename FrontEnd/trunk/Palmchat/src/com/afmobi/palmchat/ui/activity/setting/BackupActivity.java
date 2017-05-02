package com.afmobi.palmchat.ui.activity.setting;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.setting.Query.OnQueryComplete;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfPalmchat;
import com.core.AfPbInfo;
import com.core.Consts;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

/**
 * 通讯录备份
 *
 */
public class BackupActivity extends BaseActivity implements OnClickListener,
		AfHttpResultListener,   OnQueryComplete {
	
	private static final String TAG = BackupActivity.class.getCanonicalName();
	/**备份view*/
	private View mBackupView;
	/**恢复按钮*/
	private View mRecoveryView;
	/**中间显示的进度条*/
	ProgressDialog mProgress;
	/**返回键*/
	private View mBackView;
	/**本地化库接口类*/
	private AfPalmchat mAfCorePalmchat;
	/**通讯录查询*/
	private Query queryHandler;
	/**从手机获取的通讯录*/
	private List<Contacts> phoneBooklist;
	/**从服务器获取的通讯录列表*/
	private List<Contacts> newList;
	/**从手机获取的通讯录人名*/
	private String pb_name[];
	/**从手机获取的通讯录电话号码*/
	private String pb_phone[];
	/**从服务器获取的通讯录人名*/
	private String  r_name[];
	/**从服务器获取的通讯录电话号码*/
	private String r_phone[];
	/**在备份*/
	private boolean isBackUp;
	/**在恢复*/
	private boolean isRecovery;
	
	private final int ACTION_BACK_UP = 0;
	private final int ACTION_RESTORE = 1;
	private final int ACTION_RESTORE_SUCCESS = 2;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_RESTORE_SUCCESS:
				dismissProgressDialog();
				ToastManager.getInstance().show(BackupActivity.this, R.string.restore_succeeded);
				break;
			default:
				break;
			}
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void findViews() {
		getContacts();
		mAfCorePalmchat = ((PalmchatApp) getApplication()).mAfCorePalmchat;
		setContentView(R.layout.phone_asisstant);
		((TextView) findViewById(R.id.title_text)).setText(R.string.phonebook_backup);
		
		mBackupView = findViewById(R.id.backup);
		mRecoveryView = findViewById(R.id.recovery);
		mBackupView.setOnClickListener(this);
		mRecoveryView.setOnClickListener(this);

		mBackView = this.findViewById(R.id.back_button);
		mBackView.setOnClickListener(this);
		
		mProgress = new ProgressDialog(this);
		mProgress.setMax(100);
		mProgress.setMessage(getString(R.string.restore_address_book));
		mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}

	@Override
	public void init() {

	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			//备份view点击
			case R.id.backup: {
				doBackup();
			}
			break;
			//恢复view点击
			case R.id.recovery: {
				doRecovery();
			}
			break;
			//返回按钮
			case R.id.back_button: {
				finish();
			}
			break;
			default:
				break;
		}
	}

	
	/**
	 * 处理备份事件
	 */
	private void doBackup() {
		AppDialog appDialog = new AppDialog(this);
		appDialog.createConfirmDialog(this, getString(R.string.back_up_address_book), new OnConfirmButtonDialogListener() {
			
			@Override
			public void onRightButtonClick() {
				isBackUp = true;
				getContacts();
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.U_PB_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.U_PB_SUCC);
			}
			
			@Override
			public void onLeftButtonClick() {
				
			}
		});
		appDialog.show();
	}

	/***
	 * 处理恢复事件
	 */
	private void doRecovery() {
		AppDialog appDialog = new AppDialog(this);
		appDialog.createConfirmDialog(this, getString(R.string.sure_to_restore), new OnConfirmButtonDialogListener() {
			
			@Override
			public void onRightButtonClick() {
				getContacts();
				isRecovery = true;
				
				// heguiming 2013-12-04
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.D_PB_SUCC);
//				MobclickAgent.onEvent(context, ReadyConfigXML.D_PB_SUCC);
			}
			
			@Override
			public void onLeftButtonClick() {
				
			}
		});
		appDialog.show();
	}
	
	/**
	 * 请求码成功时处理的事件
	 * @param flag 
	 * @param userdata
	 * @param result
	 */
	private void doReqSuccess(int flag,int userdata,Object result) {
		if (Consts.REQ_PHONEBOOK_BACKUP == flag) {
			switch(userdata) {
				//返回
				case  ACTION_BACK_UP: {
					isBackUp = false;
					isRecovery = false;
					dismissProgressDialog();
					// 备份
					ToastManager.getInstance().show(this,
							R.string.bak_succeeded);
				}
				break;
				//恢复
				case ACTION_RESTORE: {
					isBackUp = false;
					isRecovery = false;
					AfPbInfo info = (AfPbInfo) result;
					
					if (info != null) {
						r_name = info.name;
						r_phone = info.phone;
						
						newList = new ArrayList<Contacts>();
						for(int i=0; i< r_name.length;i++) {
							Contacts contact =new Contacts();
							String name = r_name[i];
							String phone = r_phone[i];
							contact.name = name;
							contact.number = phone;
							newList.add(contact);
						}
						
						Query q = new Query(null, BackupActivity.this);
						q.saveOrUpdate(newList, phoneBooklist, handler);
					} else {
						dismissProgressDialog();
						ToastManager.getInstance().show(this,R.string.upload_phonebook);
					}
				}
				break;
				default:
					break;
			}
		}
	}
	
	@Override
	public void  AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data){
		int userdata = (Integer) user_data;
		if (code == Consts.REQ_CODE_SUCCESS) {
			doReqSuccess(flag,userdata,result);
		} else {
			dismissProgressDialog();
			Consts.getInstance().showToast(context, code, flag,http_code);
		}
	}



	/**
	 * 获取手机通讯录
	 */
	private void getContacts() {
		queryHandler = new Query(getContentResolver(), this);
		queryHandler.setQueryComplete(this);
		queryHandler.query();
	}


	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
	

	@Override
	public void onComplete(Cursor c) {
		phoneBooklist = queryHandler.getContacts(this, c);
		PalmchatLogUtils.e(TAG, "------------------onComplete:" + (null != phoneBooklist ? phoneBooklist.size() : null));
		if (isBackUp) {
			if (null != phoneBooklist && !phoneBooklist.isEmpty()) {
				showProgressDialog(getString(R.string.loading));
				pb_name = new String[phoneBooklist.size()];
				pb_phone = new String[phoneBooklist.size()];
				for (int i = 0; i < phoneBooklist.size(); i++) {
					Contacts contacts = phoneBooklist.get(i);
					pb_name[i] = contacts.name;
					pb_phone[i] = contacts.number;
				}
				if (pb_name != null && pb_phone != null && pb_name.length > 0
						&& pb_phone.length > 0) {
					PalmchatLogUtils.e(TAG, "------------------pb_name.length:" + pb_name.length);
					PalmchatLogUtils.e(TAG, "------------------pb_phone.length:" + pb_phone.length);
					mAfCorePalmchat.AfHttpPhonebookBackup(pb_name, pb_phone,
							Consts.HTTP_ACTION_A, 0, pb_phone.length, ACTION_BACK_UP,
							BackupActivity.this);
				}
			} else {
				ToastManager.getInstance().show(BackupActivity.this,getString(R.string.address_book_empty));
			}
		}
		if (isRecovery) {
			showProgressDialog(getString(R.string.loading));
			mAfCorePalmchat.AfHttpPhonebookBackup(null,
					null, Consts.HTTP_ACTION_B, 0, 10000,
					ACTION_RESTORE, BackupActivity.this);
		}
	}
}
