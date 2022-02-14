package hr.rainbow.util

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.rainbow.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

fun BottomNavigationView.uncheckAllItems() {
    menu.setGroupCheckable(0, true, false)

    for (i in 0 until menu.size()) {
        menu.getItem(i).isChecked = false
    }

    menu.setGroupCheckable(0, true, true)
}

fun createNotificationChannel(
    context: Context,
    channel_id: String,
    name: String,
    description: String,
    importance: Int
) {
    val channel = NotificationChannel(channel_id, name, importance)
    channel.description = description

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    notificationManager?.createNotificationChannel(channel)
}

fun notifyProgressNotification(
    context: Context,
    notification_id: Int,
    channel_id: String,
    title: String,
    message: String,
    max: Int,
    progress: Int,
    indeterminate: Boolean

) {
    val builder = NotificationCompat.Builder(context, channel_id)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setVibrate(LongArray(0))
        .setProgress(max, progress, indeterminate)

    NotificationManagerCompat.from(context).notify(notification_id, builder.build())
}

@Throws(Exception::class)
fun Context.writeBitmapToTmpFile(bitmap: Bitmap): Uri {
    val name = String.format("FromCamera%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, TMP_DIR)

    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }

        }
    }
    return Uri.fromFile(outputFile)
}

@kotlin.jvm.Throws(IOException::class)
fun Context.createTmpFile(): Uri {
    val name = String.format("TmpFile-%s", UUID.randomUUID().toString())
    val outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    val file = File.createTempFile(
        name,
        ".png",
        outputDir
    )
    return FileProvider.getUriForFile(
        this,
        "hr.rainbow.images",
        file
    )
}

fun Context.showErrorDialog(
    title: String,
    message: String,
    okListener: DialogInterface.OnClickListener?
) {
    AlertDialog.Builder(this, R.style.AppTheme_ErrorAlertDialog).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.ok), okListener)
        setCancelable(false)
        create()
        show()
    }
}

fun Context.showConfirmDialog(
    title: String,
    message: String,
    okListener: DialogInterface.OnClickListener?
) {
    AlertDialog.Builder(this, R.style.AppTheme_ErrorAlertDialog).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.yes), okListener)
        setNegativeButton(getString(R.string.no), null)
        setCancelable(false)
        create()
        show()
    }
}


fun Context.permissionGranted(activity: Activity, permission: String, key: Int): Boolean {
    val isPermissionDenied =
        ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_DENIED

    return if (isPermissionDenied) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            key
        )
        false
    } else {
        true
    }
}

var didDoubleClickHappen: Boolean = false
var lastClickTime: Long = 0
fun isDoubleClick(): Boolean {

    val isDoubleClick = SystemClock.elapsedRealtime() - lastClickTime < DOUBLE_CLICK_TIME_BUFFER
    lastClickTime = SystemClock.elapsedRealtime()
    didDoubleClickHappen = isDoubleClick
    return isDoubleClick
}

fun fireOnSecondClick(task: () -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        delay(DOUBLE_CLICK_TIME_BUFFER)
        if (!didDoubleClickHappen
            && SystemClock.elapsedRealtime() - lastClickTime > DOUBLE_CLICK_TIME_BUFFER
        ) {
            didDoubleClickHappen = false
            task()
        }
    }
}

fun fireOnDoubleClick(task: () -> Unit) {
    if (isDoubleClick()) {
        task()
    }
}

fun getMondayDateOfWeek(date: LocalDate): LocalDate {
    return if (date.dayOfWeek == DayOfWeek.MONDAY) {
        date
    } else {
        getMondayDateOfWeek(date.minusDays(1))
    }
}

fun View.hideKeyboard(){
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    return false
}