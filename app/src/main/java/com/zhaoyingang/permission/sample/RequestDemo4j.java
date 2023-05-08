/*
 * Copyright 2023 Zhao-YinGang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.zhaoyingang.permission.sample;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.hjq.permissions.Permission;
import com.zhaoyingang.permission.XXPermissionsExt;

/**
 * Java 权限申请示例
 */
public class RequestDemo4j {
    public static void run(Activity activity) {
        XXPermissionsExt.with(activity)
            // 申请位置权限
            .permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            // 申请相机权限
            .permissions(Permission.CAMERA)
            // 申请BLE权限
            .permissions(Permission.BLUETOOTH_CONNECT)
            // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
            .onShouldShowRationale((shouldShowRationaleList, onUserResult) -> {
                // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                new AlertDialog.Builder(activity)
                    .setTitle("权限申请")
                    // 根据 rationalePermissions 进行适当的提示
                    .setMessage(
                        "应用需要以下权限：" +
                            RequestDemoKt.permissionsNames(shouldShowRationaleList) +
                            "如果拒绝授予这些权限，则应用部分功能将受限"
                    )
                    .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("去申请", (dialog, which) -> {
                        dialog.dismiss();
                        // 用户同意，通过此回调通知框架
                        onUserResult.onResult(true);
                    })
                    .setOnCancelListener(dialog -> {
                        // 用户不同意，通过此回调通知框架
                        onUserResult.onResult(false);
                    })
                    .show();
            })
            .onDoNotAskAgain((doNotAskAgainList, onUserResult) -> {
                // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                new AlertDialog.Builder(activity)
                    .setTitle("权限被永久拒绝")
                    // 根据被拒绝的权限列表进行适当的提示
                    .setMessage(
                        "以下权限被永久拒绝：" +
                            RequestDemoKt.permissionsNames(doNotAskAgainList) +
                            "请进入设置界面手动授予权限"
                    )
                    .setNegativeButton("取消", (dialog, which) -> dialog.cancel())
                    .setPositiveButton("去设置", (dialog, which) -> {
                        dialog.dismiss();
                        // 用户同意，通过此回调通知框架
                        onUserResult.onResult(true);
                    })
                    .setOnCancelListener(dialog -> {
                        // 用户不同意，通过此回调通知框架
                        onUserResult.onResult(false);
                    })
                    .show();
            })
            .onResult((allGranted, grantedList, deniedList) -> ToastHelper.toast(activity,
                "allGranted: " + allGranted +
                    "\ngrantedList: " + grantedList +
                    "\ndeniedList: " + deniedList
            ))
            .request();
    }
}
