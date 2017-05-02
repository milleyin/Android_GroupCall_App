//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.afmobi.palmchat.ui.activity.palmcall;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import com.justalk.cloud.lemon.MtcApi;
import com.justalk.cloud.lemon.MtcCli;
import com.justalk.cloud.lemon.MtcProvDb;
import com.justalk.cloud.lemon.MtcUe;
import com.justalk.cloud.zmf.ZmfAudio;
import com.justalk.cloud.zmf.ZmfVideo;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class PalmcallLoginDelegate {
    private static WeakReference<PalmcallLoginDelegate.Callback> sCallback;
    private static int sInitState = 0;
    private static boolean mIsLogin = false;
    private static boolean mAutoLogin = false;
    private static BroadcastReceiver mLoginOkReceiver;
    private static BroadcastReceiver mLoginDidFailedReceiver;
    private static BroadcastReceiver mDidLogoutReceiver;
    private static BroadcastReceiver mLogoutedReceiver;
    private static BroadcastReceiver mAuthRequiredReceiver;
    private static BroadcastReceiver mAuthExpiredReceiver;
    private static BroadcastReceiver mNetworkChangedReceiver;

    public PalmcallLoginDelegate() {
    }

    private static String getProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List appProcesses = activityManager.getRunningAppProcesses();
        Iterator var4 = appProcesses.iterator();

        RunningAppProcessInfo appProcess;
        do {
            if(!var4.hasNext()) {
                return "";
            }

            appProcess = (RunningAppProcessInfo)var4.next();
        } while(appProcess.pid != pid);

        return appProcess.processName;
    }

    public static int init(Context context, String appKey) {
        if(!getProcessName(context).equals(context.getPackageName())) {
            return 2;
        } else {
            ZmfAudio.initialize(context);
            ZmfVideo.initialize(context);
            if(MtcApi.init(context, appKey, (JSONObject)null))  {
                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
                if(mLoginOkReceiver == null) {
                    mLoginOkReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            PalmcallLoginDelegate.mIsLogin = true;
                            PalmcallLoginDelegate.Callback callback = PalmcallLoginDelegate.getActiveCallback();
                            if(callback != null) {
                                callback.mtcLoginOk();
                            }

                        }
                    };
                    broadcastManager.registerReceiver(mLoginOkReceiver, new IntentFilter(MtcApi.MtcLoginOkNotification));
                }

                if(mLoginDidFailedReceiver == null) {
                    mLoginDidFailedReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            PalmcallLoginDelegate.Callback callback = PalmcallLoginDelegate.getActiveCallback();
                            if(callback != null) {
                                callback.mtcLoginDidFail();
                            }

                        }
                    };
                    broadcastManager.registerReceiver(mLoginDidFailedReceiver, new IntentFilter(MtcApi.MtcLoginDidFailNotification));
                }

                if(mDidLogoutReceiver == null) {
                    mDidLogoutReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            PalmcallLoginDelegate.Callback callback = PalmcallLoginDelegate.getActiveCallback();
                            PalmcallLoginDelegate.mIsLogin = false;
                            MtcProvDb.Mtc_ProvDbSetCurProfUser((String)null);
                            if(callback != null) {
                                callback.mtcLogoutOk();
                            }

                        }
                    };
                    broadcastManager.registerReceiver(mDidLogoutReceiver, new IntentFilter(MtcApi.MtcDidLogoutNotification));
                }

                if(mLogoutedReceiver == null) {
                    mLogoutedReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            PalmcallLoginDelegate.Callback callback = PalmcallLoginDelegate.getActiveCallback();
                            PalmcallLoginDelegate.mIsLogin = false;
                            if(callback != null) {
                                callback.mtcLogouted();
                            }

                        }
                    };
                    broadcastManager.registerReceiver(mLogoutedReceiver, new IntentFilter(MtcApi.MtcLogoutedNotification));
                }

                if(mAuthRequiredReceiver == null) {
                    mAuthRequiredReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            String id = null;
                            String nonce = null;

                            try {
                                String callback = intent.getStringExtra("extra_info");
                                JSONObject json = (JSONObject)(new JSONTokener(callback)).nextValue();
                                id = json.getString("MtcUeUriKey");
                                nonce = json.getString("MtcUeAuthNonceKey");
                            } catch (Exception var7) {
                                var7.printStackTrace();
                                return;
                            }

                            PalmcallLoginDelegate.Callback callback1 = PalmcallLoginDelegate.getActiveCallback();
                            if(callback1 != null) {
                                callback1.mtcAuthRequire(id, nonce);
                            }

                        }
                    };
                    broadcastManager.registerReceiver(mAuthRequiredReceiver, new IntentFilter("MtcUeAuthorizationRequireNotification"));
                }

                if(mAuthExpiredReceiver == null) {
                    mAuthExpiredReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            MtcUe.Mtc_UeRefreshAuth();
                        }
                    };
                    broadcastManager.registerReceiver(mAuthExpiredReceiver, new IntentFilter("MtcUeAuthorizationExpiredNotification"));
                }

                if(mNetworkChangedReceiver == null) {
                    mNetworkChangedReceiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            PalmcallLoginDelegate.networkChanged(context);
                        }
                    };
                    broadcastManager.registerReceiver(mNetworkChangedReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                }

                boolean bHasNet = hasNet(context);
                if(mAutoLogin && bHasNet) {
                    String currentUser = MtcProvDb.Mtc_ProvDbGetCurProfUser();
                    if(currentUser != null && currentUser.length() != 0) {
                        MtcApi.login(currentUser, (JSONObject)null);
                    }
                }

                sInitState = 1;
                return 1;
            } else {
                sInitState = 0;
                return 0;
            }
        }
    }

    public static int getInitState() {
        return sInitState;
    }

    private static void networkChanged(Context context) {
        boolean bHasNet = hasNet(context);
        int iClientState = MtcCli.Mtc_CliGetState();
       //MtcUtil.Mtc_AnyLogInfoStr("JusLogin", "networkChanged.");
        if(mAutoLogin && bHasNet && iClientState == -2) {
            String currentUser = MtcProvDb.Mtc_ProvDbGetCurProfUser();
            if(currentUser != null && currentUser.length() != 0) {
                MtcApi.login(currentUser, (JSONObject)null);
                return;
            }
        }

        if(MtcCli.Mtc_CliGetState() == 2) {
            MtcCli.Mtc_CliNetworkChanged(bHasNet?-1:-2);
        }

        if(mIsLogin) {
            if(bHasNet) {
                MtcCli.Mtc_CliWakeup(true);
            } else {
                MtcCli.Mtc_CliWakeup(false);
            }
        }

    }

    private static boolean hasNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public static boolean login(String user, String password, String server, String network) {
        if(user.length() != 0 && password.length() != 0 && server.length() != 0) {
            JSONObject json = new JSONObject();

            try {
                json.put("key_server_address", server);
                json.put("key_password", password);
                if(network != null && network.length() > 0) {
                    json.put("key_network_address", network);
                }
            } catch (JSONException var6) {
                var6.printStackTrace();
            }

            return MtcApi.login(user, json) == 0;
        } else {
            return false;
        }
    }

    public static boolean autologin() {
        String currentUser = MtcProvDb.Mtc_ProvDbGetCurProfUser();
        if(currentUser != null && currentUser != "") {
            MtcApi.login(currentUser, (JSONObject)null);
            return true;
        } else {
            return false;
        }
    }

    public static boolean logout() {
        return MtcApi.logout() == 0;
    }

    public static void destroy() {
        MtcApi.destroy();
        ZmfVideo.terminate();
        ZmfAudio.terminate();
    }

    public static boolean promptAuthCode(String Code) {
        return MtcUe.Mtc_UePromptAuthCode(Code) == 0;
    }

    public static void enterForeground() {
        if(mIsLogin) {
            MtcCli.Mtc_CliWakeup(true);
        }

    }

    public static void enterBackground() {
        if(mIsLogin) {
            MtcCli.Mtc_CliWakeup(false);
        }

    }

    public static void setCallback(PalmcallLoginDelegate.Callback callback) {
        sCallback = callback == null?null:new WeakReference(callback);
    }

    public static PalmcallLoginDelegate.Callback getCallback() {
        return sCallback == null?null:(PalmcallLoginDelegate.Callback)sCallback.get();
    }

    public static PalmcallLoginDelegate.Callback getActiveCallback() {
        PalmcallLoginDelegate.Callback callback = getCallback();
        return callback;
    }

    public static void setAutoLogin(boolean autoLogin) {
        mAutoLogin = autoLogin;
    }

    public interface InitStat {
        int MTC_INIT_FAIL = 0;
        int MTC_INIT_SUCCESS = 1;
        int MTC_INIT_ALREADY = 2;
    }

    public interface Callback {
        void mtcLoginOk();

        void mtcLoginDidFail();

        void mtcLogoutOk();

        void mtcLogouted();

        void mtcAuthRequire(String var1, String var2);
    }
}
