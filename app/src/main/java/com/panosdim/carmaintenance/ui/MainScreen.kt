package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Response

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val carsResponse =
        viewModel.cars.collectAsStateWithLifecycle(initialValue = Response.Loading)
    var cars by remember { mutableStateOf(emptyList<Car>()) }
    val pagerState = rememberPagerState(pageCount = { cars.size })

    var isLoading by remember {
        mutableStateOf(false)
    }

    when (carsResponse.value) {
        is Response.Success -> {
            isLoading = false

            cars =
                (carsResponse.value as Response.Success<List<Car>>).data
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

    if (isLoading) {
        ProgressBar()
    } else {
        HorizontalPager(state = pagerState) { page ->
            CarDetails(selectedCar = cars[page])
        }
    }
}