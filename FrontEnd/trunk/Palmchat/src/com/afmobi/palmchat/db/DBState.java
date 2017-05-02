package com.afmobi.palmchat.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.afmobi.palmchat.PalmchatApp;
import com.core.cache.CacheManager.Country;
import com.core.cache.CacheManager.Region;
import com.afmobi.palmchat.util.FileUtils;
/**
 * 从数据库读取改为从json文件读取省市信息
 * @author wxl
 *
 */
public class DBState  
{

	private static DBState instance;   
	public static DBState getInstance(Context context) {
		if (instance == null) { 
			instance = new DBState(context,null);
		}
		return instance;
	}
	public DBState(Context context ,String dbName) {
 	} 
	
	/**
	 * 从statescity.json文件获取省市信息
	 * @param strCountry
	 * @return
	 */
	public static Region readAssertStatesList(String strCountry  ) { 
        AssetManager assetManager = PalmchatApp.getApplication().getAssets();
        Region region=null;
        String strResponse = "";
        try {
            InputStream ims = assetManager.open("statescity.json");
            strResponse = FileUtils.getStringFromInputStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        JSONObject _json; 
		try {
			_json = new JSONObject(strResponse);
			
			JSONObject _jsonCountry= _json.getJSONObject(strCountry );
			if(_jsonCountry!=null){
				region=new Region();
				region.setName(strCountry);
				region.setCountryCode(_jsonCountry.getString("cc"));
				JSONArray _jsonArrStates=_jsonCountry.getJSONArray("states");
				String [] _states=new String[_jsonArrStates.length()];
				
				JSONArray _jsonArrCitys=null;
				JSONObject _jobj=null;
				String[] _citys=null;
				List<String[]> _listCitys=new ArrayList<String[]>();
				for(int i=0;i<_jsonArrStates.length();i++){
					_jobj=(JSONObject) _jsonArrStates.get(i) ;
					_states[i]= _jobj.getString( "name") ;
					_jsonArrCitys=_jobj.getJSONArray("citys");
					_citys=new String[_jsonArrCitys.length()];
					for(int j=0;j<_citys.length;j++){
						_citys[j]=_jsonArrCitys.getString(j);
					}
					_listCitys.add(_citys);
				}
				region.setRegions(_states);
				region.setCities(_listCitys);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 
		}
		
        return region;
    }
	private static JSONArray allCountryCCMccJson;
	
	public   String[] getCountry_cc_FromMcc(String mcc){
		if(allCountryCCMccJson==null){
			readAssertCountryList( );
		}
		String [] strResult=new String[]{"",""};
		try{
			if(allCountryCCMccJson!=null){
				int count =allCountryCCMccJson.length();
				JSONObject _js=null;
				for(int i=0;i<count;i++){
					_js=  allCountryCCMccJson.getJSONObject( i);
					if(_js.getString("mcc").equals(mcc)){
						strResult[0]= _js.getString("cc");
						strResult[1]= _js.getString("country");
						return strResult;
					} 
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strResult;
	}
	
	public   String getCountryFromCode(String cc){
		if(allCountryCCMccJson==null){
			readAssertCountryList( );
		}
		 
		try{
			if(allCountryCCMccJson!=null){
				int count =allCountryCCMccJson.length();
				JSONObject _js=null;
				for(int i=0;i<count;i++){
					_js=  allCountryCCMccJson.getJSONObject( i);
					if(_js.getString("cc").equals(cc)){
					 
						return _js.getString("country");
					}
				 
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public String [] getCountryAndDefaultRegionFromMcc(String mcc){
		if(allCountryCCMccJson==null){
			readAssertCountryList( );
		}
		String [] strResult=new String[]{"","",""};
		try{
			if(allCountryCCMccJson!=null){
				int count =allCountryCCMccJson.length();
				JSONObject _js=null;
				for(int i=0;i<count;i++){
					_js=  allCountryCCMccJson.getJSONObject( i);
					if(_js.getString("mcc").equals(mcc)){ 
						strResult[0]= _js.getString("country");
						return strResult;
					}
					
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!TextUtils.isEmpty( strResult[0])){
			Region _r=readAssertStatesList(mcc);
			if(_r!=null){
				String [] states=_r.getRegions();
				if(states!=null&&states.length>0 ){
					strResult[1]=states[0];
					List<String[]> _tt= _r.getCities() ;
					if(!_tt.isEmpty()){
						String []_citys=_tt.get(0);
						strResult[2]=_citys[0];
					}
				}
			}
		}
		return strResult;
	}
	/**
	 * 从country_cc_mcc.json 获取国家列表
	 * @return
	 */
	public static JSONArray readAssertCountryList( ) { 
		if(allCountryCCMccJson==null){
	        AssetManager assetManager = PalmchatApp.getApplication().getAssets();
	        String strResponse = "";
	        try {
	            InputStream ims = assetManager.open("country_cc_mcc.json");
	            strResponse = FileUtils.getStringFromInputStream(ims);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        JSONObject _json;
	    	  
			try {
				_json = new JSONObject(strResponse);
				_json=_json.getJSONObject( "contents");
				allCountryCCMccJson=_json.getJSONArray("list");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        return allCountryCCMccJson;
    }
	
	public ArrayList<Country>  getCountryList_toArrayList(){
		ArrayList<Country> countrys = new ArrayList<Country>();
		if(allCountryCCMccJson==null){
			readAssertCountryList( );
		}
		if(allCountryCCMccJson!=null){
			int count =allCountryCCMccJson.length();
			JSONObject _js=null;
			Country _country=null;
			try{
				for(int i=0;i<count;i++){
					_js=  allCountryCCMccJson.getJSONObject( i); 
					_country=new Country();
					_country.setCountry(_js.getString("country"));
					_country.setCode(_js.getString("cc"));
					_country.setMCC(_js.getString("mcc"));
					countrys.add(_country);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return countrys;
	}

    
	/**
	 * add zhh 根据国家获取国码
	 * @param country
	 * @return
	 */
	public String getCcFromCountry(String country){
		if(allCountryCCMccJson==null){
			readAssertCountryList( );
		}
		String cc = "";
		try{
			if(allCountryCCMccJson!=null){
				int count =allCountryCCMccJson.length();
				JSONObject _js=null;
				for(int i=0;i<count;i++){
					_js=  allCountryCCMccJson.getJSONObject( i);
					if(_js.getString("country").equals(country)){
						cc = _js.getString("cc");
					} 
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cc;
	}
}
