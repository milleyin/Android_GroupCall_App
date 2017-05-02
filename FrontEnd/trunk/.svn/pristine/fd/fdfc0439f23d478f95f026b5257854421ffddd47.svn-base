package com.afmobi.palmchat;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.MainTab;
import com.afmobi.palmchat.ui.activity.store.StoreFaceDetailActivity;
import com.afmobi.palmchat.ui.activity.store.StoreFragmentActivity;
//import com.afmobi.palmchat.util.image.ImageLoader;

public class MyActivityManager {

	private static final String TAG = MyActivityManager.class.getCanonicalName();

	private Stack<Activity> activityStack;
	private Stack<Fragment> fragmentStack;
	private Stack<BaseFragmentActivity> baseFragmentStack;

	private static MyActivityManager instance;

	/**
	 * 是否清空所有的导航，设置此变量主要是清空导航栈时，在baseActivity在从数据源删除数据，导航遍历关闭acitivity时出错
	 */
	public boolean isAllClear;
	private MyActivityManager() {
		activityStack = new Stack<Activity>();
		fragmentStack = new Stack<Fragment>();
		baseFragmentStack = new Stack<BaseFragmentActivity>();
	}

	public static MyActivityManager getScreenManager() {
		if (instance == null) {
			instance = new MyActivityManager();
		}
		return instance;
	}

	public static Stack<Activity> getActivityStack() {
		return getScreenManager().activityStack;
	}

	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	public void popActivity(Class<?> cls) {
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			Activity activity = activityStack.get(i);
			if (activity != null) {
				if (activity.getClass().equals(cls)) {
					popActivity(activity);
					break;
				}
			} else {
				break;
			}
		}
	}

	public void popActivity() {
		if (activityStack.size() > 0) {
			Activity activity = activityStack.pop();
			if (activity != null) {
				activity.finish();
			}
			System.out.println(activity + "");
		}
	}

	public Activity currentActivity() {
		Activity activity = null;
		if (!activityStack.empty()) {
			activity = activityStack.lastElement();
		}
		return activity;
	}

	public BaseFragmentActivity currentFragmentActivity() {
		BaseFragmentActivity activity = null;
		if (!baseFragmentStack.empty()) {
			activity = baseFragmentStack.lastElement();
		}
		return activity;
	}

	public Fragment currentFragment() {
		Fragment fragment = null;
		if (!fragmentStack.empty()) {
			fragment = fragmentStack.lastElement();
			System.out.println("MyActivityManager  fragment  " + fragment);
		}
		return fragment;
	}

	public void pushActivity(Activity activity) {
		PalmchatLogUtils.i(TAG, "pushActivity==" + activity);
		activityStack.add(activity);
	}

	public void pushBaseFragmentActivity(BaseFragmentActivity baseFragmentActivity) {
		PalmchatLogUtils.i(TAG, "pushBaseFragmentActivity==" + baseFragmentActivity);
		baseFragmentStack.add(baseFragmentActivity);
	}

	public void popBaseFragmentActivity(BaseFragmentActivity baseFragmentActivity) {
		baseFragmentStack.remove(baseFragmentActivity);
	}

	public void pushFragment(Fragment fragment) {
		PalmchatLogUtils.println("--stack push fragment = " + fragment);
		fragmentStack.add(fragment);
	}

	public void popFragment(Fragment fragment) {
		PalmchatLogUtils.println("--stack pop fragment = " + fragment);
		fragmentStack.remove(fragment);
	}

	public boolean isExistsActivity(Class<?> cls) {
		boolean isExists = false;
		for (int i = 0; i < activityStack.size(); i++) {
			Activity activity = activityStack.get(i);
			if (cls.equals(activity.getClass())) {
				isExists = true;
				break;
			}
		}
		return isExists;
	}

	/**
	 * 获取指定的activity
	 * 
	 * @param cls
	 * @return
	 */
	public Activity getActivity(Class<?> cls) {
		for (int i = 0; i < activityStack.size(); i++) {
			Activity activity = activityStack.get(i);
			if (cls.equals(activity.getClass())) {
				return activity;
			}
		}
		return null;
	}

	public Fragment getFragment(String fragment_name) {
		Fragment frag = null;
		for (int i = 0; i < fragmentStack.size(); i++) {
			frag = fragmentStack.get(i);
			if (frag.getClass().getName().equals(fragment_name)) {
				break;
			}
		}
		PalmchatLogUtils.println("getFragment " + frag.getClass().getName());
		return frag;
	}

	public static Stack<Fragment> getFragmentStack() {
		return getScreenManager().fragmentStack;
	}

	public boolean isHasFragment(Fragment fragment) {
		boolean isExists = false;
		for (int i = 0; i < fragmentStack.size(); i++) {
			Fragment frag = fragmentStack.get(i);
			if (fragment.getClass().getName().equals(frag.getClass().getName())) {
				isExists = true;
				break;
			}
		}
		PalmchatLogUtils.println("isHasFragment  isExists  " + isExists);
		return isExists;
	}

	private void popAllActivityExceptOne() {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			popActivity(activity);
		}
	}

	public boolean IsEmpty() {
		return (null != activityStack && (0 == activityStack.size()));
	}

	public void popAllActivityExceptOne(Class<?> cls) {
		Activity topActivity = null;
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (activity.getClass().equals(cls)) {
				activityStack.remove(activity);
				topActivity = activity;
				continue;
			}
			popActivity(activity);
		}
		if (topActivity != null) {
			activityStack.add(0, topActivity);
		}
	}

	/**
	 * get current activity
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		if (activityStack != null && activityStack.size() > 0) {
			return activityStack.get(activityStack.size() - 1);
		}
		return null;
	}

	/**
	 * get current Fragment
	 * 
	 * @return
	 */
	public Fragment getCurrentFragment() {
		if (fragmentStack != null && fragmentStack.size() > 0) {
			return fragmentStack.get(fragmentStack.size() - 1);
		}
		return null;
	}

	public Context getCurrentBaseFragmentActivity() {
		if (baseFragmentStack != null && baseFragmentStack.size() > 0) {
			PalmchatLogUtils.i(TAG, "getCurrentBaseFragmentActivity  baseFragmentStack.get(0)  " + baseFragmentStack.get(0));
			return baseFragmentStack.get(0);
		}
		PalmchatLogUtils.i(TAG, "getCurrentBaseFragmentActivity  PalmchatApp.getApplication()  " + PalmchatApp.getApplication());
		return PalmchatApp.getApplication();
	}

	/**
	 * Clear Cache data
	 */
	public void clear() {
		isAllClear = true;
		popFragmentActivity(StoreFragmentActivity.class);
		popAllFragment(MyActivityManager.getScreenManager().getCurrentBaseFragmentActivity());
		popAllActivity();
		popAllFragmentActivity();

		activityStack.clear();
		fragmentStack.clear();
		baseFragmentStack.clear();

//		ImageLoader.getInstance().cleanOtherImageCache();
		if (PalmchatApp.getApplication().mMemoryCache != null) {
			PalmchatApp.getApplication().mMemoryCache.evictAll();
		}
		/*if (PalmchatApp.getApplication().mMemoryBitmapCache != null) {
			PalmchatApp.getApplication().mMemoryBitmapCache.evictAll();
		}*/
		isAllClear = false;
	}

	private void popFragmentActivity(Class<StoreFragmentActivity> class1) {
		for (int i = baseFragmentStack.size() - 1; i >= 0; i--) {
			BaseFragmentActivity activity = baseFragmentStack.get(i);
			if (activity != null) {
				if (activity.getClass().equals(class1)) {
					if (activity != null) {
						activity.finish();
						baseFragmentStack.remove(activity);
						activity = null;
					}
					break;
				}
			} else {
				break;
			}
		}

		for (Activity act : MyActivityManager.getActivityStack()) {
			PalmchatLogUtils.e("popFragmentActivity", act.getClass().getName());
			if (act instanceof StoreFaceDetailActivity) {
				MyActivityManager.getScreenManager().popActivity(act);
			}
		}

		Activity activity = PalmchatApp.getCurActivity();
		PalmchatLogUtils.println("getCurActivity :" + activity);
		if (activity != null) {
			activity.finish();
		}
	}

	private void popAllActivity() {
		if (null != activityStack) {
			for (int i = 0; i < activityStack.size(); i++) {
				activityStack.get(i).finish();
			}
		}
	}

	private void popAllFragmentActivity() {
		if (null != baseFragmentStack) {
			for (int i = 0; i < baseFragmentStack.size(); i++) {
				baseFragmentStack.get(i).finish();
			}
		}
	}

	private void popAllFragment(Context mContext) {
		if (fragmentStack != null && fragmentStack.size() > 0) {
			int size = fragmentStack.size();
			for (int i = 0; i < size; i++) {
				try {
					Fragment fragment = currentFragment();
					if (fragment != null && mContext instanceof MainTab) {
						MainTab mainAct = (MainTab) mContext;
						PalmchatLogUtils.i(TAG, "popAllFragment  begin  mainAct  " + mainAct + "  fragment  " + fragment);
						if (mainAct != null && !mainAct.isFinishing()) {
							// int result =
							// mainAct.getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
							// int result =
							// mainAct.getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
							// PalmchatLogUtils.println("popAllFragment result:"+result);
							popFragment(fragment);
							// PalmchatLogUtils.println("popAllFragment result isPop:"+count);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	//------------- add by HJG 2015-12-08  start  ------------------
	/**
	 * remove all except self
	 * @param cls
	 */
	public synchronized void popAllActivityExceptOneWithRouteChange(Class<? extends Activity> cls){
		if(cls==null){
			return;
		}
		
		boolean isFindInBaseActivityStack = false;
		
		Stack<Activity> tempActivityStack = new Stack<Activity>();
		if(activityStack!=null && activityStack.size()>0){
			
			tempActivityStack.clear();
			
			for(Activity mActivity : activityStack){
				if(cls.equals(mActivity.getClass())){
					isFindInBaseActivityStack = true;
				}else{
					tempActivityStack.add(mActivity);
				}
			}
			
			activityStack.removeAll(tempActivityStack);
			
			if(tempActivityStack!=null && tempActivityStack.size()>0){
				for(Activity mActivity : tempActivityStack){
					mActivity.finish();
				}
			}
		}
		
		
		if(baseFragmentStack!=null && baseFragmentStack.size()>0){
			
			tempActivityStack.clear();
			
			for(Activity mActivity : baseFragmentStack){
				if(cls.equals(mActivity.getClass())){
					// activity has in activityStack, so if this activity also in baseFragmentStack, than remove it .
					if(isFindInBaseActivityStack){
						tempActivityStack.add(mActivity);
					}
				}else{
					tempActivityStack.add(mActivity);
				}
			}
			
			baseFragmentStack.removeAll(tempActivityStack);
			
			if(tempActivityStack!=null && tempActivityStack.size()>0){
				for(Activity mActivity : tempActivityStack){
					mActivity.finish();
				}
			}
		}
		
		tempActivityStack.clear();
		tempActivityStack = null;
		
	}
	
	/**
	 * remove from Stack
	 * @param cls
	 */
	public synchronized void popActivityWithRouteChange(Class<?> cls) {
		if(cls==null){
			return;
		}
		
		Activity activity = null;
		
		PalmchatLogUtils.e("HJG", ""+(cls));
		
		if(activityStack!=null && activityStack.size()>0){
			for (int i = activityStack.size() - 1; i >= 0; i--) {
				activity = activityStack.get(i);
				PalmchatLogUtils.e("HJG", "----activityStack->: "+(activity==null?"null":activity.getClass()));
				if (activity != null && activity.getClass().equals(cls)) {
					activity.finish();
					activityStack.remove(activity);
					activity = null;
					break;
				}
			}
		}
		
		if(baseFragmentStack!=null && baseFragmentStack.size()>0){
			for (int i = baseFragmentStack.size() - 1; i >= 0; i--) {
				activity = baseFragmentStack.get(i);
				PalmchatLogUtils.e("HJG", "---baseFragmentStack->: "+(activity==null?"null":activity.getClass()));
				if (activity != null && activity.getClass().equals(cls)) {
					activity.finish();
					baseFragmentStack.remove(activity);
					activity = null;
					break;
				} 
			}
		}
		
		activity = null;
		
	}
	
	//------------- add by HJG 2015-12-08  end    ------------------

}
