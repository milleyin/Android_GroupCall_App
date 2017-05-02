package com.afmobi.palmchat.ui.activity.store.adapter;

import java.util.ArrayList;

import com.afmobigroup.gphone.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class StoreFaceDetailAdapter extends BaseAdapter {
	/**布局加载器*/
	private LayoutInflater mInflater;
	/**上下文*/
	private Context mContext;
	/**数据列表*/
	private ArrayList<String> mStringList;

	public StoreFaceDetailAdapter(Context c, ArrayList<String> arrayList) {
		this.mContext = c;
		this.mStringList = arrayList;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mStringList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.store_facedetail_gridview_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.facedetail_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Bitmap b = BitmapFactory.decodeFile(mStringList.get(position));
		if (b != null) {
			holder.img.setImageBitmap(b);
		}
		
		return convertView;
	}

	public ArrayList<String> getStringList() {
		return mStringList;
	}
	
	public void clean() {
		if(null!=mStringList) {
			mStringList.clear();
			mStringList=null;
		}
	}
	
	/**
	 * viewHolder
	 *
	 */
	public final class ViewHolder {
		/**显示图片*/
		ImageView img;
	}

}
