package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Cable
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.NoCrash
import androidx.compose.material.icons.outlined.TireRepair
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Response
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toLocalDate
import java.time.LocalDate

@Composable
fun CarCard(carResponse: State<Response<List<Car>>>, index: Int) {
    val car = when (carResponse.value) {
        is Response.Success -> {
            (carResponse.value as Response.Success<List<Car>>).data[index]
        }

        else -> Car()
    }
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val openDeleteDialog = remember { mutableStateOf(false) }
    var openDialog by rememberSaveable { mutableStateOf(false) }

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    ElevatedCard(
        modifier = Modifier
            .padding(paddingSmall)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        onClick = {}
    ) {
        Column(
            Modifier
                .padding(paddingLarge)
                .fillMaxWidth()
        ) {
            // Car name
            Text(
                text = car.name,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.padding(paddingSmall))
            // Purchase Date
            Text(
                text = car.purchaseDate,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.padding(paddingLarge))

            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    text = { Text(stringResource(R.string.service)) },
                    icon = { Icon(Icons.Outlined.CarRepair, contentDescription = null) },
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 }
                )
                Tab(
                    text = { Text(stringResource(R.string.tires)) },
                    icon = { Icon(Icons.Outlined.TireRepair, contentDescription = null) },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 }
                )
                Tab(
                    text = { Text(stringResource(R.string.kteo)) },
                    icon = {
                        BadgedBox(
                            badge = {
                                Badge {
                                    KteoStatusIcon(car)
                                }
                            }
                        ) {
                            Icon(Icons.Outlined.NoCrash, contentDescription = "KTEO")
                        }
                    },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 2 }
                )
                Tab(
                    text = { Text(stringResource(R.string.injector)) },
                    icon = { Icon(Icons.Outlined.MonitorHeart, contentDescription = null) },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 3 }
                )
                Tab(
                    text = { Text(stringResource(R.string.timing_belt)) },
                    icon = { Icon(Icons.Outlined.Cable, contentDescription = null) },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 4 }
                )
            }

            when (selectedTabIndex) {
                0 -> {
                    ServiceDetails(car = car)
                }

                1 -> {
                    TiresDetails(car = car)
                }

                2 -> {
                    KTEODetails(car = car)
                }

                3 -> {
                    InjectorDetails(car = car)
                }

                4 -> {
                    TimingBeltDetails(car)
                }
            }

            Spacer(modifier = Modifier.padding(paddingLarge))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { openDeleteDialog.value = true },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete_car),
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(id = R.string.delete_car))
                }

                FilledTonalButton(
                    onClick = { openDialog = true },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = stringResource(id = R.string.edit_car),
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(id = R.string.edit_car))
                }
            }
        }
    }

    UpdateCarDialog(
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        car = car
    )

    if (openDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDeleteDialog.value = false
            },
            title = {
                Text(text = stringResource(id = R.string.delete_car_dialog_title))
            },
            text = {
                Text(
                    stringResource(
                        id = R.string.delete_car_dialog_description,
                        car.name
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDeleteDialog.value = false
                        viewModel.deleteCar(car)
                        Toast.makeText(
                            context, R.string.delete_toast,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    Text(stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDeleteDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.dismiss))
                }
            }
        )
    }
}

@Composable
fun KteoStatusIcon(car: Car) {
    val kteo = car.kteo.date.toLocalDate()
    val exhaust = car.kteo.exhaustCard.toLocalDate()
    val now = LocalDate.now()
    if (kteo.isBefore(now) || exhaust.isBefore(now)
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = Color.Red
        )
    } else if (kteo.minusWeeks(1).isBefore(now) || exhaust.minusWeeks(1).isBefore(now)) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = Color.Yellow
        )
    } else {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color.Green
        )
    }
}