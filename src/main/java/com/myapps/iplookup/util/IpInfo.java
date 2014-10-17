package com.myapps.iplookup.util;

import java.io.Serializable;

public class IpInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String city;

	private String country;

	private String region;

	private Long ipcount;

	private String errorMsg;

	public IpInfo() {
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setIpcount(Long ipcount) {
		this.ipcount = ipcount;
	}

	public Long getIpcount() {
		return ipcount;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

	public IpInfo(Long ipcount) {
		this.ipcount = ipcount;

	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public String toString() {
		return "IpLookup [city=" + city + ", country=" + country + ", region="
				+ region + ", ipcount=" + ipcount + ", errorMsg=" + errorMsg
				+ "]";
	}
}