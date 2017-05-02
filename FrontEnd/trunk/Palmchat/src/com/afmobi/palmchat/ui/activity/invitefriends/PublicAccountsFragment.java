package com.afmobi.palmchat.ui.activity.invitefriends;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.eventbusmodel.EventFollowNotice;
import com.afmobigroup.gphone.R;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView;
import com.afmobi.palmchat.ui.activity.chattingroom.widget.XListView.IXListViewListener;
import com.afmobi.palmchat.ui.activity.main.building.BaseFragment;
import com.afmobi.palmchat.ui.activity.publicaccounts.PublicAccountDetailsActivity;
import com.afmobi.palmchat.util.CommonUtils;
//import com.afmobi.palmchat.util.image.ImageOnScrollListener;
//import com.afmobi.palmchat.util.image.ListViewAddOn;
import com.core.AfFriendInfo;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.listener.AfHttpResultListener;
//import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public class PublicAccountsFragment extends BaseFragment implements OnItemClickListener, IXListViewListener, AfHttpResultListener {

    private static final String TAG = PublicAccountsFragment.class.getSimpleName();
    private EditText mLikeSearch;

    private ImageView vImageViewBack, mCancel_btn;
    private Dialog dialog;
    private XListView mListView;
    private PublicAccountsAdapter mAdapter;

    /**
     * whole public accounts list data
     */
    private ArrayList<AfFriendInfo> mOriginListData = new ArrayList<AfFriendInfo>();
    /**
     * data for display(search result)
     */
    private ArrayList<AfFriendInfo> mListData = new ArrayList<AfFriendInfo>();


    //	/**
//	 * whole public accounts list data
//	 */
//	private ArrayList<AfFriendInfo> mOriginListData = new ArrayList<AfFriendInfo>();
//	/**
//	 * data for display(search result)
//	 */
//	private ArrayList<AfFriendInfo> mListData = new ArrayList<AfFriendInfo>();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
    //	private ListViewAddOn mListViewAddOn = new ListViewAddOn();
    private boolean isRefresh = false;

    private LooperThread mLooperThread;

    public PublicAccountsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mLooperThread = new LooperThread();
        mLooperThread.setName(TAG);
        mLooperThread.start();
        EventBus.getDefault().register(this);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.fragment_public_accounts);
        initViews();
        return mMainView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//		MobclickAgent.onPageStart(TAG);

		/*List<AfFriendInfo> list = CacheManager.getInstance().getmPublicAccountsList();
        if (list != null && list.size() > 0) {
			
			Handler handler = mLooperThread.handler;
			if (null != handler) {
				handler.obtainMessage(LooperThread.UPDATE_LIST, list).sendToTarget();
			}
			
		}*/
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//		MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mLooperThread.looper.quit();
    }


    private void initViews() {
        ((TextView) findViewById(R.id.title_text)).setText(R.string.home_left_item_public_account);
        mLikeSearch = (EditText) findViewById(R.id.search_et);
        mListView = (XListView) findViewById(R.id.account_listview);
        mListView.setOnItemClickListener(this);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);

//		mListView.setOnScrollListener(new ImageOnScrollListener(mListView ));


        mAdapter = new PublicAccountsAdapter(context, mListData);
        mListView.setAdapter(mAdapter);
        refreshListData();

        vImageViewBack = (ImageView) findViewById(R.id.back_button);
//		vImageViewBack.setBackgroundResource(R.drawable.t_home);
        vImageViewBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//				if(menu != null){
//					menu.showMenu();
//				}
                if (null != getActivity()) {
                    getActivity().finish();
                }
            }
        });

        mCancel_btn = (ImageView) findViewById(R.id.cancel_btn);
        mLikeSearch.addTextChangedListener(new SearchTextWatcher());
        mCancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mLikeSearch.getText().clear();
            }
        });
    }

    private void refreshListData() {

        // is not pull to refresh
        if (!isRefresh) {
            showProgressDialog();
        }
        List<AfFriendInfo> list = CacheManager.getInstance().getmPublicAccountsList();
        long time = CacheManager.getInstance().getmPublicAccountsTime();

        if (list != null && list.size() > 0 && CommonUtils.isOverTenMini(System.currentTimeMillis(), time) && !isRefresh) {

            dismissProgressDialog();
            stopRefresh();


            for (AfFriendInfo friend : list) {
                AfFriendInfo searchFrd = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).search(friend, false, true);
                if (searchFrd != null) {
                    friend.isFollow = true;
                } else {
                    friend.isFollow = false;
                }

            }
            /*过滤可以unfollow的数据*/
            list = filtUnFollowData(list);

            mOriginListData.clear();
            mOriginListData.addAll(list);

            mListData.clear();
            mListData.addAll(list);
            mAdapter.notifyDataSetChanged();

        } else {
            mAfCorePalmchat.AfHttpPublicAccountGetList(true, null, this);
        }


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void AfOnResult(int httpHandle, int flag, int code, int http_code, Object result, Object user_data) {
        // TODO Auto-generated method stub
        PalmchatLogUtils.println("PublicAccountsFragment  code " + code + "  flag  " + flag + "  result  " + result + "  user_data  " + user_data);
        if (fragmentActivity != null) {
            dismissProgressDialog();
        }
        if (isRefresh) {
            stopRefresh();
        }

        if (code == Consts.REQ_CODE_SUCCESS) {
            switch (flag) {
                case Consts.REQ_PUBLIC_ACCOUNT_LIST:

                    if (result != null) {
                        AfFriendInfo[] arrays = (AfFriendInfo[]) result;
                        PalmchatLogUtils.println("public account list size:" + arrays.length);
                        Handler handler = mLooperThread.handler;
                        if (null != handler) {
                            handler.obtainMessage(LooperThread.UPDATE_LIST, Arrays.asList(arrays)).sendToTarget();
                        }

//					AfFriendInfo[] arrays = (AfFriendInfo[]) result;
//					PalmchatLogUtils.println("public account list size:" + arrays.length);
//					Handler handler = mLooperThread.handler;
//					if (null != handler) {
//						handler.obtainMessage(LooperThread.UPDATE_LIST, Arrays.asList(arrays)).sendToTarget();
//					}

                    }

                    break;

                default:
                    break;
            }
        } else {

            if (null != context && !context.isFinishing() && isAdded()) {
                Consts.getInstance().showToast(context, code, flag, http_code);
            }

            switch (flag) {
                case Consts.REQ_PUBLIC_ACCOUNT_LIST:

                    break;

                default:
                    break;
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (position <= mAdapter.getCount() - 1) {
            int pos = (int) mAdapter.getItemId(position);
            AfFriendInfo friendInfo = mListData.get(pos);
            toDetails(friendInfo);
        }
    }

    /**
     * EventBus回调
     *
     * @param
     */
    public void onEventMainThread(EventFollowNotice eventFollowState) {
        if(mAdapter!=null) {
            for (AfFriendInfo friend : mOriginListData) {
                AfFriendInfo searchFrd = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).search(friend, false, true);
                if (searchFrd != null) {
                    friend.isFollow = true;
                } else {
                    friend.isFollow = false;
                }
            }
            mAdapter.notifyDataSetChanged();


        }
    }

    private void toDetails(AfFriendInfo info) {
        Intent intent = new Intent(getActivity(), PublicAccountDetailsActivity.class);
        intent.putExtra("Info", info);
        startActivity(intent);
    }

    private void stopRefresh() {
        isRefresh = false;
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(mDateFormat.format(new Date(System.currentTimeMillis())));
    }

    ;

    @Override
    public void onRefresh(View view) {
        // TODO Auto-generated method stub
        isRefresh = true;

        refreshListData();
    }

    @Override
    public void onLoadMore(View view) {
        // TODO Auto-generated method stub

    }

    private class SearchParams {
        String mSearch;
        List<AfFriendInfo> mList;
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case LooperThread.SEARCH_FRIENDS:

                    SearchParams params = (SearchParams) msg.obj;
                    params.mList = filtUnFollowData(params.mList);
                    mListData.clear();
                    mListData.addAll(params.mList);
                    mAdapter.notifyDataSetChanged();
                    break;

                case LooperThread.UPDATE_LIST:
                    List<AfFriendInfo> displayList = (List<AfFriendInfo>) msg.obj;

                    displayList = filtUnFollowData(displayList);

                    CacheManager.getInstance().setmPublicAccountsList(displayList);
                    CacheManager.getInstance().setmPublicAccountsTime(System.currentTimeMillis());


                    mOriginListData.clear();
                    mOriginListData.addAll(displayList);

                    mListData.clear();
                    mListData.addAll(displayList);
                    mAdapter.notifyDataSetChanged();

//				List<AfFriendInfo> displayList = (List<AfFriendInfo>) msg.obj;
//
//				CacheManager.getInstance().setmPublicAccountsList(displayList);
//				CacheManager.getInstance().setmPublicAccountsTime(System.currentTimeMillis());
//
//				mOriginListData.clear();
//				mOriginListData.addAll(displayList);
//
//				mListData.clear();
//				mListData.addAll(displayList);
//				mAdapter.notifyDataSetChanged();

                    break;

            }

        }

    };

    private class SearchTextWatcher implements TextWatcher {

        @SuppressWarnings("unchecked")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


            String text = s.toString();
            if (text == null || text.length() == 0 && mAdapter != null) {


                mListData.clear();
                mListData.addAll(mOriginListData);
                mAdapter.notifyDataSetChanged();

                mCancel_btn.setVisibility(View.GONE);

                return;
            }

            try {
                search(text, mOriginListData);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mCancel_btn.setVisibility(View.VISIBLE);

        }

    }

    @SuppressLint("DefaultLocale")
    private void search(String search, List<AfFriendInfo> list) {

        if (list != null) {

            if (null != mLooperThread) {
                Handler handler = mLooperThread.handler;
                if (null != handler) {

                    SearchParams ms = new SearchParams();
                    ms.mSearch = search;
                    ms.mList = list;

                    handler.obtainMessage(LooperThread.SEARCH_FRIENDS, ms).sendToTarget();
                }
            }
        }

    }

    class LooperThread extends Thread {

        private static final int SEARCH_FRIENDS = 200;
        private static final int UPDATE_LIST = 201;

        Handler handler;
        Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {

                        case SEARCH_FRIENDS:
                            SearchParams params = (SearchParams) msg.obj;
                            String search = params.mSearch;
                            List<AfFriendInfo> list = params.mList;
                            ArrayList<AfFriendInfo> results = new ArrayList<AfFriendInfo>();

                            Pattern pattern = null;
                            try {

                                pattern = Pattern.compile(search.toUpperCase());
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();

                            }

                            if (pattern == null) {
                                break;
                            }

                            for (int i = 0; i < list.size(); i++) {

                                if (list.get(i) == null) {
                                    break;
                                }


                                String str = list.get(i).name;
                                if (str != null) {

                                    Matcher matcher2 = pattern.matcher(str.toUpperCase());
                                    if (matcher2.find()) {

                                        results.add(list.get(i));

                                        continue;
                                    }

                                }

                                if (list.get(i).afId != null) {
                                    Matcher matcher3 = pattern.matcher((list.get(i).afId));
                                    if (matcher3.find()) {
                                        results.add(list.get(i));
                                    }
                                }

                            }

                            params.mList = results;
                            mHandler.obtainMessage(SEARCH_FRIENDS, params).sendToTarget();

                            break;

                        case UPDATE_LIST:
                            List<AfFriendInfo> listTemp = (List<AfFriendInfo>) msg.obj;
                            for (AfFriendInfo friend : listTemp) {
                                AfFriendInfo searchFrd = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).search(friend, false, true);
                                if (searchFrd != null) {
                                    friend.isFollow = true;
                                } else {
                                    friend.isFollow = false;
                                }
                            }

                            mHandler.obtainMessage(UPDATE_LIST, listTemp).sendToTarget();

//						List<AfFriendInfo> listTemp = (List<AfFriendInfo>) msg.obj;
//						for(AfFriendInfo friend : listTemp) {
//							AfFriendInfo searchFrd = CacheManager.getInstance().getFriendsCacheSortListEx(Consts.AFMOBI_FRIEND_TYPE_PUBLIC_ACCOUNT).search(friend, false, true);
//							if (searchFrd != null) {
//								friend.isFollow = true;
//							} else {
//								friend.isFollow = false;
//							}
//						}
//
//						mHandler.obtainMessage(UPDATE_LIST, listTemp).sendToTarget();
                            break;

                    }
                }

            };

            Looper.loop();

        }

    }

    public void showProgressDialog() {
        if (isAdded()) {
            if (dialog == null) {
                dialog = new Dialog(fragmentActivity, R.style.Theme_LargeDialog);
                dialog.setOnKeyListener(fragmentActivity);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_loading);
            }

            dialog.show();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.cancel();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 只显示可以unfollow的公账号
     *
     * @param dataLs
     * @return
     */
    private List<AfFriendInfo> filtUnFollowData(List<AfFriendInfo> dataLs) {
        List<AfFriendInfo> profileInfoLs = new ArrayList<AfFriendInfo>();
        for (AfFriendInfo afFriendInfo : dataLs) {
            if (afFriendInfo.unfollow_opr == false)
                profileInfoLs.add(afFriendInfo);

        }
        return profileInfoLs;
    }


}
