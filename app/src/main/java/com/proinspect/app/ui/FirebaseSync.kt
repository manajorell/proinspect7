package com.proinspect.app.ui

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.proinspect.app.data.Report
import kotlinx.coroutines.tasks.await

object FirebaseSync {

private val auth by lazy { FirebaseAuth.getInstance() }
private val db by lazy { FirebaseFirestore.getInstance() }

    val currentUser get() = auth.currentUser
    val isSignedIn get() = auth.currentUser != null

    suspend fun signInWithGoogle(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun syncReport(report: Report): Boolean {
        val user = auth.currentUser ?: return false
        return try {
            val data = mapOf(
                "id"                 to report.id,
                "reportNumber"       to report.reportNumber,
                "propertyAddress"    to report.propertyAddress,
                "propertyCity"       to report.propertyCity,
                "clientName"         to report.clientName,
                "clientEmail"        to report.clientEmail,
                "inspectorName"      to report.inspectorName,
                "inspectorCert"      to report.inspectorCert,
                "inspectorCompany"   to report.inspectorCompany,
                "inspectorPhone"     to report.inspectorPhone,
                "inspectionDate"     to report.inspectionDate,
                "inspectionTime"     to report.inspectionTime,
                "weatherConditions"  to report.weatherConditions,
                "yearBuilt"          to report.yearBuilt,
                "squareFootage"      to report.squareFootage,
                "overviewNarrative"  to report.overviewNarrative,
                "limitations"        to report.limitations,
                "roofingNarrative"   to report.roofingNarrative,
                "exteriorNarrative"  to report.exteriorNarrative,
                "structureNarrative" to report.structureNarrative,
                "electricalNarrative" to report.electricalNarrative,
                "hvacNarrative"      to report.hvacNarrative,
                "plumbingNarrative"  to report.plumbingNarrative,
                "interiorNarrative"  to report.interiorNarrative,
                "insulationNarrative" to report.insulationNarrative,
                "garageNarrative"    to report.garageNarrative,
                "inspectionService"  to report.inspectionService,
                "inspectionAmount"   to report.inspectionAmount,
                "ancillaryServices"  to report.ancillaryServices,
                "ancillaryAmount"    to report.ancillaryAmount,
                "paymentStatus"      to report.paymentStatus,
                "paymentMethod"      to report.paymentMethod,
                "paymentNotes"       to report.paymentNotes,
                "propertyType"       to report.propertyType,
                "roofType"           to report.roofType,
                "roofAge"            to report.roofAge,
                "heatType"           to report.heatType,
                "heatBrand"          to report.heatBrand,
                "heatAge"            to report.heatAge,
                "acType"             to report.acType,
                "acBrand"            to report.acBrand,
                "acAge"              to report.acAge,
                "panelBrand"         to report.panelBrand,
                "panelAmps"          to report.panelAmps,
                "whType"             to report.whType,
                "whAge"              to report.whAge,
                "whCapacity"         to report.whCapacity,
                "createdAt"          to report.createdAt,
                "lastModified"       to System.currentTimeMillis()
            )
            db.collection("users")
                .document(user.uid)
                .collection("reports")
                .document(report.id.toString())
                .set(data, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteReport(reportId: Long): Boolean {
        val user = auth.currentUser ?: return false
        return try {
            db.collection("users")
                .document(user.uid)
                .collection("reports")
                .document(reportId.toString())
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun fetchAllReports(): List<Map<String, Any>> {
        val user = auth.currentUser ?: return emptyList()
        return try {
            db.collection("users")
                .document(user.uid)
                .collection("reports")
                .get()
                .await()
                .documents
                .mapNotNull { it.data }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
