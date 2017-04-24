package ch.abertschi.adump.presenter

import android.content.Context
import android.provider.Settings
import ch.abertschi.adump.UpdateManager
import ch.abertschi.adump.model.PreferencesFactory
import ch.abertschi.adump.view.home.HomeView
import com.github.javiersantos.appupdater.AppUpdater
import org.jetbrains.anko.AnkoLogger

/**
 * Created by abertschi on 15.04.17.
 */

class HomePresenter(val homeView: HomeView, val preferencesFactory: PreferencesFactory) : AnkoLogger {

    lateinit private var mAppUpdater: AppUpdater

    fun onCreate(context: Context) {
        showPermissionRequiredIfNecessary(context)
        homeView.setPowerState(preferencesFactory.isBlockingEnabled())
        mAppUpdater = UpdateManager().appUpdaterForInAppUse(context)
        mAppUpdater.start()
    }

    fun onResume(context: Context) {
        showPermissionRequiredIfNecessary(context)
    }

    private fun showPermissionRequiredIfNecessary(context: Context) {
        if (hasNotificationPermission(context)) {
            homeView.showEnjoyAdFree()
        } else {
            homeView.showPermissionRequired()
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        val permission = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        if (permission == null || !permission.contains(context.packageName)) {
            return false
        }
        return true
    }

    fun enabledStatusChanged(status: Boolean) {
        preferencesFactory.setBlockingEnabled(status)
    }
}
