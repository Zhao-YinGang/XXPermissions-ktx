package com.zhaoyingang.permission

fun interface OnPermissionsDenied {
    /**
     * 有权限被拒绝授予时回调
     *
     * @param deniedPermissions            请求失败的权限组
     * @param doNotAskAgain          是否勾选了不再询问选项
     */
    fun onDenied(deniedPermissions: List<String>, doNotAskAgain: Boolean)
}