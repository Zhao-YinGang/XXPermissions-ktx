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

package com.zhaoyingang.permission

/**
 * 被授予的权限
 */
interface GrantedPermissions {
    val grantedList: List<String>   // 所有被授予的权限
    val isAllGranted: Boolean         // 申请的权限是否全部被授予了
}

fun interface OnPermissionsGranted {
    /**
     * 有权限被同意授予时回调
     *
     * @param granted       被授予的权限
     */
    fun onGranted(granted: GrantedPermissions)
}