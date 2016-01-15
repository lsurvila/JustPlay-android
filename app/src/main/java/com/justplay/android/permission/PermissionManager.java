package com.justplay.android.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionManager {

    private static final String PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private PermissionCallback callback;
    private Fragment fragment;
    private AppCompatActivity activity;

    public void bindFragmentOrActivity(Object fragmentOrActivity) {
        if (fragmentOrActivity instanceof Fragment) {
            fragment = (Fragment) fragmentOrActivity;
        } else if (fragmentOrActivity instanceof AppCompatActivity) {
            activity = (AppCompatActivity) fragmentOrActivity;
        }
    }

    public void setCallback(PermissionCallback callback) {
        this.callback = callback;
    }

    /**
     * pass result from onRequestPermissionsResult to handleResponse.
     */
    public void requestPermissionIfNeeded(int requestCode) {
        int permissionCheck = ContextCompat.checkSelfPermission(fragment.getActivity(), PERMISSION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionGranted(requestCode);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.getActivity(), PERMISSION)) {
                callback.onPermissionDenied();
            } else {
                requestPermission(requestCode);
            }
        }
    }

    private void requestPermission(int requestCode) {
        if (fragment != null) {
            fragment.requestPermissions(new String[]{PERMISSION}, requestCode);
        } else if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{PERMISSION}, requestCode);
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
