package com.afmobi.palmchat.ui.activity.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heguiming on 2016/1/19.
 */
public class AreaListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<AfResponseComm.AfBCRegionBroadcast> beans;
    // ���ڼ�¼ÿ��RadioButton��״̬������ֻ֤��ѡһ��
    private int checkedIndex = -1;
    class ViewHolder {

        TextView tvName,tvStateName,tyPostNumName;
        RadioButton rb_state;
    }

    public AreaListAdapter(Context context, ArrayList<AfResponseComm.AfBCRegionBroadcast> default_list) {
        // TODO Auto-generated constructor stub
        this.beans = default_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // ҳ��
        final ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.broadcast_arealist_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_device_country_name);
            holder.tvStateName = (TextView)convertView.findViewById(R.id.tv_device_state_name);
            holder.tyPostNumName = (TextView)convertView.findViewById(R.id.tv_device_postnum_name);
            holder.rb_state =(RadioButton) convertView.findViewById(R.id.rb_light);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String country = beans.get(position).country;
        holder.tvName.setText(country);
        String state = beans.get(position).state;
        holder.tvStateName.setText(state);
        holder.tyPostNumName.setText("("+beans.get(position).post_num+")");

        holder.rb_state.setFocusable(false);
        holder.rb_state.setId(position);
        holder.rb_state.setChecked(position == checkedIndex);
			/*holder.rb_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						if(checkedIndex != -1){
							int chece = checkedIndex - BroadcastFragment.mAreaListView.getFirstVisiblePosition();
							if(chece >= 0){
								View item  = BroadcastFragment.mAreaListView.getChildAt(chece);
								if(item != null){
									RadioButton rb3 =(RadioButton)item.findViewById(checkedIndex);
									if(rb3 != null){
										rb3.setChecked(false);
									}
								}
							}
						}
                        checkedIndex = buttonView.getId();
					}
				}
			});*/
        return convertView;
    }

}
