package com.justplay.android.helper;

import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionManagerTest {

    private PermissionManager permissionManager;

    @Mock
    AndroidPermissionChecker permissionChecker;

    @Mock
    PermissionManager.PermissionCallback callback;

    @Before
    public void setUp() throws Exception {
        permissionManager = new PermissionManager(permissionChecker);
        permissionManager.setCallback(callback);
    }

    @Test
    public void shouldPermissionGrantedBeCalled() throws Exception {
        int requestCode = 10;
        when(permissionChecker.checkSelfPermission()).thenReturn(PackageManager.PERMISSION_GRANTED);

        permissionManager.requestPermissionIfNeeded(requestCode);

        verify(callback).onPermissionGranted(10);
    }

    @Test
    public void shouldPermissionDeniedBeCalledIfRationale() throws Exception {
        int requestCode = 11;
        when(permissionChecker.checkSelfPermission()).thenReturn(PackageManager.PERMISSION_DENIED);
        when(permissionChecker.shouldShowPermissionRationale()).thenReturn(true);

        permissionManager.requestPermissionIfNeeded(requestCode);

        verify(callback).onPermissionDenied();
    }

    @Test
    public void shouldPermissionIsRequestedIfNotRationale() throws Exception {
        int requestCode = 12;
        when(permissionChecker.checkSelfPermission()).thenReturn(PackageManager.PERMISSION_DENIED);
        when(permissionChecker.shouldShowPermissionRationale()).thenReturn(false);

        permissionManager.requestPermissionIfNeeded(requestCode);

        verify(permissionChecker).requestPermission(12);
    }

    @Test
    public void shouldPermissionGrantBeCalledAfterPositiveResponse() throws Exception {
        int requestCode = 13;
        int[] grantResults = new int[] { PackageManager.PERMISSION_GRANTED };

        permissionManager.handleResponse(requestCode, grantResults);

        verify(callback).onPermissionGranted(13);
    }

    @Test
    public void shouldPermissionDeniedBeCalledAfterNegativeResponse() throws Exception {
        int requestCode = 14;
        int[] grantResults = new int[] { PackageManager.PERMISSION_DENIED };

        permissionManager.handleResponse(requestCode, grantResults);

        verify(callback).onPermissionDenied();
    }

    @Test
    public void shouldPermissionDeniedBeCalledAfterEmptyResponse() throws Exception {
        int requestCode = 14;
        int[] grantResults = new int[] { };

        permissionManager.handleResponse(requestCode, grantResults);

        verify(callback).onPermissionDenied();
    }

}