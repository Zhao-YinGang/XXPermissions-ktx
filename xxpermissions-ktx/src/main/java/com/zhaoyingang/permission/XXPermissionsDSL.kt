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

inline fun Activity.xxPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(XXPermissions4j.with(this)).apply { block(this) }.xxPermissions.request()

inline fun androidx.fragment.app.Fragment.xxPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(XXPermissions4j.with(this)).apply { block(this) }.xxPermissions.request()

inline fun Fragment.xxPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(XXPermissions4j.with(this)).apply { block(this) }.xxPermissions.request()

class XXPermissionsDSL(@PublishedApi internal val xxPermissions: XXPermissions4j) {
    /**
     * 添加权限
     */
    fun permission(permission: String) {
        xxPermissions.permission(permission)
    }

    /**
     * 添加多个权限
     */
    fun permissions(vararg permissions: String) {
        xxPermissions.permissions(permissions)
    }

    /**
     * 添加多个权限
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out String>) {
        xxPermissions.permissions(permissions)
    }

    /**
     * 添加多个权限
     */
    fun permissions(permissions: List<String>) {
        xxPermissions.permissions(permissions)
    }

    /**
     * 需要展示申请原因时回调
     */
    fun onShowRationale(onShowRationale: OnShowRequestPermissionRationale) {
        xxPermissions.onShowRationale(onShowRationale)
    }

    /**
     * 有权限被拒绝授予时回调
     */
    fun onDenied(onDenied: OnPermissionsDenied) {
        xxPermissions.onDenied(onDenied)
    }

    /**
     * 有权限被同意授予时回调
     */
    fun onGranted(onDenied: OnPermissionsGranted) {
        xxPermissions.onGranted(onDenied)
    }
}