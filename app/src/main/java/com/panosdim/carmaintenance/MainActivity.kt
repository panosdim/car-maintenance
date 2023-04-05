package com.panosdim.carmaintenance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Service
import com.panosdim.carmaintenance.ui.CarDetails
import com.panosdim.carmaintenance.ui.CarSelection
import com.panosdim.carmaintenance.ui.SignOut
import com.panosdim.carmaintenance.ui.theme.CarMaintenanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel>()

        setContent {
            val selectedCar by viewModel.selectedCar.collectAsState()

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
                            CarSelection(viewModel)
                            SignOut()
                        }
                        Spacer(Modifier.size(padding))
                        CarDetails(selectedCar)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val carsViewModel = MainViewModel()
    val padding = 8.dp

    val selectedCar = Car(
        name = "Test Car",
        service = Service(date = "03-04-2023", odometer = "150.000km", nextService = "175.000km")
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
                    CarSelection(carsViewModel)
                    SignOut()
                }
                Spacer(Modifier.size(padding))
                CarDetails(selectedCar)
            }
        }
    }
}