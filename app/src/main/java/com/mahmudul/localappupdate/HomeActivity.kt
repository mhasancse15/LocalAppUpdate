package com.mahmudul.localappupdate

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.mahmudul.easyappupdate.DownloadApp

class HomeActivity : AppCompatActivity() {
    private var versionText: TextView? = null
    private var version: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        versionText = findViewById(R.id.txt_version)
        with(versionText) { this?.setText("Current Version $version") }
    }
    /** TODO: Must need to check the External Storage Permission Because we are storing the
     * ApK in the External Or Internal Storage.
     */
    private fun checkWriteExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            downloadTask()
        } else {
            requestWriteExternalStoragePermission()
        }
    }

    private fun requestWriteExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadTask()
        } else {
            Toast.makeText(this@HomeActivity, "Permission Not Granted.", Toast.LENGTH_SHORT).show()
        }
    }

    fun download(view: View?) {
        checkWriteExternalStoragePermission()
    }

    private fun downloadTask() {
        val downloadApk = DownloadApp(this@HomeActivity)
        downloadApk.startDownloadingApk(
            "https://github.com/mhasancse15/LocalAppUpdate/blob/main/app/release/app-release.apk?raw=true",
            "Update 2.0"
        )
    }

    companion object {
        private const val MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1001
    }
}