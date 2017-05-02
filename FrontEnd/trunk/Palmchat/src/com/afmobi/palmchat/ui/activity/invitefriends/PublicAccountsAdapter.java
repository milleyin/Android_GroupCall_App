package com.afmobi.palmchat.ui.activity.invitefriends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.BaseFragmentActivity;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.EventRefreshFriendList;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.util.DialogUtils;
import com.afmobi.palmchat.util.EmojiParser;
import com.afmobi.palmchat.util.ImageUtil;
import com.afmobi.palmchat.util.ToastManager;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.afmobi.palmchat.util.universalimageloader.core.ImageManager;
import com.core.AfFriendInfo;
import com.core.AfPalmchat;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public class PublicAccountsAdapter extends BaseAdapter implements AfHttpResultListener {
    private Context mContext;
    private List<AfFriendInfo> mList;
    //	private ListViewAddOn listViewAddOn = new ListViewAddOn();
    private int type;
    private AfPalmchat mAfCorePalmchat;
    private DialogUtils mDialog;
    public static final int LAST_ENTRY = 1;
    public static final int COMMON_ENTRY = 2;
    private HashMap<String, Integer> mHashMap = new HashMap<String, Integer>();

    public PublicAccountsAdapter(Context context, List<AfFriendInfo> list) {
        mAfCorePalmchat = PalmchatApp.getApplication().mAfCorePalmchat;
        mDialog = new DialogUtils();
        mContext = context;

        mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (LAST_ENTRY == type) {
            return mList.get(position);
        }
        int pos = --position;
        if (pos < 0) return null;
        return mList.get(pos);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        if (LAST_ENTRY == type) {
            return position;
        }
        return --position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.public_account_list_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.wraphead);
            holder.followBtn = (TextView) convertView.findViewById(R.id.follow);
            holder.name = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.linefault_view = convertView.findViewById(R.id.linefault_view);
            holder.linefull_view = convertView.findViewById(R.id.linefull_view);
            holder.followed = (TextView) convertView.findViewById(R.id.followed);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.followBtn.setVisibility(View.VISIBLE);
        holder.followed.setVisibility(View.GONE);
        bintSetLine(holder, position);
        final AfFriendInfo friendInfo = mList.get(position);

        if (friendInfo.isFollow) {
            holder.followBtn.setVisibility(View.GONE);
            holder.followed.setVisibility(View.VISIBLE);

        } else {
            holder.followBtn.setVisibility(View.VISIBLE);
            holder.followed.setVisibility(View.GONE);
            holder.followBtn.setFocusable(true);
            holder.followBtn.setText(R.string.follow);
            holder.followBtn.setSelected(false);
            addListener(holder, friendInfo);
        }

        holder.name.setText(friendInfo.name);

        String sign = friendInfo.signature == null ? "" : friendInfo.signature;
        CharSequence text = EmojiParser.getInstance(mContext).parse(sign, ImageUtil.dip2px(mContext, 18));

        holder.content.setText(text);

        //wxl 20151012调UIL的显示头像方法, 替换原来的AvatarImageInfo.让图片管理更好
        ImageManager.getInstance().DisplayAvatarImage(holder.icon, friendInfo.getServerUrl(),
                friendInfo.getAfidFromHead(), Consts.AF_HEAD_MIDDLE, friendInfo.sex, friendInfo.getSerialFromHead(), null);
        /*AvatarImageInfo imageInfo = new AvatarImageInfo(friendInfo.getAfidFromHead(), friendInfo.getSerialFromHead(), friendInfo.name,
                Consts.AF_HEAD_MIDDLE, friendInfo.getServerUrl(), friendInfo.sex);
		ImageLoader.getInstance().displayImage(holder.icon, imageInfo, listViewAddOn.isSlipping());*/

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetails(friendInfo, position);
            }
        });

        return convertView;

    }

    private void toDetails(AfFriendInfo info, int position) {
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_PBL_PF);
//		MobclickAgent.onEvent(mContext, ReadyConfigXML.ENTRY_PBL_PF);
        Intent intent = new Intent(mContext, PublicAccountDetailsActivity.class);
        intent.putExtra("Info", info);
        mHashMap.put(info.afId, position);
        mContext.startActivity(intent);
    }

    private void addListener(ViewHolder holder, final AfFriendInfo friendInfo) {
        holder.followBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//				follow public account
                new ReadyConfigXML().saveReadyInt(ReadyConfigXML.FOL_PBL);
//				MobclickAgent.onEvent(mContext, ReadyConfigXML.FOL_PBL);

                showDialog(mContext);
                mAfCorePalmchat.AfHttpFriendOpr(null, friendInfo.afId,
                        Consts.HTTP_ACTION_A, Consts.FRIENDS_PUBLIC_ACCOUNT,
                        Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT, null, friendInfo,
                        PublicAccountsAdapter.this);
            }
        });
    }


    private class ViewHolder {
        ImageView icon;
        TextView followBtn;
        TextView followed;
        TextView name;
        TextView content;
        View linefault_view;
        View linefull_view;
    }

    private void bintSetLine(ViewHolder viewHolder, int position) {
        if (position == (getCount() - 1)) {
            viewHolder.linefault_view.setVisibility(View.GONE);
            viewHolder.linefull_view.setVisibility(View.VISIBLE);
        } else {
            viewHolder.linefault_view.setVisibility(View.VISIBLE);
            viewHolder.linefull_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        if (code == Consts.REQ_CODE_SUCCESS) {

            switch (flag) {
                case Consts.REQ_FRIEND_LIST:

                    AfFriendInfo info = (AfFriendInfo) user_data;
                    info.type = Consts.AFMOBI_FRIEND_TYPE_PFF;
                    CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).saveOrUpdate(info, true, true);

                    if (!TextUtils.isEmpty(info.afId)) {
                        if (null != mList) {
                            int size = mList.size();
                            for (int i = 0; i < size; i++) {
                                AfFriendInfo itemInfo = mList.get(i);
                                if (info.afId.equals(itemInfo.afId)) {
                                    ToastManager.getInstance().show(mContext,
                                            mContext.getString(R.string.public_follow_success));
                                    info.isFollow = true;
                                    break;
                                }
                            }
                        }
                    }

//				ToastManager.getInstance().show(mContext,
//						mContext.getString(R.string.add_friend_req));
                    notifyDataSetChanged();
                    dismissDialog(mContext);
//				mContext.sendBroadcast(new Intent(Constants.REFRESH_FRIEND_LIST_ACTION));
                    EventBus.getDefault().post(new EventRefreshFriendList());
                    break;

                default:
                    break;
            }

        } else {

            dismissDialog(mContext);
            ToastManager.getInstance().show(mContext,
                    mContext.getString(R.string.req_failed));
        }


    }

    private void showDialog(Context context) {
        if (context instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) context;
            activity.showProgressDialog(R.string.following);
        } else if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            activity.showProgressDialog(R.string.following);
        } else {
            mDialog.showCustomProgressDialog(context, context.getString(R.string.following));
        }
    }

    private void dismissDialog(Context context) {
        if (context instanceof BaseFragmentActivity) {
            BaseFragmentActivity activity = (BaseFragmentActivity) context;
            activity.dismissProgressDialog();
        } else if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            activity.dismissProgressDialog();
        } else {
            mDialog.dismissCustomProgressDialog();
        }
    }

}
