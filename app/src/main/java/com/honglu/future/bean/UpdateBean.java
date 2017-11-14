package com.honglu.future.bean;

import java.io.Serializable;

public class UpdateBean implements Serializable {
	/**
	 * oldVersionNumber : 1
	 * changeProperties :
	 * changeDesc :
	 * popup : 0
	 * downloadUrl :
	 * andVersion :
	 * changeLog :
	 * versionNumber :
	 */

	private String oldVersionNumber;
	private String changeProperties;
	private String changeDesc;
	private String popup;
	private String downloadUrl;
	private String andVersion;
	private String changeLog;
	private String versionNumber;
	private String md5;

	// 是否静默下载：有新版本时不提示直接下载
	public boolean isSilent = false;

	public String getOldVersionNumber() {
		return oldVersionNumber;
	}

	public void setOldVersionNumber(String oldVersionNumber) {
		this.oldVersionNumber = oldVersionNumber;
	}

	public String getChangeProperties() {
		return changeProperties;
	}

	public void setChangeProperties(String changeProperties) {
		this.changeProperties = changeProperties;
	}

	public String getChangeDesc() {
		return changeDesc;
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}

	public String getPopup() {
		return popup;
	}

	public void setPopup(String popup) {
		this.popup = popup;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getAndVersion() {
		return andVersion;
	}

	public void setAndVersion(String andVersion) {
		this.andVersion = andVersion;
	}

	public String getChangeLog() {
		return changeLog;
	}

	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
