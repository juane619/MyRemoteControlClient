package com.juane.remotecontrol.utils;

import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;

public class Utils {
    public static boolean validateIP(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

    public static String getVersion(Fragment f){
        try {
            return f.getActivity().getPackageManager().getPackageInfo(f.getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        };

        return null;
    }
}
