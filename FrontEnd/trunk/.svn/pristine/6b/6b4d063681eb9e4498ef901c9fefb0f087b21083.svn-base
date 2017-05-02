package com.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.afmobi.palmchat.util.MessagesUtils;
import com.afmobi.palmchat.util.StartTimer;

public class AfMessageInfo implements Parcelable {

	/**************************************************************************************************/
	//message status
	/**************************************************************************************************/
	public final static int  MESSAGE_READ = 0x1;									/*已读*/
	public final static int  MESSAGE_UNREAD = (0x1 << 1);									/*未读*/
	public final static int  MESSAGE_READ_UNDOWNLOAD = (MESSAGE_READ | (0x1 << 2));    		/*已读但下载内容失�???*/
	public final static int  MESSAGE_READ_DOWNLOAD =  (MESSAGE_READ | (0x1 << 3));		 	/*已读且成功下载内�???*/
	public final static int  MESSAGE_STATUS_SEPERATOR = 0XFF;						/* < MESSAGE_STATUS_SEPERATOR: 接收消息*/	
	
	public final static int  MESSAGE_SENT =(0x1 << 8);								/*已发�???*/
	public final static int  MESSAGE_UNSENT = (0x1 << 9);							/*发�?�失�???*/
	public final static int  MESSAGE_SENTING = (0x1 << 10);          				/*正在发�??*/
	public final static int  MESSAGE_SENT_AND_READ = (0x1 << 11);          			 /*sent and read*/

	/**************************************************************************************************/
	//message type
	/**************************************************************************************************/	
	public final static int   MESSAGE_TYPE_MASK_GRP = (0x1 << 8);				//群信�???
	public final static int   MESSAGE_TYPE_MASK_PRIV = (0x1 << 9);				//私聊信息
	public final static int   MESSAGE_TYPE_MASK_CHATROOM = (0x1 << 10);			//聊天室信�???
	public final static int   MESSAGE_TYPE_SYSTEM = (0x1 << 11);			//聊天室信�???
	
	public final static int   MESSAGE_TYPE_MASK_MSG_TYPE = (0XFF00);	
	
	public final static int   MESSAGE_TYPE_MASK = (0XFF);	
	
	public final static int MESSAGE_TEXT = 0;    					/*文本消息*/
	public final static int MESSAGE_SHAKE = 1;    					/*震动信息*/
	public final static int MESSAGE_EMOTIONS = 2;					/*趣味表情 早就不要了*/
	public final static int MESSAGE_CARD = 3;						/*名片消息*/	
	public final static int MESSAGE_NOMAL = 4;						/*之前为普通信???*/
	public final static int MESSAGE_IMAGE = 5;						/*图片消息*/	
	public final static int MESSAGE_VOICE = 6;    					/*语音消息*/
	public final static int MESSAGE_LOCATION = 7;    				/*地理位置消息*/
	public final static int MESSAGE_FRIEND_REQ = 8;					/*好友请求消息*/
	public final static int MESSAGE_FRIEND_REQ_SUCCESS = 9;			/*请好友成*/
	public final static int MESSAGE_GRP_FRIEND_REQ	= 10; 		    //invited friend req success
	public final static int MESSAGE_STORE_EMOTIONS = 11;			//store emotions
	public final static int MESSAGE_FOLLOW = 15;			        //follow
	public final static int MESSAGE_BC50 = 16;			        //BC50
	public final static int MESSAGE_FLOWER = 18;			    //送花的通知消息(msg内容为送花数量)
	public final static int MESSAGE_PUBLIC_ACCOUNT = 12;		//公众帐号文字通知消息
	public final static int MESSAGE_PA_URL = 13;			    //公众帐号图文通知消息
	public final static int MESSAGE_PREDICT = 14;			    //PREDICT 消息
	public final static int MESSAGE_PAYMENT = 17;			    //PAYMENT 消息
	public final static int MESSAGE_LOTTERY =10;               //开奖通知 消息
	public final static int MESSAGE_PRIZE =19;                 //派彩通知 消息
	
	//群广播消
	public final static int MESSAGE_GRP_SYS_MIN = 20;
	public final static int MESSAGE_GRP_CREATE = 20;			//群主创建群成功之讯息广播
	public final static int MESSAGE_GRP_MODIFY = 21;			//群主修改群讯息之讯息广播
	public final static int MESSAGE_GRP_REMOVE_MEMBER = 22;		//群主移除群成员讯息之讯息广播
	public final static int MESSAGE_GRP_DESTROY = 23;			//群主移除群成员讯息之讯息广播	
	public final static int MESSAGE_GRP_ADD_MEMBER = 24;		//User请群成员之讯息广
	public final static int MESSAGE_GRP_DROP = 25;			    //User出群成功之讯息广	
	public final static int MESSAGE_GRP_HAVE_BEEN_REMOVED = 26;			    //User已被移出群成功之讯息广	
	public final static int MESSAGE_GRP_SYS_MAX = 26;
		
	//chatroom	 
//	public final static int MESSAGE_CHATROOM_MIN = 26;
	public final static int MESSAGE_CHATROOM_NORMAL = 26;		//一般讯息
	public final static int MESSAGE_CHATROOM_SYSTEM_ADD = 27;   //add 讯息
	public final static int MESSAGE_CHATROOM_SYSTEM_TOP	= 28;	//置顶讯息
	public final static int MESSAGE_CHATROOM_SYSTEM_ENTRY	= 29;	//用户进入聊天室的系统提示
	public final static int MESSAGE_CHATROOM_SYSTEM_AD = 30;        //Advertisement
	public final static int MESSAGE_CHATROOM_SYSTEM_AD_CANCEL = 31; //Advertisement CANCEL
	public final static int MESSAGE_CHATROOM_SYSTEM_INNER_AT = 32;   //chatting in room and receive
	//	public final static int MESSAGE_CHATROOM_MAX	= MESSAGE_CHATROOM_SYSTEM_AD_CANCEL;
	
	// payment
	public final static int MESSAGE_PAY_COIN_CHANGE = 100;   //B29 ,coin change
	public final static int MESSAGE_PAY_PAY_CONFIRM = 101;   //B30 ,pay confirm,暂不支持，V5.1.3无须处理
	
	// bc52 share tag&br msg
	public final static int MESSAGE_BROADCAST_SHARE_BRMSG = 102;   //B26 , broadcast share br msg
	public final static int MESSAGE_BROADCAST_SHARE_TAG = 103;   //B28 , broadcast share tag msg
	
	public final static int MESSAGE_PAY_SYS_NOTIFY = 104;   //B31 , pay system notify
	
	public int _id;
	public int type;  									//msg type
	public int status;									//msg status 
	public long client_time; 							//client time
	public long server_time; 							//server time
	public String msg; 									// msg content
	public String fromAfId; 							//send afid
	public String toAfId;								//receive afid
	
	public int attach_id;								//attach id
	public int unReadNum;								//unReadNum
	
	public Object attach;								//media\voice info etc.
	
	
	//chatroom 
	public String name;	
	public byte age;
	public byte sex;
	public String cname;
	
	//forward unique 
	public String fid;
	
	public int action = MessagesUtils.ACTION_INSERT;    
	public List<String> afidList = new ArrayList<String>();
	public boolean isTopMessage;
	
	public boolean accept_friend_defualt = false;
	
	public int retryCount_imgType=1;//add by wxl ,发送图片失败后 可以重试发的次数
	public AfMessageInfo(){
		
	}
	private void setAttach( Object attach){
		this.attach = attach;
	}
	public AfMessageInfo(String fromAfId){
		this.fromAfId = fromAfId;
	}
	
	public String getKey() {
		int temp = type & MESSAGE_TYPE_MASK_MSG_TYPE;
		
		if( status < MESSAGE_STATUS_SEPERATOR){//接收的信
			if( temp == MESSAGE_TYPE_MASK_CHATROOM || temp == MESSAGE_TYPE_MASK_GRP){
				return toAfId;
			}
			return fromAfId;
		}else{//发�?�数�?
			if( temp == MESSAGE_TYPE_MASK_CHATROOM ||  temp == MESSAGE_TYPE_MASK_GRP){
				return fromAfId;
			}
			return toAfId;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(_id);
		dest.writeInt(type);
		dest.writeInt(status);
		dest.writeLong(client_time);
		dest.writeLong(server_time);
		dest.writeString(msg);
		dest.writeString(fromAfId);
		dest.writeString(toAfId);
		dest.writeInt(attach_id);
		dest.writeInt(unReadNum);
		dest.writeValue(attach);
	}

	private StartTimer startTimer;
	public StartTimer getStartTimer() {
		return startTimer;
	}
	public void setStartTimer(StartTimer startTimer) {
		this.startTimer = startTimer;
	}
	
	
	public final Parcelable.Creator<AfMessageInfo> CREATOR = new Parcelable.Creator<AfMessageInfo>() {  
		@Override  
		public AfMessageInfo createFromParcel(Parcel source) {  
			AfMessageInfo afMessage = new AfMessageInfo();  
			afMessage._id = source.readInt();  
			afMessage.type = source.readInt();
			afMessage.status = source.readInt();
			afMessage.client_time = source.readLong();
			afMessage.server_time = source.readLong();
			afMessage.msg = source.readString();
			afMessage.fromAfId = source.readString();
			afMessage.toAfId = source.readString();
			afMessage.attach_id = source.readInt();
			afMessage.unReadNum = source.readInt();
			if(attach != null && attach instanceof AfAttachImageInfo){
				afMessage.attach = source.readValue(AfAttachImageInfo.class.getClassLoader());
			}else if(attach != null && attach instanceof AfAttachVoiceInfo){
				afMessage.attach = source.readValue(AfAttachVoiceInfo.class.getClassLoader());
			}else if(attach != null && attach instanceof AfAttachPAMsgInfo){
				afMessage.attach = source.readValue(AfAttachPAMsgInfo.class.getClassLoader());
			}
			return afMessage;  
		}

		@Override
		public AfMessageInfo[] newArray(int size) {
			return new AfMessageInfo[size];
		}
	};
			
	public static class ChatsComparator implements Comparator<AfMessageInfo> {

		@Override
		public int compare(AfMessageInfo arg0, AfMessageInfo arg1) {
			return arg0.toAfId.compareTo(arg1.toAfId);
		}
	}
	
	public byte mSex;
	public String mName;
	public String mAlias;
	public String headImagePath;
	public boolean autoPlay = false;
	public int autoPlayPosition = 0;
	
	public String getSerialFromHead() {
		if (headImagePath != null) {
			String [] ss = headImagePath.split(",");
			if (ss.length >= 2) {
				return ss[1];
			}
		}
		return "";
	}
	
	public String getServerUrl() {
		if (headImagePath != null) {
			String [] ss = headImagePath.split(",");
			if (ss.length >= 3) {
				return ss[2].replaceAll("/d", "");
			}
		}
		return "";
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "name  "+name +" age  "+age +"  sex  "+sex  + "  cname  "+cname
				+"  _id  "+_id +"  type  "+type+"  status  "+status  +"  msg  "+msg 
				+"  client_time  "+client_time + "  server_time  "+server_time
				+"  fromAfId  "+fromAfId +"  toAfid  "+toAfId;
	}
	
}


