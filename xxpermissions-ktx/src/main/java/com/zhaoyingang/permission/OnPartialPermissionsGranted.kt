package com.zhaoyingang.permission

fun interface OnPartialPermissionsGranted {
    /**
     * 部分权限被同意授予时回调
     *
     * @param grantedPermissions           请求成功的权限组
     */
    fun onPartialGranted(grantedPermissions: List<String>)
}