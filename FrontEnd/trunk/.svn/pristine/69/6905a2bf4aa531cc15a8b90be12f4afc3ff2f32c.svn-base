package com.core;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.util.CommonUtils;

public class AfGrpNotifyMsg {
	
	public final static int   MODIFY_TYPE_SIGN = (0X1);			//change group sign	
	public final static int   MODIFY_TYPE_NAME = (0X1 << 1);		//change group name
	public final static int   MODIFY_TYPE_SIGN_NAME = (MODIFY_TYPE_NAME | MODIFY_TYPE_SIGN);		//change group name and sign

	public final static int   BEEN_REMOVED = -22;		//you have been removed
	
	public int type;
	public int modify_type;  //修改的类型
	public String gname;
	public String gid;
	public String afid;
	public String name;
	public String sign;
	public int  gver;
	public List<String> users_name;
	public List<String> users_afid;
	
	public AfGrpNotifyMsg(){
		
	}
	
	//native used
	private void set(String gname, String gid, String afid, String name, String sign, String []users_name, String []users_afid,
			int gver, int type, int modify_type){
		this.gname = gname;
		this.gid = gid;
		this.afid = afid;
		this.name = name;
		this.gver = gver;
		this.type = type;
		this.sign = sign;
		this.modify_type = modify_type;
		if( null != users_name && null != users_afid){
			this.users_name =  Arrays.asList(users_name);
			this.users_afid =  Arrays.asList(users_afid);
		}
	}
	
	
	private void set(String gname, String gid, String afid, String name, String sign, String []users_name, 
			int gver, int type, int modify_type){
		this.gname = gname;
		this.gid = gid;
		this.afid = afid;
		this.name = name;
		this.gver = gver;
		this.type = type;
		this.sign = sign;
		this.modify_type = modify_type;
		if( null != users_name){
			this.users_name =  Arrays.asList(users_name);
		}
	}
	
	
	public static AfGrpNotifyMsg getAfGrpNotifyMsg(String gname, String gid, String afid, String name, String sign, String []users_name, String []users_afid,
			int gver, int type, int modify_type){
		AfGrpNotifyMsg afGrpNotifyMsg = new AfGrpNotifyMsg();
		afGrpNotifyMsg.set(gname, gid, afid, name, sign, users_name, gver, type, modify_type);
		return afGrpNotifyMsg;
	}

	
	public static AfMessageInfo toAfMessageInfo(AfGrpNotifyMsg afGrpNotifyMsg,PalmchatApp context) {
		// TODO Auto-generated method stub
		String gname = afGrpNotifyMsg.gname;
		String gid = afGrpNotifyMsg.gid;
		String afid = afGrpNotifyMsg.afid;
		String name = afGrpNotifyMsg.name;
		int notifyType = afGrpNotifyMsg.modify_type;
		String sign = afGrpNotifyMsg.sign;
		List<String> users_name = afGrpNotifyMsg.users_name;
		//List<String> users_afid= afGrpNotifyMsg.users_afid;
		//int gver = afGrpNotifyMsg.gver;
		int type = afGrpNotifyMsg.type | AfMessageInfo.MESSAGE_TYPE_MASK_GRP;
		AfMessageInfo afMessageInfo = new AfMessageInfo();
		afMessageInfo.fromAfId = afid;
		afMessageInfo.toAfId = gid;
		
		afMessageInfo.client_time = System.currentTimeMillis();
		afMessageInfo.name = gname;
		afMessageInfo.type = type;
		switch (afGrpNotifyMsg.type) {
		case AfMessageInfo.MESSAGE_GRP_CREATE://群创建
//			afMessageInfo.msg = name + " has invited "+getAppendUserName(users_name)+".";
			String msg = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_create));
			msg = CommonUtils.replace(DefaultValueConstant.TARGET_MEMBERS, getAppendUserName(users_name), msg);
			afMessageInfo.msg = msg;
			break;
		case AfMessageInfo.MESSAGE_GRP_ADD_MEMBER://群添加成员
//			afMessageInfo.msg = name + " has invited "+getAppendUserName(users_name)+".";
			String msgAdd = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_add_members));
			msgAdd = CommonUtils.replace(DefaultValueConstant.TARGET_MEMBERS, getAppendUserName(users_name), msgAdd);
			afMessageInfo.msg = msgAdd;
			break;
		case AfMessageInfo.MESSAGE_GRP_FRIEND_REQ://群好友请求
			
			break;
		case AfMessageInfo.MESSAGE_GRP_MODIFY://群更改
			if(AfGrpNotifyMsg.MODIFY_TYPE_NAME == notifyType){
//				afMessageInfo.msg = name + " changed the group name to "+gname+".";
				String msgModifyName = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_modify_name));
				if(TextUtils.isEmpty(gname)){
					msgModifyName = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, " ", msgModifyName);
				}
				else {
					msgModifyName = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, gname, msgModifyName);
				}

				afMessageInfo.msg = msgModifyName;
			}else if(AfGrpNotifyMsg.MODIFY_TYPE_SIGN == notifyType){
//				afMessageInfo.msg = name + " changed the group status to "+sign+".";
				String msgModifySign = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_modify_sign));
				msgModifySign = CommonUtils.replace(DefaultValueConstant.TARGET_STATUS, sign, msgModifySign);
				afMessageInfo.msg = msgModifySign;
			}
//			else if(AfGrpNotifyMsg.MODIFY_TYPE_SIGN_NAME == notifyType){
//				afMessageInfo.msg = name + " changed the group name to "+gname+" and change group status to "+sign;
//			}
			break;
		case AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER://群移除成员
//			afMessageInfo.msg = name + " has removed "+getAppendUserName(users_name)+".";
			String msgRemove = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_remove));
			msgRemove = CommonUtils.replace(DefaultValueConstant.TARGET_MEMBERS, getAppendUserName(users_name), msgRemove);
			afMessageInfo.msg = msgRemove;
			break;
		case AfMessageInfo.MESSAGE_GRP_DROP://群某成员退群
//			afMessageInfo.msg = name + " has exit "+gname+".";
			String msgExit = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_exit));
			msgExit = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, gname, msgExit);
			afMessageInfo.msg = msgExit;
			break;
		case AfMessageInfo.MESSAGE_GRP_DESTROY://群销毁解散
//			afMessageInfo.msg = name + " has dismissed "+gname+".";
			String msgDismiss = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_dismissed));
			msgDismiss = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, gname, msgDismiss);
			afMessageInfo.msg = msgDismiss;
			break;
		case AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED:
			afMessageInfo.msg = context.getString(R.string.you_have_been_removed);
			break;
		default:
			break;
		}
		return afMessageInfo;
	}	
	
	
	public static AfMessageInfo toAfMessageInfoForYou(AfGrpNotifyMsg afGrpNotifyMsg,PalmchatApp context) {
		// TODO Auto-generated method stub
		String gname = afGrpNotifyMsg.gname;
		String gid = afGrpNotifyMsg.gid;
		String afid = afGrpNotifyMsg.afid;
		String name = afGrpNotifyMsg.name;
		int notifyType = afGrpNotifyMsg.modify_type;
		String sign = afGrpNotifyMsg.sign;
		List<String> users_name = afGrpNotifyMsg.users_name;
		//List<String> users_afid= afGrpNotifyMsg.users_afid;
		//int gver = afGrpNotifyMsg.gver;
		int type = afGrpNotifyMsg.type | AfMessageInfo.MESSAGE_TYPE_MASK_GRP;
		AfMessageInfo afMessageInfo = new AfMessageInfo();
		afMessageInfo.fromAfId = afid;
		afMessageInfo.toAfId = gid;
		
		afMessageInfo.client_time = System.currentTimeMillis();
		afMessageInfo.name = gname;
		afMessageInfo.type = type;
		switch (afGrpNotifyMsg.type) {
		case AfMessageInfo.MESSAGE_GRP_CREATE://群创建
//			afMessageInfo.msg = name + " has invited "+getAppendUserName(users_name)+".";
			String msg = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_create_you));
			msg = CommonUtils.replace(DefaultValueConstant.TARGET_MEMBERS, getAppendUserName(users_name), msg);
			afMessageInfo.msg = msg;
			break;
		case AfMessageInfo.MESSAGE_GRP_ADD_MEMBER://群添加成员
//			afMessageInfo.msg = name + " has invited "+getAppendUserName(users_name)+".";
			String msgAdd = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_add_members_you));
			msgAdd = CommonUtils.replace(DefaultValueConstant.TARGET_MEMBERS, getAppendUserName(users_name), msgAdd);
			afMessageInfo.msg = msgAdd;
			break;
		case AfMessageInfo.MESSAGE_GRP_FRIEND_REQ://群好友请求
			
			break;
		case AfMessageInfo.MESSAGE_GRP_MODIFY://群更改
			if(AfGrpNotifyMsg.MODIFY_TYPE_NAME == notifyType){
//				afMessageInfo.msg = name + " changed the group name to "+gname+".";
				String msgModifyName = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_modify_name_you));
				msgModifyName = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, gname, msgModifyName);
				afMessageInfo.msg = msgModifyName;
			}else if(AfGrpNotifyMsg.MODIFY_TYPE_SIGN == notifyType){
//				afMessageInfo.msg = name + " changed the group status to "+sign+".";
				String msgModifySign = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_modify_sign_you));
				msgModifySign = CommonUtils.replace(DefaultValueConstant.TARGET_STATUS, sign, msgModifySign);
				afMessageInfo.msg = msgModifySign;
			}
//			else if(AfGrpNotifyMsg.MODIFY_TYPE_SIGN_NAME == notifyType){
//				afMessageInfo.msg = name + " changed the group name to "+gname+" and change group status to "+sign;
//			}
			break;
		case AfMessageInfo.MESSAGE_GRP_REMOVE_MEMBER://群移除成员
//			afMessageInfo.msg = name + " has removed "+getAppendUserName(users_name)+".";
			String msgRemove = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_remove_you));
			msgRemove = CommonUtils.replace(DefaultValueConstant.TARGET_MEMBERS, getAppendUserName(users_name), msgRemove);
			afMessageInfo.msg = msgRemove;
			break;
		case AfMessageInfo.MESSAGE_GRP_DROP://群某成员退群
//			afMessageInfo.msg = name + " has exit "+gname+".";
			String msgExit = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_exit_you));
			msgExit = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, gname, msgExit);
			afMessageInfo.msg = msgExit;
			break;
		case AfMessageInfo.MESSAGE_GRP_DESTROY://群销毁解散
//			afMessageInfo.msg = name + " has dismissed "+gname+".";
			String msgDismiss = CommonUtils.replace(DefaultValueConstant.TARGET_NAME, name, context.getString(R.string.system_info_dismissed_you));
			msgDismiss = CommonUtils.replace(DefaultValueConstant.TARGET_GNAME, gname, msgDismiss);
			afMessageInfo.msg = msgDismiss;
			break;
		case AfMessageInfo.MESSAGE_GRP_HAVE_BEEN_REMOVED:
			afMessageInfo.msg = context.getString(R.string.you_have_been_removed);
			break;
			
		default:
			break;
		}
		return afMessageInfo;
	}	
	
	
	private static String getAppendUserName(List<String> usersName){
		String userNameAppend = "";
		if(usersName == null){
			return userNameAppend;
		}
		int size = usersName.size();
		if(size > 0){
			for (int i = 0; i < size; i++) {
				String afid = usersName.get(i);
				if(TextUtils.isEmpty(afid)){
					continue;
				}
				if(i < size-1){
					int temp = i + 1;
					if(temp <= size -1){
						String afidTemp = usersName.get(temp);
						if(TextUtils.isEmpty(afidTemp)){
							userNameAppend = userNameAppend + afid;
						}else{
							userNameAppend = userNameAppend + afid +",";
						}
					}
				}else{
					userNameAppend = userNameAppend + afid;
				}
			}
		}
		return userNameAppend;
	}
}
