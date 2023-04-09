package com.zhaoyingang.permission

fun interface OnPermissionsTemporaryDenied {
    /**
     * 有权限被暂时拒绝授予时回调
     *
     * @param deniedPermissions            请求失败的权限组
     */
    fun onTemporaryDenied(deniedPermissions: List<String>)
}