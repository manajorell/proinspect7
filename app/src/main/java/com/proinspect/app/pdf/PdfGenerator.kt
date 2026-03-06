package com.proinspect.app.pdf

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Environment
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.draw.LineSeparator
import com.proinspect.app.data.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {

    private val cNavy     = BaseColor(26, 39, 68)
    private val cGold     = BaseColor(201, 151, 58)
    private val cRed      = BaseColor(180, 35, 35)
    private val cOrange   = BaseColor(200, 80, 12)
    private val cYellow   = BaseColor(160, 110, 0)
    private val cGreen    = BaseColor(22, 130, 60)
    private val cGray     = BaseColor(100, 110, 120)
    private val cLightBg  = BaseColor(248, 245, 239)
    private val cBorder   = BaseColor(220, 215, 200)
    private val cWhite    = BaseColor.WHITE
    private val cOffWhite = BaseColor(252, 251, 248)

    private val fH3White = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor.WHITE)
    private val fBody    = Font(Font.FontFamily.HELVETICA, 10f, Font.NORMAL, BaseColor(40, 45, 55))
    private val fSmall   = Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL, BaseColor(100, 110, 120))
    private val fSmallB  = Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD, BaseColor(100, 110, 120))
    private val fGold    = Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor(201, 151, 58))
    private val fWhite   = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.WHITE)
    private val fWhiteSm = Font(Font.FontFamily.HELVETICA, 9f, Font.NORMAL, BaseColor.WHITE)
    private val fH3      = Font(Font.FontFamily.HELVETICA, 11f, Font.BOLD, BaseColor(26, 39, 68))

    private fun rColor(r: Rating) = when (r) {
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
        pageExecutiveSummary(doc, report, items, photos)
        pageFullDetails(doc, report, items, photos)
        pageCertifications(doc, report, settings)
        doc.close()
        return file
    }

    private fun pageCover(doc: Document, report: Report, items: List<InspectionItem>, settings: AppSettings) {
        // Header with optional logo
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
        hCell.addElement(Paragraph(report.propertyAddress.ifBlank { "Property Address" },
            Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.WHITE)))
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
                    img.scaleToFit(140f, 100f)
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
        grid.addCell(lbl("CLIENT"));     grid.addCell(value(report.clientName))
        grid.addCell(lbl("DATE"));       grid.addCell(value(report.inspectionDate))
        grid.addCell(lbl("PROPERTY"));   grid.addCell(value(report.propertyAddress))
        grid.addCell(lbl("INSPECTOR"));  grid.addCell(value(report.inspectorName))
        grid.addCell(lbl("YEAR BUILT")); grid.addCell(value(report.yearBuilt))
        grid.addCell(lbl("CERT #"));     grid.addCell(value(report.inspectorCert))
        grid.addCell(lbl("SQ FT"));      grid.addCell(value(report.squareFootage))
        grid.addCell(lbl("COMPANY"));    grid.addCell(value(report.inspectorCompany))
        doc.add(grid)

        doc.add(Chunk(LineSeparator(0.5f, 100f, cBorder, Element.ALIGN_CENTER, -2f)))

        // Rating legend
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
            val cell = PdfPCell()
            cell.border = Rectangle.TOP; cell.borderColorTop = color; cell.borderWidthTop = 3f
            cell.backgroundColor = cOffWhite
            cell.paddingTop = 10f; cell.paddingBottom = 10f
            cell.paddingLeft = 10f; cell.paddingRight = 10f
            cell.horizontalAlignment = Element.ALIGN_CENTER
            cell.addElement(Paragraph(count.toString(),
                Font(Font.FontFamily.HELVETICA, 22f, Font.BOLD, color)).apply { alignment = Element.ALIGN_CENTER })
            cell.addElement(Paragraph(label,
                Font(Font.FontFamily.HELVETICA, 7f, Font.NORMAL, cGray)).apply { alignment = Element.ALIGN_CENTER })
            legend.addCell(cell)
        }
        doc.add(legend)
        
        // Certification badges
        val badgePaths = listOf(
            settings.badge1Path, settings.badge2Path,
            settings.badge3Path, settings.badge4Path
        ).filter { it.isNotBlank() && File(it).exists() }

        if (badgePaths.isNotEmpty()) {
            doc.add(Paragraph(" "))
            doc.add(Chunk(LineSeparator(0.5f, 100f, cBorder, Element.ALIGN_CENTER, -2f)))
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

    private fun pageExecutiveSummary(
        doc: Document, report: Report,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        val titleTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
        val tc = PdfPCell()
        tc.backgroundColor = cNavy; tc.border = Rectangle.NO_BORDER
        tc.paddingTop = 14f; tc.paddingBottom = 14f; tc.paddingLeft = 14f; tc.paddingRight = 14f
        tc.addElement(Paragraph("Executive Summary", fWhite))
        tc.addElement(Paragraph("Items requiring attention, listed by priority", fWhiteSm))
        titleTbl.addCell(tc)
        doc.add(titleTbl)

        val safety  = items.filter { it.rating == Rating.SAFETY }
        val major   = items.filter { it.rating == Rating.MAJOR }
        val monitor = items.filter { it.rating == Rating.MONITOR }

        if (safety.isEmpty() && major.isEmpty() && monitor.isEmpty()) {
            val noIssues = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
            val ni = PdfPCell()
            ni.backgroundColor = BaseColor(240, 253, 244)
            ni.border = Rectangle.BOX; ni.borderColor = cGreen
            ni.paddingTop = 16f; ni.paddingBottom = 16f; ni.paddingLeft = 16f; ni.paddingRight = 16f
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
                Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, cGreen)))
            gh.backgroundColor = BaseColor(240, 253, 244); gh.border = Rectangle.NO_BORDER
            gh.paddingTop = 8f; gh.paddingBottom = 8f; gh.paddingLeft = 8f; gh.paddingRight = 8f
            gHdr.addCell(gh)
            doc.add(gHdr)
            val goodTbl = PdfPTable(3).apply { widthPercentage = 100f; spacingAfter = 16f }
            good.forEach { item ->
                val ci = InspectionSections.allItems().find { it.id == item.itemId }
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
            val ci = InspectionSections.allItems().find { it.id == item.itemId }
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

    private fun pageFullDetails(
        doc: Document, report: Report,
        items: List<InspectionItem>, photos: List<InspectionPhoto>
    ) {
        val titleTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
        val tc = PdfPCell()
        tc.backgroundColor = cNavy; tc.border = Rectangle.NO_BORDER
        tc.paddingTop = 14f; tc.paddingBottom = 14f; tc.paddingLeft = 14f; tc.paddingRight = 14f
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
            val sh = PdfPCell()
            sh.backgroundColor = cNavy; sh.border = Rectangle.NO_BORDER
            sh.paddingTop = 10f; sh.paddingBottom = 10f; sh.paddingLeft = 16f; sh.paddingRight = 16f
            sh.addElement(Paragraph("$icon  ${sectionName.uppercase()}", fH3White))
            sHdr.addCell(sh)
            doc.add(sHdr)
            doc.add(Chunk(LineSeparator(2f, 100f, cGold, Element.ALIGN_CENTER, 0f)))
            doc.add(Paragraph(" "))

            val tbl = PdfPTable(3).apply {
                widthPercentage = 100f; spacingAfter = 10f
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

            sectionItems.forEach { ci ->
                val found  = items.find { it.itemId == ci.id }
                val rating = found?.rating ?: Rating.NOT_RATED
                val color  = rColor(rating)
                val nameCell = PdfPCell(Phrase(ci.title, fBody))
                nameCell.border = Rectangle.BOTTOM; nameCell.borderColorBottom = cBorder
                nameCell.paddingTop = 7f; nameCell.paddingBottom = 7f; nameCell.paddingLeft = 7f; nameCell.paddingRight = 7f
                tbl.addCell(nameCell)
                val rCell = PdfPCell()
                rCell.border = Rectangle.BOTTOM; rCell.borderColorBottom = cBorder
                rCell.paddingTop = 7f; rCell.paddingBottom = 7f; rCell.paddingLeft = 7f; rCell.paddingRight = 7f
                rCell.horizontalAlignment = Element.ALIGN_CENTER
                rCell.backgroundColor = if (rating != Rating.NOT_RATED)
                    BaseColor(color.red, color.green, color.blue, 25) else cWhite
                rCell.addElement(Paragraph(rating.short,
                    Font(Font.FontFamily.HELVETICA, 9f, Font.BOLD, color)).apply { alignment = Element.ALIGN_CENTER })
                tbl.addCell(rCell)
                val narrative = found?.narrative?.ifBlank { null }
                val nCell = PdfPCell(Phrase(narrative ?: "—", if (narrative != null) fBody else fSmall))
                nCell.border = Rectangle.BOTTOM; nCell.borderColorBottom = cBorder
                nCell.paddingTop = 7f; nCell.paddingBottom = 7f; nCell.paddingLeft = 7f; nCell.paddingRight = 7f
                tbl.addCell(nCell)
            }
            doc.add(tbl)

            val narrative = sectionNarratives[section]
            if (!narrative.isNullOrBlank()) {
                val nBox = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 10f }
                val nCell = PdfPCell()
                nCell.border = Rectangle.BOX; nCell.borderColor = cGold
                nCell.backgroundColor = BaseColor(253, 249, 242)
                nCell.paddingTop = 12f; nCell.paddingBottom = 12f; nCell.paddingLeft = 12f; nCell.paddingRight = 12f
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
                            val pc = PdfPCell(img)
                            pc.border = Rectangle.BOX; pc.borderColor = cBorder
                            pc.paddingTop = 3f; pc.paddingBottom = 3f; pc.paddingLeft = 3f; pc.paddingRight = 3f
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
        }
    }

    private fun pageCertifications(doc: Document, report: Report, settings: AppSettings) {
        doc.newPage()

        // Disclaimer section
        val hdrTbl = PdfPTable(1).apply { widthPercentage = 100f; spacingAfter = 16f }
        val h = PdfPCell()
        h.backgroundColor = cNavy; h.border = Rectangle.NO_BORDER
        h.paddingTop = 14f; h.paddingBottom = 14f; h.paddingLeft = 14f; h.paddingRight = 14f
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
}
           
