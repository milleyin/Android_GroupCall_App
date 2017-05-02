package com.afmobi.palmchat.ui.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.logic.MsgAlarmManager;
import com.afmobi.palmchat.logic.ReadyConfigXML;
import com.afmobi.palmchat.ui.activity.login.LoginActivity;
import com.afmobi.palmchat.util.CommonUtils;
import com.afmobi.palmchat.util.SettingMode;
import com.core.AfLoginInfo;
import com.core.AfPalmchat;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
import com.core.listener.AfHttpSysListener;
//import com.umeng.analytics.MobclickAgent;

import java.io.IOException;


/**
 * 声音设置界面
 *
 */
public class SoundSettingActivty extends BaseActivity  {

	/**声音列表*/
    private String[] mStrArr_Name=null;
	/**显示列表*/
	private ListView mListView;
	/**适配器*/
    private MyBaseAdapter mAdapter;
    /**音频播放器*/
    private MediaPlayer mMediaPlayer;
    /**当前选中项*/
    public int m_current_select_id;
    /***/
    private boolean mBln_Mute; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ReadyConfigXML().saveReadyInt(ReadyConfigXML.ENTRY_SOUND);
//        MobclickAgent.onEvent(context, ReadyConfigXML.ENTRY_SOUND);
    }

    @Override
    public void findViews() {
        SettingMode settingMode = new SettingMode(getApplication());
        settingMode.getSettingValue();
        app.setSettingMode(settingMode);
      
        setContentView(R.layout.activity_sound_setting);
        mListView=(ListView)findViewById(R.id.listview);

        LinearLayout tmp_title_layout =  (LinearLayout)findViewById(R.id.sound_title);

        TextView tmp_title  =(TextView )tmp_title_layout.findViewById(R.id.title_text);
        ImageView back_button =(ImageView)tmp_title_layout.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        tmp_title.setText(getResources().getString(R.string.mute_btn));
        mBln_Mute = app.getSettingMode().isMute();


    }

    @Override
    public void init()
    {

        mMediaPlayer = new MediaPlayer();
        mStrArr_Name = app.getSettingMode().getToneNameArr();
        mAdapter = new MyBaseAdapter(this,mStrArr_Name );
        mListView.setAdapter(mAdapter);
        m_current_select_id=get_store_id();
        mAdapter.setCurrentID(m_current_select_id);
        mAdapter.setMute(mBln_Mute);
        mAdapter.notifyDataSetChanged();
        set_list_ItemClickListener();
    }
    
    /**
     * 获取当前选择的id
     * @return int
     */
	private int get_store_id()
	{
	    String id_str = app.getSettingMode().getToneId();

	    return Integer.valueOf(id_str);
	}

	/**
	 * item点击监听
	 */
	private void set_list_ItemClickListener()
	{
	    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
	    {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	            if(position == m_current_select_id){
	            	if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
	            		mMediaPlayer.stop();
	                    return;
	                }
	            }
	            
	            {

	                if(position==0)//system default
	                {
	                	//gtf 2014-11-16
	                	new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_SYSTEM);
//	                	MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_SYSTEM);
	                	
	                    mAdapter.setCurrentID(position);
	                    mAdapter.notifyDataSetChanged();
	                    String system_tone_name = Settings.System.getString(getContentResolver(),Settings.System.NOTIFICATION_SOUND);

	                    if(TextUtils.isEmpty(system_tone_name)){
	                    	 m_current_select_id=position;
	                         Integer it = m_current_select_id;//position-1;
	                         app.getSettingMode().setToneId(it.toString());
	                    	return;
	                    }
	                    if(mMediaPlayer.isPlaying()){
	                    	mMediaPlayer.stop();
	                    }
	                    mMediaPlayer.reset();
	                    try {
	                        mMediaPlayer.setDataSource(SoundSettingActivty.this, Uri.parse(system_tone_name));
	                        mMediaPlayer.prepare();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    mMediaPlayer.start();
	                    m_current_select_id=position;
	                    Integer it = m_current_select_id;//position-1;
	                    app.getSettingMode().setToneId(it.toString());
	                }
	                else
	                {
	                    mAdapter.setCurrentID(position);
	                    mAdapter.notifyDataSetChanged();
	                    String header = "tone";//context.getResources().getString(R.string.tone_header);
	                    int pos = position;//position-1;
	                    dian(pos);
	                    int tmp_id = SoundSettingActivty.this.getResources().getIdentifier(header+pos, "raw", getApplication().getPackageName());
	                    if(mMediaPlayer.isPlaying()){
	                        mMediaPlayer.stop();
	                    }
	                    mMediaPlayer.reset();
	                    mMediaPlayer=null;
	                    PalmchatLogUtils.println("tmp_id "+tmp_id);
	                    mMediaPlayer= MediaPlayer.create(SoundSettingActivty.this, tmp_id);
	                    if(null != mMediaPlayer){
	                    	mMediaPlayer.start();
	                    	m_current_select_id=position;
	                    	Integer it =pos;
	                    	app.getSettingMode().setToneId(it.toString());
	                    }
	                }
	
	            }
	        }
	    });
	}

	private void dian(int pos) {
		switch (pos) {
		case 1:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_DRUM);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_DRUM);
			break;
		case 2:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_BLOOM);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_BLOOM);
			break;
		case 3:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_BUBBLE);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_BUBBLE);
			break;
		case 4:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_GLASS);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_GLASS);
			break;
		case 5:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_SHORTMSG);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_SHORTMSG);
			break;
		case 6:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_SHORTMSG2);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_SHORTMSG2);
			break;
		case 7:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_TWEET);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_TWEET);
			break;
		case 8:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_BUBBLE2);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_BUBBLE2);
			break;
		case 9:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_DROP);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_DROP);
			break;
		case 10:
			new ReadyConfigXML().saveReadyInt(ReadyConfigXML.SOUND_FRESH);
//			MobclickAgent.onEvent(context, ReadyConfigXML.SOUND_FRESH);
			break;
		default:
			break;
		}
	}

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
       
    }

    private void toLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
  
 


    /**
     * 适配器类
     *
     */
    class MyBaseAdapter extends BaseAdapter
    {	
    	/**名字组*/
        String[] m_name_array;
        /**总数*/
        int m_count;
        /**布局加载器*/
        LayoutInflater m_inflater;
        /**上下文*/
        Context m_context;
        /**当前选中id*/
        int  m_currentID=-1;
        /***/
        boolean  m_b_mute=true;
        
        MyBaseAdapter(Context contexts,String[] name_array) {
            m_inflater=LayoutInflater.from(contexts);
            m_name_array = name_array;
            m_count=m_name_array.length;
            m_context = contexts;
        }

        @Override
        public int getCount()
        {
            return m_count;
        }
        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView , ViewGroup parent)
        {
               return get_tone_item_view(position, convertView);
        }
        
        /**
         * 
         * @param position
         * @param convertView
         * @return
         */
        private View get_tone_item_view(int position, View convertView )
        {
            HolderItem myHolder;
            if (convertView==null)
            {
                myHolder=new HolderItem();
                convertView= m_inflater.inflate(R.layout.tone_picker_item, null);
                if(convertView!=null)
                {
                    myHolder.text=(TextView)convertView.findViewById(R.id.name);
                    myHolder.text.setTextSize(15);
                    myHolder.image = convertView.findViewById(R.id.radio_select);
                    myHolder.viewTop = convertView.findViewById(R.id.view_top);
                    myHolder.viewBottom = convertView.findViewById(R.id.view_Bottom);

                    convertView.setTag(myHolder);
                }
            }
            else
            {
                myHolder=(HolderItem)convertView.getTag();
            }
            if (position == 0) {
            	myHolder.viewTop.setVisibility(View.VISIBLE);
			}else {
				myHolder.viewTop.setVisibility(View.GONE);
			}
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,1);
	        Log.e("position", position+"");
	        if (position == (m_count - 1)) {
	        	params.setMargins(0, 0, 0, 0);
	        	myHolder.viewBottom.setLayoutParams(params);
			}else {
				params.setMargins(18, 0, 0, 0);
				myHolder.viewBottom.setLayoutParams(params);
			}
            
            if(position==m_currentID) {
            	myHolder.image.setBackgroundResource(R.drawable.radiobutton_press);
            }                
            else {
            	myHolder.image.setBackgroundResource(R.drawable.radiobutton_normal);
            }
        	myHolder.text.setText(m_name_array[position]);
            return convertView;
        }


        public void setCurrentID(int currentID) {
            this.m_currentID = currentID;
        }

        public void setMute(boolean b_mute)
        {
            m_b_mute=b_mute;
        }
        
        public boolean getMute()
        {
            return  m_b_mute;
        }

        class HolderItem{
            View image;
            TextView text;
            View viewTop;
            View viewBottom;
        }
    }

}
