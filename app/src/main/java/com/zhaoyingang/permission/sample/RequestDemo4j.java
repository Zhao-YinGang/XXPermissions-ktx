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
import com.zhaoyingang.permission.XXPermissions4j;

/**
 * Java 权限申请示例
 */
public class RequestDemo4j {
    public static void run(Activity activity) {
        XXPermissions4j.with(activity)
            // 申请BLE权限
            .permissions(Permission.BLUETOOTH_CONNECT)
            // 申请相机权限
            .permissions(Permission.CAMERA)
            // 申请位置权限
            .permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
            // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
            .onShowRationale(rationale -> {
                // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                new AlertDialog.Builder(activity)
                    .setTitle("权限申请")
                    // 根据 rationalePermissions 进行适当的提示
                    .setMessage(
                        "应用需要以下权限：" +
                            RequestDemoKt.permissionsNames(rationale.getRationaleList()) +
                            "如果拒绝授予这些权限，则应用部分功能将受限"
                    )
                    .setNeutralButton("取消", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("去申请", (dialog, which) -> {
                        dialog.dismiss();
                        // 用户同意，通过此回调通知框架开始权限申请
                        rationale.getOnConsent().invoke();
                    })
                    .show();
            })
            // 授予了权限
            .onGranted(granted -> {
                if (granted.isAllGranted()) {  // 用户同意了所有权限
                    ToastHelper.toast(activity, "所有权限获取成功");
                } else { // 用户同意了部分权限
                    ToastHelper.toast(activity, "获取到了以下权限：" + RequestDemoKt.permissionsNames(granted.getGrantedList()));
                }
            })
            // 拒绝了权限
            .onDenied(denied -> {
                if (denied.getHasDoNotAskAgain()) {
                    // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
                    new AlertDialog.Builder(activity)
                        .setTitle("权限被永久拒绝")
                        // 根据被拒绝的权限列表进行适当的提示
                        .setMessage(
                            "以下权限被永久拒绝：" +
                                RequestDemoKt.permissionsNames(denied.getDoNotAskAgainList()) +
                                "请进入设置界面手动授予权限"
                        )
                        .setNeutralButton("取消", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("去申请", (dialog, which) -> {
                            dialog.dismiss();
                            // 用户同意，通过此回调通知框架进入应用设置界面
                            denied.getOnConsent().invoke();
                        })
                        .show();
                } else {
                    ToastHelper.toast(activity, "以下权限被拒绝：" + RequestDemoKt.permissionsNames(denied.getDeniedList()));
                    run(activity);
                }
            })
            .request();
    }
}
