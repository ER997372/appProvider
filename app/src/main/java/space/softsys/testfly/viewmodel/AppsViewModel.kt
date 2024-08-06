package space.softsys.testfly.viewmodel

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.softsys.testfly.model.ApiResponse
import space.softsys.testfly.remote.RetrofitClient
import java.io.File


class AppsViewModel : ViewModel() {

    val apps = MutableLiveData<ApiResponse>()

    fun queryApps() {
        viewModelScope.launch {
            try {
                val call = RetrofitClient.apiService.getApps()
                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                        if(response.isSuccessful) {
                            apps.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.e("RetrofitError", t.message.toString())
                    }

                })
            } catch(e: Exception) {
                Log.e("RetrofitError", e.message.toString())
            }
        }
    }

    fun performDownload(context: Context, id: Int, name: String) {
        viewModelScope.launch {
            try {
                val fileName = "app.apk"
                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = Uri.parse("http://192.168.100.10:3000/api/download/$id")
                val token = "wV6t>nQxo7p2gQ(?pRe<[l5HSW*/[pQa"

                // Create request for android download manager
                val request = DownloadManager.Request(uri).apply {
                    setTitle("Downloading APK")
                    setDescription("Downloading $name")
                    setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setAllowedOverMetered(true)
                    setAllowedOverRoaming(true)
                    addRequestHeader("Authorization","Bearer $token")
                }

                // Enqueue the download and get the download ID
                val downloadId = downloadManager.enqueue(request)

                // Register a receiver to listen for when the download completes
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                        if (id == downloadId) {
                            // Download completed, initiate APK installation
                            val apkFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                            installApk(context, apkFile)
                        }
                    }
                }

                // Register the receiver for download completion
                context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            } catch(e: Exception) {
                Log.e("DownloadError", e.message.toString())
            }
        }
    }

    private fun installApk(context: Context, apkFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!context.packageManager.canRequestPackageInstalls()) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                    .setData(Uri.parse("package:${context.packageName}"))
                context.startActivity(intent)
                return
            }
        }

        val apkUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)
        } else {
            Uri.fromFile(apkFile)
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)
    }

    fun fetchApps(): MutableLiveData<ApiResponse>{
        queryApps()
        return apps;
    }
}