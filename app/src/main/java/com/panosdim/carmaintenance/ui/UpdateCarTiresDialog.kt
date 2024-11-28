package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Tires
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarTiresDialog(
    bottomSheetState: SheetState,
    car: Car,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()

    if (bottomSheetState.isVisible) {
        var carTires by rememberSaveable {
            if (car.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(car.tires.odometer)
            }
        }
        var nextTiresChange by rememberSaveable {
            if (car.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(car.tires.nextChange)
            }
        }
        var nextTiresSwap by rememberSaveable {
            if (car.tires.odometer.isBlank()) {
                return@rememberSaveable mutableStateOf("")
            } else {
                return@rememberSaveable mutableStateOf(car.tires.nextSwap)
            }
        }
        var swapPerformed by rememberSaveable {
            return@rememberSaveable mutableStateOf(car.tires.swapPerformed)
        }
        val date = remember {
            derivedStateOf {
                if (car.tires.date.isBlank()) {
                    return@derivedStateOf LocalDate.now()
                } else {
                    return@derivedStateOf car.tires.date.toLocalDate()
                }
            }
        }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.value.toEpochMilli())


        fun isFormValid(): Boolean {
            return carTires.isNotBlank() && nextTiresChange.isNotBlank() && nextTiresSwap.isNotBlank()
        }

        ModalBottomSheet(
            onDismissRequest = { scope.launch { bottomSheetState.hide() } },
            sheetState = bottomSheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingLarge)
                    .imePadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(paddingSmall)
            ) {
                Text(
                    stringResource(id = R.string.car_tires_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedDatePicker(
                    state = datePickerState,
                    label = stringResource(R.string.car_tires_date),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = carTires,
                    onValueChange = {
                        carTires = it
                        try {
                            nextTiresChange = (it.toInt() + 40000).toString()
                            nextTiresSwap = (it.toInt() + 20000).toString()
                        } catch (_: NumberFormatException) {
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.car_tires_placeholder)
                        )
                    },
                    suffix = { Text(text = stringResource(R.string.km)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = carTires.isBlank(),
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.car_tires_change)) },
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nextTiresSwap,
                    onValueChange = {
                        nextTiresSwap = it
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.next_car_tires_swap_placeholder)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = nextTiresSwap.isBlank(),
                    singleLine = true,
                    suffix = { Text(text = stringResource(R.string.km)) },
                    label = { Text(text = stringResource(R.string.next_car_tires_swap)) },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    Text("Swap Performed")
                    Switch(
                        checked = swapPerformed,
                        onCheckedChange = {
                            swapPerformed = it
                        }
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nextTiresChange,
                    onValueChange = {
                        nextTiresChange = it
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.next_car_tires_next_placeholder)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = nextTiresChange.isBlank(),
                    singleLine = true,
                    suffix = { Text(text = stringResource(R.string.km)) },
                    label = { Text(text = stringResource(R.string.next_car_tires)) },
                )

                Row(
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        enabled = isFormValid(),
                        onClick = {
                            val newTires = datePickerState.selectedDateMillis?.toLocalDate()?.let {
                                Tires(
                                    date = it.toFormattedString(),
                                    odometer = carTires,
                                    nextChange = nextTiresChange,
                                    nextSwap = nextTiresSwap,
                                    swapPerformed = swapPerformed
                                )

                            }

                            if (newTires != null) {
                                car.tires = newTires

                                viewModel.updateCar(car)

                                Toast.makeText(
                                    context, R.string.car_tires_message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            scope.launch { bottomSheetState.hide() }
                        },
                    ) {
                        Icon(
                            Icons.Filled.Save,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(id = R.string.update))
                    }
                }
            }
        }
    }
}