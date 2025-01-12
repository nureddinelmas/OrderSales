package com.nureddinelmas.onlinesales.widgets.order

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.viewModel.OrderViewModel


@Composable
fun ShippingDialog(
	order: Order,
	onDismiss: () -> Unit,
	onSave: (Double) -> Unit,
) {
	var shippingCost by remember { mutableDoubleStateOf(order.shipping) }
	Dialog(onDismissRequest = onDismiss) {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight(),
			shape = MaterialTheme.shapes.extraLarge,
		) {
			Column {
				OutlinedTextField(
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					value = shippingCost.toInt().toString(),
					onValueChange = { newValue -> shippingCost = newValue.toDoubleOrNull() ?: 0.0
					},
					label = { Text("Shipping Cost") },
					modifier = Modifier.padding(16.dp)
				)
				Button(
					onClick = {
						onSave(shippingCost)
						onDismiss()
					},
					modifier = Modifier
						.padding(16.dp)
						.fillMaxWidth()
				) {
					Text("Save")
				}
			}
		}
	}
	
	
}