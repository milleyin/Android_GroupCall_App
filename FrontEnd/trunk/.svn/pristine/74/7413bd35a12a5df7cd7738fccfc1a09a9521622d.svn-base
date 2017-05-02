package com.afmobi.palmchat.util.Bluetooth;

/**
 * Created by starw on 2016/9/12.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;

@TargetApi(11)
public abstract class Bluetooth {
    private final String TAG = Bluetooth.class.getSimpleName();
    protected Context mContext;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothHeadset mBluetoothHeadset = null;
    private BluetoothDevice mConnectedHeadset = null;
    private HashSet<BluetoothDevice> mBluetoothDevices = new HashSet();
    private AudioManager mAudioManager;
    private final String BLUETOOTH_ADDRESS = "00::00:00::00";
    private final String BLUETOOTH_NAME = "Bluetooth Headset";
    private boolean mIsCountDownOn;
    private boolean mIsStarting;
    private boolean mIsHeadsetConnected;
    private boolean mIsOnHeadsetSco;
    private boolean mIsStarted;
    private BroadcastReceiver mBroadcastReceiver = null;
    private CountDownTimer mCountDown = null;
    private BluetoothProfile.ServiceListener mHeadsetProfileListener = null;
    private BroadcastReceiver mHeadsetBroadcastReceiver = null;
    private CountDownTimer mCountDown11 = null;

    public Bluetooth(Context context) {
        this.mContext = context;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mAudioManager = (AudioManager)context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if(Build.VERSION.SDK_INT < 11) {
            this.mBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
                        BluetoothDevice state = (BluetoothDevice)intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        BluetoothClass bluetoothClass = state.getBluetoothClass();
                        if(bluetoothClass != null) {
                            int deviceClass = bluetoothClass.getDeviceClass();
                            if(deviceClass == 1032 || deviceClass == 1028) {
                                mConnectedHeadset = state;
                                if(!mIsHeadsetConnected) {
                                    mIsHeadsetConnected = true;
                                    onHeadsetConnected(state.getAddress(), state.getName());
                                }
                            }
                        }

                        Log.d(TAG, state.getName() + " connected");
                    } else if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                        Log.d(TAG, "Headset disconnected");
                        if(mIsCountDownOn) {
                            mIsCountDownOn = false;
                            mCountDown.cancel();
                        }

                        mAudioManager.setMode(0);
                        if(mIsHeadsetConnected) {
                            mIsHeadsetConnected = false;
                            onHeadsetDisconnected(mConnectedHeadset != null?mConnectedHeadset.getAddress():"00::00:00::00");
                        }

                        mConnectedHeadset = null;
                    } else if(action.equals(AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED)) {
                        int state1 = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
                        if(state1 == 1) {
                            if(mIsStarting) {
                                mIsStarting = false;
                                if(!mIsHeadsetConnected) {
                                    mIsHeadsetConnected = true;
                                    onHeadsetConnected("00::00:00::00", "Bluetooth Headset");
                                }
                            }

                            if(mIsCountDownOn) {
                                mIsCountDownOn = false;
                                mCountDown.cancel();
                            }

                            if(!mIsOnHeadsetSco) {
                                mIsOnHeadsetSco = true;
                                onScoAudioConnected();
                            }

                            Log.d(TAG, "Sco connected");
                        } else if(state1 == 0) {
                            Log.d(TAG, "Sco disconnected");
                            if(!mIsStarting) {
                                mAudioManager.stopBluetoothSco();
                                mAudioManager.setBluetoothScoOn(false);
                                if(mIsOnHeadsetSco) {
                                    mIsOnHeadsetSco = false;
                                    onScoAudioDisconnected();
                                }
                            }
                        }
                    }

                }
            };
            this.mCountDown = new CountDownTimer(10000L,0x1) {
                public void onTick(long millisUntilFinished) {
                    mAudioManager.setBluetoothScoOn(true);
                    mAudioManager.startBluetoothSco();
                    Log.d(TAG, "\nonTick start bluetooth Sco");
                }

                public void onFinish() {
                    mIsCountDownOn = false;
                    mAudioManager.setMode(0);
                    Log.d(TAG, "\nonFinish fail to connect to headset audio");
                }
            };
        } else {
            this.mHeadsetProfileListener = new BluetoothProfile.ServiceListener() {
                public void onServiceDisconnected(int profile) {
                    Log.d(TAG, "Profile listener onServiceDisconnected");
                    stopBluetooth11();
                }

                @TargetApi(11)
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    Log.d(TAG, "Profile listener onServiceConnected");
                    mBluetoothHeadset = (BluetoothHeadset)proxy;
                    Iterator i$ = mBluetoothHeadset.getConnectedDevices().iterator();

                    while(i$.hasNext()) {
                        BluetoothDevice device = (BluetoothDevice)i$.next();
                        if(!mBluetoothDevices.contains(device)) {
                            mBluetoothDevices.add(device);
                            onHeadsetConnected(device.getAddress(), device.getName());
                        }
                    }

                    mHeadsetBroadcastReceiver = new BroadcastReceiver() {
                        @TargetApi(11)
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            int state;
                            if(action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                                state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0);
                                BluetoothDevice device = (BluetoothDevice)intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                Log.d(TAG, "\nAction = " + action + "\nState = " + state);
                                if(state == 2) {
                                    if(!mBluetoothDevices.contains(device)) {
                                        mBluetoothDevices.add(device);
                                        onHeadsetConnected(device.getAddress(), device.getName());
                                    }
                                } else if(state == 0) {
                                    if(device.equals(mConnectedHeadset)) {
                                        if(mIsCountDownOn) {
                                            mIsCountDownOn = false;
                                            mCountDown11.cancel();
                                        }

                                        mConnectedHeadset = null;
                                    }

                                    if(mBluetoothDevices.contains(device)) {
                                        mBluetoothDevices.remove(device);
                                        onHeadsetDisconnected(device.getAddress());
                                    }
                                }
                            } else {
                                state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 10);
                                Log.d(TAG, "\nAction = " + action + "\nState = " + state);
                                if(state == 12) {
                                    Log.d(TAG, "\nHeadset audio connected");
                                    if(mIsCountDownOn) {
                                        mIsCountDownOn = false;
                                        mCountDown11.cancel();
                                    }

                                    if(!mIsOnHeadsetSco) {
                                        mIsOnHeadsetSco = true;
                                        onScoAudioConnected();
                                    }
                                } else if(state == 10) {
                                    if(Build.VERSION.SDK_INT < 18) {
                                        mAudioManager.stopBluetoothSco();
                                        mAudioManager.setBluetoothScoOn(false);
                                    } else {
                                        try {
                                            Class e = mBluetoothHeadset.getClass();
                                            Method method = e.getDeclaredMethod("stopScoUsingVirtualVoiceCall", new Class[]{BluetoothDevice.class});
                                            method.invoke(mBluetoothHeadset, new Object[]{mConnectedHeadset});
                                        } catch (Exception var8) {
                                            var8.printStackTrace();
                                        }
                                    }

                                    if(mIsOnHeadsetSco) {
                                        mIsOnHeadsetSco = false;
                                        onScoAudioDisconnected();
                                    }

                                    Log.d(TAG, "Headset audio disconnected");
                                }
                            }

                        }
                    };
                    mContext.registerReceiver(mHeadsetBroadcastReceiver, new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED));
                    mContext.registerReceiver(mHeadsetBroadcastReceiver, new IntentFilter(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED));
                }
            };
            this.mCountDown11 = new CountDownTimer(10000L, 0x1) {
                @TargetApi(11)
                public void onTick(long millisUntilFinished) {
                    if(Build.VERSION.SDK_INT < 18) {
                        mAudioManager.startBluetoothSco();
                        mAudioManager.setBluetoothScoOn(true);
                    } else {
                        try {
                            Class e = mBluetoothHeadset.getClass();
                            Method method = e.getDeclaredMethod("startScoUsingVirtualVoiceCall", new Class[]{BluetoothDevice.class});
                            method.invoke(mBluetoothHeadset, new Object[]{mConnectedHeadset});
                        } catch (Exception var5) {
                            var5.printStackTrace();
                        }
                    }

                    Log.d(TAG, "onTick startVoiceRecognition");
                }

                public void onFinish() {
                    mIsCountDownOn = false;
                    Log.d(TAG, "\nonFinish fail to connect to headset audio");
                }
            };
        }

    }

    public boolean start() {
        if(!this.mIsStarted) {
            this.mIsStarted = true;
            if(Build.VERSION.SDK_INT < 11) {
                this.mIsStarted = this.startBluetooth();
            } else {
                this.mIsStarted = this.startBluetooth11();
            }
        }

        return this.mIsStarted;
    }

    public boolean link(String address) {
        if(Build.VERSION.SDK_INT < 11) {
            if(this.mIsHeadsetConnected && !this.mIsCountDownOn && !this.mIsOnHeadsetSco) {
                this.mAudioManager.setMode(2);
                this.mIsCountDownOn = true;
                this.mCountDown.start();
                Log.d(TAG, "Start link count down");
                return true;
            }
        } else {
            if(this.mConnectedHeadset != null) {
                if(this.mConnectedHeadset.getAddress().equals(address)) {
                    return true;
                }

                if(this.mIsCountDownOn) {
                    this.mIsCountDownOn = false;
                    this.mCountDown11.cancel();
                }

                if(this.mIsOnHeadsetSco) {
                    if(Build.VERSION.SDK_INT < 18) {
                        this.mAudioManager.stopBluetoothSco();
                        this.mAudioManager.setBluetoothScoOn(false);
                    } else {
                        try {
                            Class items = this.mBluetoothHeadset.getClass();
                            Method device = items.getDeclaredMethod("stopScoUsingVirtualVoiceCall", new Class[]{BluetoothDevice.class});
                            device.invoke(this.mBluetoothHeadset, new Object[]{this.mConnectedHeadset});
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }
                    }
                }

                this.mConnectedHeadset = null;
            }

            if(!this.mIsCountDownOn) {
                Iterator items1 = this.mBluetoothDevices.iterator();

                while(items1.hasNext()) {
                    BluetoothDevice device1 = (BluetoothDevice)items1.next();
                    if(device1.getAddress().equals(address)) {
                        this.mConnectedHeadset = device1;
                        this.mIsCountDownOn = true;
                        this.mCountDown11.start();
                        Log.d(TAG, "Start link count down");
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean unlink() {
        if(Build.VERSION.SDK_INT < 11) {
            if(this.mIsHeadsetConnected) {
                if(this.mIsCountDownOn) {
                    this.mIsCountDownOn = false;
                    this.mCountDown.cancel();
                }

                if(this.mIsOnHeadsetSco) {
                    this.mAudioManager.stopBluetoothSco();
                    this.mAudioManager.setBluetoothScoOn(false);
                }

                return true;
            }
        } else if(this.mConnectedHeadset != null) {
            if(this.mIsCountDownOn) {
                this.mIsCountDownOn = false;
                this.mCountDown11.cancel();
            }

            if(this.mIsOnHeadsetSco) {
                if(Build.VERSION.SDK_INT < 18) {
                    this.mAudioManager.stopBluetoothSco();
                    this.mAudioManager.setBluetoothScoOn(false);
                } else {
                    try {
                        Class e = this.mBluetoothHeadset.getClass();
                        Method method = e.getDeclaredMethod("stopScoUsingVirtualVoiceCall", new Class[]{BluetoothDevice.class});
                        method.invoke(this.mBluetoothHeadset, new Object[]{this.mConnectedHeadset});
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }
            }

            this.mConnectedHeadset = null;
            return true;
        }

        return false;
    }

    public void stop() {
        if(this.mIsStarted) {
            this.mIsStarted = false;
            if(Build.VERSION.SDK_INT < 11) {
                this.stopBluetooth();
            } else {
                this.stopBluetooth11();
            }
        }

    }

    public boolean isHeadsetConnected() {
        return Build.VERSION.SDK_INT < 11?this.mIsHeadsetConnected:!this.mBluetoothDevices.isEmpty();
    }

    public boolean isOnHeadsetSco() {
        return this.mIsOnHeadsetSco;
    }

    public abstract void onHeadsetDisconnected(String var1);

    public abstract void onHeadsetConnected(String var1, String var2);

    public abstract void onScoAudioDisconnected();

    public abstract void onScoAudioConnected();

    private boolean startBluetooth() {
        Log.d(TAG, "startBluetooth");
        if(this.mBluetoothAdapter != null && this.mAudioManager.isBluetoothScoAvailableOffCall()) {
            this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
            this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
            this.mContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.media.SCO_AUDIO_STATE_CHANGED"));
            this.mIsStarting = true;
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(11)
    private boolean startBluetooth11() {
        Log.d(TAG, "startBluetooth11");
        return this.mBluetoothAdapter != null && this.mAudioManager.isBluetoothScoAvailableOffCall() && this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mHeadsetProfileListener, 1);
    }

    private void stopBluetooth() {
        Log.d(TAG, "stopBluetooth");
        if(this.mIsCountDownOn) {
            this.mIsCountDownOn = false;
            this.mCountDown.cancel();
        }

        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mAudioManager.stopBluetoothSco();
        this.mAudioManager.setMode(0);
        this.mAudioManager.setBluetoothScoOn(false);
    }

    @TargetApi(11)
    protected void stopBluetooth11() {
        Log.d(TAG, "stopBluetooth11");
        if(this.mIsCountDownOn) {
            this.mIsCountDownOn = false;
            this.mCountDown11.cancel();
        }

        if(this.mHeadsetBroadcastReceiver != null) {
            this.mContext.unregisterReceiver(this.mHeadsetBroadcastReceiver);
            this.mHeadsetBroadcastReceiver = null;
        }

        if(this.mBluetoothHeadset != null) {
            if(Build.VERSION.SDK_INT < 18) {
                this.mAudioManager.stopBluetoothSco();
                this.mAudioManager.setBluetoothScoOn(false);
            } else {
                try {
                    Class e = this.mBluetoothHeadset.getClass();
                    Method method = e.getDeclaredMethod("stopScoUsingVirtualVoiceCall", new Class[]{BluetoothDevice.class});
                    method.invoke(this.mBluetoothHeadset, new Object[]{this.mConnectedHeadset});
                } catch (Exception var3) {
                    var3.printStackTrace();
                }
            }

            this.mBluetoothAdapter.closeProfileProxy(1, this.mBluetoothHeadset);
            this.mBluetoothHeadset = null;
        }

    }
}

