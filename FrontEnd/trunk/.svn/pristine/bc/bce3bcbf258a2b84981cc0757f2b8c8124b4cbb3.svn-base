package com.afmobi.palmchat.ui.activity.profile;


import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.ReplaceConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.main.model.DialogItem;
import com.afmobi.palmchat.ui.activity.profile.LargeImageDialog.ILargeImageDialog;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls;
import com.afmobi.palmchat.ui.activity.social.AdapterBroadcastUitls.IAdapterBroadcastUitls;
import com.afmobi.palmchat.ui.activity.social.BaroadCast_ViewHolder;
import com.afmobi.palmchat.ui.activity.social.BroadcastDetailActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.AppDialog.OnConfirmButtonDialogListener;
import com.afmobi.palmchat.ui.customview.CollapsibleTextView;
import com.afmobi.palmchat.ui.customview.videoview.VedioManager;
import com.afmobi.palmchat.util.BroadcastUtil;
import com.afmobi.palmchat.util.ByteUtils;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.SoundManager;
import com.afmobi.palmchat.util.ToastManager;
import com.afmobi.palmchat.util.VoiceManager;
import com.afmobigroup.gphone.R;
import com.core.AfMessageInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.AfResponseComm;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfCommentInfo;
import com.core.AfResponseComm.AfLikeInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;
import de.greenrobot.event.EventBus;

/**
 * AdapterBroadcastMessages
 * @author gtf
 *
 */
public class AdapterBroadcastProfile extends BaseAdapter implements IAdapterBroadcastUitls{
	String TAG= AdapterBroadcastProfile.class.getCanonicalName();
	private Activity mContext;
	private List<AfChapterInfo> broadcastMessageList = new ArrayList<AfChapterInfo>();
	//记录comment
	private ArrayList<CommentModel> aModels = new ArrayList<CommentModel>();
	private CacheManager cacheManager ;
	private AfProfileInfo myprofileInfo ;
	/*private final int clikeLike = 111;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case clikeLike:
				BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) msg.obj;
				AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
				viewHolder.txt_like.setText(afChapterInfo .total_like+"");
				viewHolder.txt_like.setSelected(true);
				viewHolder.txt_like.setClickable(false);
				PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_LIKE);
				break;

			default:
				break;
			}
		};
	};*/
	private AfPalmchat mAfCorePalmchat;
	private BaseActivity baseActivity;
	private int c_index =-1;
	private AdapterBroadcastUitls adapterBroadcastUitls;
	private LargeImageDialog largeImageDialog;
//	private ListViewAddOn listViewAddOn;
	public AdapterBroadcastProfile(Activity mContext, List<AfChapterInfo> broadcastMessageList, int actType, BaseActivity baseActivity  ,AfProfileInfo info) {
		this.mContext = mContext;
		cacheManager = CacheManager.getInstance();
		myprofileInfo = cacheManager.getMyProfile();
		mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
		this.baseActivity =  baseActivity;
		setBroadcastMessageList(broadcastMessageList, false);
		adapterBroadcastUitls = new AdapterBroadcastUitls(mContext, BroadcastDetailActivity.FROM_PROFILE,info);
		adapterBroadcastUitls.setIAdapterBroadcastUitls(this);
//		this.listViewAddOn = listViewAddOn;
	}

	@Override
	public int getCount() {
		return broadcastMessageList.size();
	}

	@Override
	public AfChapterInfo getItem(int position) {
		return broadcastMessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clear(){
		if (null != this.broadcastMessageList) {
			this.broadcastMessageList.clear();
		}
	}

	/**
	 * 判断是否刷新数据还是叠加数据
	 * @param broadcastMessageList
	 * @param isLoadMore
	 */
	private void setBroadcastMessageList(List<AfChapterInfo> broadcastMessageList, boolean isLoadMore) {
		if (!isLoadMore) {
			this.broadcastMessageList.clear();
		}
		if (null != broadcastMessageList) {
			this.broadcastMessageList.addAll(broadcastMessageList);
		}
		
	}

	/**
	 * 刷新列表
	 * @param broadcastMessageList
	 * @param isLoadMore
	 */
	public void notifyDataSetChanged(List<AfChapterInfo> broadcastMessageList, boolean isLoadMore) {
		setBroadcastMessageList(broadcastMessageList, isLoadMore);
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChanged(AfChapterInfo afChapterInfo,int _id) {
		int index = ByteUtils.indexOfBroadCastListByid(broadcastMessageList, _id);
		if (index > -1) {
			broadcastMessageList.remove(index);
			broadcastMessageList.add(index, afChapterInfo);
		}
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChanged(AfChapterInfo afChapterInfo) {
		broadcastMessageList.add(0, afChapterInfo);
		notifyDataSetChanged();
	}
	public void notifyDataSetChanged_removeBymid(String mid) {
		int index = ByteUtils.indexOfBroadCastListBymid(broadcastMessageList, mid);
		if (index > -1) {
			broadcastMessageList.remove(index);    
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position,View convertView,final ViewGroup parent) {
		BaroadCast_ViewHolder viewHolder = null;
		final AfChapterInfo info = broadcastMessageList.get(position);
		if (null == convertView) {
			viewHolder = new BaroadCast_ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.broadcast_list_messages_item_text, null);
			adapterBroadcastUitls.initTextView(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (BaroadCast_ViewHolder) convertView.getTag();
		}
		
//		/**add by zhh listview分割线*/
//		if (position == broadcastMessageList.size() - 1)
//			viewHolder.line.setVisibility(View.GONE);
//		else
 		viewHolder.line.setVisibility(View.VISIBLE);
//		PalmchatLogUtils.e(TAG, "listViewAddOn.isSlipping()="+listViewAddOn.isSlipping()+",position = "+position);
		adapterBroadcastUitls.bindView(viewHolder, info, position,mContext.getClass().getName());

		bindComment(viewHolder, info, position);
		bindCommentClick(viewHolder, position);
		return convertView;
	}

	public void bindCommentClick(final BaroadCast_ViewHolder viewHolder, final int position) {
		viewHolder.chatting_options_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				// TODO Auto-generated method stub
				String hint_msg = mContext.getString(R.string.hint_commet);
				CommentModel commentModel = new CommentModel();
				commentModel.setPosition(position);
				commentModel.setHolder(viewHolder);
				commentModel.setHint_msg(hint_msg);
				aModels.clear();
				aModels.add(commentModel);
			}
		});
	}



	public List<AfChapterInfo> getLists() {
		return broadcastMessageList;
	}

	/**
	 * 设置评论
	 * @param viewHolder
	 * @param info
	 * @param position
	 */
	private void bindComment(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo info,final int position){
//		viewHolder.comment_part.setVisibility(View.GONE);
//		viewHolder.division_up_view.setVisibility(View.GONE);
//		viewHolder.division_down_view.setVisibility(View.GONE);
		viewHolder.lin_comment.setVisibility(View.GONE);
		viewHolder.chatting_options_layout.setVisibility(View.GONE);
		if (info != null) {
			int comment_count = 0;
			if (comment_count > 0) {
				String total =(mContext.getString(R.string.bc_comment_total)).replace(ReplaceConstant.TARGET_FOR_REPLACE, String.valueOf( comment_count) );
				viewHolder.txt_comment_total.setVisibility(View.GONE);
				if (comment_count > 3) {
					viewHolder.view_comment_more.setVisibility(View.VISIBLE);
				}else {
					viewHolder.view_comment_more.setVisibility(View.GONE);
				}
				viewHolder.txt_comment_total.setText(total);
				viewHolder.lin_comment.setVisibility(View.VISIBLE);
				CollapsibleTextView c_tView = null;
				viewHolder.listView_comment.removeAllViews();
				for (int i = 0; i < comment_count; i++) {
					if (i < 3) {
						if (info.list_comments.size() > 0) {
							final AfCommentInfo afCommentInfo = info.list_comments.get(i);
							c_tView = new CollapsibleTextView(mContext);
							String name = afCommentInfo.profile_Info.name ;
							String conent = afCommentInfo.comment;
							String reply = mContext.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, "");
							String str ="";
							if (conent.contains(reply)) {
								str = CommonUtils.ToDBC(name+""+ conent);
							}else  {
								str = CommonUtils.ToDBC(name+":"+ conent);
							}
							CharSequence text = EmojiParser.getInstance(mContext).parse(str ,name, CommonUtils.dip2px(mContext, 16));
							c_tView.setDesc(text, BufferType.NORMAL);
							c_tView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View view) {
									PalmchatLogUtils.e(TAG, "position="+position);
									String hint_name = mContext.getString(R.string.reply_xxxx).replace(Constants.REPLY_STRING, afCommentInfo.profile_Info.name);
									CommentModel commentModel = new CommentModel();;
									commentModel.setPosition(position);
									commentModel.setHolder(viewHolder);
									String to_afid = afCommentInfo.afid;
									commentModel.setTo_afid(to_afid,afCommentInfo.profile_Info.name);
									commentModel.setHint_msg(hint_name);
									aModels.clear();
									aModels.add(commentModel);
								}
							});
							c_tView.setBackgroundResource(R.drawable.comment_uilist);
							c_tView.setPadding(20, 5, 20, 5);
							viewHolder.listView_comment.addView(c_tView);
						}
					}
				}
			}else {
				viewHolder.lin_comment.setVisibility(View.GONE);
			}
		}
	}

	public AfHttpResultListener afHttpResultListener = new AfHttpResultListener() {
		
		@Override
		public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
			PalmchatLogUtils.e(TAG, "----flag:" + flag + "----code:" + code + "----result:" + result);
			cacheManager.getsendTime(System.currentTimeMillis()).toString();
			if (code == Consts.REQ_CODE_SUCCESS) {
				int status =  AfMessageInfo.MESSAGE_SENT;
				switch (flag) {
				case Consts.REQ_BCMSG_AGREE:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
						int like_db_id =   (Integer) viewHolder.txt_like.getTag();
						mAfCorePalmchat.AfDBBCLikeUpdateStatusByID(status, like_db_id);
						mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
						viewHolder.txt_like.setTag(0);
						sendUpdateLikeBroadcastList(afChapterInfo);
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.LIKE_BCM_PF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.LIKE_BCM_PF);
						
					}
					break;
				case Consts.REQ_BCMSG_COMMENT:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						int c_db_id =   (Integer) viewHolder.txt_comment.getTag();
						mAfCorePalmchat.AfDBBCCommentUpdateStatusByID(status, c_db_id);
						if (result != null) {
							mAfCorePalmchat.AfDBBCCommentUpdateCidByID(String.valueOf(result), c_db_id);
						}
						viewHolder.txt_comment.setTag(0);
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.COM_BCM);
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.COM_BCM_PF);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.COM_BCM_PF);
					}
					break;
				case Consts.REQ_BCMSG_ACCUSATION:
					if (baseActivity !=null ) {
						baseActivity.dismissProgressDialog();
					}
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.REPORT_BCM);
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.REPORT_BCM_PF);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.REPORT_BCM_PF);
					ToastManager.getInstance().show(mContext, R.string.report_success);
					break;
				case Consts.REQ_BCMSG_DELETE://广播删除
					if (baseActivity !=null ) {
						baseActivity.dismissProgressDialog();
					}
					new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM_SUCC);
//					MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM_SUCC);
					ToastManager.getInstance().show(mContext, R.string.bc_del_success);
					if (user_data != null) {
//						int position = (Integer) user_data;
//						AfChapterInfo afInfo = getItem(position );
//						broadcastMessageList.remove(position);
						AfChapterInfo afInfo = (AfChapterInfo) user_data;
						sendUpdateDeleteBroadcastList(afInfo);
						mAfCorePalmchat.AfDBBCChapterDeleteByID(afInfo._id);
//						notifyDataSetChanged();
						
						if (largeImageDialog != null) {
							if (largeImageDialog.isShowing()) {
								largeImageDialog.dismiss();
							}
						}
					}
					if (getCount() <= 0) {
						if (iAdapterBroadcastMessages_profile != null) {
							iAdapterBroadcastMessages_profile.on_showNoData();
						}
					}
					break;
				case Consts.REQ_BCCOMMENT_DELETE://广播评论删除
					if (baseActivity !=null ) {
						baseActivity.dismissProgressDialog();
					}
					ToastManager.getInstance().show(mContext, R.string.success);
					if (user_data != null) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						AfChapterInfo afChapterInfo =(AfChapterInfo) viewHolder.lin_comment.getTag();
						if (c_index != -1) {
							afChapterInfo.list_comments.remove(c_index);
							afChapterInfo.total_comment --;
							viewHolder.bc_item.getTag();
							viewHolder.txt_comment.setText(afChapterInfo.total_comment+"");
							int c_db_id =   (Integer) viewHolder.txt_comment.getTag();
							mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
						}
						c_index =-1;
					}
					break;
				default:
					break;
				}
			}else {
				switch (flag) {
				case Consts.REQ_BCMSG_AGREE:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						AfChapterInfo afChapterInfo = (AfChapterInfo) viewHolder.lin_comment.getTag();
						if (code == Consts.REQ_CODE_142) {// 广播不存在或已经被删除
//							Consts.getInstance().showToast(mContext, code, flag, http_code);提示语最后统一处理 这里就不重复了
							mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
							sendUpdateDeleteBroadcastList(afChapterInfo);// 发广播通知删除广播
						} else{
							if (code == Consts.REQ_CODE_169){//重复点赞
								 PalmchatApp.getApplication().mAfCorePalmchat.AfBcLikeFlagSave(afChapterInfo.mid);
							}else {// 其他网络等原因引起的赞失败
								afChapterInfo.isLike = false;
								viewHolder.txt_like.setSelected(false);
								viewHolder.txt_like.setClickable(true);
//								Consts.getInstance().showToast(mContext, code, flag, http_code);提示语最后统一处理 这里就不重复了
							}
							
						} 
						if(afChapterInfo!=null&& afChapterInfo.list_likes.size()>0){ 
							afChapterInfo.total_like --;
							viewHolder.txt_like.setText(String.valueOf( afChapterInfo.total_like ));
							int like_db_id =   (Integer) viewHolder.txt_like.getTag();
							mAfCorePalmchat.AfDBBCLikeDeleteByID(like_db_id);
					 
							afChapterInfo.list_likes.remove(0);
						}
						sendUpdateLikeBroadcastList(afChapterInfo);
					}
					break;
				case Consts.REQ_BCMSG_COMMENT:
					if (user_data != null && user_data instanceof BaroadCast_ViewHolder) {
						BaroadCast_ViewHolder viewHolder = (BaroadCast_ViewHolder) user_data;
						AfChapterInfo afChapterInfo =(AfChapterInfo) viewHolder.lin_comment.getTag();
						afChapterInfo.list_comments.remove(0);
						afChapterInfo.total_comment --;
						viewHolder.bc_item.getTag();
						viewHolder.txt_comment.setText(afChapterInfo.total_comment+"");
						int c_db_id =   (Integer) viewHolder.txt_comment.getTag();
						mAfCorePalmchat.AfDBBCCommentDeleteByID(c_db_id);
					}
					break;
				case Consts.REQ_BCMSG_ACCUSATION:
				case Consts.REQ_BCMSG_DELETE:
				case Consts.REQ_BCCOMMENT_DELETE:
					if (baseActivity !=null ) {
						if (user_data != null && user_data instanceof AfChapterInfo){
							AfChapterInfo afChapterInfo = (AfChapterInfo) user_data;
							if(code == Consts.REQ_CODE_142 || code == Consts.REQ_CODE_201 || code == Consts.REQ_CODE_202) {
								 
								mAfCorePalmchat.AfDBBCChapterDeleteByID(afChapterInfo._id);
								sendUpdateDeleteBroadcastList(afChapterInfo);
							}
						}
						((BaseActivity) baseActivity).dismissProgressDialog();
						
					}
					break;
				}
				Consts.getInstance().showToast(mContext, code, flag, http_code);
			}
			
		}
	};

	class CommentModel{
		
		public CommentModel() {
			
		}
		private int position;
		private BaroadCast_ViewHolder holder;

		private String to_afid;
		private String msg;
		public void setTo_afid(String to_afid,String _to_sname) {
			if (to_afid.equals(myprofileInfo.afId)) {
				to_afid = "";
			}
			this.to_afid = to_afid;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public void setHint_msg(String hint_msg) {
			if (TextUtils.isEmpty(to_afid)) {
				hint_msg  = mContext.getString(R.string.hint_commet);
			}
		}
		public int getPosition() {
			return position;
		}
		
		public void setPosition(int position) {
			this.position = position;
		}
		
		public BaroadCast_ViewHolder getHolder() {
			return holder;
		}
		
		public void setHolder(BaroadCast_ViewHolder holder) {
			this.holder = holder;
		}
	}
 
	public void showAppDialog(String msg ,final int code, final AfChapterInfo afChapterInfo, final int position,final boolean isDelete){
		AppDialog appDialog = new AppDialog(mContext);
		appDialog.createConfirmDialog(mContext, msg, new OnConfirmButtonDialogListener() {
			@Override
			public void onRightButtonClick() {
					if (afChapterInfo != null) {
						if (baseActivity !=null ) {
							if(isDelete){
								baseActivity.showProgressDialog(R.string.deleting);
							}
							else {
								baseActivity.showProgressDialog(R.string.please_wait);
							}
						}
						mAfCorePalmchat.AfHttpBCMsgOperate(code, myprofileInfo.afId, "", afChapterInfo.mid, null, null, afChapterInfo,  afHttpResultListener);
					}
			}
			@Override
			public void onLeftButtonClick() {
				
			}
		});
		appDialog.show();
	}

@Override
public void onClikeLike(final BaroadCast_ViewHolder viewHolder, final AfChapterInfo afChapterInfo, int position) {
	// TODO Auto-generated method stub
	
	/*new Thread(new Runnable() {
		
		@Override
		public void run() {*/
			if(!viewHolder.txt_like.isSelected()){
					
			String time = String.valueOf( System.currentTimeMillis());
			int like_db_id = PalmchatApp.getApplication().mAfCorePalmchat.AfDBBCLikeInsert(Consts.DATA_BROADCAST_PAGE, afChapterInfo._id, "", time , myprofileInfo.afId,  AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));
//			PalmchatLogUtils.e(AdapterBroadcastMessages.class.getCanonicalName()+"_like", "mid="+afChapterInfo.mid);
			AfLikeInfo afLikeInfo = BroadcastUtil.toAfLikeinfo(like_db_id, afChapterInfo._id, "", time, myprofileInfo.afId, AfMessageInfo.MESSAGE_SENTING, AfProfileInfo.profileToFriend(myprofileInfo));
			afChapterInfo.list_likes.add(afLikeInfo);
			afChapterInfo.total_like ++;
			afChapterInfo.isLike = true;
			viewHolder.txt_like.setTag(like_db_id);
			viewHolder.lin_comment.setTag(afChapterInfo);
			sendUpdateLikeBroadcastList(afChapterInfo);
		
			PalmchatApp.getApplication().mAfCorePalmchat.AfHttpBCMsgOperate(Consts.REQ_BCMSG_AGREE, myprofileInfo.afId, "", afChapterInfo.mid, null, null, viewHolder, afHttpResultListener);
		/*	Message msg = new Message();
			msg.what = clikeLike;
			msg.obj = viewHolder;
			handler.sendMessage(msg);*/
			viewHolder.txt_like.setText(afChapterInfo .total_like+"");
			viewHolder.txt_like.setSelected(true);
			viewHolder.txt_like.setClickable(false);
			PalmchatApp.getApplication().getSoundManager().playInAppSound(SoundManager.IN_APP_SOUND_LIKE);
		
			}
			/*}
	}).start();*/
}
	
	@Override
	public void onClikeMore(DialogItem dialogItem) {
		if (VoiceManager.getInstance().isPlaying()) {
			VoiceManager.getInstance().pause();
		}

 	AfChapterInfo afChapterInfo = (AfChapterInfo)dialogItem.getObject();
		switch (dialogItem.getTextId()) {
			case R.string.delete:
				String msg = mContext.getString(R.string.bc_del);
				showAppDialog(msg, Consts.REQ_BCMSG_DELETE, afChapterInfo, dialogItem.getPositionId(), true);
				new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM);
				break;
			case R.string.report_abuse:
				String msgAbuse = mContext.getString(R.string.sure_report_abuse);
				showAppDialog(msgAbuse, Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, dialogItem.getPositionId(), false);
				break;
		}
	}

	/**
	 * 点击广播图片 查看大图
	 */
	@Override
	public void onBindLookePicture(BaroadCast_ViewHolder viewHolder , final AfChapterInfo afChapterInfo,final int position,final ImageView imageView, final int img_index) {
		// TODO Auto-generated method stub
 
				largeImageDialog = new LargeImageDialog(mContext ,  afChapterInfo.list_mfile , img_index, LargeImageDialog.TYPE_BROADCASTLIST, afChapterInfo);
				largeImageDialog.show();
				largeImageDialog.setILargeImageDialog(new ILargeImageDialog() { 
					@Override
					public void onItemClickeReportAbuse() {
						String msg = mContext.getString(R.string.sure_report_abuse);
						showAppDialog(msg,  Consts.REQ_BCMSG_ACCUSATION, afChapterInfo, position,false);
					}
					
					@Override
					public void onItemClickeDelete() { 
						new ReadyConfigXML().saveReadyInt(ReadyConfigXML.DEL_BCM);
//						MobclickAgent.onEvent(mContext, ReadyConfigXML.DEL_BCM);
						
						String msg = mContext.getString(R.string.bc_del);
						showAppDialog(msg,  Consts.REQ_BCMSG_DELETE, afChapterInfo, position,true);
					}
				}); 
	}
	
	
	
	@Override
	public void notifyData_VoiceManagerCompletion() {
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged_updateLikeBymid(AfChapterInfo afChapterInfo) {
		int index = ByteUtils.indexOfBroadCastListBymid(broadcastMessageList, afChapterInfo.mid);
		if (index > -1) {
			PalmchatLogUtils.e("like__", afChapterInfo.total_like+"");
			broadcastMessageList.set(index, afChapterInfo);
			notifyDataSetChanged();
		}
	}
	 
	 private void sendUpdateDeleteBroadcastList(AfChapterInfo afChapterInfo) {
			// TODO Auto-generated method stub
		 afChapterInfo.eventBus_action= Constants.UPDATE_DELECT_BROADCAST;
		 EventBus.getDefault().post(afChapterInfo);
	 }
	 
	 private   void sendUpdateLikeBroadcastList(AfChapterInfo afChapterInfo) {
		 // TODO Auto-generated method stub
		 /*Intent intent = new Intent(Constants.UPDATE_LIKE);
		 intent.putExtra(Constants.BROADCAST_MSG_OBJECT, afChapterInfo);
		 PalmchatApp.getApplication().sendBroadcast(intent);*/
		 afChapterInfo.eventBus_action=Constants.UPDATE_LIKE;
			EventBus.getDefault().post(afChapterInfo);
	} 
	 private IAdapterBroadcastProfile iAdapterBroadcastMessages_profile;
	public void setIAdapterBroadcastMessages_profile(IAdapterBroadcastProfile l){
		this.iAdapterBroadcastMessages_profile = l;
	}
	public interface IAdapterBroadcastProfile{
		public void on_showNoData();
	}
}
