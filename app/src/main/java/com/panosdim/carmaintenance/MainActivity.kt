package com.panosdim.carmaintenance

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.KTEO
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.ui.CarDetails
import com.panosdim.carmaintenance.ui.CarSelection
import com.panosdim.carmaintenance.ui.SignOut
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme
import com.panosdim.carmaintenance.utils.checkForNewVersion
import com.panosdim.carmaintenance.utils.createNotificationChannel
import com.panosdim.carmaintenance.utils.refId
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var manager: DownloadManager
    private lateinit var onComplete: BroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel>()
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    Toast.makeText(
                        this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        // Handle new version installation after the download of APK file.
        manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val referenceId = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (referenceId != -1L && referenceId == refId) {
                    val apkUri = manager.getUriForDownloadedFile(refId)
                    val installIntent = Intent(Intent.ACTION_VIEW)
                    installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    installIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    startActivity(installIntent)
                }

            }
        }
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        // Initialize Firebase
        createNotificationChannel(this)
        FirebaseApp.initializeApp(this)

        // Check for new version
        checkForNewVersion(this)

        // Check for Notifications Permissions
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        // Check for expired items
        val expiredBuilder =
            PeriodicWorkRequestBuilder<ExpiredWorker>(30, TimeUnit.DAYS)

        val expiredWork = expiredBuilder.build()
        // Then enqueue the recurring task:
        WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork(
            "carExpired",
            ExistingPeriodicWorkPolicy.KEEP,
            expiredWork
        )

        setContent {
            val selectedCar by viewModel.selectedCar.collectAsState()
            val options = viewModel.cars.collectAsStateWithLifecycle(initialValue = emptyList())

            CarMaintenanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(padding)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CarSelection(
                                options = options.value,
                                addNewCar = { viewModel.addNewCar(it) },
                                changeSelectedCar = { viewModel.changeSelectedCar(it) }
                            )
                            SignOut()
                        }
                        Spacer(Modifier.size(padding))
                        CarDetails(selectedCar) { viewModel.updateCar(it) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val padding = 8.dp

    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150000", nextService = "175000")
    )

    val options =
        listOf(
            Car(
                id = "1",
                name = "Opel Corsa",
                service = Service(date = "09-04-2023", "213000", "235000"),
                kteo = KTEO(date = "03-01-2024", exhaustCard = "03-01-2023")
            ),
            Car(
                id = "2",
                name = "Opel Crossland",
                service = Service(date = "01-03-2023", "85000", "95000"),
                kteo = KTEO(date = "15-06-2024", exhaustCard = "15-06-2023")
            )
        )


    CarMaintenanceTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(padding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CarSelection(
                        options = options,
                        changeSelectedCar = { },
                        addNewCar = { })
                    SignOut()
                }
                Spacer(Modifier.size(padding))
                CarDetails(selectedCar) {}
            }
        }
    }
}