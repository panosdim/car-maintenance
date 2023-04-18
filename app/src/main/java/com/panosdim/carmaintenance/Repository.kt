package com.panosdim.carmaintenance

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.panosdim.carmaintenance.model.Car
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class Repository {
    fun getCars(): Flow<List<Car>> {
        return callbackFlow {
            val carsRef = user?.let { database.getReference("cars").child(it.uid) }

            val listener = carsRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cars = mutableListOf<Car>()
                    snapshot.children.forEach { car ->
                        val item = car.getValue(Car::class.java)
                        if (item != null) {
                            item.id = car.key
                            cars.add(item)
                        }
                    }
                    // Emit the user data to the flow
                    trySend(cars)
                }

                override fun onCancelled(error: DatabaseError) {
                    cancel()
                }

            })

            awaitClose {
                if (listener != null) {
                    carsRef.removeEventListener(listener)
                }
            }
        }
    }

    fun addNewCar(car: Car) {
        val carsRef = user?.let { database.getReference("cars").child(it.uid) }

        carsRef?.push()?.setValue(car)
    }

    fun updateCarService(car: Car) {
        val carsRef = user?.let {
            car.id?.let { id ->
                database.getReference("cars").child(it.uid).child(
                    id
                )
            }
        }

        carsRef?.setValue(car)
        carsRef?.child("id")?.removeValue()
    }
}