package com.panosdim.carmaintenance.ui

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarCard(car: Car, onClick: (Car) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()
    val skipPartiallyExpanded by remember { mutableStateOf(true) }

    val openDeleteDialog = remember { mutableStateOf(false) }
    val updateCarSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    ElevatedCard(
        modifier = Modifier
            .padding(paddingLarge)
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        onClick = { onClick(car) },
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(paddingLarge)) {
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
                    onClick = { scope.launch { updateCarSheetState.show() } },
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
            bottomSheetState = updateCarSheetState,
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
}