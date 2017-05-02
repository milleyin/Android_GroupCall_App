package com.afmobi.palmchat.util.Bluetooth;

/**
 * Created by starw on 2016/9/12.
 */
import android.content.Context;
import android.media.AudioManager;
import com.justalk.cloud.juscall.MtcBluetooth;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BluetoothHelper extends Bluetooth {
    protected Context mContext;
    public ArrayList<String> mNameList = new ArrayList();
    public ArrayList<String> mAddressList = new ArrayList();
    private boolean mSpeakerOn = false;
    private WeakReference<BluetoothHelper.Callback> mCallback;

    public BluetoothHelper(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setCallback(BluetoothHelper.Callback callback) {
        this.mCallback = callback == null?null:new WeakReference(callback);
    }

    public BluetoothHelper.Callback getCallback() {
        return this.mCallback == null?null:(BluetoothHelper.Callback)this.mCallback.get();
    }

    public boolean unlink(boolean speakerOn) {
        boolean ret = super.unlink();
        AudioManager audioManager = (AudioManager)this.mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(speakerOn);
        this.mSpeakerOn = speakerOn;
        return ret;
    }

    public void onScoAudioDisconnected() {
        AudioManager audioManager = (AudioManager)this.mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(this.mSpeakerOn);
        BluetoothHelper.Callback callback = this.getCallback();
        if(callback != null) {
            callback.mtcBluetoothChanged(false);
        }
    }

    public void onScoAudioConnected() {
        this.mSpeakerOn = false;
    }

    public void onHeadsetDisconnected(String address) {
        int index = this.mAddressList.indexOf(address);
        this.mAddressList.remove(index);
        this.mNameList.remove(index);
        BluetoothHelper.Callback callback = this.getCallback();
        if(callback != null) {
            callback.mtcBluetoothChanged(false);
        }
    }

    public void onHeadsetConnected(String address, String name) {
        this.mAddressList.add(address);
        this.mNameList.add(name);
        BluetoothHelper.Callback callback = this.getCallback();
        if(callback != null) {
            callback.mtcBluetoothChanged(true);
        }
    }

    public int getCount() {
        return this.mNameList.size();
    }

    public interface Callback {
        void mtcBluetoothChanged(boolean connected);
    }
}
