package com.example.photowastakenapp.Activites

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.photowastakenapp.databinding.ActivityMainBinding
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import com.example.photowastakenapp.R
import com.example.photowastakenapp.Services.ImageService


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    companion object {
        const val STOP = "Stop Service"
        const val START = "Start Service"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        initButtons()
        updateTextButton()
        threadUpdateTextBtn()

    }

    private fun threadUpdateTextBtn() {
        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                mainHandler.postDelayed(this, 1000)
                // updateTextButton According if the Service Working
                updateTextButton()


            }
        })
    }

    // check if permission denied and shoe msg
    private fun permissionDenied() {
        // It is still possible to view the confirmation request message
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            val alertBuilder = android.app.AlertDialog.Builder(this)
            alertBuilder.setCancelable(true)
            alertBuilder.setMessage(getString(R.string.You_Must_Permission))
            alertBuilder.setPositiveButton(
                getString(R.string.Ok)
            ) { _, _ -> startReadExternalStorageLauncher() }
            alertBuilder.setNeutralButton(getString(R.string.Cancel), null)
            alertBuilder.show()

            // After twice the system has presented to the user
        } else {
            // make toast with msg that help the user to accept permission prom settings
            val alertBuilder = android.app.AlertDialog.Builder(this)
            alertBuilder.setCancelable(true)
            alertBuilder.setMessage(getString(R.string.Dont_ask_again_state))
            alertBuilder.setPositiveButton(
                getString(R.string.Got_it)
            ) { _, _ -> openPermissionSettings() }
            alertBuilder.show()
        }

    }

    private fun openPermissionSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)


    }


    private fun startReadExternalStorageLauncher() {
        requestReadStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    private val requestReadStoragePermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startOrStopService()
            } else {
                permissionDenied()

            }
        }


    private fun initButtons() {
        binding.mainBTNStartService.setOnClickListener(startServiceBtn)

    }


    private val startServiceBtn = View.OnClickListener { startReadExternalStorageLauncher() }


    private fun startOrStopService() {
        if (binding.mainBTNStartService.text.equals(START)) {
            actionToService(ImageService.START_IMAGE_SERVICE)
        } else {
            actionToService(ImageService.STOP_IMAGE_SERVICE)
        }

    }


    private fun actionToService(action: String) {

        val intent = Intent(this, ImageService::class.java)
        intent.action = action

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)


        } else {
            startService(intent)

        }

    }

    private fun updateTextButton() {
        if (!ImageService.isServiceRunningInForeground(this, ImageService::class.java)) {
            if (binding.mainBTNStartService.text != START) {
                binding.mainBTNStartService.text = START
            }

        } else {
            if (binding.mainBTNStartService.text != STOP) {
                binding.mainBTNStartService.text = STOP

            }
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        updateTextButton()
    }
}

