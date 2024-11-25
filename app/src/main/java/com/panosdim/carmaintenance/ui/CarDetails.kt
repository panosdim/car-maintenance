package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall

@Composable
fun CarDetails(car: Car, onCarDeleted: () -> Unit) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val openDeleteDialog = remember { mutableStateOf(false) }
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val verticalScrollState = rememberScrollState()


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

        Column(
            modifier = Modifier
                .verticalScroll(verticalScrollState)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(paddingSmall)
        ) {
            ServiceDetails(car = car)
            TiresDetails(car = car)
            KTEODetails(car = car)
            InjectorDetails(car = car)
            TimingBeltDetails(car)
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
                        onCarDeleted()
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