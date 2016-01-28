package com.justplay.android.helper;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

public class AndroidPermissionChecker {

    private static final String PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private Fragment fragment;
    private FragmentActivity activity;

    @Inject
    public void setFragmentOrActivity(Object fragmentOrActivity) {
        if (fragmentOrActivity instanceof Fragment) {
            fragment = (Fragment) fragmentOrActivity;
            activity = fragment.getActivity();
        } else if (fragmentOrActivity instanceof AppCompatActivity) {
            activity = (AppCompatActivity) fragmentOrActivity;
        }
    }

    public int checkSelfPermission() {
        return ContextCompat.checkSelfPermission(activity, PERMISSION);
    }

    public boolean shouldShowPermissionRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION);
    }


    public void requestPermission(int requestCode) {
        if (fragment != null) {
            fragment.requestPermissions(new String[]{PERMISSION}, requestCode);
        } else if (activity != null) {
            ActivityCompat.requestPermissions(activity, new String[]{PERMISSION}, requestCode);
        }
    }

}
