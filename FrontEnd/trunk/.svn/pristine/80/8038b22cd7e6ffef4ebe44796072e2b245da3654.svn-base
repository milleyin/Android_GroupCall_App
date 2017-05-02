package com.core;

import java.io.Serializable;
import java.util.ArrayList;



public class AfPalmCallResp {
	
	public Object  respobj = null;
    public int     leftTime = 0;  // 用户PalmCall的剩余通话分钟数(以分为单位),GetHotList返回
    public int     rewardTime = 0; // rewardTime，GetHotList返回
    public String   rechargeUrl = ""; // rechargeUrl，GetHotList返回
	
	public static class AfPalmCallHotListItem implements Serializable
	{
        public int		_id = -1;             // this is create by database auto create
		public String afid = "";             // afid
		public int answeringTimes = 0;      // 通话次数
		public int availableType = 0;       // audio,1;video:2
		public String mediaDescUrl = "";  // 简介描述url(现修改为返回ts，由上层拼url，By Yunxiang 20160817)
		public int duration   = 0;       // 语音简介时长
        public String coverUrl = "";  // 头像url(现修改为返回ts，由上层拼url，By Yunxiang 20160817)
        public String name = "";  // name
        public int sex   = 0;       // sex
        public int age   = 0;       // age
        public int errorCode = 0;  // 当afid不存在时返回此字段，-1表示afid不存在，此时用户profile中只返回 afid 字段

		
		public AfPalmCallHotListItem(){}
	}
	public  void AddHotListItem(int _id, String afid, int answeringTimes,int availableType,String mediaDescUrl,int duration,
                                String coverUrl, String name, int sex, int age, int errorCode)
	{
		if(respobj == null)
		{
			respobj = new  ArrayList<AfPalmCallHotListItem>();
		}

		AfPalmCallHotListItem item = new AfPalmCallHotListItem();
		item.afid = afid;
		item.answeringTimes = answeringTimes;
		item.availableType = availableType;
		item.mediaDescUrl = mediaDescUrl;
		item.duration = duration;
        item.coverUrl = coverUrl;
        item.name = name;
        item.age = age;
        item.sex = sex;
        item._id = _id;
        item.errorCode = errorCode;

		ArrayList<AfPalmCallHotListItem> arrayList = (ArrayList<AfPalmCallHotListItem>)respobj;
		arrayList.add(item);
	}


	public static final int AFMOBI_CALL_TYPE_MIN		= -1;	/* MIN  */
	public static final int AFMOBI_CALL_TYPE_UNKOWN		= 0;	/* Unkown */
	public static final int AFMOBI_CALL_TYPE_IN			= 1;	/* Call in */
	public static final int AFMOBI_CALL_TYPE_OUT		= 2;	/* Call out */
	public static final int AFMOBI_CALL_TYPE_MISSED		= 3;	/* Missed call */
    public static final int AFMOBI_CALL_TYPE_DECLINE	= 4;        /* Decined call */
	public static final int AFMOBI_CALL_TYPE_MAX = AFMOBI_CALL_TYPE_DECLINE+1;	/* MAX */
	public static class AfPalmCallRecord
	{
		public int				_id = -1;		/* this is create by database auto create */
		public String				afId = "";       /* AFID */
		public String				mediaUrl = "";   /* media url */
		public long				callId = 0;     /* call's ID */
		public long				callTime = 0;	/* call's time */
		public int	                callType = AFMOBI_CALL_TYPE_UNKOWN;	/* call's type */
	}
	public  void AddCallRecord(int _id, String afId, String mediaUrl,long callId,long callTime,int callType)
	{
		if(respobj == null)
		{
			respobj = new  ArrayList<AfPalmCallRecord>();
		}

		AfPalmCallRecord item = new AfPalmCallRecord();
		item._id = _id;
		item.afId = afId;
		item.mediaUrl = mediaUrl;
		item.callId = callId;
		item.callTime = callTime;
		item.callType = callType;

		ArrayList<AfPalmCallRecord> arrayList = (ArrayList<AfPalmCallRecord>)respobj;
		arrayList.add(item);
	}
	
	public static class AfPalmCallMakeCallResp
    {
        public int     leftTime = 0;  // 用户PalmCall的剩余通话分钟数(以分为单位)
        public String  justalkId = "";  // 第三方ID
    }

    public void SetMakeCallResp(int leftTime, String justalkId)
    {
        AfPalmCallMakeCallResp resp = null;
        if(respobj != null)
        {
            resp = (AfPalmCallMakeCallResp)respobj;
        }
        else
        {
            resp = new AfPalmCallMakeCallResp();
        }

        resp.leftTime = leftTime;
        resp.justalkId = justalkId;

        respobj = resp;
    }

    public static class AfPalmCallGetAlonePeriodResp
    {
        public int            open = 0; // 1,open;0,closed;
        public int            startTime = 0; // 免打扰开始时间（小时，当startTime  > endTime时，表示跨天）
        public int            endTime = 0; // 免打扰结束时间（小时）
    }

    public void SetAlonePeriodResp(int open, int startTime,int endTime)
    {
        AfPalmCallGetAlonePeriodResp resp = null;
        if(respobj != null)
        {
            resp = (AfPalmCallGetAlonePeriodResp)respobj;
        }
        else
        {
            resp = new AfPalmCallGetAlonePeriodResp();
        }

        resp.open = open;
        resp.startTime  = startTime;
        resp.endTime  = endTime;

        respobj = resp;
    }
	
}
