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

@file:Suppress("unused", "deprecation")

package com.zhaoyingang.permission

import android.app.Activity
import android.app.Fragment
import androidx.core.app.ActivityCompat
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.OnPermissionPageCallback
import com.hjq.permissions.XXPermissions
import com.zhaoyingang.permission.internal.DeniedPermissionsImpl
import com.zhaoyingang.permission.internal.EmptyOnConsentCallback
import com.zhaoyingang.permission.internal.GrantedPermissionsImpl
import com.zhaoyingang.permission.internal.RationalePermissionsImpl

class XXPermissions4j private constructor(private val activity: Activity) {
    private val permissionList = mutableListOf<String>()
    private var onDenied: OnPermissionsDenied? = null
    private var onGranted: OnPermissionsGranted? = null
    private var onShowRationale: OnShowRequestPermissionRationale? = null

    companion object {
        @JvmStatic
        fun with(activity: Activity): XXPermissions4j {
            return XXPermissions4j(activity)
        }

        @JvmStatic
        fun with(fragment: Fragment): XXPermissions4j {
            return with(requireNotNull(fragment.activity))
        }

        @JvmStatic
        fun with(fragment: androidx.fragment.app.Fragment): XXPermissions4j {
            return with(fragment.requireActivity())
        }
    }

    /**
     * 添加权限
     */
    fun permission(permission: String): XXPermissions4j {
        permissionList.add(permission)
        return this
    }

    /**
     * 添加多个权限
     */
    fun permissions(vararg permissions: String): XXPermissions4j {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * 添加多个权限
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out String>): XXPermissions4j {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * 添加多个权限
     */
    fun permissions(permissions: List<String>): XXPermissions4j {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * 需要展示申请原因时回调
     */
    fun onShowRationale(onShowRationale: OnShowRequestPermissionRationale): XXPermissions4j {
        this.onShowRationale = onShowRationale
        return this
    }

    /**
     * 有权限被拒绝授予时回调
     */
    fun onDenied(onDenied: OnPermissionsDenied): XXPermissions4j {
        this.onDenied = onDenied
        return this
    }

    /**
     * 有权限被同意授予时回调
     */
    fun onGranted(onGranted: OnPermissionsGranted): XXPermissions4j {
        this.onGranted = onGranted
        return this
    }

    /**
     * 发起权限请求
     */
    fun request() {
        return XXPermissions.with(activity)
            .permission(permissionList)
            .interceptor(object : IPermissionInterceptor {
                override fun launchPermissionRequest(
                    activity: Activity, allPermissions: MutableList<String>, callback: OnPermissionCallback?
                ) {
                    val onShowRationale = onShowRationale
                    if (onShowRationale == null || XXPermissions.isPermanentDenied(activity, allPermissions)) {
                        super.launchPermissionRequest(activity, allPermissions, callback)
                        return
                    }
                    val rationalePermissions = allPermissions.filter {
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                    }
                    if (rationalePermissions.isNotEmpty()) {
                        onShowRationale.onShowRationale(
                            RationalePermissionsImpl(
                                rationaleList = rationalePermissions,
                                onConsent = {
                                    super.launchPermissionRequest(activity, allPermissions, callback)
                                }
                            )
                        )
                    } else {
                        super.launchPermissionRequest(activity, allPermissions, callback)
                    }
                }
            })
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, allGranted: Boolean) {
                    onGranted?.onGranted(
                        GrantedPermissionsImpl(
                            grantedList = permissions,
                            isAllGranted = allGranted
                        )
                    )
                }

                override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                    onDenied?.onDenied(
                        DeniedPermissionsImpl(
                            deniedList = permissions,
                            doNotAskAgainList = if (doNotAskAgain) permissions.filter { XXPermissions.isPermanentDenied(activity, it) } else emptyList(),
                            onConsent = if (doNotAskAgain) OnConsentCallback {
                                XXPermissions.startPermissionActivity(activity,
                                    permissions, object : OnPermissionPageCallback {
                                        override fun onGranted() {
                                            request()
                                        }

                                        override fun onDenied() {
                                            request()
                                        }
                                    })
                            } else EmptyOnConsentCallback
                        )
                    )
                }
            })
    }
}