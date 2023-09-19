package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panosdim.carmaintenance.MainViewModel
import com.panosdim.carmaintenance.R
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge

@Composable
fun CarDetails(selectedCar: Car?) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val openDeleteDialog = remember { mutableStateOf(false) }

    selectedCar?.let {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = it.name,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.padding(paddingLarge))
            ServiceCard(it)
            Spacer(Modifier.padding(paddingLarge))
            TiresCard(it)
            Spacer(Modifier.padding(paddingLarge))
            KTEOCard(it)
            Spacer(Modifier.padding(paddingLarge))
            InjectorCard(it)
            Spacer(Modifier.padding(paddingLarge))
            if (it.timingBelt != null) {
                TimingBeltCard(it)
                Spacer(Modifier.padding(paddingLarge))
            }
            FilledTonalButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
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
        }

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
                        stringResource(id = R.string.delete_car_dialog_description)
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDeleteDialog.value = false
                            viewModel.deleteCar(it)
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
}