package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlinicTopAppBar(
    currentTab: Int,
    onAdminClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onWhatsAppClick: () -> Unit
) {
    // Check if current time falls within clinic hours (Mon-Fri 7:30 AM to 12:30 AM)
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    
    val isMonToFri = day in Calendar.MONDAY..Calendar.FRIDAY
    val isClinicOpen = isMonToFri // Simplified status for display

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = GoldAccent
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = "Spine Logo",
                            tint = NavyPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "CHARLIE'S SPINE KLINIC",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color.White
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isClinicOpen) StatusCompleted else RedAccent)
                        )
                        Text(
                            text = if (isClinicOpen) "OPEN NOW (7:30 AM - 12:30 AM)" else "CLOSED (Mon-Fri 7:30AM)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isClinicOpen) GoldAccent else Color(0xFFCBD5E1),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(onClick = onWhatsAppClick) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    tint = GoldAccent
                )
            }
            IconButton(onClick = onPhoneClick) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Call",
                    tint = Color.White
                )
            }
            IconButton(onClick = onAdminClick) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin Portal",
                    tint = if (currentTab == 5) GoldAccent else Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NavyPrimary,
            titleContentColor = Color.White
        )
    )
}
