package com.justplay.android.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionManager {

    private static final int PERMISSION_REQUEST = 1;
    private static final String PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private Callback callback;
    private Fragment fragment;
    private AppCompatActivity activity;

    public void bindFragmentOrActivity(Object fragmentOrActivity) {
        if (fragmentOrActivity instanceof Fragment) {
            fragment = (Fragment) fragmentOrActivity;
        } else if (fragmentOrActivity instanceof AppCompatActivity) {
            activity = (AppCompatActivity) fragmentOrActivity;
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * pass result from onRequestPermissionsResult to handleResponse.
     */
    public void requestPermissionIfNeeded() {
        int permissionCheck = ContextCompat.checkSelfPermission(fragment.getActivity(), PERMISSION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), PERMISSION)) {
                callback.onPermissionDenied();
            } else {
                requestPermission();
            }
        }
    }

    private void requestPermission() {
        if (fragment != null) {
            fragment.requestPermissions(new String[]{PERMISSION}, PERMISSION_REQUEST);
        } else if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{PERMISSION}, PERMISSION_REQUEST);
        }
    }

    public void handleResponse(int requestCode, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callback.onPermissionGranted();
                } else {
                    callback.onPermissionDenied();
                }
        }
    }

    public interface Callback {
        void onPermissionGranted();
        void onPermissionDenied();
    }

}
