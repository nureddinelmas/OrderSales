package com.nureddinelmas.onlinesales.widgets

import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: OrderViewModel) {
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val navController = rememberNavController()
	val scope = rememberCoroutineScope()
	ModalNavigationDrawer(
		drawerContent = { DrawerContent(navController, drawerState) },
		drawerState = drawerState
	) {
		Scaffold(
			topBar = {
				TopAppBar(
					title = { Text("Order List") },
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
				startDestination = "list",
				modifier = Modifier.padding(paddingValues)
			) {
				composable("list") {
					OrderListScreen(viewModel, paddingValues)
				}
				composable("add") {
					// AddOrderScreen()
				}
			}
		}
	}
	
}