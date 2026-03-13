package com.proinspect.app.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.AreaBreakType
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import com.itextpdf.layout.properties.HorizontalAlignment
import com.proinspect.app.data.*
import java.io.ByteArrayOutputStream
import java.io.File

object PdfGenerator {

    // Color definitions
    private val navyColor = DeviceRgb(26, 35, 126)
    private val goldColor = DeviceRgb(212, 175, 55)
    private val redColor = DeviceRgb(220, 38, 38)
    private val orangeColor = DeviceRgb(249, 115, 22)
    private val yellowColor = DeviceRgb(234, 179, 8)
    private val greenColor = DeviceRgb(34, 197, 94)
    private val grayColor = DeviceRgb(107, 114, 128)
    private val lightGrayColor = DeviceRgb(229, 231, 235)

    fun generate(
        context: Context,
        report: Report,
        items: List<InspectionItem>,
        photos: List<InspectionPhoto>,
        settings: AppSettings
    ): File {
        val pdfFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "inspection_report_${System.currentTimeMillis()}.pdf"
        )

        val writer = PdfWriter(pdfFile)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        // Add cover page
        addCoverPage(document, report, settings)

        // Add clickable rating legend summary
        addClickableRatingLegend(document, items)

        // Add detailed findings sections (destinations for clicks)
        addDetailedFindings(document, items)

        // Add property information
        addPropertyInfo(document, report)

        // Add each section with full details
        val sections = listOf(
            "roofing", "exterior", "structure", "electrical", 
            "hvac", "plumbing", "interior", "insulation", "garage"
        )

        sections.forEach { section ->
            val sectionItems = items.filter { it.section == section }
            if (sectionItems.isNotEmpty()) {
                addSection(document, section, sectionItems, photos, report)
            }
        }

        // Add signature page if signed agreement exists
        if (report.signedAgreementPath.isNotBlank()) {
            addSignaturePage(document, report)
        }

        document.close()
        return pdfFile
    }

    private fun addCoverPage(document: Document, report: Report, settings: AppSettings) {
        // Add company logo if exists
        if (settings.companyLogoPath.isNotBlank()) {
            try {
                val logoFile = File(settings.companyLogoPath)
                if (logoFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(logoFile.absolutePath)
                    val scaledBitmap = scaleBitmap(bitmap, 200, 100)
                    val stream = ByteArrayOutputStream()
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    val imageData = ImageDataFactory.create(stream.toByteArray())
                    val logo = Image(imageData)
                        .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    document.add(logo)
                }
            } catch (e: Exception) {
                // Skip logo if error
            }
        }

        document.add(Paragraph("\n\n"))

        // Title
        document.add(
            Paragraph("HOME INSPECTION REPORT")
                .setFontSize(28f)
                .setBold()
                .setFontColor(navyColor)
                .setTextAlignment(TextAlignment.CENTER)
        )

        document.add(Paragraph("\n"))

        // Property address
        document.add(
            Paragraph(report.propertyAddress.ifBlank { "Property Address Not Provided" })
                .setFontSize(16f)
                .setFontColor(grayColor)
                .setTextAlignment(TextAlignment.CENTER)
        )

        document.add(
            Paragraph(report.propertyCity.ifBlank { "" })
                .setFontSize(14f)
                .setFontColor(grayColor)
                .setTextAlignment(TextAlignment.CENTER)
        )

        document.add(Paragraph("\n\n"))

        // Inspection details
        val detailsTable = Table(2)
            .setWidth(UnitValue.createPercentValue(70f))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)

        detailsTable.addCell(createDetailCell("Inspection Date:", report.inspectionDate))
        detailsTable.addCell(createDetailCell("Inspector:", report.inspectorName))
        detailsTable.addCell(createDetailCell("Client:", report.clientName))
        detailsTable.addCell(createDetailCell("Cert #:", report.inspectorCert))

        document.add(detailsTable)

        document.add(Paragraph("\n\n"))

        // Add badges if they exist
        addBadges(document, settings)

        // Page break
        document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
    }

    private fun addClickableRatingLegend(document: Document, items: List<InspectionItem>) {
        document.add(
            Paragraph("INSPECTION SUMMARY")
                .setFontSize(24f)
                .setBold()
                .setFontColor(navyColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20f)
        )

        document.add(
            Paragraph("Rating Legend (Click to view details)")
                .setFontSize(14f)
                .setFontColor(grayColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15f)
        )

        // Count items by rating
        val safetyCount = items.count { it.rating == Rating.SAFETY }
        val majorCount = items.count { it.rating == Rating.MAJOR }
        val monitorCount = items.count { it.rating == Rating.MONITOR }
        val goodCount = items.count { it.rating == Rating.GOOD }

        // Create clickable rating legend table
        val legendTable = Table(4)
            .setWidth(UnitValue.createPercentValue(90f))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
            .setMarginBottom(30f)

        // Safety Concern
        legendTable.addCell(createClickableRatingCell(
            "🚨",
            "SAFETY",
            safetyCount,
            redColor,
            "findings_safety"
        ))

        // Major Concern
        legendTable.addCell(createClickableRatingCell(
            "⚠️",
            "MAJOR",
            majorCount,
            orangeColor,
            "findings_major"
        ))

        // Monitor
        legendTable.addCell(createClickableRatingCell(
            "👁️",
            "MONITOR",
            monitorCount,
            yellowColor,
            "findings_monitor"
        ))

        // Good
        legendTable.addCell(createClickableRatingCell(
            "✓",
            "GOOD",
            goodCount,
            greenColor,
            "findings_good"
        ))

        document.add(legendTable)

        // Add note
        document.add(
            Paragraph("Click on any rating above to jump to detailed findings.")
                .setFontSize(10f)
                .setFontColor(grayColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic()
                .setMarginBottom(20f)
        )

        // Page break
        document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
    }

    private fun createClickableRatingCell(
        icon: String,
        label: String,
        count: Int,
        color: DeviceRgb,
        destination: String
    ): Cell {
        val cell = Cell()
            .setBorder(SolidBorder(lightGrayColor, 2f))
            .setPadding(15f)
            .setBackgroundColor(ColorConstants.WHITE)
            .setTextAlignment(TextAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)

        // Create clickable link
        val link = Link(
            "$icon\n$label\n$count",
            PdfAction.createGoTo(destination)
        )
            .setFontColor(color)
            .setUnderline()
            .setBold()

        val para = Paragraph()
            .add(Paragraph(icon).setFontSize(24f))
            .add(Paragraph(label).setFontSize(12f).setBold())
            .add(Paragraph(count.toString()).setFontSize(20f).setBold())
            .setTextAlignment(TextAlignment.CENTER)

        // Make entire cell content clickable
        val clickablePara = Paragraph()
            .add(link)
            .setFontSize(14f)

        cell.add(clickablePara)
        return cell
    }

    private fun addDetailedFindings(document: Document, items: List<InspectionItem>) {
        document.add(
            Paragraph("DETAILED FINDINGS")
                .setFontSize(24f)
                .setBold()
                .setFontColor(navyColor)
                .setMarginBottom(20f)
        )

        // Safety Findings
        addFindingsByRating(document, items, Rating.SAFETY, "🚨 SAFETY CONCERNS", redColor, "findings_safety")

        // Major Findings
        addFindingsByRating(document, items, Rating.MAJOR, "⚠️ MAJOR CONCERNS", orangeColor, "findings_major")

        // Monitor Findings
        addFindingsByRating(document, items, Rating.MONITOR, "👁️ ITEMS TO MONITOR", yellowColor, "findings_monitor")

        // Good Findings
        addFindingsByRating(document, items, Rating.GOOD, "✓ ITEMS IN GOOD CONDITION", greenColor, "findings_good")

        // Page break
        document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
    }

    private fun addFindingsByRating(
        document: Document,
        items: List<InspectionItem>,
        rating: Rating,
        title: String,
        color: DeviceRgb,
        destination: String
    ) {
        // Create destination anchor
        val anchor = Paragraph()
            .setDestination(destination)
            .setHeight(0f)
        document.add(anchor)

        val filteredItems = items.filter { it.rating == rating }

        if (filteredItems.isEmpty()) {
            document.add(
                Paragraph(title)
                    .setFontSize(18f)
                    .setBold()
                    .setFontColor(color)
                    .setMarginTop(15f)
                    .setMarginBottom(10f)
            )
            document.add(
                Paragraph("No items in this category.")
                    .setFontSize(11f)
                    .setFontColor(grayColor)
                    .setItalic()
                    .setMarginBottom(20f)
            )
            return
        }

        document.add(
            Paragraph(title)
                .setFontSize(18f)
                .setBold()
                .setFontColor(color)
                .setMarginTop(15f)
                .setMarginBottom(10f)
        )

        filteredItems.forEach { item ->
            val checklistItem = InspectionSections.allItems.find { it.id == item.itemId }
            val sectionName = item.section.replaceFirstChar { it.uppercase() }

            // Item card
            val itemTable = Table(1)
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(10f)

            val cell = Cell()
                .setBorder(SolidBorder(color, 2f))
                .setPadding(10f)
                .setBackgroundColor(ColorConstants.WHITE)

            // Section and title
            cell.add(
                Paragraph("$sectionName")
                    .setFontSize(9f)
                    .setFontColor(grayColor)
                    .setBold()
                    .setMarginBottom(3f)
            )

            cell.add(
                Paragraph(checklistItem?.title ?: item.itemId)
                    .setFontSize(12f)
                    .setBold()
                    .setFontColor(navyColor)
                    .setMarginBottom(5f)
            )

            // Narrative
            if (item.narrative.isNotBlank()) {
                cell.add(
                    Paragraph(item.narrative)
                        .setFontSize(10f)
                        .setFontColor(grayColor)
                        .setMarginBottom(5f)
                )
            }

            // Rating badge
            cell.add(
                Paragraph(rating.short)
                    .setFontSize(9f)
                    .setBold()
                    .setFontColor(ColorConstants.WHITE)
                    .setBackgroundColor(color)
                    .setPadding(3f)
                    .setWidth(UnitValue.createPointValue(60f))
            )

            itemTable.addCell(cell)
            document.add(itemTable)
        }

        document.add(Paragraph("\n"))
    }

    private fun addPropertyInfo(document: Document, report: Report) {
        document.add(
            Paragraph("PROPERTY INFORMATION")
                .setFontSize(20f)
                .setBold()
                .setFontColor(navyColor)
                .setMarginBottom(15f)
        )

        val infoTable = Table(2)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(20f)

        infoTable.addCell(createInfoCell("Property Address:", report.propertyAddress))
        infoTable.addCell(createInfoCell("City, State, ZIP:", report.propertyCity))
        infoTable.addCell(createInfoCell("Year Built:", report.yearBuilt))
        infoTable.addCell(createInfoCell("Square Footage:", report.squareFootage))
        infoTable.addCell(createInfoCell("Weather Conditions:", report.weatherConditions))
        infoTable.addCell(createInfoCell("Inspection Date:", report.inspectionDate))

        document.add(infoTable)

        // Client info
        document.add(
            Paragraph("CLIENT & INSPECTOR")
                .setFontSize(16f)
                .setBold()
                .setFontColor(navyColor)
                .setMarginTop(15f)
                .setMarginBottom(10f)
        )

        val clientTable = Table(2)
            .setWidth(UnitValue.createPercentValue(100f))
            .setMarginBottom(20f)

        clientTable.addCell(createInfoCell("Client Name:", report.clientName))
        clientTable.addCell(createInfoCell("Client Email:", report.clientEmail))
        clientTable.addCell(createInfoCell("Inspector Name:", report.inspectorName))
        clientTable.addCell(createInfoCell("Inspector Cert #:", report.inspectorCert))
        clientTable.addCell(createInfoCell("Company:", report.inspectorCompany))
        clientTable.addCell(createInfoCell("Phone:", report.inspectorPhone))

        document.add(clientTable)

        // Overview narrative
        if (report.overviewNarrative.isNotBlank()) {
            document.add(
                Paragraph("Property Overview")
                    .setFontSize(14f)
                    .setBold()
                    .setFontColor(navyColor)
                    .setMarginTop(15f)
                    .setMarginBottom(8f)
            )
            document.add(
                Paragraph(report.overviewNarrative)
                    .setFontSize(11f)
                    .setMarginBottom(15f)
            )
        }

        // Limitations
        if (report.limitations.isNotBlank()) {
            document.add(
                Paragraph("Access Limitations")
                    .setFontSize(14f)
                    .setBold()
                    .setFontColor(navyColor)
                    .setMarginTop(15f)
                    .setMarginBottom(8f)
            )
            document.add(
                Paragraph(report.limitations)
                    .setFontSize(11f)
                    .setMarginBottom(15f)
            )
        }

        // Page break
        document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
    }

    private fun addSection(
        document: Document,
        section: String,
        items: List<InspectionItem>,
        photos: List<InspectionPhoto>,
        report: Report
    ) {
        val sectionName = InspectionSections.sectionNames[section] 
            ?: section.replaceFirstChar { it.uppercase() }

        document.add(
            Paragraph(sectionName.uppercase())
                .setFontSize(22f)
                .setBold()
                .setFontColor(navyColor)
                .setMarginBottom(15f)
        )

        // Add section overview photos
        val sectionPhotos = photos.filter { it.section == section && it.itemId == null }
        if (sectionPhotos.isNotEmpty()) {
            document.add(
                Paragraph("Section Overview Photos")
                    .setFontSize(12f)
                    .setBold()
                    .setMarginBottom(8f)
            )
            addPhotosToDocument(document, sectionPhotos)
        }

        // Add items
        items.forEach { item ->
            addInspectionItem(document, item, photos)
        }

        // Add section narrative
        val narrative = when (section) {
            "roofing" -> report.roofingNarrative
            "exterior" -> report.exteriorNarrative
            "structure" -> report.structureNarrative
            "electrical" -> report.electricalNarrative
            "hvac" -> report.hvacNarrative
            "plumbing" -> report.plumbingNarrative
            "interior" -> report.interiorNarrative
            "insulation" -> report.insulationNarrative
            "garage" -> report.garageNarrative
            else -> ""
        }

        if (narrative.isNotBlank()) {
            document.add(
                Paragraph("Overall $sectionName Notes")
                    .setFontSize(14f)
                    .setBold()
                    .setFontColor(navyColor)
                    .setMarginTop(15f)
                    .setMarginBottom(8f)
            )
            document.add(
                Paragraph(narrative)
                    .setFontSize(11f)
                    .setMarginBottom(20f)
            )
        }

        // Page break
        document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
    }

    private fun addInspectionItem(
        document: Document,
        item: InspectionItem,
        photos: List<InspectionPhoto>
    ) {
        val checklistItem = InspectionSections.allItems.find { it.id == item.itemId }
        
        if (checklistItem == null) return

        // Item title
        document.add(
            Paragraph(checklistItem.title)
                .setFontSize(13f)
                .setBold()
                .setMarginTop(10f)
                .setMarginBottom(5f)
        )

        // Rating
        val ratingColor = when (item.rating) {
            Rating.SAFETY -> redColor
            Rating.MAJOR -> orangeColor
            Rating.MONITOR -> yellowColor
            Rating.GOOD -> greenColor
            else -> grayColor
        }

        document.add(
            Paragraph("Rating: ${item.rating.short}")
                .setFontSize(10f)
                .setBold()
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(ratingColor)
                .setPadding(4f)
                .setWidth(UnitValue.createPointValue(100f))
                .setMarginBottom(8f)
        )

        // Narrative
        if (item.narrative.isNotBlank()) {
            document.add(
                Paragraph(item.narrative)
                    .setFontSize(10f)
                    .setMarginBottom(8f)
            )
        }

        // Photos for this item
        val itemPhotos = photos.filter { it.itemId == item.itemId }
        if (itemPhotos.isNotEmpty()) {
            addPhotosToDocument(document, itemPhotos)
        }

        document.add(Paragraph("\n"))
    }

    private fun addPhotosToDocument(document: Document, photos: List<InspectionPhoto>) {
        photos.chunked(2).forEach { photoRow ->
            val photoTable = Table(photoRow.size)
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(10f)

            photoRow.forEach { photo ->
                try {
                    val photoFile = File(photo.filePath)
                    if (photoFile.exists()) {
                        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                        val scaledBitmap = scaleBitmap(bitmap, 250, 250)
                        val stream = ByteArrayOutputStream()
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
                        val imageData = ImageDataFactory.create(stream.toByteArray())
                        val image = Image(imageData)
                            .setWidth(UnitValue.createPercentValue(100f))
                            .setHeight(UnitValue.createPointValue(200f))

                        val cell = Cell()
                            .add(image)
                            .setPadding(5f)
                        photoTable.addCell(cell)
                    }
                } catch (e: Exception) {
                    // Skip photo if error
                }
            }

            document.add(photoTable)
        }
    }

    private fun addBadges(document: Document, settings: AppSettings) {
        val badgePaths = listOf(
            settings.badge1Path,
            settings.badge2Path,
            settings.badge3Path,
            settings.badge4Path
        ).filter { it.isNotBlank() }

        if (badgePaths.isEmpty()) return

        val badgeTable = Table(badgePaths.size.coerceAtMost(4))
            .setWidth(UnitValue.createPercentValue(60f))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)

        badgePaths.forEach { path ->
            try {
                val badgeFile = File(path)
                if (badgeFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(badgeFile.absolutePath)
                    val scaledBitmap = scaleBitmap(bitmap, 80, 80)
                    val stream = ByteArrayOutputStream()
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                    val imageData = ImageDataFactory.create(stream.toByteArray())
                    val badge = Image(imageData)
                        .setWidth(60f)
                        .setHeight(60f)

                    val cell = Cell()
                        .add(badge)
                        .setBorder(null)
                        .setPadding(5f)
                    badgeTable.addCell(cell)
                }
            } catch (e: Exception) {
                // Skip badge if error
            }
        }

        document.add(badgeTable)
    }

    private fun addSignaturePage(document: Document, report: Report) {
        document.add(
            Paragraph("SIGNED AGREEMENT")
                .setFontSize(20f)
                .setBold()
                .setFontColor(navyColor)
                .setMarginBottom(15f)
        )

        document.add(
            Paragraph("A signed pre-inspection agreement is on file for this inspection.")
                .setFontSize(11f)
                .setMarginBottom(20f)
        )
    }

    private fun createDetailCell(label: String, value: String): Cell {
        val cell = Cell()
            .setBorder(null)
            .setPadding(5f)

        cell.add(
            Paragraph(label)
                .setFontSize(11f)
                .setBold()
                .setFontColor(grayColor)
        )
        cell.add(
            Paragraph(value.ifBlank { "—" })
                .setFontSize(12f)
        )

        return cell
    }

    private fun createInfoCell(label: String, value: String): Cell {
        val cell = Cell()
            .setBorder(SolidBorder(lightGrayColor, 1f))
            .setPadding(8f)

        cell.add(
            Paragraph(label)
                .setFontSize(10f)
                .setBold()
                .setFontColor(grayColor)
                .setMarginBottom(3f)
        )
        cell.add(
            Paragraph(value.ifBlank { "—" })
                .setFontSize(11f)
        )

        return cell
    }

    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight

        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }
}
