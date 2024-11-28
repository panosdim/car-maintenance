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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall
import com.panosdim.carmaintenance.utils.toEpochMilli
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCarDialog(
    bottomSheetState: SheetState,
    car: Car
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()

    if (bottomSheetState.isVisible) {
        var carName by rememberSaveable { mutableStateOf(car.name) }
        val focusRequester = FocusRequester()

        val date = remember { car.purchaseDate.toLocalDate() }
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = date.toEpochMilli())

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
                    stringResource(id = R.string.car_update_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    value = carName,
                    onValueChange = { carName = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.car_name_placeholder)
                        )
                    },
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.car_name)) },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxWidth()
                )
                OutlinedDatePicker(
                    modifier = Modifier.fillMaxWidth(),
                    state = datePickerState,
                    label = stringResource(R.string.purchase_date)
                )
                LaunchedEffect(Unit) {
                    coroutineContext.job.invokeOnCompletion {
                        focusRequester.requestFocus()
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        enabled = carName.isNotBlank(),
                        onClick = {
                            if (carName.isNotBlank()) {
                                car.name = carName
                                car.purchaseDate = datePickerState.selectedDateMillis?.toLocalDate()
                                    ?.toFormattedString() ?: LocalDate.now().toFormattedString()
                                viewModel.updateCar(car)

                                Toast.makeText(
                                    context, R.string.update_car_message,
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