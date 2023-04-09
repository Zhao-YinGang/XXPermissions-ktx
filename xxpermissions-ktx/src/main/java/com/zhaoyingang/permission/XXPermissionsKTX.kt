@file:Suppress("unused", "deprecation")

package com.zhaoyingang.permission

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

class XXPermissionsKTX private constructor(private val context: Context) {
    private val permissionList = mutableListOf<String>()
    private var onPermissionDenied: OnPermissionsDenied? = null
    private var onPermissionsPermanentDenied: OnPermissionsPermanentDenied? = null
    private var onPermissionsTemporaryDenied: OnPermissionsTemporaryDenied? = null
    private var onPermissionGranted: OnPermissionsGranted? = null
    private var onAllPermissionsGranted: OnAllPermissionsGranted? = null
    private var onPartialPermissionsGranted: OnPartialPermissionsGranted? = null
    private var onShowRequestPermissionRationale: OnShowRequestPermissionRationale? = null

    companion object {
        fun with(context: Context): XXPermissionsKTX {
            return XXPermissionsKTX(context)
        }

        fun with(fragment: Fragment): XXPermissionsKTX {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                with(requireNotNull(fragment.context))
            } else {
                with(requireNotNull(fragment.activity))
            }
        }

        fun with(fragment: androidx.fragment.app.Fragment): XXPermissionsKTX {
            return with(fragment.requireContext())
        }
    }

    /**
     * 添加权限
     */
    fun permission(permission: String): XXPermissionsKTX {
        permissionList.add(permission)
        return this
    }

    /**
     * 添加多个权限
     */
    fun permissions(vararg permissions: String): XXPermissionsKTX {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * 添加多个权限
     */
    fun permissions(vararg permissions: Array<out String>): XXPermissionsKTX {
        permissions.forEach {
            permissionList.addAll(it)
        }
        return this
    }

    /**
     * 添加多个权限
     */
    fun permissions(permissions: List<String>): XXPermissionsKTX {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * 添加多个权限
     */
    fun permissions(vararg permissions: List<String>): XXPermissionsKTX {
        permissions.forEach {
            permissionList.addAll(it)
        }
        return this
    }

    /**
     * 有权限被拒绝授予时回调
     */
    fun onDenied(onPermissionDenied: OnPermissionsDenied): XXPermissionsKTX {
        this.onPermissionDenied = onPermissionDenied
        return this
    }

    /**
     * 有权限被永久拒绝授予时回调
     */
    fun onPermanentDenied(onPermissionsPermanentDenied: OnPermissionsPermanentDenied): XXPermissionsKTX {
        this.onPermissionsPermanentDenied = onPermissionsPermanentDenied
        return this
    }


    /**
     * 有权限被暂时拒绝授予时回调
     */
    fun onTemporaryDenied(onPermissionsTemporaryDenied: OnPermissionsTemporaryDenied): XXPermissionsKTX {
        this.onPermissionsTemporaryDenied = onPermissionsTemporaryDenied
        return this
    }

    /**
     * 有权限被同意授予时回调
     */
    fun onGranted(onPermissionGranted: OnPermissionsGranted): XXPermissionsKTX {
        this.onPermissionGranted = onPermissionGranted
        return this
    }

    /**
     * 全部权限被同意授予时回调
     */
    fun onAllGranted(onAllPermissionsGranted: OnAllPermissionsGranted): XXPermissionsKTX {
        this.onAllPermissionsGranted = onAllPermissionsGranted
        return this
    }

    /**
     * 部分权限被同意授予时回调
     */
    fun onPartialGranted(onPartialPermissionsGranted: OnPartialPermissionsGranted): XXPermissionsKTX {
        this.onPartialPermissionsGranted = onPartialPermissionsGranted
        return this
    }

    /**
     * 需要展示申请原因时回调
     */
    fun onShowRationale(onShowRequestPermissionRationale: OnShowRequestPermissionRationale): XXPermissionsKTX {
        this.onShowRequestPermissionRationale = onShowRequestPermissionRationale
        return this
    }

    fun request() {
        return XXPermissions.with(context)
            .permission(permissionList)
            .interceptor(object : IPermissionInterceptor {
                override fun launchPermissionRequest(
                    activity: Activity, allPermissions: MutableList<String>, callback: OnPermissionCallback?
                ) {
                    val onShowRationale = onShowRequestPermissionRationale
                    if (onShowRationale == null || XXPermissions.isPermanentDenied(activity, allPermissions)) {
                        super.launchPermissionRequest(activity, allPermissions, callback)
                        return
                    }
                    val rationalePermissions = allPermissions.filter {
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                    }
                    if (rationalePermissions.isNotEmpty()) {
                        onShowRationale.onShowRationale(rationalePermissions) {
                            super.launchPermissionRequest(activity, allPermissions, callback)
                        }
                    } else {
                        super.launchPermissionRequest(activity, allPermissions, callback)
                    }
                }
            })
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, allGranted: Boolean) {
                    onPermissionGranted?.onGranted(permissions, allGranted)
                    if (allGranted) {
                        onAllPermissionsGranted?.onAllGranted()
                    } else {
                        onPartialPermissionsGranted?.onPartialGranted(permissions)
                    }
                }

                override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                    onPermissionDenied?.onDenied(permissions, doNotAskAgain)
                    if (doNotAskAgain) {
                        onPermissionsPermanentDenied?.onPermanentDenied(permissions)
                    } else {
                        onPermissionsTemporaryDenied?.onTemporaryDenied(permissions)
                    }
                }
            })
    }
}