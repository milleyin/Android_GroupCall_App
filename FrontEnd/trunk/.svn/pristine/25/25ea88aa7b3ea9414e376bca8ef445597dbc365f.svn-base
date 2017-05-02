package com.afmobi.palmchat.ui.activity.social;

import java.util.ArrayList;

import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.customview.RectImageView;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm.AfBCPriefInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.cache.CacheManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * Trending 列表适配器
 * @author Transsion
 *
 */
public class TrendingAdapter extends BaseAdapter implements OnClickListener{

	private final String TAG = TrendingAdapter.class.getSimpleName();
	/**布局加载器*/
	private LayoutInflater mInflater;	
	/**上下文*/
	private Context mContext;
	/**照片数据信息*/
	private ArrayList<ArrayList<AfBCPriefInfo>> mPhotoGraphys;
	
	/**
	 * 构造方法
	 * @param context
	 */
	public TrendingAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mPhotoGraphys = new ArrayList<ArrayList<AfBCPriefInfo>>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null==mPhotoGraphys?0:mPhotoGraphys.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0>mPhotoGraphys.size()?null:mPhotoGraphys.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(null==convertView) {
			convertView = mInflater.inflate(R.layout.item_trending,null);
			viewHolder = new ViewHolder();
			viewHolder.rIv_First = (RectImageView)convertView.findViewById(R.id.trending_imageview_first_id);
			viewHolder.rIv_Second = (RectImageView)convertView.findViewById(R.id.trending_imageview_second_id);
			viewHolder.rIv_Third = (RectImageView)convertView.findViewById(R.id.trending_imageview_third_id);
			viewHolder.iv_Fourth = (ImageView)convertView.findViewById(R.id.trending_imageview_fourth_id);
			viewHolder.iv_Fifth = (ImageView)convertView.findViewById(R.id.trending_imageview_fifth_id);
			viewHolder.iv_Sixth = (ImageView)convertView.findViewById(R.id.trending_imageview_sixth_id);
			viewHolder.rIv_Seventh = (RectImageView)convertView.findViewById(R.id.trending_imageview_seventh_id);
			viewHolder.rIv_Eighth = (RectImageView)convertView.findViewById(R.id.trending_imageview_eighth_id);
			viewHolder.rIv_Nineth = (RectImageView)convertView.findViewById(R.id.trending_imageview_nineth_id);
			viewHolder.rIv_VideoFlag = new RectImageView[9];
			viewHolder.rIv_VideoFlag[0] = (RectImageView)convertView.findViewById(R.id.trending_imageview_first_vf_id);
			viewHolder.rIv_VideoFlag[1] = (RectImageView)convertView.findViewById(R.id.trending_imageview_second_vf_id);
			viewHolder.rIv_VideoFlag[2] = (RectImageView)convertView.findViewById(R.id.trending_imageview_third_vf_id);
			viewHolder.rIv_VideoFlag[3] = (RectImageView)convertView.findViewById(R.id.trending_imageview_fourth_vf_id);
			viewHolder.rIv_VideoFlag[4] = (RectImageView)convertView.findViewById(R.id.trending_imageview_fifth_vf_id);
			viewHolder.rIv_VideoFlag[5] = (RectImageView)convertView.findViewById(R.id.trending_imageview_sixth_vf_id);
			viewHolder.rIv_VideoFlag[6] = (RectImageView)convertView.findViewById(R.id.trending_imageview_seventh_vf_id);
			viewHolder.rIv_VideoFlag[7] = (RectImageView)convertView.findViewById(R.id.trending_imageview_eighth_vf_id);
			viewHolder.rIv_VideoFlag[8] = (RectImageView)convertView.findViewById(R.id.trending_imageview_nineth_vf_id);



			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.rIv_First.setOnClickListener(this);
		viewHolder.rIv_First.setTag(position);
		viewHolder.rIv_Second.setOnClickListener(this);
		viewHolder.rIv_Second.setTag(position);
		viewHolder.rIv_Third.setOnClickListener(this);
		viewHolder.rIv_Third.setTag(position);
		viewHolder.iv_Fourth.setOnClickListener(this);
		viewHolder.iv_Fourth.setTag(position);
		viewHolder.iv_Fifth.setOnClickListener(this);
		viewHolder.iv_Fifth.setTag(position);
		viewHolder.iv_Sixth.setOnClickListener(this);
		viewHolder.iv_Sixth.setTag(position);
		viewHolder.rIv_Seventh.setOnClickListener(this);
		viewHolder.rIv_Seventh.setTag(position);
		viewHolder.rIv_Eighth.setOnClickListener(this);
		viewHolder.rIv_Eighth.setTag(position);
		viewHolder.rIv_Nineth.setOnClickListener(this);
		viewHolder.rIv_Nineth.setTag(position);
		
		ArrayList<AfBCPriefInfo> brief_list = mPhotoGraphys.get(position);
		
		if(null!=brief_list) {
			
			if(brief_list.size()<DefaultValueConstant.TRENDINGDEFAULTNUMBER) {
				convertView.setVisibility(View.GONE);
				return convertView;
			}
			
			ImageManager.getInstance().DisplayImage(viewHolder.rIv_First,CacheManager.getInstance().getTrThumb_url(brief_list.get(0).pic_url,false,true),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.rIv_Second,CacheManager.getInstance().getTrThumb_url(brief_list.get(1).pic_url,false,true),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.rIv_Third,CacheManager.getInstance().getTrThumb_url(brief_list.get(2).pic_url,false,true),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.iv_Fourth,CacheManager.getInstance().getTrThumb_url(brief_list.get(3).pic_url,false,false),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.iv_Fifth,CacheManager.getInstance().getTrThumb_url(brief_list.get(4).pic_url,false,false),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.iv_Sixth,CacheManager.getInstance().getTrThumb_url(brief_list.get(5).pic_url,false,false),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.rIv_Seventh,CacheManager.getInstance().getTrThumb_url(brief_list.get(6).pic_url,false,false),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.rIv_Eighth,CacheManager.getInstance().getTrThumb_url(brief_list.get(7).pic_url,false,false),
					R.color.base_back,false,null);
			ImageManager.getInstance().DisplayImage(viewHolder.rIv_Nineth,CacheManager.getInstance().getTrThumb_url(brief_list.get(8).pic_url,false,false),
					R.color.base_back,false,null);
		}

		for(int i=0;i<viewHolder.rIv_VideoFlag.length;i++){
			if(brief_list.get(i).pic_url.contains(DefaultValueConstant.TRENGING_VIDEO_FLAG_URL)){
				viewHolder.rIv_VideoFlag[i].setVisibility(View.VISIBLE);
			} else {
				viewHolder.rIv_VideoFlag[i].setVisibility(View.GONE);
			}
		}
		convertView.setVisibility(View.VISIBLE);
		return convertView;
	}

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.trending_imageview_first_id: {
			jumpToBroDetail(0,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_second_id:{
			jumpToBroDetail(1,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_third_id:{
			jumpToBroDetail(2,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_fourth_id:{
			jumpToBroDetail(3,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_fifth_id:{
			jumpToBroDetail(4,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_sixth_id:{
			jumpToBroDetail(5,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_seventh_id:{
			jumpToBroDetail(6,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_eighth_id:{
			jumpToBroDetail(7,(Integer) v.getTag());
			break;
		}
		case R.id.trending_imageview_nineth_id: {
			jumpToBroDetail(8,(Integer) v.getTag());			
			break;
		}
		
		default:
			break;
		}
	}
	
	private void jumpToBroDetail(int index,Integer position) {
		if(position>=mPhotoGraphys.size()||(index>=mPhotoGraphys.get(position).size())){
			return;
		}
		Intent intent = new Intent();
		AfChapterInfo afChapterInfo = new AfChapterInfo();
		afChapterInfo.mid = mPhotoGraphys.get(position).get(index).mid;
		Bundle bundle = new Bundle();
		bundle.putSerializable(JsonConstant.KEY_BC_AFCHAPTERINFO, afChapterInfo);
		bundle.putInt(JsonConstant.KEY_BC_SKIP_TYPE, BroadcastDetailActivity.TYPE_BROADCASTLIST);
		intent.setClass((Activity)mContext, BroadcastDetailActivity.class);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
	}
	
	/**
	 * 更新列表数据
	 * @param brief_list
	 * @param isRefresh
	 */
	public void updateListData(ArrayList<AfBCPriefInfo> brief_list,boolean isRefresh) {
		
		if(isRefresh) {
			mPhotoGraphys.clear();	
		}
		ArrayList<AfBCPriefInfo> brief_list1;
		ArrayList<AfBCPriefInfo> brief_list2;
		if(null!=brief_list) {
			if(brief_list.size()>=DefaultValueConstant.TRENDINGLOADMORNUMBER){
				brief_list1 = new ArrayList<AfBCPriefInfo>(brief_list.subList(0,DefaultValueConstant.TRENDINGDEFAULTNUMBER));
				brief_list2 = new ArrayList<AfBCPriefInfo>(brief_list.subList(DefaultValueConstant.TRENDINGDEFAULTNUMBER,DefaultValueConstant.TRENDINGLOADMORNUMBER));
				mPhotoGraphys.add(brief_list1);
				mPhotoGraphys.add(brief_list2);
				notifyDataSetChanged();
			} else if(brief_list.size()>=DefaultValueConstant.TRENDINGDEFAULTNUMBER){
				mPhotoGraphys.add(brief_list);
				notifyDataSetChanged();
			}
		} else {
			PalmchatLogUtils.i(TAG,"-------------brief_list==NULL---------------");
		}
	}
	
	/**
	 * 清空数据
	 */
	public void cleanData(){
		if(null!=mPhotoGraphys){
			mPhotoGraphys.clear();
			notifyDataSetChanged();
		}
	}

	/**
	 * 显示内部类
	 *
	 */
	private class ViewHolder {
		/**第一张照片*/
		public RectImageView rIv_First;
		/**第二张照片*/
		public RectImageView rIv_Second;
		/**第三张照片*/
		public RectImageView rIv_Third;
		/**第四张照片*/
		public ImageView iv_Fourth;
		/**第五张照片*/
		public ImageView iv_Fifth;
		/**第六张照片*/
		public ImageView iv_Sixth;
		/**第七张照片*/
		public RectImageView rIv_Seventh;
		/**第八张照片*/
		public RectImageView rIv_Eighth;
		/**第九张照片*/
		public RectImageView rIv_Nineth;
		/**视频标记*/
		public RectImageView[] rIv_VideoFlag;
	}
}
