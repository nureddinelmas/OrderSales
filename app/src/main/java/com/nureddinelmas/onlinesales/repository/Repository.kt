package com.nureddinelmas.onlinesales.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nureddinelmas.onlinesales.CONSTANTS_FIREBASE_COLLECTION_CUSTOMERS
import com.nureddinelmas.onlinesales.CONSTANTS_FIREBASE_COLLECTION_ORDERS
import com.nureddinelmas.onlinesales.CONSTANTS_FIREBASE_COLLECTION_PRODUCTS
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import kotlinx.coroutines.tasks.await

interface Repository {
	suspend fun getOrders(): Result<List<Order>>
	suspend fun getProducts(): Result<List<Product>>
	suspend fun getCustomers(): Result<List<Customer>>
	suspend fun addOrder(order: Order): Result<Unit>
	suspend fun addProduct(product: Product): Result<Unit>
	suspend fun addCustomer(customer: Customer): Result<Unit>
	suspend fun deleteOrder(orderId: String): Result<Unit>
	suspend fun deleteProduct(productId: String): Result<Unit>
	suspend fun updateOrder(order: Order): Result<Unit>

}

class RepositoryImpl(private val db: FirebaseFirestore) : Repository {


	override suspend fun getOrders(): Result<List<Order>> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
			.get()
			.await()
			.documents
			.mapNotNull { it.toObject(Order::class.java)?.copy(orderId = it.id) }
			.sortedByDescending { it.orderDate }
	}

	override suspend fun getProducts(): Result<List<Product>> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_PRODUCTS)
			.get()
			.await()
			.documents
			.mapNotNull { it.toObject(Product::class.java)?.copy(productId = it.id) }
			.sortedByDescending { it.productId }
	}
	
	override suspend fun getCustomers(): Result<List<Customer>> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_CUSTOMERS)
			.get()
			.await()
			.documents
			.mapNotNull { it.toObject(Customer::class.java)?.copy(customerId = it.id) }
			.sortedByDescending { it.customerId }
	}
	
	override suspend fun addOrder(order: Order): Result<Unit> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
			.add(order)
			.addOnSuccessListener { documentReference ->
				Log.d("!!!", "DocumentSnapshot added with ID: ${documentReference.id}")
				db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS).document(documentReference.id)
					.update("orderId", documentReference.id)
			}
			.addOnFailureListener { e ->
				Log.w("!!!", "Error adding document", e)
			}
	}

	override suspend fun addProduct(product: Product): Result<Unit> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_PRODUCTS)
			.add(product)
			.addOnSuccessListener { documentReference ->
				Log.d("!!!", "DocumentSnapshot added with ID: ${documentReference.id}")
				db.collection(CONSTANTS_FIREBASE_COLLECTION_PRODUCTS).document(documentReference.id)
					.update("productId", documentReference.id)
			}
			.addOnFailureListener { e ->
				Log.w("!!!", "Error adding document", e)
			}
	}
	
	override suspend fun addCustomer(customer: Customer): Result<Unit> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_CUSTOMERS)
			.add(customer)
			.addOnSuccessListener { documentReference ->
				Log.d("!!!", "DocumentSnapshot added with ID: ${documentReference.id}")
				db.collection(CONSTANTS_FIREBASE_COLLECTION_CUSTOMERS).document(documentReference.id)
					.update("customer Id", documentReference.id)
			}
			.addOnFailureListener { e ->
				Log.w("!!!", "Error adding document", e)
			}
	}
	
	override suspend fun updateOrder(order: Order): Result<Unit> = runCatching {
		db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
			.document(order.orderId!!)
			.set(order)
			.addOnSuccessListener { Log.d("!!!", "DocumentSnapshot added with ID: ${order.orderId}") }
			.addOnFailureListener { e -> Log.w("!!!", "Error adding document", e) }
	}
	override suspend fun deleteOrder(orderId: String): Result<Unit> =
		runCatching {
			val orderRef = db.collection(CONSTANTS_FIREBASE_COLLECTION_ORDERS)
				.document(orderId)
			orderRef.delete()
		}

	override suspend fun deleteProduct(productId: String): Result<Unit> =
		runCatching {
			val orderRef = db.collection(CONSTANTS_FIREBASE_COLLECTION_PRODUCTS)
				.document(productId)
			orderRef.delete()
		}





}