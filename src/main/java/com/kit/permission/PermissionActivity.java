/*
 * Copyright (c) 2018.
 * Author：Zhao
 * Email：joeyzhao1005@gmail.com
 */

package com.kit.permission;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * @author joeyzhao
 */
public class PermissionActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 64;
    private boolean isRequireCheck;

    private String[] permission;
    private String key;
    private boolean showTip;
    private TipInfo tipInfo;

    private final String defaultTitle = "帮助";
    private final String defaultContent = "当前应用缺少必要权限。\n \n 请点击 \"设置\"-\"权限\"-打开所需权限。";
    private final String defaultCancel = "取消";
    private final String defaultEnsure = "设置";
    View empty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra("permission")) {
            finish();
            return;
        }

        isRequireCheck = true;
        Intent intent = getIntent();
        Bundle extras = null;
        if (intent != null) {
            extras = getIntent().getExtras();
        }

        if (extras != null) {
            if (extras.containsKey("permission")) {
                permission = extras.getStringArray("permission");
            }

            if (extras.containsKey("key")) {
                key = extras.getString("key");
            }

            if (extras.containsKey("showTip")) {
                showTip = extras.getBoolean("showTip", true);
            }

            TipInfo ser = null;
            if (extras.containsKey("tipInfo")) {
                ser = extras.getParcelable("tipInfo");
            }
            if (ser == null) {
                tipInfo = new TipInfo(defaultTitle, defaultContent, defaultCancel, defaultEnsure);
            } else {
                tipInfo = ser;
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            if (PermissionManager.hasPermission(this, permission)) {
                permissionsGranted();
            } else {
                // 请求权限,回调时会触发onResume
                requestPermissions(permission);
                isRequireCheck = false;
            }
        } else {
            isRequireCheck = true;
        }
    }

    // 请求权限兼容低版本
    private void requestPermissions(String[] permission) {
        try {
            ActivityCompat.requestPermissions(this, permission, PERMISSION_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //部分厂商手机系统返回授权成功时，厂商可以拒绝权限，所以要用PermissionChecker二次判断
        if (requestCode == PERMISSION_REQUEST_CODE && PermissionManager.isGranted(grantResults)
                && PermissionManager.hasPermission(this, permissions)) {
            permissionsGranted();
        }
//        else if (showTip) { //被拒绝 显示弹窗
//            showMissingPermissionDialog();
//        }
        else { //被拒绝
            permissionsDenied();
        }
    }


    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
//        Snackbar.make(findViewById(R.id.empty), TextUtils.isEmpty(tipInfo.content) ? defaultContent : tipInfo.content, Integer.MAX_VALUE)
//                .setAction(TextUtils.isEmpty(tipInfo.ensure) ? defaultEnsure : tipInfo.ensure, (v) -> {
//                    PermissionManager.gotoSetting(PermissionActivity.this);
//                }).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this);
//
//        builder.setTitle(TextUtils.isEmpty(tipInfo.title) ? defaultTitle : tipInfo.title);
//        builder.setMessage(TextUtils.isEmpty(tipInfo.content) ? defaultContent : tipInfo.content);
//
//        builder.setNegativeButton(TextUtils.isEmpty(tipInfo.cancel) ? defaultCancel : tipInfo.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                permissionsDenied();
//            }
//        });
//
//        builder.setPositiveButton(TextUtils.isEmpty(tipInfo.ensure) ? defaultEnsure : tipInfo.ensure, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PermissionManager.gotoSetting(PermissionActivity.this);
//            }
//        });
//
//        builder.setCancelable(false);
//        builder.show();
    }

    private void permissionsDenied() {
        PermissionManager.PermissionListener listener = PermissionManager.fetchListener(key);
        if (listener != null) {
            listener.onPermission(permission, false);
        }
        finish();
    }

    /**
     * 全部权限均已获取
     */
    private void permissionsGranted() {
        PermissionManager.PermissionListener listener = PermissionManager.fetchListener(key);
        if (listener != null) {
            listener.onPermission(permission, true);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        PermissionManager.fetchListener(key);
        super.onDestroy();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

    }
}