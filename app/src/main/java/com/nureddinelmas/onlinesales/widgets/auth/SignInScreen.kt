package com.nureddinelmas.onlinesales.widgets.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_MAIN_SCREEN
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.viewModel.AuthViewModel

@Composable
fun SignInScreen(navController: NavController, viewModel: AuthViewModel) {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var isLoading by remember { mutableStateOf(false) }
	var errorMessage by remember { mutableStateOf<String?>(null) }
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text("Sign In", style = MaterialTheme.typography.bodyMedium)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		OutlinedTextField(
			value = email,
			onValueChange = { email = it },
			label = { Text("Email") },
			modifier = Modifier.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		OutlinedTextField(
			value = password,
			onValueChange = { password = it },
			label = { Text("Password") },
			modifier = Modifier.fillMaxWidth(),
			visualTransformation = PasswordVisualTransformation()
		)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Button(
			onClick = {
				isLoading = true
//				viewModel.signUp(email, password,
//					onSuccess = {
//						isLoading = false
//						navController.navigate(NAVIGATION_SCREEN_MAIN_SCREEN) {
//							popUpTo(NAVIGATION_SCREEN_ORDER_LIST) { inclusive = true }
//						}
//					},
//					onError = { error ->
//						isLoading = false
//						errorMessage = error
//					}
//				)
				viewModel.signIn(email, password,
					onSuccess = {
						isLoading = false
						navController.navigate(NAVIGATION_SCREEN_MAIN_SCREEN) {
							popUpTo(NAVIGATION_SCREEN_ORDER_LIST) { inclusive = true }
						}
					},
					onError = { error ->
						isLoading = false
						errorMessage = error
					}
				)
			},
			modifier = Modifier.fillMaxWidth()
		) {
			if (isLoading) {
				CircularProgressIndicator(modifier = Modifier.size(24.dp))
			} else {
				Text("Sign In")
			}
		}
		
		errorMessage?.let {
			Spacer(modifier = Modifier.height(8.dp))
			Text(it, color = MaterialTheme.colorScheme.error)
		}
	}
}