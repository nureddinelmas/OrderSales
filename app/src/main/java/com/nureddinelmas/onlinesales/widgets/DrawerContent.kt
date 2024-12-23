package com.nureddinelmas.onlinesales.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState) {
	val scope = rememberCoroutineScope()
	val topBarHeight = 64.dp
	
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
			.background(color = Color(0xFF77787A))
	) {
		Spacer(modifier = Modifier.height(topBarHeight))
		Text(
			text = "Navigation",
			style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
			modifier = Modifier.padding(8.dp)
		)
		
		Text(
			text = "Order List",
			modifier = Modifier
				.padding(8.dp)
				.clickable {
					scope.launch {
						drawerState.close()
						navController.navigate("list")
					}
				}
		)
		
		Text(
			text = "Add Order",
			modifier = Modifier
				.padding(8.dp)
				.clickable {
					scope.launch {
						drawerState.close()
						navController.navigate("add")
					}
				}
		)
	}
}