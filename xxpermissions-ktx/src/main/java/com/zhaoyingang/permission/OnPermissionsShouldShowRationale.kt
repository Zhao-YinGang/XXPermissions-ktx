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

fun interface OnPermissionsShouldShowRationale {
    /**
     * Called when you should show request permission rationale.
     *
     * @param shouldShowRationaleList Permissions that you should explain.
     * @param onUserResult Call it when the user agrees or refuses to allow permission request.
     */
    fun onShouldShowRationale(shouldShowRationaleList: List<String>, onUserResult: OnUserResultCallback)
}