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

    // ── Colors ─────────────────────────────────────────────────────────────────
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

    // ── Fonts ──────────────────────────────────────────────────────────────────
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

    // ── Helpers ────────────────────────────────────────────────────────────────
    private fun rColor(r: Rating) = when (r) {
        Rating.SAFETY    -> cRed
        Rating.MAJOR     -> cOrange
        Rating.MONITOR   -> cBlue
        Rating.GOOD      -> cGreen
        Rating.NOT_RATED -> cGray
    }

    private fun rLightBg(r: Rating) = when (r) {
        Rating.SAFETY    -> cRedLight
        Rating.MAJOR     -> cOrangeLight
        Rating.MONITOR   -> cBlueLight
        Rating.GOOD      -> cGreenLight
        Rating.NOT_RATED -> cOffWhite
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

    // ── Entry Point ────────────────────────────────────────────────────────────
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

        doc.close()
        return file
    }

    // ── Cover Page ─────────────────────────────────────────────────────────────
    private fun pageCover(doc: Document, report: Report, items: List<InspectionItem>, settings: AppSettings) {

        // Header bar
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

        // Info grid
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

        // Certification badges
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

    // ── Rating Legend ──────────────────────────────────────────────────────────
    private fun addRatingLegendWithCounts(doc: Document, items: List<InspectionItem>) {
        doc.add(Paragraph("\n"))
        val legendTitle = Paragraph("Rating Legend",
            Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, cNavy))
        legendTitle.alignment = Element.ALIGN_CENTER
        legendTitle.spacingAfter = 6f
        doc.add(legendTitle)

        val safetyCount  = items.count { it.rating == Rating.SAFETY }
        val majorCount   = items.count { it.rating == Rating.MAJOR }
        val monitorCount = items.count { it.rating == Rating.MONITOR }
        val goodCount    = items.count { it.rating == Rating.GOOD }
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

    // ── House Photo Page ───────────────────────────────────────────────────────
    private fun addHousePhotoPage(doc: Document, photos: List<InspectionPhoto>) {
        val housePhotos = photos.filter { it.section == "info" && it.itemId == null }
        if (housePhotos.isEmpty()) return

        addSectionHeader(doc, "📷", "Property Photo", "Front exterior view of inspected property")

        try {
            val photoFile = File(housePhotos.first().filePath)
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

    // ── Executive Summary ──────────────────────────────────────────────────────
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

        if (safety.isNotEmpty())  summaryGroup(doc, "SAFETY ISSUES — Correct Immediately",          cRed,    safety,  photos)
        if (major.isNotEmpty())   summaryGroup(doc, "MAJOR CONCERNS — Correct Prior to Closing",    cOrange, major,   photos)
        if (monitor.isNotEmpty()) summaryGroup(doc, "MONITOR — Repair or Maintain",                 cBlue,   monitor, photos)

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
                val gc = PdfPCell(Phrase("✓  ${ci?.title ?: item.itemId}",
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
            textCell.addElement(Paragraph("$sectionName  ›  ${ci?.title ?: item.itemId}",
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

    // ── Signed Agreement ───────────────────────────────────────────────────────
    private fun addSignedAgreement(doc: Document, report: Report, context: Context) {
        if (report.signedAgreementPath.isBlank()) return
        try {
            doc.add(Paragraph("\n"))
            addThinLine(doc)
            doc.add(Paragraph("\n"))

            val agreementHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 12f }
            val hdrCell = PdfPCell()
            hdrCell.backgroundColor = cNavy; hdrCell.border = Rectangle.NO_BORDER
            hdrCell.paddingTop = 10f; hdrCell.paddingBottom = 10f
            hdrCell.paddingLeft = 14f; hdrCell.paddingRight = 14f
            hdrCell.addElement(Paragraph("Signed Inspection Agreement",
                Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)))
            agreementHdr.addCell(hdrCell)
            doc.add(agreementHdr)

            val bmp = if (report.signedAgreementPath.startsWith("content://")) {
                val uri = android.net.Uri.parse(report.signedAgreementPath)
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } else {
                val filePath = if (report.signedAgreementPath.startsWith("file://"))
                    report.signedAgreementPath.removePrefix("file://")
                else report.signedAgreementPath
                BitmapFactory.decodeFile(filePath)
            }

            if (bmp != null) {
                val stream = java.io.ByteArrayOutputStream()
                bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, stream)
                val img = Image.getInstance(stream.toByteArray())
                img.scaleToFit(500f, 650f)
                img.alignment = Element.ALIGN_CENTER
                val imgTable = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 12f }
                val imgCell = PdfPCell()
                imgCell.border = Rectangle.BOX; imgCell.borderColor = cBorder; imgCell.borderWidth = 1f
                imgCell.paddingTop = 10f; imgCell.paddingBottom = 10f
                imgCell.paddingLeft = 10f; imgCell.paddingRight = 10f
                imgCell.horizontalAlignment = Element.ALIGN_CENTER
                imgCell.addElement(img)
                imgTable.addCell(imgCell)
                doc.add(imgTable)
                val caption = Paragraph(
                    "Inspection agreement signed by client on ${report.inspectionDate}",
                    Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, cGray)
                )
                caption.alignment = Element.ALIGN_CENTER
                caption.spacingAfter = 10f
                doc.add(caption)
            }
        } catch (_: Exception) {
            val errorNote = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 12f }
            val noteCell = PdfPCell()
            noteCell.backgroundColor = BaseColor(254, 243, 199)
            noteCell.border = Rectangle.BOX; noteCell.borderColor = cOrange
            noteCell.paddingTop = 12f; noteCell.paddingBottom = 12f
            noteCell.paddingLeft = 12f; noteCell.paddingRight = 12f
            noteCell.addElement(Paragraph("Signed inspection agreement on file",
                Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(120, 53, 15))))
            errorNote.addCell(noteCell)
            doc.add(errorNote)
        }
    }

    // ── Full Details ───────────────────────────────────────────────────────────
    private fun pageFullDetails(
        doc: Document, report: Report,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        addSectionHeader(doc, "🔍", "Full Inspection Details",
            "Complete findings for all inspected systems and components")

        val sectionNarratives = mapOf(
            "roofing"    to report.roofingNarrative,
            "exterior"   to report.exteriorNarrative,
            "structure"  to report.structureNarrative,
            "electrical" to report.electricalNarrative,
            "hvac"       to report.hvacNarrative,
            "plumbing"   to report.plumbingNarrative,
            "interior"   to report.interiorNarrative,
            "insulation" to report.insulationNarrative,
            "garage"     to report.garageNarrative
        )

        InspectionSections.sections.forEach { section ->
            val sectionItemsList = InspectionSections.items[section] ?: return@forEach
            val sectionName = InspectionSections.sectionNames[section] ?: section
            val icon = InspectionSections.sectionIcons[section] ?: ""
            val sectionItems = sectionItemsList.map { ci ->
                ci to (items.find { it.itemId == ci.id })
            }

            // Section header
            val sHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingBefore = 16f; spacingAfter = 0f }
            val sh = PdfPCell()
            sh.backgroundColor = cNavy; sh.border = Rectangle.NO_BORDER
            sh.paddingTop = 10f; sh.paddingBottom = 10f; sh.paddingLeft = 16f; sh.paddingRight = 16f
            sh.addElement(Paragraph("$icon  ${sectionName.uppercase()}", fH3White))
            sHdr.addCell(sh)
            doc.add(sHdr)
            doc.add(Chunk(LineSeparator(2f, 100f, cGold, Element.ALIGN_CENTER, 0f)))
            doc.add(Paragraph(" "))

            // ── Section stats bar ──────────────────────────────────────────────
            addSectionStatsBar(doc, sectionItems.map { it.second })

            // ── Inspector-filled fields per section ────────────────────────────
            addSectionSpecificFields(doc, section, report)

            // ── Checklist items table ──────────────────────────────────────────
            val tbl = PdfPTable(3).apply {
                widthPercentage = 100f; spacingAfter = 4f
                setWidths(floatArrayOf(3.5f, 1f, 3f))
            }
            fun thdr(t: String): PdfPCell {
                val c = PdfPCell(Phrase(t, fSmallB))
                c.backgroundColor = cLightBg; c.border = Rectangle.BOTTOM; c.borderColorBottom = cBorder
                c.paddingTop = 6f; c.paddingBottom = 6f; c.paddingLeft = 6f; c.paddingRight = 6f
                return c
            }
            tbl.addCell(thdr("COMPONENT / SYSTEM"))
            tbl.addCell(thdr("RATING"))
            tbl.addCell(thdr("FINDINGS"))

            sectionItemsList.forEach { ci ->
                val found  = items.find { it.itemId == ci.id }
                val rating = found?.rating ?: Rating.NOT_RATED
                val color  = rColor(rating)

                val nameCell = PdfPCell(Phrase(ci.title, fBody))
                nameCell.border = Rectangle.BOTTOM; nameCell.borderColorBottom = cBorder
                nameCell.paddingTop = 7f; nameCell.paddingBottom = 7f
                nameCell.paddingLeft = 7f; nameCell.paddingRight = 7f
                tbl.addCell(nameCell)

                val rCell = PdfPCell()
                rCell.border = Rectangle.BOTTOM; rCell.borderColorBottom = cBorder
                rCell.paddingTop = 7f; rCell.paddingBottom = 7f
                rCell.paddingLeft = 7f; rCell.paddingRight = 7f
                rCell.horizontalAlignment = Element.ALIGN_CENTER
                rCell.backgroundColor = if (rating != Rating.NOT_RATED)
                    BaseColor(color.red, color.green, color.blue, 25) else cWhite
                rCell.addElement(Paragraph(rating.short,
                    Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)).apply {
                    alignment = Element.ALIGN_CENTER
                })
                tbl.addCell(rCell)

                val narrative = found?.narrative?.ifBlank { null }
                val nCell = PdfPCell(Phrase(narrative ?: "—",
                    if (narrative != null) fBody else fSmall))
                nCell.border = Rectangle.BOTTOM; nCell.borderColorBottom = cBorder
                nCell.paddingTop = 7f; nCell.paddingBottom = 7f
                nCell.paddingLeft = 7f; nCell.paddingRight = 7f
                tbl.addCell(nCell)
            }
            doc.add(tbl)

            // ── Inline photos for flagged items ────────────────────────────────
            addFlaggedItemPhotos(doc, section, sectionItemsList, items, photos)

            // ── Good items compact list ────────────────────────────────────────
            val goodItems = sectionItems.filter { (_, item) -> item?.rating == Rating.GOOD }
            if (goodItems.isNotEmpty()) {
                doc.add(Paragraph(" "))
                val gHdr = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 4f }
                val gh = PdfPCell(Phrase("✓  Items Inspected — No Deficiencies",
                    Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD, cGreen)))
                gh.backgroundColor = cGreenLight; gh.border = Rectangle.NO_BORDER
                gh.paddingTop = 5f; gh.paddingBottom = 5f; gh.paddingLeft = 8f; gh.paddingRight = 8f
                gHdr.addCell(gh)
                doc.add(gHdr)
                val goodTbl = PdfPTable(3).apply { widthPercentage = 100f; spacingAfter = 8f }
                goodItems.forEach { (ci, _) ->
                    val gc = PdfPCell(Phrase("✓  ${ci.title}",
                        Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, cGreen)))
                    gc.border = Rectangle.NO_BORDER
                    gc.paddingTop = 2f; gc.paddingBottom = 2f; gc.paddingLeft = 3f; gc.paddingRight = 3f
                    goodTbl.addCell(gc)
                }
                repeat((3 - goodItems.size % 3) % 3) {
                    goodTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
                }
                doc.add(goodTbl)
            }

            // ── Section narrative box ──────────────────────────────────────────
            val narrative = sectionNarratives[section]
            if (!narrative.isNullOrBlank()) {
                val nBox = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 10f }
                val nCell = PdfPCell()
                nCell.border = Rectangle.BOX; nCell.borderColor = cGold; nCell.borderWidth = 2f
                nCell.backgroundColor = BaseColor(253, 249, 242)
                nCell.paddingTop = 12f; nCell.paddingBottom = 12f
                nCell.paddingLeft = 12f; nCell.paddingRight = 12f
                nCell.addElement(Paragraph("📝  Inspector Narrative", fGold).apply { spacingAfter = 4f })
                nCell.addElement(Paragraph(narrative, fBody))
                nBox.addCell(nCell)
                doc.add(nBox)
            }

            // ── Section overview photos ────────────────────────────────────────
            val sectionPhotos = photos.filter { it.section == section && it.itemId == null }.take(4)
            if (sectionPhotos.isNotEmpty()) {
                doc.add(Paragraph(" "))
                val photoLabel = Paragraph("Section Overview Photos",
                    Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD, cGray))
                photoLabel.spacingAfter = 4f
                doc.add(photoLabel)
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
                            val pc = PdfPCell(img)
                            pc.border = Rectangle.BOX; pc.borderColor = cBorder
                            pc.paddingTop = 3f; pc.paddingBottom = 3f
                            pc.paddingLeft = 3f; pc.paddingRight = 3f
                            pc.horizontalAlignment = Element.ALIGN_CENTER
                            photoTbl.addCell(pc)
                        }
                    } catch (_: Exception) {}
                }
                repeat(cols - sectionPhotos.size) {
                    photoTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
                }
                doc.add(photoTbl)
            }

            doc.add(Chunk(LineSeparator(0.5f, 100f, cBorder, Element.ALIGN_CENTER, -2f)))
        }
    }

    // ── Section Stats Bar ──────────────────────────────────────────────────────
    private fun addSectionStatsBar(doc: Document, sectionItems: List<InspectionItem?>) {
        val safety  = sectionItems.count { it?.rating == Rating.SAFETY }
        val major   = sectionItems.count { it?.rating == Rating.MAJOR }
        val monitor = sectionItems.count { it?.rating == Rating.MONITOR }
        val good    = sectionItems.count { it?.rating == Rating.GOOD }
        val total   = sectionItems.size

        val statsTbl = PdfPTable(5).apply {
            widthPercentage = 100f; spacingAfter = 10f
        }

        data class Stat(val label: String, val count: Int, val color: BaseColor, val bg: BaseColor)
        listOf(
            Stat("Safety",  safety,  cRed,    cRedLight),
            Stat("Major",   major,   cOrange, cOrangeLight),
            Stat("Monitor", monitor, cBlue,   cBlueLight),
            Stat("Good",    good,    cGreen,  cGreenLight),
            Stat("Total",   total,   cNavy,   cLightBg)
        ).forEach { stat ->
            val cell = PdfPCell()
            cell.border = Rectangle.BOX
            cell.borderColor = stat.color
            cell.borderWidth = 1f
            cell.backgroundColor = stat.bg
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.paddingTop = 6f; cell.paddingBottom = 6f

            val countPara = Paragraph(stat.count.toString(),
                Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD, stat.color))
            countPara.alignment = Element.ALIGN_CENTER
            cell.addElement(countPara)

            val labelPara = Paragraph(stat.label,
                Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, stat.color))
            labelPara.alignment = Element.ALIGN_CENTER
            cell.addElement(labelPara)

            statsTbl.addCell(cell)
        }
        doc.add(statsTbl)
    }

    // ── Section-Specific Inspector Fields ─────────────────────────────────────
    private fun addSectionSpecificFields(doc: Document, section: String, report: Report) {
        val fields: List<Pair<String, String>> = when (section) {
            "roofing" -> listOf(
                "Roof Type"    to report.roofType,
                "Roof Age"     to report.roofAge,
                "Roof Method"  to report.roofMethod
            )
            "exterior" -> listOf(
                "Siding Type"  to report.sidingType,
                "Driveway"     to report.drivewayType
            )
            "structure" -> listOf(
                "Foundation"   to report.foundationType,
                "Framing"      to report.framingType
            )
            "electrical" -> listOf(
                "Panel Brand"  to report.panelBrand,
                "Panel Amps"   to report.panelAmps,
                "Panel Type"   to report.panelType,
                "Wiring Type"  to report.wiringType,
                "Service"      to report.serviceEntrance
            )
            "hvac" -> listOf(
                "Heat Type"    to report.heatType,
                "Heat Brand"   to report.heatBrand,
                "Heat Age"     to report.heatAge,
                "AC Type"      to report.acType,
                "AC Brand"     to report.acBrand,
                "AC Age"       to report.acAge,
                "Fuel Type"    to report.fuelType,
                "Filter Date"  to report.filterDate
            )
            "plumbing" -> listOf(
                "Supply"       to report.supplyMaterial,
                "Drain"        to report.drainMaterial,
                "Water Heater" to report.whType,
                "WH Age"       to report.whAge,
                "WH Capacity"  to report.whCapacity
            )
            "interior" -> listOf(
                "Property Type" to report.propertyType
            )
            "insulation" -> listOf(
                "Attic Insulation" to report.atticInsulation,
                "Attic R-Value"    to report.atticRValue,
                "Crawl Insulation" to report.crawlInsulation
            )
            "garage" -> listOf(
                "Garage Type"  to report.garageType,
                "Garage Size"  to report.garageCars
            )
            else -> emptyList()
        }.filter { it.second.isNotBlank() }

        if (fields.isEmpty()) return

        val cols = minOf(fields.size, 4)
        val fieldTbl = PdfPTable(cols * 2).apply {
            widthPercentage = 100f; spacingAfter = 10f
            val widths = FloatArray(cols * 2) { if (it % 2 == 0) 0.8f else 1.2f }
            setWidths(widths)
        }

        fields.take(cols * 2).forEach { (label, value) ->
            val lc = PdfPCell(Phrase(label, fSmallB))
            lc.border = Rectangle.NO_BORDER
            lc.backgroundColor = cLightBg
            lc.paddingTop = 5f; lc.paddingBottom = 5f; lc.paddingLeft = 6f; lc.paddingRight = 4f
            fieldTbl.addCell(lc)

            val vc = PdfPCell(Phrase(value, fBodySm))
            vc.border = Rectangle.BOTTOM; vc.borderColorBottom = cBorder
            vc.paddingTop = 5f; vc.paddingBottom = 5f; vc.paddingLeft = 4f; vc.paddingRight = 6f
            fieldTbl.addCell(vc)
        }

        // Fill empty cells if needed
        val remainder = (cols * 2) - (fields.size * 2) % (cols * 2)
        if (remainder != cols * 2) {
            repeat(remainder) {
                fieldTbl.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
            }
        }

        doc.add(fieldTbl)
    }

    // ── Flagged Item Inline Photos ─────────────────────────────────────────────
    private fun addFlaggedItemPhotos(
        doc: Document,
        section: String,
        sectionItemsList: List<ChecklistItem>,
        items: List<InspectionItem>,
        photos: List<InspectionPhoto>
    ) {
        val flaggedItems = sectionItemsList.mapNotNull { ci ->
            val item = items.find { it.itemId == ci.id } ?: return@mapNotNull null
            if (item.rating == Rating.NOT_RATED || item.rating == Rating.GOOD) return@mapNotNull null
            val itemPhotos = photos.filter { it.itemId == ci.id }
            if (itemPhotos.isEmpty() && item.narrative.isBlank()) return@mapNotNull null
            Triple(ci, item, itemPhotos)
        }

        if (flaggedItems.isEmpty()) return

        doc.add(Paragraph(" "))
        val findingsLabel = Paragraph("Detailed Findings with Photos",
            Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, cNavy))
        findingsLabel.spacingAfter = 6f
        doc.add(findingsLabel)

        flaggedItems.forEach { (ci, item, itemPhotos) ->
            val color = rColor(item.rating)
            val bg    = rLightBg(item.rating)

            val itemTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 8f }
            val itemCell = PdfPCell()
            itemCell.border = Rectangle.LEFT
            itemCell.borderColorLeft = color
            itemCell.borderWidthLeft = 4f
            itemCell.backgroundColor = bg
            itemCell.paddingTop = 8f; itemCell.paddingBottom = 8f
            itemCell.paddingLeft = 10f; itemCell.paddingRight = 10f

            // Title row
            val titlePara = Paragraph()
            titlePara.add(Chunk(item.rating.short + "  ",
                Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)))
            titlePara.add(Chunk(ci.title,
                Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, cNavy)))
            itemCell.addElement(titlePara)

            // Narrative
            if (item.narrative.isNotBlank()) {
                itemCell.addElement(Paragraph(item.narrative, fBody).apply { spacingBefore = 4f })
            }

            // Photos inline
            if (itemPhotos.isNotEmpty()) {
                val photoCols = minOf(itemPhotos.size, 3)
                val photoRow = PdfPTable(photoCols).apply {
                    widthPercentage = 100f
                    spacingBefore = 6f
                }
                itemPhotos.take(3).forEach { photo ->
                    try {
                        val bmp = BitmapFactory.decodeFile(photo.filePath)
                        if (bmp != null) {
                            val stream = java.io.ByteArrayOutputStream()
                            bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, stream)
                            val img = Image.getInstance(stream.toByteArray())
                            img.scaleToFit(140f, 105f)
                            val pc = PdfPCell()
                            pc.border = Rectangle.BOX; pc.borderColor = cBorder
                            pc.paddingTop = 3f; pc.paddingBottom = 3f
                            pc.paddingLeft = 3f; pc.paddingRight = 3f
                            pc.horizontalAlignment = Element.ALIGN_CENTER
                            pc.addElement(img)
                            photoRow.addCell(pc)
                        }
                    } catch (_: Exception) {}
                }
                repeat(photoCols - itemPhotos.size) {
                    photoRow.addCell(PdfPCell().apply { border = Rectangle.NO_BORDER })
                }
                itemCell.addElement(photoRow)
            }

            itemTbl.addCell(itemCell)
            doc.add(itemTbl)
        }
    }

    // ── Certifications / Scope & Limitations ──────────────────────────────────
    private fun pageCertifications(doc: Document, report: Report, settings: AppSettings) {
        doc.newPage()
        addSectionHeader(doc, "📜", "Scope & Limitations")

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
        doc.add(Paragraph(
            "\nInspection performed in accordance with InterNACHI Standards of Practice  |  www.nachi.org",
            Font(Font.FontFamily.HELVETICA, 8f, Font.ITALIC, cGray)
        ).apply { alignment = Element.ALIGN_CENTER })
    }
}

// ── Header / Footer ────────────────────────────────────────────────────────────
private class HeaderFooterEvent(private val report: Report) : PdfPageEventHelper() {
    override fun onEndPage(writer: PdfWriter, document: Document) {
        val cb = writer.directContent
        val footer = Phrase(
            "ProInspect  |  ${report.propertyAddress}  |  Page ${writer.pageNumber}",
            Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, BaseColor(100, 110, 120))
        )
        ColumnText.showTextAligned(
            cb, Element.ALIGN_CENTER, footer,
            (document.left() + document.right()) / 2,
            document.bottom() - 15f, 0f
        )
    }
}
