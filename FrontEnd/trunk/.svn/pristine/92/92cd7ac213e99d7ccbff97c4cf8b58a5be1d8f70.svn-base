package com.afmobi.palmchat.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.social.HomeAdapterData;
import com.core.AfFriendInfo;
import com.core.AfMessageInfo;
import com.core.AfMessageInfo.ChatsComparator;
import com.core.AfNearByGpsInfo;
import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfCommentInfo;
import com.core.AfResponseComm.AfMFileInfo;
import com.core.cache.CacheManager;




public class ByteUtils {
	
	
	public static int indexOfAfMFileInfo(List<AfMFileInfo> list , int target) {
		//	long startTime = System.currentTimeMillis();
		List<AfMFileInfo> temp = new ArrayList<AfMFileInfo>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (target == list.get(i)._id) {
				//		long endTime = System.currentTimeMillis();
				return i;
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
	public static int indexOfBroadCast(List<AfNearByGpsInfo> list , int target) {
		//	long startTime = System.currentTimeMillis();
		List<AfNearByGpsInfo> temp = new ArrayList<AfNearByGpsInfo>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (target == list.get(i)._id) {
				//		long endTime = System.currentTimeMillis();
				return i;
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
//	public static int indexOfRecordSendSuccessData(List<AfChapterInfo> list , String mid) {
//		List<AfChapterInfo> temp = new ArrayList<AfChapterInfo>();
//		temp.addAll(list);
//		int size = temp.size();
//		for (int i = size - 1; i >= 0 ; i--) {
//			if (mid.equals(temp.get(i).mid)) {
//				return i;
//			}
//		}
//		return -1;
//	}
	
	
	public static List<AfChapterInfo> add_sendSuccessLocalData(List<AfChapterInfo> list_aAfChapterInfos) {
		try {
			ArrayList<AfChapterInfo> temp = CacheManager.getInstance().getBC_RecordSendSuccessData();
			int size = temp.size();
			if (size <0) {
				PalmchatLogUtils.e("clear_sendSuccessLocalData,getBC_RecordSendSuccessData().size <0 ","");
				return list_aAfChapterInfos;
			}
			for (int i = size-1; i >= 0; i--) {
				AfChapterInfo afChapterInfo = temp.get(i);
				list_aAfChapterInfos.add(0, afChapterInfo);
			}
		} catch (Exception e) {
			CacheManager.getInstance().getBC_RecordSendSuccessData().clear();
		}
		
		return list_aAfChapterInfos;
	}
	public static List<AfChapterInfo> clear_sendSuccessLocalData2(List<AfChapterInfo> list_aAfChapterInfos) {
		try {
			ArrayList<AfChapterInfo> remove_data = new ArrayList<AfChapterInfo>();
			ArrayList<AfChapterInfo> temp = CacheManager.getInstance().getBC_RecordSendSuccessData();
			int size = temp.size();
			if (size <0) {
				PalmchatLogUtils.e("clear_sendSuccessLocalData,getBC_RecordSendSuccessData().size <0 ","");
				return list_aAfChapterInfos;
			}
			for (int i = size-1; i >= 0; i--) {
				AfChapterInfo recordSendSuccessAfChapterInfo =  temp.get(i);
				for (int j = 0; j < list_aAfChapterInfos.size() ; i++) {
					AfChapterInfo afChapterInfo = list_aAfChapterInfos.get(j);
					if (afChapterInfo.mid.equals(recordSendSuccessAfChapterInfo.mid)) {
						remove_data.add(recordSendSuccessAfChapterInfo);
//					temp.remove(i);
//					if (i <= CacheManager.getInstance().getBC_RecordSendSuccessData().size()) {
//					}
						PalmchatLogUtils.e("clear_sendSuccessLocalData,romove=", recordSendSuccessAfChapterInfo.content);
					}
				}
				PalmchatLogUtils.e("clear_sendSuccessLocalData,add=", recordSendSuccessAfChapterInfo.content);
				list_aAfChapterInfos.add(0, recordSendSuccessAfChapterInfo);
				CacheManager.getInstance().getBC_RecordSendSuccessData().removeAll(remove_data);
			}
		} catch (Exception e) {
			CacheManager.getInstance().getBC_RecordSendSuccessData().clear();
		}
		
		return list_aAfChapterInfos;
	}
	
	
	public static int indexOfBroadCastListBymid(List<AfChapterInfo> list , String target) {
		//	long startTime = System.currentTimeMillis();
		List<AfChapterInfo> temp = new ArrayList<AfChapterInfo>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (target.equals(list.get(i).mid)) {
				//		long endTime = System.currentTimeMillis();
				return i;
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
	public static int indexOfHomeBroadCastListBymid(List<HomeAdapterData> list , String target) {
		//	long startTime = System.currentTimeMillis();
		List<HomeAdapterData> temp = new ArrayList<HomeAdapterData>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (list.get(i).mAfChapterInfo != null) {
				if (target.equals(list.get(i).mAfChapterInfo.mid)) {
					//		long endTime = System.currentTimeMillis();
					return i;
				}
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
	public static int indexOfHomePeopleInfoListByid(List<HomeAdapterData> list , String palmID) {
		//	long startTime = System.currentTimeMillis();
		List<HomeAdapterData> temp = new ArrayList<HomeAdapterData>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (list.get(i).mAfPeopleInfo != null) {
				if (palmID.equals(list.get(i).mAfPeopleInfo.afid)) {
					//		long endTime = System.currentTimeMillis();
					return i;
				}
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
	public static int indexOfAfChapterInfo_listCommentBy_id(ArrayList<AfCommentInfo> list_comments , int _id) {
		//	long startTime = System.currentTimeMillis();
		List<AfCommentInfo> temp = new ArrayList<AfCommentInfo>();
		temp.addAll(list_comments);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (list_comments.get(i) != null) {
				if (_id == temp.get(i)._id) {
					//		long endTime = System.currentTimeMillis();
					return i;
				}
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
	public static int indexOfBroadCastListByid(List<AfChapterInfo> list , int target) {
		//	long startTime = System.currentTimeMillis();
		List<AfChapterInfo> temp = new ArrayList<AfChapterInfo>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (target == list.get(i)._id) {
				//		long endTime = System.currentTimeMillis();
				return i;
			}
		}
		//	long endTime = System.currentTimeMillis();
		return -1;
	}
	
	public static int indexOf(List<AfMessageInfo> list , int target) {
	//	long startTime = System.currentTimeMillis();
		List<AfMessageInfo> temp = new ArrayList<AfMessageInfo>();
		temp.addAll(list);
		int size = temp.size();
		for (int i = size - 1; i >= 0 ; i--) {
			if (target == list.get(i)._id) {
		//		long endTime = System.currentTimeMillis();
				return i;
			}
		}
	//	long endTime = System.currentTimeMillis();
		return -1;
	}
	
	public static List<AfFriendInfo> searchFilterFriend(List<AfFriendInfo> list , String filter) {
		//long startTime = System.currentTimeMillis();
		List<AfFriendInfo> temp = new ArrayList<AfFriendInfo>();
		temp.addAll(list);
		Collections.sort(temp, new MyComparator());
		List<AfFriendInfo> result = new ArrayList<AfFriendInfo>();
		for (AfFriendInfo f : temp) {
			String afid = f.afId;
			if (afid.startsWith(RequestConstant.SERVICE_FRIENDS)) {
				result.add(f);
			}
		}
		temp = null;
		//long endTime = System.currentTimeMillis();
		return result;
	}
	
	
	public static AfFriendInfo searchFriend(List<AfFriendInfo> list , String target) {
	//	long startTime = System.currentTimeMillis();
		List<AfFriendInfo> temp = new ArrayList<AfFriendInfo>();
		temp.addAll(list);
		Collections.sort(temp, new MyComparator());
		int start = 0;
		int end = temp.size() - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			AfFriendInfo f = temp.get(mid);
			if(f != null){
				String afid = f.afId;
				if (target != null && afid != null && afid.compareTo(target) < 0 ) {
					start = mid + 1; 
				} else if (target != null && afid != null && afid.compareTo(target) > 0 ) {
					end = mid - 1; 
				} else {
				//	long endTime = System.currentTimeMillis();
					return f;
				}
			}
		}
		temp = null;
		//long endTime = System.currentTimeMillis();
		return null;
	}
	
	public static AfMessageInfo searchChats(List<AfMessageInfo> list , String target) {
	//	long startTime = System.currentTimeMillis();
		List<AfMessageInfo> temp = new ArrayList<AfMessageInfo>();
		temp.addAll(list);
		Collections.sort(temp, new ChatsComparator());
		int start = 0;
		int end = temp.size() - 1;
		while (start <= end) {
			int mid = (start + end) / 2;
			if (temp.get(mid).toAfId.compareTo(target) < 0) {
				start = mid + 1; 
			} else if (temp.get(mid).toAfId.compareTo(target) > 0) {
				end = mid - 1; 
			} else {
			//	long endTime = System.currentTimeMillis();
				return temp.get(mid);
			}
		}
		temp = null;
	//	long endTime = System.currentTimeMillis();
		return null;
	}
}
