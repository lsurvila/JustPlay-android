package com.justplay.android.helper;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class PermissionManager {

    private final AndroidPermissionChecker permissionChecker;

    private PermissionCallback callback;

    @Inject
    public PermissionManager(AndroidPermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    public void setCallback(PermissionCallback permissionCallback) {
        callback = permissionCallback;
        permissionChecker.setFragmentOrActivity(callback);
    }

    public void requestPermissionIfNeeded(int requestCode) {
        int permissionCheck = permissionChecker.checkSelfPermission();
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted(requestCode);
        } else {
            if (permissionChecker.shouldShowPermissionRationale()) {
                callback.onPermissionDenied();
            } else {
                permissionChecker.requestPermission(requestCode);
            }
        }
    }

    public void handleResponse(int requestCode, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted(requestCode);
        } else {
            callback.onPermissionDenied();
        }
    }

    public interface PermissionCallback {
        void onPermissionGranted(int requestCode);
        void onPermissionDenied();
    }

}
