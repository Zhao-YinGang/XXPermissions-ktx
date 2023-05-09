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
    XXPermissionsDSL(XXPermissionsExt.with(this)).apply { block(this) }.xxPermissions.request()

inline fun androidx.fragment.app.Fragment.xxPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(XXPermissionsExt.with(this)).apply { block(this) }.xxPermissions.request()

inline fun Fragment.xxPermissions(block: XXPermissionsDSL.() -> Unit) =
    XXPermissionsDSL(XXPermissionsExt.with(this)).apply { block(this) }.xxPermissions.request()

class XXPermissionsDSL(@PublishedApi internal val xxPermissions: XXPermissionsExt) {
    /**
     * add permissions
     */
    fun permissions(vararg permissions: String) {
        xxPermissions.permissions(permissions)
    }

    /**
     * add permissions
     */
    @JvmName("permissionsArray")
    fun permissions(permissions: Array<out String>) {
        xxPermissions.permissions(permissions)
    }

    /**
     * add permissions
     */
    fun permissions(permissions: List<String>) {
        xxPermissions.permissions(permissions)
    }

    /**
     * Called when you should tell user to allow these permissions in settings.
     */
    fun onDoNotAskAgain(onDoNotAskAgain: OnPermissionsDoNotAskAgain) {
        xxPermissions.onDoNotAskAgain(onDoNotAskAgain)
    }

    /**
     * Called when you should show request permission rationale.
     */
    fun onShouldShowRationale(onShouldShowRationale: OnPermissionsShouldShowRationale) {
        xxPermissions.onShouldShowRationale(onShouldShowRationale)
    }

    /**
     * Callback for the permissions request result.
     */
    fun onResult(onResult: OnPermissionResult) {
        xxPermissions.onResult(onResult)
    }
}