package com.afmobi.palmchat.eventbusmodel;

/**
 * coins的event模型类
 * 
 * @author zhh 2015-11-30
 * 
 */
public class UpdateCoinsEvent {
	private int mBalance; // 当前最新余额
	private boolean isUpdateFromServer = false;// add by zhh 是否从服务器重新获取

	public UpdateCoinsEvent(int mBalance, boolean isUpdateFromServer) {
		this.mBalance = mBalance;
		this.isUpdateFromServer = isUpdateFromServer;
	}

	public long getBalance() {
		return mBalance;
	}

	public void setBalance(int mBalance) {
		this.mBalance = mBalance;
	}

	public boolean isUpdateFromServer() {
		return isUpdateFromServer;
	}

	public void setUpdateFromServer(boolean isUpdateFromServer) {
		this.isUpdateFromServer = isUpdateFromServer;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("UpadteRechargeResultEvent{");
		sb.append("mBalance=").append(mBalance);
		sb.append('}');
		return sb.toString();
	}
}
