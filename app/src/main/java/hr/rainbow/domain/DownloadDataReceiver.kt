package hr.rainbow.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.rainbow.ui.MainActivity

class DownloadDataReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        MainActivity.startActivity(context, Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}