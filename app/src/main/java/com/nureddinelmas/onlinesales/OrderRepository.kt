package com.nureddinelmas.onlinesales

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nureddinelmas.onlinesales.models.Order
import kotlinx.coroutines.tasks.await

interface OrderRepository {
	suspend fun getOrders(): Result<List<Order>>
	suspend fun addOrder(order: Order): Result<Unit>
	suspend fun deleteOrder(orderId: String): Result<Unit>
}

class OrderRepositoryImpl(private val db: FirebaseFirestore) : OrderRepository {
	override suspend fun getOrders(): Result<List<Order>> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
			.get()
			.await()
			.documents
			.mapNotNull { it.toObject(Order::class.java)?.copy(orderId = it.id) }
	}
	
	override suspend fun addOrder(order: Order): Result<Unit> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
			.add(order)
			.addOnSuccessListener { documentReference ->
				Log.d("!!!", "DocumentSnapshot added with ID: ${documentReference.id}")
				db.collection("Orders").document(documentReference.id)
					.update("orderId", documentReference.id)
			}
			.addOnFailureListener { e ->
				Log.w("!!!", "Error adding document", e)
			}
	}
	
	override suspend fun deleteOrder(orderId: String): Result<Unit> =
		runCatching {
			val orderRef = db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
				.document(orderId)
			orderRef.delete()
		}
	
	
}