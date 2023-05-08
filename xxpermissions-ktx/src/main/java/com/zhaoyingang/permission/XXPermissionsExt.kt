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

class XXPermissionsExt private constructor(private val activity: Activity) {
    private val permissionList = mutableListOf<String>()
    private var onResult: OnPermissionResult? = null
    private var onShouldShowRationale: OnPermissionsShouldShowRationale? = null
    private var onDoNotAskAgain: OnPermissionsDoNotAskAgain? = null

    companion object {
        @JvmStatic
        fun with(activity: Activity): XXPermissionsExt {
            return XXPermissionsExt(activity)
        }

        @JvmStatic
        fun with(fragment: Fragment): XXPermissionsExt {
            return with(requireNotNull(fragment.activity))
        }

        @JvmStatic
        fun with(fragment: androidx.fragment.app.Fragment): XXPermissionsExt {
            return with(fragment.requireActivity())
        }
    }

    /**
     * add permissions
     */
    fun permissions(vararg permissions: String): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * add permissions
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out String>): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * add permissions
     */
    fun permissions(permissions: List<String>): XXPermissionsExt {
        permissionList.addAll(permissions)
        return this
    }

    /**
     * Called when you should tell user to allow these permissions in settings.
     */
    fun onDoNotAskAgain(onDoNotAskAgain: OnPermissionsDoNotAskAgain): XXPermissionsExt {
        this.onDoNotAskAgain = onDoNotAskAgain
        return this
    }

    /**
     * Called when you should show request permission rationale.
     */
    fun onShouldShowRationale(onShouldShowRationale: OnPermissionsShouldShowRationale): XXPermissionsExt {
        this.onShouldShowRationale = onShouldShowRationale
        return this
    }

    /**
     * Callback for the permissions request result.
     */
    fun onResult(onResult: OnPermissionResult): XXPermissionsExt {
        this.onResult = onResult
        return this
    }

    /**
     * 发起权限请求
     */
    fun request() {
        return XXPermissions.with(activity).permission(permissionList).interceptor(object : IPermissionInterceptor {
                override fun launchPermissionRequest(activity: Activity, allList: List<String>, callback: OnPermissionCallback?) {
                    val showRationale = onShouldShowRationale ?: return super.launchPermissionRequest(activity, allList, callback)
                    val rationalePermissions = allList.filter { ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }
                    if (rationalePermissions.isEmpty()) return super.launchPermissionRequest(activity, allList, callback)

                    showRationale.onShouldShowRationale(rationalePermissions) { isAgree ->
                        if (isAgree) {
                            super.launchPermissionRequest(activity, allList, callback)
                        } else {
                            val deniedList = XXPermissions.getDenied(activity, allList)
                            callback?.onDenied(deniedList, false)
                        }
                    }
                }

                override fun deniedPermissionRequest(
                    activity: Activity, allList: List<String>, deniedList: List<String>, doNotAskAgain: Boolean, callback: OnPermissionCallback?
                ) {
                    if (doNotAskAgain) {
                        showPermissionSettingDialog(activity, allList, deniedList, callback)
                    } else {
                        callback?.onDenied(deniedList, false)
                    }
                }

                private fun showPermissionSettingDialog(
                    activity: Activity, allList: List<String>, deniedList: List<String>, callback: OnPermissionCallback?
                ) {
                    val onDoNotAskAgain = onDoNotAskAgain ?: return super.deniedPermissionRequest(activity, allList, deniedList, true, callback)
                    val doNotAskAgainList = deniedList.filter { XXPermissions.isPermanentDenied(activity, it) }
                    onDoNotAskAgain.onDoNotAskAgain(doNotAskAgainList) { isAgree ->
                        if (isAgree) {
                            XXPermissions.startPermissionActivity(activity, doNotAskAgainList, object : OnPermissionPageCallback {
                                override fun onGranted() {
                                    callback?.onGranted(allList, true)
                                }

                                override fun onDenied() {
                                    request()
                                }
                            })
                        } else {
                            callback?.onDenied(deniedList, true)
                        }
                    }
                }
            }).request(object : OnPermissionCallback {
                private var deniedList: List<String>? = null
                private var grantedList: List<String>? = null
                override fun onGranted(permissions: List<String>, allGranted: Boolean) {
                    if (allGranted || deniedList != null) {
                        onResult?.onResult(allGranted, permissions, deniedList.orEmpty())
                    } else {
                        grantedList = permissions
                    }
                }

                override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                    if (grantedList != null) {
                        onResult?.onResult(false, grantedList.orEmpty(), permissions)
                    } else {
                        deniedList = permissions
                    }
                }
            })
    }
}
