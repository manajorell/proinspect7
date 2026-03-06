package com.proinspect.app.pdf

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.proinspect.app.data.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import com.itextpdf.text.pdf.draw.LineSeparator

object PdfGenerator {

    private val cNavy    = BaseColor(26,  39,  68)
    private val cGold    = BaseColor(201, 151, 58)
    private val cRed     = BaseColor(180, 35,  35)
    private val cOrange  = BaseColor(200, 80,  12)
    private val cYellow  = BaseColor(160, 110, 0)
    private val cGreen   = BaseColor(22,  130, 60)
    private val cGray    = BaseColor(100, 110, 120)
    private val cLightBg = BaseColor(248, 245, 239)
    private val cBorder  = BaseColor(220, 215, 200)
    private val cWhite   = BaseColor.WHITE
    private val cOffWhite= BaseColor(252, 251, 248)

    private val fTitle    = Font(Font.FontFamily.HELVETICA, 26f, Font.BOLD,   cNavy)
    private val fH2       = Font(Font.FontFamily.HELVETICA, 15f, Font.BOLD,   cNavy)
    private val fH3       = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD,   cNavy)
    private val fH3White  = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD,   cWhite)
    private val fBody     = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(40, 45, 55))
    private val fSmall    = Font(Font.FontFamily.HELVETICA, 8f,  Font.NORMAL, cGray)
    private val fSmallB   = Font(Font.FontFamily.HELVETICA, 8f,  Font.BOLD,   cGray)
    private val fGold     = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD,   cGold)
    private val fWhite    = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD,   cWhite)
    private val fWhiteSm  = Font(Font.FontFamily.HELVETICA, 9f,  Font.NORMAL, cWhite)

    private fun rColor(r: Rating) = when(r) {
        Rating.SAFETY    -> cRed
        Rating.MAJOR     -> cOrange
        Rating.MONITOR   -> cYellow
        Rating.GOOD      -> cGreen
        Rating.NOT_RATED -> cGray
    }

    suspend fun generate(
        context: Context,
        report: Report,
        items: List<InspectionItem>,
        photos: List<InspectionPhoto>
    ): File {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val dir   = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file  = File(dir, "ProInspect_${stamp}.pdf")
        val doc   = Document(PageSize.LETTER, 50f, 50f, 55f, 55f)
        val writer = PdfWriter.getInstance(doc, FileOutputStream(file))
        writer.pageEvent = PageEvent(report)
        doc.open()
        pageCover(doc, report, items)
        pageExecutiveSummary(doc, report, items, photos)
        pageFullDetails(doc, report, items, photos)
        pageDisclaimer(doc, report)
        doc.close()
        return file
    }

    private fun pageCover(doc: Document, report: Report, items: List<InspectionItem>) {
        val hdr = PdfPTable(1).apply { widthPercentage = 100f }
        val hCell = PdfPCell().apply {
            backgroundColor = cNavy; border = Rectangle.NO_BORDER
            paddingTop = 40f; paddingBottom = 36f; paddingLeft = 30f; paddingRight = 30f
        }
        hCell.addElement(Paragraph("ProInspect", Font(Font.FontFamily.HELVETICA, 32f, Font.BOLD, cGold)))
        hCell.addElement(Paragraph("HOME INSPECTION REPORT", fWhiteSm))
        hCell.addElement(Paragraph(" "))
        hCell.addElement(Paragraph(report.propertyAddress.ifBlank { "Property Address" },
            Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, cWhite)))
        if (report.propertyCity.isNotBlank())
            hCell.addElement(Paragraph(report.propertyCity,
                Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, BaseColor(180,190,210))))
        hdr.addCell(hCell)
        doc.add(hdr)
        doc.add(Chunk(LineSeparator(3f, 100f, cGold, Element.ALIGN_CENTER, 0f)))

        val grid = PdfPTable(4).apply {
            widthPercentage = 100f; spacingBefore = 20f; spacingAfter = 24f
            setWidths(floatArrayOf(1f, 1.5f, 1f, 1.5f))
        }
        fun lbl(t: String) = PdfPCell(Phrase(t, fSmallB)).apply {
            border = Rectangle.NO_BORDER; paddingBottom = 2f; paddingTop = 8f }
        fun value(t: String) = PdfPCell(Phrase(t.ifBlank { "—" }, fH3)).apply {
            border = Rectangle.BOTTOM; borderColor = cBorder; paddingBottom = 6f; paddingTop = 0f }
        grid.addCell(lbl("CLIENT"));       grid.addCell(value(report.clientName))
        grid.addCell(lbl("DATE"));         grid.addCell(value(report.inspectionDate))
        grid.addCell(lbl("PROPERTY"));     grid.addCell(value(report.propertyAddress))
        grid.addCell(lbl("INSPECTOR"));    grid.addCell(value(report.inspectorName))
        grid.addCell(lbl("YEAR BUILT"));   grid.addCell(value(report.yearBuilt))
        grid.addCell(lbl("CERT #"));       grid.addCell(value(report.inspectorCert))
        grid.addCell(lbl("SQ FT"));        grid.addCell(value(report.squareFootage))
        grid.addCell(lbl("COMPANY"));      grid.addCell(value(report.inspectorCompany))
        doc.add(grid)

        doc.add(Chunk(LineSeparator(0.5f, 100f, cBorder, Element.ALIGN_CENTER, -2f)))
        val legend = PdfPTable(5).apply {
            widthPercentage = 80f; spacingBefore = 12f; spacingAfter = 8f
            horizontalAlignment = Element.ALIGN_CENTER
        }
        listOf(
            Triple("Safety Issue",  cRed,    items.count { it.rating == Rating.SAFETY }),
            Triple("Major Concern", cOrange, items.count { it.rating == Rating.MAJOR }),
            Triple("Monitor",       cYellow, items.count { it.rating == Rating.MONITOR }),
            Triple("Good",          cGreen,  items.count { it.rating == Rating.GOOD }),
            Triple("Not Rated",     cGray,   items.count { it.rating == Rating.NOT_RATED })
        ).forEach { (label, color, count) ->
            val cell = PdfPCell().apply {
                border = Rectangle.TOP; borderColorTop = color; borderWidthTop = 3f
                backgroundColor = cOffWhite; paddingAll = 10f
                horizontalAlignment = Element.ALIGN_CENTER
            }
            cell.addElement(Paragraph(count.toString(),
                Font(Font.FontFamily.HELVETICA, 22f, Font.BOLD, color)).apply { alignment = Element.ALIGN_CENTER })
            cell.addElement(Paragraph(label,
                Font(Font.FontFamily.HELVETICA, 7f, Font.NORMAL, cGray)).apply { alignment = Element.ALIGN_CENTER })
            legend.addCell(cell)
        }
        doc.add(legend)
        doc.newPage()
    }

    private fun pageExecutiveSummary(
        doc: Document, report: Report,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        val titleTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
        val tc = PdfPCell().apply { backgroundColor = cNavy; border = Rectangle.NO_BORDER; paddingAll = 14f }
        tc.addElement(Paragraph("Executive Summary", fWhite))
        tc.addElement(Paragraph("Items requiring attention, listed by priority", fWhiteSm))
        titleTbl.addCell(tc)
        doc.add(titleTbl)

        val safety  = items.filter { it.rating == Rating.SAFETY }
        val major   = items.filter { it.rating == Rating.MAJOR }
        val monitor = items.filter { it.rating == Rating.MONITOR }

        if (safety.isEmpty() && major.isEmpty() && monitor.isEmpty()) {
            val noIssues = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
            val ni = PdfPCell().apply {
                backgroundColor = BaseColor(240, 253, 244)
                border = Rectangle.BOX; borderColor = cGreen; paddingAll = 16f
            }
            ni.addElement(Paragraph("No significant deficiencies were observed at the time of inspection.",
                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, cGreen)))
            noIssues.addCell(ni)
            doc.add(noIssues)
        }

        if (safety.isNotEmpty())  summaryGroup(doc, "SAFETY ISSUES — Correct Immediately", cRed, safety, photos)
        if (major.isNotEmpty())   summaryGroup(doc, "MAJOR CONCERNS — Correct Prior to Closing", cOrange, major, photos)
        if (monitor.isNotEmpty()) summaryGroup(doc, "MONITOR — Repair or Maintain", cYellow, monitor, photos)

        val good = items.filter { it.rating == Rating.GOOD }
        if (good.isNotEmpty()) {
            doc.add(Paragraph(" "))
            val gHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 6f }
            val gh = PdfPCell(Phrase("ITEMS INSPECTED — No Deficiencies Noted",
                Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, cGreen))).apply {
                backgroundColor = BaseColor(240, 253, 244); border = Rectangle.NO_BORDER; paddingAll = 8f }
            gHdr.addCell(gh)
            doc.add(gHdr)
            val goodTbl = PdfPTable(3).apply { widthPercentage = 100f; spacingAfter = 16f }
            good.forEach { item ->
                val ci = InspectionSections.allItems().find { it.id == item.itemId }
                goodTbl.addCell(PdfPCell(Phrase("✓  ${ci?.title ?: item.itemId}",
                    Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, cGreen))).apply {
                    border = Rectangle.NO_BORDER; paddingAll = 3f })
            }
            repeat((3 - good.size % 3) % 3) {
                goodTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER }) }
            doc.add(goodTbl)
        }
        doc.newPage()
    }

    private fun summaryGroup(
        doc: Document, heading: String, color: BaseColor,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        val hdrTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 4f; spacingBefore = 8f }
        val hdrCell = PdfPCell(Phrase(heading,
            Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, color))).apply {
            border = Rectangle.BOTTOM; borderColorBottom = color; borderWidthBottom = 2f
            paddingBottom = 6f; paddingTop = 4f; backgroundColor = cOffWhite }
        hdrTbl.addCell(hdrCell)
        doc.add(hdrTbl)

        items.forEach { item ->
            val ci = InspectionSections.allItems().find { it.id == item.itemId }
            val itemPhotos = photos.filter { it.itemId == item.itemId }.take(2)
            val row = PdfPTable(if (itemPhotos.isNotEmpty()) 2 else 1).apply {
                widthPercentage = 100f; spacingAfter = 6f
                if (itemPhotos.isNotEmpty()) setWidths(floatArrayOf(2.5f, 1f))
            }
            val textCell = PdfPCell().apply {
                border = Rectangle.LEFT; borderColorLeft = color; borderWidthLeft = 3f
                paddingLeft = 10f; paddingTop = 8f; paddingBottom = 8f; paddingRight = 8f
                backgroundColor = cOffWhite
            }
            val sectionName = InspectionSections.sectionNames[item.section] ?: item.section
            textCell.addElement(Paragraph("$sectionName  ›  ${ci?.title ?: item.itemId}",
                Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)))
            if (item.narrative.isNotBlank())
                textCell.addElement(Paragraph(item.narrative, fBody).apply { spacingBefore = 4f })
            row.addCell(textCell)
            if (itemPhotos.isNotEmpty()) {
                val photoCell = PdfPCell().apply {
                    border = Rectangle.NO_BORDER; paddingLeft = 4f; backgroundColor = cOffWhite }
                itemPhotos.forEach { photo ->
                    try {
                        val bmp = BitmapFactory.decodeFile(photo.filePath)
                        if (bmp != null) {
                            val stream = java.io.ByteArrayOutputStream()
                            bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 75, stream)
                            val img = Image.getInstance(stream.toByteArray())
                            img.scaleToFit(100f, 80f)
                            photoCell.addElement(img)
                        }
                    } catch (_: Exception) {}
                }
                row.addCell(photoCell)
            }
            doc.add(row)
        }
    }

    private fun pageFullDetails(
        doc: Document, report: Report,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        val titleTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
        val tc = PdfPCell().apply { backgroundColor = cNavy; border = Rectangle.NO_BORDER; paddingAll = 14f }
        tc.addElement(Paragraph("Full Inspection Details", fWhite))
        tc.addElement(Paragraph("Complete findings for all inspected systems and components", fWhiteSm))
        titleTbl.addCell(tc)
        doc.add(titleTbl)

        val sectionNarratives = mapOf(
            "roofing" to report.roofingNarrative, "exterior" to report.exteriorNarrative,
            "structure" to report.structureNarrative, "electrical" to report.electricalNarrative,
            "hvac" to report.hvacNarrative, "plumbing" to report.plumbingNarrative,
            "interior" to report.interiorNarrative, "insulation" to report.insulationNarrative,
            "garage" to report.garageNarrative
        )

        InspectionSections.sections.forEach { section ->
            val sectionItems = InspectionSections.items[section] ?: return@forEach
            val sectionName  = InspectionSections.sectionNames[section] ?: section
            val icon         = InspectionSections.sectionIcons[section] ?: ""

            val sHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingBefore = 16f; spacingAfter = 0f }
            val sh = PdfPCell().apply {
                backgroundColor = cNavy; border = Rectangle.NO_BORDER
                paddingTop = 10f; paddingBottom = 10f; paddingLeft = 16f }
            sh.addElement(Paragraph("$icon  ${sectionName.uppercase()}", fH3White))
            sHdr.addCell(sh)
            doc.add(sHdr)
            doc.add(Chunk(LineSeparator(2f, 100f, cGold, Element.ALIGN_CENTER, 0f)))
            doc.add(Paragraph(" "))

            val tbl = PdfPTable(3).apply {
                widthPercentage = 100f; spacingAfter = 10f
                setWidths(floatArrayOf(3.5f, 1f, 3f))
            }
            fun thdr(t: String) = PdfPCell(Phrase(t, fSmallB)).apply {
                backgroundColor = cLightBg; border = Rectangle.BOTTOM
                borderColorBottom = cBorder; paddingAll = 6f }
            tbl.addCell(thdr("COMPONENT / SYSTEM"))
            tbl.addCell(thdr("RATING"))
            tbl.addCell(thdr("FINDINGS"))

            sectionItems.forEach { ci ->
                val found  = items.find { it.itemId == ci.id }
                val rating = found?.rating ?: Rating.NOT_RATED
                val color  = rColor(rating)
                tbl.addCell(PdfPCell(Phrase(ci.title, fBody)).apply {
                    border = Rectangle.BOTTOM; borderColorBottom = cBorder; paddingAll = 7f })
                val rCell = PdfPCell().apply {
                    border = Rectangle.BOTTOM; borderColorBottom = cBorder; paddingAll = 7f
                    horizontalAlignment = Element.ALIGN_CENTER
                    backgroundColor = if (rating != Rating.NOT_RATED)
                        BaseColor(color.red, color.green, color.blue, 25) else cWhite }
                rCell.addElement(Paragraph(rating.short,
                    Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)).apply {
                    alignment = Element.ALIGN_CENTER })
                tbl.addCell(rCell)
                val narrative = found?.narrative?.ifBlank { null }
                tbl.addCell(PdfPCell(Phrase(narrative ?: "—",
                    if (narrative != null) fBody else fSmall)).apply {
                    border = Rectangle.BOTTOM; borderColorBottom = cBorder; paddingAll = 7f })
            }
            doc.add(tbl)

            val narrative = sectionNarratives[section]
            if (!narrative.isNullOrBlank()) {
                val nBox = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 10f }
                val nCell = PdfPCell().apply {
                    border = Rectangle.BOX; borderColor = cGold
                    backgroundColor = BaseColor(253, 249, 242); paddingAll = 12f }
                nCell.addElement(Paragraph("Inspector Narrative", fGold).apply { spacingAfter = 4f })
                nCell.addElement(Paragraph(narrative, fBody))
                nBox.addCell(nCell)
                doc.add(nBox)
            }

            val sectionPhotos = photos.filter { it.section == section && it.itemId == null }.take(4)
            if (sectionPhotos.isNotEmpty()) {
                val cols = minOf(sectionPhotos.size, 4)
                val photoTbl = PdfPTable(cols).apply { widthPercentage = 100f; spacingAfter = 12f }
                sectionPhotos.forEach { photo ->
                    try {
                        val bmp = BitmapFactory.decodeFile(photo.filePath)
                        if (bmp != null) {
                            val stream = java.io.ByteArrayOutputStream()
                            bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 75, stream)
                            val img = Image.getInstance(stream.toByteArray())
                            img.scaleToFit(120f, 95f)
                            val pc = PdfPCell(img).apply {
                                border = Rectangle.BOX; borderColor = cBorder
                                paddingAll = 3f; horizontalAlignment = Element.ALIGN_CENTER }
                            photoTbl.addCell(pc)
                        }
                    } catch (_: Exception) {}
                }
                repeat(cols - sectionPhotos.size) {
                    photoTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER }) }
                doc.add(photoTbl)
            }
        }
    }

    private fun pageDisclaimer(doc: Document, report: Report) {
        doc.newPage()
        val hdrTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
        val h = PdfPCell().apply { backgroundColor = cNavy; border = Rectangle.NO_BORDER; paddingAll = 14f }
        h.addElement(Paragraph("Scope & Limitations", fWhite))
        hdrTbl.addCell(h)
        doc.add(hdrTbl)
        val text = buildString {
            if (report.limitations.isNotBlank()) { append(report.limitations); append("\n\n") }
            append("This inspection report was prepared in accordance with the InterNACHI Standards of Practice. ")
            append("The inspection is a visual examination of the readily accessible installed systems and components of a home. ")
            append("It is not technically exhaustive.\n\n")
            append("Recommend evaluation and repair by appropriately licensed contractors for all items rated Safety Issue or Major Concern prior to closing.\n\n")
            append("Inspector: ${report.inspectorName}   |   Cert #: ${report.inspectorCert}   |   Date: ${report.inspectionDate}\n")
            append("Company: ${report.inspectorCompany}   |   Phone: ${report.inspectorPhone}")
        }
        doc.add(Paragraph(text, fBody).apply { spacingAfter = 20f })
        doc.add(Chunk(LineSeparator(1f, 100f, cGold, Element.ALIGN_CENTER, -2f)))
        doc.add(Paragraph("\nInspection performed in accordance with InterNACHI Standards of Practice  |  www.nachi.org",
            Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, cGray)).apply { alignment = Element.ALIGN_CENTER })
    }

    class PageEvent(private val report: Report) : PdfPageEventHelper() {
        override fun onEndPage(writer: PdfWriter, document: Document) {
            val cb   = writer.directContent
            val font = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, BaseColor(150, 155, 165))
            val address = report.propertyAddress.ifBlank { "ProInspect Report" }
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                Phrase(address, font), document.left(), document.bottom() - 12, 0f)
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                Phrase("Page ${document.pageNumber}", font), document.right(), document.bottom() - 12, 0f)
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                Phrase("ProInspect  |  InterNACHI Standards", font),
                (document.left() + document.right()) / 2, document.bottom() - 12, 0f)
        }
    }
}
