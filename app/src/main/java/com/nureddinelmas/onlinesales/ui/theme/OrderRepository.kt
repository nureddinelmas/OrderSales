package com.nureddinelmas.onlinesales.ui.theme

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nureddinelmas.onlinesales.models.Order
import kotlinx.coroutines.tasks.await

class OrderRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    suspend fun getOrders(): Result<List<Order>> = runCatching {
        db.collection("orders")
            .get()
            .await()
            .documents
            .map { it.toObject(Order::class.java)!! }
    }

    fun addOrder(order: Order): Result<Unit> = runCatching {
        db.collection("orders")
            .add(order)
            .addOnSuccessListener { documentReference ->
                Log.d("!!!", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("!!!", "Error adding document", e)
            }
    }

}