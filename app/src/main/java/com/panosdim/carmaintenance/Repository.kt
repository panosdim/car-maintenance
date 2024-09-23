package com.panosdim.carmaintenance

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.panosdim.carmaintenance.model.Car
import com.panosdim.carmaintenance.model.Response
import com.panosdim.carmaintenance.utils.toFormattedString
import com.panosdim.carmaintenance.utils.toLocalDate
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class Repository {
    private val user = Firebase.auth.currentUser
    private val database = Firebase.database
    private var listener: ValueEventListener? = null
    private var carsRef: DatabaseReference? = null

    fun getCars(): Flow<Response<List<Car>>> {
        return callbackFlow {
            carsRef = user?.let { database.getReference("cars").child(it.uid) }

            listener = carsRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(Response.Loading)
                    val cars = mutableListOf<Car>()
                    snapshot.children.forEach { car ->
                        val item = car.getValue(Car::class.java)
                        if (item != null) {
                            item.id = car.key
                            cars.add(item)
                        }
                    }
                    // Emit the user data to the flow
                    trySend(Response.Success(cars))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Response.Error(error.message))
                    cancel()
                }

            })

            awaitClose {
                listener?.let {
                    carsRef?.removeEventListener(it)
                }
            }
        }
    }

    fun addNewCar(car: Car) {
        car.kteo.date = car.purchaseDate.toLocalDate().plusYears(4).toFormattedString()
        car.kteo.exhaustCard = car.purchaseDate.toLocalDate().plusYears(4).toFormattedString()
        val carsRef = user?.let { database.getReference("cars").child(it.uid) }

        carsRef?.push()?.setValue(car)
    }

    fun updateCar(car: Car) {
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

    fun deleteCar(car: Car) {
        val carsRef = user?.let {
            car.id?.let { id ->
                database.getReference("cars").child(it.uid).child(
                    id
                )
            }
        }

        carsRef?.removeValue()
    }

    fun signOut() {
        listener?.let {
            carsRef?.removeEventListener(it)
        }

    }
}
