package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object NotificationManager {

    const val CLINIC_PHONE = "+919082249505"
    const val CLINIC_WHATSAPP_NUMBER = "919082249505"
    const val CLINIC_MAP_ADDRESS = "Jehovah Jireh House, Auxilium College Road, Gandhinagar, Vellore, Tamil Nadu 632006"

    fun openWhatsAppBookingMessage(context: Context, appointmentId: String, serviceName: String, date: String, slot: String, name: String) {
        val text = "Hello Dr. Charles P. Joseph,\nI have booked an appointment at Charlie's Spine Klinic.\n\n" +
                "📌 *Booking ID:* $appointmentId\n" +
                "👤 *Patient Name:* $name\n" +
                "🩺 *Service:* $serviceName\n" +
                "📅 *Date & Time:* $date at $slot\n\n" +
                "Please confirm my booking."
        val encodedText = Uri.encode(text)
        val url = "https://wa.me/$CLINIC_WHATSAPP_NUMBER?text=$encodedText"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun openWhatsAppDirect(context: Context) {
        val url = "https://wa.me/$CLINIC_WHATSAPP_NUMBER?text=" + Uri.encode("Hello Dr. Charles P. Joseph, I would like to inquire about Spine Physiotherapy consultation.")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun makePhoneCall(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$CLINIC_PHONE"))
        context.startActivity(intent)
    }

    fun openGoogleMapsLocation(context: Context) {
        val gmmIntentUri = Uri.parse("geo:12.9602,79.1417?q=" + Uri.encode(CLINIC_MAP_ADDRESS))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val webMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=" + Uri.encode(CLINIC_MAP_ADDRESS)))
            context.startActivity(webMapIntent)
        }
    }
}
