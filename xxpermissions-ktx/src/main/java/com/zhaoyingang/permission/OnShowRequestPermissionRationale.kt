package com.zhaoyingang.permission

fun interface OnShowRequestPermissionRationale {

    /**
     * 需要展示申请原因时回调
     *
     * @param rationalePermissions      展示申请原因
     * @param onRationaleResult         展示申请原因后根据用户是否同意调用此回调，必须调用
     */
    fun onShowRationale(
        rationalePermissions: List<String>,
        onRationaleResult: OnConsentRationale
    )
}