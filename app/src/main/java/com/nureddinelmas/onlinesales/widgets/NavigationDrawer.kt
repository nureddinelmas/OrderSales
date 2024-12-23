package com.nureddinelmas.onlinesales.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawer(modifier: Modifier) {
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()
	ModalNavigationDrawer(
		modifier = modifier,
		drawerState = drawerState,
		drawerContent = { ModalDrawerSheet {
			Column(
				modifier = Modifier.padding(horizontal = 16.dp)
					.verticalScroll(rememberScrollState())
			) {
				Spacer(Modifier.height(12.dp))
				Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
				HorizontalDivider()
				
				Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
				NavigationDrawerItem(
					label = { Text("Item 1") },
					selected = false,
					onClick = { /* Handle click */ }
				)
				NavigationDrawerItem(
					label = { Text("Item 2") },
					selected = false,
					onClick = { /* Handle click */ }
				)
				
				HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
				
				Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
				NavigationDrawerItem(
					label = { Text("Settings") },
					selected = false,
					icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
					badge = { Text("20") }, // Placeholder
					onClick = { /* Handle click */ }
				)
				NavigationDrawerItem(
					label = { Text("Help and feedback") },
					selected = false,
					icon = { Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = null) },
					onClick = { /* Handle click */ },
				)
				Spacer(Modifier.height(12.dp))
			}
		} }
	) {
		Scaffold(
//			floatingActionButton = {
//				ExtendedFloatingActionButton(
//					text = { Text("") },
//					icon = { Icon(Icons.Filled.Add, contentDescription = "") },
//					onClick = {
//						scope.launch {
//							drawerState.apply {
//								if (isOpen) close() else open()
//							}
//						}
//					}
//				)
//			}
		) { contentPadding ->
			IconButton(
				modifier = Modifier.padding(contentPadding),
				onClick = {
					scope.launch {
						drawerState.apply {
							if (isOpen) close() else open()
						}
					}
				}
			) {
				Icon(imageVector = Icons.Default.Home,
					contentDescription = "Ekle",
					modifier = Modifier.size(40.dp))
			}
		}
	}
}


@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview() {
	NavigationDrawer(modifier = Modifier)
}
