package com.afmobi.palmchat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by hj on 2016/6/29.
 * Follower操作名单类
 */
public class FolllowerUtil {
    private FolllowerUtil(){}
    private static FolllowerUtil folllowerUtil = new FolllowerUtil();
    /**
     * 粉丝集合列表
     */
    ArrayList<String> arrayList = new ArrayList<>();
    /**
     * 查找粉丝数
     */
    int count;
    /**
     * 开始索引
     */
    int startIndex;
    /**
     * 是否已获取Follower列表
     */
    boolean mIsLoadFollowers = false;

    public static FolllowerUtil getInstance(){
        return folllowerUtil;
    }

    /**
     * 粉丝数，主要用于在myprofile显示，条件是进入follower页面获取到粉丝
     */
    private int mFollowerCount;

    /** 添加所有的数据
     * @param arr
     */
    public synchronized  void addAll(String[] arr) {
        String[] arrTtemp = null;
        if(arrayList.size() > 0){//说明未获取服务器上的粉丝，客户端已经添加了粉丝
            arrTtemp = new String[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++){
                arrTtemp[i] = arrayList.get(0);
            }
        }
        arrayList.clear();
        for (String str : arr) {
            arrayList.add(str);
        }
        if(arrTtemp != null && arrTtemp.length > 0){
            for (String fid : arrTtemp){
                if(!arrayList.contains(fid)){//如果服务器端未包含本地的粉丝，则添加上
                    arrayList.add(0,fid);
                }
            }
        }
        mFollowerCount = arrayList.size();
    }

    /** 删除一个粉丝
     * @param afid
     */
    public synchronized void remove(String afid) {
        arrayList.remove(afid);
        mFollowerCount = arrayList.size();
    }

    /** 新增一个粉丝
     * @param afid
     */
    public synchronized void add(String afid) {
        if(!arrayList.contains(afid)) {
            arrayList.add(0, afid);
        }
        if(mIsLoadFollowers) {
            mFollowerCount = arrayList.size();
        }else {
            mFollowerCount ++;
        }
    }

    /** 获取粉丝数
     * @return
     */
    public int getSize() {
        return arrayList.size();
    }

    /** 当还未从服务器端获取粉丝列表时，调用此方法获取粉丝数
     * @return
     */
    public int getSizeForDefault(){
        if(!mIsLoadFollowers) {
            return mFollowerCount;
        }
        else {
            return arrayList.size();
        }
    }

    /** 设置是否已经下载过粉丝数据
     * @param isLoadFollowers
     */
    public void setIsLoadFollower(boolean isLoadFollowers){
        mIsLoadFollowers = isLoadFollowers;
    }

    /** 获取是否已下载粉丝数据
     * @return
     */
    public boolean getIsLoadFollower(){
        return  mIsLoadFollowers;
    }

    /**
     * 清空数据
     */
    public void clear(){
        mIsLoadFollowers = false;
        mFollowerCount = 0;
        arrayList.clear();
    }

    /** 设置粉丝数
     * @param followerCount
     */
    public void setFollowerCount(int followerCount){
        mFollowerCount = followerCount;
    }

    /** 获取粉丝AFID
     * @param page 第几页
     * @param size 每次显示的多少
     * @return
     */
    public String[] getAfids(int page, int size){
        count = page * size;
        int sizeTemp = getSize();
        if(count > sizeTemp)
        {
            count = sizeTemp - (page - 1) * size;
        }
        else{
            count = size;
        }
        if(count <= 0)
            return null;
        String[] newArray = new String[count];
        if(page == 1){
            startIndex = 0;
        }else {
            startIndex = (page - 1) * size;
        }
        System.arraycopy(arrayList.toArray(),startIndex,newArray,0,count);
        return newArray;
    }
}
