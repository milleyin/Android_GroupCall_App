package com.afmobi.palmchat.ui.customview;

import java.util.ArrayList;
import java.util.List;

import com.afmobigroup.gphone.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 下拉文本框
 * 
 * @author zhh 2015/12/7
 * 
 */
public class TextViewDropDown extends TextView implements OnClickListener, OnItemClickListener {
	List<String> dataLs = new ArrayList<String>();
	LayoutInflater layouatInflater;
	PopupWindow selectPopupWindow;
	PopupAdapter popupAdapter;
	TVSelectedListener tvSelectedListener;
	private int selectedPosition;
	private boolean isFirst = true;

	public TextViewDropDown(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		layouatInflater = LayoutInflater.from(context);
		setOnClickListener(this);
	}

	public TextViewDropDown(Context context, AttributeSet attrs) {
		super(context, attrs);
		layouatInflater = LayoutInflater.from(context);
		setOnClickListener(this);
	}

	public TextViewDropDown(Context context) {
		super(context);
		layouatInflater = LayoutInflater.from(context);
		setOnClickListener(this);
	}

	public void setDatas(List<String> strLs, TVSelectedListener selectedListener) {
		if (strLs != null && strLs.size() > 0) {
			tvSelectedListener = selectedListener;
			setText(strLs.get(0)); // 初始化为第一项
			dataLs = strLs;
			if (popupAdapter != null)
				popupAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取选中项位置
	 * 
	 * @return
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * 显示列表框
	 */
	private void showPopupWindow() {
		if (selectPopupWindow == null) { // 第一次
			// PopupWindow浮动下拉框布局
			View mView = (View) layouatInflater.inflate(R.layout.popupwindow_dropdown, null);
			ListView listView = (ListView) mView.findViewById(R.id.lv);
			// 设置自定义Adapter
			popupAdapter = new PopupAdapter();
			listView.setAdapter(popupAdapter);
			listView.setOnItemClickListener(this);

			selectPopupWindow = new PopupWindow(mView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
			selectPopupWindow.setOutsideTouchable(true);
			selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			selectPopupWindow.showAsDropDown(this, 0, -3);
			// Drawable drawable =
			// getResources().getDrawable(R.drawable.ic_attr_open);
			// setCompoundDrawables(null, null, drawable, null);
		} else if (selectPopupWindow.isShowing()) {
			selectPopupWindow.dismiss();
		} else {
			selectPopupWindow.showAsDropDown(this, 0, -3);
		}

	}

	class PopupAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataLs.size();
		}

		@Override
		public Object getItem(int position) {
			return dataLs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				// 下拉项布局
				convertView = layouatInflater.inflate(R.layout.item_popupwindow_dropdown, null);
				holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textView.setText(dataLs.get(position));
			if (isFirst && position == 0) {
				isFirst = false;
				holder.textView.setTextColor(getResources().getColor(R.color.log_blue));
			}

			return convertView;
		}

	}

	class ViewHolder {
		TextView textView;
	}

	@Override
	public void onClick(View v) {
		if (dataLs != null && dataLs.size() > 1) // 当数据大于一条时才弹，只有一条时不弹
			showPopupWindow();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if (position < dataLs.size()) {
			selectedPosition = position;
			setText(dataLs.get(position));
		}

		for (int i = 0; i < dataLs.size(); i++) {
			View mView = parent.getChildAt(i);
			if (mView != null) {
				TextView tv = (TextView) mView.findViewById(R.id.tv_text);
				if (tv != null) {
					if (i == position) {
						tv.setTextColor(getResources().getColor(R.color.log_blue));
					} else {
						tv.setTextColor(getResources().getColor(R.color.public_group_memeber_color));
					}
				}

			}

		}

		if (popupAdapter != null)
			popupAdapter.notifyDataSetChanged();

		if (selectPopupWindow != null && selectPopupWindow.isShowing())
			selectPopupWindow.dismiss();

		if (tvSelectedListener != null) {
			tvSelectedListener.OnSelected(position);
		}
	}

	/**
	 * 当选项改变时回调
	 * 
	 * @author zhh
	 * 
	 */
	public interface TVSelectedListener {
		void OnSelected(int position);
	}
}
