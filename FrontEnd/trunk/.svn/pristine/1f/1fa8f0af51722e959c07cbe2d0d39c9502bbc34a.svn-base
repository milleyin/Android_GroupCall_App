package com.afmobi.palmchat.ui.activity.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * @author Stone
 *
 */
public class Contacts implements  Parcelable,Serializable,Comparable<Contacts> {
	
	private int id;
	public String name;
	public String number;
	public String sortKey;
	public List<String> numbers;
	SearchFilter filter = SearchFactory.getSearchFilter(SearchFactory.DEFAULT_CODE);
	
	public int getId() {
		
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		if(null==name){
			return "";
		}
		return name;
	}
	public void setName(String name) {
		this.sortKey = filter.getFullSpell(name);
		this.name = name;
	}
	public String getNumber() {
		if(null==number){
			return "";
		}
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public List<String> getNumbers() {
		if(null==numbers){
			return null;
		}
		return numbers;
	}
	public void setNumbers(String number) {
		if(numbers == null){
			numbers = new ArrayList<>();
		}
		numbers.add(number);
	}
	public String getSortKey() {
		if(null==sortKey){
			return "";
		}
		return sortKey;
	}
//	public void setSortKey(String sortKey) {
//		this.sortKey = sortKey;
//	}
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public int compareTo(Contacts another) {
		if (getSortKey().length() == 0 || another.getSortKey().length() == 0) {
			return getSortKey().compareTo(another.getSortKey());
		}
		return getSortKey().substring(0, 1).toUpperCase().compareTo(another.getSortKey().substring(0, 1).toUpperCase());
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(number);
		dest.writeString(sortKey);
	}
	
	@Override
	public String toString() {
		return "Contacts [id=" + id + ", name=" + name + ", number=" + number
				+ ", sortKey=" + sortKey + "]";
	}

	public static final Parcelable.Creator<Contacts> CREATOR = new Parcelable.Creator<Contacts>() {  
		@Override  
		public Contacts createFromParcel(Parcel source) {  
			Contacts c = new Contacts();  
			c.id = source.readInt();  
			c.name = source.readString();  
			c.number = source.readString();
			c.sortKey = source.readString();
			return c;  
		}

		@Override
		public Contacts[] newArray(int size) {
			return new Contacts[size];
		}
	};
}
