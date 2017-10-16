package br.ufpe.cin.if710.podcast.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionsManager {
    public static boolean requestPermissions(Context context) {
        if(ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            return false;
        } else {
            return true;
        }

    }
}
