package com.blankj.utilcode.pkg.helper


import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.PermissionUtils.OnRationaleListener.ShouldRequest
import com.dykj.module.R

/**
 * ```
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2018/01/10
 * desc  : helper about dialog
 * ```
 */

 class DialogHelper {

   fun showRationaleDialog(shouldRequest: ShouldRequest) {
        val topActivity = ActivityUtils.getTopActivity()
        AlertDialog.Builder(topActivity)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(R.string.permission_rationale_message)
                .setPositiveButton(android.R.string.ok) { _, _ -> shouldRequest.again(true) }
                .setNegativeButton(android.R.string.cancel) { _, _ -> shouldRequest.again(false) }
                .setCancelable(false)
                .create()
                .show()
    }

    fun showOpenAppSettingDialog() {
        val topActivity = ActivityUtils.getTopActivity()
        AlertDialog.Builder(topActivity)
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(R.string.permission_denied_forever_message)
            .setPositiveButton(android.R.string.ok) { _, _ -> PermissionUtils.launchAppDetailsSettings() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setCancelable(false)
            .create()
            .show()
    }

}
