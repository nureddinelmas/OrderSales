package com.nureddinelmas.onlinesales.print

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import com.google.type.DateTime
import com.nureddinelmas.onlinesales.R
import com.nureddinelmas.onlinesales.helper.toSekFormat
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Order
import com.nureddinelmas.onlinesales.models.Product
import com.nureddinelmas.onlinesales.viewModel.OrderUiState
import com.nureddinelmas.onlinesales.viewModel.ProductViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*




fun createAndShareAllProductsList(context: Context, productViewModel: ProductViewModel) {
	val products = productViewModel.getOnlyHavePriceProducts()
	try {
		// Create a new PDF document
		val pdfDocument = android.graphics.pdf.PdfDocument()
		
		// Define the page size
		var pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(330, 600, 1).create()
		
		var page = pdfDocument.startPage(pageInfo)
		
		// Access the canvas to draw the content
		var canvas: Canvas = page.canvas
		val paint = Paint()
		paint.textSize = 12f
		// Draw a custom logo at the top of the document
		val logoBitmap = android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.logo) // Replace with your logo resource
		canvas.drawBitmap(logoBitmap.scale(300, 70), 0f, 10f, null)
		
		// Set the starting position
		var yPosition = 20f
		yPosition += 90f
		var index = 0
		var totalQuantity = 0.0
		// Draw order details
		paint.textSize = 16f
		paint.isFakeBoldText = true
		canvas.drawText("Güncel Ürün Fiyatları", 10f, yPosition, paint)
		paint.textSize = 12f
		paint.isFakeBoldText = false
		yPosition += 40f
		
		
		paint.textSize = 13f
		paint.isFakeBoldText = true
		canvas.drawText("     Ürün Adı", 10f, yPosition, paint)
		canvas.drawText("Fiyatı", 260f, yPosition, paint)
		yPosition += 20f
		
		
		paint.textSize = 12f
		paint.isFakeBoldText = false
		products.forEach { product ->
				canvas.drawText(
					"${index + 1}. ${product.productName.take(40)}${if (product.productName.length > 40) "..." else ""}",
					10f,
					yPosition,
					paint
				)
				canvas.drawText(
					"${product.productPrice.toDouble().toSekFormat(product.productCurrency)} / ${product.defineSortOfQuantity()}",
					230f,
					yPosition,
					paint
				)
				
			
				yPosition += 20f
				if (yPosition > 580f) {
					pdfDocument.finishPage(page)
					pageInfo =
						android.graphics.pdf.PdfDocument.PageInfo.Builder(330, 600, index + 2)
							.create()
					page = pdfDocument.startPage(pageInfo)
					canvas = page.canvas
					yPosition = 20f
				}
				index++
				totalQuantity += product.productQuantity
			
			
		}
		
		paint.textSize = 10f
		paint.isFakeBoldText = true
		paint.letterSpacing = 0.1f
		yPosition += 10f
		canvas.drawText("Ürünlerimiz ${SimpleDateFormat("d MMMM yyyy", Locale("tr", "TR")).format(Date().time)} tarihli güncel fiyatlardır.", 10f, yPosition, paint)
		yPosition += 20f
		
		
		
		// Finish the page
		pdfDocument.finishPage(page)
		
		// Save the PDF file
		val fileName = "Urun:Listesi.pdf"
		val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
		val file = File(directory, fileName)
		FileOutputStream(file).use { pdfDocument.writeTo(it) }
		
		// Close the document
		pdfDocument.close()
		
		// Share the PDF via WhatsApp
		sharePdf(context, file)
		
		println("PDF saved to: ${file.absolutePath}")
	} catch (e: Exception) {
		e.printStackTrace()
	}
}