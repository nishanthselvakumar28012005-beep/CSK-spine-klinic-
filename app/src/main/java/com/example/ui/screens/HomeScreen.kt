package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    onBookClick: () -> Unit,
    onCallClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onViewTariffClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateBackground),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // --- HERO BANNER ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(NavyPrimary, NavyLight)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = RedAccent,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "SPECIALIST SPINE & PAIN CARE",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            )
                        }

                        Text(
                            text = "Spine & Musculoskeletal Physiotherapy",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                lineHeight = 30.sp
                            )
                        )

                        Text(
                            text = "Personalized physiotherapy care for spine, musculoskeletal and rehabilitation needs in Vellore.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFCBD5E1),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        )

                        Divider(color = Color(0xFF334155), thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onBookClick,
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldAccent,
                                    contentColor = NavyPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = "Book",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "BOOK APPT",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            OutlinedButton(
                                onClick = onCallClick,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(GoldAccent)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("CALL", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // --- DOCTOR & PHYSIOTHERAPIST PROFILE ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(72.dp)
                                .border(2.dp, GoldAccent, CircleShape),
                            shape = CircleShape,
                            color = NavyPrimary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Doctor Profile",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Charles P. Joseph, PT",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )
                            Text(
                                text = "Spine Physio Specialist",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = GoldDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Former Head, Pain Physio Clinic, CMC Vellore",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NavyLight,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Divider(color = SlateBorder)

                    Text(
                        text = "ACADEMIC CREDENTIALS:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary,
                            letterSpacing = 0.5.sp
                        )
                    )

                    val credentials = listOf(
                        "BPT (CMC, Vellore)",
                        "MPT (Hamdard)",
                        "MSc (Psychology)",
                        "CMT (Manual Therapy, MAANIPS)",
                        "Doctorate in Acupuncture (Colombo)",
                        "PGDSM (Sports Medicine)",
                        "CMLD (Norton Institute, USA)"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        credentials.forEach { cred ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusCompleted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = cred,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color(0xFF334155),
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- QUICK TARIFF & SERVICES HIGHLIGHT ---
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Services & Tariff",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary
                        )
                    )
                    TextButton(onClick = onViewTariffClick) {
                        Text("View Full Tariff", color = GoldDark, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TariffQuickCard(
                        modifier = Modifier.weight(1f),
                        title = "Spine Assessment",
                        duration = "15-30 mins",
                        price = "₹1,000 - ₹1,500",
                        icon = Icons.Default.HealthAndSafety,
                        onBookClick = onBookClick
                    )
                    TariffQuickCard(
                        modifier = Modifier.weight(1f),
                        title = "Manual Therapy",
                        duration = "30 mins",
                        price = "₹1,500 - ₹2,500",
                        icon = Icons.Default.PanTool,
                        onBookClick = onBookClick
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TariffQuickCard(
                        modifier = Modifier.weight(1f),
                        title = "Online Consult",
                        duration = "20 mins",
                        price = "₹1,000",
                        icon = Icons.Default.VideoCall,
                        onBookClick = onBookClick
                    )
                    TariffQuickCard(
                        modifier = Modifier.weight(1f),
                        title = "Home Visit",
                        duration = "Doorstep",
                        price = "₹650 - ₹750",
                        icon = Icons.Default.HomeWork,
                        onBookClick = onBookClick
                    )
                }
            }
        }

        // --- CLINIC HOURS CARD ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = NavyPrimary
                        )
                        Text(
                            text = "Clinic Working Hours",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary
                            )
                        )
                    }

                    Divider(color = SlateBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Monday – Friday",
                            fontWeight = FontWeight.SemiBold,
                            color = NavyPrimary
                        )
                        Text(
                            text = "7:30 AM – 12:30 AM",
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Saturday & Sunday",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "Closed",
                            fontWeight = FontWeight.Bold,
                            color = RedAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TariffQuickCard(
    modifier: Modifier = Modifier,
    title: String,
    duration: String,
    price: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onBookClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NavyPrimary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary,
                    fontSize = 13.sp
                )
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
            )
            Text(
                text = price,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldDark,
                    fontSize = 14.sp
                )
            )
        }
    }
}
