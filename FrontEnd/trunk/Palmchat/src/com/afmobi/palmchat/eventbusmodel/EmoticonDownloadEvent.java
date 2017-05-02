package com.afmobi.palmchat.eventbusmodel;

public class EmoticonDownloadEvent{

	/**每一项的id号*/
	private String item_id;
	/**进度*/
	private int progress;
	/**是否下载完*/
    private boolean isDownloaded;
    /**表情是否改变*/
    private boolean is_face_change;
    /**strType*/
    private String strType;
    /**sort*/
    private int sort;

	public EmoticonDownloadEvent(String item_id, int progress, boolean isDownloaded, boolean is_face_change, String strType, int sort) {
		super();
		this.item_id = item_id;
		this.progress = progress;
		this.isDownloaded = isDownloaded;
		this.is_face_change = is_face_change;
		this.strType = strType;
		this.sort = sort;
	}

	public String getStrType() {
		return strType;
	}
	
	public void setStrType(String strType) {
		this.strType = strType;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public boolean isDownloaded() {
		return isDownloaded;
	}
	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}
	public boolean isIs_face_change() {
		return is_face_change;
	}
	public void setIs_face_change(boolean is_face_change) {
		this.is_face_change = is_face_change;
	}
	
	@Override
	public String toString() {
		return "EmoticonDownloadEvent [item_id=" + item_id + ", progress=" + progress + ", isDownloaded=" + isDownloaded + ", is_face_change=" + is_face_change + "]";
	}
}