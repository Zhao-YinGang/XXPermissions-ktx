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
 * 需要展示申请原因的权限
 */
interface RationalePermissions {
    val rationaleList: List<String>     // 所有需要展示申请原因的权限
    val onConsent: OnConsentCallback    // 展示申请原因后，如果用户同意，则调用此回调，否则申请流程直接结束不需要调用
}

fun interface OnShowRequestPermissionRationale {
    /**
     * 需要展示申请原因时回调
     *
     * @param rationale      需要展示申请原因的权限
     */
    fun onShowRationale(rationale: RationalePermissions)
}