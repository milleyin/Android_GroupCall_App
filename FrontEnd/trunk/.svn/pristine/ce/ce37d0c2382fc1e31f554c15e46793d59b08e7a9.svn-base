package com.afmobi.palmchat.util;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.core.Consts;
import com.core.cache.CacheManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;


public class SettingMode {
	
	private boolean backup;
	private boolean mute;
	private boolean inAppSound=true;
	private boolean vibratio;
	private boolean newMsgNotice = true;
	private boolean showNewMsgDetail = true;
	private boolean isPopMsg;
	private boolean notification;
	private Context mContext;
    private String m_tone_id;
	private String[] m_tone_name_array;
	private int newMsgDisturb;
	
	public SettingMode(Context context) {
		//super();
		this.mContext = context;
        get_tone_name_array();
	}
	public boolean isBackup() {
		return backup;
	}
	public void setBackup(boolean backup) {
		this.backup = backup;
	}
	public boolean isMute() {
		return mute;
	}
	public void setMute(boolean mute) {
		this.mute = mute;
	}
	public boolean isInAppSound() {
		return inAppSound;
	}
	public void setInAppSound(boolean inAppSound) {
		this.inAppSound = inAppSound;
	}	
	public boolean isVibratio() {
		return vibratio;
	}
	public void setVibratio(boolean vibratio) {
		this.vibratio = vibratio;
	}
	public boolean isNewMsgNotice() {
		return newMsgNotice;
	}
	public void setNewMsgNotice(boolean newMsgNotice) {
		this.newMsgNotice = newMsgNotice;
	}
	public boolean isShowNewMsgDetail() {
		return showNewMsgDetail;
	}
	public void setShowNewMsgDetail(boolean showNewMsgDetail) {
		this.showNewMsgDetail = showNewMsgDetail;
	}
	public int getMsgDisturb() {
		return newMsgDisturb;
	}
	public void setMsgDisturb(int newMsgDisturb) {
		this.newMsgDisturb = newMsgDisturb;
	}
	public boolean isPopMsg() {
		return isPopMsg;
	}
	public void setPopMsg(boolean isPopMsg) {
		this.isPopMsg = isPopMsg;
	}
	public boolean isNotification() {
		return notification;
	}
	public void setNotification(boolean notification) {
		this.notification = notification;
	}



	public void getSettingValue() {
		SharedPreferences sp = mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
		setBackup(sp.getBoolean("backup", false));
        String str_afid = CacheManager.getInstance().getMyProfile().afId;
		boolean isSoundValue = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbSettingGetSoundOrVibrate(str_afid ,true);
		boolean isNewNotice = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbOnoffNofitymsg(str_afid ,true,false);
		int isNoDisturb = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbAvoidDisturb(str_afid ,Consts.AVOID_DISTURB_OFF,false);
		boolean isShowDetail = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbOnoffDetailmsg(str_afid ,true,false);
		boolean isVibrateValue = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbSettingGetSoundOrVibrate(str_afid ,false);
		boolean isPopMsg = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbPopUpOpr(str_afid, false, false);
		
        //        change by chengyu 2014-3-21 10:30:30
        m_tone_id = ((PalmchatApp)mContext).mAfCorePalmchat.AfDbSettingVoiceTypeOpr(str_afid ,null,false);
        if(m_tone_id==null)
            m_tone_id="0";
        
        boolean isInAppsound=SharePreferenceUtils.getInstance((PalmchatApp)mContext).getInAppSound(str_afid);
        setInAppSound(isInAppsound);
		setMute(!isSoundValue);
		setVibratio(isVibrateValue);
		setNewMsgNotice(isNewNotice);
		setShowNewMsgDetail(isShowDetail);
		setMsgDisturb(isNoDisturb);
		PalmchatLogUtils.println("---ssssddd isPopMsg:" + isPopMsg);
		
		setPopMsg(isPopMsg);
		
		setNotification(sp.getBoolean("notification", true));
	}


    /**
     *       added by chengyu
     */
    public String getToneId()
    {
        return m_tone_id;
    }

    public void setToneId(String id)
    {
        String str_afid = CacheManager.getInstance().getMyProfile().afId;
        ((PalmchatApp)mContext).mAfCorePalmchat.AfDbSettingVoiceTypeOpr(str_afid ,id,true);
        m_tone_id = id;
    }
	public void setSettingValue() {
		SharedPreferences sp = mContext.getSharedPreferences("setting", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("backup", backup);
		editor.putBoolean("mute", mute);
		editor.putBoolean("inAppSound", inAppSound); 
		editor.putBoolean("vibratio", vibratio);
		editor.putBoolean("notification", notification);
		editor.commit();
	}


    public String[] getToneNameArr()
    {
        return m_tone_name_array;
    }


    private void get_tone_name_array()
    {

        ToneXmlAdapter tone_adapter = new ToneXmlAdapter(mContext);
        XmlParser xmlParser = new XmlParser(tone_adapter);
        xmlParser.parseFromAssets(mContext, "tone_select.xml");


        int m = tone_adapter.getCount();
        m_tone_name_array = new String[m+1]; // add 1 as system defalut tone
        m_tone_name_array[0]=mContext.getResources().getString(R.string.default_tone);

        for(int i =0; i<m; i++)
        {
            ToneXmlData tmp = tone_adapter.getItem(i);
            m_tone_name_array[i+1]=tmp.song_name;

        }

    }



    /**
     *
     *          Class Declear ToneXmlAdapter
     *
     */
    class ToneXmlAdapter implements XmlParserData
    {
        ArrayList<ToneXmlData> m_list=new ArrayList<ToneXmlData>();
        ToneXmlData m_data=null;
        String Tag="Tone";

        Context context;
        public ToneXmlAdapter(Context mContext) {
			// TODO Auto-generated constructor stub
        	context = mContext;
		}



		/**
         *          Implements  Method
         */
        public void startDoc(XmlPullParser xpp,String nodeName)
        {
            /*printf("\n\n");
            printf("Adapter : startDoc: nodeName=%",nodeName);*/
        }



        public void dataParse(XmlPullParser xpp,String nodeName)
        {
//            printf("Adapter : dataParse: nodeName=%",nodeName);
            String packageName = mContext.getPackageName();
            if(nodeName!=null && nodeName.equals(Tag))
                m_data = new ToneXmlData();


            int len = xpp.getAttributeCount();
            for(int i =0 ;i<len;i++)
            {
                String name = xpp.getAttributeName(i);
                if(name!=null && name.equals("id")){
                    String str = xpp.getAttributeValue(i);
                    m_data.m_id = Integer.valueOf(str);

                }
                else if(name!=null && name.equals("name")){
                    String str = xpp.getAttributeValue(i);
                    int rid = context.getResources().getIdentifier(str, "string", packageName);
                    String soundOfMessage = context.getResources().getString(rid);
                    PalmchatLogUtils.println("soundOfMessage  "+soundOfMessage);
                    m_data.song_name = soundOfMessage;

                }
                else if(name!=null && name.equals("ext")){
                    String str = xpp.getAttributeValue(i);
                    m_data.song_extision=str;

                }
            }
        }




        public void endParse(String nodeName)
        {

            if(nodeName!=null && nodeName.equals(Tag))
            {

                m_list.add(m_data);
                m_data=null;
            }
        }


        public void endDoc()
        {
//                int len= m_list.size();
//                printf("endDoc: printAll:"+len);
//                for (ToneXmlData c : m_list) {
//                    printf("&&&&----" + c);
//                }
        }


        public ToneXmlData getItem(int id)
        {
            return m_list.get(id);
        }

        public int getCount()
        {
            return m_list.size();
        }
    }


    /**
     *
     *      Class Declear:  ToneXmlData
     *
     */
    class ToneXmlData
    {
        int m_id;
        String song_name;
        String song_extision;

        @Override
        public String toString()
        {
            return String.format("id:%d,name:%s,ext:%s",m_id,song_name,song_extision);
        }
    }
}	

