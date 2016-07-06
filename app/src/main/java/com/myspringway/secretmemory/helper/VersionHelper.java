package com.myspringway.secretmemory.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class VersionHelper {
    public static String getAppVersion(Context context) {
        String version = null;
        try {
            PackageInfo i = context.getPackageManager() .getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getOSVersion()
    {
        return Build.VERSION.RELEASE;
    }
}
