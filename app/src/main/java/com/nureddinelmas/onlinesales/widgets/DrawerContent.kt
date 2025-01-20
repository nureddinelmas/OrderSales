package com.nureddinelmas.onlinesales.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_ORDER_LIST
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_SIGN_IN
import com.nureddinelmas.onlinesales.NAVIGATION_SCREEN_UPDATE_ORDER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState) {
	val scope = rememberCoroutineScope()
	val topBarHeight = 50.dp
	
	Column(
		modifier = Modifier
			.fillMaxHeight()
            .padding(16.dp)
            .background(color = Color(0xFFD3D5D9))
	) {
		Spacer(modifier = Modifier.height(topBarHeight))
		Text(
			text = "Menu",
			style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
			modifier = Modifier.padding(8.dp)
		)
		HorizontalDivider(modifier = Modifier.padding(8.dp), thickness = 2.dp)
		Text(
			"Lists",
			style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
			modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Order List",
			Icons.Default.Star,
			NAVIGATION_SCREEN_ORDER_LIST
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Product List",
			Icons.Default.Star,
			"productList"
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Customer List",
			Icons.Default.Star,
			"customerList"
		)
		HorizontalDivider(modifier = Modifier.padding(8.dp), thickness = 2.dp)
		Text(
			"Add new",
			style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
			modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Add new Order",
			Icons.Default.AddCircle,
			"add"
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Add new product",
			Icons.Default.AddCircle,
			"newProduct"
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Add new customer",
			Icons.Default.AddCircle,
			"newCustomer"
		)
		NavigationBarItem(
			scope,
			drawerState,
			navController,
			"Sign Out",
			Icons.Default.Close,
			NAVIGATION_SCREEN_SIGN_IN
		)
	}
}


@Composable
fun NavigationBarItem(
	scope: CoroutineScope,
	drawerState: DrawerState,
	navController: NavHostController,
	title: String,
	icon: ImageVector,
	navigatorState: String
) {
	NavigationDrawerItem(
		icon = {
			Icon(
				imageVector = icon,
				contentDescription = "Navigation Bar Item"
				, tint = Color.Black
			)
		},
		label = {
			Text(title, style = TextStyle(fontSize = 16.sp, color = Color.Black))
		},
		selected = false,
		onClick = {
			scope.launch {
				drawerState.close()
				navController.navigate(navigatorState)
			}
		}
	)
}

@Composable
fun ButtonWithIcon(
	scope: CoroutineScope,
	drawerState: DrawerState,
	navController: NavHostController,
	title: String,
	icon: ImageVector,
	navigatorState: String
) {
	Button(
		modifier = Modifier
            .padding(5.dp)
            .clickable {
                scope.launch {
                    drawerState.close()
                    navController.navigate(navigatorState)
                }
            },
		shape = RoundedCornerShape(1.dp),
		onClick = {
			scope.launch {
				drawerState.close()
				navController.navigate(navigatorState)
			}
		}
	) {
		Row(
			modifier = Modifier.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			Icon(
				imageVector = icon,
				contentDescription = title
			)
			Spacer(modifier = Modifier.width(30.dp))
			Text(text = title)
		}
		
	}
}