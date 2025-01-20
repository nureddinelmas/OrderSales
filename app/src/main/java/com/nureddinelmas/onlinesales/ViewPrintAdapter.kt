package com.nureddinelmas.onlinesales
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import android.util.Log
import android.view.View
import java.io.FileOutputStream
import android.view.ViewTreeObserver


class ViewPrintAdapter(private val context: Context, private val view: View) : PrintDocumentAdapter() {
	private var pdfDocument: PrintedPdfDocument? = null
	
	override fun onLayout(
		oldAttributes: PrintAttributes?,
		newAttributes: PrintAttributes?,
		cancellationSignal: CancellationSignal?,
		callback: LayoutResultCallback?,
		extras: android.os.Bundle?
	) {
		pdfDocument = PrintedPdfDocument(context, newAttributes!!)
		val info = PrintDocumentInfo.Builder("order_list.pdf")
			.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
			.setPageCount(1)
			.build()
		callback?.onLayoutFinished(info, true)
	}
	
	override fun onWrite(
		pages: Array<out PageRange>?,
		destination: ParcelFileDescriptor?,
		cancellationSignal: CancellationSignal?,
		callback: WriteResultCallback?
	) {
		view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				view.viewTreeObserver.removeOnGlobalLayoutListener(this)
				if (view.width > 0 && view.height > 0) {
					val page = pdfDocument!!.startPage(0)
					val content = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
					val canvas = Canvas(content)
					view.draw(canvas)
					page.canvas.drawBitmap(content, 0f, 0f, null)
					pdfDocument!!.finishPage(page)
					
					try {
						pdfDocument!!.writeTo(FileOutputStream(destination!!.fileDescriptor))
					} catch (e: Exception) {
						Log.e("Print", "Error writing PDF", e)
					} finally {
						pdfDocument!!.close()
						pdfDocument = null
					}
					callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
				} else {
					Log.e("Print", "View dimensions are invalid")
				}
			}
		})
	}
}