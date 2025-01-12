package com.nureddinelmas.onlinesales


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.nureddinelmas.onlinesales.helper.CustomerViewModelFactory
import com.nureddinelmas.onlinesales.helper.OrderViewModelFactory
import com.nureddinelmas.onlinesales.helper.ProductViewModelFactory
import com.nureddinelmas.onlinesales.repository.RepositoryImpl
import com.nureddinelmas.onlinesales.ui.theme.OnlineSalesTheme
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.MainScreen


class MainActivity : ComponentActivity() {
	private val repository by lazy { RepositoryImpl(FirebaseFirestore.getInstance()) }
	private val orderViewModelFactory by lazy { OrderViewModelFactory(repository) }
	private val productViewModelFactory by lazy { ProductViewModelFactory(repository) }
	private val orderViewModel: OrderViewModel by viewModels { orderViewModelFactory }
	private val productViewModel: ProductViewModel by viewModels { productViewModelFactory }
	private val customerViewModelFactory by lazy { CustomerViewModelFactory(repository) }
	private val customerViewModel: CustomerViewModel by viewModels { customerViewModelFactory }
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			OnlineSalesTheme {
				MainScreen(orderViewModel, productViewModel, customerViewModel = customerViewModel)
			}
		}
	}
}

