package com.zhaoyingang.permission.sample

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.hjq.permissions.OnPermissionPageCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.zhaoyingang.permission.XXPermissionsKTX

fun Activity.requestBleCameraLocationPermissions() {
    XXPermissionsKTX.with(this)
        // 申请BLE权限
        .permissions(Permission.BLUETOOTH_CONNECT)
        // 申请相机权限
        .permissions(Permission.CAMERA)
        // 申请位置权限
        .permissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION)
        // 如果申请权限之前需要向用户展示权限申请理由，则走此回调
        .onShowRationale { rationalePermissions, callback ->
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this)
                .setTitle("权限申请")
                // 根据 rationalePermissions 进行适当的提示
                .setMessage(
                    "应用需要以下权限：" +
                      "${mapPermissions(rationalePermissions).joinToString("、 ")}。" +
                      "如果拒绝授予这些权限，则应用部分功能将受限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("去申请") { dialog, _ ->
                    dialog.dismiss()
                    // 用户同意，通过此回调通知框架开始权限申请
                    callback.onConsent()
                }
                .show()
        }
        // 如果用户同意了所有权限则走此回调
        .onAllGranted {
            toast("所有权限获取成功")
        }
        // 如果用户只同意部分权限则走此回调
        .onPartialGranted { grantedList ->
            toast("获取到了以下权限：${mapPermissions(grantedList).joinToString("、 ")}")
        }
        // 如果用户选择了拒绝且不再询问，则走此回调
        .onPermanentDenied { deniedList ->
            // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
            AlertDialog.Builder(this)
                .setTitle("权限被永久拒绝")
                // 根据被拒绝的权限列表进行适当的提示
                .setMessage(
                    "以下权限被永久拒绝：" +
                      "${mapPermissions(deniedList).joinToString("、 ")}。" +
                      "请进入设置界面手动授予权限"
                )
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("去设置") { dialog, _ ->
                    dialog.dismiss()
                    XXPermissions.startPermissionActivity(this,
                        deniedList, object : OnPermissionPageCallback {
                            override fun onGranted() {
                                toast("设置界面-权限获取成功")
                            }

                            override fun onDenied() {
                                toast("设置界面-权限获取失败")
                                requestBleCameraLocationPermissions()
                            }
                        })
                }
                .show()
        }
        // 如果用户选择了拒绝，则走此回调
        .onTemporaryDenied { deniedList ->
            toast("以下权限被拒绝：${mapPermissions(deniedList).joinToString("、 ")}")
            requestBleCameraLocationPermissions()
        }
        .request()
}

fun mapPermissions(permissions: List<String>): List<String> {
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
    return msgList
}