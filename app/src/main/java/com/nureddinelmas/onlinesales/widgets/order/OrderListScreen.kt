package com.nureddinelmas.onlinesales.widgets.order

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nureddinelmas.onlinesales.helper.toSekFormat
import com.nureddinelmas.onlinesales.helper.totalQuantity
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.getOrderProcessColor
import com.nureddinelmas.onlinesales.print.createAndShareAllOrders
import com.nureddinelmas.onlinesales.viewModel.CustomerViewModel
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun OrderListScreen(
	orderViewModel: OrderViewModel,
	navController: NavController
) {
	val uiState by orderViewModel.uiState.collectAsState()
	var showDialogDelete by remember { mutableStateOf(false) }
	var showDialogArchive by remember { mutableStateOf(false) }
	var currentOrder by remember { mutableStateOf(Order()) }
	val context = LocalContext.current
	
	when {
		uiState.isLoading -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				CircularProgressIndicator()
			}
			
		}
		
		(uiState.error != null) -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "Error: ${uiState.error}")
			}
		}
		
		
		uiState.orders.isNotEmpty() -> {
			Column {
				PullToRefreshBox(
					isRefreshing = uiState.isRefreshState,
					onRefresh = { orderViewModel.refreshOrders() },
					state = PullToRefreshState(),
					modifier = Modifier.weight(16f)
				) {
				LazyColumn(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color(0xFFF0F0F0))
						
				) {
					items(
						items = orderViewModel.onlyNotArchivedOrders(),
						key = { it.orderId!! }
					) { order ->
						var offsetX by remember { mutableFloatStateOf(0f) }
						val maxOffset = 900f
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.offset { IntOffset(offsetX.toInt(), 0) }
								.background(if (offsetX < -100f) Color.Red else if (offsetX > 100f) Color.Green else Color.White)
								.shadow(4.dp)
								.pointerInput(Unit) {
									detectHorizontalDragGestures(
										onDragEnd = {
											if (offsetX < -maxOffset / 2) {
												currentOrder = order
												showDialogDelete = true
											} else if (offsetX > maxOffset / 2) {
												currentOrder = order
												showDialogArchive = true
											}
											offsetX = 0f
										},
										onHorizontalDrag = { _, dragAmount ->
											offsetX = (offsetX + dragAmount).coerceIn(
												-maxOffset,
												maxOffset
											)
										},
									)
								}
						) {
							if (order.totalQuantity() != 0.0) OrderItem(
								order,
								onUpdateClick = {
									val gson = Gson()
									val orderJson = gson.toJson(order)
									val encodedOrderJson = java.net.URLEncoder.encode(
										orderJson,
										StandardCharsets.UTF_8.toString()
									)
									navController.navigate("update/${encodedOrderJson}")
								},
								onClick = {
									val gson = Gson()
									val orderJson = gson.toJson(order)
									val encodedOrderJson = java.net.URLEncoder.encode(
										orderJson,
										StandardCharsets.UTF_8.toString()
									)
									navController.navigate("details/${encodedOrderJson}")
									
								},
								customerName = order.customer?.customerName
									?: ""
							)
						}
					}
				}
				}
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color(0xFFF0F0F0))
						.weight(1f)
						.padding(horizontal = 4.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					val orderText  = if(orderViewModel.onlyNotArchivedOrders().size >1 ) "orders /" else "order /"
					Text(
						text = "Total : ${orderViewModel.onlyNotArchivedOrders().size} $orderText " + "${
							totalQuantity(
								uiState.orders
							)
						} kg / " + orderViewModel.getTotalPrice()
							.toSekFormat(uiState.orders[0].productList[0].productCurrency.uppercase()),
						modifier = Modifier
							.padding(horizontal = 12.dp, vertical = 4.dp)
							.weight(12f),
					)
					IconButton(
						modifier = Modifier
							.weight(1f)
							.padding(end = 10.dp), onClick = {
							createAndShareAllOrders(
								context = context,
								orders = orderViewModel.onlyNotArchivedOrders()
							)
						}) {
						Icon(
							imageVector = Icons.Default.Share,
							contentDescription = "Share Order",
							modifier = Modifier.size(20.dp)
						)
					}
				}
				
			}
			
		}
		
		uiState.orders.isEmpty() -> {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(text = "No Orders Found", style = TextStyle(fontSize = 20.sp))
			}
		}
	}
	
	if (showDialogDelete) {
		AlertDialogCustom(
			onAlertDialog = { showDialogDelete = false },
			orderViewModel = orderViewModel,
			currentOrder = currentOrder,
			title = "Delete Order",
			body = "Are you sure you want to delete this order?",
			onYesButtonClick = { orderViewModel.deleteOrder(currentOrder.orderId!!) }
		)
	}
	
	if (showDialogArchive) {
		AlertDialogCustom(
			onAlertDialog = { showDialogArchive = false },
			orderViewModel = orderViewModel,
			currentOrder = currentOrder,
			title = "Archive Order",
			body = "Are you sure you want to archive this order?",
			onYesButtonClick = { orderViewModel.updateOrder(currentOrder.copy(isArchived = true)) }
		)
	}
}

@SuppressLint("DefaultLocale")
@Composable
fun OrderItem(order: Order, onClick: () -> Unit, onUpdateClick: () -> Unit, customerName: String) {
	
	Card(
		modifier = Modifier
			.padding(8.dp)
			.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(4.dp)
	) {
		Column(
			modifier = Modifier
				.clickable { onClick() }
				.padding(8.dp),
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 2.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "Customer Name",
					modifier = Modifier
						.padding(vertical = 1.dp)
						.weight(1.5f),
					textAlign = TextAlign.Start
				)
				Text(
					text = "Quantity",
					modifier = Modifier
						.padding(vertical = 1.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
				Text(
					text = "Price",
					modifier = Modifier
						.padding(vertical = 1.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
			}
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = 2.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = customerName.uppercase(),
					modifier = Modifier
						.padding(vertical = 1.dp)
						.weight(1.5f),
					textAlign = TextAlign.Start
				)
				
				Text(
					text = "${order.totalQuantity()} kg",
					modifier = Modifier
						.padding(vertical = 1.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
				
				Text(
					text = order.totalPrice()
						.toSekFormat(order.productList[0].productCurrency.uppercase()),
					modifier = Modifier
						.padding(vertical = 1.dp)
						.weight(1f),
					textAlign = TextAlign.Center
				)
			}
			Box(
				modifier = Modifier
					.padding(4.dp)
					.fillMaxWidth(),
				contentAlignment = Alignment.BottomEnd
			) {
				Row {
					Text(
						text = "Process : ",
						style = TextStyle(
							fontSize = 18.sp,
							fontWeight = FontWeight.Normal,
							letterSpacing = 3.sp
						),
						textAlign = TextAlign.End
					)
					Text(
						text = order.process.toString().uppercase(),
						style = TextStyle(
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Color.White,
							background = Color(getOrderProcessColor(order.process!!)),
							letterSpacing = 1.sp
						),
						textAlign = TextAlign.Start,
					)
				}
			}
			
		}
	}
}

@Composable
fun AlertDialogCustom(
	onAlertDialog: () -> Unit,
	orderViewModel: OrderViewModel,
	currentOrder: Order,
	title: String,
	body: String,
	onYesButtonClick: () -> Unit
) {
	AlertDialog(
		onDismissRequest = { onAlertDialog() },
		title = { Text(text = title) },
		text = { Text(body) },
		confirmButton = {
			Button(
				onClick = {
					onYesButtonClick()
					onAlertDialog()
				},
				modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp)
			) {
				Text("Yes")
			}
		},
		dismissButton = {
			Button(
				onClick = { onAlertDialog() },
				modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
			) {
				Text("No")
			}
		}
	)
}