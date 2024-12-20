package com.nureddinelmas.onlinesales


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.ui.theme.OnlineSalesTheme
import com.nureddinelmas.onlinesales.ui.theme.OrderRepository
import com.nureddinelmas.onlinesales.viewModel.OrderListViewModel

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore
    private val viewModel by viewModels<OrderListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnlineSalesTheme {
                val navController = rememberNavController()
                val orders = listOf<Order>()
                NavHost(navController = navController, startDestination = "list") {

                    composable("list") { OrdersListScreen(navController,viewModel ) }
                    composable("add") { /* Add Order UI */ }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(navController: NavHostController, viewModel: OrderListViewModel) {

val orders by produceState<List<Order>>(initialValue = emptyList()) {
        value = viewModel.orders.value
    }

Log.d("!!!", "Orders: $orders")
    val isLoading by remember { mutableStateOf(true) }
    val errorMessage by remember { mutableStateOf<String?>(null) }


    if (isLoading) {
        CircularProgressIndicator()
    } else if (errorMessage != null) {
        Text(text = "Error: $errorMessage")
    } else {
        orders.forEach { data ->
            Text(text = "Order Id: ${data.orderId}, Customer Name: ${data.customerId}")
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order List") },
                actions = {
                    IconButton(onClick = {
                       // navController.navigate("add")

                        viewModel.addOrder(Order(1, "Nureddin", 5, 2))

                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Order")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF0F0F0))
        ) {
            items(items = orders, key = { it.orderId }) { order ->
                SwipeToDismissItem(
                    order = order,
                    onDismiss = {
                        // Handle order dismissal
                        // Maybe delete from Firebase
                    }
                )
            }
        }
    }
}

@Composable
fun SwipeToDismissItem(order: Order, onDismiss: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDismiss()
                true
            } else {
                false
            }
        }
    )

//    SwipeToDismiss(
//        state = dismissState,
//        directions = setOf(SwipeToDismissBoxValue.EndToStart),
//        background = {
//            val color = if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
//                Color.Transparent
//            } else {
//                Color.Red
//            }
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(color)
//                    .padding(8.dp)
//            ) {
//                Text(
//                    text = "Delete",
//                    modifier = Modifier.align(Alignment.CenterEnd),
//                    color = Color.White,
//                    fontSize = 16.sp
//                )
//            }
//        },
//        dismissContent = {
//            Card(
//                modifier = Modifier
//                    .padding(vertical = 4.dp, horizontal = 8.dp)
//                    .fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                ListItem(
//                    headlineContent = { Text(order.customerId) },
//                    supportingContent = { Text("Quantity: ${order.orderQuantity}") }
//                )
//            }
//        }
//    )
}
