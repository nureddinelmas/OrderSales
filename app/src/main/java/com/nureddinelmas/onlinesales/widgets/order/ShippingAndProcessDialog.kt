package com.nureddinelmas.onlinesales.widgets.order

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.OrderProcess
import com.nureddinelmas.onlinesales.models.getOrderProcessColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAndProcessDialog(
	order: Order,
	onDismiss: () -> Unit,
	onSave: (Double, OrderProcess) -> Unit,
) {
	var shippingCost by remember { mutableDoubleStateOf(order.shipping) }
	var expanded by remember { mutableStateOf(false) }
	var selectedOption by remember { mutableStateOf(order.process) }
	
	// For managing the text field input
	val textFieldValue = remember(selectedOption) { selectedOption?.displayName }
	Dialog(onDismissRequest = onDismiss) {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.wrapContentHeight(),
			shape = MaterialTheme.shapes.extraLarge,
		) {
			Column {
				OutlinedTextField(
					value = if (shippingCost == 0.0) "" else shippingCost.toString().replace('.', ','),
					onValueChange = { newValue ->
						if (newValue.isBlank()) {
							shippingCost = 0.0
							return@OutlinedTextField
						}
						val normalized = newValue.replace(',', '.')
						val parsed = normalized.toDoubleOrNull()
						
						if (parsed != null) {
							shippingCost = parsed
						}
					},
					label = { Text("Shipping Cost (kr)") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
					modifier = Modifier.padding(16.dp)
				)
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp)
				) {
					// Text Field with Dropdown
					ExposedDropdownMenuBox(
						expanded = expanded,
						onExpandedChange = { expanded = !expanded }
					) {
						OutlinedTextField(
							value = textFieldValue ?: "",
							onValueChange = {},
							readOnly = true,
							label = { Text("Update process") },
							trailingIcon = {
								ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
							},
							modifier = Modifier
								.menuAnchor() // Align the dropdown menu with the text field
								.fillMaxWidth()
						)
						
						// Dropdown Menu
						DropdownMenu(
							expanded = expanded,
							onDismissRequest = { expanded = false }
						) {
							OrderProcess.entries.forEach { option ->
								DropdownMenuItem(
									text = {
										Text(
											option.displayName.uppercase(), style = TextStyle(
												color = Color(
													getOrderProcessColor(option)
												)
											)
										)
									},
									onClick = {
										selectedOption = option
										expanded = false
									}
								)
							}
						}
					}
				}
				Button(
					onClick = {
						onSave(shippingCost, selectedOption!!)
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