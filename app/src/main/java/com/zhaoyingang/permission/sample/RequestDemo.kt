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

package com.zhaoyingang.permission.sample

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.hjq.permissions.Permission
import com.zhaoyingang.permission.xxPermissions

/**
 * Kotlin 权限申请基本用法示例
 */
fun Activity.requestBasicDemo() {
    xxPermissions {
        // 申请位置权限
        permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
        // 权限申请结果
        onResult { allGranted, grantedList, deniedList ->
            toast(
                "allGranted: " + allGranted +
                    "\ngrantedList: " + grantedList +
                    "\ndeniedList: " + deniedList
            )
        }
    }
}

/**
 * Kotlin 权限申请完整示例
 */
fun Activity.requestDemo() {
    xxPermissions {
        // 申请BLE权限
        permissions(Permission.BLUETOOTH_CONNECT)
        // 申请相机权限
        permissions(Permission.CAMERA)
        // 申请位置权限
        permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
        // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
        onShouldShowRationale { shouldShowRationaleList, onUserResult ->
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this@requestDemo)
                .setTitle("权限申请")
                // 根据 rationalePermissions 进行适当的提示
                .setMessage(
                    "应用需要以下权限：" +
                        permissionsNames(shouldShowRationaleList) +
                        "如果拒绝授予这些权限，则应用部分功能将受限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("去申请") { dialog, _ ->
                    dialog.dismiss()
                    // 用户同意，通过此回调通知框架开始权限申请
                    onUserResult.onResult(true)
                }
                .setOnCancelListener {
                    // 用户不同意，通过此回调通知框架
                    onUserResult.onResult(false)
                }
                .show()
        }

        onDoNotAskAgain { doNotAskAgainList, onUserResult ->
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this@requestDemo)
                .setTitle("权限被永久拒绝")
                // 根据被拒绝的权限列表进行适当的提示
                .setMessage(
                    "以下权限被永久拒绝：" +
                        permissionsNames(doNotAskAgainList) +
                        "请进入设置界面手动授予权限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton("去设置") { dialog, _ ->
                    dialog.dismiss()
                    // 用户同意，通过此回调通知框架进入应用设置界面
                    onUserResult.onResult(true)
                }
                .setOnCancelListener {
                    // 用户不同意，通过此回调通知框架
                    onUserResult.onResult(false)

                }
                .show()
        }
        // 权限申请结果
        onResult { allGranted, grantedList, deniedList ->
            toast(
                "allGranted: " + allGranted +
                    "\ngrantedList: " + grantedList +
                    "\ndeniedList: " + deniedList
            )
        }
    }
}

fun permissionsNames(permissions: List<String>): String {
    val msgList = mutableListOf<String>()
    if (permissions.contains(Permission.BLUETOOTH_CONNECT)) {
        msgList.add("发现并连接到附近设备")
    }
    if (permissions.contains(Permission.CAMERA)) {
        msgList.add("拍摄照片")
    }
    if (permissions.any { it in listOf(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION) }) {
        msgList.add("获取位置信息")
    }
    return msgList.joinToString("、 ")
}