package com.zhaoyingang.permission

fun interface OnAllPermissionsGranted {
    /**
     * 全部权限被同意授予时回调
     */
    fun onAllGranted()
}