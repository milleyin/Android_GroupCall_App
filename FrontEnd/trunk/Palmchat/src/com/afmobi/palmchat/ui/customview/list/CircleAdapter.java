package com.afmobi.palmchat.ui.customview.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.ui.activity.register.RegionOneActivity;
import com.afmobi.palmchat.ui.activity.register.RegionThreeActivity;
import com.afmobi.palmchat.ui.activity.register.RegionTwoActivity;
import com.afmobi.palmchat.ui.activity.setting.AboutActivity;
import com.afmobi.palmchat.ui.activity.setting.LanguageActivity;
import com.afmobi.palmchat.ui.activity.setting.MyAccountActivity;
import com.afmobi.palmchat.ui.customview.FlowLayout.LayoutParams;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;

public class CircleAdapter extends BaseAdapter {
	private Context context;
	private String[] mFrom;
	private int[] mTo;
	private List<? extends Map<String, Object>> mData;
	private int resource;
	private LayoutInflater mInflater;
	private int mCenter;
	private int mLast;
	private int mFrist;
	private int mDefault;
	private int mPosition = -1;
	//private OnItemClickListener mItemClickListener;
	private OnClickListener mClickListener;
	private OnItemBindListener mOnItemBindListener;
	private OnItemClickListener mItemListener;
	
	private Map<Integer, View> viewMap = new HashMap<Integer, View>();
	private Map<String, View> viewValueMap = new HashMap<String, View>();
	
	private boolean flag = false;
	private boolean rateApp = false;
	/**存在新版本*/
	private boolean mExitNewVer = false;
	
	private boolean isSettingLanguage = false;
	private int selectead_language_position;
	public boolean isSettingLanguage() {
		return isSettingLanguage;
	}
	
	public void setIsSettingLanguage(boolean isSettingLanguage) {
		this.isSettingLanguage = isSettingLanguage;
	}
	
	public int getSelectead_language_position() {
		return selectead_language_position;
	}

	public void SetSelectead_language_position(int selectead_language_position) {
		this.selectead_language_position = selectead_language_position;
	}
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public boolean isRateApp() {
		return rateApp;
	}
	
	public void setRateApp(boolean rateApp) {
		this.rateApp = rateApp;
	}

	public CircleAdapter(Context context, List<? extends Map<String, Object>> data,
			int resource, String[] from, int[] to) {
		this.mData = data;
		this.mFrom = from;
		this.mTo = to;
		this.resource = resource;
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public CircleAdapter(Context context, List<? extends Map<String, Object>> data,
			int resource, String[] from, int[] to ,boolean flag,boolean rateApp) {
		this.mData = data;
		this.mFrom = from;
		this.mTo = to;
		this.resource = resource;
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.flag = flag;
		this.rateApp = rateApp;
	}
	
	public CircleAdapter(Context context, List<? extends Map<String, Object>> data,
			int resource, String[] from, int[] to,boolean isSettingLanguage,int selectead_language_position) {
		this.mData = data;
		this.mFrom = from;
		this.mTo = to;
		this.resource = resource;
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.isSettingLanguage = isSettingLanguage;
		this.selectead_language_position = selectead_language_position;
	}
	
	public void setPosition(int position) {
		mPosition = position;
	}
	
	public void setData(List<? extends Map<String, Object>> data) {
		this.mData = data;
	}

	public void setOOnItemBindListener (OnItemBindListener l) {
		this.mOnItemBindListener = l;
	}
	
	public void setOnClickListener (OnClickListener l) {
		this.mClickListener = l;
	}

	public void setItemClickListener(OnItemClickListener l) {
		this.mItemListener = l;
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.w("getView", "postion=" + position);
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, null);
		} else {
			v = convertView;
		}
		if (position == 0 && position == mData.size() - 1) {
			v.setBackgroundResource(mDefault);
		} else if (position == mData.size() - 1) {
			v.setBackgroundResource(mLast);
		} else if (position == 0) {
			v.setBackgroundResource(mFrist);
		} else {
			v.setBackgroundResource(mCenter);
		}
		bindView(position, v);
	
		if( null != mOnItemBindListener){
			mOnItemBindListener.OnBindView(position, v);
		}
		
		v.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemListener != null) {
					mItemListener.onItemClick(getItem(position), v, position);
				}
				if (mClickListener != null) {
					Log.w("Click", v.getTag()+"");
					mClickListener.onClick(v , position);
				}
			}
		});
		
/*		if(isRateApp()){
			View view = v.findViewById(R.id.tv_new);
			if(view != null && position == 0){
				view.setVisibility(View.VISIBLE);
			}
		}*/
		
		if(mExitNewVer){
			View view = v.findViewById(R.id.img_new);
			if(view != null && position == 0){
				view.setVisibility(View.VISIBLE);
			}
		}
		
		if(flag && !isSettingLanguage){
			View view = v.findViewById(R.id.img_arrow);
			if(view != null && position == 2){
				view.setVisibility(View.VISIBLE);
			}
		}
		View bottom_view = v.findViewById(R.id.bottom_view);
		View top_view = v.findViewById(R.id.top_view);
		if (context instanceof AboutActivity) {
			if (position == getCount() -1) {
				top_view.setVisibility(View.GONE);
				bottom_view.setVisibility(View.GONE);
			}
		}
		if (context instanceof LanguageActivity //|| context instanceof RegionOneActivity
				|| context instanceof RegionTwoActivity
				|| context instanceof RegionThreeActivity || context instanceof MyAccountActivity) {
			if (position == 0) {
				top_view.setVisibility(View.VISIBLE);
			}else {
				top_view.setVisibility(View.GONE);
				
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
			if (position == (mData.size()-1)) {
				params.setMargins(0, 0, 0, 0);
			}else {
				params.setMargins(CommonUtils.dip2px(context, 18), 0, 0, 0);
			}
			bottom_view.setLayoutParams(params);
		}
		
		if(isSettingLanguage){
			ImageView view = (ImageView)v.findViewById(R.id.img_arrow);
			if(view != null){
				view.setVisibility(View.VISIBLE);
				view.setImageResource(R.drawable.radiobutton_normal);
				//selectead_language_position
				if(position == selectead_language_position){
					view.setImageResource(R.drawable.radiobutton_press);
				}
			}
		}
		
		return v;
	}
	

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Map<String, Object> getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void update(LinearLayoutListView listView) {
		for (int i = 0; i < mData.size(); i++) {
			getView(i, listView.getChildAt(i), listView);
		}
	}
	
	public void updateValues(Map<String ,Object> values) {
		if (viewValueMap != null) {
			//方法三
			for(Map.Entry<String ,View> entry: viewValueMap.entrySet()) {
				for(Map.Entry<String ,Object> entrySub: values.entrySet()) {
					if (entrySub.getKey().equals(entry.getKey())) {
						View v = entry.getValue();
						Object obj = entrySub.getValue();
						if (v instanceof TextView) {
							if (obj instanceof Integer) {
								((TextView)v).setText((Integer)obj);
							} else if (obj instanceof String) {
								((TextView)v).setText((String)obj);
							}
							
						} else if (v instanceof ImageView) {
							if (obj != null) {
								setViewImage((ImageView)v, (String)obj);
							}
						}
					}
						
				}
			}
		}
	}

	private void bindView(final int position, View view) {
		viewMap.put(position, view);
		final Map<String , Object> dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}
		
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}
				if (LinearLayoutListView.KEY_VALUE.equals(text)) {
					viewValueMap.put(text, v);
				}
				view.setTag(text);
				boolean bound = false;

				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
							((CheckBox) v).setOnCheckedChangeListener(new OnCheckedChangeListener() {
								
								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									if (mClickListener != null) {
										mClickListener.onClick(v , position);
									}
								}
							});
							v.setOnClickListener(new android.view.View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if (mClickListener != null) {
										mClickListener.onClick(v , position);
									}
								}
							});
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at the
							// bottom of these
							// ifs since a lot of views are TextViews (e.g.
							// CheckBoxes).
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass()
									.getName()
									+ " should be bound to a Boolean, not a "
									+ (data == null ? "<unknown type>"
											: data.getClass()));
						}
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						Log.e("imageview", mPosition + "->" + R.id.setting_arrow);
						if (position == mPosition && v.getId() == R.id.setting_arrow) {
							((ImageView) v).setImageBitmap(null);
						} else {
							if (data instanceof Integer) {
								setViewImage((ImageView) v, (Integer) data);
							} else {
								String url = data + "";
								if(url.startsWith("http://") || url.startsWith("https://")) 
									ImageManager.getInstance().DisplayImage((ImageView) v, url, 0, false, null);
								else
								setViewImage((ImageView) v, text);
							}
						}
						
					} else {
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a "
										+ " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}
	
	
	public interface OnItemClickListener {
		void onItemClick(Map<String, Object> item , View view , int position);
	}
	
	public interface OnItemBindListener {
		public void OnBindView(int position, View parent);
	}
	
	public interface OnClickListener {
		void onClick(View v , int position);
	}
	
	
	private void setViewImage(ImageView v, String text) {
		try {
			v.setImageResource(Integer.parseInt(text));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(text));
		}
	}

	private void setViewImage(ImageView v, int data) {
		v.setImageResource(data);
	}

	private void setViewText(TextView v, String text) {
		if (v.getId() == R.id.setting_arrow) {
			if (context.getString(R.string.version).equals(text)) {
//				v.setBackgroundDrawable(null);
				//v.setText(AppUtils.getAppVersionName(context));
			} else {
				//v.setText(null);
			}
		} else {
			v.setText(text);
		}
	}

	public void setFristBackground(int res) {
		this.mFrist = res;
	}

	public void setLastBackgroud(int res) {
		this.mLast = res;
	}

	public void setCenterBackgroud(int res) {
		this.mCenter = res;
	}

	public void setBackgroud(int res) {
		this.mDefault = res;
	}
	
	/**
	 * 设置是否存在新版本
	 * @param exitNewVer
	 */
	public void setExitNewVer(boolean exitNewVer) {
		this.mExitNewVer = exitNewVer;
	}
	
	/**
	 * 获取是否存在新版本(adapter)
	 */
	public boolean isExitNewVer() {
		return this.mExitNewVer;
	}
}
