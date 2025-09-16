package com.nureddinelmas.onlinesales.widgets

import androidx.compose.foundation.layout.padding
import com.google.gson.Gson
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_UPDATE_ORDER
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import com.nureddinelmas.onlinesales.widgets.customer.AddNewCustomerScreen
import com.nureddinelmas.onlinesales.widgets.customer.CustomerListScreen
import com.nureddinelmas.onlinesales.widgets.order.AddOrderScreen
import com.nureddinelmas.onlinesales.widgets.order.OrderDetailsScreen
import com.nureddinelmas.onlinesales.widgets.order.OrderListScreen
import com.nureddinelmas.onlinesales.widgets.order.UpdateOrderScreen
import com.nureddinelmas.onlinesales.widgets.product.AddNewProductScreen
import com.nureddinelmas.onlinesales.widgets.product.ProductListScreen
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	orderViewModel: OrderViewModel,
	productViewModel: ProductViewModel,
	customerViewModel: CustomerViewModel,
) {
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val navController = rememberNavController()
	val scope = rememberCoroutineScope()
	val currentTitle = remember { mutableStateOf("Order List") }
	var shouldShowTopBar = remember { mutableStateOf(true) }
	ModalNavigationDrawer(
		drawerContent = { DrawerContent(navController, drawerState) },
		drawerState = drawerState
	) {
		Scaffold(
			topBar = {
				if (shouldShowTopBar.value) TopAppBar(
					title = { Text(currentTitle.value) },
					navigationIcon = {
						IconButton(onClick = {
							scope.launch {
								if (drawerState.isClosed) drawerState.open() else drawerState.close()
							}
						}) {
							Icon(Icons.Default.Menu, contentDescription = "Menu")
						}
					}
				)
			}
		) { paddingValues ->
			NavHost(
				navController = navController,
				startDestination = NAVIGATION_SCREEN_ORDER_LIST,
				modifier = Modifier.padding(paddingValues)
			) {
				composable(NAVIGATION_SCREEN_ORDER_LIST) {
					currentTitle.value = "Order List"
					OrderListScreen(orderViewModel, navController)
					shouldShowTopBar.value = true
				}
				composable("add") {
					currentTitle.value = "Add Order"
					AddOrderScreen(
						productViewModel,
						navController,
						orderViewModel,
						customerViewModel = customerViewModel
					)
				}
				composable("newProduct") {
					currentTitle.value = "Add new product"
					AddNewProductScreen(
						navController,
						product = Product(),
						false,
						productViewModel,
						productViewModel::addProduct
					)
				}
				composable("productList") {
					currentTitle.value = "Product List"
					ProductListScreen(productViewModel, navController)
				}
				
				composable("newCustomer") {
					currentTitle.value = "Add new customer"
					AddNewCustomerScreen(
						navController,
						Customer(),
						customerViewModel,
						false,
						orderViewModel = orderViewModel
					) {}
				}
				composable("customerList") {
					currentTitle.value = "Customer List"
					CustomerListScreen(customerViewModel, navController, orderViewModel)
				}
				
				composable(NAVIGATION_SCREEN_UPDATE_ORDER) { backStackEntry ->
					val orderJson = backStackEntry.arguments?.getString("order")
					val decodeOrderJson =
						URLDecoder.decode(orderJson, StandardCharsets.UTF_8.toString())
					val order = Gson().fromJson(decodeOrderJson, Order::class.java)
					currentTitle.value = "Update Order"
					UpdateOrderScreen(order) { }
				}
				composable("details/{order}") { backStackEntry ->
					val orderJson = backStackEntry.arguments?.getString("order")
					val decodeOrderJson =
						URLDecoder.decode(orderJson, StandardCharsets.UTF_8.toString())
					val order = Gson().fromJson(decodeOrderJson, Order::class.java)
					currentTitle.value = "Order Details"
					OrderDetailsScreen(
						order,
						productViewModel,
						orderViewModel,
						customerViewModel,
						navController
					)
					shouldShowTopBar.value = false
				}
			}
		}
	}
	
}