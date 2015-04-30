package com.yzy.supercleanmaster.model;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable appIcon;
	private String appName;
	private String packname;
	private String version;
    private long pkgSize;
	private int uid;
	private String versionName;
	private String versionCode;
	private String packageSize;
    /**
	 * 应用程序可以被安装到不同的位置 , 手机内存 外部存储sd卡
	 */


	private boolean inRom;
	
	private boolean userApp;
	
	
	public String getPackageSize() {
		return packageSize;
	}

	public void setPackageSize(String packageSize) {
		this.packageSize = packageSize;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

    public long getPkgSize() {
        return pkgSize;
    }

    public void setPkgSize(long pkgSize) {
        this.pkgSize = pkgSize;
    }



	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isInRom() {
		return inRom;
	}

	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	
	
	
}
