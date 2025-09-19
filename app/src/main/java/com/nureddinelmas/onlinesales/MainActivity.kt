package com.nureddinelmas.onlinesales


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.nureddinelmas.onlinesales.ui.theme.OnlineSalesTheme
import com.nureddinelmas.onlinesales.viewModel.AuthViewModel
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.MainScreen
import com.nureddinelmas.onlinesales.widgets.auth.SignInScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val orderViewModel: OrderViewModel by viewModels()
	private val productViewModel: ProductViewModel by viewModels()
	private val customerViewModel: CustomerViewModel by viewModels()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val firebaseAuth = FirebaseAuth.getInstance()
		enableEdgeToEdge()
		setContent {
			val navController = rememberNavController()
			
			OnlineSalesTheme {
				NavHost(
					navController = navController,
					startDestination = if(firebaseAuth.currentUser == null) NAVIGATION_SCREEN_SIGN_IN else NAVIGATION_SCREEN_MAIN_SCREEN
				) {
					composable(NAVIGATION_SCREEN_SIGN_IN) {
						SignInScreen(navController = navController, viewModel = AuthViewModel())
					}
					composable(NAVIGATION_SCREEN_MAIN_SCREEN) {
						MainScreen(
							orderViewModel,
							productViewModel,
							customerViewModel = customerViewModel
						)
					}
				}
			}
		}
	}
}

