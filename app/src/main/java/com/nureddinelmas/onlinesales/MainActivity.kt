package com.nureddinelmas.onlinesales


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.nureddinelmas.onlinesales.helper.OrderViewModelFactory
import com.nureddinelmas.onlinesales.ui.theme.OnlineSalesTheme
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.widgets.MainScreen


	class MainActivity : ComponentActivity() {
		private val repository by lazy { OrderRepositoryImpl(FirebaseFirestore.getInstance()) }
		private val viewModelFactory by lazy { OrderViewModelFactory(repository) }
		private val viewModel: OrderViewModel by viewModels { viewModelFactory }
		
		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			enableEdgeToEdge()
			setContent {
				OnlineSalesTheme {
					MainScreen(viewModel)
				}
			}
		}
	}

