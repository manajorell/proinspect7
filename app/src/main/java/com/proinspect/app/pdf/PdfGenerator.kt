package com.proinspect.app.pdf

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.ColumnText
import com.proinspect.app.data.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {

    private val cNavy     = BaseColor(26, 39, 68)
    private val cGold     = BaseColor(201, 151, 58)
    private val cRed      = BaseColor(220, 38, 38)
    private val cOrange   = BaseColor(249, 115, 22)
    private val cBlue     = BaseColor(37, 99, 235)
    private val cGreen    = BaseColor(34, 197, 94)
    private val cGray     = BaseColor(107, 114, 128)
    private val cLightBg  = BaseColor(248, 245, 239)
    private val cBorder   = BaseColor(220, 215, 200)
    private val cWhite    = BaseColor.WHITE
    private val cOffWhite = BaseColor(252, 251, 248)
    private val cRedLight    = BaseColor(254, 242, 242)
    private val cOrangeLight = BaseColor(255, 247, 237)
    private val cBlueLight   = BaseColor(239, 246, 255)
    private val cGreenLight  = BaseColor(240, 253, 244)

    private val fH1      = Font(Font.FontFamily.HELVETICA, 22f, Font.BOLD,   BaseColor.WHITE)
    private val fH2      = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD,   BaseColor.WHITE)
    private val fH3      = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD,   BaseColor(26, 39, 68))
    private val fH3White = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD,   BaseColor.WHITE)
    private val fH4      = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD,   BaseColor(26, 39, 68))
    private val fBody    = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(40, 45, 55))
    private val fBodySm  = Font(Font.FontFamily.HELVETICA, 9f,  Font.NORMAL, BaseColor(40, 45, 55))
    private val fSmall   = Font(Font.FontFamily.HELVETICA, 8f,  Font.NORMAL, BaseColor(100, 110, 120))
    private val fSmallB  = Font(Font.FontFamily.HELVETICA, 8f,  Font.BOLD,   BaseColor(100, 110, 120))
    private val fGold    = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD,   BaseColor(201, 151, 58))
    private val fWhite   = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD,   BaseColor.WHITE)
    private val fWhiteSm = Font(Font.FontFamily.HELVETICA, 9f,  Font.NORMAL, BaseColor.WHITE)
    private val fItalic  = Font(Font.FontFamily.HELVETICA, 9f,  Font.ITALIC, BaseColor(100, 110, 120))

    private fun rColor(r: Rating) = when (r) {
        Rating.SAFETY      -> cRed
        Rating.MAJOR       -> cOrange
        Rating.MONITOR     -> cBlue
        Rating.GOOD        -> cGreen
        Rating.NOT_RATED   -> cGray
        Rating.NOT_PRESENT -> cGray
    }

    private fun rLightBg(r: Rating) = when (r) {
        Rating.SAFETY      -> cRedLight
        Rating.MAJOR       -> cOrangeLight
        Rating.MONITOR     -> cBlueLight
        Rating.GOOD        -> cGreenLight
        Rating.NOT_RATED   -> cOffWhite
        Rating.NOT_PRESENT -> cOffWhite
    }

    private fun noBorderCell(content: String, font: Font): PdfPCell {
        val c = PdfPCell(Phrase(content, font))
        c.border = Rectangle.NO_BORDER
        c.paddingTop = 3f; c.paddingBottom = 3f
        c.paddingLeft = 4f; c.paddingRight = 4f
        return c
    }

    private fun addSectionHeader(doc: Document, icon: String, title: String, subtitle: String = "") {
        val tbl = PdfPTable(1).apply { widthPercentage = 100f; spacingBefore = 16f; spacingAfter = 0f }
        val cell = PdfPCell().apply {
            backgroundColor = cNavy; border = Rectangle.NO_BORDER
            paddingTop = 12f; paddingBottom = 12f; paddingLeft = 16f; paddingRight = 16f
        }
        cell.addElement(Paragraph("$icon  ${title.uppercase()}", fH3White))
        if (subtitle.isNotBlank())
            cell.addElement(Paragraph(subtitle, fWhiteSm))
        tbl.addCell(cell)
        doc.add(tbl)
        doc.add(Chunk(LineSeparator(2f, 100f, cGold, Element.ALIGN_CENTER, 0f)))
        doc.add(Paragraph(" "))
    }

    private fun addGoldLine(doc: Document) {
        doc.add(Chunk(LineSeparator(2f, 100f, cGold, Element.ALIGN_CENTER, 0f)))
    }

    private fun addThinLine(doc: Document) {
        doc.add(Chunk(LineSeparator(0.5f, 100f, cBorder, Element.ALIGN_CENTER, -2f)))
    }

    suspend fun generate(
        context: Context,
        report: Report,
        items: List<InspectionItem>,
        photos: List<InspectionPhoto>,
        settings: AppSettings
    ): File {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file = File(dir, "ProInspect_${stamp}.pdf")
        val doc = Document(PageSize.LETTER, 50f, 50f, 55f, 55f)
        val writer = PdfWriter.getInstance(doc, FileOutputStream(file))
        writer.pageEvent = HeaderFooterEvent(report)
        doc.open()

        pageCover(doc, report, items, settings)
        addHousePhotoPage(doc, photos)
        pageExecutiveSummary(doc, report, items, photos, context)
        pageFullDetails(doc, report, items, photos)
        pageCertifications(doc, report, settings)
        pageScopeAndPurpose(doc)

        doc.close()
        return file
    }

    private fun pageCover(doc: Document, report: Report, items: List<InspectionItem>, settings: AppSettings) {
        val hdr = PdfPTable(if (settings.companyLogoPath.isNotBlank()) 2 else 1).apply {
            widthPercentage = 100f
            if (settings.companyLogoPath.isNotBlank()) setWidths(floatArrayOf(2f, 1f))
        }
        val hCell = PdfPCell().apply {
            backgroundColor = cNavy; border = Rectangle.NO_BORDER
            paddingTop = 40f; paddingBottom = 36f; paddingLeft = 30f; paddingRight = 16f
        }
        hCell.addElement(Paragraph("ProInspect", Font(Font.FontFamily.HELVETICA, 32f, Font.BOLD, cGold)))
        hCell.addElement(Paragraph("HOME INSPECTION REPORT", fWhiteSm))
        hCell.addElement(Paragraph(" "))
        hCell.addElement(Paragraph(
            report.propertyAddress.ifBlank { "Property Address" },
            Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.WHITE)
        ))
        if (report.propertyCity.isNotBlank())
            hCell.addElement(Paragraph(report.propertyCity,
                Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, BaseColor(180, 190, 210))))
        hdr.addCell(hCell)

        if (settings.companyLogoPath.isNotBlank()) {
            val logoCell = PdfPCell().apply {
                backgroundColor = cNavy; border = Rectangle.NO_BORDER
                paddingTop = 20f; paddingBottom = 20f; paddingLeft = 8f; paddingRight = 20f
                verticalAlignment = Element.ALIGN_MIDDLE
            }
            try {
                val bmp = BitmapFactory.decodeFile(settings.companyLogoPath)
                if (bmp != null) {
                    val stream = java.io.ByteArrayOutputStream()
                    bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
                    val img = Image.getInstance(stream.toByteArray())
                    img.scaleToFit(280f, 140f)
                    img.alignment = Element.ALIGN_RIGHT
                    logoCell.addElement(img)
                }
            } catch (_: Exception) {}
            hdr.addCell(logoCell)
        }
        doc.add(hdr)
        doc.add(Chunk(LineSeparator(3f, 100f, cGold, Element.ALIGN_CENTER, 0f)))

        val grid = PdfPTable(4).apply {
            widthPercentage = 100f; spacingBefore = 20f; spacingAfter = 24f
            setWidths(floatArrayOf(1f, 1.5f, 1f, 1.5f))
        }
        fun lbl(t: String): PdfPCell {
            val c = PdfPCell(Phrase(t, fSmallB))
            c.border = Rectangle.NO_BORDER; c.paddingBottom = 2f; c.paddingTop = 8f
            return c
        }
        fun value(t: String): PdfPCell {
            val c = PdfPCell(Phrase(t.ifBlank { "—" }, fH3))
            c.border = Rectangle.BOTTOM; c.borderColor = cBorder
            c.paddingBottom = 6f; c.paddingTop = 0f
            return c
        }
        grid.addCell(lbl("CLIENT"));      grid.addCell(value(report.clientName))
        grid.addCell(lbl("DATE"));        grid.addCell(value(report.inspectionDate))
        grid.addCell(lbl("PROPERTY"));    grid.addCell(value(report.propertyAddress))
        grid.addCell(lbl("INSPECTOR"));   grid.addCell(value(report.inspectorName))
        grid.addCell(lbl("YEAR BUILT"));  grid.addCell(value(report.yearBuilt))
        grid.addCell(lbl("CERT #"));      grid.addCell(value(report.inspectorCert))
        grid.addCell(lbl("SQ FT"));       grid.addCell(value(report.squareFootage))
        grid.addCell(lbl("COMPANY"));     grid.addCell(value(report.inspectorCompany))
        if (report.weatherConditions.isNotBlank()) {
            grid.addCell(lbl("WEATHER")); grid.addCell(value(report.weatherConditions))
            grid.addCell(lbl("PHONE"));   grid.addCell(value(report.inspectorPhone))
        }
        doc.add(grid)

        addThinLine(doc)
        addRatingLegendWithCounts(doc, items)

        val badgePaths = listOf(
            settings.badge1Path, settings.badge2Path,
            settings.badge3Path, settings.badge4Path
        ).filter { it.isNotBlank() && File(it).exists() }

        if (badgePaths.isNotEmpty()) {
            doc.add(Paragraph(" "))
            addThinLine(doc)
            doc.add(Paragraph(" "))
            val certHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 8f }
            val ch = PdfPCell(Phrase("Inspector Certifications & Credentials",
                Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, cNavy)))
            ch.border = Rectangle.NO_BORDER
            ch.horizontalAlignment = Element.ALIGN_CENTER
            ch.paddingBottom = 4f
            certHdr.addCell(ch)
            doc.add(certHdr)
            val cols = minOf(badgePaths.size, 4)
            val badgeTbl = PdfPTable(cols).apply {
                widthPercentage = 60f; spacingAfter = 12f
                horizontalAlignment = Element.ALIGN_CENTER
            }
            badgePaths.forEach { path ->
                try {
                    val bmp = BitmapFactory.decodeFile(path)
                    if (bmp != null) {
                        val stream = java.io.ByteArrayOutputStream()
                        bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
                        val img = Image.getInstance(stream.toByteArray())
                        img.scaleToFit(100f, 100f)
                        val bc = PdfPCell(img)
                        bc.border = Rectangle.NO_BORDER
                        bc.paddingTop = 6f; bc.paddingBottom = 6f
                        bc.paddingLeft = 10f; bc.paddingRight = 10f
                        bc.horizontalAlignment = Element.ALIGN_CENTER
                        badgeTbl.addCell(bc)
                    }
                } catch (_: Exception) {}
            }
            repeat(cols - badgePaths.size) {
                badgeTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
            }
            doc.add(badgeTbl)
        }
        doc.newPage()
    }

    private fun addRatingLegendWithCounts(doc: Document, items: List<InspectionItem>) {
        doc.add(Paragraph("\n"))
        val legendTitle = Paragraph("Rating Legend",
            Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, cNavy))
        legendTitle.alignment = Element.ALIGN_CENTER
        legendTitle.spacingAfter = 6f
        doc.add(legendTitle)

        val safetyCount   = items.count { it.rating == Rating.SAFETY }
        val majorCount    = items.count { it.rating == Rating.MAJOR }
        val monitorCount  = items.count { it.rating == Rating.MONITOR }
        val goodCount     = items.count { it.rating == Rating.GOOD }
        val notRatedCount = items.count { it.rating == Rating.NOT_RATED }

        val legendTable = PdfPTable(5)
        legendTable.widthPercentage = 90f
        legendTable.spacingBefore = 5f
        legendTable.spacingAfter = 10f
        legendTable.horizontalAlignment = Element.ALIGN_CENTER

        addClickableLegendItem(legendTable, "Safety Issue",   cRed,    "Immediate correction required", "🛑", safetyCount)
        addClickableLegendItem(legendTable, "Major Concern",  cOrange, "Correct prior to closing",      "🩹", majorCount)
        addClickableLegendItem(legendTable, "Monitor",        cBlue,   "Repair or maintain",            "🔍", monitorCount)
        addClickableLegendItem(legendTable, "Good",           cGreen,  "No deficiencies noted",         "👍", goodCount)
        addClickableLegendItem(legendTable, "Not Rated",      cGray,   "Not inspected or N/A",          "",   notRatedCount)
        doc.add(legendTable)

        val instructionText = Paragraph("Click on any count above to view detailed findings",
            Font(Font.FontFamily.HELVETICA, 9f, Font.ITALIC, cGray))
        instructionText.alignment = Element.ALIGN_CENTER
        instructionText.spacingBefore = 5f
        doc.add(instructionText)
        doc.add(Chunk(LineSeparator(0.5f, 100f, cBorder, Element.ALIGN_CENTER, -2f)))
        doc.add(Paragraph("\n"))
    }

    private fun addClickableLegendItem(
        table: PdfPTable, label: String, color: BaseColor,
        description: String, icon: String, count: Int
    ) {
        val cell = PdfPCell()
        cell.border = Rectangle.BOX
        cell.borderColor = color
        cell.borderWidth = 1.5f
        cell.backgroundColor = BaseColor(color.red, color.green, color.blue, 15)
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        cell.paddingTop = 10f; cell.paddingBottom = 10f
        cell.paddingLeft = 8f; cell.paddingRight = 8f

        val paragraph = Paragraph()
        paragraph.alignment = Element.ALIGN_CENTER
        if (icon.isNotEmpty()) {
            paragraph.add(Chunk(icon, Font(Font.FontFamily.HELVETICA, 20f, Font.NORMAL)))
            paragraph.add(Chunk("\n"))
        }
        paragraph.add(Chunk(label, Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)))
        paragraph.add(Chunk("\n"))
        paragraph.add(Chunk(description, Font(Font.FontFamily.HELVETICA, 7f, Font.NORMAL, cGray)))
        paragraph.add(Chunk("\n\n"))
        cell.addElement(paragraph)

        val countChunk = Chunk("Count: $count", Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD, color))
        countChunk.setLocalGoto("executive_summary")
        val countPara = Paragraph()
        countPara.add(countChunk)
        countPara.alignment = Element.ALIGN_CENTER
        cell.addElement(countPara)
        table.addCell(cell)
    }

    private fun addHousePhotoPage(doc: Document, photos: List<InspectionPhoto>) {
        val housePhotos = photos.filter { it.section == "info" && it.itemId == null }
        if (housePhotos.isEmpty()) return

        addSectionHeader(doc, "📷", "Property Photo", "Front exterior view of inspected property")

        try {
            val photoFile = File(housePhotos.first().photoPath)
            if (photoFile.exists()) {
                val bmp = BitmapFactory.decodeFile(photoFile.absolutePath)
                if (bmp != null) {
                    val stream = java.io.ByteArrayOutputStream()
                    bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, stream)
                    val img = Image.getInstance(stream.toByteArray())
                    img.scaleToFit(450f, 350f)
                    img.alignment = Element.ALIGN_CENTER
                    val imgTable = PdfPTable(1).apply {
                        widthPercentage = 90f; spacingAfter = 12f
                        horizontalAlignment = Element.ALIGN_CENTER
                    }
                    val imgCell = PdfPCell()
                    imgCell.border = Rectangle.BOX; imgCell.borderColor = cBorder; imgCell.borderWidth = 2f
                    imgCell.paddingTop = 10f; imgCell.paddingBottom = 10f
                    imgCell.paddingLeft = 10f; imgCell.paddingRight = 10f
                    imgCell.horizontalAlignment = Element.ALIGN_CENTER
                    imgCell.backgroundColor = cOffWhite
                    imgCell.addElement(img)
                    imgTable.addCell(imgCell)
                    doc.add(imgTable)
                }
            }
        } catch (_: Exception) {}
        doc.newPage()
    }

    private fun pageExecutiveSummary(
        doc: Document, report: Report,
        items: List<InspectionItem>, photos: List<InspectionPhoto>,
        context: Context
    ) {
        val destChunk = Chunk(" ")
        destChunk.setLocalDestination("executive_summary")
        doc.add(Paragraph(destChunk))

        addSectionHeader(doc, "📋", "Executive Summary", "Items requiring attention, listed by priority")

        val safety  = items.filter { it.rating == Rating.SAFETY }
        val major   = items.filter { it.rating == Rating.MAJOR }
        val monitor = items.filter { it.rating == Rating.MONITOR }

        if (safety.isEmpty() && major.isEmpty() && monitor.isEmpty()) {
            val noIssues = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
            val ni = PdfPCell()
            ni.backgroundColor = cGreenLight
            ni.border = Rectangle.BOX; ni.borderColor = cGreen; ni.borderWidth = 2f
            ni.paddingTop = 16f; ni.paddingBottom = 16f
            ni.paddingLeft = 16f; ni.paddingRight = 16f
            ni.addElement(Paragraph("No significant deficiencies were observed at the time of inspection.",
                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, cGreen)))
            noIssues.addCell(ni)
            doc.add(noIssues)
        }

        if (safety.isNotEmpty())  summaryGroup(doc, "SAFETY ISSUES — Correct Immediately",       cRed,    safety,  photos)
        if (major.isNotEmpty())   summaryGroup(doc, "MAJOR CONCERNS — Correct Prior to Closing", cOrange, major,   photos)
        if (monitor.isNotEmpty()) summaryGroup(doc, "MONITOR — Repair or Maintain",              cBlue,   monitor, photos)

        val good = items.filter { it.rating == Rating.GOOD }
        if (good.isNotEmpty()) {
            doc.add(Paragraph(" "))
            val gHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 6f }
            val gh = PdfPCell(Phrase("ITEMS INSPECTED — No Deficiencies Noted",
                Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, cGreen)))
            gh.backgroundColor = cGreenLight; gh.border = Rectangle.NO_BORDER
            gh.paddingTop = 8f; gh.paddingBottom = 8f; gh.paddingLeft = 8f; gh.paddingRight = 8f
            gHdr.addCell(gh)
            doc.add(gHdr)
            val goodTbl = PdfPTable(3).apply { widthPercentage = 100f; spacingAfter = 16f }
            good.forEach { item ->
                val ci = InspectionSections.allItems.find { it.id == item.itemId }
                val gc = PdfPCell(Phrase("✓  ${ci?.label ?: item.itemId}",
                    Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, cGreen)))
                gc.border = Rectangle.NO_BORDER
                gc.paddingTop = 3f; gc.paddingBottom = 3f; gc.paddingLeft = 3f; gc.paddingRight = 3f
                goodTbl.addCell(gc)
            }
            repeat((3 - good.size % 3) % 3) {
                goodTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
            }
            doc.add(goodTbl)
        }

        addSignedAgreement(doc, report, context)
        doc.newPage()
    }

    private fun summaryGroup(
        doc: Document, heading: String, color: BaseColor,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        val hdrTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 4f; spacingBefore = 8f }
        val hdrCell = PdfPCell(Phrase(heading, Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, color)))
        hdrCell.border = Rectangle.BOTTOM; hdrCell.borderColorBottom = color; hdrCell.borderWidthBottom = 2f
        hdrCell.paddingBottom = 6f; hdrCell.paddingTop = 4f; hdrCell.backgroundColor = cOffWhite
        hdrTbl.addCell(hdrCell)
        doc.add(hdrTbl)

        items.forEach { item ->
            val ci = InspectionSections.allItems.find { it.id == item.itemId }
            val itemPhotos = photos.filter { it.itemId == item.itemId }.take(2)
            val row = PdfPTable(if (itemPhotos.isNotEmpty()) 2 else 1).apply {
                widthPercentage = 100f; spacingAfter = 6f
                if (itemPhotos.isNotEmpty()) setWidths(floatArrayOf(2.5f, 1f))
            }
            val textCell = PdfPCell()
            textCell.border = Rectangle.LEFT; textCell.borderColorLeft = color; textCell.borderWidthLeft = 3f
            textCell.paddingLeft = 10f; textCell.paddingTop = 8f; textCell.paddingBottom = 8f; textCell.paddingRight = 8f
            textCell.backgroundColor = cOffWhite
            val sectionName = InspectionSections.sectionNames[item.section] ?: item.section
            textCell.addElement(Paragraph("$sectionName  ›  ${ci?.label ?: item.itemId}",
                Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)))
            if (item.narrative.isNotBlank())
                textCell.addElement(Paragraph(item.narrative, fBody).apply { spacingBefore = 4f })
            row.addCell(textCell)
            if (itemPhotos.isNotEmpty()) {
                val photoCell = PdfPCell()
                photoCell.border = Rectangle.NO_BORDER; photoCell.paddingLeft = 4f
                photoCell.backgroundColor = cOffWhite
                itemPhotos.forEach { photo ->
                    try {
                        val bmp = BitmapFactory.decodeFile(photo.photoPath)
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

    private fun addSignedAgreement(doc: Document, report: Report, context: Context) {
        if (report.signedAgreementPath.isBlank()) return
        try {
            doc.add(Paragraph("\n"))
            addThinLine(doc)
