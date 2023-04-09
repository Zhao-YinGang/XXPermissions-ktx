package com.zhaoyingang.permission

fun interface OnPermissionsGranted {
    /**
     * 有权限被同意授予时回调
     *
     * @param grantedPermissions           请求成功的权限组
     * @param allGranted            是否全部授予了
     */
    fun onGranted(grantedPermissions: List<String>, allGranted: Boolean)
}