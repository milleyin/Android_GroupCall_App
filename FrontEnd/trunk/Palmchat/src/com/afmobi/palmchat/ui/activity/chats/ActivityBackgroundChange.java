package com.afmobi.palmchat.ui.activity.chats;

import java.util.ArrayList;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.ui.activity.chats.adapter.ActivityBackgroundChangeAdapter;
import com.afmobi.palmchat.ui.activity.chats.model.BackgroundModel;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.core.cache.CacheManager;

public class ActivityBackgroundChange extends BaseActivity{

	private GridView mGridView;
	private ActivityBackgroundChangeAdapter backgroundChangeAdapter;
	private ArrayList<BackgroundModel> list = new ArrayList<BackgroundModel>();
	
	private ImageView rightView;
	private String selected_background;
	private     int[] background_thumb_res_id ;
	/*= new int[]{
		R.color.bg_chatting,
		R.drawable.img_chatbg_icon_00_256, R.drawable.img_chatbg_icon_01_256, R.drawable.img_chatbg_icon_02_256,
		R.drawable.img_chatbg_icon_03_256, R.drawable.img_chatbg_icon_04_256, R.drawable.img_chatbg_icon_05_256,
		R.drawable.img_chatbg_icon_06_256, R.drawable.img_chatbg_icon_07_256
	};
	*/
	
/*	private static final int[] background_large_res_id = new int[]{
		R.color.bg_chatting,
		R.drawable.img_chatbg_00_720, R.drawable.img_chatbg_01_720, R.drawable.img_chatbg_02_720,
		R.drawable.img_chatbg_03_720, R.drawable.img_chatbg_04_720, R.drawable.img_chatbg_05_720,
		R.drawable.img_chatbg_06_720, R.drawable.img_chatbg_07_720
	};*/
	private boolean isChangeProfileBackGround;
	@Override
	public void findViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_chatting_background);
		final String from_afid = getIntent().getStringExtra(JsonConstant.KEY_FROM_UUID);
		final String myAfid = CacheManager.getInstance().getMyProfile().afId;
		 
		final String fromPage= getIntent().getStringExtra(JsonConstant.KEY_FROM);
		 if("profile".equals(fromPage)){//如果是来自Profile的
			 isChangeProfileBackGround=true;
//			 background_thumb_res_id = new int[]{
//						R.drawable.pic_store01, R.drawable.pic_store02, R.drawable.pic_store03,
//						R.drawable.pic_store04, R.drawable.pic_store05, R.drawable.pic_store06,
//						R.drawable.pic_store07, R.drawable.pic_store08
//					};
			 selected_background = SharePreferenceUtils.getInstance(context).getProfileAblum( );
	      }else{
	    	  background_thumb_res_id = new int[]{
	    				R.color.bg_chatting,
	    				R.drawable.img_chatbg_icon_00_256, R.drawable.img_chatbg_icon_01_256, R.drawable.img_chatbg_icon_02_256,
	    				R.drawable.img_chatbg_icon_03_256, R.drawable.img_chatbg_icon_04_256, R.drawable.img_chatbg_icon_05_256,
	    				R.drawable.img_chatbg_icon_06_256, R.drawable.img_chatbg_icon_07_256
	    			};
	    	  selected_background = SharePreferenceUtils.getInstance(this).getBackgroundForAfid(myAfid, from_afid);
	  	 }
		
		mGridView = (GridView) findViewById(R.id.gridview);
		rightView = (ImageView) findViewById(R.id.op2);
		rightView.setVisibility(View.VISIBLE);
		rightView.setBackgroundResource(R.drawable.navigation_yes_btn_selector);
		rightView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(backgroundChangeAdapter.selected_background_model != null){
					BackgroundModel backgroundModel = backgroundChangeAdapter.selected_background_model;
					if(isChangeProfileBackGround){ 
						setResult(backgroundModel.res_id_little);
					}else{
						SharePreferenceUtils.getInstance(ActivityBackgroundChange.this).setBackgroundForAfid(myAfid, from_afid, backgroundModel.background_name);
					}
					finish();
				}
				
			}
		});
		initData();
		setAdapter();
		
		TextView title = (TextView) findViewById(R.id.title_text);
		title.setText(R.string.background);
		
		findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < background_thumb_res_id.length; i++) {
			BackgroundModel backgroundModel = new BackgroundModel();
			if(isChangeProfileBackGround){
				if(i<9){
					backgroundModel.background_name = "0" + (i+1);
				}else{
					backgroundModel.background_name =String.valueOf(i+1);
				}
			}else{
				backgroundModel.background_name = "background" + i;
			}
			backgroundModel.res_id_little = background_thumb_res_id[i]; 
			if(backgroundModel.background_name.equals(selected_background)){// set selected background
				setAdapter();
				backgroundChangeAdapter.hashMap.put(backgroundModel.background_name, backgroundModel);
			}
			list.add(backgroundModel);
		}
	}
	

	private void setAdapter() {
		// TODO Auto-generated method stub
		if(backgroundChangeAdapter == null){
			backgroundChangeAdapter = new ActivityBackgroundChangeAdapter(this, list);
			mGridView.setAdapter(backgroundChangeAdapter);
		}else{
			backgroundChangeAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
