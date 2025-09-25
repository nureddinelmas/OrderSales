package com.nureddinelmas.onlinesales.print

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.nureddinelmas.onlinesales.R
import com.nureddinelmas.onlinesales.helper.toSekFormat
import com.nureddinelmas.onlinesales.models.Customer
import com.nureddinelmas.onlinesales.models.Order
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.graphics.scale

fun createAndShareOrderPdf(context: Context, order: Order, customer: Customer) {
	try {
		// Create a new PDF document
		val pdfDocument = android.graphics.pdf.PdfDocument()
		
		// Define the page size
		var pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(450, 600, 1).create()
		
		var page = pdfDocument.startPage(pageInfo)
		val orderDate =
			SimpleDateFormat("d MMMM yyyy", Locale("tr", "TR")).format(Date(order.orderDate))
		// Access the canvas to draw the content
		var canvas: Canvas = page.canvas
		val paint = Paint()
		paint.textSize = 12f
		// Draw a custom logo at the top of the document
		val logoBitmap = android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.logo) // Replace with your logo resource
		canvas.drawBitmap(logoBitmap.scale(400, 70), 10f, 10f, null)
		
		
		// Set the starting position
		var yPosition = 20f
		var index = 0
		// Draw order details
		
		yPosition += 90f
		paint.textSize = 16f
		paint.isFakeBoldText = true
		canvas.drawText("Sipariş Detayları", 10f, yPosition, paint)
		paint.textSize = 12f
		paint.isFakeBoldText = false
		yPosition += 40f
		canvas.drawText("Sipariş tarihi : $orderDate", 10f, yPosition, paint)
		yPosition += 20f
		
		canvas.drawText(
			"Müşteri Adı : ${customer.customerName?.uppercase()}",
			10f,
			yPosition,
			paint
		)
		yPosition += 40f
		
		paint.textSize = 13f
		paint.isFakeBoldText = true
		canvas.drawText("     Ürün Adı", 10f, yPosition, paint)
		canvas.drawText("Kilo", 200f, yPosition, paint)
		canvas.drawText("Kilo Fiyatı", 270f, yPosition, paint)
		canvas.drawText("Toplam Fiyatı", 360f, yPosition, paint)
		yPosition += 20f
		
		
		paint.textSize = 12f
		paint.isFakeBoldText = false
		order.productList.forEach { product ->
			
			canvas.drawText(
				"${index + 1}. ${product.productName.take(30)}${if (product.productName.length > 30) "..." else ""}",
				10f,
				yPosition,
				paint
			)
			canvas.drawText(
				"${product.productQuantity} kg",
				200f,
				yPosition,
				paint
			)
			
			canvas.drawText(
				(product.productPrice.toDouble().toSekFormat(product.productCurrency.uppercase())),
				270f,
				yPosition,
				paint
			)
			canvas.drawText(
				(product.totalPrice().toSekFormat(product.productCurrency.uppercase())),
				360f,
				yPosition,
				paint
			)
			yPosition += 20f
			if (yPosition > 580f) {
				pdfDocument.finishPage(page)
				pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(450, 600, index + 2).create()
				page = pdfDocument.startPage(pageInfo)
				canvas = page.canvas
				yPosition = 20f
			}
			index++
		}
		
		paint.textSize = 12f
		paint.isFakeBoldText = true
		paint.letterSpacing = 0.1f
		yPosition += 10f
		canvas.drawText("Kargo :  ${order.shipping}", 10f, yPosition, paint)
		yPosition += 20f
		canvas.drawText("Toplam kilo :  ${order.totalQuantity()} kg", 10f, yPosition, paint)
		yPosition += 20f
		
		canvas.drawText(
			"Toplam Fiyat :  ${
				order.totalPrice()
					.toSekFormat(order.productList[0].productCurrency.uppercase())
			}", 10f, yPosition, paint
		)
		
		
		// Finish the page
		pdfDocument.finishPage(page)
		
		// Save the PDF file
		val fileName = "${customer.customerName}_Siparis_Detaylari.pdf"
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

fun sharePdf(context: Context, file: File) {
	try {
		// Ensure the file exists before sharing
		if (file.exists()) {
			val pdfUri: Uri = FileProvider.getUriForFile(
				context,
				"com.nureddinelmas.onlinesales.provider", // Replace with your app's authority
				file // Use 'file' instead of 'pdfFile'
			)
			val shareIntent = Intent(Intent.ACTION_SEND).apply {
				type = "application/pdf"
				putExtra(Intent.EXTRA_STREAM, pdfUri)
				addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
			}
			context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
		} else {
			Log.e("PDF Error", "File does not exist: ${file.absolutePath}")
		}
	} catch (e: Exception) {
		e.printStackTrace()
	}
}