package com.core.listener;

public interface AfHttpProgressListener {
	public void  AfOnProgress(int httpHandle, int flag, int progress, Object user_data);
}
