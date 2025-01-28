package com.panosdim.carmaintenance.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.paddingLarge
import com.panosdim.carmaintenance.paddingSmall

@Composable
fun CarDetails(car: Car) {
    val verticalScrollState = rememberScrollState()

    Column(
        Modifier
            .padding(paddingLarge)
            .fillMaxWidth()
    ) {
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
    }
}