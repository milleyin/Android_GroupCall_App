package com.afmobi.palmchat.ui.activity.palmcall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afmobi.palmchat.util.DateUtil;
import com.afmobigroup.gphone.R;
import com.core.AfResponseComm;

import java.util.ArrayList;

/**
 * Created by mqy on 2016/7/5.
 */
public class DaysListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList <String> weekdays_list;
    String select_list;
    class ViewHolder {

        TextView tvName;
        RadioButton rb_state;
    }
    @Override
    public int getCount() {
        return weekdays_list.size();
    }


    public DaysListAdapter(Context context,ArrayList <String> _weekdays_list,String _select_list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        weekdays_list=_weekdays_list;
        select_list=_select_list;
    }

    @Override
    public Object getItem(int position) {

        return weekdays_list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.palmcall_setting_dayslist_item, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_device_days_name);
            holder.rb_state =(RadioButton) convertView.findViewById(R.id.rb_light);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String country = weekdays_list.get(position).toString();/**�еĵ���������������Ŀ��ܻ��пո����԰ѿո�ȥ��*/
        holder.tvName.setText(country);
        holder.rb_state.setFocusable(false);
        holder.rb_state.setId(position);
        holder.rb_state.setChecked(select_list.contains(country));
        return convertView;

    }
}
