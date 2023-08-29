package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge

@Composable
fun CarDetails(selectedCar: Car?) {
    selectedCar?.let {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = selectedCar.name,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.padding(paddingLarge))
            ServiceCard(it)
            Spacer(Modifier.padding(paddingLarge))
            TiresCard(it)
            Spacer(Modifier.padding(paddingLarge))
            KTEOCard(it)
        }
    }
}