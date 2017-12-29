package com.lenabru.googlelocation.managers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class PermissionsManager {

    private static final int PERMISSIONS_REQUEST = 1000;

    public interface OnPermissionRationaleUpdate {
        void onPermissionRationaleContinue(Activity context, String permission);
    }

    public interface OnPermissionStatusUpdate {
        void onPermissionGranted(String permission);

        void onShowPermissionRationale(String permission, OnPermissionRationaleUpdate update);

        void onPermissionDenied(String permission);
    }

    private OnPermissionStatusUpdate permissionStatusUpdate;
    private SharedPreferences sharedPreferences;
    private boolean showedPermissionRationale = false;

    private void setIsShowedPermissionRationale(String permission, boolean showed) {
        sharedPreferences.edit().putBoolean(permissionPrefKey(permission), showed).apply();
        showedPermissionRationale = showed;
    }

    private OnPermissionRationaleUpdate onPermissionRationaleUpdate = new OnPermissionRationaleUpdate() {
        @Override
        public void onPermissionRationaleContinue(Activity context, String permission) {
            makePermissionRequest(context, permission);
        }
    };

    public void requestPermission(Activity context, String permission, OnPermissionStatusUpdate listener) {
        sharedPreferences = context.getSharedPreferences(permissionPrefKey(permission), Context.MODE_PRIVATE);
        showedPermissionRationale = sharedPreferences.getBoolean(permissionPrefKey(permission), false);
        if (ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                if (listener != null) {
                    listener.onShowPermissionRationale(permission, onPermissionRationaleUpdate);
                    setIsShowedPermissionRationale(permission, true);
                }
            } else {
                permissionStatusUpdate = listener;
                if (showedPermissionRationale) {
                    listener.onPermissionDenied(permission);
                    return;
                }
                makePermissionRequest(context, permission);
            }
        } else {
            listener.onPermissionGranted(permission);
        }
    }

    @NonNull
    private String permissionPrefKey(String permission) {
        return permission + "request";
    }

    private void makePermissionRequest(Activity context, String permission) {
        ActivityCompat.requestPermissions(context, new String[]{permission}, PERMISSIONS_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                int permissionStatus = grantResults[i];
                if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                    if (permissionStatusUpdate != null) {
                        permissionStatusUpdate.onPermissionGranted(permissions[i]);
                        setIsShowedPermissionRationale(permissions[i], false);
                    }
                } else {
                    if (permissionStatusUpdate != null) {
                        permissionStatusUpdate.onPermissionDenied(permissions[i]);
                    }
                }
            }
        }
    }

    public void showPermissionSettings(Activity context) {
        String defaultSettingsActivity = android.provider.Settings.ACTION_SETTINGS;
        Uri packageName = Uri.fromParts("package", context.getPackageName(), null);
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageName);

        boolean activityExists = intent.resolveActivityInfo(context.getPackageManager(), 0) != null;
        if (activityExists) {
            context.startActivityForResult(intent, PERMISSIONS_REQUEST);
            return;
        }
        context.startActivityForResult(new Intent(defaultSettingsActivity,packageName), PERMISSIONS_REQUEST);
    }

}
