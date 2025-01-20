package com.nureddinelmas.onlinesales.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
	private val auth: FirebaseAuth = FirebaseAuth.getInstance()
	
	fun signIn(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
		auth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					onSuccess()
					Log.d("!!!", "Sign in successful")
				} else {
					onError(task.exception?.message ?: "Sign in failed")
					Log.d("!!!", "Sign in failed")
				}
			}
	}
	
	fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					onSuccess()
				} else {
					onError(task.exception?.message ?: "Sign up failed")
				}
			}
	}
}