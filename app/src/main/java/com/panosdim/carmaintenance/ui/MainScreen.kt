package com.panosdim.carmaintenance.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.panosdim.carmaintenance.LoginActivity
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Response
import com.panosdim.carmaintenance.onComplete
import com.panosdim.carmaintenance.paddingLarge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val carsResponse =
        viewModel.cars.collectAsStateWithLifecycle(initialValue = Response.Loading)
    var cars by remember { mutableStateOf(emptyList<Car>()) }
    rememberPagerState(pageCount = { cars.size })
    var selectedCar by remember { mutableStateOf<Car?>(null) }

    var isLoading by remember {
        mutableStateOf(false)
    }
    val skipPartiallyExpanded by remember { mutableStateOf(true) }

    val newCarSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    when (carsResponse.value) {
        is Response.Success -> {
            isLoading = false

            cars = emptyList()
            cars =
                (carsResponse.value as Response.Success<List<Car>>).data

            val oldSelectedCar = selectedCar
            selectedCar = null
            selectedCar = cars.find { it.id == oldSelectedCar?.id }
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
        if (selectedCar != null) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = selectedCar!!.name)
                        },
                        navigationIcon = {
                            IconButton(onClick = { selectedCar = null }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                    )
                },
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    CarDetails(selectedCar!!)
                }
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(id = R.string.app_name))
                        },
                        actions = {
                            IconButton(onClick = {
                                context.unregisterReceiver(onComplete)
                                Firebase.auth.signOut()

                                val intent = Intent(context, LoginActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { scope.launch { newCarSheetState.show() } },
                        expanded = true,
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                stringResource(id = R.string.add_new_car)
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.add_new_car)) },
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (cars.isNotEmpty()) {
                        cars.forEach {
                            CarCard(it) { car ->
                                selectedCar = car
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
                            ElevatedCard(
                                modifier = Modifier
                                    .padding(paddingLarge)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Column {
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
                        }
                    }
                }
            }
        }
        AddNewCarDialog(bottomSheetState = newCarSheetState)
    }
}