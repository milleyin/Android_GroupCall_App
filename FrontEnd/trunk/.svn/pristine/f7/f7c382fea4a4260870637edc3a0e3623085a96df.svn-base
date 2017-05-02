package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.gif.GifImageView;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chats.Chatting;
import com.afmobi.palmchat.ui.activity.chattingroom.model.ImageFolderInfo;
import com.afmobi.palmchat.ui.activity.groupchat.GroupChatActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.SoundManager;
import com.core.Consts;
import com.core.cache.CacheManager;

public class FaceFooterView {
	
	private LayoutInflater mInflater;
	private Context context;
	private EmojjView emojjView;
	int screen = 0;
	GridView gridView;
	private MySimpleAdapter emotionAdapter;
	public MyPagerAdapter pagerAdapter;
	private ArrayList<View> views;
	private final static int ICON_NOT_SELETED = 0;
	private final static int ICON_SELETED = 1;
	private ArrayList<FrameLayout> iconViews;
	public int mSelectItemIndex = -1;
	public FaceFooterView(){
		
	}
	
	public FaceFooterView(Context context,EmojjView emojjView){
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.emojjView = emojjView;
		iconViews = new ArrayList<FrameLayout>();
	}
	
	public void addGifFooter() {
//		CacheManager.getInstance().getGifImageUtilInstance().getListFolders();
		ArrayList<ArrayList<ImageFolderInfo>> listFolders = CacheManager.getInstance().getGifImageUtilInstance().getListFolders();
		int listSize = listFolders.size();
		if(iconViews != null) {
			iconViews.clear();
		}
//		item_gif_face_foot_tab
		if(listSize >0 && listSize - 1 <= mSelectItemIndex){
			mSelectItemIndex = listSize - 1;
		}
		else if (mSelectItemIndex == -1 || listSize == 0) {
			if(emojjView != null) {
				mSelectItemIndex = -1;
				emojjView.select(EmojiParser.SUN);
			}
		}
		for (int i = 0; i < listSize; i++) {
			final FrameLayout view = (FrameLayout) mInflater.inflate(R.layout.item_gif_face_foot_tab, null);
			ImageView gif_option = (ImageView) view.findViewById(R.id.gif_one);
			gif_option.setTag(i+"");
//			LinearLayout l_unread_msg = (LinearLayout) view.findViewById(R.id.l_unread_msg);
			ArrayList<ImageFolderInfo> arrayListImage = listFolders.get(i);
			view.setTag(arrayListImage);
			setSelectedOrNot(arrayListImage, gif_option, ICON_NOT_SELETED);
			iconViews.add(view);
			final int mIndex = i;
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ArrayList<ImageFolderInfo> gifImageFolder = (ArrayList<ImageFolderInfo>) view.getTag();
					setUnSelectedOrInternalSeletecd(view);//setSelectedOrNot(gifImageFolder,(ImageView) view.findViewById(R.id.gif_one),ICON_SELETED);
					view.setSelected(true);

					String item_id = getItemIdFromView(gifImageFolder);
					CacheManager.getInstance().setSelectedEmotionItemId(item_id);
					mSelectItemIndex = mIndex;
					emojjView.getLinearBtn().setTag(item_id);

					boolean to_update = CacheManager.getInstance().getItemid_update().containsKey(item_id);

					PalmchatLogUtils.println("selected item_id:" + item_id + "  to_update:" + to_update);

					if (context instanceof Chatting && to_update) {
						((Chatting) context).setShowOrNot(item_id);
					} else {
						emojjView.getViewPager().setVisibility(View.VISIBLE);
						emojjView.getPageLayout().setVisibility(View.VISIBLE);
						addViewToFooter(gifImageFolder);
					}

//					setShowOrNot();

				}

			});
			if(i == mSelectItemIndex){
				view.performClick();
			}
			emojjView.getChatting_emotion_type_layout().addView(view);
		}
	}

	void setSelectedOrNot(ArrayList<ImageFolderInfo> arrayListImage,ImageView imageview,int type){
		String iconPath = arrayListImage.get(2).dir;
//		if(!TextUtils.isEmpty(iconPath)){//xd Phone maybe download hd and xd 
//			iconPath = iconPath.substring(0, iconPath.lastIndexOf(CacheManager.getInstance().getScreenString())+2);
//		}
		if(ICON_NOT_SELETED == type){
			iconPath = iconPath+"/ic.pal";
		}else if(ICON_SELETED == type){
			iconPath = iconPath+"/ic_sel.pal";
			int selected_position = Integer.parseInt(imageview.getTag().toString());
			CacheManager.getInstance().setSelectedPosition(selected_position+1);
			PalmchatLogUtils.println("selected  position:"+selected_position);
		}
		PalmchatLogUtils.println("addGifFooter iconPath:"+iconPath);
		imageview.setImageBitmap(BitmapFactory.decodeFile(iconPath));
		
		
		emojjView.setUnSelected();
		
	}
	
	public void setUnSelectedOrInternalSeletecd(FrameLayout frameLayout){
		int size = iconViews.size();
		for (int i = 0; i < size; i++) {
			FrameLayout f = iconViews.get(i);
			f.setSelected(false);
			setSelectedOrNot((ArrayList<ImageFolderInfo>) f.getTag(), (ImageView) f.findViewById(R.id.gif_one), ICON_NOT_SELETED);
		}
		if(frameLayout != null){
			setSelectedOrNot((ArrayList<ImageFolderInfo>) frameLayout.getTag(), (ImageView) frameLayout.findViewById(R.id.gif_one), ICON_SELETED);
		}
	}
	
	void addViewToFooter(ArrayList<ImageFolderInfo> gifImageFolder){
		views = new ArrayList<View>();
		int position = 0;
		int row = 2;
		int column = 4;
		int total = 0;
		if(gifImageFolder != null && gifImageFolder.size() == 3){
			ImageFolderInfo imageInfo = gifImageFolder.get(position);
			total = imageInfo.filePathesMap.size();
		}else{
			PalmchatLogUtils.println("addViewToFooter return");
			return;
		}
		/*--5.0改成竖向滚动
		int oneScreen = row * column;
		screen = ((total % oneScreen) == 0) ? total / oneScreen : total / oneScreen + 1;
		 */
		/*--5.0改成竖向滚动
		MyOnPageChangeListener pageListener = new MyOnPageChangeListener(context,screen,emojjView.getPageLayout()); 
		emojjView.getViewPager().setOnPageChangeListener(pageListener);
		for (int i = 0; i < screen ; i++) {
		 */
			gridView = createGridView(column);
			ImageFolderInfo imageInfo = gifImageFolder.get(position);
//			if(!imageInfo.dir.contains("source")){
//				imageInfo = gifImageFolder.get(position+1);
//			}
			if(!imageInfo.dir.contains("icon")){
				imageInfo = gifImageFolder.get(position+1);
			}
//			total = imageInfo.filePathes.size();  
			/*--5.0改成竖向滚动 
			int end = Math.min((i + 1) * row * column, total);//emotionData.subList(i * row * column, end)
			emotionAdapter = new MySimpleAdapter(context, imageInfo.filePathesMap.subList(i * row * column, end),EmojiParser.GIF);
			 */
			emotionAdapter = new MySimpleAdapter(context, imageInfo.filePathesMap,EmojiParser.GIF);
			gridView.setAdapter(emotionAdapter);
			gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Map<String, Object> item = (Map<String, Object>)arg0.getAdapter().getItem(position);
					String id = (String) item.get("IMAGE");
					String value = (String) item.get("VALUE");
					PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_SENDEMOTION);
					PalmchatLogUtils.println("addViewToFooter onItemClick id:"+id+" value:"+value);
					if(context instanceof Chatting){
						Chatting chatting = (Chatting) context;
						chatting.sendGifImage(value, -1, -1, true);
						//chatting.closeEmotions();
					}else if(context instanceof GroupChatActivity){
						GroupChatActivity chatting = (GroupChatActivity) context;
						chatting.sendGifImage(value);
						//chatting.closeEmotions();
						
					}
				}
				
			});
			gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
//					Map<String, Object> item = (Map<String, Object>)arg0.getAdapter().getItem(position);
//					String id = (String) item.get("IMAGE");
//					String value = (String) item.get("VALUE");
//					if (!TextUtils.isEmpty(value)) {
//						 int[] location = new int[2];
//                         view.getLocationOnScreen(location);
//						 showPopupWindowDel(context, view, location[0], location[1], value);
//					}
					return true;					
				}
				
				
			});
			gridView.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							break;
						case MotionEvent.ACTION_MOVE:
							break;
						case MotionEvent.ACTION_UP:
							if(pop != null){
								pop.dismiss();
							}
							break;
						
					}
					return false;
				}
			});
			views.add(gridView);
		pagerAdapter = new MyPagerAdapter(views,EmojiParser.GIF);
		emojjView.getViewPager().setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();
//		} 
//		pagerAdapter.notifyDataSetChanged();
//		pageListener.initImageView(0,emojjView.getImageViews());
	}
	
	private GridView createGridView(int number) {
		final GridView view = new GridView(context);
		view.setNumColumns(number);
		view.setHorizontalSpacing(CommonUtils.dip2px(PalmchatApp.getApplication(), 20));
		view.setVerticalSpacing(CommonUtils.dip2px(PalmchatApp.getApplication(), 6));
//		view.setPadding(CommonUtils.dip2px(PalmchatApp.getApplication(), 22), 0, CommonUtils.dip2px(PalmchatApp.getApplication(), 22), 0);
		view.setSelector(android.R.color.transparent);
		LinearLayout.LayoutParams lp  = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		view.setGravity(Gravity.CENTER);
//		view.setBackgroundResource(R.drawable.profile_photobg);
		view.setVerticalScrollBarEnabled(false);
		return view;
	}
	
	public PopupWindow pop;
	public void showPopupWindowDel(final Context c, final View view, float x, float y, String str) {
		LayoutInflater inflater = LayoutInflater.from(c);
		View v = inflater.inflate(R.layout.item_face_popup, null);
		final GifImageView tv_img = (GifImageView) v.findViewById(R.id.tv_img);
		tv_img.displayGif(c, tv_img, str,new GifImageView.GifImageCallBack() {
			
			@Override
			public void gifOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
				// TODO Auto-generated method stub
				Consts.getInstance().showToast(context, code, flag, http_code);
				if(pop != null){
					pop.dismiss();
				}
			}
		});
		pop = new PopupWindow(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		// pop.setAnimationStyle(R.style.popwin_anim_style);
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setFocusable(true);
		pop.update();
		pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				PalmchatLogUtils.println("pop gif dismiss.");
//				tv_img.destroy();
				if(tv_img !=null){
					tv_img.destroyDrawingCache();
				}
			}
		});
		v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		// v.getMeasuredHeight(), 1).show();
//		pop.showAtLocation(v, Gravity.TOP, (int) (x - (view.getMeasuredWidth() * 1.5)), (int)(y - view.getMeasuredHeight()));
		if(context instanceof Activity){
			Activity activity = (Activity) context;
			Display defaultDisplay = activity.getWindow().getWindowManager().getDefaultDisplay();
			int w = activity.getIntent().getIntExtra("display_width", defaultDisplay.getWidth());
			int h = activity.getIntent().getIntExtra("display_height", defaultDisplay.getHeight());
			if(w >= 1080){
				pop.showAtLocation(v, Gravity.TOP, (int) (x - (view.getMeasuredWidth() * 1.5)), (int)(y - 1.5*view.getMeasuredHeight()));
			}else if(w >= 720){
				pop.showAtLocation(v, Gravity.TOP, (int) (x - (view.getMeasuredWidth() * 1.5)), (int)(y - 1.5*view.getMeasuredHeight()));
			}else if(w >= 320){
				pop.showAtLocation(v, Gravity.TOP, (int) (x - (view.getMeasuredWidth() * 1.5)), (int)(y - 1.5*view.getMeasuredHeight()));
			}else{
				// no handle
				pop.showAtLocation(v, Gravity.TOP, (int) (x - (view.getMeasuredWidth() * 1.5)), (int)(y - view.getMeasuredHeight()));
			}
			
		}
		

	}
	
	
	public void refreshFaceFootView(){
		if(CacheManager.getInstance().isFaceChange()){
			int size = emojjView.getChatting_emotion_type_layout().getChildCount();
			if(size > 0){
				for (int i = 1; i < size; size--) {
					View view = emojjView.getChatting_emotion_type_layout().getChildAt(i);
					if(view != null){
						emojjView.getChatting_emotion_type_layout().removeViewAt(i);
					}
				}
			}
			CacheManager.getInstance().setIsFaceChange(false);
			addGifFooter();
			emojjView.setFaceFooterView(this);
		}
	}
	
	
	public void showDownlaodStoreFace(){
		int position = CacheManager.getInstance().getSelectedPosition()-1;
		PalmchatLogUtils.println("showDownlaodStoreFace  position:"+position);
		if(iconViews != null && iconViews.size()-1 >= position && position >= 0){
			PalmchatLogUtils.println("showDownlaodStoreFace  iconViews size:"+iconViews.size());
			FrameLayout f = iconViews.get(position);
			if(f != null){
				f.performClick();
				setSelectedOrNot((ArrayList<ImageFolderInfo>) f.getTag(), (ImageView) f.findViewById(R.id.gif_one), ICON_SELETED);
			}
		}
	}
	
	private String getItemIdFromView(ArrayList<ImageFolderInfo> gifImageFolder) {
		String iconPath = gifImageFolder.get(2).dir;
		if(!TextUtils.isEmpty(iconPath)){
			int end_index = iconPath.lastIndexOf(CacheManager.getInstance().getScreenString());
			if(end_index != -1){
				end_index = end_index + +2;
				iconPath = iconPath.substring(0, end_index);
			}else{
				return "";
			}
		}
		String my_afid = CacheManager.getInstance().getMyProfile().afId+"/";
		String item_id = iconPath.substring(iconPath.lastIndexOf(my_afid)+my_afid.length(), iconPath.lastIndexOf("/"));
		return item_id;
	}
	
	public void showNewSymbolOrNot(){
		if(iconViews != null && iconViews.size() > 0){
			int size = iconViews.size();
			for (int i = 0; i < size; i++) {
				FrameLayout f = iconViews.get(i);
				RelativeLayout l_new = (RelativeLayout) f.findViewById(R.id.l_unread_msg);
				if(l_new != null){
					ArrayList<ImageFolderInfo> gifImageFolder = (ArrayList<ImageFolderInfo>) f.getTag();
					String item_id = getItemIdFromView(gifImageFolder);
					boolean to_update = CacheManager.getInstance().getItemid_update().containsKey(item_id);
					if(to_update){
						l_new.setVisibility(View.VISIBLE);
					}else{
						l_new.setVisibility(View.GONE);
					}
				}
			}
		}
	}
	
}
