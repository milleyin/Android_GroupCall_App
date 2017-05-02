package com.afmobi.palmcall.innerApi.response;

public class CheckPCTokenResponse {
	private String code;
	private String country;
	private String region;
	private String city;
	private String afid;
	private String ua;
	private String ver;
	private String mver;
	private String osver;
	private String cc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAfid() {
		return afid;
	}

	public void setAfid(String afid) {
		this.afid = afid;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getMver() {
		return mver;
	}

	public void setMver(String mver) {
		this.mver = mver;
	}

	public String getOsver() {
		return osver;
	}

	public void setOsver(String osver) {
		this.osver = osver;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	@Override
	public String toString() {
		return "CheckPCTokenResponse{" +
				"code='" + code + '\'' +
				", country='" + country + '\'' +
				", region='" + region + '\'' +
				", city='" + city + '\'' +
				", afid='" + afid + '\'' +
				", ua='" + ua + '\'' +
				", ver='" + ver + '\'' +
				", mver='" + mver + '\'' +
				", osver='" + osver + '\'' +
				", cc='" + cc + '\'' +
				'}';
	}

}
