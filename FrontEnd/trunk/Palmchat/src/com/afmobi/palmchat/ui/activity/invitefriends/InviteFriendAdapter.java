package com.afmobi.palmchat.ui.activity.invitefriends;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.util.ToastManager;
import com.core.AfFriendInfo;
import com.core.cache.CacheManager;
//import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by hj on 2015/11/20.
 */
public class InviteFriendAdapter extends BaseAdapter {
    private Context mContext;
    private List<AfFriendInfo> mList;

    public InviteFriendAdapter(Context context, List<AfFriendInfo> list) {
        mContext = context;
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AfFriendInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder1;

        if(convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.invite_friends_item, null);
            holder1 = new ViewHolder();
//			holder1.icon = (ImageView) arg3.findViewById(R.id.icon);
//			holder1.gender = (ImageView) arg3.findViewById(R.id.gender);
            holder1.title = (TextView) convertView.findViewById(R.id.title);
            holder1.content = (TextView) convertView.findViewById(R.id.content);
            holder1.add_btn = (ImageView) convertView.findViewById(R.id.add_btn);

            convertView.setTag(holder1);

        } else {
            holder1 = (ViewHolder)convertView.getTag();
        }
        final AfFriendInfo contacts  = mList.get(position);
        holder1.title.setText(contacts.name);
        holder1.content.setText(contacts.user_msisdn);
        holder1.add_btn.setBackgroundResource(R.drawable.invite_friend_sendsms);

        //btn add/invite
        holder1.add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

         /*       final AppDialog appDialog = new AppDialog(mContext);
                String title = mContext.getString(R.string.input_name);
                final String inputMsg = CacheManager.getInstance().getMyProfile().name;

                appDialog.createEditDialog(mContext, title, inputMsg,  new AppDialog.OnInputDialogListener() {
                    @Override
                    public void onRightButtonClick(String inputStr) {
                        if(inputStr == null || "".equals(inputStr)) {
                            ToastManager.getInstance().show(mContext, mContext.getString(R.string.input_name_note));
                            return;
                        }*/

                        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.PB_T_INVITE);
//                        MobclickAgent.onEvent(mContext, ReadyConfigXML.PB_T_INVITE);

                        Uri smsUri = Uri.parse("smsto:" + contacts.user_msisdn);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                        intent.putExtra("sms_body", mContext.getString(R.string.default_invite_sms).replace("*", CacheManager.getInstance().getMyProfile().name).replace("-", CacheManager.getInstance().getMyProfile().showAfid()));
                        mContext.startActivity(intent);
       /*             }

                    @Override
                    public void onLeftButtonClick() {

                    }
                });
                appDialog.show();*/
            }

        });

        return convertView;
    }
    private void setFriendInfos(List<AfFriendInfo> friendInfos) {
        if (null != friendInfos) {
            mList.clear();
            this.mList.addAll(friendInfos);
        }
    }

    public void notifyDataSetChanged(List<AfFriendInfo> friendInfos) {
        setFriendInfos(friendInfos);
        notifyDataSetChanged();
    }
    /**
     * group holder
     * @author afmobi
     *
     */
    private class ViewHolder {
        TextView title;
        TextView content;
        ImageView add_btn;

    }
}
