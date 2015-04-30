/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzy.supercleanmaster.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.yzy.supercleanmaster.model.AppInfo;
import com.yzy.supercleanmaster.model.SDCardInfo;
import com.yzy.supercleanmaster.model.StorageSize;

// TODO: Auto-generated Javadoc

public class StorageUtil {

	/***
	 * 、 计算 内存 可用的 百分比 包含怎样转换
	 * 
	 * @param size
	 * @return
	 */
	public static String convertStorage(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		} else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else
			return String.format("%d B", size);
	}

	public static StorageSize convertStorageSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		StorageSize sto = new StorageSize();
		if (size >= gb) {

			sto.suffix = "GB";
			sto.value = (float) size / gb;
			return sto;
		} else if (size >= mb) {

			sto.suffix = "MB";
			sto.value = (float) size / mb;

			return sto;
		} else if (size >= kb) {

			sto.suffix = "KB";
			sto.value = (float) size / kb;

			return sto;
		} else {
			sto.suffix = "B";
			sto.value = (float) size;

			return sto;
		}

	}

	/***
	 * 获取内存卡信息
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public static SDCardInfo getSDCardInfo() {
		// String sDcString = Environment.getExternalStorageState();

		if (Environment.isExternalStorageRemovable()) {
			String sDcString = Environment.getExternalStorageState();
			if (sDcString.equals(Environment.MEDIA_MOUNTED)) {
				File pathFile = Environment.getExternalStorageDirectory();

				try {
					StatFs statfs = new StatFs(pathFile.getPath());

					// 获取SDCard上BLOCK总数
					long nTotalBlocks = statfs.getBlockCount();

					// 获取SDCard上每个block的SIZE
					long nBlocSize = statfs.getBlockSize();

					// 获取可供程序使用的Block的数量
					long nAvailaBlock = statfs.getAvailableBlocks();

					// 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
					long nFreeBlock = statfs.getFreeBlocks();

					SDCardInfo info = new SDCardInfo();
					// 计算SDCard 总容量大小MB
					info.total = nTotalBlocks * nBlocSize;

					// 计算 SDCard 剩余大小MB
					info.free = nAvailaBlock * nBlocSize;

					return info;
				} catch (IllegalArgumentException e) {

				}
			}
		}
		return null;
	}

	/** 获取系统的存储大小 */
	@SuppressLint("NewApi")
	public static SDCardInfo getSystemSpaceInfo(Context context) {
		File path = Environment.getDataDirectory();
		// File path = context.getCacheDir().getAbsoluteFile();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		// long blockSize = stat.getBlockSizeLong();

		// long totalBlocks = stat.getBlockCount();
		long totalBlocks = stat.getBlockCount();

		long availableBlocks = stat.getAvailableBlocks();
		// long availableBlocks = stat.getAvailableBlocksLong();

		long totalSize = blockSize * totalBlocks;
		long availSize = availableBlocks * blockSize;
		SDCardInfo info = new SDCardInfo();
		info.total = totalSize;
		info.free = availSize;
		return info;

	}

	public static SDCardInfo getRootSpaceInfo() {
		File path = Environment.getRootDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		long availableBlocks = stat.getAvailableBlocks();

		long totalSize = blockSize * totalBlocks;
		long availSize = availableBlocks * blockSize;
		// 获取SDCard上每个block的SIZE
		long nBlocSize = stat.getBlockSize();

		SDCardInfo info = new SDCardInfo();
		// 计算SDCard 总容量大小MB
		info.total = totalSize;

		// 计算 SDCard 剩余大小MB
		info.free = availSize;
		return info;

	}

	/**
	 * get the installed app size
	 * @param mPackageManager
	 * @return
	 */
	public static ArrayList<AppInfo> getInstalledApps(
			PackageManager mPackageManager) {

		ArrayList<AppInfo> res = new ArrayList<AppInfo>();
		List<ApplicationInfo> installedAppList = mPackageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		if (installedAppList == null) {
			return null;
		}
		List<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo appInfo : installedAppList) {
			boolean flag = false;
			if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
				flag = true;
			} else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				flag = true;
			}
			if (flag) {
				appList.add(appInfo);
			}
		}
		for (int i = 0; i < appList.size(); i++) {
			ApplicationInfo applicationInfo = appList.get(i);

			String dir = applicationInfo.publicSourceDir;

			int size = Integer.valueOf((int) new File(dir).length());
			
			String packageSize = convertStorage(size);
//			LogUtils.d("applicationInfo.Name -- >  == "+(applicationInfo.loadLabel(mPackageManager).toString()));
//			LogUtils.d("packageSize -- >  == "+(packageSize));
			
			String date = new Date(new File(dir).lastModified()).toGMTString();
			AppInfo newInfo = new AppInfo();
			newInfo.setAppName(applicationInfo.loadLabel(mPackageManager).toString());
			newInfo.setPackname(applicationInfo.packageName);
			newInfo.setPackageSize(packageSize);
			// newInfo.size = round(size);
			try {
				newInfo.setVersionName(mPackageManager.getPackageInfo(
						newInfo.getPackname(), 0).versionName);
				newInfo.setVersionCode(String.valueOf(mPackageManager
						.getPackageInfo(newInfo.getPackname(), 0).versionCode));
			} catch (Exception e) {
				LogUtils.e("PackageUtil", e.getMessage());
			}
			newInfo.setAppIcon(applicationInfo.loadIcon(mPackageManager));
			res.add(newInfo);
		}
		return res;
	}

	/**
	 * get the app Capacity(size)
	 * @param applicationInfo
	 * @return
	 */
	public static String getInstallAppCapacity(ApplicationInfo applicationInfo){
		
		if(applicationInfo != null){

			String dir = applicationInfo.publicSourceDir;
			int size = Integer.valueOf((int) new File(dir).length());
			String packageSize = convertStorage(size);
			return packageSize;
		}
		return null;
	}
	
	/**
	 * get the app Capacity(size)
	 * @param applicationInfo
	 * @return
	 */
	public static int getInstallAppSize(ApplicationInfo applicationInfo){
		
		if(applicationInfo != null){

			String dir = applicationInfo.publicSourceDir;
			int size = Integer.valueOf((int) new File(dir).length());
			String packageSize = convertStorage(size);
			return size;
		}
		return 0;
	}
	
}
