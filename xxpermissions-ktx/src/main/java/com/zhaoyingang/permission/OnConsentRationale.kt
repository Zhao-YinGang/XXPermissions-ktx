package com.zhaoyingang.permission

fun interface OnConsentRationale {
    /**
     * 用户查看申请原因并同意后回调
     */
    fun onConsent()
}