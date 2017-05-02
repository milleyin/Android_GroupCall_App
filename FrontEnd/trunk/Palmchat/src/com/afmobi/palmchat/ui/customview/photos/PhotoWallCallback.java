package com.afmobi.palmchat.ui.customview.photos;

public interface PhotoWallCallback {
	
	void photoWallPhotoTaped(int index);
	void photoWallMovePhotoFromIndex(int index,int toIndex);
	void photoWallAddAction();
	void photoWallDeleteAction();
	void photoWallDeleteFinish();
	void deleteAction();

}
