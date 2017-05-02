package com.afmobi.palmchat.ui.activity.social;

import com.core.AfResponseComm.AfChapterInfo;
import com.core.AfResponseComm.AfPeopleInfo;

public class HomeAdapterData {
	public AfPeopleInfo mAfPeopleInfo;
	public AfChapterInfo mAfChapterInfo;
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (mAfPeopleInfo != null && mAfPeopleInfo.afid != null 
				&& ((HomeAdapterData)o).mAfPeopleInfo != null) {
			
			return mAfPeopleInfo.afid.equals(((HomeAdapterData)o).mAfPeopleInfo.afid);
			
		} else if (mAfChapterInfo != null && mAfChapterInfo.mid != null 
				&& ((HomeAdapterData)o).mAfChapterInfo != null) {
			
			return mAfChapterInfo.mid.equals(((HomeAdapterData)o).mAfChapterInfo.mid);
		} else {
			return super.equals(o);
		}
	}
}
