package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Response

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val carsResponse =
        viewModel.cars.collectAsStateWithLifecycle(initialValue = Response.Loading)
    var cars by remember { mutableStateOf(emptyList<Car>()) }
    rememberPagerState(pageCount = { cars.size })
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    var isLoading by remember {
        mutableStateOf(false)
    }

    when (carsResponse.value) {
        is Response.Success -> {
            isLoading = false

            cars = emptyList()
            val oldSelectedCar = selectedCar
            selectedCar = null

            cars =
                (carsResponse.value as Response.Success<List<Car>>).data

            selectedCar = cars.find { it.id == oldSelectedCar?.id } ?: cars.firstOrNull()
        }

        is Response.Error -> {
            Toast.makeText(
                context,
                (carsResponse.value as Response.Error).errorMessage,
                Toast.LENGTH_SHORT
            )
                .show()

            isLoading = false
        }

        is Response.Loading -> {
            isLoading = true
        }
    }

    val onCarSelected: (Car) -> Unit = { car ->
        selectedCar = car
    }

    if (isLoading) {
        ProgressBar()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .systemBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (cars.isNotEmpty()) {
                selectedCar?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        CarDetails(it)
                    }

                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = stringResource(id = R.string.no_items),
                        modifier = Modifier

                    )
                    Text(
                        text = stringResource(id = R.string.no_items)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                BottomCard(cars, onCarSelected)
            }
        }
    }
}