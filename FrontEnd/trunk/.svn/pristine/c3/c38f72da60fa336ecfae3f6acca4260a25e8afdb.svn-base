package com.afmobi.palmchat.ui.activity.store.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfPalmchat;
import com.core.AfPaystoreCommon.AfStoreDlProdInfo;

public class StoreMyEmojiListAdapter extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 中间件数据列表
     */
    private List<AfStoreDlProdInfo> mList;
    /**
     * 进入类型 1：LAST_ENTRY 2：COMMON_ENTRY
     */
    private int mType;
    /**
     * 布局加载器
     */
    private LayoutInflater mInflater;
    /**
     * 本地库接口类
     */
    private AfPalmchat mAfCorePalmchat;
    /**
     * 判断是否是编辑模式
     */
    private boolean mIsDragSort;
    /***/
    public static final int LAST_ENTRY = 1;
    /**
     * 正常进入
     */
    public static final int COMMON_ENTRY = 2;

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

    public StoreMyEmojiListAdapter(Context context, List<AfStoreDlProdInfo> list, boolean isDragSort) {
        this.mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
        mAfCorePalmchat = ((PalmchatApp) PalmchatApp.getApplication()).mAfCorePalmchat;
        mIsDragSort = isDragSort;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null == mList ? 0 : mList.size();
    }

    @Override
    public AfStoreDlProdInfo getItem(int position) {

        if (position >= getCount() || position < 0) {
            return null;
        }
        return mList.get(position);

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        if (LAST_ENTRY == mType) {
            return position;
        }
        return --position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.store_myemoji_item, null);
            holder = new ViewHolder();

//			    holder.age = (TextView)convertView.findViewById(R.id.text_age);
            holder.range = (TextView) convertView.findViewById(R.id.drag_handle);
            holder.head = (ImageView) convertView.findViewById(R.id.img_photo);
            holder.name = (TextView) convertView.findViewById(R.id.text_name);
            holder.score = (TextView) convertView.findViewById(R.id.text_sign);
            holder.free = (TextView) convertView.findViewById(R.id.text_score_free);
            holder.distance = (TextView) convertView.findViewById(R.id.text_distance);
            holder.time = (TextView) convertView.findViewById(R.id.text_time);
            holder.fire = (LinearLayout) convertView.findViewById(R.id.right_layout);
            holder.linefault_view = convertView.findViewById(R.id.linefault_view);
            holder.linefull_view = convertView.findViewById(R.id.linefull_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//		in edit mode
        if (mIsDragSort) {
            holder.fire.setVisibility(View.VISIBLE);
            holder.range.setVisibility(View.VISIBLE);
        } else {
            holder.fire.setVisibility(View.GONE);
            holder.range.setVisibility(View.GONE);
        }
        bintSetLine(holder, position);
        AfStoreDlProdInfo profile = mList.get(position);

        holder.name.setText(profile.alas_name);
        holder.score.setText(profile.price + "");
        holder.time.setText(mDateFormat.format(new Date(profile.datetime)));
        if (profile.price > 0) {
            holder.free.setVisibility(View.GONE);
            holder.score.setVisibility(View.VISIBLE);
            holder.score.setText(String.valueOf(profile.price));
        } else {
            holder.free.setVisibility(View.VISIBLE);
            holder.score.setVisibility(View.INVISIBLE);
        }

        ImageManager.getInstance().DisplayImage(holder.head, profile.save_path, R.drawable.store_emoji_default, false, null);
        return convertView;
    }

    /**
     * 设置line显示
     *
     * @param viewHolder
     * @param position
     */
    private void bintSetLine(ViewHolder viewHolder, int position) {
        if (position == (getCount() - 1)) {
            viewHolder.linefault_view.setVisibility(View.GONE);
            viewHolder.linefull_view.setVisibility(View.VISIBLE);
        } else {
            viewHolder.linefault_view.setVisibility(View.VISIBLE);
            viewHolder.linefull_view.setVisibility(View.GONE);
        }
    }

    /**
     * viewHolder
     */
    private class ViewHolder {
        TextView age;
        TextView range;
        ImageView head;
        TextView name;
        TextView score;
        TextView free;
        TextView distance;
        TextView time;
        LinearLayout fire;
        View linefault_view;
        View linefull_view;

    }

}
