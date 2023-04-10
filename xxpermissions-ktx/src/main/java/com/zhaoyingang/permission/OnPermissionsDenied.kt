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
 * 被拒绝的权限
 */
interface DeniedPermissions {
    val deniedList: List<String>        // 所有被拒绝的权限
    val askAgainList: List<String>      // 可以再次发起申请的权限
    val doNotAskAgainList: List<String> // 被勾选了不再询问选项，不要进入设置界面手动开启的权限
    val hasDoNotAskAgain: Boolean       // 是否包含被勾选了不再询问选项的权限
    val onConsent: OnConsentCallback   // 如果包含被勾选了不再询问选项的权限，用户查看申请原因并同意后执行此回调，进入应用设置界面
}

fun interface OnPermissionsDenied {
    /**
     * 有权限被拒绝授予时回调
     *
     * @param denied    被拒绝的权限
     */
    fun onDenied(denied: DeniedPermissions)
}