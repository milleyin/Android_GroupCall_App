package com.core;
import java.io.Serializable;
import java.util.ArrayList;

import android.R.integer;
import android.text.TextUtils;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;

public class AfResponseComm implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**************************************************begin broadcast upload object class*****************************************************/
	public static class AfBroadcastURL{
		public AfBroadcastURL(String url, String resume_url,String token){
		   this.set(url, resume_url, token);
		}
		public void set(String url, String resume_url,String token)
		{
			this.url = url;
			this.resume_url= resume_url;
			this.token = token;
		}
		public   String    url;
		public   String    resume_url;
		public   String    token;
	}
	public void set_broadcast_url(String url, String resume_url, String token)
	{
		obj = new AfBroadcastURL(url, resume_url, token);
	}
	
	/**************************************************end broadcast upload object class*****************************************************/
	
	/**************************************************begin broadcast message object class*****************************************************/
	public static class AfBrinfoList{
		public void set_list(int pid, String afid, byte sex, String name, String head_img_path, int age, String signature, String content,String time, String range,
				  int msg_id,int status, byte type, boolean is_friend, String url, String resume_url, String token)
		{
			this.msg_list.add(new AfNearByGpsInfo(pid,afid,sex,name, head_img_path, age, signature,  content, time,  range, msg_id, status,  type,  is_friend,  url,  resume_url,  token));
		}
		public ArrayList<AfNearByGpsInfo> msg_list =  new  ArrayList<AfNearByGpsInfo>();	//members 
	}
	public void set_brinfo_list(int pid, String afid, byte sex, String name, String head_img_path, int age, String signature, String content,String time, String range,
			  int msg_id,  int status, byte type, boolean is_friend, String url, String resume_url, String token)
	{
		if(obj == null){
		    obj = new AfBrinfoList();
		}
		((AfBrinfoList)obj).set_list(pid, afid,sex,name, head_img_path, age, signature,  content, time,  range,
				  msg_id,  status, type,  is_friend, url, resume_url, token);
		
	}
	/**************************************************end broadcast message object class*****************************************************/

	/***************************************************begin BC50 define***********************************************************************/
	public static class AfPeopleInfo{
			public AfPeopleInfo(int pos,String distance, String afid, byte sex, int age,String name,String sign,String head_img_path,boolean online, int logout_time){
			   this.set( pos, distance,afid,  sex,  age, name, sign, head_img_path, online,  logout_time);
			}
			public AfPeopleInfo() {
			}
			public void set(int pos,String distance, String afid, byte sex, int age,String name,String sign,String head_img_path,boolean online, int logout_time)
			{
				this.afid = afid;
				this.sex= sex;
				this.age = age;
				this.name = name;
				this.sex= sex;
				this.sign = sign;
				this.head_img_path= head_img_path;
				this.online= online;
				this.logout_time= logout_time;
				this.pos = pos;
				this.distance = distance;
			}
			public	 String    afid;
			public   byte      sex;
			public   int       age;
			public	 String    name;
			public	 String    sign;
			public   String    head_img_path;
			public   boolean   online;
			public   int       logout_time;
			public   int       pos;
			public   String    distance;
			

			public String getAfidFromHead() {
				if (head_img_path != null) {
					String [] ss = head_img_path.split(",");
					if (ss.length >= 1) {
						return ss[0];
					}
				}
				return afid;
			}
			
			public String getSerialFromHead() {
				if (head_img_path != null) {
					String [] ss = head_img_path.split(",");
					if (ss.length >= 2) {
						return ss[1];
					}
				}
				return "";
			}
			
			public String getServerUrl() {
				if (head_img_path != null) {
					String [] ss = head_img_path.split(",");
					if (ss.length >= 3) {
						return ss[2].replaceAll("/d", "");
					}
				}
				return "";
			}
			
	}

	public static class AfLikeInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public AfLikeInfo(int _id, int aid, String like_id, String time,String afid, int status,byte sex, int age, String name, String sign, String region, String local_img_path,int user_class, boolean identify){
			   this.set(_id, aid, like_id, time,afid ,status,sex,age,name,sign,region,local_img_path,user_class, identify);
			}
			public void set(int _id, int aid, String like_id,String time, String afid, int status,byte sex, int age, String name, String sign, String region, String local_img_path,int user_class, boolean identify)
			{
			    this._id = _id;
				this.aid = aid;
				this.status = status;
				this.like_id= like_id;
				this.time= time;
				this.afid = afid;
				this.profile_Info = set_friend_info(sex,age,name,sign,region,local_img_path,user_class, identify);
			}
			public   int       _id;
			public   int       aid;
			public   int       status;
			public	 String    like_id;
			public	 String    time;
			public	 String    afid;
			public AfFriendInfo profile_Info; 
   }
   public static class AfCommentInfo implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public AfCommentInfo(int _id, int aid, String comment_id, String time,String afid,String to_afid,String comment, int status,byte sex, int age, String name, String sign, String region, String local_img_path,String safid,String sname,int user_class, boolean identify){
		   this.set(_id, aid, comment_id, time,afid, to_afid, comment, status,sex,age,name,sign,region,local_img_path,safid,sname,user_class,identify);
		}
		public void set(int _id, int aid, String comment_id, String time,String afid,String to_afid,String comment, int status,byte sex, int age, String name, String sign, String region, String local_img_path,String safid,String sname,int user_class, boolean identify)
		{
		    this._id = _id;
			this.aid = aid;
			this.status = status;
			this.comment_id= comment_id;
			this.time= time;
			this.afid = afid;
			this.to_afid = to_afid;
			this.comment = comment;
			this.safid = safid;
			this.sname = sname;
			this.profile_Info = set_friend_info(sex,age,name,sign,region,local_img_path,user_class, identify);
		}
		public   int       _id;
		public   int       aid;
		public   int       status;
		public	 String    comment_id;
		public	 String    time;
		public	 String    afid;
		public	 String    to_afid;
		public  String    comment;
		public  String    safid;
		public  String    sname;
		public AfFriendInfo profile_Info;
		
		public String getAfidFromHead() {
			if (profile_Info.head_img_path != null) {
				String [] ss = profile_Info.head_img_path.split(",");
				if (ss.length >= 1) {
					return ss[0];
				}
			}
			return afid;
		}
		
		public String getSerialFromHead() {
			if (profile_Info.head_img_path != null) {
				String [] ss = profile_Info.head_img_path.split(",");
				if (ss.length >= 2) {
					return ss[1];
				}
			}
			return "";
		}
		
		public String getServerUrl() {
			if (profile_Info.head_img_path != null) {
				String [] ss = profile_Info.head_img_path.split(",");
				if (ss.length >= 3) {
					return ss[2].replaceAll("/d", "");
				}
			}
			return "";
		}
   }
   public static class AfMFileInfo implements Serializable{
	   /**
	 * 
	 */
	private static final long serialVersionUID = -8555558820207238486L;

	public AfMFileInfo(){
		   
	   }
	   		public AfMFileInfo(int _id, int aid, String local_img_path, String local_thumb_path,String url, String thumb_url, int url_type, int status,int duration){
			   this.set(_id, aid,  local_img_path,  local_thumb_path, url, thumb_url, url_type,  status, duration);
			}
			public void set(int _id, int aid, String local_img_path, String local_thumb_path,String url,String thumb_url,int url_type, int status,int duration)
			{
			    this._id = _id;
				this.aid= aid;
				this.local_img_path= local_img_path;
				this.local_thumb_path= local_thumb_path;
				this.url = url;
				this.thumb_url = thumb_url;
				this.url_type = url_type;
				this.status = status;
				this.duration = duration;
			}
			public   int       _id;
			public   int       aid;
			public	 String    local_img_path;
			public	 String    local_thumb_path;
			public	 String    url;
			public   String    thumb_url;
			public   int       url_type;
			public   int       status;
			public   int       duration;  //video duration;
			
			public byte type;
			public boolean voicePlaying;
			public int recordTime;
			public boolean isSingle;//如果是图片是否是单图模式 add by Wxl
			public String  original_picture_path;// add by wxl 20160119   当是自己发的图片时候选的那张图片的路径，记录下来用于排除已选图片
//			public long[]  TecnoCameraPicName ;// add by wxl 20160317 记录Tecno手机拍照前后的时间 解决两张图的问题
	   		public boolean isVisionEdit;//是否美图编辑过 add by wxl 20160811
	   		public String picFilter;//滤镜效果 用于统计
			public boolean is_add;//列表中 add图片按钮
			public int[] resize;//图片在列表中排布显示尺寸 width height
			public int[] cut;//图片在列表中裁切   x y w h

		   /**
			* 发送的时候取滤镜类型
			* @return
			*/
	   		 public String getPicFilterForSend(){
				if(!TextUtils.isEmpty(local_thumb_path)){
					int _inx=local_thumb_path.lastIndexOf(Constants.PICTURE_FILTER);
					if(_inx>=0){
						String filter=local_thumb_path.substring(_inx+Constants.PICTURE_FILTER.length());
						PalmchatLogUtils.i("WXL",filter);
						return filter;
					}
				}
				return "Default";//如果没选过 就是这个
			}
			public String getResize(){
				if(resize!=null&&resize.length==2){
					 return resize[0]+"_"+resize[1] ;
				}
				return null;
			}
			public String getCut(){
				if(cut!=null&&cut.length==4){ 
					return cut[0]+"_"+cut[1]+"_"+cut[2]+"_"+cut[3]; 
				}
				return null;
			}
   }

public static class AfChapterInfo  implements Serializable{
	   private static final long serialVersionUID = -8507779159656066923L; 
	   public AfChapterInfo(){
		}
		public AfChapterInfo(int pos, int _id, String tag, byte type, String mid, String title,String time,String mstoken, String content, String country,byte purview,String afid, int total_like, int total_comment, int status,byte sex, int age, String name, String sign, String region, String local_img_path,
				             String lat, String lng, String desc,String share_mid,String tagvip_pa, String pic_rule,int share_flag,int aid,int share_del,byte content_flag,int user_class, boolean identify){
		   this.set(pos, _id, tag, type,mid, title, time,mstoken,content,country,purview,afid, total_like,total_comment,status, sex, age, name, sign, region, local_img_path, lat, lng,desc, share_mid, tagvip_pa, pic_rule, share_flag,aid,share_del,content_flag,user_class,identify);
		}
		public void set(int pos, int _id,String tag, byte type, String mid, String title,String time,String mstoken, String content, String country,byte purview,String afid, int total_like, int total_comment, int status,byte sex, int age, String name, String sign, String region, String local_img_path, 
				        String lat, String lng, String desc, String share_mid,String tagvip_pa, String pic_rule,int share_flag,int aid,int share_del,byte content_flag,int user_class, boolean identify)
		{
		    this._id = _id;
			this.tag = tag;
			this.type = type;
			this.mid = mid;
			this.title= title;
			this.time = time;
			this.mstoken = mstoken;
			this.content= content;
			this.country= country;
			this.lat = lat;
			this.lng = lng;
			this.purview= purview;
			this.afid = afid;
			this.total_like= total_like;
			this.total_comment= total_comment;
			this.status = status;
			this.pos = pos;
			this.desc = desc;
			this.share_mid = share_mid;
			this.tagvip_pa = tagvip_pa;
			this.pic_rule = pic_rule;
			this.share_flag = share_flag;
			this.share_del = share_del;
			this.aid = aid;
			this.content_flag = content_flag;
			
			this.profile_Info = set_friend_info(sex,age,name,sign,region,local_img_path,user_class,identify);
			
			this.profile_Info.afId = afid;
		}

		
		public void set_like_list(int _id, int aid, String like_id, String time,String afid, int status,byte sex, int age, String name, String sign, String region, String local_img_path, boolean bAddShare,int user_class, boolean identify)
		{
			if(!bAddShare)
			{
				this.list_likes.add(new AfLikeInfo(_id, aid, like_id, time, afid, status,sex,age,name,sign,region,local_img_path,user_class,identify));
			}
			else
			{
				if(share_info == null)
				{
					share_info = new AfChapterInfo();  // 转发的广播内容
				}
				share_info.list_likes.add(new AfLikeInfo(_id, aid, like_id, time, afid, status,sex,age,name,sign,region,local_img_path,user_class,identify));
			}
		}
		public void set_comment_list(int _id, int aid, String comment_id, String time,String afid,String to_afid,String comment, int status,byte sex, int age, String name, String sign, String region, String local_img_path,String safid,String sname, boolean bAddShare,int user_class, boolean identify )
		{
			if(!bAddShare)
			{
				this.list_comments.add(new AfCommentInfo(_id, aid, comment_id,  time, afid, to_afid, comment, status,sex,age,name,sign,region,local_img_path,safid,sname,user_class,identify));
			}
			else
			{
				if(share_info == null)
				{
					share_info = new AfChapterInfo();  // 转发的广播内容
				}
				share_info.list_comments.add(new AfCommentInfo(_id, aid, comment_id,  time, afid, to_afid, comment, status,sex,age,name,sign,region,local_img_path,safid,sname,user_class,identify));
			}	
		}
		public void set_url_list(int _id, int aid, String local_img_path, String local_thumb_path,String url, String thumb_url,int url_type, int status, boolean bAddShare,int duration )
		{
			if(!bAddShare)
			{
				this.list_mfile.add(new AfMFileInfo(_id, aid,  local_img_path,  local_thumb_path, url,thumb_url,url_type,  status,duration ));
			}
			else
			{
				if(share_info == null)
				{
					share_info = new AfChapterInfo();  // 转发的广播内容
				}
				share_info.list_mfile.add(new AfMFileInfo(_id, aid,  local_img_path,  local_thumb_path, url,thumb_url,url_type,  status,duration ));
			}
		}
		
		public void set_tag_list(int _id, int aid, String tag,String pa,int type, int status, int datasource_type, boolean bAddShare )
		{
			if(!bAddShare)
			{
				this.list_tags.add(new AfBroadCastTagInfo(_id, aid, tag, pa, type, status, datasource_type ));
			}
			else
			{
				if(share_info == null)
				{
					share_info = new AfChapterInfo();  // 转发的广播内容
				}
				
				share_info.list_tags.add(new AfBroadCastTagInfo(_id, aid, tag, pa, type, status, datasource_type ));
			}
		}
		public   int       _id;
		public   String    tag;
		public   byte      type;
		public	 String    mid;//title id;
		public	 String    title;
		public	 String    time;//publish time;
		public	 String    content;
		public	 String    country;// the publish author country
		public  String    lat;
		public  String    lng;
		public	 byte      purview;//0 - public  1 to private
		public	 String    afid;//publish owner afid;
		public   int       total_like;
		public   int       total_comment;
		public   int       status;
		public   int       pos;
		public   String    desc = "";
		public   String    share_mid = "";    //分享广播的ID（只能是原文ID）
		public   int       share_del = 0;    // 1表示已删除
		public   String    tagvip_pa = "";    // 绑定的官方账号
		public   String    pic_rule = "";     //新版图片排版格式
		public   int      share_flag = 0;    //为1：带有评论的分享，2：未带评论的分享（新版本不要显示content）
		public   byte     content_flag; // 0 : content length <= 140 1: content is big text 
		public AfFriendInfo profile_Info;
		public ArrayList<AfMFileInfo> list_mfile =  new  ArrayList<AfMFileInfo>();
		public ArrayList<AfLikeInfo> list_likes =  new  ArrayList<AfLikeInfo>();
		public ArrayList<AfCommentInfo> list_comments =  new  ArrayList<AfCommentInfo>();
		public ArrayList<AfBroadCastTagInfo> list_tags =  new  ArrayList<AfBroadCastTagInfo>(); // tag list
		
		public AfChapterInfo share_info = null;
		public int aid = 0;   
		
		public boolean download_success = false ;
		public int cur_count;
		public int count;
		public String mstoken;
		public boolean isLike = false;
		public int ds_type;
		
		public int eventBus_action;//add by wxl 作为EventBus 事件的类型 入删除事件  更新事件等
		
		
//		public static AfProfileInfo NearByGpsInfoToProfile(AfFriendInfo f) {
//			AfProfileInfo dto = new AfProfileInfo();
//			dto.afId = afid;
//			dto.name = f.name;
//			dto.age = f.age;
//			dto.sex = f.sex;
//			dto.head_img_path = f.head_img_path;
//			dto.alias = f.name;
//			// dto.setFromFriend(true);
//			return dto;
//		}

		
		public String getAfidFromHead() {
			if (profile_Info.head_img_path != null) {
				String [] ss = profile_Info.head_img_path.split(",");
				if (ss.length >= 1) {
					return ss[0];
				}
			}
			return afid;
		}
		
		public String getSerialFromHead() {
			if (profile_Info.head_img_path != null) {
				String [] ss = profile_Info.head_img_path.split(",");
				if (ss.length >= 2) {
					return ss[1];
				}
			}
			return "";
		}
		
		public String getServerUrl() {
			if (profile_Info.head_img_path != null) {
				String [] ss = profile_Info.head_img_path.split(",");
				if (ss.length >= 3) {
					return ss[2].replaceAll("/d", "");
				}
			}
			return "";
		}
		 
		public byte getType() {  
		  AfMFileInfo info = null;
			if (list_mfile.size() > 0) {
				info = list_mfile.get(0);
			}
			if (TextUtils.isEmpty(this.content)) {
				
				if (info != null) {
					switch (info.url_type) {
					case Consts.URL_TYPE_IMG:
						type = Consts.BR_TYPE_IMAGE;
						break;
					case Consts.URL_TYPE_VOICE:
						type = Consts.BR_TYPE_VOICE;
						break;
					case Consts.URL_TYPE_GIF:
					{
						type = Consts.BR_TYPE_UNKNOW;
					}
					break;
                    case Consts.URL_TYPE_VEDIO:
                    {
                        type = Consts.BR_TYPE_VIDEO;
                    }
                    break;
					default:
						type = Consts.BR_TYPE_TEXT;
						break;
					}
				}else {
					type = Consts.BR_TYPE_TEXT;
				}
				return type;
			}else {
				if (info != null) {
					switch (info.url_type) {
					case Consts.URL_TYPE_IMG:
						type = Consts.BR_TYPE_IMAGE_TEXT;
						break;
					case Consts.URL_TYPE_VOICE:
						type = Consts.BR_TYPE_VOICE_TEXT;
						break;
					case Consts.URL_TYPE_GIF:
					{
						type = Consts.BR_TYPE_UNKNOW;
					}
					break;
					case Consts.URL_TYPE_VEDIO:
					{
						type = Consts.BR_TYPE_VIDEO_TEXT;
					}
					break;
					default:
						type = Consts.BR_TYPE_TEXT;
						break;
					}
				}else {
					type = Consts.BR_TYPE_TEXT;
				}
				return type;
			}
			 }
			 
}
public AfFriendConn frdConnList=null;
public class AfFriendConn{
	public ArrayList<String> midList = new  ArrayList<String>();
	public boolean next = false;
}
public void setFrdConn(String mid,boolean next)
{
	if(frdConnList==null)
		frdConnList = new AfFriendConn();
	frdConnList.midList.add(mid);
	frdConnList.next = next;
}
public static AfFriendInfo set_friend_info(byte sex, int age, String name, String sign, String region, String local_img_path,int user_class, boolean identify)
{
	AfFriendInfo profile_Info = new AfFriendInfo();
	profile_Info.sex = sex;
	profile_Info.age = age;
	profile_Info.name = name;
	profile_Info.signature = sign;
	profile_Info.region = region;
	profile_Info.head_img_path = local_img_path;
	profile_Info.user_class = user_class;
	profile_Info.identify = identify;
	return profile_Info;
}
public static class AfPeoplesChaptersList{
		public void set_people_list(int pos, String distance ,String afid, byte sex, int age,String name,String sign,String head_img_path,boolean online, int logout_time)
		{
			this.list_peoples.add(new AfPeopleInfo(pos, distance, afid, sex, age, name, sign, head_img_path, online,  logout_time));
		}
		
		public void set_chapter_like_list(int _id, int aid, String like_id, String time,String afid, int status,byte sex,int age,String name, String sign, String region, String local_img_path, boolean bAddShare,int user_class, boolean identify)
		{
		     if(obj == null){
				obj = new AfChapterInfo();
		    }
			((AfChapterInfo)obj).set_like_list(_id, aid, like_id, time, afid, status,sex,age,name,sign,region,local_img_path,bAddShare,user_class, identify);
		}
		public void set_chapter_comment_list(int _id, int aid, String comment_id, String time,String afid,String to_afid,String comment, int status,byte sex,int age,String name, String sign, String region, String local_img_path, String safid,String sname, boolean bAddShare,int user_class, boolean identify)
		{
		     if(obj == null){
				obj = new AfChapterInfo();
		    }
			((AfChapterInfo)obj).set_comment_list(_id, aid, comment_id, time, afid, to_afid, comment, status,sex,age,name,sign,region,local_img_path,safid,sname,bAddShare,user_class,identify);
		}
		
		public void set_chapter_tag_list(int _id, int aid, String tag,String pa,int type, int status, int datasource_type, boolean bAddShare )
		{
			if(obj == null){
				obj = new AfChapterInfo();
		    }
			
			((AfChapterInfo)obj).set_tag_list(_id, aid, tag, pa, type, status, datasource_type,bAddShare );
		}
		
		public void set_url_list(int _id, int aid, String local_img_path, String local_thumb_path,String url, String thumb_url, int url_type, int status, boolean bAddShare,int duration)
		{
		     if(obj == null){
				obj = new AfChapterInfo();
		    }
			((AfChapterInfo)obj).set_url_list(_id, aid,  local_img_path,  local_thumb_path, url, thumb_url,  url_type,  status,bAddShare,duration );
		}
		public void set_chapter_list(int pos, int _id,String tag, byte type,String mid, String title,String time, String mstoken, String content, String country,byte purview,String afid, int total_like, int total_comment, int status
				,byte sex,int age,String name, String sign, String region, String local_img_path, String lat, String lng, String desc, String share_mid,String tagvip_pa, String pic_rule,int share_flag, boolean bAddShare,int aid,int share_del,byte content_flag,int user_class, boolean identify)
		{
			
			if(bAddShare)
			{
				if(obj == null){
					obj = new AfChapterInfo(pos, _id, tag, type,mid, title, time, mstoken, content,  country, purview, afid,  total_like,total_comment, status,sex,age,name,sign,region,local_img_path,lat,lng,desc,share_mid,tagvip_pa,pic_rule,share_flag,aid,share_del,content_flag,user_class,identify);
					//((AfChapterInfo)obj).set_friend_info(sex, age, name, sign, region, local_img_path);
					
					((AfChapterInfo)obj).share_info = new AfChapterInfo(pos, _id, tag, type,mid, title, time, mstoken, content,  country, purview, afid,  total_like,total_comment, status,sex,age,name,sign,region,local_img_path,lat,lng,desc,share_mid,tagvip_pa,pic_rule,share_flag,aid,share_del,content_flag,user_class,identify);
			    }
				else {
					((AfChapterInfo)obj).share_info.set(pos, _id, tag, type,mid, title, time, mstoken, content,  country, purview, afid,  total_like,total_comment, status,sex, age, name, sign, region, local_img_path, lat, lng,desc,share_mid,tagvip_pa,pic_rule,share_flag,aid,share_del,content_flag,user_class,identify);
				}
			
			}
			else
			{
				if(obj == null){
					obj = new AfChapterInfo(pos, _id, tag, type,mid, title, time, mstoken, content,  country, purview, afid,  total_like,total_comment, status,sex,age,name,sign,region,local_img_path,lat,lng,desc,share_mid,tagvip_pa,pic_rule,share_flag, aid,share_del,content_flag,user_class,identify);
					//((AfChapterInfo)obj).set_friend_info(sex, age, name, sign, region, local_img_path);
			    }else{
			        ((AfChapterInfo)obj).set(pos, _id, tag, type,mid, title, time, mstoken, content,  country, purview, afid,  total_like,total_comment, status,sex, age, name, sign, region, local_img_path, lat, lng,desc,share_mid,tagvip_pa,pic_rule,share_flag, aid,share_del,content_flag,user_class,identify);
			        //((AfChapterInfo)obj).set_friend_info(sex, age, name, sign, region, local_img_path);
			    }
				this.list_chapters.add((AfChapterInfo)obj);
				obj = null;
			}
		    
		    
		}
		public Object  obj= null;
		public int res_total;
		public boolean next = false;
		public String pa = "";  //only for REQ_BCGET_COMMENTS_BY_TAGNAME & REQ_BCGET_RECENT_HOTS_BY_TAGNAME
		public ArrayList<AfPeopleInfo> list_peoples =  new  ArrayList<AfPeopleInfo>();
		public ArrayList<AfChapterInfo> list_chapters =  new  ArrayList<AfChapterInfo>();
	}

    public void set_chapters_property(int res_total, boolean next, String pa)
    {
    	if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
    	((AfPeoplesChaptersList)obj).res_total = res_total;
    	((AfPeoplesChaptersList)obj).next = next;
    	((AfPeoplesChaptersList)obj).pa = pa;
    }

	public void set_people_list(int pos, String distance, String afid, byte sex, int age,String name,String sign,String head_img_path,boolean online, int logout_time)
	{
		if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
		((AfPeoplesChaptersList)obj).set_people_list(pos, distance, afid, sex, age, name, sign, head_img_path, online,logout_time);
		
	}
	
	public void set_chapter_like_list(int _id, int aid, String like_id, String time,String afid, int status,byte sex,int age,String name, String sign, String region, String local_img_path, boolean bAddShare,int user_class,boolean identify)
	{
		if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
		((AfPeoplesChaptersList)obj).set_chapter_like_list(_id, aid, like_id, time, afid, status,sex, age, name, sign, region, local_img_path,bAddShare,user_class,identify);
		
	}
	public void set_chapter_comment_list(int _id,int aid,  String comment_id, String time,String afid,String to_afid,String comment,int status,byte sex,int age,String name, String sign, String region, String local_img_path,String safid, String sname, boolean bAddShare,int user_class,boolean identify)
	{
		if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
		((AfPeoplesChaptersList)obj).set_chapter_comment_list(_id, aid,comment_id, time, afid, to_afid, comment, status,sex, age, name, sign, region, local_img_path, safid, sname,bAddShare,user_class,identify);
		
	}
	
	public void set_chapter_tag_list(int _id, int aid, String tag,String pa,int type, int status, int datasource_type, boolean bAddShare)
	{
		if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
		((AfPeoplesChaptersList)obj).set_chapter_tag_list(_id, aid, tag, pa, type, status, datasource_type,bAddShare);
		
	}

	public void set_url_list(int _id, int aid, String local_img_path, String local_thumb_path,String url, String thumb_url, int url_type, int status, boolean bAddShare,int duration )
	{
		if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
		((AfPeoplesChaptersList)obj).set_url_list(_id, aid, local_img_path, local_thumb_path, url, thumb_url, url_type,  status,bAddShare,duration );
		
	}
    public void set_chapter_list(int pos,int _id,String tag, byte type, String mid, String title,String time,String mstoken,String content, String country,byte purview,String afid, int total_like, int total_comment, int status
    		,byte sex,int age,String name, String sign, String region, String local_img_path, String lat, String lng, String desc,String share_mid,String tagvip_pa, String pic_rule,int share_flag, boolean bAddShare, int aid,int share_del,byte content_flag,int user_class,boolean identify)
	{
		if(obj == null){
		    obj = new AfPeoplesChaptersList();
		}
		((AfPeoplesChaptersList)obj).set_chapter_list(pos, _id, tag, type, mid, title, time,  mstoken, content,  country, purview, afid,  total_like,total_comment, status
				,sex,age,name,sign,region,local_img_path, lat,lng, desc,share_mid,tagvip_pa,pic_rule,share_flag,bAddShare,aid,share_del, content_flag,user_class,identify);
		
	}

	public static class AfPulishInfo{
		public AfPulishInfo(String mid, String mstoken){
			   this.set(mid, mstoken);
			}
			public void set(String mid, String mstoken)
			{
				this.mid= mid;
				this.mstoken= mstoken;
			}
			public	 String    mid;
			public	 String    mstoken;
   }

	public void set_publish_info(String mid, String mstoken)
	{
		if(obj == null){
		    obj = new AfPulishInfo(mid, mstoken);
		}
	}
	/***************************************************end rBC50 define**********************************/
	
	/**************************************** 5.2 bc tag start *******************************************/
	// tag type
	public final static int AFTAGINFO_TAGTYPE_NORMAL          = 0; /* normal tag */
	public final static int AFTAGINFO_TAGTYPE_PUBLIC_ACCOUNT  = 1; /* public account's tag */
	
	// use type
	public final static int AFTAGINFO_USETYPE_NOT_LIMITED = -1;  /* not limited */
	public final static int AFTAGINFO_USETYPE_UNKNOW = 0;        /* unknow type */
	public final static int AFTAGINFO_USETYPE_BANNER = 1;        /* banner tag */
	public final static int AFTAGINFO_USETYPE_HOT = 2;           /* hot tags */
	public final static int AFTAGINFO_USETYPE_DEFAULT_TREND = 3; /* default trend */
	public final static int AFTAGINFO_USETYPE_DEFAULT_TAGS = 4; /* default tags */
	
	// tag content type
	public final static int AFMOBI_TREND_CONTENT_TYPE_MIN = -1;
	public final static int AFMOBI_TREND_CONTENT_TAG_TYPE = 0;
	public final static int AFMOBI_TREND_CONTENT_URL_TYPE = 1;
	public final static int AFMOBI_TREND_HOT_TODAY_TYPE = 2;

	
	// 广播列表返回taginfo详细结构
	public static class AfBroadCastTagInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7436145953844291312L;
		/**
		 * 
		 */
		public AfBroadCastTagInfo(int _id, int aid, String tag,String pa,int type, int status, int datasource_type)
		{
			this.set(_id, aid, tag, pa, type, status, datasource_type);
		}
		public void set(int _id, int aid, String tag,String pa,int type, int status, int datasource_type)
		{
		    this._id = _id;
			this.aid = aid;
			this.status = status;
			this.tag = tag;
			this.pa = pa;
			this.type = type;
			this.status = status;
			this.datasource_type = datasource_type;
		}
		public   int       _id;             // this is create by database auto create
		public   int       aid;             // attach id, relate to DB_BRINFO
		public	 String    tag;             // tag
		public	 String    pa;              // public account
		public   int       type;            // type,AFTAGINFO_TAGTYPE_xx
		public   int       status;          // current status;
		public   int       datasource_type; // data source type , default: 0 - unkown   other: user define (must > 0)
			
   }
	
	// Tag下发图文广播简要结构定义
	public static class AfBCPriefInfo
	{
		public	 int       _id;         // this is create by database auto create
		public	 String    mid;         // mid of broadcas
		public	 String    pic_url;     // picture's url
		
		public AfBCPriefInfo(){}
    }
	
	

	
	// Tag结构定义
	public static class AfTagInfo
	{
		public	 int       _id;              // this is create by database auto create
		public	 String    tag;              // type=AFMOBI_TREND_CONTENT_TAG_TYPE, tag content;type=AFMOBI_TREND_CONTENT_URL_TYPE,url content;
		public	 String    pic_url;          // picture's url
		public   long       post_number;      // number
		public   int       use_type;         // usetype,such as:AFTAGINFO_USETYPE_xx
		public   int       type;			// type (only for trends),such AFMOBI_TREND_CONTENT_XX
		
		public AfTagInfo(){}
    }
	
	public static void AddBCPriefComm(ArrayList<AfBCPriefInfo> list, int id, String mid, String pic_url)
	{
		if(list == null)
		{
			list = new ArrayList<AfBCPriefInfo>();
		}
		AfBCPriefInfo node = new AfBCPriefInfo();
		node._id = id;
		node.mid = mid;
		node.pic_url = pic_url;
		
		list.add(node);
	}
	
	public static void AddTagInfoComm(ArrayList<AfTagInfo> list, int id, long post_number, int use_type, String tag, String pic_url, int type)
	{
		if(list == null)
		{
			list = new ArrayList<AfTagInfo>();
		}
		AfTagInfo node = new AfTagInfo();
		node._id = id;
		node.post_number = post_number;
		node.use_type = use_type;
		node.tag = tag;
		node.pic_url = pic_url;
		node.type = type;
		
		list.add(node);
	}
	
	
	
	// 默认推荐tag内容
	public static class AfTagGetDefaultTrend
	{
		
		public String  tag_name = "";
		public ArrayList<AfBCPriefInfo> brief_list =  new  ArrayList<AfBCPriefInfo>(); // list of AfBCPriefInfo
		
		public AfTagGetDefaultTrend(){}
	}
	
	// 获取trends首页返回
	public static class AfTagGetTrendsResp
	{
		public ArrayList<AfTagInfo> banner_list =  new  ArrayList<AfTagInfo>(); // list of AfTagInfo
		public ArrayList<AfTagInfo> hottags_list =  new  ArrayList<AfTagInfo>(); // list of AfTagInfo
		
		public AfTagGetDefaultTrend defaluttrend = null;
		
		
		public void SetDefaultTrendsTagName(String tag_name)
		{
			if(defaluttrend == null)
			{
				defaluttrend = new AfTagGetDefaultTrend();
			}
			
			defaluttrend.tag_name = tag_name;
		}
		public void AddBCPrief(int id, String mid, String pic_url)
		{
			if(defaluttrend == null)
			{
				defaluttrend = new AfTagGetDefaultTrend();
			}
			
			AddBCPriefComm(defaluttrend.brief_list,id, mid, pic_url);
		}
		
		public void AddBanner(int id, long post_number, int datasource_type, String tag, String pic_url,int type)
		{
			AddTagInfoComm(banner_list, id, post_number, datasource_type, tag, pic_url,type);
		}
		
		public void AddHottags(int id, long post_number, int datasource_type, String tag, String pic_url,int type)
		{
			AddTagInfoComm(hottags_list, id, post_number, datasource_type, tag, pic_url,type);
		}
		
		public AfTagGetTrendsResp(){}
    } 
	
	// 获取Trends More返回
	public static class AfTagGetTrendsMoreResp
	{
		public boolean  next = false;
		
		public AfTagGetDefaultTrend defaluttrend = null;
		
		//public ArrayList<AfBCPriefInfo> defaul_list =  new  ArrayList<AfBCPriefInfo>(); // list of AfBCPriefInfo
		
		public void AddBCPrief(int id, String mid, String pic_url)
		{
			if(defaluttrend == null)
			{
				defaluttrend = new AfTagGetDefaultTrend();
			}
			
			AddBCPriefComm(defaluttrend.brief_list,id, mid, pic_url);
		}
		
		public AfTagGetTrendsMoreResp(){}
    } 
	
	// 热门TAG或搜索TAG返回
	public static class AfTagGetTagsResp
	{
		public boolean  next = false;
		public ArrayList<AfTagInfo> tags_list =  new  ArrayList<AfTagInfo>(); // list of AfTagInfo
		
		public void AddTags(int id, long post_number, int datasource_type, String tag, String pic_url,int type)
		{
			AddTagInfoComm(tags_list, id, post_number, datasource_type, tag, pic_url,type);
		}
		
		public AfTagGetTagsResp(){}
    } 
	
	// 分享TAG或广播返回
	public static class AfTagShareTagOrBCResp
	{
		public String  tag_name = "";          // tag name
		public String  pic_url = "";           // picture's url
		public long     post_number = 0;        // number 
		public String  forward_id = "";        // forward id 
		public String  datetime = "";          // date time
		
		public AfTagShareTagOrBCResp(){}
    } 
	
	// 获取DefaultTags返回
	public static class AfTagGetDefaultTagsResp
	{
		public ArrayList<String> tags_list =  new  ArrayList<String>(); // list of String
		
		public void AddTags(String tags)
		{
			if(tags != null && tags.length() > 0 )
			{
				tags_list.add(tags);
			}
		}

		public AfTagGetDefaultTagsResp(){}
    } 
	
	// 获取tag语言包结构
	public static class AfTagLangPackageItem
	{
		public	 String    tag_name;     // tagname
		public   int       type;         // type (default : 0 , local : 1)
		
		public AfTagLangPackageItem(){}
    }
	
	// 获取tag语言包返回
	public static class AfTagGetLangPackageResp
	{
		public ArrayList<String> default_list =  new  ArrayList<String>(); // list of String
		public ArrayList<String> local_list =  new  ArrayList<String>(); // list of String
		public String  language_ver = ""; // lang ver
		public int     lastest  = 0;      // ( 0 : NO 1: YES )
		
		public ArrayList<AfTagLangPackageItem> db_list =  new  ArrayList<AfTagLangPackageItem>(); // AfTagLangPackageItem
		
		public void AddTags(String tags, boolean isdefault)
		{
			if(tags != null && tags.length() > 0 )
			{
				if(isdefault)
				{
					default_list.add(tags);
				}
				else
				{
					local_list.add(tags);
				}
			}
		}
		
		public void AddTagLang(String tag, int type)
		{
			if(tag != null && tag.length() > 0 )
			{
				AfTagLangPackageItem item = new AfTagLangPackageItem();
				item.tag_name = tag;
				item.type = type;
				
				db_list.add(item);
			}
		}

		public AfTagGetLangPackageResp(){}
    } 
	
	// 指定区域Broadcast相关信息
	public static class AfBCRegionBroadcast
	{
		public	 String    country;         // country
		public	 String    state;           // state
		public   long       post_num;        // post_num
		
		public AfBCRegionBroadcast(){}
    }
	
	// 获取指定区域Broadcast相关信息返回
	public static class AfBCGetRegionBroadcastResp
	{
		public ArrayList<AfBCRegionBroadcast> default_list =  new  ArrayList<AfBCRegionBroadcast>(); // list of AfBCRegionBroadcast
		public void AddRegionBroadcast(String country, String state, long post_num)
		{
			AfBCRegionBroadcast node = new AfBCRegionBroadcast();
			node.country = country;
			node.state = state;
			node.post_num = post_num;
			
			default_list.add(node);
		}
		
		
		public AfBCGetRegionBroadcastResp(){}
    } 
	
	/**************************************** 5.2 bc tag end *******************************************/
	
	public Object  obj= null;
}
