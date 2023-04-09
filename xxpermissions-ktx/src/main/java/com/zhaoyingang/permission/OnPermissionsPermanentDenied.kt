package com.zhaoyingang.permission

fun interface OnPermissionsPermanentDenied {
    /**
     * 有权限被永久拒绝授予时回调
     *
     * @param deniedPermissions            请求失败的权限组
     */
    fun onPermanentDenied(deniedPermissions: List<String>)
}